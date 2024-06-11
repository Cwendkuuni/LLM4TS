import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RenameLabelsTest {
  private AbstractCompiler compiler;
  private RenameLabels renameLabels;
  private NodeTraversal nodeTraversal;
  private Node root;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    renameLabels = new RenameLabels(compiler);
    NodeFactory nodeFactory = NodeFactory.getFactory();
    root = nodeFactory.createRoot();
  }

  @Test
  public void testRenameLabels() {
    // Test standard usage of RenameLabels
    String originalCode = "function foo() {" +
        "  a: {b: break a;}" +
        "  c: {" +
        "    d: {e: break c;}" +
        "    break a;" +
        "  }" +
        "}";
    Node testCode = NodeUtil.parseJs(originalCode);
    renameLabels.process(null, testCode);

    String expectedCode = "function foo() {" +
        "  a: {a1: {a2: break a;" +
        "    a2: null;" +
        "    a1: null;" +
        "  }}" +
        "  c: {" +
        "    c1: {" +
        "      c2: {c3: break c;" +
        "        c3: null;" +
        "        c2: null;" +
        "      }" +
        "      c1: null;" +
        "    }" +
        "    break a;" +
        "  }" +
        "}";
    String actualCode = NodeUtil.toSource(root);
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testRenameLabelsWithUnusedLabel() {
    // Test that an unused label is removed
    String originalCode = "function foo() {" +
        "  a: {}";
    Node testCode = NodeUtil.parseJs(originalCode);
    renameLabels.process(null, testCode);

    String actualCode = NodeUtil.toSource(root);
    assertTrue(actualCode.contains("// Label a removed by RenameLabels"));
    assertFalse(actualCode.contains("a:"));
  }

  @Test(expected = RuntimeException.class)
  public void testRenameLabelsWithLabelInBlockStatement() {
    // Test that a label in a block statement throws an exception
    String originalCode = "function foo() {" +
        "  a: {";
    Node testCode = NodeUtil.parseJs(originalCode);
    renameLabels.process(null, testCode);
  }

  @Test(expected = RuntimeException.class)
  public void testRenameLabelsWithLabelInVariableDeclaration() {
    // Test that a label in a variable declaration throws an exception
    String originalCode = "function foo() {" +
        "  var a: {b: break a;}" +
        "}";
    Node testCode = NodeUtil.parseJs(originalCode);
    renameLabels.process(null, testCode);
  }

  @Test(expected = RuntimeException.class)
  public void testRenameLabelsWithLabelInExpressionStatement() {
    // Test that a label in an expression statement throws an exception
    String originalCode = "function foo() {" +
        "  a: {};";
    Node testCode = NodeUtil.parseJs(originalCode);
    renameLabels.process(null, testCode);
  }

  @Test(expected = RuntimeException.class)
  public void testRenameLabelsWithLabelInConditionalExpression() {
    // Test that a label in a conditional expression throws an exception
    String originalCode = "function foo() {" +
        "  a: {" +
        "    i = 0;" +
        "    b: i > 0 ? break a : 0;" +
        "  }" +
        "}";
    Node testCode = NodeUtil.parseJs(originalCode);
    renameLabels.process(null, testCode);
  }
}