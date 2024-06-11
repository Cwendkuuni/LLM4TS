import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

public class RemoveConstantExpressionsTest {

    @Test
    public void testProcess_Method() {
        AbstractCompiler compiler = new AbstractCompiler();
        RemoveConstantExpressions.RemoveConstantRValuesCallback cb = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        Node externs = null;
        Node root = new Node(Node.SCRIPT);
        Node exprResultNode = new Node(Token.EXPR_RESULT);
        Node expressionNode = new Node(Token.PLUS);
        expressionNode.addChildToBack(new Node(Token.NUMBER, "1"));
        expressionNode.addChildToBack(new Node(Token.CALL, new Node(Token.NAME, "foo")));
        expressionNode.addChildToBack(new Node(Token.CALL, new Node(Token.NAME, "bar")));
        exprResultNode.addChildToBack(expressionNode);
        root.addChildToBack(exprResultNode);

        cb.visit(new NodeTraversal(compiler, null), exprResultNode, root);

        // Verifying that the expression node is replaced with side effect nodes correctly
        Node newRoot = cb.getResult().getAst();
        Node newExprResultNode = newRoot.getFirstChild();
        Node newExpressionNode = newExprResultNode.getFirstChild();
        Node newFooCallNode = newExpressionNode.getFirstChild();
        Node newBarCallNode = newExpressionNode.getLastChild();
        
        assertEquals(Token.CALL, newFooCallNode.getType());
        assertEquals(Token.CALL, newBarCallNode.getType());
        assertEquals(Token.NAME, newFooCallNode.getFirstChild().getType());
        assertEquals(Token.NAME, newBarCallNode.getFirstChild().getType());
        assertEquals("foo", newFooCallNode.getFirstChild().getString());
        assertEquals("bar", newBarCallNode.getFirstChild().getString());
    }

    @Test
    public void testTrySimplify_Method_WithNoSubexpression() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        Node parent = new Node(Token.STMT_LIST);
        Node exprResultNode = new Node(Token.EXPR_RESULT);
        exprResultNode.addChildToBack(new Node(Token.NUMBER, "1"));

        callback.trySimplify(parent, exprResultNode);

        // Verifying that no changes are made to the AST
        assertEquals(parent.getFirstChild(), exprResultNode);
    }

    @Test
    public void testTrySimplify_Method_WithSubexpression() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        Node parent = new Node(Token.STMT_LIST);
        Node exprResultNode = new Node(Token.EXPR_RESULT);
        Node expressionNode = new Node(Token.PLUS);
        expressionNode.addChildToBack(new Node(Token.NUMBER, "1"));
        expressionNode.addChildToBack(new Node(Token.CALL, new Node(Token.NAME, "foo")));
        expressionNode.addChildToBack(new Node(Token.NUMBER, "2"));
        exprResultNode.addChildToBack(expressionNode);

        callback.trySimplify(parent, exprResultNode);

        // Verifying that the expression node is replaced with side effect nodes
        assertEquals(parent.getFirstChild(), exprResultNode);

        Node newExpressionNode = exprResultNode.getFirstChild();
        Node newFooCallNode = newExpressionNode.getFirstChild();
        Node newNumberNode = newExpressionNode.getLastChild();
        assertEquals(Token.CALL, newFooCallNode.getType());
        assertEquals(Token.NAME, newFooCallNode.getFirstChild().getType());
        assertEquals("foo", newFooCallNode.getFirstChild().getString());
        assertEquals(Token.NUMBER, newNumberNode.getType());
        assertEquals("2", newNumberNode.getString());
    }

    @Test
    public void testGetSideEffectNodes_Method() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        Node expressionNode = new Node(Token.PLUS);
        expressionNode.addChildToBack(new Node(Token.NUMBER, "1"));
        expressionNode.addChildToBack(new Node(Token.CALL, new Node(Token.NAME, "foo")));
        expressionNode.addChildToBack(new Node(Token.NUMBER, "2"));

        List<Node> sideEffectNodes = callback.getSideEffectNodes(expressionNode);

        // Verifying that the list of side effect nodes is correct
        assertEquals(2, sideEffectNodes.size());

        Node firstSideEffectNode = sideEffectNodes.get(0);
        Node secondSideEffectNode = sideEffectNodes.get(1);
        
        assertEquals(Token.EXPR_RESULT, firstSideEffectNode.getType());
        assertEquals(Token.EXPR_RESULT, secondSideEffectNode.getType());

        Node firstStatementNode = firstSideEffectNode.getFirstChild();
        Node secondStatementNode = secondSideEffectNode.getFirstChild();

        assertEquals(Token.CALL, firstStatementNode.getType());
        assertEquals(Token.NUMBER, secondStatementNode.getType());

        Node firstStatementNameNode = firstStatementNode.getFirstChild();
        Node secondStatementNumberNode = secondStatementNode.getFirstChild();

        assertEquals(Token.NAME, firstStatementNameNode.getType());
        assertEquals("foo", firstStatementNameNode.getString());
        assertEquals(Token.NUMBER, secondStatementNumberNode.getType());
        assertEquals("2", secondStatementNumberNode.getString());
    }

    @Test
    public void testNodeRemoved_Method() {
        RemoveConstantExpressions.ReportCodeHasChangedListener listener = new RemoveConstantExpressions.ReportCodeHasChangedListener(new RemoveConstantExpressions.RemoveConstantRValuesCallback.Result());
        Node node = new Node(Token.NAME, "foo");
        
        listener.nodeRemoved(node);

        // Verifying that the changed flag is set to true
        assertTrue(listener.result.changed);
    }
  
}