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
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    private DOMAttributeIterator iterator;
    private NodePointer parent;
    private QName name;

    @Before
    public void setUp() {
        parent = new NodePointer();
        name = new QName("test", "attr");
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testConstructor() {
        assertEquals(parent, iterator.parent);
        assertEquals(name, iterator.name);
        assertEquals(0, iterator.position);
        assertEquals(0, iterator.attributes.size());
    }

    @Test
    public void testGetAttribute() {
        Element element = new Element();
        Attr attr = new Attr();
        element.setAttributeNode(attr);

        assertNull(iterator.getAttribute(element, name));

        name.setPrefix("test");
        attr.setNamespaceURI("http://www.example.com");
        attr.setLocalName("attr");
        element.setAttributeNodeNS(attr);

        assertEquals(attr, iterator.getAttribute(element, name));
    }

    @Test
    public void testEqualStrings() {
        String s1 = "string1";
        String s2 = "string2";
        String s3 = "string1";

        assertFalse(iterator.equalStrings(s1, s2));
        assertFalse(iterator.equalStrings(s3, s2));
        assertTrue(iterator.equalStrings(s1, s3));
    }

    @Test
    public void testTestAttr() {
        Attr attr = new Attr();
        QName testName = new QName("test", "attr");

        attr.setPrefix("test");
        attr.setLocalName("attr");

        assertTrue(iterator.testAttr(attr, testName));

        testName.setPrefix("");
        testName.setName("attr");
        assertFalse(iterator.testAttr(attr, testName));

        testName.setName("nonattr");
        assertFalse(iterator.testAttr(attr, testName));

        attr.setNamespaceURI("http://www.example.com");
        testName.setPrefix("test");
        testName.setName("attr");

        assertTrue(iterator.testAttr(attr, testName));

        QName differentNamespaceTestName = new QName("test", "attr2");
        assertFalse(iterator.testAttr(attr, differentNamespaceTestName));
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());

        iterator.position = 2;
        assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertFalse(iterator.setPosition(0));

        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.position);

        assertFalse(iterator.setPosition(2));

        iterator.attributes.add(new Attr());
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.position);
    }

    @Test
    public void testGetNodePointer() {
        parent.setNode(new Node());
        iterator.attributes.add(new Attr());

        NodePointer pointer = iterator.getNodePointer();

        assertEquals(parent, pointer.parent);
        assertEquals(iterator.attributes.get(0), ((DOMAttributePointer) pointer).attribute);
    }
}