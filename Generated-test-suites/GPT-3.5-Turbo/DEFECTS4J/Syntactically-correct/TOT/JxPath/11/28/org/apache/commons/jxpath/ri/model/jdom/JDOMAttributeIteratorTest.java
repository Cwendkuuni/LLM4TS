import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    private NodePointer parent;

    @Before
    public void setup() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");
        parent = new JDOMAttributePointer(null, element.getAttribute("attr2"));
    }

    @Test
    public void testGetNodePointer() {
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, new QName("attr2"));
        NodePointer nodePointer = attributeIterator.getNodePointer();

        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals("attr2", ((Attribute) nodePointer.getNode()).getName());
    }

    @Test
    public void testGetPosition() {
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, new QName("*"));
        assertEquals(0, attributeIterator.getPosition());

        attributeIterator.setPosition(2);
        assertEquals(2, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, new QName("*"));

        assertFalse(attributeIterator.setPosition(0));
        assertEquals(0, attributeIterator.getPosition());
        assertTrue(attributeIterator.setPosition(1));
        assertEquals(1, attributeIterator.getPosition());
        assertTrue(attributeIterator.setPosition(3));
        assertEquals(3, attributeIterator.getPosition());

        assertFalse(attributeIterator.setPosition(4));
    }
}