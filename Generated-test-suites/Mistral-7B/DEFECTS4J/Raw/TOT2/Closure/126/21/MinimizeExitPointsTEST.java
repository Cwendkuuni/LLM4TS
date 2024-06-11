import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.test_util.Context;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {

  AbstractCompiler compiler;
  NodeFactory nodeFactory;
  Context context;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    nodeFactory = compiler.getFactory();
    context = new Context(compiler);
  }

  @Test
  public void testSimpleBreak() {
    String code = "function foo() {"
        + "for (var i = 0; i < 5; i++) {"
        + "if (i == 2) break;"
        + "}"
        + "}";

    Node root = ParseTest.parseAndCompile(context, code);
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    minimizeExitPoints.process(context.getExterns(), root);
    assertEquals("expected 1 statements but found 2", 1, root.getChildCount());
  }

  @Test
  public void testSimpleContinue() {
    String code = "function foo() {"
        + "for (var i = 0; i < 5; i++) {"
        + "if (i == 2) continue;"
        + "}"
        + "}";

    Node root = ParseTest.parseAndCompile(context, code);
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    minimizeExitPoints.process(context.getExterns(), root);
    assertEquals("expected 2 statements but found 1", 2, root.getChildCount());
  }

  @Test
  public void testComplexBreak() {
    String code = "function foo() {"
        + "for (var i = 0; i < 5; i++) {"
        + "if (i % 2 == 0) {"
        + " break;"
        + "}"
        + "}"
        + "}";

    Node root = ParseTest.parseAndCompile(context, code);
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    minimizeExitPoints.process(context.getExterns(), root);
    assertEquals("expected 1 statements but found 3", 1, root.getChildCount());
  }

  @Test
  public void testComplexContinue() {
    String code = "function foo() {"
        + "for (var i = 0; i < 5; i++) {"
        + "if (i % 2 == 0) {"
        + " continue;"
        + "}"
        + "}"
        + "}";

    Node root = ParseTest.parseAndCompile(context, code);
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    minimizeExitPoints.process(context.getExterns(), root);
    assertEquals("expected 6 statements but found 5", 6, root.getChildCount());
  }

  @Test
  public void testLabeledBreak() {
    String code = "function foo() {"
        + "Label1:"
        + " for (var i = 0; i < 5; i++) {"
        + " if (i == 2) break Label1;"
        + "}"
        + "}";

    Node root = ParseTest.parseAndCompile(context, code);
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    minimizeExitPoints.process(context.getExterns(), root);
    assertEquals("expected 2 statements but found 1", 2, root.getChildCount());
  }

  @Test
  public void testLabeledContinue() {
    String code = "function foo() {"
        + "Label1:"
        + " for (var i = 0; i < 5; i++) {"
        + " if (i % 2 == 0) continue Label1;"
        + "}"
        + "}";

    Node root = ParseTest.parseAndCompile(context, code);
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    minimizeExitPoints.process(context.getExterns(), root);
    assertEquals("expected 6 statements but found 5", 6, root.getChildCount());
  }

  @Test(expected = AssertionError.class)
  public void testReturnWithoutLabeledBreak() {
    String code = "function foo() {"
        + "return;"
        + "Label1:"
        + " for (var i = 0; i < 5; i++) {"
        + " if (i == 2) break Label1;"
        + "}"
        + "}";

    Node root = ParseTest.parseAndCompile(context, code);
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    minimizeExitPoints.process(context.getExterns(), root);
    fail("Expected an AssertionError.");
  }

  @Test
  public void testReturnWithLabeledBreak() {
    String code = "function foo() {"
        + "Label1:"
        + " for (var i = 0; i < 5; i++) {"
        + " if (i == 2) return; " // Should be ignored
        + " if (i == 2) break Label1;"
        + "}"
        + "}";

    Node root = ParseTest.parseAndCompile(context, code);
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    minimizeExitPoints.process(context.getExterns(), root);
    assertEquals("expected 1 statements but found 2", 1, root.getChildCount());
  }

  @Test
  public void testLabeledBreakWithLabeledContinue() {
    String code = "function foo() {"
        + "Label1:"
        + " for (var i = 0; i < 5; i++) {"
        + " if (i == 2) break Label1;"
        + " if (i % 2 == 0) continue Label1;"
        + "}"
        + "}";

    Node root = ParseTest.parseAndCompile(context, code);
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);
    minimizeExitPoints.process(context.getExterns(), root);
    assertEquals("expected 3 statements but found 2", 3, root.getChildCount());
  }
}