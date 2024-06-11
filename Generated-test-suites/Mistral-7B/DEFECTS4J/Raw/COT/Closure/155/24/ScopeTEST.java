import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createName;
import static com.google.javascript.rhino.NodeFactory.createNumberLiteral;
import static com.google.javascript.rhino.NodeFactory.createScope;
import static com.google.javascript.rhino.NodeFactory.createThis;
import static com.google.javascript.rhino.NodeFactory.createTypeName;
import static com.google.javascript.rhino.token.TokenType.*;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Compiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Types;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope;
import org.junit.Test;

public class ScopeTest {
  @Test
  public void testScopeConstructorWithParentAndRootNode() {
    Node rootNode = createFunctionDeclaration(createName("test"), createThis(), createBlock());
    Scope parentScope = createScope(createName("parent"), createThis(), null, null);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);

    assertEquals(parentScope, scopeUnderTest.getParent());
    assertEquals(rootNode, scopeUnderTest.getRootNode());
    assertFalse(scopeUnderTest.isBottom());
    assertEquals(1, scopeUnderTest.getDepth());
  }

  @Test
  public void testScopeConstructorWithRootNodeAndThisType() {
    Node rootNode = createThis();
    Scope scopeUnderTest = new Scope(null, rootNode, createObjectType("Object"), null);

    assertEquals(null, scopeUnderTest.getParent());
    assertEquals(rootNode, scopeUnderTest.getRootNode());
    assertTrue(scopeUnderTest.isBottom());
    assertEquals(0, scopeUnderTest.getDepth());
  }

  @Test
  public void testScopeConstructorWithRootNodeAndThisTypeAndBottom() {
    Node rootNode = createThis();
    Scope scopeUnderTest = new Scope(null, rootNode, createObjectType("Object"), null, true);

    assertEquals(null, scopeUnderTest.getParent());
    assertEquals(rootNode, scopeUnderTest.getRootNode());
    assertTrue(scopeUnderTest.isBottom());
    assertEquals(0, scopeUnderTest.getDepth());
  }

  @Test
  public void testGetParent() {
    Scope parentScope = createScope(createName("parent"), createThis(), null, null);
    Scope childScope = new Scope(parentScope, createFunctionDeclaration(createName("test"), createThis(), createBlock()));

    assertEquals(parentScope, childScope.getParent());
  }

  @Test
  public void testGetRootNode() {
    Node rootNode = createFunctionDeclaration(createName("test"), createThis(), createBlock());
    Scope scopeUnderTest = new Scope(null, rootNode);

    assertEquals(rootNode, scopeUnderTest.getRootNode());
  }

  @Test
  public void testIsBottom() {
    Node rootNode = createThis();
    Scope scopeUnderTest1 = new Scope(null, rootNode, createObjectType("Object"), null);
    Scope scopeUnderTest2 = new Scope(null, rootNode, createObjectType("Object"), null, true);

    assertFalse(scopeUnderTest1.isBottom());
    assertTrue(scopeUnderTest2.isBottom());
  }

  @Test
  public void testGetDepth() {
    Scope parentScope = createScope(createName("parent"), createThis(), null, null);
    Scope childScope = new Scope(parentScope, createFunctionDeclaration(createName("test"), createThis(), createBlock()));

    assertEquals(1, childScope.getDepth());
  }

  @Test
  public void testGetGlobalScope() {
    Node rootNode = createBlock();
    Scope globalScope = new Scope(rootNode, null, null, null);
    Scope scopeUnderTest = new Scope(null, createFunctionDeclaration(createName("test"), createThis(), rootNode));

    assertEquals(globalScope, scopeUnderTest.getGlobalScope());
  }

  @Test
  public void testDeclare() {
    Compiler compiler = new Compiler();
    Node rootNode = createFunctionDeclaration(createName("test"), createThis(), createBlock());
    Scope parentScope = createScope(createName("parent"), createThis(), null, null);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);
    Var varUnderTest = scopeUnderTest.declare("variable", createName("variable"), createIntType(), null, false);

    assertEquals("variable", varUnderTest.getName());
    assertEquals(createName("variable"), varUnderTest.getNameNode());
    assertEquals(createIntType(), varUnderTest.getType());
    assertEquals(scopeUnderTest, varUnderTest.getScope());
    assertEquals(0, varUnderTest.getIndex());
    assertNull(varUnderTest.getInput());
    assertFalse(varUnderTest.isDefine());

    Multimap<String, Var> expectedVars = LinkedHashMultimap.create();
    expectedVars.put("variable", varUnderTest);
    assertEquals(expectedVars, scopeUnderTest.getVars());
  }

  @Test
  public void testDeclareWithDefine() {
    Compiler compiler = new Compiler();
    Node rootNode = createFunctionDeclaration(createName("test"), createThis(), createBlock());
    Scope parentScope = createScope(createName("parent"), createThis(), null, null);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);
    Var varUnderTest = scopeUnderTest.declare("variable", createName("variable"), createIntType(), null, null, true);

    assertEquals("variable", varUnderTest.getName());
    assertEquals(createName("variable"), varUnderTest.getNameNode());
    assertEquals(createIntType(), varUnderTest.getType());
    assertEquals(scopeUnderTest, varUnderTest.getScope());
    assertEquals(0, varUnderTest.getIndex());
    assertNull(varUnderTest.getInput());
    assertTrue(varUnderTest.isDefine());
  }

  @Test(expected = IllegalStateException.class)
  public void testDeclareTwice() {
    Compiler compiler = new Compiler();
    Node rootNode = createFunctionDeclaration(createName("test"), createThis(), createBlock());
    Scope parentScope = createScope(createName("parent"), createThis(), null, null);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);
    Var varUnderTest = scopeUnderTest.declare("variable", createName("variable"), createIntType(), null, false);
    scopeUnderTest.declare("variable", createName("variable"), createIntType(), null, false);
  }

  @Test
  public void testUndeclare() {
    Compiler compiler = new Compiler();
    Node rootNode = createFunctionDeclaration(createName("test"), createThis(), createBlock());
    Scope parentScope = createScope(createName("parent"), createThis(), null, null);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);
    Var varUnderTest = scopeUnderTest.declare("variable", createName("variable"), createIntType(), null, false);
    scopeUnderTest.undeclare(varUnderTest);

    assertNull(scopeUnderTest.getSlot("variable"));
  }

  @Test
  public void testGetSlot() {
    Compiler compiler = new Compiler();
    Node rootNode = createFunctionDeclaration(createName("test"), createThis(), createBlock());
    Scope parentScope = createScope(createName("parent"), createThis(), null, null);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);
    Var varUnderTest = scopeUnderTest.declare("variable", createName("variable"), createIntType(), null, false);

    Var result = scopeUnderTest.getSlot("variable");
    assertEquals(varUnderTest, result);
  }

  @Test
  public void testGetOwnSlot() {
    Compiler compiler = new Compiler();
    Node rootNode = createFunctionDeclaration(createName("test"), createThis(), createBlock());
    Scope parentScope = createScope(createName("parent"), createThis(), null, null);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);
    Var varUnderTest = scopeUnderTest.declare("variable", createName("variable"), createIntType(), null, false);

    Var result = scopeUnderTest.getOwnSlot("variable");
    assertEquals(varUnderTest, result);
  }

  @Test
  public void testIsDeclared() {
    Compiler compiler = new Compiler();
    Node rootNode = createFunctionDeclaration(createName("test"), createThis(), createBlock());
    Scope parentScope = createScope(createName("parent"), createThis(), null, null);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);
    Var varUnderTest = scopeUnderTest.declare("variable", createName("variable"), createIntType(), null, false);

    assertTrue(scopeUnderTest.isDeclared("variable", false));
    assertFalse(scopeUnderTest.isDeclared("variable", true));
    assertTrue(parentScope.isDeclared("variable", true));
  }

  @Test
  public void testGetVars() {
    Compiler compiler = new Compiler();
    Node rootNode = createFunctionDeclaration(createName("test"), createThis(), createBlock());
    Scope parentScope = createScope(createName("parent"), createThis(), null, null);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);
    Var var1UnderTest = scopeUnderTest.declare("variable1", createName("variable1"), createIntType(), null, false);
    Var var2UnderTest = scopeUnderTest.declare("variable2", createName("variable2"), createIntType(), null, false);

    Iterator<Var> vars = scopeUnderTest.getVars().iterator();
    Var firstVar = vars.next();
    Var secondVar = vars.next();

    assertEquals(var1UnderTest, firstVar);
    assertEquals(var2UnderTest, secondVar);
  }

  @Test
  public void testGetVarCount() {
    Compiler compiler = new Compiler();
    Node rootNode = createFunctionDeclaration(createName("test"), createThis(), createBlock());
    Scope parentScope = createScope(createName("parent"), createThis(), null, null);
    Scope scopeUnderTest = new Scope(parentScope, rootNode);
    Var var1UnderTest = scopeUnderTest.declare("variable1", createName("variable1"), createIntType(), null, false);
    Var var2UnderTest = scopeUnderTest.declare("variable2", createName("variable2"), createIntType(), null, false);

    assertEquals(2, scopeUnderTest.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    Node rootNode = createBlock();
    Scope globalScope = new Scope(rootNode, null, null, null);
    Scope localScope = new Scope(globalScope, createFunctionDeclaration(createName("test"), createThis(), createBlock()));

    assertTrue(globalScope.isGlobal());
    assertFalse(localScope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    Node rootNode = createBlock();
    Scope localScope = new Scope(null, createFunctionDeclaration(createName("test"), createThis(), createBlock()));
    Scope globalScope = new Scope(localScope.getParent(), rootNode);

    assertFalse(globalScope.isLocal());
    assertTrue(localScope.isLocal());
  }

  private Node createBlock() {
    return createNode(BLOCK, null, null);
  }

  private Node createFunctionDeclaration(Node name, Node thisExpr, Node body) {
    return createNode(FUNCTION_DECLARATION, name, createNode(LIST, new Node[]{thisExpr}, null), body);
  }

  private Node createName(String name) {
    return createNode(NAME, name, null);
  }

  private Node createThis() {
    return createNode(THIS, null, null);
  }

  private Node createIntType() {
    return createTypeName("Number");
  }

  private Node createNode(int type, Node... children) {
    return compiler.makeNode(type, children);
  }

  private Node createObjectType(String name) {
    return Types.createObjectType(name);
  }
}