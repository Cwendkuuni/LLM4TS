import org.junit.Before;
import org.junit.Test;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodePointer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AttributeContextTest {

    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        attributeContext = new AttributeContext(null, new NodeNameTest("test"));
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNull(currentNodePointer);
    }

    @Test
    public void testReset() {
        attributeContext.nextNode();
        attributeContext.reset();
        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testSetPosition() {
        assertTrue(attributeContext.setPosition(1));
        assertTrue(attributeContext.setPosition(2));
        assertFalse(attributeContext.setPosition(0));
    }

    @Test
    public void testNextNode() {
        assertTrue(attributeContext.nextNode());
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNotNull(currentNodePointer);
    }

    @Test
    public void testNextNodeWithInvalidNodeTest() {
        AttributeContext invalidNodeTestContext = new AttributeContext(null, new NodeNameTest(""));
        assertFalse(invalidNodeTestContext.nextNode());
    }

    @Test
    public void testNextNodeWithNullIterator() {
        AttributeContext nullIteratorContext = new AttributeContext(null, new NodeNameTest("test"));
        nullIteratorContext.nextNode();
        assertNull(nullIteratorContext.getCurrentNodePointer());
    }
}