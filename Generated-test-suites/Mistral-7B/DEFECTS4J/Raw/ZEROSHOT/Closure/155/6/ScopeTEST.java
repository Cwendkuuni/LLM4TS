import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.StaticScope;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticSlot;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScopeTest {

  private AbstractCompiler compiler;
  private Node rootNode;
  private Scope scope;
  private CompilerInput input;
  private StaticScope<JSType> parentScope;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    rootNode = mock(Node.class);
    when(rootNode.getType()).thenReturn(null);
    when(rootNode.getParent()).thenReturn(null);

    compiler = mock(AbstractCompiler.class);
    TypeRegistry typeRegistry = mock(TypeRegistry.class);
    when(compiler.getTypeRegistry()).thenReturn(typeRegistry);
    when(typeRegistry.getNativeObjectType(JSTypeNative.GLOBAL_THIS)).thenReturn(
        new ObjectType("Object"));

    input = new CompilerInput("test.js") {};

    parentScope = mock(StaticScope.class);
    when(parentScope.getParentScope()).thenReturn(null);
    scope = new Scope(parentScope, rootNode);
  }

  @Test
  public void testCreateScope() {
    assertEquals(0, scope.getDepth());
    assertEquals(null, scope.getParent());
    assertEquals(new ObjectType("Object"), scope.getTypeOfThis());
    assertTrue(scope.isBottom());
  }

  @Test
  public void testCreateFunctionScope() {
    Node functionNode = mock(Node.class);
    when(functionNode.getType()).thenReturn(new FunctionType(new ObjectType("Function"), new ObjectType("Object"), new ObjectType("Object"), new Node[0][]));

    scope = new Scope(parentScope, functionNode);

    assertEquals(1, scope.getDepth());
    assertEquals(parentScope, scope.getParent());
    assertEquals(new ObjectType("Function"), scope.getTypeOfThis());
    assertFalse(scope.isBottom());
  }

  @Test
  public void testDeclareVariable() {
    String variableName = "testVariable";
    Node variableNameNode = mock(Node.class);
    when(variableNameNode.getName()).thenReturn(variableName);
    JSType variableType = new ObjectType("Object");

    scope.declare(variableName, variableNameNode, variableType, input);

    Var testVariable = scope.getVar(variableName);
    assertNotNull(testVariable);
    assertEquals(variableName, testVariable.getName());
    assertEquals(variableNameNode, testVariable.getNameNode());
    assertEquals(variableType, testVariable.getType());
    assertFalse(testVariable.isTypeInferred());
    assertEquals(input, testVariable.getInput());
    assertFalse(testVariable.isDefine());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVariableNullName() {
    String variableName = null;
    Node variableNameNode = mock(Node.class);
    JSType variableType = new ObjectType("Object");

    scope.declare(variableName, variableNameNode, variableType, input);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVariableNullNameNode() {
    String variableName = "testVariable";
    Node variableNameNode = null;

    scope.declare(variableName, variableNameNode, new ObjectType("Object"), input);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVariableNullType() {
    String variableName = "testVariable";
    Node variableNameNode = mock(Node.class);
    JSType variableType = null;

    scope.declare(variableName, variableNameNode, variableType, input);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVariableNullInput() {
    String variableName = "testVariable";
    Node variableNameNode = mock(Node.class);
    JSType variableType = new ObjectType("Object");

    scope.declare(variableName, variableNameNode, variableType, null);
  }

  @Test
  public void testDeclareMultipleVariables() {
    String variable1Name = "testVariable1";
    Node variable1NameNode = mock(Node.class);
    when(variable1NameNode.getName()).thenReturn(variable1Name);
    JSType variable1Type = new ObjectType("Object1");

    String variable2Name = "testVariable2";
    Node variable2NameNode = mock(Node.class);
    when(variable2NameNode.getName()).thenReturn(variable2Name);
    JSType variable2Type = new ObjectType("Object2");

    scope.declare(variable1Name, variable1NameNode, variable1Type, input);
    scope.declare(variable2Name, variable2NameNode, variable2Type, input);

    Var variable1 = scope.getVar(variable1Name);
    Var variable2 = scope.getVar(variable2Name);

    assertEquals(variable1, variable1);
    assertEquals(variable2, variable2);
    assertEquals(variable1Name, variable1.getName());
    assertEquals(variable2Name, variable2.getName());
    assertEquals(variable1NameNode, variable1.getNameNode());
    assertEquals(variable2NameNode, variable2.getNameNode());
    assertEquals(variable1Type, variable1.getType());
    assertEquals(variable2Type, variable2.getType());
    assertFalse(variable1.isTypeInferred());
    assertFalse(variable1.isDefine());
    assertFalse(variable2.isTypeInferred());
    assertFalse(variable2.isDefine());
  }

  @Test
  public void testGetVarNullName() {
    assertNull(scope.getVar(null));
  }

  @Test
  public void testGetVarUndefinedVariable() {
    assertNull(scope.getVar("undefinedVariable"));
  }

  @Test
  public void testGetVarVariableInParentScope() {
    String variableName = "testVariable";
    Node variableNameNode = mock(Node.class);
    when(variableNameNode.getName()).thenReturn(variableName);
    JSType variableType = new ObjectType("Object");

    Var testVariable = new Var(false, variableName, variableNameNode, variableType, scope, 0, input, false, null);
    scope.vars.put(variableName, testVariable);
    Scope parentScope = mock(StaticScope.class);
    when(parentScope.getVar(variableName)).thenReturn(new Var(false, variableName, variableNameNode, variableType, scope, 0, input, false, null));

    assertEquals(testVariable, scope.getVar(variableName));
  }

  @Test
  public void testIsDeclaredVariableInParentScope() {
    String variableName = "testVariable";

    assertTrue(scope.isDeclared(variableName, false));
  }

  @Test
  public void testIsDeclaredVariableInThisScope() {
    String variableName = "testVariable";
    Node variableNameNode = mock(Node.class);
    when(variableNameNode.getName()).thenReturn(variableName);

    scope.declare(variableName, variableNameNode, new ObjectType("Object"), input);

    assertTrue(scope.isDeclared(variableName, false));
  }

  @Test
  public void testIsDeclaredVariableInParentScopeWithRecursion() {
    String variableName = "testVariable";

    assertTrue(scope.isDeclared(variableName, true));
  }

  @Test
  public void testIsDeclaredUndefinedVariable() {
    assertFalse(scope.isDeclared("undefinedVariable", false));
  }

  @Test
  public void testGetVarsEmpty() {
    assertEquals(0, scope.getVars().size());
  }

  @Test
  public void testGetVarsWithVariables() {
    String variable1Name = "testVariable1";
    Node variable1NameNode = mock(Node.class);
    when(variable1NameNode.getName()).thenReturn(variable1Name);
    JSType variable1Type = new ObjectType("Object1");

    String variable2Name = "testVariable2";
    Node variable2NameNode = mock(Node.class);
    when(variable2NameNode.getName()).thenReturn(variable2Name);
    JSType variable2Type = new ObjectType("Object2");

    scope.declare(variable1Name, variable1NameNode, variable1Type, input);
    scope.declare(variable2Name, variable2NameNode, variable2Type, input);

    int size = 0;
    for (StaticSlot<JSType> var : scope.getVars()) {
      size++;
    }

    assertEquals(2, size);
  }

  @Test
  public void testGetVarCount() {
    String variable1Name = "testVariable1";
    Node variable1NameNode = mock(Node.class);
    when(variable1NameNode.getName()).thenReturn(variable1Name);
    JSType variable1Type = new ObjectType("Object1");

    String variable2Name = "testVariable2";
    Node variable2NameNode = mock(Node.class);
    when(variable2NameNode.getName()).thenReturn(variable2Name);
    JSType variable2Type = new ObjectType("Object2");

    scope.declare(variable1Name, variable1NameNode, variable1Type, input);
    scope.declare(variable2Name, variable2NameNode, variable2Type, input);

    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsGlobalScope() {
    assertTrue(scope.isGlobal());
  }

  @Test
  public void testIsLocalScope() {
    assertFalse(scope.isLocal());
  }

  @Test
  public void testGetParentScope() {
    assertEquals(parentScope, scope.getParentScope());
  }
}