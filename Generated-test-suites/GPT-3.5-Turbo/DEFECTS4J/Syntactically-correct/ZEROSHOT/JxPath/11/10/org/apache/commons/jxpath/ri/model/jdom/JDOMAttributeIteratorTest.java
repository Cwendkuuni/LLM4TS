import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator iterator;

    @Before
    public void setup() {
        Element element = new Element("testElement");
        element.setAttribute(new Attribute("attr1", "value1"));
        element.setAttribute(new Attribute("attr2", "value2"));
        element.setAttribute(new Attribute("attr3", "value3"));
        iterator = new JDOMAttributeIterator(new JDOMAttributePointer(element), new QName("attr1"));
    }

    @Test
    public void testGetNodePointer() {
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(1));
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(-1));
        assertFalse(iterator.setPosition(4));
    }

    private static class JDOMAttributePointer implements NodePointer {
        private Element element;

        public JDOMAttributePointer(Element element) {
            this.element = element;
        }

        @Override
        public Object getNode() {
            return element;
        }

        @Override
        public void setValue(Object value) {
            // Empty implementation
        }

        @Override
        public QName getName() {
            return new QName("attr1");
        }

        @Override
        public boolean isCollection() {
            return false;
        }

        @Override
        public int getLength() {
            return 1;
        }

        @Override
        public NodeIterator attributeIterator(QName qName) {
            return null;
        }

        @Override
        public NodeIterator childIterator(QName qName, boolean b, String s, boolean b1) {
            return null;
        }

        @Override
        public NodePointer getParent() {
            return null;
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
        public int compareChildNodePointers(NodePointer nodePointer, NodePointer nodePointer1) {
            return 0;
        }

        @Override
        public String getNamespaceURI() {
            return null;
        }

        @Override
        public String getPrefix() {
            return null;
        }

        @Override
        public String getLocalName() {
            return null;
        }

        @Override
        public String getNameAsString() {
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
        public void remove() {
            // Empty implementation
        }

        @Override
        public void setValue(String s, Object value) {
            // Empty implementation
        }

        @Override
        public NodePointer createPath(String s) {
            return null;
        }

        @Override
        public NodePointer createChild(String s, String s1, String s2, Object o) {
            return null;
        }

        @Override
        public NodePointer createAttribute(String s, String s1) {
            return null;
        }

        @Override
        public NodePointer createNamespace(String s, String s1) {
            return null;
        }
    }
}