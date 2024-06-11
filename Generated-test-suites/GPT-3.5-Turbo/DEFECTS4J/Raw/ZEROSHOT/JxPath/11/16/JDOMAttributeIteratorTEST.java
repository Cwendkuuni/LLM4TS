import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    @Test
    public void testGetNodePointer() {
        // Test when parent node is an instance of Element
        NodePointer parent = new JDOMNodePointer(new Element("test"));
        QName name = new QName(null, null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());

        // Test when parent node is not an instance of Element
        parent = new JDOMNodePointer(new Object());
        iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());

        // Test when parent node is an instance of Element and name is specified
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        parent = new JDOMNodePointer(element);
        name = new QName(null, "attr1", null);
        iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator.getNodePointer());
        assertEquals("attr1", iterator.getNodePointer().getNode().getName());

        // Test when parent node is an instance of Element, name is specified, but attribute is not found
        name = new QName(null, "attr3", null);
        iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = new JDOMNodePointer(new Element("test"));
        QName name = new QName(null, null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        NodePointer parent = new JDOMNodePointer(element);
        QName name = new QName(null, null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        // Test when position is less than 1
        assertFalse(iterator.setPosition(0));

        // Test when position is greater than the number of attributes
        assertFalse(iterator.setPosition(3));

        // Test when position is equal to the number of attributes
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());

        // Test when position is within the range of attributes
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
    }
}