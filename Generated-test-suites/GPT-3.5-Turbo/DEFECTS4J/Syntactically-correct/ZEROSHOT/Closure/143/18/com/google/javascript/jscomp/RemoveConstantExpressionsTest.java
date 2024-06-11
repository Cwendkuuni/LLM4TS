import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RemoveConstantExpressionsTest {
    RemoveConstantExpressions removeConstantExpressions;

    @Before
    public void setUp() {
        removeConstantExpressions = new RemoveConstantExpressions();
    }

    @After
    public void tearDown() {
        removeConstantExpressions = null;
    }

    @Test
    public void testProcess() {
        // Test case with a simple expression
        Node externs = new Node(Token.EXTERN, new Node(Token.BLOCK));
        Node root = new Node(Token.ROOT, new Node(Token.BLOCK));

        Node exprResult = new Node(Token.EXPR_RESULT);
        Node expression = new Node(Token.ADD,
                                   new Node(Token.ADD,
                                            new Node(Token.NUMBER, 1),
                                            new Node(Token.CALL, new Node(Token.NAME, new Node(Token.STRING, "foo")))),
                                   new Node(Token.CALL, new Node(Token.NAME, new Node(Token.STRING, "bar"))));
        exprResult.addChildToBack(expression);
        root.addChildToFront(exprResult);

        removeConstantExpressions.process(externs, root);

        assertEquals(2, root.getChildCount());

        Node actualExprResult1 = root.getFirstChild();
        assertEquals(Token.EXPR_RESULT, actualExprResult1.getType());

        Node actualExpression1 = actualExprResult1.getFirstChild();
        assertEquals(Token.CALL, actualExpression1.getType());
        assertEquals("foo", actualExpression1.getString());

        Node actualExprResult2 = actualExprResult1.getNext();
        assertEquals(Token.EXPR_RESULT, actualExprResult2.getType());

        Node actualExpression2 = actualExprResult2.getFirstChild();
        assertEquals(Token.CALL, actualExpression2.getType());
        assertEquals("bar", actualExpression2.getString());
    }

    @Test
    public void testRemoveConstantRValuesCallback() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback =
                removeConstantExpressions.new RemoveConstantRValuesCallback();

        NodeTraversal traversal = new NodeTraversal(null, null);
        Node parent = new Node(Token.BLOCK);
        Node node = new Node(Token.EXPR_RESULT);

        // Test case with an expression that does not need to be simplified
        Node exprBody = new Node(Token.CALL, new Node(Token.NAME, new Node(Token.STRING, "foo")));
        node.addChildToFront(exprBody);
        callback.visit(traversal, node, parent);

        assertEquals(1, parent.getChildCount());
        assertEquals(node, parent.getFirstChild());

        // Test case with an expression that can be simplified
        exprBody = new Node(Token.NUMBER, 1);
        node = new Node(Token.EXPR_RESULT);
        node.addChildToFront(exprBody);
        parent.addChildToFront(node);

        callback.visit(traversal, node, parent);

        assertEquals(1, parent.getChildCount());
        assertEquals(Token.EXPR_RESULT, parent.getFirstChild().getType());

        Node actualExprResult = parent.getFirstChild();
        assertEquals(Token.CALL, actualExprResult.getFirstChild().getType());
        assertEquals("foo", actualExprResult.getFirstChild().getString());
    }

    @Test
    public void testGetSideEffectNodes() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback =
                removeConstantExpressions.new RemoveConstantRValuesCallback();

        Node exprBody = new Node(Token.CALL, new Node(Token.NAME, new Node(Token.STRING, "foo")));
        List<Node> sideEffectNodes = callback.getSideEffectNodes(exprBody);

        assertEquals(1, sideEffectNodes.size());
        assertEquals(Token.EXPR_RESULT, sideEffectNodes.get(0).getType());

        Node actualExprResult = sideEffectNodes.get(0).getFirstChild();
        assertEquals(Token.CALL, actualExprResult.getType());
        assertEquals("foo", actualExprResult.getString());
    }

    @Test
    public void testResultGetResult() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback =
                removeConstantExpressions.new RemoveConstantRValuesCallback();
        RemoveConstantExpressions.Result result = callback.getResult();

        assertFalse(result.changed);

        result.changed = true;
        assertTrue(result.changed);
    }
}