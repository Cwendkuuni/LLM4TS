import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourceFile;

import java.util.Map;

public class ScopeTest {

  private static Scope createScope(Node rootNode) {
    SourceFile input = new SourceFile("test.js", 1, 1);
    Compiler compiler = new Compiler(input, new ErrorReporter());
    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler);
    Node root = rootNode;
    NodeTraversal traversal = new NodeTraversal(root, abstractCompiler);
    return new Scope(root, abstractCompiler);
  }

  @org.junit.Test
  public void testCreateGlobalScope() {
    // Create a global scope
    Scope globalScope = createScope(Node.BLOCK);

    // Check that it has no parent and a this type
    assertNull(globalScope.getParent());
    assertEquals(ObjectType.OBJECT, globalScope.getTypeOfThis());

    // Create local variables
    Var a = globalScope.declare("a", Node.NAME("a"), NumberType.INT, null);
    Var b = globalScope.declare("b", Node.NAME("b"), StringType.STRING, null);

    // Check that they are accessible in the global scope
    assertEquals(a, globalScope.getVar("a"));
    assertEquals(b, globalScope.getVar("b"));

    // Check that their types are the ones declared
    assertEquals(NumberType.INT, a.getType());
    assertEquals(StringType.STRING, b.getType());
  }

  @org.junit.Test
  public void testCreateFunctionScope() {
    // Create a function scope
    Node functionNode = Node.FUNCTION(Node.NAME("f"), ImmutableMap.of(), Node.BLOCK);
    Scope functionScope = createScope(functionNode);

    // Check that it has a parent and a this type
    assertEquals(createScope(Node.BLOCK), functionScope.getParent());
    assertEquals(functionScope.getTypeOfThis(), FunctionType.FUNCTION_TYPE_WITH_THIS_TYPE);

    // Create local variables
    Var x = functionScope.declare("x", Node.NAME("x"), NumberType.INT, null);
    Var y = functionScope.declare("y", Node.NAME("y"), StringType.STRING, null);

    // Check that they are accessible in the function scope
    assertEquals(x, functionScope.getVar("x"));
    assertEquals(y, functionScope.getVar("y"));

    // Check that they are not accessible in the global scope
    assertNull(createScope(Node.BLOCK).getVar("x"));
    assertNull(createScope(Node.BLOCK).getVar("y"));

    // Check that their types are the ones declared
    assertEquals(NumberType.INT, x.getType());
    assertEquals(StringType.STRING, y.getType());
  }

  @org.junit.Test
  public void testCreateChainOfScopes() {
    // Create a chain of scopes
    Node outerFunctionNode = Node.FUNCTION(Node.NAME("outer"), ImmutableMap.of(), Node.BLOCK);
    Node innerFunctionNode = Node.FUNCTION(Node.NAME("inner"), ImmutableMap.of(), Node.BLOCK);
    Node rootNode = Node.BLOCK;
    rootNode.addChild(outerFunctionNode);
    outerFunctionNode.addChild(innerFunctionNode);

    Scope rootScope = createScope(rootNode);
    Scope outerScope = createScope(outerFunctionNode);
    Scope innerScope = createScope(innerFunctionNode);

    // Check that the scopes are nested correctly
    assertEquals(rootScope, innerScope.getParent());
    assertEquals(outerScope, innerScope.getParent());
    assertEquals(rootScope, outerScope.getParent());

    // Create local variables
    Var a = rootScope.declare("a", Node.NAME("a"), NumberType.INT, null);
    Var b = outerScope.declare("b", Node.NAME("b"), StringType.STRING, null);
    Var c = innerScope.declare("c", Node.NAME("c"), BooleanType.BOOLEAN, null);

    // Check that they are accessible in their respective scopes
    assertEquals(a, rootScope.getVar("a"));
    assertEquals(b, outerScope.getVar("b"));
    assertEquals(c, innerScope.getVar("c"));

    // Check that they are not accessible in other scopes
    assertNull(createScope(Node.BLOCK).getVar("a"));
    assertNull(createScope(Node.BLOCK).getVar("b"));
    assertNull(createScope(Node.BLOCK).getVar("c"));

    // Check that their types are the ones declared
    assertEquals(NumberType.INT, a.getType());
    assertEquals(StringType.STRING, b.getType());
    assertEquals(BooleanType.BOOLEAN, c.getType());
  }

  @org.junit.Test
  public void testDeclareVariableWithSameNameInDifferentScopes() {
    // Create a chain of scopes
    Node outerFunctionNode = Node.FUNCTION(Node.NAME("outer"), ImmutableMap.of(), Node.BLOCK);
    Node innerFunctionNode = Node.FUNCTION(Node.NAME("inner"), ImmutableMap.of(), Node.BLOCK);
    Node rootNode = Node.BLOCK;
    rootNode.addChild(outerFunctionNode);
    outerFunctionNode.addChild(innerFunctionNode);

    Scope rootScope = createScope(rootNode);
    Scope outerScope = createScope(outerFunctionNode);
    Scope innerScope = createScope(innerFunctionNode);

    // Declare a variable with the same name in different scopes
    Var outerA = outerScope.declare("a", Node.NAME("a"), NumberType.INT, null);
    Var innerA = innerScope.declare("a", Node.NAME("a"), StringType.STRING, null);

    // Check that they are not the same variable
    assertNotEquals(outerA, innerA);

    // Check that they are accessible in their respective scopes
    assertEquals(outerA, outerScope.getVar("a"));
    assertEquals(innerA, innerScope.getVar("a"));

    // Check that they are not accessible in other scopes
    assertNull(createScope(Node.BLOCK).getVar("a"));

    // Check that their types are the ones declared
    assertEquals(NumberType.INT, outerA.getType());
    assertEquals(StringType.STRING, innerA.getType());
  }

  @org.junit.Test
  public void testLocalVariableShadowsGlobalVariable() {
    // Create a global variable
    Var globalA = createScope(Node.BLOCK).declare("a", Node.NAME("a"), NumberType.INT, null);

    // Create a local variable with the same name
    Node functionNode = Node.FUNCTION(Node.NAME("f"), ImmutableMap.of(), Node.BLOCK);
    Scope functionScope = createScope(functionNode);
    Var localA = functionScope.declare("a", Node.NAME("a"), StringType.STRING, null);

    // Check that the local variable shadows the global one
    assertEquals(localA, functionScope.getVar("a"));
    assertNull(createScope(Node.BLOCK).getVar("a"));
  }

  @org.junit.Test
  public void testGetVarWithNullName() {
    Scope scope = createScope(Node.BLOCK);
    assertNull(scope.getVar(null));
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testGetVarWithEmptyName() {
    Scope scope = createScope(Node.BLOCK);
    scope.getVar("");
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testGetVarWithInvalidName() {
    Scope scope = createScope(Node.BLOCK);
    scope.getVar(" ");
  }
}