
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeContextTest {
    private AttributeContext attributeContext;
    private EvalContext parentContext;
    private NodeTest nodeTest;

    @Before
    public void setUp() {
        parentContext = new EvalContext(null);
        nodeTest = new NodeNameTest(null, null);
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer expected = null;
        NodePointer actual = attributeContext.getCurrentNodePointer();
        assertEquals(expected, actual);
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertFalse(attributeContext.setStarted);
        assertNull(attributeContext.iterator);
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPosition() {
        int position = 1;
        boolean expected = true;
        boolean actual = attributeContext.setPosition(position);
        assertEquals(expected, actual);
    }

    @Test
    public void testNextNode() {
        attributeContext.setStarted = false;
        attributeContext.nodeTest = new NodeNameTest(null);
        attributeContext.parentContext = new EvalContext(null);
        attributeContext.iterator = new NodeIterator() {
            @Override
            public NodePointer getNodePointer() {
                return null;
            }

            @Override
            public boolean setPosition(int position) {
                return false;
            }

            @Override
            public int getPosition() {
                return 0;
            }
        };
        boolean expected = false;
        boolean actual = attributeContext.nextNode();
        assertEquals(expected, actual);
    }
}
