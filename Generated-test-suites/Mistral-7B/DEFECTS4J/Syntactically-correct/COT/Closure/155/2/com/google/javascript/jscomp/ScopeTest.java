import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSDocInfo.FunctionType;
import static com.google.javascript.rhino.JSDocInfo.Property;
import static com.google.javascript.rhino.JSDocInfo.Static;
import static com.google.javascript.rhino.JSDocInfo.Variable;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.StaticScope;

@RunWith(MockitoJUnitRunner.class)
public class ScopeTest {

  @Mock
  private ErrorReporter errorReporter;

  @Mock
  private Node rootNode;

  @Mock
  private AbstractCompiler compiler;

  @Mock
  private CompilerInput input;

  @Mock
  private NodeTraversal nodeTraversal;

  @Mock
  private DataFlowAnalysis dataFlowAnalysis;

  private Scope scope;

  @Before
  public void setUp() {
    when(input.getName()).thenReturn("input.js");
  }

  @Test
  public void testScopeConstructionWithParent() {
    Node parentRootNode = Node.newScope(Node.SCOPE);
    when(parentRootNode.getType()).thenReturn(JSType.UNDEFINED);
    Node rootNode = Node.newFunction(Node.FUNCTION, "testFunc", null, null);
    Scope parentScope = new Scope(parentRootNode, null);
    scope = new Scope(rootNode, parentScope);

    assertEquals(parentScope, scope.getParent());
    assertEquals(rootNode, scope.getRootNode());
    assertEquals(JSTypeNative.FUNCTION_TYPE, scope.getTypeOfThis());
    assertFalse(scope.isBottom());
  }

  @Test
  public void testScopeConstructionWithGlobal() {
    Node rootNode = Node.newBlock(Node.BLOCK, null);
    scope = new Scope(rootNode, null, null);

    assertEquals(null, scope.getParent());
    assertEquals(rootNode, scope.getRootNode());
    assertEquals(JSTypeNative.getNativeObjectType(JSTypeNative.GLOBAL_THIS),
        scope.getTypeOfThis());
    assertFalse(scope.isBottom());
  }

  @Test
  public void testScopeConstructionWithEmpty() {
    Node rootNode = Node.newBlock(Node.BLOCK, null);
    scope = new Scope(rootNode, null, null, true);

    assertEquals(null, scope.getParent());
    assertEquals(rootNode, scope.getRootNode());
    assertTrue(scope.isBottom());
    assertFalse(scope.isBottom());
  }

  @Test
  public void testDeclareVariable() {
    String name = "testVar";
    JSType type = JSType.STRING;

    scope = new Scope(rootNode, null);
    Var declaredVar = scope.declare(name, null, type, input);

    assertEquals(name, declaredVar.getName());
    assertEquals(type, declaredVar.getType());
    assertFalse(declaredVar.isTypeInferred());
    assertEquals(scope, declaredVar.getScope());
    assertEquals(input, declaredVar.getInput());
    assertTrue(declaredVar.isDefine());
    assertNull(declaredVar.getParentNode());
    assertNull(declaredVar.getJSDocInfo());
    assertFalse(declaredVar.isExtern());
    assertFalse(declaredVar.isConst());
    assertFalse(declaredVar.isBleedingFunction());
    assertFalse(declaredVar.isNoShadow());

    assertEquals(1, scope.getVarCount());
  }

  @Test
  public void testDeclareVariableWithJSDocInfo() {
    String name = "testVar";
    JSType type = JSType.NUMBER;
    JSDocInfo info = new JSDocInfo(Variable, Static, null, null);

    scope = new Scope(rootNode, null);
    Var declaredVar = scope.declare(name, null, type, input, info);

    assertEquals(name, declaredVar.getName());
    assertEquals(type, declaredVar.getType());
    assertFalse(declaredVar.isTypeInferred());
    assertEquals(scope, declaredVar.getScope());
    assertEquals(input, declaredVar.getInput());
    assertTrue(declaredVar.isDefine());
    assertNull(declaredVar.getParentNode());
    assertEquals(info, declaredVar.getJSDocInfo());
    assertFalse(declaredVar.isExtern());
    assertFalse(declaredVar.isConst());
    assertFalse(declaredVar.isBleedingFunction());
    assertFalse(declaredVar.isNoShadow());

    assertEquals(1, scope.getVarCount());
  }

