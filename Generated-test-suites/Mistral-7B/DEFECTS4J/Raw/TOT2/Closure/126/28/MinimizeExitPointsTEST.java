import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.rhino.Token.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Nodeops;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {

  AbstractCompiler compiler;
  MinimizeExitPoints minimizeExitPoints;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    minimizeExitPoints = new MinimizeExitPoints(compiler);
  }

  @Test
  public void testSimpleLabelExit() {
    Node root = makeNode("myLabel: { if (true) break myLabel; }");
    minimizeExitPoints.process(null, root);
    assertFalse(root.getLastChild().hasChildren());
    assertEquals("myLabel", root.getFirstChild().getString());
  }

  @Test
  public void testSimpleIfExit() {
    Node root = makeNode("if (true) return;");
    minimizeExitPoints.process(null, root);
    assertTrue(root.isReturn());
  }

  @Test
  public void testSimpleFunctionExit() {
    Node root = makeNode("function f() { return; }");
    minimizeExitPoints.process(null, root);
    assertNotNull(root.getLastChild());
    assertTrue(root.getLastChild().isFunctionDeclaration());
  }

  @Test
  public void testSimpleIfElseExit() {
    Node root = makeNode("if (true) return; else return;");
    minimizeExitPoints.process(null, root);
    assertTrue(root.isReturn());
  }

  @Test
  public void testSimpleIfElseIfExit() {
    Node root = makeNode("if (true) return; else if (false) return;");
    minimizeExitPoints.process(null, root);
    assertTrue(root.isEmpty());
  }

  @Test
  public void testSimpleIfElseIfElseExit() {
    Node root = makeNode("if (true) return; else if (false) return; else return;");
    minimizeExitPoints.process(null, root);
    assertTrue(root.isEmpty());
  }

  @Test
  public void testSimpleIfWhileExit() {
    Node root = makeNode("while (true) if (true) return;");
    minimizeExitPoints.process(null, root);
    assertTrue(root.isEmpty());
  }

  @Test
  public void testSimpleLabelBreak() {
    Node root = makeNode("myLabel: { break myLabel; }");
    minimizeExitPoints.process(null, root);
    assertFalse(root.getLastChild().hasChildren());
    assertEquals("myLabel", root.getFirstChild().getString());
  }

  @Test
  public void testSimpleLabelContinue() {
    Node root = makeNode("myLabel: { continue myLabel; }");
    minimizeExitPoints.process(null, root);
    assertTrue(root.isEmpty());
  }

  @Test
  public void testSimpleLabelBreakWithName() {
    Node root = makeNode("myLabel: { break myLabel; }");
    minimizeExitPoints.process(null, root);
    assertFalse(root.getLastChild().hasChildren());
    assertEquals("myLabel", root.getFirstChild().getString());

    root = makeNode("myLabel: { break labelName; }");
    minimizeExitPoints.process(null, root);
    assertFalse(root.getLastChild().hasChildren());
    assertEquals("labelName", root.getFirstChild().getString());
  }

  @Test
  public void testSimpleLabelContinueWithName() {
    Node root = makeNode("myLabel: { continue myLabel; }");
    minimizeExitPoints.process(null, root);
    assertTrue(root.isEmpty());

    root = makeNode("myLabel: { continue labelName; }");
    minimizeExitPoints.process(null, root);
    assertTrue(root.isEmpty());
  }

  @Test
  public void testComplexLabelExit() {
    Node root = makeNode("myLabel: { if (true) break myLabel; else if (true) break myLabel; }");
    minimizeExitPoints.process(null, root);
    assertTrue(root.isEmpty());
  }

  @Test
  public void testComplexIfElseExit() {
    Node root = makeNode("if (true) { if (false) return; } else return;");
    minimizeExitPoints.process(null, root);
    assertTrue(root.isEmpty());
  }

  @Test
  public void testComplexIfElseIfExit() {
    Node root = makeNode("if (true) { if (false) return; } else if (false) return;");
    minimizeExitPoints.process(null, root);
    assertTrue(root.isEmpty());
  }

  @Test
  public void testComplexIfElseIfElseExit() {
    Node root = makeNode("if (true) { if (false) return; } else if (true) return; } else return;");
    minimizeExitPoints.process(null, root);
    assertTrue(root.isEmpty());
  }

  @Test
  public void testComplexIfElseIfElseIfExit() {
    Node root = makeNode("if (true) { if (false) return; } else if (false) { if (true) return; } else return;");
    minimizeExitPoints.process(null, root);
    assertTrue(root.isEmpty());
  }

  @Test
  public void testComplexIfElseIfElseIfElseExit() {
    Node root = makeNode("if (true) { if (false) return; } else if (false) { if (false) return; } else if (true) return; } else return;");
    minimizeExitPoints.process(null, root);
    assertTrue(root.isEmpty());
  }

  @Test
  public void testComplexIfWhileExit() {
    Node root = makeNode("while (true) { if (true) return; } if (false) return;");
    minimizeExitPoints.process(null, root);
    assertTrue(root.isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testInvalidExit() {
    Node root = makeNode("while (true) { switch (1) { case 0: return; } }");
    minimizeExitPoints.process(null, root);
  }

  private Node makeNode(String code) {
    return compiler.parseTree(code, null).getFirstChild();
  }
}