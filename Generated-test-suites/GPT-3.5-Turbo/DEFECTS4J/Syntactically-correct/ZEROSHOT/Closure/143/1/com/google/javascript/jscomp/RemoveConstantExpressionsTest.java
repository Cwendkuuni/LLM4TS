import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RemoveConstantExpressionsTest {
    
    private AbstractCompiler mockCompiler;
    private Node externs;
    private Node root;
    
    @Before
    public void setUp() {
        mockCompiler = Mockito.mock(AbstractCompiler.class);
        externs = new Node(Token.EXTERN_RESULT);
        root = new Node(Token.ROOT);
    }
    
    @Test
    public void testProcess() {
        RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(mockCompiler);
        removeConstantExpressions.process(externs, root);
        verify(mockCompiler).process(externs, root);
    }
    
    @Test
    public void testRemoveConstantRValuesCallback() {
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        assertNotNull(callback.getResult());
    }
    
    @Test
    public void testVisit() {
        NodeTraversal traversal = Mockito.mock(NodeTraversal.class);
        Node parent = new Node(Token.EXPR_RESULT);
        Node node = new Node(Token.EXPR_RESULT);
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        callback.visit(traversal, node, parent);
        assertEquals(parent.getType(), node.getType());
    }
    
    @Test
    public void testTrySimplify_noSideEffects() {
        Node parent = new Node(Token.EXPR_RESULT);
        Node node = new Node(Token.EXPR_RESULT);
        Node exprBody = new Node(Token.EMPTY);
        node.addChildToBack(exprBody);
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        callback.trySimplify(parent, node);
        assertEquals(exprBody, parent.getFirstChild());
    }
    
    @Test
    public void testTrySimplify_withSideEffects() {
        Node parent = new Node(Token.EXPR_RESULT);
        Node node = new Node(Token.EXPR_RESULT);
        Node exprBody = new Node(Token.GETPROP);
        exprBody.addChildToFront(new Node(Token.NAME, "foo"));
        exprBody.addChildToBack(new Node(Token.STRING, "bar"));
        node.addChildToBack(exprBody);
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        callback.trySimplify(parent, node);
        assertNotEquals(exprBody, parent.getFirstChild());
    }
    
    @Test
    public void testGetSideEffectNodes() {
        List<Node> subexpressions = new ArrayList<>();
        Node node1 = new Node(Token.NAME, "foo");
        Node node2 = new Node(Token.STRING, "bar");
        subexpressions.add(node1);
        subexpressions.add(node2);
        RemoveConstantExpressions.RemoveConstantRValuesCallback callback = new RemoveConstantExpressions.RemoveConstantRValuesCallback();
        List<Node> replacements = callback.getSideEffectNodes(subexpressions);
        assertEquals(replacements.size(), subexpressions.size());
        assertEquals(replacements.get(0).getType(), Token.EXPR_RESULT);
        assertEquals(replacements.get(1).getType(), Token.EXPR_RESULT);
    }
    
    @Test
    public void testReportCodeHasChangedListener() {
        RemoveConstantExpressions.ReportCodeHasChangedListener listener = new RemoveConstantExpressions.ReportCodeHasChangedListener(new RemoveConstantExpressions.RemoveConstantRValuesCallback().getResult());
        Node node = new Node(Token.NAME, "foo");
        listener.nodeRemoved(node);
        assertTrue(listener.result.isChanged());
    }
}