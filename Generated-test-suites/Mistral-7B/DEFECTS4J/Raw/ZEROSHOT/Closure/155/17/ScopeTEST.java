import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.Var.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.FunctionType;
import com.google.javascript.jscomp.Scope.ObjectType;
import com.google.javascript.jscomp.Scope.StaticScope;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.Iterator;
import java.util.Map;

public class ScopeTest {

  private Node rootNode;
  private NodeTraversal traversal;
  private ErrorReporter errorReporter;
  private CompilerInput input;
  private Scope scope;

  @org.junit.Before
  public void setUp() {
    rootNode = Node.newProgram(-1, null);
    traversal = new NodeTraversal(rootNode, null);
    errorReporter = new ErrorReporter() {};
    input = new CompilerInput("test.js", "");
  }

  @org.junit.Test
  public void testCreateScope() {
    Scope globalScope = new Scope(rootNode, errorReporter, input);
    assertNotNull(globalScope);
    assertEquals(0, globalScope.getDepth());
    assertTrue(globalScope.isGlobal());
    assertNull(globalScope.getParent());
    assertNull(globalScope.thisType);
  }

  @org.junit.Test
  public void testCreateFunctionScope() {
    Node functionNode = Node.newFunction(1, null, null, null, null);
    Scope functionScope = new Scope(functionNode, errorReporter, input, new ObjectType(JSType.NULL));
    assertNotNull(functionScope);
    assertEquals(1, functionScope.getDepth());
    assertFalse(functionScope.isGlobal());
    assertNotNull(functionScope.getParent());
    assertNotNull(functionScope.thisType);
  }

  @org.junit.Test
  public void testDeclareVariable() {
    scope = new Scope(rootNode, errorReporter, input);

    // declare a variable
    String name = "x";
    Node nameNode = Node.newName(name);
    JSType type = JSType.INT;
    Var variable = scope.declare(name, nameNode, type, input);
    assertEquals(name, variable.getName());
    assertEquals(nameNode, variable.getNameNode());
    assertNotNull(variable.getType());
    assertEquals(type, variable.getType());
    assertEquals(scope, variable.getScope());
    assertEquals(0, variable.getIndex());
    assertFalse(variable.isDefine());
    assertNull(variable.getJSDocInfo());
    assertFalse(variable.isTypeInferred());
    assertNull(variable.getInputName());
    assertFalse(variable.isNoShadow());
  }

  @org.junit.Test
  public void testDeclareVariableWithDocInfo() {
    scope = new Scope(rootNode, errorReporter, input);
    String name = "x";
    Node nameNode = Node.newName(name);
    JSType type = JSType.INT;
    JSDocInfo docInfo = new JSDocInfo();
    docInfo.setNoShadow(true);
    Var variable = scope.declare(name, nameNode, type, input, null, docInfo);
    assertEquals(name, variable.getName());
    assertEquals(nameNode, variable.getNameNode());
    assertNotNull(variable.getType());
    assertEquals(type, variable.getType());
    assertEquals(scope, variable.getScope());
    assertEquals(0, variable.getIndex());
    assertTrue(variable.isDefine());
    assertEquals(docInfo, variable.getJSDocInfo());
    assertFalse(variable.isTypeInferred());
    assertNull(variable.getInputName());
    assertTrue(variable.isNoShadow());
  }

  @org.junit.Test
  public void testDeclareVariableWithInput() {
    scope = new Scope(rootNode, errorReporter, input);
    String name = "x";
    Node nameNode = Node.newName(name);
    JSType type = JSType.INT;
    Var variable = scope.declare(name, nameNode, type, null);
    assertEquals(name, variable.getName());
    assertEquals(nameNode, variable.getNameNode());
    assertNotNull(variable.getType());
    assertEquals(type, variable.getType());
    assertEquals(scope, variable.getScope());
    assertEquals(0, variable.getIndex());
    assertFalse(variable.isDefine());
    assertNull(variable.getJSDocInfo());
    assertFalse(variable.isTypeInferred());
    assertEquals(input, variable.getInput());
    assertFalse(variable.isNoShadow());
  }

  @org.junit.Test
  public void testDeclareVariableWithInferredType() {
    scope = new Scope(rootNode, errorReporter, input);
    String name = "x";
    Node nameNode = Node.newName(name);
    Var variable = scope.declare(name, nameNode, null, input, true);
    assertEquals(name, variable.getName());
    assertEquals(nameNode, variable.getNameNode());
    assertNull(variable.getType());
    assertFalse(variable.isTypeInferred());
    assertTrue(variable.isDefine());
    assertNull(variable.getJSDocInfo());
    assertNull(variable.getInputName());
    assertFalse(variable.isNoShadow());
  }

