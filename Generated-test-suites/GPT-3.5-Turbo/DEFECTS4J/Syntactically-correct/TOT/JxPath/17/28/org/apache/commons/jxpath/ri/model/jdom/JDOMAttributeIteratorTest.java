import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator iterator;
    private NodePointer parent;

    @Before
    public void setup() {
        Element element = new Element("testElement");
        element.setAttribute(new Attribute("attribute1", "value1"));
        element.setAttribute(new Attribute("attribute2", "value2"));
        element.setAttribute(new Attribute("attribute3", "value3"));
        parent = new ElementPointer(element);
    }

    @Test
    public void testGetNodePointer() {
        iterator = new JDOMAttributeIterator(parent, new QName("attribute1"));

        NodePointer nodePointer = iterator.getNodePointer();

        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals("attribute1", ((JDOMAttributePointer) nodePointer).getAttribute().getName());
    }

    @Test
    public void testGetNodePointerWithInvalidAttribute() {
        iterator = new JDOMAttributeIterator(parent, new QName("invalidAttribute"));

        NodePointer nodePointer = iterator.getNodePointer();

        assertNull(nodePointer);
    }

    @Test
    public void testGetPosition() {
        iterator = new JDOMAttributeIterator(parent, new QName("*"));

        assertEquals(0, iterator.getPosition());

        iterator.setPosition(2);

        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        iterator = new JDOMAttributeIterator(parent, new QName("*"));

        assertFalse(iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());

        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());

        assertTrue(iterator.setPosition(3));
        assertEquals(3, iterator.getPosition());

        assertFalse(iterator.setPosition(4));
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testSetPositionWithNoAttributes() {
        Element element = new Element("testElement");
        parent = new ElementPointer(element);

        iterator = new JDOMAttributeIterator(parent, new QName("*"));

        assertFalse(iterator.setPosition(1));
        assertEquals(0, iterator.getPosition());
    }

    private class ElementPointer implements NodePointer {

        private Element element;

        public ElementPointer(Element element) {
            this.element = element;
        }

        @Override
        public Object getNode() {
            return element;
        }

        @Override
        public NodePointer getValuePointer() {
            return null;
        }

        @Override
        public NodePointer createPath(QName qName) {
            return null;
        }

        @Override
        public NodePointer createPath(QName qName, boolean b) {
            return null;
        }

        @Override
        public NodePointer createChild(NodePointer nodePointer) {
            return null;
        }

        @Override
        public boolean isCollection() {
            return false;
        }

        @Override
        public int getLength() {
            return 0;
        }

        @Override
        public QName getName() {
            return new QName(element.getName());
        }

        @Override
        public QName getNamespaceURI() {
            Namespace namespace = element.getNamespace();
            return new QName(namespace.getPrefix(), namespace.getURI());
        }

        @Override
        public String getNamespaceURI(String s) {
            return element.getNamespace().getURI();
        }

        @Override
        public String getNamespacePrefix(String s) {
            return element.getNamespace().getPrefix();
        }

        @Override
        public String asPath() {
            return null;
        }

        @Override
        public boolean isActual() {
            return false;
        }

        @Override
        public boolean isContainer() {
            return false;
        }

        @Override
        public Object getBaseValue() {
            return null;
        }

        @Override
        public Object getImmediateNode() {
            return null;
        }

        @Override
        public int compareChildNodePointers(NodePointer nodePointer, NodePointer nodePointer1) {
            return 0;
        }

        @Override
        public boolean testNode(NodeTest nodeTest) {
            return false;
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public int compareChildNodePointers(NodePointer nodePointer) {
            return 0;
        }

        @Override
        public void setValue(Object o) {

        }
    }
}