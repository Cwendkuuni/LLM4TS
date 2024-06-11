import org.junit.Test;
import static org.junit.Assert.*;

public class VarCheckTest {

  @Test
  public void testUndefinedVariable() {
    VarCheck varCheck = new VarCheck();
    // Create a scope with a variable declaration
    Scope scope = new Scope();
    scope.declare("x", null, null, null);

    // Test accessing an undefined variable
    Node nameNode = new Node(Token.NAME, "y");
    varCheck.visit(new NodeTraversal(), nameNode, null);
    assertTrue(nameNode.isReported(VarCheck.UNDEFINED_VAR_ERROR));
  }

  @Test
  public void testViolatedModuleDependency() {
    VarCheck varCheck = new VarCheck();
    // Create two modules
    JSModule module1 = new JSModule("module1");
    JSModule module2 = new JSModule("module2");
    // Set module dependencies
    module1.addDependency(module2);

    // Create a scope in module1 with a variable declaration
    Scope scope1 = new Scope();
    scope1.declare("x", null, new CompilerInput(module1), null);

    // Create a scope in module2 with a reference to the variable in module1
    Scope scope2 = new Scope();
    scope2.declare("y", null, new CompilerInput(module2), null);

    // Test violated module dependency
    Node nameNode = new Node(Token.NAME, "y");
    varCheck.visit(new NodeTraversal(), nameNode, null);
    assertTrue(nameNode.isReported(VarCheck.VIOLATED_MODULE_DEP_ERROR));
  }

  @Test
  public void testMissingModuleDependency() {
    VarCheck varCheck = new VarCheck();
    // Create two modules
    JSModule module1 = new JSModule("module1");
    JSModule module2 = new JSModule("module2");
    // Set module dependencies
    module1.addDependency(module2);

    // Create a scope in module1 with a variable declaration
    Scope scope1 = new Scope();
    scope1.declare("x", null, new CompilerInput(module1), null);

    // Create a scope in module2 with a reference to an undefined variable in module1
    Scope scope2 = new Scope();
    scope2.declare("y", null, new CompilerInput(module2), null);

    // Test missing module dependency
    Node nameNode = new Node(Token.NAME, "y");
    varCheck.visit(new NodeTraversal(), nameNode, null);
    assertTrue(nameNode.isReported(VarCheck.MISSING_MODULE_DEP_ERROR));
  }

  @Test
  public void testStrictModuleDependency() {
    VarCheck varCheck = new VarCheck();
    // Create two modules
    JSModule module1 = new JSModule("module1");
    JSModule module2 = new JSModule("module2");

    // Create a scope in module1 with a variable declaration
    Scope scope1 = new Scope();
    scope1.declare("x", null, new CompilerInput(module1), null);

    // Create a scope in module2 with a reference to the variable in module1
    Scope scope2 = new Scope();
    scope2.declare("y", null, new CompilerInput(module2), null);

    // Test strict module dependency
    Node nameNode = new Node(Token.NAME, "y");
    varCheck.visit(new NodeTraversal(), nameNode, null);
    assertTrue(nameNode.isReported(VarCheck.STRICT_MODULE_DEP_ERROR));
  }

  @Test
  public void testNameReferenceInExterns() {
    VarCheck varCheck = new VarCheck();
    // Create a scope in the externs input with a variable declaration
    Scope scope = new Scope();
    scope.declare("x", null, new CompilerInput(null, true), null);

    // Test name reference in externs
    Node nameNode = new Node(Token.NAME, "x");
    varCheck.visit(new NodeTraversal(), nameNode, null);
    assertTrue(nameNode.isReported(VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR));
  }

  @Test
  public void testUndefinedExternVariable() {
    VarCheck varCheck = new VarCheck();
    // Create a scope with a variable declaration in the externs input
    Scope scope = new Scope();
    scope.declare("x", null, new CompilerInput(null, true), null);

    // Test accessing an undefined extern variable
    Node nameNode = new Node(Token.NAME, "y");
    varCheck.visit(new NodeTraversal(), nameNode, null);
    assertTrue(nameNode.isReported(VarCheck.UNDEFINED_EXTERN_VAR_ERROR));
  }

  @Test
  public void testInvalidFunctionDeclaration() {
    VarCheck varCheck = new VarCheck();
    // Create a function declaration with an empty name
    Node functionNode = new Node(Token.FUNCTION);
    Node nameNode = new Node(Token.NAME, "");
    functionNode.addChildToBack(nameNode);
    
    // Test invalid function declaration
    varCheck.visit(new NodeTraversal(), nameNode, functionNode);
    assertTrue(nameNode.isReported(VarCheck.INVALID_FUNCTION_DECL));
  }

  @Test
  public void testValidFunctionDeclaration() {
    VarCheck varCheck = new VarCheck();
    // Create a function declaration with a valid name
    Node functionNode = new Node(Token.FUNCTION);
    Node nameNode = new Node(Token.NAME, "foo");
    functionNode.addChildToBack(nameNode);
    
    // Test valid function declaration
    varCheck.visit(new NodeTraversal(), nameNode, functionNode);
    assertFalse(nameNode.isReported(VarCheck.INVALID_FUNCTION_DECL));
  }
}