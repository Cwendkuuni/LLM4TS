import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class MinimizeExitPointsTest {

    private MinimizeExitPoints minimizeExitPoints;
    private AbstractCompiler abstractCompiler;

    @Before
    public void setUp() {
        abstractCompiler = mock(AbstractCompiler.class);
        minimizeExitPoints = new MinimizeExitPoints(abstractCompiler);
    }

    @Test
    public void testProcess_WithValidInput_ExecuteCorrectly() {
        Node externs = mock(Node.class);
        Node root = mock(Node.class);
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);

        when(abstractCompiler.reportCodeChange()).thenReturn(true);
        when(abstractCompiler.traverse(eq(root), any(AbstractPostOrderCallback.class))).thenReturn(nodeTraversal);

        minimizeExitPoints.process(externs, root);

        verify(abstractCompiler).traverse(eq(root), any(AbstractPostOrderCallback.class));
        verify(nodeTraversal).traverse(eq(root));
        verify(abstractCompiler).reportCodeChange();
    }

    @Test
    public void testVisit_WithLabelNodeAndMatchingExitType_RemovesExitNode() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node parent = mock(Node.class);
        Node labelNode = mock(Node.class);
        Node lastChild = mock(Node.class);

        when(labelNode.getLastChild()).thenReturn(lastChild);
        when(labelNode.getFirstChild()).thenReturn(mock(Node.class));
        when(labelNode.getFirstChild().getString()).thenReturn("myLabel");
        when(lastChild.getType()).thenReturn(Token.BREAK);
        when(lastChild.hasChildren()).thenReturn(false);
        when(parent.getParent()).thenReturn(mock(Node.class));

        minimizeExitPoints.visit(nodeTraversal, labelNode, parent);

        verify(lastChild.getParent()).removeChild(eq(lastChild));
        verify(abstractCompiler).reportCodeChange();
    }

    @Test
    public void testVisit_WithLoopNodesAndMatchingExitType_RemovesExitNode() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node parent = mock(Node.class);
        Node loopNode = mock(Node.class);
        Node loopCodeBlock = mock(Node.class);

        when(NodeUtil.getLoopCodeBlock(eq(loopNode))).thenReturn(loopCodeBlock);
        when(loopCodeBlock.getLastChild()).thenReturn(mock(Node.class));
        when(loopCodeBlock.getLastChild().getType()).thenReturn(Token.CONTINUE);

        minimizeExitPoints.visit(nodeTraversal, loopNode, parent);

        verify(loopCodeBlock).removeChild(eq(loopCodeBlock.getLastChild()));
        verify(abstractCompiler).reportCodeChange();
    }

    @Test
    public void testVisit_WithDoNodeAndMatchingExitTypeWithFalseCondition_RemovesExitNode() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node parent = mock(Node.class);
        Node doNode = mock(Node.class);
        Node conditionExpression = mock(Node.class);
        Node firstChild = mock(Node.class);

        when(NodeUtil.getConditionExpression(eq(doNode))).thenReturn(conditionExpression);
        when(NodeUtil.getImpureBooleanValue(eq(conditionExpression))).thenReturn(TernaryValue.FALSE);
        when(doNode.getFirstChild()).thenReturn(firstChild);
        when(firstChild.getType()).thenReturn(Token.BREAK);
        when(firstChild.hasChildren()).thenReturn(false);

        minimizeExitPoints.visit(nodeTraversal, doNode, parent);

        verify(firstChild.getParent()).removeChild(eq(firstChild));
        verify(abstractCompiler).reportCodeChange();
    }

    @Test
    public void testVisit_WithFunctionNode_RemovesExitNode() {
        NodeTraversal nodeTraversal = mock(NodeTraversal.class);
        Node parent = mock(Node.class);
        Node functionNode = mock(Node.class);

        Node lastChild = mock(Node.class);
        when(functionNode.getLastChild()).thenReturn(lastChild);
        when(lastChild.getType()).thenReturn(Token.RETURN);
        when(lastChild.hasChildren()).thenReturn(false);

        minimizeExitPoints.visit(nodeTraversal, functionNode, parent);

        verify(lastChild.getParent()).removeChild(eq(lastChild));
        verify(abstractCompiler).reportCodeChange();
    }
}