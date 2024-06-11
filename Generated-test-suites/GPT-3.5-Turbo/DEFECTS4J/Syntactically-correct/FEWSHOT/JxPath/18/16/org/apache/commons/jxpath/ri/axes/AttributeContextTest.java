import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {
    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setup() {
        parentContext = new EvalContext();
        nodeTest = new NodeNameTest(new QName("", "attribute"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
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
        assertFalse(attributeContext.setPosition(1));
    }

    @Test
    public void testNextNode() {
        assertFalse(attributeContext.nextNode());
    }
}