import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testGetNodePointer() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");

        NodePointer parent = new JDOMNodePointer(element);
        QName name = new QName("attr", "");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);

        Attribute attribute = ((JDOMAttributePointer) nodePointer).getAttribute();
        assertEquals("attr1", attribute.getName());
        assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testGetPosition() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");

        NodePointer parent = new JDOMNodePointer(element);
        QName name = new QName("attr", "");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertEquals(0, iterator.getPosition());

        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());

        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());

        iterator.setPosition(3);
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");

        NodePointer parent = new JDOMNodePointer(element);
        QName name = new QName("attr", "");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());

        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());

        assertFalse(iterator.setPosition(3));
        assertEquals(0, iterator.getPosition());
    }
}