import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class MustBeReachingVariableDefTest {

  private static final String TEST_CODE =
      "function foo() {" +
          "var x = 1;" +
          "var y = 2;" +
          "var z = 3;" +
          "if (true) {" +
          "  x = y;" +
          "  y = z;" +
          "} else {" +
          "  x = z;" +
          "  z = y;" +
          "}" +
          "return x;" +
          "}";

  private Scope jsScope;
  private MustBeReachingVariableDef analyzer;

  @Before
  public void setUp() {
    jsScope = Scope.createGlobalScope();
    analyzer = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(Token.TOK_BLOCK, null, null, null, null, null),
        jsScope,
        new AbstractCompiler() {
          @Override
          public Node compile(Node n) {
            return null;
          }
        });
  }

  @Test
  public void testBasicForwardFlow() {
    Node root = NodeUtil.parse(TEST_CODE);
    analyzer.compute(root);
    Node xDef = analyzer.getDef("x", root);
    Node yDef = analyzer.getDef("y", root);
    Node zDef = analyzer.getDef("z", root);

    // In the basic forward flow, x should have the definition of y and y should
    // have the definition of z.
    assertThat(xDef).isEqualTo(yDef);
    assertThat(yDef).isEqualTo(zDef);
  }

  @Test
  public void testBasicConditionalFlow() {
    Node root = NodeUtil.parse(TEST_CODE);
    analyzer.compute(root);

    // When the conditional is true, x should have the definition of y.
    // When the conditional is false, x should have the definition of z.
    Node useX = Node.newLocal("useX");
    Node useY = Node.newLocal("useY");
    Node useZ = Node.newLocal("useZ");
    Node conditional = NodeUtil.parseConditional(true);
    Node assignmentX = Node.newAssignment(useX, Node.newName("x"));
    Node assignmentY = Node.newAssignment(useY, Node.newName("y"));
    Node assignmentZ = Node.newAssignment(useZ, Node.newName("z"));
    Node block = Node.newBlock(
        new Node[]{conditional, assignmentX, assignmentY, assignmentZ},
        null, null, null, null, null);
    Node useXInBlock = Node.newName("x");

    analyzer.mustBeReachingDefinition(useXInBlock, block);

    Node defX = analyzer.getDef("x", block);
    Node defY = analyzer.getDef("y", block);
    Node defZ = analyzer.getDef("z", block);

    // In the basic conditional flow, x should have the definition of y when the
    // conditional is true and the definition of z when the conditional is false.
    assertThat(defX).isNull();
    assertThat(defY).isNotNull();
    assertThat(defY).isEqualTo(yDef);
    assertThat(defZ).isNotNull();
    assertThat(defZ).isEqualTo(zDef);
  }

  @Test
  public void testNoDef() {
    Node root = NodeUtil.parse(TEST_CODE);
    analyzer.compute(root);
    Node useNonExisting = Node.newName("nonExisting");

    analyzer.mustBeReachingDefinition(useNonExisting, root);

    Node def = analyzer.getDef(useNonExisting.toString(), root);

    // There should be no definition for non-existent variables.
    assertThat(def).isNull();
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    String testCodeWithOuterScope =
        "function foo() {" +
            "var outer = 0;" +
            "function bar() {" +
            "  var x = 1;" +
            "  var y = 2;" +
            "  if (outer > 0) {" +
            "    x = y;" +
            "  } else {" +
            "    x = 3;" +
            "  }" +
            "  return x;" +
            "}";

    Node root = NodeUtil.parse(testCodeWithOuterScope);
    analyzer.compute(root);

    Node useX = Node.newName("x");
    Node useY = Node.newName("y");

    Node bar = NodeUtil.parseFunction("bar", ImmutableSet.of("outer"), null);
    Node useXInBar = Node.newName("x");
    Node useYInBar = Node.newName("y");
    Node callBar = Node.newCall(bar, Node.newArray(Node.newName("outer")), null);
    Node block = Node.newBlock(
        new Node[]{useXInBar, useYInBar, callBar},
        null, null, null, null, null);

    // x should depend on the outer variable y in this case.
    analyzer.mustBeReachingDefinition(useX, block);

    Node defX = analyzer.getDef(useX.toString(), block);
    Node defY = analyzer.getDef(useY.toString(), block);

    // In this case, x depends on the outer variable y.
    Node defOuter = analyzer.getDef(useX.toString(), root);
    assertThat(defOuter).isEqualTo(analyzer.getDef("outer", root));
    assertThat(defX).isNull();
    assertThat(defY).isNull();
  }

  @Test(expected = AssertionError.class)
  public void testCycle() {
    String testCodeWithCycle =
        "function foo() {" +
            "var x = 1;" +
            "var y = 2;" +
            "x = y;" +
            "y = x;" +
            "return x;" +
            "}";

    Node root = NodeUtil.parse(testCodeWithCycle);
    analyzer.compute(root);
    Node useX = Node.newName("x");
    Node useY = Node.newName("y");
    Node block = Node.newBlock(
        new Node[]{useX, useY}, null, null, null, null, null);

    analyzer.mustBeReachingDefinition(useX, block);

    // This test case should fail due to a cycle.
  }
}