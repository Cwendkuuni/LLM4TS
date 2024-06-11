import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;
  private ProcessCommonJSModules processor;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);
    nodeFactory = compiler.getFactory();
    processor = new ProcessCommonJSModules(compiler, "");
  }

  @Test
  public void testProcessMethod() {
    String input = "function main() {" +
        "require('module1');" +
        "console.log('Hello, World!');" +
        "}";

    Node root = nodeFactory.parseTree(input);

    ProcessCommonJSModulesCallback callback = new ProcessCommonJSModulesCallback();

    try {
      traverse(compiler, root, processor);
      traverse(compiler, root, callback);

      Node mainFunction = root.getFirstChild();
      assertEquals("function main() {" +
          "goog.require('module1');" +
          "goog.provide('main');" +
          "module1.module$exports = undefined;" +
          "console.log('Hello, World!');" +
          "}", mainFunction.toString());

      Node requireCall = mainFunction.getChildAtIndex(1);
      assertEquals("require", requireCall.getFirstChild().getQualifiedName());
      assertEquals("module1", requireCall.getChildAtIndex(1).getString());

    } catch (IOException e) {
      fail("IOException in test: " + e.getMessage());
    }
  }

  @Test
  public void testGuessCJSModuleName() {
    assertEquals("module$main", ProcessCommonJSModules.toModuleName("main.js"));
    assertEquals("module$submodule.module$subsubmodule",
        ProcessCommonJSModules.toModuleName("submodule/subsubmodule.js"));
    assertEquals("module$submodule", ProcessCommonJSModules.toModuleName("submodule.js"));
  }

  @Test
  public void testNormalizeSourceName() {
    assertEquals("submodule.js", ProcessCommonJSModules.normalizeSourceName("submodule.js"));
    assertEquals("submodule/subsubmodule.js", ProcessCommonJSModules.normalizeSourceName("submodule/subsubmodule.js"));
    assertEquals("submodule", ProcessCommonJSModules.normalizeSourceName("submodule"));
  }

  @Test(expected = URISyntaxException.class)
  public void testToModuleNameWithInvalidURI() {
    ProcessCommonJSModules.toModuleName("invalid:uri");
  }

  @Test
  public void testToModuleNameWithRelativePath() {
    String currentFilename = "main.js";
    String requiredFilename = "./submodule/subsubmodule.js";
    assertEquals("module$subsubmodule", ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename));
  }

  @Test
  public void testVisitRequireCall() {
    String input = "function main() {" +
        "require('module1');" +
        "console.log('Hello, World!');" +
        "}";

    Node root = nodeFactory.parseTree(input);

    ProcessCommonJSModulesCallback callback = new ProcessCommonJSModulesCallback();

    try {
      traverse(compiler, root, processor);
      traverse(compiler, root, callback);

      Node requireCall = root.getFirstChild().getChildAtIndex(1);
      Node requireFunctionCall = requireCall.getChildAtIndex(0);
      assertEquals("goog.require", requireFunctionCall.getQualifiedName());
      assertEquals("module1", requireCall.getChildAtIndex(1).getString());

    } catch (IOException e) {
      fail("IOException in test: " + e.getMessage());
    }
  }

  @Test
  public void testVisitScript() {
    String input = "var module = {" +
        "exports: function() {" +
        "return this;" +
        "}," +
        "module: {" +
        "exports: 'module.exports'" +
        "}};" +
        "function main() {" +
        "module.exports = 'Hello, World!'";

    Node root = nodeFactory.parseTree(input);

    ProcessCommonJSModulesCallback callback = new ProcessCommonJSModulesCallback();

    try {
      traverse(compiler, root, processor);
      traverse(compiler, root, callback);

      Node moduleVar = root.getLastChild();
      assertEquals("module$$module", moduleVar.getName());
      assertEquals("function main() {" +
          "goog.provide('main');" +
          "module$$module.module$exports = undefined;" +
          "module$$module.exports = 'Hello, World!'" +
          "}", root.toString());

    } catch (IOException e) {
      fail("IOException in test: " + e.getMessage());
    }
  }

  @Test
  public void testVisitModuleExports() {
    String input = "var module = {" +
        "exports: 1," +
        "module: {" +
        "exports: 'module.exports'" +
        "}};" +
        "function main() {" +
        "module.exports = 'Hello, World!'";

    Node root = nodeFactory.parseTree(input);

    ProcessCommonJSModulesCallback callback = new ProcessCommonJSModulesCallback();

    try {
      traverse(compiler, root, processor);
      traverse(compiler, root, callback);

      Node moduleExportsProp = root.getLastChild().getChildAtIndex(0);
      Node module = moduleExportsProp.getParent();
      assertEquals("module$$module", module.getName());
      assertEquals("exports", moduleExportsProp.getQualifiedName());
      assertEquals("1", moduleExportsProp.getChildAtIndex(0).toString());
      assertEquals("module$exports", moduleExportsProp.getChildAtIndex(1).getName());

    } catch (IOException e) {
      fail("IOException in test: " + e.getMessage());
    }
  }

  @Test
  public void testRenameGlobalVariables() {
    String input = "var globalVar = 'Hello, World!'";

    Node root = nodeFactory.parseTree(input);

    ProcessCommonJSModulesCallback callback = new ProcessCommonJSModulesCallback();

    try {
      traverse(compiler, root, processor);
      traverse(compiler, root, callback);

      Node globalVar = root.getFirstChild();
      assertEquals("globalVar$$globalVar", globalVar.getName());

    } catch (IOException e) {
      fail("IOException in test: " + e.getMessage());
    }
  }

  @Test
  public void testEmitOptionalModuleExportsOverride() {
    String input = "var module = {" +
        "exports: 1," +
        "module: {" +
        "exports: 'module.exports'" +
        "}};" +
        "function main() {" +
        "module.exports = 'Hello, World!'";

    Node root = nodeFactory.parseTree(input);

    ProcessCommonJSModulesCallback callback = new ProcessCommonJSModulesCallback();

    try {
      traverse(compiler, root, processor);
      traverse(compiler, root, callback);

      Node script = root.getLastChild();
      Node optionalModuleExportsOverride = script.getLastChild();
      assertEquals("if (module$module$exports) {" +
          "module = module$module$exports;" +
          "}", optionalModuleExportsOverride.toString());

    } catch (IOException e) {
      fail("IOException in test: " + e.getMessage());
    }
  }

  public static class ProcessCommonJSModulesCallback extends
      AbstractPostOrderCallback {

    private int scriptNodeCount = 0;

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      // Test logic goes here
    }

  }

}