  @Test
  public void testDeclareVariableWithInferredType() {
    String name = "testVar";
    JSType type = JSType.BOOLEAN;

    scope = new Scope(rootNode, null);
    Var declaredVar = scope.declare(name, null, null, input, type);

    assertEquals(name, declaredVar.getName());
    assertNull(declaredVar.getType());
    assertTrue(declaredVar.isTypeInferred());
    assertEquals(scope, declaredVar.getScope());
    assertEquals(input, declaredVar.getInput());
    assertTrue(declaredVar.isDefine());
    assertNull(declaredVar.getParentNode());
    assertNull(declaredVar.getJSDocInfo());
    assertFalse(declaredVar.isExtern());
    assertFalse(declaredVar.isConst());
    assertFalse(declaredVar.isBleedingFunction());
    assertFalse(declaredVar.isNoShadow());

    assertEquals(1, scope.getVarCount());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVariableWithNullName() {
    String name = null;
    JSType type = JSType.NUMBER;

    scope = new Scope(rootNode, null);
    scope.declare(name, null, type, input);

    fail("Expected IllegalArgumentException");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVariableWithEmptyName() {
    String name = "";
    JSType type = JSType.NUMBER;

    scope = new Scope(rootNode, null);
    scope.declare(name, null, type, input);

    fail("Expected IllegalArgumentException");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeclareVariableWithNullType() {
    String name = "testVar";
    JSType type = null;

    scope = new Scope(rootNode, null);
    scope.declare(name, null, type, input);

    fail("Expected IllegalArgumentException");
  }

  @Test
  public void testGetVar() {
    String name = "testVar";
    JSType type = JSType.NUMBER;

    scope = new Scope(rootNode, null);
    Var declaredVar = scope.declare(name, null, type, input);

    Var resultVar = scope.getVar(name);

    assertEquals(declaredVar, resultVar);
  }

  @Test
  public void testGetVarParentNull() {
    String name = "testVar";
    JSType type = JSType.NUMBER;

    scope = new Scope(rootNode, null);
    Var declaredVar = scope.declare(name, null, type, input);

    Scope parentScope = new Scope(Node.newFunction(Node.FUNCTION, "testParent", null, null), null);
    Var parentDeclaredVar = parentScope.declare(name, null, type, input);

    Var resultVar = scope.getVar(name, true);

    assertNull(resultVar);
  }

  @Test
  public void testGetVarParentSame() {
    String name = "testVar";
    JSType type = JSType.NUMBER;

    scope = new Scope(rootNode, null);
    Var declaredVar = scope.declare(name, null, type, input);

    Var resultVar = scope.getVar(name);

    assertEquals(declaredVar, resultVar);
  }

  @Test
  public void testGetVarParentDifferent() {
    String name = "testVar";
    JSType type = JSType.NUMBER;

    scope = new Scope(rootNode, null);
    Var declaredVar = scope.declare(name, null, type, input);

    Scope parentScope = new Scope(Node.newFunction(Node.FUNCTION, "testParent", null, null), null);
    Var parentDeclaredVar = parentScope.declare(name, null, type, input);

    Var resultVar = scope.getVar(name, true);

    assertEquals(declaredVar, resultVar);
  }

  @Test(expected = NullPointerException.class)
  public void testGetVarNullScope() {
    String name = "testVar";

    Var resultVar = new Scope(null, null).getVar(name);

    fail("Expected NullPointerException");
  }

  @Test
  public void testGetVarWithNullName() {
    Var resultVar = scope.getVar(null);

    assertNull(resultVar);
  }

  @Test
  public void testIsDeclaredVariable() {
    String name = "testVar";

    scope = new Scope(rootNode, null);
    assertTrue(scope.isDeclared(name, true));

    Scope parentScope = new Scope(Node.newFunction(Node.FUNCTION, "testParent", null, null), null);
    parentScope.declare(name, null, JSType.NUMBER, input);

    assertFalse(scope.isDeclared(name, true));
    assertTrue(scope.isDeclared(name, false));
  }

  @Test
  public void testIsDeclaredVariableInParent() {
    String name = "testVar";

    scope = new Scope(rootNode, null);

    Scope parentScope = new Scope(Node.newFunction(Node.FUNCTION, "testParent", null, null), null);
    parentScope.declare(name, null, JSType.NUMBER, input);

    assertTrue(scope.isDeclared(name, true));
  }

  @Test
  public void testGetVars() {
    String name1 = "testVar1";
    JSType type1 = JSType.STRING;

    String name2 = "testVar2";
    JSType type2 = JSType.NUMBER;

    scope = new Scope(rootNode, null);
    Var var1 = scope.declare(name1, null, type1, input);
    Var var2 = scope.declare(name2, null, type2, input);

    Iterator<Var> resultVars = scope.getVars();

    Var var1Result = resultVars.next();
    Var var2Result = resultVars.next();

    assertEquals(var1, var1Result);
    assertEquals(var2, var2Result);
  }

  @Test
  public void testGetVarCount() {
    String name1 = "testVar1";
    JSType type1 = JSType.STRING;

    String name2 = "testVar2";
    JSType type2 = JSType.NUMBER;

    scope = new Scope(rootNode, null);
    Var var1 = scope.declare(name1, null, type1, input);
    Var var2 = scope.declare(name2, null, type2, input);

    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    assertTrue(scope.isGlobal());

    Node rootNode = Node.newFunction(Node.FUNCTION, "testFunc", null, null);
    Scope parentScope = new Scope(rootNode, null);
    scope = new Scope(Node.newFunction(Node.FUNCTION, "testFunc", null, null), parentScope);

    assertFalse(scope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    assertFalse(scope.isLocal());

    Node rootNode = Node.newFunction(Node.FUNCTION, "testFunc", null, null);
    Scope parentScope = new Scope(rootNode, null);
    scope = new Scope(Node.newFunction(Node.FUNCTION, "testFunc", null, null), parentScope);

    assertTrue(scope.isLocal());
  }

  @Test
  public void testIsDeclaredVariableWithNullScope() {
    Var resultVar = scope.isDeclared(null, false);

    assertNull(resultVar);
  }

  @Test
  public void testIsDeclaredVariableWithNullName() {
    Var resultVar = scope.isDeclared(null, false);

    assertNull(resultVar);
  }

  @Test
  public void testIsDeclaredVariableWithEmptyName() {
    Var resultVar = scope.isDeclared("", false);

    assertNull(resultVar);
  }
}