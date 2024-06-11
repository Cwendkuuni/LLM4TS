import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AttributeContextTest {

    @Mock
    private EvalContext parentContext;

    @Mock
    private NodeTest nodeTest;

    @InjectMocks
    private AttributeContext attributeContext;

    private NodeIterator iterator;
    private NodePointer currentNodePointer;

    @Before
    public void setUp() {
        currentNodePointer = mock(NodePointer.class);
        iterator = mock(NodeIterator.class);

        when(parentContext.getCurrentNodePointer()).thenReturn(currentNodePointer);
        when(parentContext.attributeIterator(new QName("test"))).thenReturn(iterator);
    }

    @Test
    public void testAttributeContextConstruction() {
        AttributeContext ac = new AttributeContext(parentContext, nodeTest);
        assertNotNull(ac.nodeTest);
        assertNotNull(ac.iterator);
        assertFalse(ac.setStarted);
        assertNull(ac.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        AttributeContext ac = new AttributeContext(parentContext, nodeTest);
        ac.reset();
        assertFalse(ac.setStarted);
        assertNull(ac.iterator);
        assertTrue(super.equals(ac));
    }

    @Test
    public void testSetPositionWhenNotAtStart() {
        AttributeContext ac = new AttributeContext(parentContext, nodeTest);
        ac.setPosition(1);
        assertFalse(ac.setStarted);
        assertNull(ac.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWhenAtStart() {
        AttributeContext ac = new AttributeContext(parentContext, nodeTest);
        ac.setPosition(0);
        assertTrue(ac.setStarted);
        assertNotNull(ac.iterator);
        assertNull(ac.currentNodePointer);
    }

    @Test
    public void testSetPositionWhenAtEnd() {
        AttributeContext ac = new AttributeContext(parentContext, nodeTest);
        ac.setPosition(1);
        ac.setPosition(1);
        assertFalse(ac.nextNode());
        assertNull(ac.currentNodePointer);
    }

    @Test
    public void testNextNodeWhenAtStart() {
        AttributeContext ac = new AttributeContext(parentContext, nodeTest);
        ac.nextNode();
        assertTrue(ac.setStarted);
        assertNotNull(ac.iterator);
        assertNull(ac.currentNodePointer);
    }

    @Test
    public void testNextNodeWhenAtEnd() {
        AttributeContext ac = new AttributeContext(parentContext, nodeTest);
        ac.nextNode();
        ac.nextNode();
        assertFalse(ac.nextNode());
        assertNull(ac.currentNodePointer);
    }

    @Test
    public void testNextNodeWhenInMiddle() {
        when(iterator.getNodePointer()).thenReturn(currentNodePointer);
        when(currentNodePointer.getNode()).thenReturn(mock(Object.class));

        AttributeContext ac = new AttributeContext(parentContext, nodeTest);
        ac.nextNode();
        ac.nextNode();
        ac.nextNode();
        assertNotNull(ac.currentNodePointer);
        assertNotNull(ac.currentNodePointer.getNode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNextNodeWhenIteratorIsEmpty() {
        AttributeContext ac = new AttributeContext(parentContext, nodeTest);
        ac.nextNode();
        ac.nextNode();
        when(iterator.getNodePointer()).thenReturn(null);
        ac.nextNode();
    }

    @Test
    public void testGetCurrentNodePointerWhenAtStart() {
        AttributeContext ac = new AttributeContext(parentContext, nodeTest);
        assertNull(ac.getCurrentNodePointer());
    }

    @Test
    public void testGetCurrentNodePointerWhenInMiddle() {
        when(iterator.getNodePointer()).thenReturn(currentNodePointer);
        when(currentNodePointer.getNode()).thenReturn(mock(Object.class));

        AttributeContext ac = new AttributeContext(parentContext, nodeTest);
        ac.nextNode();
        ac.nextNode();
        ac.nextNode();
        assertNotNull(ac.currentNodePointer);
        assertNotNull(ac.currentNodePointer.getNode());
    }
}