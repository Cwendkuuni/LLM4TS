package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link AttributeContext}.
 */
public class AttributeContextTest {

    private static final String TEST_NAME = "test";
    private static final int TEST_POSITION = 0;
    private static final int TEST_NEXT_POSITION = 1;
    private EvalContext parentContext;
    private NodeTest nodeTest;

    @Rule
    public MockitoRule mockitoRule = Mockito.rule().mockitoInstance(this);

    @Before
    public void setUp() {
        parentContext = Mockito.mock(EvalContext.class);
        nodeTest = Mockito.mock(NodeNameTest.class);
        Mockito.when(nodeTest.getNodeName()).thenReturn(new QName(TEST_NAME));
    }

    @Test
    public void testCreateAttributeContext() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNotNull(attributeContext);
        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());
        assertNull(attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testGetCurrentNodePointer_whenNotSet_thenNull() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset_whenCalled_thenResetParentContextAndVariables() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.reset();
        assertNull(attributeContext.getCurrentNodePointer());
        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.iterator);
        Mockito.verify(parentContext).reset();
    }

    @Test
    public void testSetPosition_whenPositionIsLessThanCurrentPosition_thenResetAndReturnFalse() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setPosition(TEST_POSITION);
        Mockito.verify(parentContext).setPosition(TEST_POSITION);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_whenFirstCall_thenReturnTrueAndGetFirstAttribute() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Mockito.when(parentContext.getCurrentNodePointer().getAttributeIterator(Mockito.any(QName.class)))
                .thenReturn(Mockito.mock(NodeIterator.class));
        Mockito.when(parentContext.getCurrentNodePointer().getAttributeIterator(Mockito.any(QName.class))).thenReturn(iterator);
        Mockito.when(iterator.getNodePointer()).thenReturn(Mockito.mock(NodePointer.class));
        assertTrue(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode_whenNextCall_thenReturnTrueAndGetNextAttribute() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Mockito.when(parentContext.getCurrentNodePointer().getAttributeIterator(Mockito.any(QName.class)))
                .thenReturn(Mockito.mock(NodeIterator.class));
        Mockito.when(iterator.getNodePointer()).thenReturn(Mockito.mock(NodePointer.class));
        Mockito.when(iterator.getNodePointer()).thenReturn(currentNodePointer);
        Mockito.when(currentNodePointer.getLocalName()).thenReturn("testAttribute1");
        assertTrue(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());
        Mockito.verify(iterator).setPosition(1);
        Mockito.verify(iterator).getNodePointer();
        Mockito.verify(currentNodePointer).getLocalName();
        assertTrue(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());
        Mockito.verify(iterator).setPosition(2);
        Mockito.verify(iterator).getNodePointer();
    }

    @Test
    public void testNextNode_whenEndOfIterator_thenReturnFalse() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Mockito.when(parentContext.getCurrentNodePointer().getAttributeIterator(Mockito.any(QName.class)))
                .thenReturn(Mockito.mock(NodeIterator.class));
        Mockito.when(iterator.hasNext()).thenReturn(false);
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_whenParentContextHasNextNode_thenReturnTrueAndGetNextNode() {
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        Mockito.when(parentContext.getCurrentNodePointer().getAttributeIterator(Mockito.any(QName.class)))
                .thenReturn(Mockito.mock(NodeIterator.class));
        Mockito.when(iterator.hasNext()).thenReturn(true);
        Mockito.when(iterator.getNodePointer()).thenReturn(Mockito.mock(NodePointer.class));
        Mockito.when(parentContext.nextNode()).thenReturn(true);
        assertTrue(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());
        Mockito.verify(iterator).setPosition(1);
        Mockito.verify(iterator).getNodePointer();
        Mockito.verify(parentContext).nextNode();
    }
}