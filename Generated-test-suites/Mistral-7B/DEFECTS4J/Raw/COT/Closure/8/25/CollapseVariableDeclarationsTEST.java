import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.NodeType.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CollapseVariableDeclarationsTest {
  private Compiler compiler;
  private CollapseVariableDeclarations cvd;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.addPass(new CompilerPass() {
      @Override
      public void process(Node externs, Node root) {
        cvd = new CollapseVariableDeclarations(compiler);
        cvd.process(externs, root);
      }
    });
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#process(Node, Node)}.
   */
  @Test
  public void testCollapsingVariableDeclarations() {
    String code = "function f() {"
        + "var a; "
        + "var b = 1; "
        + "var c = 2; "
        + "}";

    Node root = Parser.parse(code, null, null).body;
    cvd.process(root, null);

    assertCollapsed(root, "function f() { var a, b = 1, c = 2; }");
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#process(Node, Node)}.
   */
  @Test
  public void testCollapsingVariableAssignments() {
    String code = "function f() {"
        + "a = true; "
        + "b = true; "
        + "var c = true; "
        + "}";

    Node root = Parser.parse(code, null, null).body;
    cvd.process(root, null);

    assertCollapsed(root, "function f() { var c = b = a = true; }");
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#process(Node, Node)}.
   */
  @Test
  public void testCollapsingVariableDeclarationsWithinFunction() {
    String code = "function f() {"
        + "var a; "
        + "function g() {"
        + "var b; "
        + "} "
        + "}";

    Node root = Parser.parse(code, null, null).body;
    cvd.process(root, null);

    assertCollapsed(root, "function f() { var a; function g() { var b; } }");
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#process(Node, Node)}.
   */
  @Test
  public void testCollapsingVariableDeclarationsWithinConditional() {
    String code = "function f() {"
        + "if (true) {"
        + "var a; "
        + "var b = 1; "
        + "} "
        + "}";

    Node root = Parser.parse(code, null, null).body;
    cvd.process(root, null);

    assertCollapsed(root, "function f() { if (true) { var a, b = 1; } }");
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#process(Node, Node)}.
   */
  @Test
  public void testCollapsingVariableDeclarationsWithinLoops() {
    String code = "function f() {"
        + "for (var i = 0; i < 10; i++) {"
        + "var a; "
        + "var b = 1; "
        + "} "
        + "}";

    Node root = Parser.parse(code, null, null).body;
    cvd.process(root, null);

    assertCollapsed(root, "function f() { for (var i = 0, a, b = 1; i < 10; i++) {} }");
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#process(Node, Node)}.
   */
  @Test
  public void testCollapsingVariableDeclarationsWithinSwitch() {
    String code = "function f() {"
        + "switch (1) {"
        + "case 0:"
        + "var a; "
        + "case 1:"
        + "var b = 1; "
        + "}"
        + "}";

    Node root = Parser.parse(code, null, null).body;
    cvd.process(root, null);

    assertCollapsed(root, "function f() { switch (1) {"
        + "case 0:"
        + "var a;"
        + "case 1:"
        + "var b = 1;"
        + "}}");
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#process(Node, Node)}.
   */
  @Test
  public void testCollapsingVariableDeclarationsWithinIfElse() {
    String code = "function f() {"
        + "if (true) {"
        + "var a; "
        + "} else {"
        + "var b = 1; "
        + "} "
        + "}";

    Node root = Parser.parse(code, null, null).body;
    cvd.process(root, null);

    assertCollapsed(root, "function f() {"
        + "if (true) { var a; } else { var b = 1; } }");
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#process(Node, Node)}.
   */
  @Test
  public void testCollapsingVariableDeclarationsWithinTryCatchFinally() {
    String code = "function f() {"
        + "try {"
        + "var a; "
        + "} catch (e) {"
        + "var b = 1; "
        + "} finally {"
        + "var c = 2; "
        + "} "
        + "}";

    Node root = Parser.parse(code, null, null).body;
    cvd.process(root, null);

    assertCollapsed(root, "function f() {"
        + "try {"
        + "var a;"
        + "} catch (e) {"
        + "var b = 1;"
        + "} finally {"
        + "var c = 2;"
        + "} }");
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#process(Node, Node)}.
   */
  @Test
  public void testCollapsingVariableDeclarationsWithinSwitchWithinLoop() {
    String code = "function f() {"
        + "for (var i = 0; i < 10; i++) {"
        + "switch (i) {"
        + "case 0:"
        + "var a; "
        + "case 1:"
        + "var b = 1; "
        + "}"
        + "}"
        + "}";

    Node root = Parser.parse(code, null, null).body;
    cvd.process(root, null);

    assertCollapsed(root, "function f() {"
        + "for (var i = 0, a, b = 1; i < 10; i++) {"
        + "switch (i) {"
        + "case 0:"
        + "}"
        + "case 1:"
        + "}"
        + "} }");
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#process(Node, Node)}.
   */
  @Test
  public void testCollapsingVariableDeclarationsWithinIfElseIfElse() {
    String code = "function f() {"
        + "if (true) {"
        + "var a; "
        + "} else if (true) {"
        + "var b = 1; "
        + "} else {"
        + "var c = 2; "
        + "} "
        + "}";

    Node root = Parser.parse(code, null, null).body;
    cvd.process(root, null);

    assertCollapsed(root, "function f() {"
        + "if (true) { var a; } else if (true) { var b = 1; } else { var c = 2; } }");
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#applyCollapses()}.
   */
  @Test(expected = AssertionError.class)
  public void testCollapsingVariableDeclarationsWithinVar() {
    String code = "function f() {"
        + "var a; "
        + "var a; "
        + "}";

    Node root = Parser.parse(code, null, null).body;
    cvd.process(root, null);

    assertCollapsed(root, "function f() { var a; }");

    // This should fail because 'a' is already declared
    cvd.applyCollapses();
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#applyCollapses()}.
   */
  @Test(expected = AssertionError.class)
  public void testCollapsingVariableAssignmentsWithinVar() {
    String code = "function f() {"
        + "var a = 1; "
        + "var a = 2; "
        + "}";

    Node root = Parser.parse(code, null, null).body;
    cvd.process(root, null);

    assertCollapsed(root, "function f() { var a = 1; }");

    // This should fail because 'a' is already defined
    cvd.applyCollapses();
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#applyCollapses()}.
   */
  @Test(expected = AssertionError.class)
  public void testCollapsingVariableDeclarationsWithinFunctionWithinFunction() {
    String code = "function f() {"
        + "function g() {"
        + "var a; "
        + "function h() {"
        + "var b = 1; "
        + "} "
        + "}"
        + "}"
        + "}";

    Node root = Parser.parse(code, null, null).body;
    cvd.process(root, null);

    assertCollapsed(root, "function f() { function g() { var a; function h() { var b = 1; } } }");

    // This should fail because 'a' is already declared
    cvd.applyCollapses();
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#applyCollapses()}.
   */
  @Test(expected = AssertionError.class)
  public void testCollapsingVariableDeclarationsWithinIfElseWithinIfElse() {
    String code = "function f() {"
        + "if (true) {"
        + "if (true) {"
        + "var a; "
        + "} else {"
        + "var b = 1; "
        + "}} else {"
        + "var c = 2; "
        + "}"
        + "}"
        + "}";

    Node root = Parser.parse(code, null, null).body;
    cvd.process(root, null);

    assertCollapsed(root, "function f() {"
        + "if (true) {"
        + "if (true) { var a; } else { var b = 1; } } else { var c = 2; } }");

    // This should fail because 'a' is already declared
    cvd.applyCollapses();
  }

  /**
   * Test method for {@link CollapseVariableDeclarations#applyCollapses()}.
   */
  @Test(expected = AssertionError.class)
  public void testCollapsingVariableDeclarationsWithinTryCatchFinallyWithinTryCatchFinally() {
    String code = "function f() {"
        + "try {"
        + "try {"
        + "var a; "
        + "} catch (e) {"
        + "} finally {"
        + "try {"
        + "var b = 1; "
        + "} catch (e) {"
        + "} finally {"
        + "var c = 2; "
        + "}"
        + "}"
        + "}"
        + "}"
        + "}";

    Node root = Parser.parse(code, null, null).body;
    cvd.process(root, null);

    assertCollapsed(root, "function f() {"
        + "try {"
        + "try {"
        + "var a;"
        + "} catch (e) {"
        + "} finally {"
        + "try {"
        + "var b = 1;"
        + "} catch (e) {"
        + "} finally {"
        + "var c = 2;"
        + "}"
        + "}"
        + "}"
        + "}");

    // This should fail because 'a' is already declared
    cvd.applyCollapses();
  }

  /**
   * Check if the parsed node matches the expected code.
   *
   * @param node The parsed node
   * @param expected The expected code
   */
  private void assertCollapsed(Node node, String expected) {
    String actual = NodePrinter.printNode(node);
    assertEquals(expected, actual);
  }
}