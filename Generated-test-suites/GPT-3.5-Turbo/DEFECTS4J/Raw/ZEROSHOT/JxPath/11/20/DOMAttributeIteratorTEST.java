import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    private DOMAttributeIterator attributeIterator;
    private Element element;
    private Attr attribute;

    @Before
    public void setup() {
        NodePointer parent = new TestNodePointer();
        QName name = new QName("testName", "testPrefix", "testNamespace");
        attributeIterator = new DOMAttributeIterator(parent, name);
        element = new TestElement();
        attribute = new TestAttr();
        element.setAttributeNode(attribute);
    }

    @Test
    public void testConstructor() {
        assertNotNull(attributeIterator);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = attributeIterator.getNodePointer();
        assertTrue(nodePointer instanceof DOMAttributePointer);
        DOMAttributePointer attributePointer = (DOMAttributePointer) nodePointer;
        assertEquals(attribute, attributePointer.getAttribute());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertFalse(attributeIterator.setPosition(0));
        assertTrue(attributeIterator.setPosition(1));
    }
    
    private class TestNodePointer extends NodePointer {
        public TestNodePointer() {
            super(null);
        }
        
        @Override
        public Node getNode() {
            return element;
        }
        
        @Override
        public String getNamespaceURI(String prefix) {
            return null;
        }
    }
    
    private class TestElement extends Element {
        @Override
        public String getAttribute(String name) {
            return "";
        }

        @Override
        public Attr getAttributeNode(String name) {
            return null;
        }

        @Override
        public Attr setAttributeNode(Attr newAttr) {
            return null;
        }

        @Override
        public void removeAttribute(String name) {
            
        }

        @Override
        public NamedNodeMap getAttributes() {
            NamedNodeMap map = new NamedNodeMap() {
                @Override
                public int getLength() {
                    return 1;
                }

                @Override
                public Node item(int index) {
                    return attribute;
                }

                @Override
                public Node getNamedItem(String name) {
                    return attribute;
                }

                @Override
                public Node setNamedItem(Node arg) {
                    return null;
                }

                @Override
                public Node removeNamedItem(String name) {
                    return null;
                }

                @Override
                public Node getNamedItemNS(String namespaceURI, String localName) {
                    return null;
                }

                @Override
                public Node setNamedItemNS(Node arg) {
                    return null;
                }

                @Override
                public Node removeNamedItemNS(String namespaceURI, String localName) {
                    return null;
                }
            };
            return map;
        }
    }
    
    private class TestAttr extends Attr {
        @Override
        public String getName() {
            return "";
        }

        @Override
        public void setValue(String value) {
            
        }
    }
}