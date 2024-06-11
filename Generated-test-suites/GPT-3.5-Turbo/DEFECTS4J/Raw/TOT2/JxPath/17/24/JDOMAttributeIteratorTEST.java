import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Attribute;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");

        NodePointer nodePointer = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("attr1"));

        assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");

        NodePointer nodePointer = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("attr1"));

        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");

        NodePointer nodePointer = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("attr1"));

        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");

        NodePointer nodePointer = new JDOMNodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("attr1"));

        assertTrue(iterator.setPosition(1));
        assertFalse(iterator.setPosition(0));
    }
}