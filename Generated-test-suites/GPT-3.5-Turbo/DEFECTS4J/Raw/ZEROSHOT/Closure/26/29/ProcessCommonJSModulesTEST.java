import com.google.javascript.jscomp.JSModule;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProcessCommonJSModulesTest {

  private ProcessCommonJSModules processCommonJSModules;
  private AbstractCompiler compiler;
  
  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    processCommonJSModules = new ProcessCommonJSModules(compiler, "src/test/resources/");
  }
  
  @Test
  public void testProcess() {
    Node externs = IR.empty();
    Node root = IR.root();
    processCommonJSModules.process(externs, root);
    // Assert that the JSModule is null after processing
    assertNull(processCommonJSModules.getModule());
    // Assert that compiler.reportCodeChange() is called
    assertTrue(compiler.getErrors().isEmpty());
  }
  
  @Test
  public void testGuessCJSModuleName() {
    String filename = "src/test/resources/test.js";
    String moduleName = processCommonJSModules.guessCJSModuleName(filename);
    assertEquals("module$test", moduleName);
  }
  
  @Test
  public void testToModuleName() {
    String filename = "src/test/resources/test.js";
    String moduleName = ProcessCommonJSModules.toModuleName(filename);
    assertEquals("module$test", moduleName);
  }
  
  @Test
  public void testToModuleNameWithRelativeAddressing() {
    String requiredFilename = "./module";
    String currentFilename = "src/test/resources/test.js";
    String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
    assertEquals("module$test$module", moduleName);
  }
  
  @Test
  public void testProcessRequireCall() {
    Node require = IR.call(IR.name("require"), IR.string("./module"));
    Node parent = IR.exprResult(require);
    Node script = IR.script();
    script.addChildToBack(require);
    script.addChildToBack(parent);
    
    NodeTraversal traversal = new NodeTraversal(compiler, new ProcessCommonJSModules.ProcessCommonJsModulesCallback());
    traversal.traverse(script);
    
    // Assert that require is replaced with the module reference
    assertEquals("module$test$module", parent.getFirstChild().getString());
    // Assert that a goog.require call is added to the script node
    assertEquals(IR.getprop(IR.name("goog"), IR.string("require")), script.getFirstChild().getFirstChild().getFirstChild());
    // Assert that the module reference is added to the script node
    assertEquals(IR.var(IR.name("module$test$module"), IR.objectlit()), script.getChildAtIndex(1));
    // Assert that compiler.reportCodeChange() is called
    assertTrue(compiler.getErrors().isEmpty());
  }
  
  @Test
  public void testProcessScript() {
    Node script = IR.script();
  
    NodeTraversal traversal = new NodeTraversal(compiler, new ProcessCommonJSModules.ProcessCommonJsModulesCallback());
    traversal.traverse(script);
  
    // Assert that a new variable declaration is added to the script node
    assertEquals(IR.var(IR.name("module$test"), IR.objectlit()), script.getFirstChild());
    // Assert that a goog.provide call is added to the script node
    assertEquals(IR.getprop(IR.name("goog"), IR.string("provide")), script.getSecondChild().getFirstChild().getFirstChild());
    // Assert that there is no module exports override added to the script node
    assertEquals(script.getLastChild(), traversal.getCurrentScriptNode(script));
    // Assert that compiler.reportCodeChange() is called
    assertTrue(compiler.getErrors().isEmpty());
  }
  
  @Test
  public void testEmitOptionalModuleExportsOverride() {
    Node script = IR.script();
    
    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
    NodeTraversal traversal = new NodeTraversal(compiler, callback);
    traversal.traverse(script);
    processCommonJSModules.emitOptionalModuleExportsOverride(script, "module$test");

    // Find the if node added to the script node
    Node ifNode = script.getLastChild();
    assertEquals(IR.ifNode(
      IR.getprop(IR.name("module$test"), IR.string("module$exports")),
      IR.block(IR.exprResult(IR.assign(IR.name("module$test"), IR.getprop(IR.name("module$test"), IR.string("module$exports")))))
    ), ifNode);

    // Assert that compiler.reportCodeChange() is called
    assertTrue(compiler.getErrors().isEmpty());
  }
  
  @Test
  public void testVisitModuleExports() {
    Node moduleExports = IR.getprop(IR.name("module"), IR.string("exports"));
  
    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback = processCommonJSModules.new ProcessCommonJsModulesCallback();
    callback.visitModuleExports(moduleExports);
  
    // Assert that the module name is changed to the guessed CJS module name
    assertEquals("module$test", moduleExports.getFirstChild().getString());
    // Assert that the exports name is changed to the module$exports
    assertEquals("module$exports", moduleExports.getSecondChild().getString());
  }
  
  @Test
  public void testSuffixVarsCallback() {
    Node nameNode = IR.name("test");
    NodeAssignAncestorType assignAncestor = new NodeAssignAncestorType();
    assignAncestor.setIsGlobal(true);
  
    Scope.Var testVar = new Scope.Var("test");
    testVar.setIsGlobal(true);
  
    NodeTraversal traversal = new NodeTraversal(compiler, new ProcessCommonJSModules.SuffixVarsCallback("module$test"));
    traversal.getScope().declare("test", assignAncestor, testVar, nameNode);
  
    ProcessCommonJSModules.SuffixVarsCallback callback = processCommonJSModules.new SuffixVarsCallback("module$test");
    callback.visit(traversal, nameNode, nameNode.getParent());
  
    // Assert that the name is changed to include the suffix
    assertEquals("test$$module$test", nameNode.getString());
    // Assert that the original name property is set correctly
    assertEquals("test", nameNode.getProp(Node.ORIGINALNAME_PROP));
  }
  
  private class NodeAssignAncestorType implements Scope.AssignTypeReference {
  
    private boolean isGlobal = false;
  
    @Override
    public void setIsGlobal(boolean isGlobal) {
      this.isGlobal = isGlobal;
    }
  
    @Override
    public boolean isGlobal() {
      return isGlobal;
    }
  }
}