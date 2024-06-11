import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import static org.junit.Assert. * ;

public class AttributeContextTest {
    private AttributeContext attributeContext;
    private EvalContext parentContext;
    private NodeTest nodeTest;

    @Before
    public void setUp() {
        parentContext = new EvalContext();
        nodeTest = new NodeNameTest("attribute");
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNull(currentNodePointer);
    }

    @Test
    public void testReset() {
        attributeContext.reset();
        assertEquals(false, attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        assertFalse(attributeContext.setPosition(1));
        assertFalse(attributeContext.setPosition(-1));

        assertFalse(attributeContext.setPosition(0));
        assertEquals(false, attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());

        assertEquals(true, attributeContext.setPosition(1));
        assertEquals(false, attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNode() {
        attributeContext.reset();
        assertEquals(false, attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());

        attributeContext.setPosition(0);
        assertTrue(attributeContext.nextNode());
        assertNotNull(attributeContext.getCurrentNodePointer());

        attributeContext.reset();
        attributeContext.setPosition(1);
        assertEquals(false, attributeContext.nextNode());
        assertNull(attributeContext.getCurrentNodePointer());
    }

}