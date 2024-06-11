import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class RemoveConstantExpressionsTest {
    private RemoveConstantExpressions removeConstantExpressions;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        this.compiler = Mockito.mock(AbstractCompiler.class);
        this.removeConstantExpressions = new RemoveConstantExpressions(this.compiler);
    }

    @After
    public void tearDown() {
        this.compiler = null;
        this.removeConstantExpressions = null;
    }

    @Test
    public void testProcess_OneExprResultNode_ReplacementNodesAdded() {
        // Arrange
        Node externs = new Node(Token.EXTERNs);
        Node root = new Node(Token.ROOT);
        RemoveConstantExpressions.RemoveConstantRValuesCallback cb = this.removeConstantExpressions.new RemoveConstantRValuesCallback();

        // Creating the input exprBody node
        Node exprBody = new Node(Token.FUNCTION, new Node(Token.NAME, "dummyFunction"), new Node(Token.EMPTY), new Node(Token.BLOCK));
        Node exprNode = new Node(Token.EXPR_RESULT, exprBody);
        root.addChildToBack(exprNode);

        // Creating the expected replacement nodes
        Node sideEffectNode = new Node(Token.EXPR_RESULT, exprBody);

        // Act
        this.removeConstantExpressions.process(externs, root);

        // Assert
        Node expectedRoot = new Node(Token.ROOT);
        expectedRoot.addChildToBack(sideEffectNode);

        assertEquals(expectedRoot, root);
    }

    @Test
    public void testProcess_MultipleExprResultNodes_NodesWithSideEffectsReplaced() {
        // Arrange
        Node externs = new Node(Token.EXTERNs);
        Node root = new Node(Token.ROOT);
        RemoveConstantExpressions.RemoveConstantRValuesCallback cb = this.removeConstantExpressions.new RemoveConstantRValuesCallback();

        // Creating the input exprBody nodes
        Node exprBody1 = new Node(Token.FUNCTION, new Node(Token.NAME, "dummyFunction1"), new Node(Token.EMPTY), new Node(Token.BLOCK));
        Node exprBody2 = new Node(Token.FUNCTION, new Node(Token.NAME, "dummyFunction2"), new Node(Token.EMPTY), new Node(Token.BLOCK));
        Node exprNode1 = new Node(Token.EXPR_RESULT, exprBody1);
        Node exprNode2 = new Node(Token.EXPR_RESULT, exprBody2);
        root.addChildToBack(exprNode1);
        root.addChildToBack(exprNode2);

        // Creating the expected replacement nodes
        Node sideEffectNode1 = new Node(Token.EXPR_RESULT, exprBody1);
        Node sideEffectNode2 = new Node(Token.EXPR_RESULT, exprBody2);

        // Act
        this.removeConstantExpressions.process(externs, root);

        // Assert
        Node expectedRoot = new Node(Token.ROOT);
        expectedRoot.addChildToBack(sideEffectNode1);
        expectedRoot.addChildToBack(sideEffectNode2);

        assertEquals(expectedRoot, root);
    }

    @Test
    public void testProcess_NoExprResultNode_NodeStructureUnchanged() {
        // Arrange
        Node externs = new Node(Token.EXTERNs);
        Node root = new Node(Token.ROOT);
        RemoveConstantExpressions.RemoveConstantRValuesCallback cb = this.removeConstantExpressions.new RemoveConstantRValuesCallback();

        // Act
        this.removeConstantExpressions.process(externs, root);

        // Assert
        Node expectedRoot = new Node(Token.ROOT);

        assertEquals(expectedRoot, root);
    }
}