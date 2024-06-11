import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.ErrorReporter;

public class ScopeTest {

  private static final AbstractCompiler compiler = new AbstractCompiler() {
    @Override
    public ErrorReporter getErrorReporter() {
      return new NoOpErrorReporter();
    }
  };

  @org.junit.Test
  public void testGlobalScope() {
    // Create a global scope
    Scope scope = new Scope(null, null);

    // Declare a global variable
    Var globalVar = scope.declare("globalVar", null, IntType.INSTANCE, null);

    // Check that the variable is declared
    assertTrue(scope.isDeclared("globalVar", false));

    // Check that the variable has the global type
    assertEquals(GlobalObjectType.INSTANCE, globalVar.type);

    // Check that the variable is a global variable
    assertTrue(globalVar.isGlobal());
  }

  @org.junit.Test
  public void testLocalScope() {
    // Create a local scope
    Node rootNode = createFunctionNode("function foo() {}", null);
    Scope scope = new Scope(null, rootNode);

    // Declare a local variable
    Var localVar = scope.declare("localVar", createNameNode("localVar"), IntType.INSTANCE, null);

    // Check that the variable is declared
    assertTrue(scope.isDeclared("localVar", false));

    // Check that the variable has the local type
    assertEquals(ObjectType.VOID, localVar.type);

    // Check that the variable is a local variable
    assertTrue(localVar.isLocal());
  }

  @org.junit.Test
  public void testFunctionScope() {
    // Create a function scope
    Node rootNode = createFunctionNode("function foo(x) {}", null);
    Scope scope = new Scope(null, rootNode);

    // Declare a local variable
    Var localVar = scope.declare("localVar", createNameNode("localVar"), IntType.INSTANCE, null);

    // Declare a parameter
    Var param = scope.getVar("x");

    // Check that the local variable is declared
    assertTrue(scope.isDeclared("localVar", false));

    // Check that the local variable has the correct type
    assertEquals(IntType.INSTANCE, localVar.type);

    // Check that the parameter has the correct type
    assertEquals(IntType.INSTANCE, param.type);

    // Check that the local variable shadows the parameter
    Var parameterInParentScope = scope.getParent().getVar("x");
    assertNull(parameterInParentScope);
  }

  @org.junit.Test
  public void testVariableWithInitialValue() {
    // Create a local scope
    Node rootNode = createFunctionNode("function foo() { var x = 1; }", null);
    Scope scope = new Scope(null, rootNode);

    // Declare a local variable with initial value
    Var localVar = scope.declare("x", createNameNode("x"), IntType.INSTANCE, null);

    // Check that the variable is declared
    assertTrue(scope.isDeclared("x", false));

    // Check that the variable has the correct initial value
    assertEquals(1, localVar.getIntValue());

    // Check that the variable type is not inferred
    assertFalse(localVar.isTypeInferred());
  }

  @org.junit.Test
  public void testVariableWithNoInitialValue() {
    // Create a local scope
    Node rootNode = createFunctionNode("function foo() { var x; }", null);
    Scope scope = new Scope(null, rootNode);

    // Declare a local variable without initial value
    Var localVar = scope.declare("x", createNameNode("x"), null, null);

    // Check that the variable is declared
    assertTrue(scope.isDeclared("x", false));

    // Check that the variable type is inferred
    assertTrue(localVar.isTypeInferred());
  }

  @org.junit.Test
  public void testVariableWithExternValue() {
    // Create a local scope
    CompilerInput input = new CompilerInput("externs.js", "/** @extern */\nfunction bar() {}");
    Node rootNode = createFunctionNode("function foo() { var x = bar; }", input);
    Scope scope = new Scope(null, rootNode);

    // Declare a local variable with extern value
    Var localVar = scope.declare("x", createNameNode("x"), FunctionType.INSTANCE, input);

    // Check that the variable is declared
    assertTrue(scope.isDeclared("x", false));

    // Check that the variable type is inferred as FunctionType
    assertEquals(FunctionType.INSTANCE, localVar.type);
  }

  @org.junit.Test
  public void testVariableWithJSDocInfo() {
    // Create a local scope
    Node rootNode = createFunctionNode("/**\n * @function foo\n */\nfunction foo() {}", null);
    Scope scope = new Scope(null, rootNode);

    // Declare a local variable with JSDocInfo
    Var localVar = scope.declare("x", createNameNode("x"), IntType.INSTANCE, null);

    // Check that the variable has JSDocInfo
    JSDocInfo docInfo = localVar.getJSDocInfo();
    assertTrue(docInfo != null);
    assertEquals("function foo", docInfo.getDescriptionText());
  }

