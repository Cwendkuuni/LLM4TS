package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.util.AbstractCompiler;
import com.google.javascript.jscomp.util.CompilerInput;
import com.google.javascript.jscomp.util.ErrorReporter;
import com.google.javascript.jscomp.util.NodeUtil;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(Parameterized.class)
public class ScopeTest {
  private AbstractCompiler compiler;
  private ErrorReporter errorReporter;
  private NodeFactory nodeFactory;
  private CompilerInput input;
  private Scope scope;

  public ScopeTest(Node rootNode) {
    this.compiler = new AbstractCompiler() {};
    this.errorReporter = new ErrorReporter() {};
    this.nodeFactory = NodeFactory.getFactory();
    this.input = new CompilerInput("test.js", null);
    this.scope = new Scope(rootNode, compiler);
  }

  @Parameters
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {
        {nodeFactory.createToken(Token.FUNCTION, 0, 0, null, null)},
        {nodeFactory.createToken(Token.BLOCK, 0, 0, null, null)}
    });
  }

  @Before
  public void setUp() {
    Node Traverse = new NodeTraversal(null, new NodeTraversal.Visitor() {
      public void visit(Node node) {
        scope = new Scope(node, compiler);
      }
    });
    Traverse.traverse(scope.rootNode);
  }

  @Test
  public void testGetDepth() {
    assertEquals(1, scope.getDepth());
  }

  @Test
  public void testIsBottom() {
    assertFalse(scope.isBottom());
  }

  @Test
  public void testGetRootNode() {
    assertEquals(scope.rootNode, scope.getRootNode());
  }

  @Test
  public void testGetParent() {
    assertNull(scope.getParent());
  }

  @Test
  public void testGetTypeOfThis() {
    ObjectType thisType = (ObjectType) JSTypeNative.getGlobalObjectType();
    assertEquals(thisType, scope.getTypeOfThis());
  }

  @Test
  public void testDeclare() {
    Var variable = scope.declare("foo", nodeFactory.createName("foo"),
        JSType.OBJECT, input);
    assertTrue(scope.isDeclared("foo", false));
    assertSame(variable, scope.getVar("foo"));
  }

  @Test
  public void testDeclareDuplicateVariable() {
    scope.declare("foo", nodeFactory.createName("foo"), JSType.OBJECT, input);
    scope.declare("foo", nodeFactory.createName("foo"), JSType.OBJECT, input);
    assertEquals(1, scope.getVarCount());
  }

  @Test
  public void testGetSlot() {
    Var variable = scope.declare("foo", nodeFactory.createName("foo"),
        JSType.OBJECT, input);
    assertEquals(variable, scope.getSlot("foo"));
  }

  @Test
  public void testGetOwnSlot() {
    Var variable = scope.declare("foo", nodeFactory.createName("foo"),
        JSType.OBJECT, input);
    assertEquals(variable, scope.getOwnSlot("foo"));
  }

  @Test
  public void testGetVar() {
    Var variable = scope.declare("foo", nodeFactory.createName("foo"),
        JSType.OBJECT, input);
    assertEquals(variable, scope.getVar("foo"));
  }

  @Test
  public void testIsDeclared() {
    scope.declare("foo", nodeFactory.createName("foo"), JSType.OBJECT, input);
    assertTrue(scope.isDeclared("foo", false));
  }

  @Test
  public void testGetVars() {
    Var variable1 = scope.declare("foo1", nodeFactory.createName("foo1"),
        JSType.OBJECT, input);
    Var variable2 = scope.declare("foo2", nodeFactory.createName("foo2"),
        JSType.OBJECT, input);
    Iterator<Var> vars = scope.getVars();
    assertTrue(vars.hasNext());
    Var firstVar = vars.next();
    assertTrue(firstVar.isEqual(variable1));
    assertTrue(vars.hasNext());
    Var secondVar = vars.next();
    assertTrue(secondVar.isEqual(variable2));
  }

  @Test
  public void testGetVarCount() {
    Var variable1 = scope.declare("foo1", nodeFactory.createName("foo1"),
        JSType.OBJECT, input);
    Var variable2 = scope.declare("foo2", nodeFactory.createName("foo2"),
        JSType.OBJECT, input);
    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsGlobal() {
    assertFalse(scope.isGlobal());
  }

  @Test
  public void testIsLocal() {
    assertTrue(scope.isLocal());
  }
}