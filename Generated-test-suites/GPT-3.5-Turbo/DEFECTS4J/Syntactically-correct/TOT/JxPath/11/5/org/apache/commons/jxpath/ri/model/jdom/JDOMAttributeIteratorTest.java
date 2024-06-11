import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    private Element element;
    private NodePointer parent;

    @Before
    public void setup() {
        element = new Element("testElement");
        parent = new NodePointer() {
            @Override
            public Object getBaseValue() {
                return null;
            }

            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public void setValue(Object value) {

            }

            @Override
            public boolean isValueEditable() {
                return false;
            }

            @Override
            public String asPath() {
                return null;
            }

            @Override
            public boolean testNode(NodeTest nodeTest) {
                return false;
            }

            @Override
            public QName getName() {
                return null;
            }

            @Override
            public Object getNode() {
                return element;
            }

            @Override
            public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
                return 0;
            }
        };
    }

    @Test
    public void testGetNodePointerWithOneMatchingAttribute() {
        Attribute attribute = new Attribute("attribute1", "value1");
        element.setAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attribute1"));
        NodePointer nodePointer = iterator.getNodePointer();

        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals(attribute, ((JDOMAttributePointer) nodePointer).getAttribute());
    }

    @Test
    public void testGetNodePointerWithMultipleMatchingAttributes() {
        Attribute attribute1 = new Attribute("attribute1", "value1");
        Attribute attribute2 = new Attribute("attribute2", "value2");
        Attribute attribute3 = new Attribute("attribute2", Namespace.XML_NAMESPACE, "value3");
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);
        element.setAttribute(attribute3);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attribute2"));
        NodePointer nodePointer1 = iterator.getNodePointer();
        NodePointer nodePointer2 = iterator.getNodePointer();
        NodePointer nodePointer3 = iterator.getNodePointer();

        assertNotNull(nodePointer1);
        assertNotNull(nodePointer2);
        assertNotNull(nodePointer3);
        assertTrue(nodePointer1 instanceof JDOMAttributePointer);
        assertTrue(nodePointer2 instanceof JDOMAttributePointer);
        assertTrue(nodePointer3 instanceof JDOMAttributePointer);
        assertEquals(attribute2, ((JDOMAttributePointer) nodePointer1).getAttribute());
        assertEquals(attribute2, ((JDOMAttributePointer) nodePointer2).getAttribute());
        assertEquals(attribute3, ((JDOMAttributePointer) nodePointer3).getAttribute());
    }

    @Test
    public void testGetNodePointerWithNonMatchingAttributeName() {
        Attribute attribute = new Attribute("attribute1", "value1");
        element.setAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attribute2"));
        NodePointer nodePointer = iterator.getNodePointer();

        assertNull(nodePointer);
    }

    @Test
    public void testGetPosition() {
        Attribute attribute1 = new Attribute("attribute1", "value1");
        Attribute attribute2 = new Attribute("attribute2", "value2");
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attribute1"));

        assertEquals(0, iterator.getPosition());

        iterator.getNodePointer();
        assertEquals(1, iterator.getPosition());

        iterator.getNodePointer();
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPositionWithValidPosition() {
        Attribute attribute1 = new Attribute("attribute1", "value1");
        Attribute attribute2 = new Attribute("attribute2", "value2");
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attribute1"));

        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());

        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPositionWithInvalidPosition() {
        Attribute attribute1 = new Attribute("attribute1", "value1");
        Attribute attribute2 = new Attribute("attribute2", "value2");
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attribute1"));

        assertFalse(iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());

        assertFalse(iterator.setPosition(3));
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testGetNodePointerWithNoMatchingNamespace() {
        Attribute attribute = new Attribute("attribute1", Namespace.XML_NAMESPACE, "value1");
        element.setAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attribute1"));

        assertFalse(iterator.setPosition(1));
        assertNull(iterator.getNodePointer());
    }
}