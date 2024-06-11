
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

public class UnionContextTest {
    
    private static final int NULL_POSITION = -1;
    private UnionContext unionContext;
    private EvalContext mockEvalContext;
    private BasicNodeSet mockNodeSet;
    private NodePointer mockNodePointer;
    
    @Before
    public void setUp() {
        mockEvalContext = mock(EvalContext.class);
        mockNodeSet = mock(BasicNodeSet.class);
        mockNodePointer = mock(NodePointer.class);
        unionContext = new UnionContext(mockEvalContext, new EvalContext[]{mockEvalContext});
        unionContext.setNodeSet(mockNodeSet);
    }
    
    @Test
    public void testGetDocumentOrder() {
        assertEquals(1, unionContext.getDocumentOrder());
        
        EvalContext[] multipleContexts = {mockEvalContext, mockEvalContext};
        UnionContext multipleUnionContext = new UnionContext(mockEvalContext, multipleContexts);
        assertEquals(1, multipleUnionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition_PreparedIsFalse_ReturnsFalse() {
        unionContext.prepared = false;
        
        assertFalse(unionContext.setPosition(0));
    }
    
    @Test
    public void testSetPosition_PreparedIsTrue_ReturnsTrue() {
        unionContext.prepared = true;
        
        assertTrue(unionContext.setPosition(0));
    }
    
    @Test
    public void testSetPosition_WithContexts_ReturnsTrue() {
        EvalContext[] multipleContexts = {mockEvalContext, mockEvalContext};
        UnionContext multipleUnionContext = new UnionContext(mockEvalContext, multipleContexts);
        multipleUnionContext.prepared = true;
        
        assertTrue(multipleUnionContext.setPosition(0));
    }
    
    @Test
    public void testSetPosition_AddsPointersToNodeSet() {
        ArrayList<NodePointer> mockPointers = new ArrayList<>();
        mockPointers.add(mockNodePointer);
        when(mockNodePointer.getNode()).thenReturn(new Object());
        when(mockNodePointer.getIndex()).thenReturn(1);
        when(mockEvalContext.getCurrentNodePointer()).thenReturn(mockNodePointer);
        when(mockEvalContext.nextSet()).thenReturn(true, false);
        when(mockEvalContext.nextNode()).thenReturn(true, false);
        
        unionContext.setPosition(0);
        
        verify(mockNodeSet, times(1)).add(mockNodePointer);
    }
    
    @Test
    public void testSetPosition_AddsEachPointerOnlyOnce() {
        ArrayList<NodePointer> mockPointers = new ArrayList<>();
        mockPointers.add(mockNodePointer);
        when(mockNodePointer.getNode()).thenReturn(new Object());
        when(mockNodePointer.getIndex()).thenReturn(1);
        when(mockEvalContext.getCurrentNodePointer()).thenReturn(mockNodePointer);
        when(mockEvalContext.nextSet()).thenReturn(true, false);
        when(mockEvalContext.nextNode()).thenReturn(true, false);
        
        unionContext.setPosition(0);
        unionContext.setPosition(1);
        
        verify(mockNodeSet, times(1)).add(mockNodePointer);
    }
    
    @Test
    public void testSetPosition_WithMultipleContexts_AddsPointersToNodeSet() {
        ArrayList<NodePointer> mockPointers = new ArrayList<>();
        mockPointers.add(mockNodePointer);
        when(mockNodePointer.getNode()).thenReturn(new Object());
        when(mockNodePointer.getIndex()).thenReturn(1);
        when(mockEvalContext.getCurrentNodePointer()).thenReturn(mockNodePointer);
        when(mockEvalContext.nextSet()).thenReturn(true, false);
        when(mockEvalContext.nextNode()).thenReturn(true, false);
        
        EvalContext[] multipleContexts = {mockEvalContext, mockEvalContext};
        UnionContext multipleUnionContext = new UnionContext(mockEvalContext, multipleContexts);
        multipleUnionContext.prepared = true;
        multipleUnionContext.setPosition(0);
        
        verify(mockNodeSet, times(2)).add(mockNodePointer);
    }
    
    @Test
    public void testSetPosition_WithMultipleContexts_AddsEachPointerOnlyOnce() {
        ArrayList<NodePointer> mockPointers = new ArrayList<>();
        mockPointers.add(mockNodePointer);
        when(mockNodePointer.getNode()).thenReturn(new Object());
        when(mockNodePointer.getIndex()).thenReturn(1);
        when(mockEvalContext.getCurrentNodePointer()).thenReturn(mockNodePointer);
        when(mockEvalContext.nextSet()).thenReturn(true, false);
        when(mockEvalContext.nextNode()).thenReturn(true, false);
        
        EvalContext[] multipleContexts = {mockEvalContext, mockEvalContext};
        UnionContext multipleUnionContext = new UnionContext(mockEvalContext, multipleContexts);
        multipleUnionContext.prepared = true;
        multipleUnionContext.setPosition(0);
        multipleUnionContext.setPosition(1);
        
        verify(mockNodeSet, times(2)).add(mockNodePointer);
    }
}