  @org.junit.Test
  public void testGetSlot() {
    String name = "x";
    TestVariable testVariable = new TestVariable();
    scope = new Scope(rootNode, errorReporter, input);
    Var variable = scope.declare(name, Node.newName(name), null, input, true, testVariable);
    assertEquals(variable, scope.getSlot(name));
  }

  @org.junit.Test
  public void testGetOwnSlot() {
    String name = "x";
    TestVariable testVariable = new TestVariable();
    scope = new Scope(rootNode, errorReporter, input);
    Var variable = scope.declare(name, Node.newName(name), null, input, true, testVariable);
    assertEquals(variable, scope.getOwnSlot(name));
  }

  @org.junit.Test
  public void testIsDeclared() {
    String name = "x";
    scope = new Scope(rootNode, errorReporter, input);
    assertTrue(scope.isDeclared(name, false));
    scope.declare(name, Node.newName(name), null, input, true);
    assertTrue(scope.isDeclared(name, false));
    assertTrue(scope.isDeclared(name, true));
    scope = new Scope(rootNode, errorReporter, input);
    assertFalse(scope.isDeclared(name, false));
    assertFalse(scope.isDeclared(name, true));
  }

  @org.junit.Test
  public void testIsDeclaredWithParentScope() {
    String name = "x";
    Scope parentScope = new Scope(Node.newFunction(1, null, null, null, null), errorReporter, input);
    Scope childScope = new Scope(Node.newBlock(1, null), errorReporter, input, parentScope.thisType);
    parentScope.declare(name, Node.newName(name), null, input, true);
    assertTrue(childScope.isDeclared(name, false));
    assertTrue(childScope.isDeclared(name, true));
  }

  @org.junit.Test
  public void testGetVars() {
    String name1 = "x";
    String name2 = "y";
    scope = new Scope(rootNode, errorReporter, input);
    Var variable1 = scope.declare(name1, Node.newName(name1), null, input, true);
    Var variable2 = scope.declare(name2, Node.newName(name2), null, input, true);
    Iterator<Var> iterator = scope.getVars();
    assertEquals(2, iterator.size());
    assertTrue(iterator.hasNext());
    Var next = iterator.next();
    assertEquals(variable1, next);
    assertTrue(iterator.hasNext());
    next = iterator.next();
    assertEquals(variable2, next);
  }

  @org.junit.Test
  public void testGetVarCount() {
    String name1 = "x";
    String name2 = "y";
    scope = new Scope(rootNode, errorReporter, input);
    Var variable1 = scope.declare(name1, Node.newName(name1), null, input, true);
    Var variable2 = scope.declare(name2, Node.newName(name2), null, input, true);
    assertEquals(2, scope.getVarCount());
  }

  @org.junit.Test
  public void testGetParentScope() {
    Node functionNode = Node.newFunction(1, null, null, null, null);
    Scope parentScope = new Scope(functionNode, errorReporter, input);
    Scope childScope = new Scope(Node.newBlock(1, null), errorReporter, input, parentScope.thisType);
    assertEquals(parentScope, childScope.getParentScope());
  }

  @org.junit.Test
  public void testGetTypeOfThis() {
    FunctionType functionType = new FunctionType(JSType.OBJECT, JSType.VOID);
    ObjectType objectType = new ObjectType(JSType.NULL);
    Scope scope = new Scope(Node.newFunction(1, null, null, null, null), errorReporter, input, objectType);
    assertEquals(objectType, scope.getTypeOfThis());
    Scope globalScope = new Scope(Node.newProgram(-1, null), errorReporter, input);
    assertEquals(new ObjectType(JSType.NULL), globalScope.getTypeOfThis());
  }

  @org.junit.Test
  public void testGetRootNode() {
    Scope scope = new Scope(Node.newFunction(1, null, null, null, null), errorReporter, input);
    assertEquals(Node.newFunction(1, null, null, null, null), scope.getRootNode());
  }

  @org.junit.Test
  public void testIsGlobal() {
    Scope scope = new Scope(Node.newFunction(1, null, null, null, null), errorReporter, input);
    assertFalse(scope.isGlobal());
    Scope globalScope = new Scope(Node.newProgram(-1, null), errorReporter, input);
    assertTrue(globalScope.isGlobal());
  }

  @org.junit.Test
  public void testIsLocal() {
    Scope scope = new Scope(Node.newFunction(1, null, null, null, null), errorReporter, input);
    assertFalse(scope.isLocal());
    Scope globalScope = new Scope(Node.newProgram(-1, null), errorReporter, input);
    assertTrue(globalScope.isLocal());
  }

  // TODO: Test other methods

  static class TestVariable {
  }
}