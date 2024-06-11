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

    private NodePointer parent;
    private QName name;

    @Before
    public void setUp() {
        parent = new NodePointer();
        name = new QName("test");
    }

    @Test
    public void testConstructor() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        assertEquals(parent, iterator.getParent());
        assertEquals(name, iterator.getName());
    }

    @Test
    public void testGetNodePointer() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);

        parent.setNode(new Element() {
            @Override
            public String getNodeName() {
                return "testElement";
            }

            @Override
            public short getNodeType() {
                return Node.ELEMENT_NODE;
            }

            @Override
            public Node getParentNode() {
                return null;
            }
            
            @Override
            public NamedNodeMap getAttributes() {
                return new NamedNodeMap() {
                    @Override
                    public Node item(int index) {
                        return null;
                    }

                    @Override
                    public int getLength() {
                        return 0;
                    }

                    @Override
                    public Node getNamedItem(String name) {
                        return null;
                    }

                    @Override
                    public Node getNamedItemNS(String namespaceURI, String localName) {
                        return new Attr() {
                            @Override
                            public String getName() {
                                return "testAttribute";
                            }

                            @Override
                            public String getNodeValue() {
                                return null;
                            }

                            @Override
                            public short getNodeType() {
                                return Node.ATTRIBUTE_NODE;
                            }

                            @Override
                            public Node getParentNode() {
                                return null;
                            }

                            @Override
                            public String getNamespaceURI() {
                                return null;
                            }

                            @Override
                            public boolean isId() {
                                return false;
                            }

                            // Other overridden methods

                        };
                    }

                    // Other overridden methods

                };
            }

            // Other overridden methods

        });
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(DOMAttributePointer.class, nodePointer.getClass());
    }

    @Test
    public void testGetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(3);
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertTrue(iterator.setPosition(1));
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(2));
    }

}