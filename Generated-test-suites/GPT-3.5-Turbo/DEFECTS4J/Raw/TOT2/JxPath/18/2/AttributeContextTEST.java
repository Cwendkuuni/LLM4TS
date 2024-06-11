import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        attributeContext = new AttributeContext(null, new NodeNameTest(new QName("test")));
    }

    @Test
    public void testGetCurrentNodePointer() {
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        attributeContext.setPosition(2);
        attributeContext.reset();
        assertEquals(0, attributeContext.getCurrentPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(attributeContext.setPosition(0));
        assertFalse(attributeContext.setPosition(-1));
    }

    @Test
    public void testNextNode() {
        assertTrue(attributeContext.nextNode());
    }

    @Test
    public void testNextNodeWithNonNodeNameTest() {
        attributeContext = new AttributeContext(null, new NodeTest() {
            @Override
            public boolean test(NodePointer node) {
                return false;
            }
        });
        assertFalse(attributeContext.nextNode());
    }
}