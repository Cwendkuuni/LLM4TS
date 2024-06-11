package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class DOMAttributeIteratorTest {
    private DOMAttributeIterator iterator;
    private NodePointer parent;

    @Before
    public void setUp() {
        parent = new NodePointer() {
            public Object getNode() {
                return new Element() {
                    public String getNamespaceURI() {
                        return null;
                    }
                    public String getNodeName() {
                        return null;
                    }
                    public String getLocalName() {
                        return null;
                    }
                    public short getNodeType() {
                        return 0;
                    }
                    public String getNodeValue() {
                        return null;
                    }
                    public void setNodeValue(String value) {}
                    public Node getParentNode() {
                        return null;
                    }
                    public Node getFirstChild() {
                        return null;
                    }
                    public Node getLastChild() {
                        return null;
                    }
                    public Node getPreviousSibling() {
                        return null;
                    }
                    public Node getNextSibling() {
                        return null;
                    }
                    public NamedNodeMap getAttributes() {
                        return new NamedNodeMap() {
                            public Node getNamedItem(String name) {
                                return null;
                            }
                            public Node setNamedItem(Node arg) {
                                return null;
                            }
                            public Node removeNamedItem(String name) {
                                return null;
                            }
                            public Node item(int index) {
                                return null;
                            }
                            public int getLength() {
                                return 0;
                            }
                            public Node getNamedItemNS(String namespaceURI, String localName) {
                                return null;
                            }
                            public Node setNamedItemNS(Node arg) {
                                return null;
                            }
                            public Node removeNamedItemNS(String namespaceURI,
                                    String localName) {
                                return null;
                            }
                        };
                    }
                    public boolean hasChildNodes() {
                        return false;
                    }
                    public Node cloneNode(boolean deep) {
                        return null;
                    }
                    public void normalize() {}
                    public boolean isSupported(String feature, String version) {
                        return false;
                    }
                    public String getNamespaceURI(String prefix) {
                        return null;
                    }
                    public String getPrefix() {
                        return null;
                    }
                    public void setPrefix(String prefix) {}
                    public String getLocalName(String prefix) {
                        return null;
                    }
                    public boolean hasAttributes() {
                        return false;
                    }
                };
            }

            public Object getBaseValue() {
                return null;
            }

            public QName getName() {
                return null;
            }

            public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
                return 0;
            }

            public boolean isCollection() {
                return false;
            }

            public boolean isLeaf() {
                return false;
            }

            public boolean isActual() {
                return false;
            }

            public boolean isNode() {
                return false;
            }

            public boolean isContainer() {
                return false;
            }

            public boolean isCollectionElement() {
                return false;
            }
        };
    }

    // TODO: Test for constructor

    @Test
    public void testGetNodePointer() {
        iterator = new DOMAttributeIterator(parent, null);
        assertNull(iterator.getNodePointer());
        
        // TODO: Add more test cases
    }

    // TODO: Add more test methods

    @Test
    public void testGetPosition() {
        iterator = new DOMAttributeIterator(parent, null);
        assertEquals(0, iterator.getPosition());
        
        // TODO: Add more test cases
    }

    @Test
    public void testSetPosition() {
        iterator = new DOMAttributeIterator(parent, null);
        assertEquals(0, iterator.setPosition(0));
        assertEquals(0, iterator.getPosition());
        
        assertEquals(1, iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        
        assertEquals(0, iterator.setPosition(-1));
        assertEquals(0, iterator.getPosition());
        
        assertEquals(2, iterator.setPosition(2));
        assertEquals(0, iterator.getPosition());
        
        // TODO: Add more test cases
    }
}