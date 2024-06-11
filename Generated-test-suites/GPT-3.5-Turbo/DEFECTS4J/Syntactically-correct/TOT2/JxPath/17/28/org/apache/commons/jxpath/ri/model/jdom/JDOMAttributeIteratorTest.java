import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Element;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorConstruction() {
        Element element = new Element("testElement");
        NodePointer parentNodePointer = new JDOMNodePointer(element);
        QName qName = new QName("testAttributeName");
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parentNodePointer, qName);
        assertNotNull(jdomAttributeIterator);
    }

    @Test
    public void testGetNodePointer() {
        Element element = new Element("testElement");
        element.setAttribute("testAttribute", "testValue");
        NodePointer parentNodePointer = new JDOMNodePointer(element);
        QName qName = new QName("testAttribute");
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parentNodePointer, qName);
        NodePointer nodePointer = jdomAttributeIterator.getNodePointer();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
    }

    @Test
    public void testGetPosition() {
        Element element = new Element("testElement");
        NodePointer parentNodePointer = new JDOMNodePointer(element);
        QName qName = new QName("testAttributeName");
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parentNodePointer, qName);
        assertEquals(0, jdomAttributeIterator.getPosition());
        jdomAttributeIterator.setPosition(1);
        assertEquals(1, jdomAttributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Element element = new Element("testElement");
        element.setAttribute("testAttribute", "testValue");
        NodePointer parentNodePointer = new JDOMNodePointer(element);
        QName qName = new QName("testAttribute");
        JDOMAttributeIterator jdomAttributeIterator = new JDOMAttributeIterator(parentNodePointer, qName);
        assertTrue(jdomAttributeIterator.setPosition(1));
        assertFalse(jdomAttributeIterator.setPosition(0));
        assertFalse(jdomAttributeIterator.setPosition(2));
    }
}