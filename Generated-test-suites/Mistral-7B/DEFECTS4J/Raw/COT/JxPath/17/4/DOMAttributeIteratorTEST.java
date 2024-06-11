package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructorWithElementAndQName() {
        NodePointer parent = new NodePointer(null, new Element() {});
        QName name = new QName("test", "test-namespace", "test-local-name");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testConstructorWithElementAndWildcardQName() {
        NodePointer parent = new NodePointer(null, new Element() {});
        QName name = new QName("*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNextWhenHasAttributes() {
        NodePointer parent = new NodePointer(null, new Element() {
            {
                setAttributeNode(new Attr("test1", "test1-value"));
                setAttributeNode(new Attr("test2", "test2-value"));
            }
        });
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testHasNextWhenNoAttributes() {
        NodePointer parent = new NodePointer(null, new Element() {});
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNodePointerWhenNoMoreAttributes() {
        NodePointer parent = new NodePointer(null, new Element() {
            {
                setAttributeNode(new Attr("test1", "test1-value"));
                setAttributeNode(new Attr("test2", "test2-value"));
            }
        });
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();
        DOMAttributePointer attributePointer = iterator.getNodePointer();
        assertNotNull(attributePointer);
        iterator.next(); // should throw an exception
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parent = new NodePointer(null, new Element() {
            {
                setAttributeNode(new Attr("test1", "test1-value"));
            }
        });
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        DOMAttributePointer attributePointer = iterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals("test1", attributePointer.getNodeName().getName());
        assertEquals(parent, attributePointer.getNodePointer().getParent());
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = new NodePointer(null, new Element() {
            {
                setAttributeNode(new Attr("test1", "test1-value"));
                setAttributeNode(new Attr("test2", "test2-value"));
            }
        });
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPositionWhenValidPosition() {
        NodePointer parent = new NodePointer(null, new Element() {
            {
                setAttributeNode(new Attr("test1", "test1-value"));
                setAttributeNode(new Attr("test2", "test2-value"));
            }
        });
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPositionWhenInvalidPosition() {
        NodePointer parent = new NodePointer(null, new Element() {
            {
                setAttributeNode(new Attr("test1", "test1-value"));
                setAttributeNode(new Attr("test2", "test2-value"));
            }
        });
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testGetAttributeWhenAttributeExists() {
        NodePointer parent = new NodePointer(null, new Element() {
            {
                setAttributeNode(new Attr("test", "test-value"));
            }
        });
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        Attr attribute = iterator.getNodePointer().getNode();
        assertEquals("test-value", attribute.getValue());
    }

    @Test
    public void testGetAttributeWhenAttributeDoesntExist() {
        NodePointer parent = new NodePointer(null, new Element() {});
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer().getNode());
    }

    @Test
    public void testTestAttrWithNamespace() {
        NodePointer parent = new NodePointer(null, new Element() {
            {
                setAttributeNode(new Attr("xmlns:test", "test-namespace-uri"));
                setAttributeNode(new Attr("test", "test-value"));
            }
        });
        QName name = new QName("{http://test}", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertTrue(iterator.testAttr(iterator.getNodePointer().getNode()));
    }

    @Test
    public void testTestAttrWithoutNamespace() {
        NodePointer parent = new NodePointer(null, new Element() {
            {
                setAttributeNode(new Attr("test", "test-value"));
            }
        });
        QName name = new QName("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertTrue(iterator.testAttr(iterator.getNodePointer().getNode()));
    }

    @Test
    public void testTestAttrWithDifferentNamespace() {
        NodePointer parent = new NodePointer(null, new Element() {
            {
                setAttributeNode(new Attr("xmlns:test1", "test1-namespace-uri"));
                setAttributeNode(new Attr("xmlns:test2", "test2-namespace-uri"));
                setAttributeNode(new Attr("test", "test-value"));
            }
        });
        QName name = new QName("{http://test2}", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertFalse(iterator.testAttr(iterator.getNodePointer().getNode()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTestAttrWithNullPrefix() {
        NodePointer parent = new NodePointer(null, new Element() {});
        DOMAttribeIterator iterator = new DOMAttribeIterator(parent, null);
        assertNull(iterator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTestAttrWithNullLocalName() {
        NodePointer parent = new NodePointer(null, new Element() {});
        DOMAttribeIterator iterator = new DOMAttribeIterator(parent, null);
        assertNull(iterator);
    }
}