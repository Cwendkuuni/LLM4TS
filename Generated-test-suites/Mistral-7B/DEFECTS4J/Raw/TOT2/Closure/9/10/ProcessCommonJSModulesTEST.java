import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.NodeFactory.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.ProcessCommonJSModules.JSModule;

public class ProcessCommonJSModulesTest {
  public void testProcessCommonJSModules() {
    // Given
    AbstractCompiler compiler = new Compiler();
    String filenamePrefix = "test";
    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, filenamePrefix);
    CompilerInput ci = new CompilerInput("test.js", "test.js");

    // When
    Node root = new ObjectLiteral();
    p.process(ci, root);

    // Then
    JSModule module = p.getModule();
    assertNotNull(module);
    assertEquals("test", module.getName());

    // Test for basic functionality
    Node requireCall = new Call("require", array("module1"));
    Node script = new Script();
    script.addChildToFront(requireCall);
    Traversable rootTraversable = traverse(compiler, root, new RewriteRequireCallback());
    Node newScript = (Script) rootTraversable.getChildAtIndex(0);
    Node newRequireCall = newScript.getChildAtIndex(0);
    assertEquals("module1", newRequireCall.getFirstChild().getString());
    assertEquals("module$module1", newRequireCall.getChildAtIndex(1).getString());

    // Test for edge cases and exception handling
    try {
      new URI("test:test.js");
      fail("Expected URISyntaxException");
    } catch (URISyntaxException e) {
      // Expected
    }
    String filenameWithDosPath = "test\\test.js";
    String normalizedFileName = p.normalizeSourceName(filenameWithDosPath);
    assertEquals("test/test.js", normalizedFileName);

    // Test for required filename with relative path
    String currentFileName = "test/main.js";
    String requiredFileName = "./module1.js";
    String moduleName = p.toModuleName(requiredFileName, currentFileName);
    assertEquals("module$module1", moduleName);

    // Test for required filename with absolute path
    currentFileName = "test/main.js";
    requiredFileName = "/test/module1.js";
    moduleName = p.toModuleName(requiredFileName, currentFileName);
    assertEquals("module$test/module1", moduleName);

    // Test for script node with require statement
    Node requireStatement = newCall("require", array("module1"));
    Node scriptNode = newScript();
    scriptNode.addChildToFront(requireStatement);
    rootTraversable = traverse(compiler, root, new ProcessCommonJsModulesCallback());
    newScript = (Script) rootTraversable.getChildAtIndex(0);
    Node newRequireStatement = newScript.getChildAtIndex(0);
    assertEquals("module1", newRequireStatement.getFirstChild().getString());
    assertEquals("module$module1", newRequireStatement.getChildAtIndex(1).getString());

    // Test for module.exports
    Node moduleExports = newObjectLiteral();
    moduleExports.setProperty("foo", newString("bar"));
    Node moduleExportsAssignment = newAssign(
        newGet("module$module1", "module$exports"), moduleExports);
    Node moduleExportsStatement = newExpressionStatement(moduleExportsAssignment);
    Node module = newObjectLiteral();
    module.setProperty("module$exports", moduleExports);
    Node moduleStatement = newExpressionStatement(
        newAssign(newGet(newName("module$module1"), "module$exports"), module));
    Node scriptWithModuleExports = newScript();
    scriptWithModuleExports.addChildToFront(moduleExportsStatement);
    scriptWithModuleExports.addChildToBack(moduleStatement);
    rootTraversable = traverse(compiler, root, new ProcessCommonJsModulesCallback());
    newScript = (Script) rootTraversable.getChildAtIndex(0);
    Node newModuleExports = newGet(newName("module$module1"), "module$exports");
    Node newModule = newGet(newName("module$module1"), "module$module1");
    assertEquals(moduleExports, newModuleExports.cloneTree());
    assertEquals(module, newModule.cloneTree());

    // Test for module.exports override
    String moduleNameWithExports = "moduleWithExports";
    Node moduleWithExports = newObjectLiteral();
    moduleWithExports.setProperty("module$exports", newObjectLiteral());
    Node moduleWithExportsStatement = newExpressionStatement(
        newAssign(newGet(newName(moduleNameWithExports), "module$exports"), moduleWithExports));
    Node scriptWithModuleExportsOverride = newScript();
    scriptWithModuleExportsOverride.addChildToFront(moduleWithExportsStatement);
    rootTraversable = traverse(compiler, root, new ProcessCommonJsModulesCallback());
    newScript = (Script) rootTraversable.getChildAtIndex(0);
    Node newModuleExportsOverride = newGet(newName(moduleNameWithExports), "module$exports");
    Node newModuleOverride = newGet(newName(moduleNameWithExports), moduleNameWithExports);
    assertEquals(moduleWithExports, newModuleExportsOverride.cloneTree());
    assertEquals(newName(moduleNameWithExports), newModuleOverride.cloneTree());
  }

  static class RewriteRequireCallback extends AbstractPostOrderCallback {
    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isCall() && n.getChildCount() == 2 &&
          "require".equals(n.getFirstChild().getQualifiedName()) &&
          n.getChildAtIndex(1).isString()) {
        Node requireCall = n;
        Node moduleRef = IR.name("module$" + toModuleName(n.getChildAtIndex(1).getString()))
            .copyInformationFromForTree(requireCall);
        parent.replaceChild(requireCall, moduleRef);
      }
    }
  }
}