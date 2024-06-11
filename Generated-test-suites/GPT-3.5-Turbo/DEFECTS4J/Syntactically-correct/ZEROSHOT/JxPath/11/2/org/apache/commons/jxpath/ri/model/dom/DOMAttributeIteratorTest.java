import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.runners.JUnit4;

public class DOMAttributeIteratorTest {

    private DOMAttributeIterator iterator;
    private NodePointer parent;
    private QName name;
    
    @Before
    public void setUp() {
        parent = new NodePointer();
        name = new QName("attribute");
        iterator = new DOMAttributeIterator(parent, name);
    }
    
    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
    }
    
    @Test
    public void testGetPosition() {
        int position = iterator.getPosition();
        assertEquals(0, position);
    }
    
    @Test
    public void testSetPosition() {
        boolean result = iterator.setPosition(1);
        assertTrue(result);
    }

}