import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        Element element = new Element("testElement");
        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr"));

        assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr*"));
        NodePointer nodePointer = iterator.getNodePointer();

        assertNotNull(nodePointer);
        assertEquals("attr1", ((Attribute)nodePointer.getNode()).getName());
        assertEquals("value1", ((Attribute)nodePointer.getNode()).getValue());
    }

    @Test
    public void testSetPosition() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");

        NodePointer parent = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr*"));

        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());

        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());

        assertFalse(iterator.setPosition(3));
    }
}