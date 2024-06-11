import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.*;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TypeChecker;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

public class ScopeTest {

  private Node rootNode;
  private Scope globalScope;
  private Scope localScope;
  private Scope nestedScope;
  private Map<String, Var> globalVariables;
  private Map<String, Var> localVariables;
  private Var globalVar;
  private Var localVar;
  private Var nestedVar;

  @Before
  public void setUp() {
    rootNode = NodeUtil.parse("function testScope() {\n" +
        "  var globalVar = 1;\n" +
        "  function localFunction() {\n" +
        "    var localVar = 2;\n" +
        "    function nestedFunction() {\n" +
        "      var nestedVar = 3;\n" +
        "    }\n" +
        "  }\n" +
        "  localFunction();\n" +
        "}\n");

    TypeChecker typeChecker = new TypeChecker();
    GlobalToLocalAssigner assigner = new GlobalToLocalAssigner(null, null, null, null, typeChecker, new ErrorReporter());
    globalScope = new Scope(null, rootNode);
    globalVariables = Maps.newHashMap();
    localScope = new Scope(globalScope, NodeUtil.getFirstChild(rootNode));
    localVariables = Maps.newHashMap();
    nestedScope = new Scope(localScope, NodeUtil.getFirstChild(NodeUtil.getLastChild(localScope.getRootNode())));

    assigner.process(rootNode, null);

    globalVar = globalScope.getVar("globalVar");
    globalVariables.put("globalVar", globalVar);

    localVar = localScope.getVar("localVar");
    localVariables.put("localVar", localVar);

    nestedVar = nestedScope.getVar("nestedVar");
  }

  @Test
  public void testGetDepth() {
    assertEquals(0, globalScope.getDepth());
    assertEquals(1, localScope.getDepth());
    assertEquals(2, nestedScope.getDepth());
  }

  @Test
  public void testIsBottom() {
    assertFalse(globalScope.isBottom());
    assertFalse(localScope.isBottom());
    assertTrue(nestedScope.isBottom());
  }

  @Test
  public void testGetRootNode() {
    assertEquals(rootNode, globalScope.getRootNode());
    assertEquals(NodeUtil.getFirstChild(rootNode), localScope.getRootNode());
    assertEquals(NodeUtil.getLastChild(NodeUtil.getFirstChild(localScope.getRootNode())), nestedScope.getRootNode());
  }

  @Test
  public void testGetParent() {
    assertNull(globalScope.getParent());
    assertEquals(globalScope, localScope.getParent());
    assertEquals(localScope, nestedScope.getParent());
  }

  @Test
  public void testGetTypeOfThis() {
    assertEquals(ObjectType.OBJECT, globalScope.getTypeOfThis());
    assertNull(localScope.getTypeOfThis());
  }

  @Test
  public void testDeclare() {
    globalScope.declare("globalVar", NodeUtil.getFirstChild(NodeUtil.getFirstChild(rootNode)), JSType.INT, null);
    localScope.declare("localVar", NodeUtil.getFirstChild(NodeUtil.getLastChild(NodeUtil.getLastChild(localScope.getRootNode()))), JSType.STRING, null);
    nestedScope.declare("nestedVar", NodeUtil.getFirstChild(NodeUtil.getLastChild(nestedScope.getRootNode())), JSType.BOOLEAN, null);

    assertEquals("globalVar", globalVar.getName());
    assertEquals("localVar", localVar.getName());
    assertEquals("nestedVar", nestedVar.getName());

    assertEquals(JSType.INT, globalVar.getType());
    assertEquals(JSType.STRING, localVar.getType());
    assertEquals(JSType.BOOLEAN, nestedVar.getType());
  }

  @Test
  public void testUndeclare() {
    globalScope.undeclare(globalVar);
    assertNull(globalScope.getVar("globalVar"));
  }

  @Test
  public void testGetSlot() {
    assertEquals(globalVar, globalScope.getSlot("globalVar"));
    assertEquals(localVar, localScope.getSlot("localVar"));
    assertEquals(nestedVar, nestedScope.getSlot("nestedVar"));
  }

  @Test
  public void testIsDeclared() {
    assertTrue(isDeclared("globalVar", globalScope, true));
    assertTrue(isDeclared("localVar", localScope, true));
    assertTrue(isDeclared("nestedVar", nestedScope, false));
    assertFalse(isDeclared("undeclaredVar", globalScope, true));
    assertFalse(isDeclared("undeclaredVar", localScope, true));
    assertFalse(isDeclared("undeclaredVar", nestedScope, false));
  }

  @Test
  public void testGetVars() {
    assertEquals(1, globalScope.getVarCount());
    assertEquals(1, localScope.getVarCount());
    assertEquals(1, nestedScope.getVarCount());

    Iterator<Var> globalIter = globalScope.getVars();
    globalIter.next();
    assertFalse(globalIter.hasNext());

    Iterator<Var> localIter = localScope.getVars();
    localIter.next();
    assertFalse(localIter.hasNext());

    Iterator<Var> nestedIter = nestedScope.getVars();
    nestedIter.next();
    assertFalse(nestedIter.hasNext());
  }

  @Test
  public void testIsGlobal() {
    assertTrue(globalScope.isGlobal());
    assertFalse(localScope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    assertFalse(globalScope.isLocal());
    assertTrue(localScope.isLocal());
  }

  private boolean isDeclared(String name, Scope scope, boolean recurse) {
    if (scope.vars.containsKey(name)) {
      return true;
    }

    if (scope.parent != null && recurse) {
      return scope.parent.isDeclared(name, recurse);
    }
    return false;
  }
}