
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UnionContextTest {

    private EvalContext parentContext;
    private EvalContext context1;
    private EvalContext context2;
    private EvalContext context3;
    private UnionContext unionContext;

    @Before
    public void setup() {
        parentContext = mock(EvalContext.class);
        context1 = mock(EvalContext.class);
        context2 = mock(EvalContext.class);
        context3 = mock(EvalContext.class);
        unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2, context3});
    }

    // Test getDocumentOrder() method

    @Test
    public void testGetDocumentOrder_ReturnsOne_WhenContextsLengthGreaterThanOne() {
        when(context1.getDocumentOrder()).thenReturn(4);
        when(context2.getDocumentOrder()).thenReturn(6);
        when(context3.getDocumentOrder()).thenReturn(2);

        int result = unionContext.getDocumentOrder();

        assertEquals(1, result);
    }

    @Test
    public void testGetDocumentOrder_ReturnsResultFromSuper_WhenContextsLengthEqualsOne() {
        when(parentContext.getDocumentOrder()).thenReturn(3);

        int result = unionContext.getDocumentOrder();

        assertEquals(3, result);
    }

    // Test setPosition() method

    @Test
    public void testSetPosition_ReturnsTrue_WhenPreparedIsFalse() {
        unionContext.setPosition(5);

        boolean result = unionContext.setPosition(10);

        assertEquals(true, result);
    }

    @Test
    public void testSetPosition_ReturnsResultFromSuper_WhenPreparedIsTrue() {
        unionContext.setPosition(5);
        unionContext.setPosition(10);

        boolean result = unionContext.setPosition(15);

        assertEquals(false, result);
    }

    @Test
    public void testSetPosition_PreparedIsTrue_WhenCalledOnce() {
        unionContext.setPosition(5);

        unionContext.setPosition(10);

        assertEquals(true, unionContext.isPrepared());
    }

    @Test
    public void testSetPosition_NodeSetContainsNodePointersFromAllContexts_WhenCalled() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        unionContext.setNodeSet(nodeSet);

        NodePointer pointer1 = mock(NodePointer.class);
        NodePointer pointer2 = mock(NodePointer.class);
        NodePointer pointer3 = mock(NodePointer.class);
        ArrayList<NodePointer> pointers1 = new ArrayList<>();
        pointers1.add(pointer1);
        ArrayList<NodePointer> pointers2 = new ArrayList<>();
        pointers2.add(pointer2);
        pointers2.add(pointer3);
        when(context1.getCurrentNodePointer()).thenReturn(pointer1);
        when(context2.getCurrentNodePointer()).thenReturn(pointer2);
        when(context3.getCurrentNodePointer()).thenReturn(pointer3);
        when(pointer1.isChildOf(any())).thenReturn(false);
        when(pointer2.isChildOf(any())).thenReturn(false);
        when(pointer3.isChildOf(any())).thenReturn(false);
        when(pointer1.compress()).thenReturn(pointer1);
        when(pointer2.compress()).thenReturn(pointer2);
        when(pointer3.compress()).thenReturn(pointer3);
        when(pointer1.getName()).thenReturn("node1");
        when(pointer2.getName()).thenReturn("node2");
        when(pointer3.getName()).thenReturn("node3");

        unionContext.setPosition(1);

        assertEquals(3, nodeSet.size());
        assertEquals(pointers1, nodeSet.getPointers());
        assertEquals(pointer1, nodeSet.get(0));
        assertEquals(pointer2, nodeSet.get(1));
        assertEquals(pointer3, nodeSet.get(2));
    }

    // Edge cases and error scenarios

    @Test
    public void testGetDocumentOrder_ReturnsZero_WhenContextsLengthIsZero() {
        UnionContext emptyUnionContext = new UnionContext(parentContext, new EvalContext[0]);

        int result = emptyUnionContext.getDocumentOrder();

        assertEquals(0, result);
    }

    @Test
    public void testSetPosition_ReturnsFalse_WhenPositionIsNegative() {
        boolean result = unionContext.setPosition(-10);

        assertEquals(false, result);
    }

    @Test
    public void testSetPosition_NodeSetIsEmpty_WhenAllContextsHaveEmptyNodeSets() {
        BasicNodeSet nodeSet = new BasicNodeSet();
        unionContext.setNodeSet(nodeSet);

        when(context1.nextSet()).thenReturn(false);
        when(context2.nextSet()).thenReturn(false);
        when(context3.nextSet()).thenReturn(false);

        unionContext.setPosition(1);

        assertEquals(0, nodeSet.size());
    }
}

