import org.junit.Test;
import static org.junit.Assert.*;

public class ProcessCommonJSModulesTest {

  @Test
  public void testGuessCJSModuleName() {
    ProcessCommonJSModules module = new ProcessCommonJSModules(null, ".");
    assertEquals("module$a", module.guessCJSModuleName("./a.js"));
  }
  
  @Test
  public void testToModuleName() {
    String moduleName = ProcessCommonJSModules.toModuleName("./a.js");
    assertEquals("module$a", moduleName);
  }
  
  @Test
  public void testToModuleName_RelativeAddressing() {
    String moduleName = ProcessCommonJSModules.toModuleName("./a.js", "b.js");
    assertEquals("module$b$a", moduleName);
  }
  
  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    compiler.addExternalReference("goog", "goog.require");
    compiler.addExternalReference("goog", "goog.provide");
    ProcessCommonJSModules module = new ProcessCommonJSModules(compiler, ".");
    module.process(null, null);
  }
  
  @Test
  public void testVisitRequireCall() {
    NodeTraversal t = new NodeTraversal(null);
    Node require = new Node(new Node(Token.NAME, "require"),
                            new Node(Token.STRING, "module$a"));
    Node parent = new Node(new Node(Token.NAME, "parent"), require);
    ProcessCommonJSModules module = new ProcessCommonJSModules(null, ".");
    module.visitRequireCall(t, require, parent);
    assertEquals("module$a", require.getFirstChild().getString());
  }
  
  @Test
  public void testVisitScript() {
    NodeTraversal t = new NodeTraversal(null);
    Node script = new Node(Token.SCRIPT);
    ProcessCommonJSModules module = new ProcessCommonJSModules(null, ".");
    module.visitScript(t, script);
    assertTrue(script.getFirstChild().isName());
    assertTrue(script.getFirstChild().isVar());
    assertEquals("module$a", script.getFirstChild().getFirstChild().getString());
  }
  
  @Test
  public void testEmitOptionalModuleExportsOverride() {
    Node script = new Node(Token.SCRIPT);
    ProcessCommonJSModules module = new ProcessCommonJSModules(null, ".");
    module.emitOptionalModuleExportsOverride(script, "module$a");
    assertEquals(1, script.getChildCount());
    Node ifNode = script.getFirstChild();
    assertTrue(ifNode.isIf());
    assertEquals("module$a.module$exports", 
                 ifNode.getFirstChild().getFirstChild().getFirstChild().getString());
  }
  
  @Test
  public void testVisitModuleExports() {
    Node prop = new Node(new Node(Token.NAME, "module"),
                         new Node(Token.NAME, "exports"));
    ProcessCommonJSModules module = new ProcessCommonJSModules(null, ".");
    module.visitModuleExports(prop);
    assertEquals("module$a", prop.getFirstChild().getString());
    assertEquals("module$exports", prop.getLastChild().getString());
  }
  
  @Test
  public void testSuffixVarsCallback() {
    NodeTraversal t = new NodeTraversal(null);
    Node n = new Node(Token.NAME, "a");
    Node parent = new Node(new Node(Token.NAME, "parent"), n);
    ParentScope parentScope = new ParentScope(null);
    Scope.Var var = new Scope.Var(null, null, "a", parentScope);
    parentScope.addVar(var);
    Scope scope = new Scope(null, parentScope);
    n.setScope(scope);
    ProcessCommonJSModules.SuffixVarsCallback callback = 
        new ProcessCommonJSModules.SuffixVarsCallback("b");
    callback.visit(t, n, parent);
    assertEquals("a$$b", n.getString());
  }
  
}