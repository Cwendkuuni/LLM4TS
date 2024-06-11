import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse analyzer;
  private Scope scope;
  private ControlFlowGraph<Node> controlFlowGraph;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    scope = new Scope("test");
    controlFlowGraph = new ControlFlowGraph<>();
    compiler = new AbstractCompiler(controlFlowGraph, scope) {
      @Override
      public boolean hasExceptionHandler(Node n) {
        return false;
      }
    };
    analyzer = new MaybeReachingVariableUse(controlFlowGraph, scope, compiler);
  }

  @Test
  public void testEmptyProgram() {
    analyzer.compute();
    assertEquals(ImmutableSet.of(), analyzer.getUses("nonExistentVar", null));
  }

  @Test
  public void testSimpleAssignment() {
    String code = "var a = 1;";
    Node node = AbstractCompiler.parse(code, null);
    analyzer.compute();
    Collection<Node> uses = analyzer.getUses("a", node);
    assertTrue(uses.isEmpty());
  }

  @Test
  public void testSimpleAssignmentWithFunctionCall() {
    String code = "var a = 1; print(a);";
    Node node = AbstractCompiler.parse(code, null);
    analyzer.compute();
    Collection<Node> uses = analyzer.getUses("a", node);
    assertFalse(uses.isEmpty());
    Node useNode = uses.iterator().next();
    assertEquals(Token.PRINT, useNode.getType());
  }

  @Test
  public void testSimpleIfStatement() {
    String code = "var a = 1; if (true) { a = 2; } print(a);";
    Node node = AbstractCompiler.parse(code, null);
    analyzer.compute();
    Collection<Node> uses = analyzer.getUses("a", node);
    assertFalse(uses.isEmpty());
    Node useNode = uses.iterator().next();
    assertEquals(Token.PRINT, useNode.getType());
  }

  @Test
  public void testSimpleIfStatementWithElse() {
    String code = "var a = 1; if (true) { a = 2; } else { a = 3; } print(a);";
    Node node = AbstractCompiler.parse(code, null);
    analyzer.compute();
    Collection<Node> uses = analyzer.getUses("a", node);
    assertFalse(uses.isEmpty());
    Node useNode = uses.iterator().next();
    assertEquals(Token.PRINT, useNode.getType());
  }

  @Test
  public void testSimpleForStatement() {
    String code = "for (var i = 0; i < 10; i++) { print(i); }";
    Node node = AbstractCompiler.parse(code, null);
    analyzer.compute();
    Collection<Node> uses = analyzer.getUses("i", node);
    for (int i = 0; i < 10; i++) {
      Node useNode = uses.iterator().next();
      assertEquals(Token.PRINT, useNode.getType());
    }
  }

  @Test
  public void testSimpleForInStatement() {
    String code = "for (var i in ['a', 'b', 'c']) { print(i); }";
    Node node = AbstractCompiler.parse(code, null);
    analyzer.compute();
    Collection<Node> uses = analyzer.getUses("i", node);
    for (int i = 0; i < 3; i++) {
      Node useNode = uses.iterator().next();
      assertEquals(Token.PRINT, useNode.getType());
    }
  }

  @Test
  public void testNestedIfStatements() {
    String code = "if (true) {" +
        "  if (false) { a = 1; } else { a = 2; } " +
        "} else { a = 3; } print(a);";
    Node node = AbstractCompiler.parse(code, null);
    analyzer.compute();
    Collection<Node> uses = analyzer.getUses("a", node);
    assertFalse(uses.isEmpty());
    Node useNode = uses.iterator().next();
    assertEquals(Token.PRINT, useNode.getType());
  }

  @Test
  public void testNestedIfElseStatements() {
    String code = "if (true) {" +
        "  if (false) { a = 1; } else { a = 2; } " +
        "} else if (true) { a = 3; } else { a = 4; } print(a);";
    Node node = AbstractCompiler.parse(code, null);
    analyzer.compute();
    Collection<Node> uses = analyzer.getUses("a", node);
    assertFalse(uses.isEmpty());
    Node useNode = uses.iterator().next();
    assertEquals(Token.PRINT, useNode.getType());
  }

  @Test
  public void testNestedForStatements() {
    String code = "for (var i = 0; i < 10; i++) {" +
        "  for (var j = 0; j < 10; j++) {" +
        "    print(i + j); " +
        "  }}" +
        "print(100);";
    Node node = AbstractCompiler.parse(code, null);
    analyzer.compute();
    Collection<Node> uses = analyzer.getUses("i", node);
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        Node useNode = uses.iterator().next();
        assertEquals(Token.PRINT, useNode.getType());
        if (i + j != 100) {
          Node useValueNode = useNode.getLastChild();
          assertEquals(Integer.toString(i + j), useValueNode.getString());
        }
      }
    }

    uses = analyzer.getUses("i", node.getLastChild());
    assertNull(uses);

    uses = analyzer.getUses("j", node);
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        Node useNode = uses.iterator().next();
        assertEquals(Token.PRINT, useNode.getType());
        if (i + j != 100) {
          Node useValueNode = useNode.getLastChild();
          assertEquals(Integer.toString(i), useValueNode.getFirstChild().getString());
          assertEquals(Integer.toString(j), useValueNode.getLastChild().getString());
        }
      }
    }

    uses = analyzer.getUses("j", node.getLastChild());
    assertNull(uses);
  }

  @Test
  public void testVariableReassignment() {
    String code = "var a = 1; a = 2; print(a);";
    Node node = AbstractCompiler.parse(code, null);
    analyzer.compute();
    Collection<Node> uses = analyzer.getUses("a", node);
    assertFalse(uses.isEmpty());
    Node useNode = uses.iterator().next();
    assertEquals(Token.PRINT, useNode.getType());
  }

  @Test
  public void testVariableReassignmentWithFunctionCall() {
    String code = "var a = 1; print(a); a = 2;";
    Node node = AbstractCompiler.parse(code, null);
    analyzer.compute();
    Collection<Node> uses = analyzer.getUses("a", node);
    assertFalse(uses.isEmpty());
    Node useNode = uses.iterator().next();
    assertEquals(Token.PRINT, useNode.getType());
  }

  @Test
  public void testVariableUseOutsideOfScope() {
    String code = "var a = 1; function foo() { print(a); }";
    Node node = AbstractCompiler.parse(code, null);
    analyzer.compute();
    Collection<Node> uses = analyzer.getUses("a", node);
    assertNull(uses);
  }

  @Test
  public void testVariableWithSameNameInDifferentScopes() {
    String code1 = "function foo() {" +
        "  var a = 1; " +
        "}";
    String code2 = "function bar() {" +
        "  var a = 2; " +
        "}";
    Node node1 = AbstractCompiler.parse(code1, null);
    Node node2 = AbstractCompiler.parse(code2, null);
    analyzer.compute();
    Collection<Node> uses1 = analyzer.getUses("a", node1);
    assertFalse(uses1.isEmpty());
    Node useNode1 = uses1.iterator().next();
    assertEquals(Token.FUNCTION, useNode1.getType());

    Collection<Node> uses2 = analyzer.getUses("a", node2);
    assertFalse(uses2.isEmpty());
    Node useNode2 = uses2.iterator().next();
    assertEquals(Token.FUNCTION, useNode2.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullDefinitionNode() {
    analyzer.getUses("a", null);
  }
}