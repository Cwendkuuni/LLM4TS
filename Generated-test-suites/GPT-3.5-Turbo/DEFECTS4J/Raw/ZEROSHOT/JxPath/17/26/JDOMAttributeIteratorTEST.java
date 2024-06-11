import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator iterator;
    private NodePointer parent;
    private QName name;

    @Before
    public void setUp() {
        parent = createNode();
        name = new QName("test", "attribute");
        iterator = new JDOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
    }

    @Test
    public void testGetPosition() {
        int position = iterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testSetPositionValid() {
        boolean result = iterator.setPosition(1);
        assertTrue(result);

        int position = iterator.getPosition();
        assertEquals(1, position);

        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
    }

    @Test
    public void testSetPositionInvalid() {
        boolean result = iterator.setPosition(10);
        assertFalse(result);

        int position = iterator.getPosition();
        assertEquals(0, position);

        NodePointer nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }
  
    // Helper method to create a sample Element node for testing
    private NodePointer createNode() {
        Element element = new Element("testElement");
        element.setAttribute("testAttribute", "testValue");
        return new JDOMNodePointer(element, null);
    }
}