import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.IR;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;

public class ProcessCommonJSModulesTest {

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    CompilerOptions options = new CompilerOptions();
    CompilerInput input = new CompilerInput(new SourceFile("test.js", new File("test.js")), options);
    AbstractCompiler compiler = new AbstractCompiler(input, new CompilerResults());
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");
    processor.process(input.getExterns(), input.getRootNode());

    JSModule module = processor.getModule();
    assertNotNull(module);
    assertEquals("test$", module.getName());

    // Test toModuleName method
    String filename = "src/main/input/test.js";
    String moduleName = ProcessCommonJSModules.toModuleName(filename);
    assertEquals("src/main/input/test$", moduleName);

    filename = "test.js";
    moduleName = ProcessCommonJSModules.toModuleName(filename, "src/main/input/test.js");
    assertEquals("test$", moduleName);

    filename = "./test.js";
    moduleName = ProcessCommonJSModules.toModuleName(filename, "src/main/input/test.js");
    URI uri = new URI("src", null, "main", null, "input", null, "#test.js");
    moduleName = ProcessCommonJSModules.toModuleName(filename, "src/main/input/test.js");
    assertEquals("test$", moduleName);

    // Test visitRequireCall method
    Node requireNode = IR.call(IR.getprop(IR.name("require"), IR.string("module1$")));
    Node moduleNameNode = IR.string("module1$");
    Node moduleRefNode = IR.name("module1$");
    Node parentNode = IR.block();
    Node originalRequireNode = requireNode.cloneTree();
    parentNode.addChildToFront(requireNode);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = new ProcessCommonJSModulesTest.ProcessCommonJSModulesCallback();
    traverse(compiler, parentNode, callback);

    Node actualModuleRefNode = parentNode.getChildAtIndex(0);
    assertEquals(moduleRefNode.toString(), actualModuleRefNode.toString());

    // Test visitScript method
    SourceFile testSourceFile = new SourceFile("test.js", new File("test.js"));
    Node testScriptNode = testSourceFile.getNodes()[0];
    JSModule testModule = new JSModule("test$");
    testModule.addAndOverrideModule(input);
    ProcessCommonJSModules processor2 = new ProcessCommonJSModules(compiler, "", false);
    processor2.process(input.getExterns(), testScriptNode);

    Node actualProvideNode = testScriptNode.getLastChild();
    Node expectedProvideNode = IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
        IR.string("test$"));
    assertEquals(expectedProvideNode.toString(), actualProvideNode.toString());

    Node actualModuleExportsNode = testScriptNode.getChildAtIndex(1);
    Node expectedModuleExportsNode = IR.getprop(IR.name("test$"), IR.string("module$exports"));
    assertEquals(expectedModuleExportsNode.toString(), actualModuleExportsNode.toString());

    Node actualModuleExportsVarNode = testScriptNode.getChildAtIndex(2);
    Node expectedModuleExportsVarNode = IR.var(IR.name("test$"), IR.objectlit());
    assertEquals(expectedModuleExportsVarNode.toString(), actualModuleExportsVarNode.toString());

    // Test suffixVarsCallback method
    String suffix = "testSuffix";
    ProcessCommonJSModulesTest.SuffixVarsCallback suffixVarsCallback = new ProcessCommonJSModulesTest.SuffixVarsCallback(suffix);
    Node testSuffixNode = IR.name("testSuffix");
    Node originalTestSuffixNode = testSuffixNode.cloneTree();
    Node parentNode2 = IR.block();
    Node testScriptNode2 = IR.scriptNode();
    parentNode2.addChildToFront(testScriptNode2);
    parentNode2.addChildToFront(testSuffixNode);

    traverse(compiler, parentNode2, suffixVarsCallback);

    Node actualTestSuffixNode = parentNode2.getChildAtIndex(0);
    assertEquals(testSuffixNode.toString() + "$$" + suffix, actualTestSuffixNode.toString());
  }

  private static class ProcessCommonJSModulesCallback extends AbstractPostOrderCallback {

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isCall() && n.getChildCount() == 2 &&
          "require".equals(n.getFirstChild().getQualifiedName()) &&
          n.getChildAtIndex(1).isString()) {
        visitRequireCall(t, n, parent);
      }
    }

    private void visitRequireCall(NodeTraversal t, Node require, Node parent) {
      String moduleName = ProcessCommonJSModules.toModuleName(require.getChildAtIndex(1).getString(),
          normalizeSourceName(t.getSourceName()));
      Node moduleRef = IR.name(moduleName).srcref(require);
      parent.replaceChild(require, moduleRef);
      Node script = getCurrentScriptNode(parent);
      assertNotNull(script);
      // Rewrite require("name").
      script.addChildToFront(IR.exprResult(
          IR.call(IR.getprop(IR.name("goog"), IR.string("require")),
              IR.string(moduleName))).copyInformationFromForTree(require));
      t.getInput().addRequire(moduleName);
      compiler.reportCodeChange();
    }

    private int scriptNodeCount = 0;

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isScript()) {
        scriptNodeCount++;
        visitScript(t, n);
      }
    }

    private void visitScript(NodeTraversal t, Node script) {
      if (scriptNodeCount != 1) {
        fail("ProcessCommonJSModules supports only one invocation per " +
            "CompilerInput / script node");
      }
      String moduleName = ProcessCommonJSModules.guessCJSModuleName(script.getSourceFileName());
      script.addChildToFront(IR.var(IR.name(moduleName), IR.objectlit())
          .copyInformationFromForTree(script));
      if (reportDependencies) {
        CompilerInput ci = t.getInput();
        ci.addProvide(moduleName);
        JSModule m = new JSModule(moduleName);
        m.addAndOverrideModule(ci);
        processor.setModule(m);
      }
      script.addChildToFront(IR.exprResult(
          IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
              IR.string(moduleName))).copyInformationFromForTree(script));

      emitOptionalModuleExportsOverride(script, moduleName);

      // Rename vars to not conflict in global scope.
      traverse(compiler, script, new SuffixVarsCallback("testSuffix"));

      compiler.reportCodeChange();
    }

    private void emitOptionalModuleExportsOverride(Node script, String moduleName) {
      Node moduleExportsProp = IR.getprop(IR.name(moduleName), IR.string("module$exports"));
      script.addChildToBack(IR.ifNode(
          moduleExportsProp,
          IR.block(IR.exprResult(IR.assign(IR.name(moduleName),
              moduleExportsProp.cloneTree()))))).copyInformationFromForTree(script);
    }

    private Node getCurrentScriptNode(Node n) {
      while (true) {
        if (n.isScript()) {
          return n;
        }
        n = n.getParent();
      }
    }
  }

  private static String normalizeSourceName(String filename) {
    if (filename.indexOf(processor.getFilenamePrefix()) == 0) {
      filename = filename.substring(processor.getFilenamePrefix().length());
    }
    return filename;
  }
}