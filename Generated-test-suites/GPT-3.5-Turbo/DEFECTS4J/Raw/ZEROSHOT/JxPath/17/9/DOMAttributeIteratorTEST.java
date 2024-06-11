import org.junit.Test;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Node;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator() {
        NodePointer parent = createNodePointer();
        QName name = createQName();

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        assertNotNull(iterator);
        assertEquals(parent, iterator.parent);
        assertEquals(name, iterator.name);
        assertNotNull(iterator.attributes);
        assertEquals(0, iterator.position);
    }

    @Test
    public void testTestAttr() {
        Attr attrWithPrefix = createAttr("prefix:name", "value");
        Attr attrWithoutPrefix = createAttr("name", "value");
        Attr attrWithNamespace = createAttr("prefix:name", "value");
        Attr attrWithoutNamespace = createAttr("name", "value");

        NodePointer parent = createNodePointer();
        QName name = createQName();

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        assertTrue(iterator.testAttr(attrWithPrefix));
        assertTrue(iterator.testAttr(attrWithoutPrefix));
        assertFalse(iterator.testAttr(attrWithNamespace));
        assertFalse(iterator.testAttr(attrWithoutNamespace));
    }

    @Test
    public void testGetAttribute() {
        NodePointer parent = createNodePointer();
        QName name = createQName();

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        Element element = createMockedElement();
        Attr attributeWithPrefix = createAttr("prefix:name", "value");
        Attr attributeWithoutPrefix = createAttr("name", "value");

        Attr result1 = iterator.getAttribute(element, name);
        Attr result2 = iterator.getAttribute(element, name);

        assertSame(attributeWithPrefix, result1);
        assertNull(result2);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parent = createNodePointer();
        QName name = createQName();

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        NodePointer result = iterator.getNodePointer();

        assertNotNull(result);
        assertEquals(parent, result.parent);
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = createNodePointer();
        QName name = createQName();

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        int result = iterator.getPosition();

        assertEquals(0, result);
    }

    @Test
    public void testSetPosition() {
        NodePointer parent = createNodePointer();
        QName name = createQName();

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        boolean result1 = iterator.setPosition(1);
        boolean result2 = iterator.setPosition(2);
        boolean result3 = iterator.setPosition(0);

        assertTrue(result1);
        assertFalse(result2);
        assertFalse(result3);
        assertEquals(1, iterator.position);
    }

    private NodePointer createNodePointer() {
        return new NodePointer() {
            @Override
            public Node getNode() {
                return null;
            }

            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public void setValue(Object value) { }

            @Override
            public boolean isLeaf() {
                return false;
            }

            @Override
            public boolean isCollection() {
                return false;
            }

            @Override
            public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
                return 0;
            }
        };
    }

    private QName createQName() {
        return new QName("", "");
    }

    private Attr createAttr(final String name, final String value) {
        return new Attr() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getValue() {
                return value;
            }

            @Override
            public void setValue(String value) { }

            @Override
            public Element getOwnerElement() {
                return null;
            }

            @Override
            public boolean getSpecified() {
                return false;
            }

            @Override
            public boolean isId() {
                return false;
            }

            @Override 
            public String getNodeName() {
                return null;
            }

            @Override 
            public String getNodeValue() {
                return null;
            }

            @Override 
            public void setNodeValue(String nodeValue) { }

            @Override 
            public short getNodeType() {
                return 0;
            }

            @Override 
            public Node getParentNode() {
                return null;
            }

            @Override 
            public NodeList getChildNodes() {
                return null;
            }

            @Override 
            public Node getFirstChild() {
                return null;
            }

            @Override 
            public Node getLastChild() {
                return null;
            }

            @Override 
            public Node getPreviousSibling() {
                return null;
            }

            @Override 
            public Node getNextSibling() {
                return null;
            }

            @Override 
            public NamedNodeMap getAttributes() {
                return null;
            }

            @Override 
            public Document getOwnerDocument() {
                return null;
            }

            @Override 
            public Node insertBefore(Node newChild, Node refChild) throws DOMException {
                return null;
            }

            @Override 
            public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
                return null;
            }

            @Override 
            public Node removeChild(Node oldChild) throws DOMException {
                return null;
            }

            @Override 
            public Node appendChild(Node newChild) throws DOMException {
                return null;
            }

            @Override 
            public boolean hasChildNodes() {
                return false;
            }

            @Override 
            public Node cloneNode(boolean deep) {
                return null;
            }

            @Override 
            public void normalize() { }

            @Override 
            public boolean isSupported(String feature, String version) {
                return false;
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
            public void setPrefix(String prefix) throws DOMException { }

            @Override 
            public String getLocalName() {
                return null;
            }

            @Override 
            public boolean hasAttributes() {
                return false;
            }
        };
    }

    private Element createMockedElement() {
        return new Element() {
            @Override
            public String getTagName() {
                return null;
            }

            @Override 
            public String getNodeName() {
                return null;
            }

            @Override 
            public String getNodeValue() {
                return null;
            }

            @Override 
            public void setNodeValue(String nodeValue) { }

            @Override 
            public short getNodeType() {
                return 0;
            }

            @Override 
            public Node getParentNode() {
                return null;
            }

            @Override 
            public NodeList getChildNodes() {
                return null;
            }

            @Override 
            public Node getFirstChild() {
                return null;
            }

            @Override 
            public Node getLastChild() {
                return null;
            }

            @Override 
            public Node getPreviousSibling() {
                return null;
            }

            @Override 
            public Node getNextSibling() {
                return null;
            }

            @Override 
            public NamedNodeMap getAttributes() {
                return null;
            }

            @Override 
            public Document getOwnerDocument() {
                return null;
            }

            @Override 
            public Node insertBefore(Node newChild, Node refChild) throws DOMException {
                return null;
            }

            @Override 
            public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
                return null;
            }

            @Override 
            public Node removeChild(Node oldChild) throws DOMException {
                return null;
            }

            @Override 
            public Node appendChild(Node newChild) throws DOMException {
                return null;
            }

            @Override 
            public boolean hasChildNodes() {
                return false;
            }

            @Override 
            public Node cloneNode(boolean deep) {
                return null;
            }

            @Override 
            public void normalize() { }

            @Override 
            public boolean isSupported(String feature, String version) {
                return false;
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
            public void setPrefix(String prefix) throws DOMException { }

            @Override 
            public String getLocalName() {
                return null;
            }

            @Override 
            public boolean hasAttributes() {
                return false;
            }
        };
    }
}