import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class UnionContextTest {
    private EvalContext parentContext;
    private EvalContext[] contexts;

    @Before
    public void setUp() {
        parentContext = mock(EvalContext.class);
        contexts = new EvalContext[] {mock(EvalContext.class), mock(EvalContext.class)};
    }

    @Test
    public void testConstructor() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        // Verify parent context is set correctly
        assertEquals(parentContext, unionContext.getParentContext());

        // Verify node set is an instance of BasicNodeSet
        assertTrue(unionContext.getNodeSet() instanceof BasicNodeSet);

        // Verify contexts are set correctly
        assertEquals(contexts, unionContext.getContexts());
    }

    @Test
    public void testGetDocumentOrderWithMultipleContexts() {
        when(parentContext.getDocumentOrder()).thenReturn(0);
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        // Verify getDocumentOrder returns 1 when there are multiple contexts
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testGetDocumentOrderWithSingleContext() {
        when(parentContext.getDocumentOrder()).thenReturn(0);
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] {mock(EvalContext.class)});

        // Verify getDocumentOrder returns the value of the parent context when there is a single context
        assertEquals(0, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        UnionContext unionContext = new UnionContext(parentContext, contexts);

        // Verify setPosition returns the value of super.setPosition
        boolean result = unionContext.setPosition(1);
        assertTrue(result);

        // Verify that the contexts are prepared only once
        unionContext.setPosition(1);
        verify(contexts[0], times(1)).nextSet();
        verify(contexts[1], times(1)).nextSet();

        // Verify that the node set contains the correct pointers
        BasicNodeSet nodeSet = (BasicNodeSet) unionContext.getNodeSet();
        verify(contexts[0], times(2)).nextNode();
        verify(contexts[0], times(2)).getCurrentNodePointer();
        verify(contexts[1], times(2)).nextNode();
        verify(contexts[1], times(2)).getCurrentNodePointer();
        assertEquals(2, nodeSet.size());
        assertTrue(nodeSet.contains(contexts[0].getCurrentNodePointer()));
        assertTrue(nodeSet.contains(contexts[1].getCurrentNodePointer()));
    }
}