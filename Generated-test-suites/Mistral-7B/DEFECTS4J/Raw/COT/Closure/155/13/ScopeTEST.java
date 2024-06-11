import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.Scope.*;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversingVisitor;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Iterator;
import java.util.Map;

/**
 * Test class for the Scope class.
 */
public class ScopeTest {
  private ErrorReporter errorReporter;
  private Scope scope;
  private Node rootNode;

  @Before
  public void setUp() {
    errorReporter = Mockito.mock(ErrorReporter.class);
    rootNode = Mockito.mock(Node.class);

    // Set up the rootNode to return a FunctionType for this type
    Mockito.when(rootNode.getJSType()).thenReturn(new FunctionType(null, null));

    scope = new Scope(null, rootNode);
  }

  @Test
  public void testScopeConstructor() {
    // Test constructor with rootNode (Function node)
    assertFalse(scope.isBottom());
    assertEquals(1, scope.getDepth());
    assertEquals(null, scope.getParent());

    // Test constructor with global rootNode
    Scope globalScope = new Scope(rootNode, Mockito.mock(AbstractCompiler.class));
    assertTrue(globalScope.isBottom());
    assertEquals(0, globalScope.getDepth());
    assertNull(globalScope.getParent());

    // Test constructor with empty Scope
    Scope emptyScope = new Scope(rootNode, null);
    assertTrue(emptyScope.isBottom());
    assertEquals(0, emptyScope.getDepth());
    assertNull(emptyScope.getParent());
  }

  @Test
  public void testGetParent() {
    Scope parentScope = Mockito.mock(Scope.class);
    when(parentScope.getDepth()).thenReturn(1);

    Scope childScope = new Scope(parentScope, rootNode);

    assertEquals(parentScope, childScope.getParent());
    assertEquals(1, childScope.getDepth());
  }

  @Test
  public void testGetRootNode() {
    assertEquals(rootNode, scope.getRootNode());
  }

  @Test
  public void testGetTypeOfThis() {
    // Test with Function node
    ObjectType expectedThisType = Mockito.mock(ObjectType.class);
    when(rootNode.getJSType()).thenReturn(new FunctionType(expectedThisType, null));
    assertEquals(expectedThisType, scope.getTypeOfThis());

    // Test with global rootNode
    ObjectType globalThisType = Mockito.mock(ObjectType.class);
    when(rootNode.getJSType()).thenReturn(null);
    scope = new Scope(rootNode, globalThisType);
    assertEquals(globalThisType, scope.getTypeOfThis());
  }

  @Test
  public void testIsBottom() {
    // Test empty Scope
    Scope emptyScope = new Scope(rootNode, null);
    assertTrue(emptyScope.isBottom());

    // Test non-empty Scope
    scope = new Scope(null, rootNode);
    assertFalse(scope.isBottom());
  }

  @Test
  public void testGetDepth() {
    assertEquals(1, scope.getDepth());
  }

  @Test
  public void testDeclare() {
    // Test var declaration
    String name = "testVar";
    JSType type = Mockito.mock(JSType.class);
    CompilerInput input = Mockito.mock(CompilerInput.class);

    scope.declare(name, null, type, input);

    Var testVar = scope.getVar(name);
    assertNotNull(testVar);
    assertEquals(name, testVar.getName());
    assertEquals(type, testVar.getType());
    assertEquals(input, testVar.getInput());

    // Test const var declaration
    Node constantNameNode = Mockito.mock(Node.class);
    JSDocInfo info = Mockito.mock(JSDocInfo.class);
    when(info.isConst()).thenReturn(true);
    when(info.isDefine()).thenReturn(false);

    scope.declare(name, constantNameNode, type, input, null);

    Var constantTestVar = scope.getVar(name);
    assertNotNull(constantTestVar);
    assertTrue(constantTestVar.isConst());
    assertFalse(constantTestVar.isDefine());

    // Test define var declaration
    JSDocInfo defineInfo = Mockito.mock(JSDocInfo.class);
    when(defineInfo.isDefine()).thenReturn(true);

    scope.declare(name, null, type, input, null, defineInfo);

    Var defineTestVar = scope.getVar(name);
    assertNotNull(defineTestVar);
    assertTrue(defineTestVar.isDefine());
  }

  @Test(expected = IllegalStateException.class)
  public void testDeclareWithExistingVar() {
    String name = "testVar";
    JSType type = Mockito.mock(JSType.class);
    CompilerInput input = Mockito.mock(CompilerInput.class);

    // Declare the variable first
    scope.declare(name, null, type, input);

    // Try to declare it again
    scope.declare(name, null, type, input);
  }

