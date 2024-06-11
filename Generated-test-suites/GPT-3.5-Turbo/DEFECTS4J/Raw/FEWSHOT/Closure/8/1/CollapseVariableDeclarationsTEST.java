import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CollapseVariableDeclarations;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.testing.TestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CollapseVariableDeclarationsTest {

  private static final String COMPILER_TEST_CASES_FILE =
      "testdata/com/google/javascript/jscomp/collapsevariabledeclarations_testcases.js";

  private void testCollapseVariableDeclarations(String jsInput, String expectedOutput) {
    AbstractCompiler compiler = new TestCompiler();
    Node root = compiler.parseTestCode(jsInput);
    new CollapseVariableDeclarations(compiler).process(null, root);
    String actualOutput = compiler.toSource(root);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testSimple() {
    testCollapseVariableDeclarations(
        "var a; var b = 1; var c = 2;",
        "var a, b = 1, c = 2;");
  }

  @Test
  public void testInsideFunction() {
    testCollapseVariableDeclarations(
        "function foo() { var a; var b = 1; var c = 2; }",
        "function foo() { var a, b = 1, c = 2; }");
  }

  @Test
  public void testCollapseReassignment() {
    testCollapseVariableDeclarations(
        "a = true; b = true; var c = true;",
        "var c = b = a = true;");
  }

  // Additional test cases from compiletests/CollapseVariableDeclarationsCollapsedOwnLine.java

  // "ansiStyle" and "param3" cannot be collapsed because they are in an if condition statement.
  @Test
  public void testNonCollapse() {
    testCollapseVariableDeclarations(
        ""
            + "var collapse = true;\n"
            + "var a;\n"
            + "if (collapse) {\n"
            + "  var ansiStyle;\n"
            + "}\n"
            + "var param3;\n"
            + "if (!collapse) {\n"
            + "  var forIssue;\n"
            + "}\n",
        ""
            + "var collapse = true;\n"
            + "var a;\n"
            + "if (collapse) {\n"
            + "  var ansiStyle;\n"
            + "}\n"
            + "var param3;\n"
            + "if (!collapse) {\n"
            + "  var forIssue;\n"
            + "}\n");
  }

  // All the variable can be collapsed.
  @Test
  public void testCollapseAll() {
    testCollapseVariableDeclarations(
        ""
            + "var collapse = true;\n"
            + "var a;\n"
            + "collapse = false;\n"
            + "var b = 2;\n"
            + "var param3;\n"
            + "if (!collapse) {\n"
            + "  var forIssue;\n"
            + "}\n",
        ""
            + "var collapse = true,\n"
            + "    a;\n"
            + "collapse = false;\n"
            + "var b = 2,\n"
            + "    param3;\n"
            + "if (!collapse) { var forIssue; }\n");
  }
}

class TestCompiler extends TestUtil.AstValidator {
  @Override
  public void validateRoot(Node root) {
    assertEquals(Token.ROOT, root.getToken());
  }

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    // CollapseVariableDeclarations has already rearranged the AST.
    // We can't fall back on the default traversal because the parent references may
    // not be present due to the rearrangements.
  }
}