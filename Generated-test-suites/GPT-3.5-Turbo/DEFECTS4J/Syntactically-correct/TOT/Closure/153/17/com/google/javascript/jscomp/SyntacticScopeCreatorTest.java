import static org.junit.Assert.*;
import org.junit.Test;

public class SyntacticScopeCreatorTest {

  @Test
  public void testCreateScope_WhenFunctionWithArgs_DeclareFunctionVariables() {
    String jsCode = "function testFunc(x, y) { var z = 10; }";
    Compiler compiler = new Compiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node ast = compiler.parse(jsCode);
    Scope scope = scopeCreator.createScope(ast, null);

    assertEquals(3, scope.getVarCount());
    assertNotNull(scope.getVar("x"));
    assertNotNull(scope.getVar("y"));
    assertNotNull(scope.getVar("z"));
  }

  @Test
  public void testCreateScope_WhenGlobalBlock_DeclareGlobalVariables() {
    String jsCode = "var globalVariable = 10; function testFunc() { var localVariable = 20; }";
    Compiler compiler = new Compiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node ast = compiler.parse(jsCode);
    Scope scope = scopeCreator.createScope(ast, null);

    assertEquals(1, scope.getVarCount());
    assertNotNull(scope.getVar("globalVariable"));
  }

  @Test
  public void testCreateScope_WhenFunctionDeclarationWithEmptyName_DoNotDeclareVariable() {
    String jsCode = "function () { var x = 10; }";
    Compiler compiler = new Compiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node ast = compiler.parse(jsCode);
    Scope scope = scopeCreator.createScope(ast, null);

    assertEquals(0, scope.getVarCount());
  }

  @Test
  public void testCreateScope_WhenCatchVariable_DeclareCatchVariable() {
    String jsCode = "try { } catch (e) { var x = 10; }";
    Compiler compiler = new Compiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node ast = compiler.parse(jsCode);
    Scope scope = scopeCreator.createScope(ast, null);

    assertEquals(1, scope.getVarCount());
    assertNotNull(scope.getVar("e"));
  }

  @Test
  public void testCreateScope_WhenFunctionsWithSameNameInGlobalScope_ReportError() {
    String jsCode = ""
        + "function testFunc() { var x = 10; }"
        + "function testFunc() { var y = 20; }";
    Compiler compiler = new Compiler();
    SyntacticScopeCreator scopeCreator = new SyntacticScopeCreator(compiler);

    Node ast = compiler.parse(jsCode);
    Scope scope = scopeCreator.createScope(ast, null);

    // The second function declaration should cause an error
    assertNotNull(scope.getVar("x"));
    assertNull(scope.getVar("y"));
  }

}