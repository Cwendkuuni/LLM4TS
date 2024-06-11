
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testGetNodePointer() {
        Element element = new Element("testElement");
        Attribute attribute = new Attribute("testAttribute", "value");
        element.setAttribute(attribute);
        NodePointer parent = new JDOMNodePointer(element);

        QName qName = new QName("", "testAttribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);
        NodePointer nodePointer = iterator.getNodePointer();

        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals(attribute, ((JDOMAttributePointer) nodePointer).getAttribute());

        QName wildCardQName = new QName("", "*");
        JDOMAttributeIterator wildCardIterator = new JDOMAttributeIterator(parent, wildCardQName);
        NodePointer wildCardNodePointer = wildCardIterator.getNodePointer();

        assertNotNull(wildCardNodePointer);
        assertTrue(wildCardNodePointer instanceof JDOMAttributePointer);
        assertEquals(attribute, ((JDOMAttributePointer) wildCardNodePointer).getAttribute());
    }

    @Test
    public void testGetPosition() {
        Element element = new Element("testElement");
        Attribute attribute = new Attribute("testAttribute", "value");
        element.setAttribute(attribute);
        NodePointer parent = new JDOMNodePointer(element);

        QName qName = new QName("", "testAttribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        assertEquals(0, iterator.getPosition());
        iterator.setPosition(2);
        assertEquals(2, iterator.getPosition());
        iterator.setPosition(0);
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Element element = new Element("testElement");
        Attribute attribute = new Attribute("testAttribute", "value");
        element.setAttribute(attribute);
        NodePointer parent = new JDOMNodePointer(element);

        QName qName = new QName("", "testAttribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        assertFalse(iterator.setPosition(0));
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(2));
        assertFalse(iterator.setPosition(3));
    }
}
