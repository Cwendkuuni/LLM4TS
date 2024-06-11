import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeContextTest {

    private AttributeContext attributeContext;

    @Before
    public void setup() {
        attributeContext = new AttributeContext(null, new NodeNameTest("test"));
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.setPosition(1);

        attributeContext.reset();

        assertEquals(0, attributeContext.getCurrentPosition());
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPosition_InvalidPosition() {
        assertFalse(attributeContext.setPosition(-1));

        assertEquals(0, attributeContext.getCurrentPosition());
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testSetPosition_ValidPosition() {
        attributeContext.iterator = new TestNodeIterator();
        attributeContext.setPosition(1);

        assertEquals(1, attributeContext.getCurrentPosition());
        assertTrue(attributeContext.setStarted);
        assertNotNull(attributeContext.iterator);
        assertNotNull(attributeContext.currentNodePointer);
    }

    @Test
    public void testNextNode_SetNotStarted() {
        assertFalse(attributeContext.nextNode());

        assertEquals(1, attributeContext.getCurrentPosition());
        assertTrue(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
    }

    @Test
    public void testNextNode_NullIterator() {
        attributeContext.setStarted = true;
        attributeContext.iterator = null;

        assertFalse(attributeContext.nextNode());

        assertEquals(1, attributeContext.getCurrentPosition());
        assertNull(attributeContext.currentNodePointer);
    }

    @Test
    public void testNextNode_ValidIterator() {
        attributeContext.setStarted = true;

        TestNodeIterator iterator = new TestNodeIterator();
        iterator.setPosition(1);
        attributeContext.iterator = iterator;

        assertTrue(attributeContext.nextNode());

        assertEquals(2, attributeContext.getCurrentPosition());
        assertNotNull(attributeContext.currentNodePointer);
    }

    private static class TestNodeIterator implements NodeIterator {

        private int position;

        @Override
        public NodePointer getNodePointer() {
            return null;
        }

        @Override
        public int getPosition() {
            return position;
        }

        @Override
        public boolean setPosition(int position) {
            this.position = position;
            return true;
        }
    }
}