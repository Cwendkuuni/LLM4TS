import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    private Element element;
    private NodePointer parent;

    @Before
    public void setup() {
        element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        parent = new JDOMAttributeIterator(element, new QName("attr1"));
    }

    @Test
    public void testGetNodePointer() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals("value1", ((Attribute) nodePointer.getNode()).getValue());
    }

    @Test
    public void testGetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        assertFalse(iterator.setPosition(0));
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(2));
        assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testEmptyAttributes() {
        element = new Element("emptyElement");
        parent = new JDOMAttributeIterator(element, new QName("attr1"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testNamespaceAttributes() {
        element = new Element("namespaceElement", Namespace.getNamespace("test", "http://test"));
        element.setAttribute("attr1", "value1", Namespace.getNamespace("test", "http://test"));
        parent = new JDOMAttributeIterator(element, new QName("attr1"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("value1", ((Attribute) nodePointer.getNode()).getValue());
    }

}