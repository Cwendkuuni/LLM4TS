import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeType.ASSIGN;
import static com.google.javascript.jscomp.NodeType.VAR;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TestBase;
import com.google.javascript.rhino.JSDocInfo;

import java.util.List;
import java.util.Set;

/**
 * Test class for CollapseVariableDeclarations
 */
public class CollapseVariableDeclarationsTest extends TestBase {

  /**
   * Test for the CollapseVariableDeclarations class
   */
  public void testCollapseVariableDeclarations() {
    String jsCode = "function foo() {"
        + "  var a;"
        + "  var b = 1;"
        + "  var c = 2;"
        + "  var a = 3;"
        + "  var d = 4;"
        + "  var e = 5;"
        + "  var a = 6;"
        + "  var f = 7;"
        + "  var g = h = i = 8;"
        + "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node rootNode = parsejs(jsCode, null, null, options);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(options.externs, rootNode);

    Node expectedRootNode = parseJs("function foo() {"
        + "  var a, b = 1, c = 2, h, i;"
        + "  var a = 6;"
        + "  var f = 7;"
        + "  var g = h = i = 8;"
        + "}", null, null, options);
    assertNodesEqual(expectedRootNode, rootNode);

    Node fooFunction = rootNode.getFirstChild();
    Node fooFunctionBody = fooFunction.getLastChild();

    // Check that variable declarations are collapsed
    Node collapsedVariables = fooFunctionBody.getFirstChild();
    assertEquals(7, collapsedVariables.getNumChildren());
    List<Node> collapsedVariableNames = Lists.newArrayList();

    for (Node child = collapsedVariables; child != null; child = child.getNext()) {
      if (child.isVar()) {
        collapsedVariableNames.add(child.getString());
      }
    }

    Set<String> expectedCollapsedVariableNames = Sets.newHashSet("a", "b", "c", "h", "i", "g");
    assertEquals("Unexpected collapsed variable names.", expectedCollapsedVariableNames,
        Sets.difference(Sets.newHashSet(collapsedVariableNames.toArray(new String[0])), Sets.newHashSet()));

    // Check that variable assignments are collapsed
    Node collapsedAssignments = collapsedVariables.getNext();
    assertEquals(3, collapsedAssignments.getNumChildren());

    List<Node> collapsedAssignmentsList = Lists.newArrayList();
    for (Node child = collapsedAssignments; child != null; child = child.getNext()) {
      if (child.isAssign()) {
        Node assign = child.getFirstChild();
        Node lhs = assign.getFirstChild();
        Node rhs = assign.getLastChild();

        collapsedAssignmentsList.add(assign);
        assertEquals(VAR, lhs.getType());
        assertEquals("Unexpected variable name.", "a", lhs.getString());
        assertEquals(VAR, rhs.getType());
        assertEquals(TOKEN_NUMBER, rhs.getFirstChild().getType());
      }
    }

    Set<Node> expectedCollapsedAssignments = Sets.newHashSet(
        new Node(Token.ASSIGN, new Node(Token.NAME, "a"), new Node(Token.NUMBER, 3)),
        new Node(Token.ASSIGN, new Node(Token.NAME, "h"), new Node(Token.NAME, "i")),
        new Node(Token.ASSIGN, new Node(Token.NAME, "i"), new Node(Token.NUMBER, 8))
    );

    assertEquals("Unexpected collapsed assignments.", expectedCollapsedAssignments,
        Sets.difference(Sets.newHashSet(collapsedAssignmentsList.toArray(new Node[0])), Sets.newHashSet()));
  }

  /**
   * Test for collapsing multiple variable declarations into a single one when
   * there is a redeclaration
   */
  public void testCollapseVariableDeclarationsWithRedeclaration() {
    String jsCode = "function foo() {"
        + "  var a;"
        + "  var a = 1;"
        + "  var b = 2;"
        + "  var b = 3;"
        + "  var c = 4;"
        + "  var c = 5;"
        + "  var d = 6;"
        + "  var d = 7;"
        + "  var e = 8;"
        + "  var e = 9;"
        + "  var f = 10;"
        + "  var g = h = i = 11";
        + "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node rootNode = parsejs(jsCode, null, null, options);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(options.externs, rootNode);

    Node expectedRootNode = parseJs("function foo() {"
        + "  var a, b, c, d, e, h, i, g;"
        + "  var a = 1;"
        + "  var b = 3;"
        + "  var c = 5;"
        + "  var d = 7;"
        + "  var e = 9;"
        + "  var h = i = 11;"
        + "}", null, null, options);
    assertNodesEqual(expectedRootNode, rootNode);
  }

  /**
   * Test for collapsing multiple variable assignments into a single one
   */
  public void testCollapseVariableAssignments() {
    String jsCode = "function foo() {"
        + "  var a;"
        + "  a = true;"
        + "  a = false;"
        + "  var b = true;"
        + "  b = false;"
        + "  var c = true;"
        + "  c = false;"
        + "  var d = true;"
        + "  d = false;"
        + "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node rootNode = parsejs(jsCode, null, null, options);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(options.externs, rootNode);

    Node expectedRootNode = parseJs("function foo() {"
        + "  var a, b, c, d;"
        + "  a = b = c = d = false;"
        + "}", null, null, options);
    assertNodesEqual(expectedRootNode, rootNode);
  }

  /**
   * Test for collapsing variable declarations and assignments inside an IF statement
   */
  public void testCollapseVariableDeclarationsAndAssignmentsInsideIfStatement() {
    String jsCode = "function foo() {"
        + "  if (true) {"
        + "    var a;"
        + "    a = 1;"
        + "  } else {"
        + "    var b = 2;"
        + "    b = 3;"
        + "  }"
        + "  var c = 4;"
        + "  var d = 5;"
        + "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node rootNode = parsejs(jsCode, null, null, options);

    CollapseVariableDeclarations collapseVariableDeclarations =
        new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(options.externs, rootNode);

    Node expectedRootNode = parseJs("function foo() {"
        + "  if (true) {"
        + "    var a, c, d;"
        + "    a = 1;"
        + "  } else {"
        + "    var b = 3;"
        + "  }"
        + "  var c = 4;"
        + "  var d = 5;"
        + "}", null, null, options);
    assertNodesEqual(expectedRootNode, rootNode);
  }
}