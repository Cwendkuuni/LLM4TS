package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    private DOMAttributeIterator iterator;
    private NodePointer parent;
    private QName name;

    @Before
    public void setup() {
        parent = new DOMNodePointer();
        name = new QName("test");
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testConstructor() {
        assertEquals(parent, iterator.parent);
        assertEquals(name, iterator.name);
        assertEquals(0, iterator.position);
        assertNotNull(iterator.attributes);
    }

    @Test
    public void testTestAttr() {
        Element element = new Element();
        Attr attr = new Attr();
        attr.setName("test:attr");
        element.addAttribute(attr);

        assertTrue(iterator.testAttr(attr, name));
    }

    @Test
    public void testEqualStrings() {
        assertTrue(DOMAttributeIterator.equalStrings("test", "test"));
    }

    @Test
    public void testGetAttribute() {
        Element element = new Element();

        assertNull(iterator.getAttribute(element, name));
    }

    @Test
    public void testGetNodePointer() {
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
    }
}