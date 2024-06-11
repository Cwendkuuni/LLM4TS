import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RemoveConstantExpressionsTest {

    private RemoveConstantExpressions underTest;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        underTest = new RemoveConstantExpressions(compiler);
    }

    @Test
    public void testProcess_WhenExprResultNode_ThenSideEffectNodesReplaced() {
        // Arrange
        Node externs = new Node(Token.EXTERN_RESULT);
        Node root = new Node(Token.ROOT);
        Node exprResultNode = new Node(Token.EXPR_RESULT);
        Node exprBody = new Node(Token.PLUS);
        exprBody.addChildToBack(new Node(Token.NUMBER));
        exprBody.addChildToBack(new Node(Token.CALL));
        exprResultNode.addChildToBack(exprBody);
        root.addChildToBack(exprResultNode);

        // Act
        underTest.process(externs, root);

        // Assert
        // The EXPR_RESULT node should be replaced with side effect nodes
        assertEquals(Token.EXPR_RESULT, exprResultNode.getType());
        assertEquals(2, exprResultNode.getChildCount());
        assertEquals(Token.CALL, exprResultNode.getFirstChild().getType());
    }

    @Test
    public void testProcess_WhenExprResultNodeWithSideEffects_ThenNoChange() {
        // Arrange
        Node externs = new Node(Token.EXTERN_RESULT);
        Node root = new Node(Token.ROOT);
        Node exprResultNode = new Node(Token.EXPR_RESULT);
        Node exprBody = new Node(Token.PLUS);
        exprBody.addChildToBack(new Node(Token.CALL));
        exprBody.addChildToBack(new Node(Token.NUMBER));
        exprResultNode.addChildToBack(exprBody);
        root.addChildToBack(exprResultNode);

        // Act
        underTest.process(externs, root);

        // Assert
        // The EXPR_RESULT node should not be changed
        assertEquals(Token.EXPR_RESULT, exprResultNode.getType());
        assertEquals(1, exprResultNode.getChildCount()); // No change in child node
    }

    @Test
    public void testProcess_WhenNonExprResultNode_ThenNoChange() {
        // Arrange
        Node externs = new Node(Token.EXTERN_RESULT);
        Node root = new Node(Token.ROOT);
        Node nonExprResultNode = new Node(Token.ASSIGN);
        root.addChildToBack(nonExprResultNode);

        // Act
        underTest.process(externs, root);

        // Assert
        // The non-EXPR_RESULT node should not be changed
        assertEquals(Token.ASSIGN, nonExprResultNode.getType());
    }

    @Test
    public void testTrySimplify_WhenNodeIsNotExprResult_ThenNoChange() {
        // Arrange
        Node parent = new Node(Token.ASSIGN);
        Node node = new Node(Token.CALL);

        // Act
        underTest.trySimplify(parent, node);

        // Assert
        // The non-EXPR_RESULT node should not be changed
        assertEquals(Token.ASSIGN, parent.getType());
    }

    @Test
    public void testTrySimplify_WhenNodeIsExprResultButNoSideEffectPresent_ThenNoChange() {
        // Arrange
        Node parent = new Node(Token.EXPR_RESULT);
        Node node = new Node(Token.CALL);
        parent.addChildToBack(node);

        // Act
        underTest.trySimplify(parent, node);

        // Assert
        // The node should not be changed as there are no side effects present
        assertEquals(Token.CALL, parent.getFirstChild().getType());
    }

    @Test
    public void testTrySimplify_WhenNodeIsExprResultAndSideEffectPresent_ThenReplacedWithSideEffectNodes() {
        // Arrange
        Node parent = new Node(Token.EXPR_RESULT);
        Node node = new Node(Token.CALL);
        parent.addChildToBack(node);

        // Act
        underTest.trySimplify(parent, node);

        // Assert
        // The node should be replaced with side effect nodes
        assertEquals(Token.EXPR_RESULT, parent.getType());
        assertEquals(1, parent.getChildCount());
    }

    @Test
    public void testGetSideEffectNodes_WhenSingleSubexpressionWithSideEffect_ThenReturnsReplacementNodes() {
        // Arrange
        Node exprBody = new Node(Token.PLUS);
        exprBody.addChildToBack(new Node(Token.CALL));
        List<Node> expectedResult = new ArrayList<>();
        expectedResult.add(new Node(Token.EXPR_RESULT, new Node(Token.CALL)));

        // Act
        List<Node> result = underTest.getSideEffectNodes(exprBody);

        // Assert
        // Only the subexpression with side effect should be returned
        assertEquals(1, result.size());
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetSideEffectNodes_WhenMultipleSubexpressionsWithSideEffects_ThenReturnsReplacementNodes() {
        // Arrange
        Node exprBody = new Node(Token.PLUS);
        exprBody.addChildToBack(new Node(Token.CALL));
        exprBody.addChildToBack(new Node(Token.STRING));
        exprBody.addChildToBack(new Node(Token.DELETE));
        List<Node> expectedResult = new ArrayList<>();
        expectedResult.add(new Node(Token.EXPR_RESULT, new Node(Token.CALL)));
        expectedResult.add(new Node(Token.EXPR_RESULT, new Node(Token.DELETE)));

        // Act
        List<Node> result = underTest.getSideEffectNodes(exprBody);

        // Assert
        // All subexpressions with side effects should be returned
        assertEquals(2, result.size());
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetResult_WhenCalled_ThenReturnsResult() {
        // Arrange
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback =
                underTest.new RemoveConstantRValuesCallback();
        RemoveConstantExpressions.Result expectedResult = new RemoveConstantExpressions.Result();

        // Act
        RemoveConstantExpressions.Result result = callback.getResult();

        // Assert
        assertEquals(expectedResult, result);
    }
}