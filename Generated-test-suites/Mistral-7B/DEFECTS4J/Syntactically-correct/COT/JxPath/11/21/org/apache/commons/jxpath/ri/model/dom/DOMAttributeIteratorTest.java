import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new NodePointer(null, null, 0);
        QName name = new QName("test", "test", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer_noAttribute() {
        NodePointer parent = new NodePointer(null, null, 0);
        QName name = new QName("test", "test", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetNodePointer_firstAttribute() {
        NodePointer parent = new NodePointer(null, new Element() {
            @Override
            public String getTagName() {
                return "test";
            }

            @Override
            public NamedNodeMap getAttributes() {
                NamedNodeMap map = new org.w3c.dom.NamedNodeMap() {
                    @Override
                    public int getLength() {
                        return 1;
                    }

                    @Override
                    public Node item(int index) {
                        return new Attr() {
                            @Override
                            public String getValue() {
                                return "test";
                            }
                        };
                    }
                };
                return map;
            }
        }, new QName("test", "test", null));
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", "test", null));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(Attr.class, nodePointer.getNodeClass());
        assertEquals("test", nodePointer.getNodeName().getLocalPart());
        assertEquals("test", nodePointer.getNodeValue());
    }

    @Test
    public void testGetNodePointer_lastAttribute() {
        NodePointer parent = new NodePointer(null, new Element() {
            @Override
            public String getTagName() {
                return "test";
            }

            @Override
            public NamedNodeMap getAttributes() {
                NamedNodeMap map = new org.w3c.dom.NamedNodeMap() {
                    @Override
                    public int getLength() {
                        return 2;
                    }

                    @Override
                    public Node item(int index) {
                        Attr attr = new Attr();
                        attr.setValue("test");
                        if (index == 1) {
                            attr.setValue("otherTest");
                        }
                        return attr;
                    }
                };
                return map;
            }
        }, new QName("test", "*", null));
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", "*", null));
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(Attr.class, nodePointer.getNodeClass());
        assertEquals("otherTest", nodePointer.getNodeName().getLocalPart());
        assertEquals("otherTest", nodePointer.getNodeValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNodePointer_outOfBounds() {
        NodePointer parent = new NodePointer(null, new Element() {
            @Override
            public String getTagName() {
                return "test";
            }

            @Override
            public NamedNodeMap getAttributes() {
                NamedNodeMap map = new org.w3c.dom.NamedNodeMap() {
                    @Override
                    public int getLength() {
                        return 1;
                    }

                    @Override
                    public Node item(int index) {
                        return new Attr() {
                            @Override
                            public String getValue() {
                                return "test";
                            }
                        };
                    }
                };
                return map;
            }
        }, new QName("test", "test", null));
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", "test", null));
        iterator.next();
        iterator.next();
        iterator.next();
        NodePointer nodePointer = iterator.getNodePointer();
        fail("Expected IndexOutOfBoundsException");
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = new NodePointer(null, new Element() {
            @Override
            public String getTagName() {
                return "test";
            }

            @Override
            public NamedNodeMap getAttributes() {
                NamedNodeMap map = new org.w3c.dom.NamedNodeMap() {
                    @Override
                    public int getLength() {
                        return 3;
                    }
                };
                return map;
            }
        }, new QName("test", "*", null));
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", "*", null));
        assertEquals(0, iterator.getPosition());
        iterator.next();
        assertEquals(1, iterator.getPosition());
        iterator.next();
        assertEquals(2, iterator.getPosition());
        iterator.next();
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        NodePointer parent = new NodePointer(null, new Element() {
            @Override
            public String getTagName() {
                return "test";
            }

            @Override
            public NamedNodeMap getAttributes() {
                NamedNodeMap map = new org.w3c.dom.NamedNodeMap() {
                    @Override
                    public int getLength() {
                        return 3;
                    }
                };
                return map;
            }
        }, new QName("test", "*", null));
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", "*", null));
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(-1));
        assertFalse(iterator.setPosition(4));
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPosition_illegalArgument() {
        NodePointer parent = new NodePointer(null, new Element() {
            @Override
            public String getTagName() {
                return "test";
            }

            @Override
            public NamedNodeMap getAttributes() {
                NamedNodeMap map = new org.w3c.dom.NamedNodeMap() {
                    @Override
                    public int getLength() {
                        return 3;
                    }
                };
                return map;
            }
        }, new QName("test", "*", null));
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("test", "*", null));
        assertFalse(iterator.setPosition(Integer.MIN_VALUE));
    }

    @Test
    public void testEquals() {
        NodePointer parent1 = new NodePointer(null, new Element() {
            @Override
            public String getTagName() {
                return "test";
            }

            @Override
            public NamedNodeMap getAttributes() {
                NamedNodeMap map = new org.w3c.dom.NamedNodeMap() {
                    @Override
                    public int getLength() {
                        return 3;
                    }
                };
                return map;
            }
        }, new QName("test", "*", null));
        NodePointer parent2 = new NodePointer(null, new Element() {
            @Override
            public String getTagName() {
                return "test";
            }

            @Override
            public NamedNodeMap getAttributes() {
                NamedNodeMap map = new org.w3c.dom.NamedNodeMap() {
                    @Override
                    public int getLength() {
                        return 3;
                    }
                };
                return map;
            }
        }, new QName("test", "*", null));
        DOMAttributeIterator iterator1 = new DOMAttributeIterator(parent1, new QName("test", "*", null));
        DOMAttributeIterator iterator2 = new DOMAttributeIterator(parent2, new QName("test", "*", null));
        DOMAttributeIterator iterator3 = new DOMAttributeIterator(parent1, new QName("test", "test", null));
        assertNotEquals(iterator1, null);
        assertNotEquals(iterator1, iterator2);
        assertNotEquals(iterator1, iterator3);
        assertTrue(iterator1.equals(iterator1));
        assertTrue(iterator1.equals(iterator1));
        assertTrue(iterator1.equals(iterator1));
        DOMAttributeIterator iterator4 = new DOMAttributeIterator(iterator1);
        assertTrue(iterator1.equals(iterator4));
        assertTrue(iterator4.equals(iterator1));
    }
}