import org.apache.commons.jxpath.ri.axes.*;
import org.apache.commons.jxpath.ri.model.*;
import org.apache.commons.jxpath.ri.compiler.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeContextTest {

    @Test
    public void testGetParentContext() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        assertEquals(parentContext, attributeContext.getParentContext());
    }

    @Test
    public void testGetNodeTest() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        assertEquals(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testGetCurrentNodePointer() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.reset();

        assertFalse(attributeContext.isSetStarted());
        assertNull(attributeContext.getIterator());
        assertNull(attributeContext.getCurrentNodePointer());
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPositionLessThanCurrentPosition() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.setPosition(2);
        attributeContext.setPosition(1);

        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testSetPositionGreaterThanCurrentPosition() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        attributeContext.setPosition(2);

        assertTrue(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeNotInstanceofNodeNameTest() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeTypeTest(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeIteratorIsNull() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

        assertNull(attributeContext.getIterator());

        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeIteratorSetPositionFalse() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setIterator(new NodeIterator() {

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
            public boolean setPosition(int position, boolean skipValidation) {
                return false;
            }

            @Override
            public int getLastPosition() {
                return 0;
            }

            @Override
            public void remove() {

            }

            @Override
            public void setNamespaceContext(NamespaceContext namespaceContext) {

            }
        });

        assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeIteratorSetPositionTrue() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setIterator(new NodeIterator() {

            private int position = 0;

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

            @Override
            public boolean setPosition(int position, boolean skipValidation) {
                this.position = position;
                return true;
            }

            @Override
            public int getLastPosition() {
                return position;
            }

            @Override
            public void remove() {

            }

            @Override
            public void setNamespaceContext(NamespaceContext namespaceContext) {

            }
        });

        assertTrue(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeDefaultCase() {
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setSetStarted(true);
        attributeContext.setIterator(new NodeIterator() {

            private int position = 0;

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

            @Override
            public boolean setPosition(int position, boolean skipValidation) {
                this.position = position;
                return true;
            }

            @Override
            public int getLastPosition() {
                return position;
            }

            @Override
            public void remove() {

            }

            @Override
            public void setNamespaceContext(NamespaceContext namespaceContext) {

            }
        });

        assertTrue(attributeContext.nextNode());
    }
}