import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RemoveConstantExpressionsTest {

    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
    }

    @Test
    public void testProcess_singleExpressionWithSideEffects_rewritesToSideEffectExpressions() {
        // Arrange
        Node externs = new Node();
        Node root = new Node();
        RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
        Node parent = new Node(Token.BLOCK);
        Node expression = new Node(Token.EXPR_RESULT);
        Node sideEffect1 = new Node(Token.SCRIPT);
        Node sideEffect2 = new Node(Token.THROW);
        expression.addChildToBack(sideEffect1);
        expression.addChildToBack(sideEffect2);

        parent.addChildToBack(expression);
        root.addChildToBack(parent);

        // Act
        pass.process(externs, root);

        // Assert
        assertFalse(compiler.hasCodeChanged());
        assertEquals(2, parent.getChildCount());
        assertEquals(sideEffect1, parent.getFirstChild());
        assertEquals(sideEffect2, parent.getLastChild());
    }

    @Test
    public void testProcess_singleExpressionWithoutSideEffects_doesNotRewrite() {
        // Arrange
        Node externs = new Node();
        Node root = new Node();
        RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
        Node parent = new Node(Token.BLOCK);
        Node expression = new Node(Token.EXPR_RESULT);
        Node constant1 = new Node(Token.STRING);
        Node constant2 = new Node(Token.NUMBER);
        expression.addChildToBack(constant1);
        expression.addChildToBack(constant2);

        parent.addChildToBack(expression);
        root.addChildToBack(parent);

        // Act
        pass.process(externs, root);

        // Assert
        assertFalse(compiler.hasCodeChanged());
        assertEquals(1, parent.getChildCount());
        assertEquals(expression, parent.getFirstChild());
    }

    @Test
    public void testProcess_singleExpressionWithNestedSideEffects_rewritesToNestedSideEffectExpressions() {
        // Arrange
        Node externs = new Node();
        Node root = new Node();
        RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
        Node parent = new Node(Token.BLOCK);
        Node expression = new Node(Token.EXPR_RESULT);
        Node nestedExpression = new Node(Token.EXPR_RESULT);
        Node sideEffect1 = new Node(Token.SCRIPT);
        Node sideEffect2 = new Node(Token.THROW);
        nestedExpression.addChildToBack(sideEffect1);
        nestedExpression.addChildToBack(sideEffect2);
        expression.addChildToBack(nestedExpression);

        parent.addChildToBack(expression);
        root.addChildToBack(parent);

        // Act
        pass.process(externs, root);

        // Assert
        assertFalse(compiler.hasCodeChanged());
        assertEquals(2, parent.getChildCount());
        assertEquals(nestedExpression, parent.getFirstChild());
        assertEquals(sideEffect1, nestedExpression.getFirstChild());
        assertEquals(sideEffect2, nestedExpression.getLastChild());
    }

    @Test
    public void testProcess_multipleExpressionsWithMixedSideEffects_rewritesToSideEffectExpressions() {
        // Arrange
        Node externs = new Node();
        Node root = new Node();
        RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
        Node parent = new Node(Token.BLOCK);
        Node expression1 = new Node(Token.EXPR_RESULT);
        Node expression2 = new Node(Token.EXPR_RESULT);
        Node sideEffect1 = new Node(Token.SCRIPT);
        Node sideEffect2 = new Node(Token.THROW);
        Node constant = new Node(Token.STRING);

        expression1.addChildToBack(sideEffect1);
        expression2.addChildToBack(constant);
        expression2.addChildToBack(sideEffect2);

        parent.addChildToBack(expression1);
        parent.addChildToBack(expression2);
        root.addChildToBack(parent);

        // Act
        pass.process(externs, root);

        // Assert
        assertFalse(compiler.hasCodeChanged());
        assertEquals(3, parent.getChildCount());
        assertEquals(sideEffect1, parent.getFirstChild());
        assertEquals(constant, parent.getChildAtIndex(1));
        assertEquals(sideEffect2, parent.getLastChild());
    }

    @Test
    public void testRemoveConstantRValuesCallback_visit_singleExpressionWithSideEffects_replacesWithSideEffectExpressions() {
        // Arrange
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback =
                new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        NodeTraversal traversal = new NodeTraversal(compiler, callback);
        Node parent = new Node(Token.BLOCK);
        Node expression = new Node(Token.EXPR_RESULT);
        Node sideEffect1 = new Node(Token.SCRIPT);
        Node sideEffect2 = new Node(Token.THROW);
        expression.addChildToBack(sideEffect1);
        expression.addChildToBack(sideEffect2);

        parent.addChildToBack(expression);

        // Act
        callback.visit(traversal, expression, parent);

        // Assert
        assertEquals(2, parent.getChildCount());
        assertEquals(sideEffect1, parent.getFirstChild());
        assertEquals(sideEffect2, parent.getLastChild());
    }

    @Test
    public void testRemoveConstantRValuesCallback_visit_singleExpressionWithoutSideEffects_doesNotReplace() {
        // Arrange
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback =
                new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        NodeTraversal traversal = new NodeTraversal(compiler, callback);
        Node parent = new Node(Token.BLOCK);
        Node expression = new Node(Token.EXPR_RESULT);
        Node constant1 = new Node(Token.STRING);
        Node constant2 = new Node(Token.NUMBER);
        expression.addChildToBack(constant1);
        expression.addChildToBack(constant2);

        parent.addChildToBack(expression);

        // Act
        callback.visit(traversal, expression, parent);

        // Assert
        assertEquals(1, parent.getChildCount());
        assertEquals(expression, parent.getFirstChild());
    }

    @Test
    public void testRemoveConstantRValuesCallback_visit_singleExpressionWithNestedSideEffects_replacesWithNestedSideEffectExpressions() {
        // Arrange
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback =
                new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        NodeTraversal traversal = new NodeTraversal(compiler, callback);
        Node parent = new Node(Token.BLOCK);
        Node expression = new Node(Token.EXPR_RESULT);
        Node nestedExpression = new Node(Token.EXPR_RESULT);
        Node sideEffect1 = new Node(Token.SCRIPT);
        Node sideEffect2 = new Node(Token.THROW);
        nestedExpression.addChildToBack(sideEffect1);
        nestedExpression.addChildToBack(sideEffect2);
        expression.addChildToBack(nestedExpression);

        parent.addChildToBack(expression);

        // Act
        callback.visit(traversal, expression, parent);

        // Assert
        assertEquals(2, parent.getChildCount());
        assertEquals(nestedExpression, parent.getFirstChild());
        assertEquals(sideEffect1, nestedExpression.getFirstChild());
        assertEquals(sideEffect2, nestedExpression.getLastChild());
    }

    @Test
    public void testRemoveConstantRValuesCallback_visit_multipleExpressionsWithMixedSideEffects_replacesWithSideEffectExpressions() {
        // Arrange
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback =
                new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        NodeTraversal traversal = new NodeTraversal(compiler, callback);
        Node parent = new Node(Token.BLOCK);
        Node expression1 = new Node(Token.EXPR_RESULT);
        Node expression2 = new Node(Token.EXPR_RESULT);
        Node sideEffect1 = new Node(Token.SCRIPT);
        Node sideEffect2 = new Node(Token.THROW);
        Node constant = new Node(Token.STRING);

        expression1.addChildToBack(sideEffect1);
        expression2.addChildToBack(constant);
        expression2.addChildToBack(sideEffect2);

        parent.addChildToBack(expression1);
        parent.addChildToBack(expression2);

        // Act
        callback.visit(traversal, expression1, parent);
        callback.visit(traversal, expression2, parent);

        // Assert
        assertEquals(3, parent.getChildCount());
        assertEquals(sideEffect1, parent.getFirstChild());
        assertEquals(constant, parent.getChildAtIndex(1));
        assertEquals(sideEffect2, parent.getLastChild());
    }

    @Test
    public void testRemoveConstantRValuesCallback_trySimplify_nodeIsNotExprResult_doesNothing() {
        // Arrange
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback =
                new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        Node parent = new Node(Token.BLOCK);
        Node node = new Node(Token.VAR);

        // Act
        callback.trySimplify(parent, node);

        // Assert
        assertEquals(0, parent.getChildCount());
    }

    @Test
    public void testRemoveConstantRValuesCallback_trySimplify_nodeWithSideEffects_replacesWithSideEffectExpressions() {
        // Arrange
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback =
                new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        Node parent = new Node(Token.BLOCK);
        Node node = new Node(Token.EXPR_RESULT);
        Node sideEffect1 = new Node(Token.SCRIPT);
        Node sideEffect2 = new Node(Token.THROW);
        node.addChildToBack(sideEffect1);
        node.addChildToBack(sideEffect2);

        // Act
        callback.trySimplify(parent, node);

        // Assert
        assertEquals(2, parent.getChildCount());
        assertEquals(sideEffect1, parent.getFirstChild());
        assertEquals(sideEffect2, parent.getLastChild());
    }

    @Test
    public void testRemoveConstantRValuesCallback_trySimplify_nodeWithoutSideEffects_doesNotReplace() {
        // Arrange
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback =
                new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        Node parent = new Node(Token.BLOCK);
        Node node = new Node(Token.EXPR_RESULT);
        Node constant1 = new Node(Token.STRING);
        Node constant2 = new Node(Token.NUMBER);
        node.addChildToBack(constant1);
        node.addChildToBack(constant2);

        // Act
        callback.trySimplify(parent, node);

        // Assert
        assertEquals(1, parent.getChildCount());
        assertEquals(node, parent.getFirstChild());
    }

    @Test
    public void testRemoveConstantRValuesCallback_getSideEffectNodes_collectsSideEffectSubExpressions() {
        // Arrange
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback =
                new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        Node expression = new Node(Token.ADD);
        Node sideEffect1 = new Node(Token.SCRIPT);
        Node constant = new Node(Token.NUMBER);
        Node sideEffect2 = new Node(Token.THROW);
        expression.addChildToBack(sideEffect1);
        expression.addChildToBack(constant);
        expression.addChildToBack(sideEffect2);

        // Act
        List<Node> sideEffectNodes = callback.getSideEffectNodes(expression);

        // Assert
        assertEquals(3, sideEffectNodes.size());
        assertEquals(sideEffect1, sideEffectNodes.get(0));
        assertEquals(constant, sideEffectNodes.get(1));
        assertEquals(sideEffect2, sideEffectNodes.get(2));
    }

    @Test
    public void testRemoveConstantRValuesCallback_getResult_returnsResultObject() {
        // Arrange
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback =
                new RemoveConstantExpressions.RemoveConstantRValuesCallback();

        // Act
        RemoveConstantExpressions.Result result = callback.getResult();

        // Assert
        assertNotNull(result);
        assertFalse(result.isChange());
    }

    @Test
    public void testReportCodeHasChangedListener_nodeRemoved_changesResultToTrue() {
        // Arrange
        RemoveConstantExpressions.Result result = new RemoveConstantExpressions.Result();
        RemoveConstantExpressions.ReportCodeHasChangedListener listener =
                new RemoveConstantExpressions.ReportCodeHasChangedListener(result);

        // Act
        listener.nodeRemoved(new Node());

        // Assert
        assertTrue(result.isChange());
    }
}