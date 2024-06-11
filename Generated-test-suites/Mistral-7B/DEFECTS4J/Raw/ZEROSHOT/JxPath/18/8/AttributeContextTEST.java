package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AttributeContextTest {

    @Mock
    private EvalContext parentContext;
    @Mock
    private NodeTest nodeTest;
    @Mock
    private NodeIterator attributeIterator;
    @Mock
    private NodePointer attributeNodePointer;

    @InjectMocks
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAttributeContextConstructor() {
        assertEquals("Parent context should be set correctly", parentContext, attributeContext.getParentContext());
        assertEquals("Node test should be set correctly", nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testGetCurrentNodePointerWhenNoAttributeFound() {
        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
        when(nodeTest.isPredicate()).thenReturn(false);
        when(nodeTest.getNodeName().getLocalPart()).thenReturn("test");

        attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNull("Current node pointer should be null", attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testGetCurrentNodePointerWhenAttributeFound() {
        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
        when(nodeTest.isPredicate()).thenReturn(false);
        when(nodeTest.getNodeName().getLocalPart()).thenReturn("test");
        when(attributeIterator.hasNext()).thenReturn(true);
        when(attributeIterator.getNodePointer()).thenReturn(attributeNodePointer);

        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
        attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNotNull("Current node pointer should not be null", attributeContext.getCurrentNodePointer());
        assertEquals("Current node pointer should be the same as the one returned by attribute iterator", attributeNodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
        when(nodeTest.isPredicate()).thenReturn(false);
        when(nodeTest.getNodeName().getLocalPart()).thenReturn("test");

        attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.reset();

        assertFalse("SetStarted flag should be false after reset", attributeContext.isSetStarted());
        assertNull("Iterator should be null after reset", attributeContext.getIterator());
    }

    @Test
    public void testSetPositionWhenNoAttributeFound() {
        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
        when(nodeTest.isPredicate()).thenReturn(false);
        when(nodeTest.getNodeName().getLocalPart()).thenReturn("test");

        attributeContext.setPosition(0);
        assertEquals("Position should be 0", 0, attributeContext.getCurrentPosition());

        attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setPosition(-1);
        assertEquals("Position should still be 0", 0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPositionWhenAttributeFound() {
        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
        when(nodeTest.isPredicate()).thenReturn(false);
        when(nodeTest.getNodeName().getLocalPart()).thenReturn("test");
        when(attributeIterator.hasNext()).thenReturn(true);

        attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setPosition(1);
        assertEquals("Position should be 1", 1, attributeContext.getCurrentPosition());

        when(attributeIterator.hasNext()).thenReturn(false);
        attributeContext.setPosition(2);
        assertEquals("Position should still be 1", 1, attributeContext.getCurrentPosition());
    }

    @Test
    public void testNextNodeWhenNoAttributeFound() {
        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
        when(nodeTest.isPredicate()).thenReturn(false);
        when(nodeTest.getNodeName().getLocalPart()).thenReturn("test");

        attributeContext.nextNode();
        assertFalse("Next node call should return false when no attribute found", attributeContext.isSetStarted());
        assertNull("Current node pointer should be null", attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWhenAttributeFound() {
        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
        when(nodeTest.isPredicate()).thenReturn(false);
        when(nodeTest.getNodeName().getLocalPart()).thenReturn("test");
        when(attributeIterator.hasNext()).thenReturn(true);
        when(attributeIterator.getNodePointer()).thenReturn(attributeNodePointer);

        when(parentContext.getCurrentNodePointer()).thenReturn(mock(NodePointer.class));
        attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.nextNode();
        assertTrue("Next node call should return true when attribute found", attributeContext.isSetStarted());
        assertEquals("Current node pointer should be the same as the one returned by attribute iterator", attributeNodePointer, attributeContext.getCurrentNodePointer());

        when(attributeIterator.hasNext()).thenReturn(false);
        attributeContext.nextNode();
        assertFalse("Next node call should return false when no more attributes", attributeContext.isSetStarted());
        assertNull("Current node pointer should be null", attributeContext.getCurrentNodePointer());
    }
}