  @org.junit.Test
  public void testVariableWithNoShadow() {
    // Create a local scope
    Node rootNode = createFunctionNode("/** @noShadow */ function foo() {}", null);
    Scope scope = new Scope(null, rootNode);

    // Declare a local variable with JSDocInfo
    Var localVar = scope.declare("x", createNameNode("x"), IntType.INSTANCE, null);

    // Try to declare the same variable in parent scope
    Scope parentScope = new Scope(scope, null);
    Var parentVar = parentScope.declare("x", createNameNode("x"), IntType.INSTANCE, null);

    // Check that the parent variable is not in the shadow of the local variable
    assertFalse(parentVar.isNoShadow());
  }

  @org.junit.Test
  public void testVariableWithTypeInference() {
    // Create a local scope
    Node rootNode = createFunctionNode("function foo(x) { var y = x + 1; }", null);
    Scope scope = new Scope(null, rootNode);

    // Declare a local variable with inferred type
    Var localVar = scope.declare("y", createNameNode("y"), null, null);

    // Check that the variable type is inferred as NumberType
    assertEquals(NumberType.INSTANCE, localVar.type);
  }

  @org.junit.Test
  public void testVariableWithTypeAnnotation() {
    // Create a local scope
    Node rootNode = createFunctionNode("function foo(x: number) { var y: string = x.toString(); }", null);
    Scope scope = new Scope(null, rootNode);

    // Declare a local variable with explicit type annotation
    Var localVar = scope.declare("y", createNameNode("y"), StringType.INSTANCE, null);

    // Check that the variable type matches the annotation
    assertEquals(StringType.INSTANCE, localVar.type);
  }

  @org.junit.Test
  public void testVariableWithConst() {
    // Create a local scope
    Node rootNode = createFunctionNode("function foo() { const x = 1; }", null);
    Scope scope = new Scope(null, rootNode);

    // Declare a constant variable
    Var constantVar = scope.declare("x", createNameNode("x"), IntType.INSTANCE, null);

    // Check that the variable is declared
    assertTrue(scope.isDeclared("x", false));

    // Check that the variable type is not inferred
    assertFalse(constantVar.isTypeInferred());

    // Try to assign a new value to the constant variable
    try {
      constantVar.setType(BooleanType.INSTANCE);
      fail("Assignment to a constant variable should not be allowed");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testVariableWithBleedingFunction() {
    // Create a local scope
    Node rootNode = createFunctionNode("function foo(x) { function bar(y) { var z = x; } }", null);
    Scope scope = new Scope(null, rootNode);

    // Declare a bleeding function variable
    Var bleedingFunctionVar = scope.getVar("bar");

    // Check that the bleeding function variable is a function
    assertEquals(FunctionType.INSTANCE, bleedingFunctionVar.type);
  }

  @org.junit.Test
  public void testVariableWithFunctionType() {
    // Create a local scope
    Node rootNode = createFunctionNode("function foo(x) { var y = function(z) {}; }", null);
    Scope scope = new Scope(null, rootNode);

    // Declare a function variable
    Var functionVar = scope.getVar("y");

    // Check that the function variable is a function
    assertEquals(FunctionType.INSTANCE, functionVar.type);
  }

  @org.junit.Test
  public void testVariableWithObjectType() {
    // Create a local scope
    Node rootNode = createFunctionNode("function foo() { var obj = {}; }", null);
    Scope scope = new Scope(null, rootNode);

    // Declare an object variable
    Var objectVar = scope.declare("obj", createNameNode("obj"), ObjectType.INSTANCE, null);

    // Check that the object variable type is ObjectType
    assertEquals(ObjectType.INSTANCE, objectVar.type);
  }

  private Node createFunctionNode(String code, CompilerInput input) {
    Node rootNode = compiler.parse(code, input);
    return rootNode;
  }

  private Node createNameNode(String name) {
    return new Node(Token.IDENT, name, null);
  }
}

class NoOpErrorReporter implements ErrorReporter {
  @Override
  public void error(int lineNumber, int lineOffset, int errorId, String message, Object... args) {
  }

  @Override
  public void warning(int lineNumber, int lineOffset, int errorId, String message, Object... args) {
  }

  @Override
  public void fatalError(String message) {
    throw new RuntimeException(message);
  }
}