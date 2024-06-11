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
        attributeContext.reset();
        assertFalse(attributeContext.setPosition(1));
    }

    @Test
    public void testSetPosition() {
        assertFalse(attributeContext.setPosition(0));
    }

    @Test
    public void testNextNode() {
        assertFalse(attributeContext.nextNode());
    }
}