  @Test
  public void testGetVar() {
    String name = "testVar";
    Var testVar = new Var(false, name, null, null, scope, 0, null, false, null);
    scope.vars.put(name, testVar);

    Var gotTestVar = scope.getVar(name);
    assertEquals(testVar, gotTestVar);
  }

  @Test
  public void testGetVarNull() {
    assertNull(scope.getVar("nonexistentVar"));
  }

  @Test
  public void testGetVarRecursive() {
    String parentName = "parentVar";
    Var parentVar = new Var(false, parentName, null, null, scope, 0, null, false, null);
    scope.vars.put(parentName, parentVar);

    String childName = "childVar";
    Var childVar = new Var(false, childName, null, null, scope, 0, null, false, null);
    parentVar.vars.put(childName, childVar);

    Var gotChildVar = scope.getVar(childName);
    assertEquals(childVar, gotChildVar);
  }

  @Test
  public void testGetOwnSlot() {
    String name = "testVar";
    Var testVar = new Var(false, name, null, null, scope, 0, null, false, null);
    scope.vars.put(name, testVar);

    StaticSlot<JSType> gotSlot = scope.getSlot(name);
    assertEquals(testVar, gotSlot);
  }

  @Test
  public void testGetSlotNull() {
    assertNull(scope.getSlot("nonexistentVar"));
  }

  @Test
  public void testGetSlotRecursive() {
    String parentName = "parentVar";
    Var parentVar = new Var(false, parentName, null, null, scope, 0, null, false, null);
    scope.vars.put(parentName, parentVar);

    String childName = "childVar";
    Var childVar = new Var(false, childName, null, null, parentVar, 0, null, false, null);
    parentVar.vars.put(childName, childVar);

    StaticSlot<JSType> gotSlot = scope.getSlot(childName);
    assertEquals(childVar, gotSlot);
  }

  @Test
  public void testIsDeclared() {
    String name = "testVar";
    Var testVar = new Var(false, name, null, null, scope, 0, null, false, null);
    scope.vars.put(name, testVar);

    assertTrue(scope.isDeclared(name, false));

    // Test recursively
    String parentName = "parentVar";
    Var parentVar = new Var(false, parentName, null, null, scope, 0, null, false, null);
    scope.vars.put(parentName, parentVar);
    parentVar.vars.put(name, testVar);

    assertTrue(scope.isDeclared(name, true));
  }

  @Test
  public void testIsDeclaredFalse() {
    assertFalse(scope.isDeclared("nonexistentVar", false));

    // Test recursively
    String parentName = "parentVar";
    Var parentVar = new Var(false, parentName, null, null, scope, 0, null, false, null);
    scope.vars.put(parentName, parentVar);

    assertFalse(scope.isDeclared("nonexistentVar", true));
  }

  @Test
  public void testGetVars() {
    String name1 = "testVar1";
    Var testVar1 = new Var(false, name1, null, null, scope, 0, null, false, null);
    scope.vars.put(name1, testVar1);

    String name2 = "testVar2";
    Var testVar2 = new Var(false, name2, null, null, scope, 0, null, false, null);
    scope.vars.put(name2, testVar2);

    Iterator<Var> vars = scope.getVars();

    Var firstVar = vars.next();
    assertEquals(testVar1, firstVar);

    Var secondVar = vars.next();
    assertEquals(testVar2, secondVar);

    assertFalse(vars.hasNext());
  }

  @Test
  public void testGetVarCount() {
    String name1 = "testVar1";
    Var testVar1 = new Var(false, name1, null, null, scope, 0, null, false, null);
    scope.vars.put(name1, testVar1);

    String name2 = "testVar2";
    Var testVar2 = new Var(false, name2, null, null, scope, 0, null, false, null);
    scope.vars.put(name2, testVar2);

    int varCount = scope.getVarCount();
    assertEquals(2, varCount);
  }

  @Test
  public void testIsGlobal() {
    assertTrue(scope.isGlobal());

    Scope parentScope = Mockito.mock(Scope.class);
    when(parentScope.isGlobal()).thenReturn(false);

    Scope childScope = new Scope(parentScope, rootNode);
    assertFalse(childScope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    assertFalse(scope.isLocal());

    Scope parentScope = Mockito.mock(Scope.class);
    when(parentScope.isLocal()).thenReturn(false);

    Scope childScope = new Scope(parentScope, rootNode);
    assertTrue(childScope.isLocal());
  }
}