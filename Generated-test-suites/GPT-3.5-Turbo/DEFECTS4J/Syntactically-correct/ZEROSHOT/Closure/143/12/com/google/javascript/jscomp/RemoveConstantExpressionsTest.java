import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class RemoveConstantExpressionsTest {

    private RemoveConstantExpressions removeConstantExpressions;
    private AbstractCompiler compiler;
    private Node externs;
    private Node root;

    @Before
    public void setUp() {
        compiler = Mockito.mock(AbstractCompiler.class);
        removeConstantExpressions = new RemoveConstantExpressions(compiler);
        externs = new Node(Token.EXTERN_RESULT);
        root = new Node(Token.ROOT);
    }

    @Test
    public void testProcess() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback cb = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
        Node node = new Node(Token.EXPR_RESULT);
        Node parent = new Node(Token.ROOT);
        cb.visit(traversal, node, parent);
        verify(traversal, times(1)).traverse(null, node, parent);
        verifyNoMoreInteractions(traversal);
    }

    @Test
    public void testTrySimplify_nonExprResultNode_noChanges() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback cb = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        Node parent = new Node(Token.ROOT);
        Node node = new Node(Token.NAME);
        cb.trySimplify(parent, node);
        verifyNoChanges();
    }

    @Test
    public void testTrySimplify_exprResultNode_noSideEffectNodes_noChanges() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback cb = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        Node parent = new Node(Token.ROOT);
        Node node = new Node(Token.EXPR_RESULT);
        Node exprBody = new Node(Token.ADD, new Node(Token.NUMBER, 1), new Node(Token.NAME, "foo"), new Node(Token.NAME, "bar"));
        node.addChildToFront(exprBody);
        cb.trySimplify(parent, node);
        verifyNoChanges();
    }

    @Test
    public void testTrySimplify_exprResultNode_sideEffectNodes_replaceWithSideEffectNodes() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback cb = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        RemoveConstantExpressions.RemoveConstantRValuesCallback spyCb = spy(cb);
        Node parent = new Node(Token.ROOT);
        Node node = new Node(Token.EXPR_RESULT);
        Node exprBody = new Node(Token.CALL, new Node(Token.NAME, "foo"));
        node.addChildToFront(exprBody);
        Node replacementNode = new Node(Token.EXPR_RESULT, new Node(Token.NAME, "foo"));
        doReturn(replacementNode).when(spyCb).getSideEffectNodes(any(Node.class));
        spyCb.trySimplify(parent, node);
        verify(spyCb, times(1)).getSideEffectNodes(exprBody);
        verify(parent, times(1)).replaceChild(node, replacementNode);
        verify(compiler, times(1)).reportChange();
    }

    @Test
    public void testGetSideEffectNodes_emptyNode_noSideEffectNodes() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback cb = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        Node node = new Node(Token.EMPTY);
        assertTrue(cb.getSideEffectNodes(node).isEmpty());
    }

    @Test
    public void testGetSideEffectNodes_nodeWithSideEffects_sideEffectNodes() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback cb = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        Node node = new Node(Token.ASSIGN, new Node(Token.NAME, "a"), new Node(Token.NAME, "b"));
        List<Node> sideEffectNodes = cb.getSideEffectNodes(node);
        assertEquals(1, sideEffectNodes.size());
        assertEquals(Token.EXPR_RESULT, sideEffectNodes.get(0).getType());
        assertEquals(Token.ASSIGN, sideEffectNodes.get(0).getFirstChild().getType());
        assertEquals(Token.NAME, sideEffectNodes.get(0).getFirstChild().getFirstChild().getType());
        assertEquals("a", sideEffectNodes.get(0).getFirstChild().getFirstChild().getString());
        assertEquals(Token.NAME, sideEffectNodes.get(0).getFirstChild().getLastChild().getType());
        assertEquals("b", sideEffectNodes.get(0).getFirstChild().getLastChild().getString());
    }

    private void verifyNoChanges() {
        verifyZeroInteractions(compiler);
    }
}