import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

    //Test getCurrentNodePointer method
    @Test
    public void testGetCurrentNodePointer() {
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(null, nodeTest);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    //Test reset method
    @Test
    public void testReset() {
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(null, nodeTest);
        attributeContext.reset();
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    //Test setPosition method when position is less than current position
    @Test
    public void testSetPositionLessThanCurrentPosition() {
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(null, nodeTest);
        attributeContext.setPosition(5);
        attributeContext.setPosition(3);
        assertEquals(3, attributeContext.getCurrentPosition());
    }

    //Test setPosition method when position is greater than current position
    @Test
    public void testSetPositionGreaterThanCurrentPosition() {
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(null, nodeTest);
        attributeContext.setPosition(2);
        assertTrue(attributeContext.setPosition(5));
        assertEquals(5, attributeContext.getCurrentPosition());
    }

    //Test nextNode method when setStarted is false
    @Test
    public void testNextNodeSetStartedFalse() {
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(null, nodeTest);
        attributeContext.nextNode();
        assertTrue(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
    }

    //Test nextNode method when iterator is null
    @Test
    public void testNextNodeIteratorNull() {
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(null, nodeTest);
        attributeContext.setStarted = true;
        attributeContext.iterator = null;
        assertFalse(attributeContext.nextNode());
    }

    //Test nextNode method when iterator position cannot be set
    @Test
    public void testNextNodeIteratorPositionCannotBeSet() {
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(null, nodeTest);
        attributeContext.setStarted = true;
        attributeContext.iterator = new NodeIterator() {
            @Override
            public NodePointer getNodePointer() {
                return null;
            }
            @Override
            public int getPosition() {
                return 0;
            }
            @Override
            public boolean setPosition(int position) {
                return false;
            }
            @Override
            public boolean next() {
                return false;
            }
        };
        assertFalse(attributeContext.nextNode());
    }

    //Test nextNode method when iterator position can be set
    @Test
    public void testNextNodeIteratorPositionCanBeSet() {
        NodeTest nodeTest = new NodeNameTest(new QName("attribute"));
        AttributeContext attributeContext = new AttributeContext(null, nodeTest);
        attributeContext.setStarted = true;
        attributeContext.iterator = new NodeIterator() {
            private boolean positionSet = false;
            @Override
            public NodePointer getNodePointer() {
                return null;
            }
            @Override
            public int getPosition() {
                return 0;
            }
            @Override
            public boolean setPosition(int position) {
                if (!positionSet) {
                    positionSet = true;
                    return true;
                }
                return false;
            }
            @Override
            public boolean next() {
                return true;
            }
        };
        assertTrue(attributeContext.nextNode());
    }

}