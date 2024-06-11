import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionRewriterTest {
  @Test
  public void testEmptyFunctionReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = new Node(Token.PROGRAM, 1, 1);
    root.addChildrenToBack(
        new Node(Token.BLOCK, 1, 1),
        new Node(Token.VAR, 1, 1, "a"),
        new Node(Token.ASSIGN, 1, 1, null, new Node(Token.NAME, 1, 1, "a"),
            new Node(Token.CALL, 1, 1, null, new Node(Token.NAME, 1, 1,
                "FunctionRewriter"), new Node(Token.NAME, 1, 1, "prototype"),
                new Node(Token.NAME, 1, 1, "b"))),
        new Node(Token.RETURN, 1, 1, null));

    functionRewriter.process(null, root);

    Node actual = root.getFirstChild().getLastChild();
    Node expected = new Node(Token.CALL, 1, 1, null,
        new Node(Token.NAME, 1, 1, "emptyFn"));

    assertEquals(expected, actual);
  }

  @Test
  public void testIdentityReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = new Node(Token.PROGRAM, 1, 1);
    root.addChildrenToBack(
        new Node(Token.BLOCK, 1, 1),
        new Node(Token.VAR, 1, 1, "a"),
        new Node(Token.ASSIGN, 1, 1, null, new Node(Token.NAME, 1, 1, "a"),
            new Node(Token.CALL, 1, 1, null, new Node(Token.NAME, 1, 1,
                "FunctionRewriter"), new Node(Token.NAME, 1, 1, "prototype"),
                new Node(Token.NAME, 1, 1, "b"), new Node(Token.NAME, 1, 1, "a"))),
        new Node(Token.RETURN, 1, 1, null));

    functionRewriter.process(null, root);

    Node actual = root.getFirstChild().getLastChild();
    Node expected = new Node(Token.CALL, 1, 1, null,
        new Node(Token.NAME, 1, 1, "identityFn"), new Node(Token.NAME, 1, 1, "a"));

    assertEquals(expected, actual);
  }

  @Test
  public void testReturnConstantReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = new Node(Token.PROGRAM, 1, 1);
    root.addChildrenToBack(
        new Node(Token.BLOCK, 1, 1),
        new Node(Token.VAR, 1, 1, "a"),
        new Node(Token.ASSIGN, 1, 1, null, new Node(Token.NAME, 1, 1, "a"),
            new Node(Token.CALL, 1, 1, null, new Node(Token.NAME, 1, 1,
                "FunctionRewriter"), new Node(Token.NAME, 1, 1, "prototype"),
                new Node(Token.NAME, 1, 1, "b"), new Node(Token.NUMBER, 1, 1, 10))),
        new Node(Token.RETURN, 1, 1, null));

    functionRewriter.process(null, root);

    Node actual = root.getFirstChild().getLastChild();
    Node expected = new Node(Token.CALL, 1, 1, null,
        new Node(Token.NAME, 1, 1, "returnconst"), new Node(Token.NUMBER, 1, 1, 10));

    assertEquals(expected, actual);
  }

  @Test
  public void testGetterReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = new Node(Token.PROGRAM, 1, 1);
    root.addChildrenToBack(
        new Node(Token.BLOCK, 1, 1),
        new Node(Token.VAR, 1, 1, "a"),
        new Node(Token.ASSIGN, 1, 1, null, new Node(Token.NAME, 1, 1, "a"),
            new Node(Token.CALL, 1, 1, null, new Node(Token.NAME, 1, 1,
                "FunctionRewriter"), new Node(Token.NAME, 1, 1, "prototype"),
                new Node(Token.NAME, 1, 1, "getA"), new Node(Token.NAME, 1, 1,
                "a"))),
        new Node(Token.ASSIGN, 1, 1, null, new Node(Token.NAME, 1, 1, "a"),
            new Node(Token.NAME, 1, 1, "b")),
        new Node(Token.RETURN, 1, 1, null));

    functionRewriter.process(null, root);

    Node actual = root.getFirstChild().getLastChild();
    Node expected = new Node(Token.CALL, 1, 1, null,
        new Node(Token.NAME, 1, 1, "getter"), new Node(Token.NAME, 1, 1, "a"));

    assertEquals(expected, actual);
  }

  @Test
  public void testSetterReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = new Node(Token.PROGRAM, 1, 1);
    root.addChildrenToBack(
        new Node(Token.BLOCK, 1, 1),
        new Node(Token.VAR, 1, 1, "a"),
        new Node(Token.ASSIGN, 1, 1, null, new Node(Token.NAME, 1, 1, "a"),
            new Node(Token.CALL, 1, 1, null, new Node(Token.NAME, 1, 1,
                "FunctionRewriter"), new Node(Token.NAME, 1, 1, "prototype"),
                new Node(Token.NAME, 1, 1, "setA"), new Node(Token.NAME, 1, 1,
                "b"))),
        new Node(Token.ASSIGN, 1, 1, null, new Node(Token.NAME, 1, 1, "a"),
            new Node(Token.NUMBER, 1, 1, 5)),
        new Node(Token.RETURN, 1, 1, null));

    functionRewriter.process(null, root);

    Node actual = root.getFirstChild().getLastChild();
    Node expected = new Node(Token.CALL, 1, 1, null,
        new Node(Token.NAME, 1, 1, "setter"), new Node(Token.NAME, 1, 1, "a"),
        new Node(Token.NUMBER, 1, 1, 5));

    assertEquals(expected, actual);
  }

  @Test(expected = AssertionError.class)
  public void testFunctionRewriterThrowsExceptionWhenNoSavings() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = new Node(Token.PROGRAM, 1, 1);
    root.addChildrenToBack(
        new Node(Token.BLOCK, 1, 1),
        new Node(Token.VAR, 1, 1, "a"),
        new Node(Token.ASSIGN, 1, 1, null, new Node(Token.NAME, 1, 1, "a"),
            new Node(Token.FUNCTION, 1, 1, null, null, new Node(Token.BLOCK, 1, 1),
                new Node(Token.RETURN, 1, 1, null))),
        new Node(Token.ASSIGN, 1, 1, null, new Node(Token.NAME, 1, 1, "a"),
            new Node(Token.NUMBER, 1, 1, 5)),
        new Node(Token.RETURN, 1, 1, null));

    functionRewriter.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testFunctionRewriterThrowsExceptionWhenNegativeSavings() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = new Node(Token.PROGRAM, 1, 1);
    root.addChildrenToBack(
        new Node(Token.BLOCK, 1, 1),
        new Node(Token.VAR, 1, 1, "a"),
        new Node(Token.ASSIGN, 1, 1, null, new Node(Token.NAME, 1, 1, "a"),
            new Node(Token.FUNCTION, 1, 1, null, null, new Node(Token.BLOCK, 1, 1),
                new Node(Token.RETURN, 1, 1, null))),
        new Node(Token.ASSIGN, 1, 1, null, new Node(Token.NAME, 1, 1, "a"),
            new Node(Token.NUMBER, 1, 1, 5)),
        new Node(Token.RETURN, 1, 1, null));

    functionRewriter.process(null, root);

    Node helperNode = root.getFirstChild().getLastChild();
    Node expectedHelperNode = new Node(
        Token.FUNCTION, 1, 1, null, null, new Node(Token.BLOCK, 1, 1),
        new Node(Token.RETURN, 1, 1, null));

    Node actualHelperNode = helperNode.getLastChild();
    Node actualHelperCode = actualHelperNode.getFirstChild();

    InlineCostEstimator estimator = new InlineCostEstimator();
    int helperCodeCost = estimator.getCost(actualHelperCode);

    functionRewriter.parseHelperCode(null);
    Node helperCode = functionRewriter.helperCode;

    int savings = helperCodeCost - InlineCostEstimator.getCost(helperNode);

    functionRewriter.process(null, root);

    int actualSavings = helperCodeCost - InlineCostEstimator.getCost(helperNode);

    fail("Expected savings to be positive, but got: " + savings);
  }
}