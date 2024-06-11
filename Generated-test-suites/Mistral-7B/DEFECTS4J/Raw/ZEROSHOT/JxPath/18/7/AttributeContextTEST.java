import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.axes.AttributeContextTests;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AttributeContextTest {

    @Test
    public void testAttributeContext() {
        // Test empty constructor
        AttributeContext emptyContext = new AttributeContext(null, null);
        assertNull(emptyContext.getNodeTest());
        assertFalse(emptyContext.isSetStarted());
        assertNull(emptyContext.getCurrentNodePointer());
        assertNull(emptyContext.getCurrentPosition());
        assertNull(emptyContext.getParentContext());
        assertNull(emptyContext.iterator);

        // Test constructor with valid arguments
        NodeTest nodeTest = mock(NodeNameTest.class);
        when(nodeTest.getNodeName()).thenReturn(new QName("test"));
        EvalContext parentContext = mock(EvalContext.class);
        AttributeContext context = new AttributeContext(parentContext, nodeTest);
        assertEquals(nodeTest, context.getNodeTest());
        assertTrue(context.isSetStarted());
        assertNull(context.getCurrentNodePointer());
        assertEquals(0, context.getCurrentPosition());
        assertEquals(parentContext, context.getParentContext());
        assertNull(context.iterator);
    }

    @Test
    public void testReset() {
        AttributeContext context = createContext();
        context.nextNode();
        context.reset();
        assertFalse(context.isSetStarted());
        assertNull(context.getCurrentNodePointer());
        assertNull(context.iterator);
        assertEquals(0, context.getCurrentPosition());
    }

    @Test
    public void testSetPosition() {
        AttributeContext context = createContext();
        context.nextNode();
        context.setPosition(1);
        assertTrue(context.isSetStarted());
        assertNotNull(context.getCurrentNodePointer());
        assertEquals(1, context.getCurrentPosition());
        assertNotNull(context.iterator);
    }

    @Test
    public void testNextNode() {
        AttributeContext context = createContext();
        context.nextNode();
        assertTrue(context.isSetStarted());
        assertNotNull(context.getCurrentNodePointer());
        assertEquals(1, context.getCurrentPosition());
        assertNotNull(context.iterator);

        context.nextNode();
        assertTrue(context.isSetStarted());
        assertNotNull(context.getCurrentNodePointer());
        assertEquals(2, context.getCurrentPosition());
        assertNotNull(context.iterator);

        context.nextNode();
        assertFalse(context.isSetStarted());
        assertNull(context.getCurrentNodePointer());
        assertNull(context.iterator);
    }

    private AttributeContext createContext() {
        NodeTest nodeTest = mock(NodeNameTest.class);
        when(nodeTest.getNodeName()).thenReturn(new QName("test"));
        EvalContext parentContext = mock(EvalContext.class);
        return new AttributeContext(parentContext, nodeTest);
    }

}