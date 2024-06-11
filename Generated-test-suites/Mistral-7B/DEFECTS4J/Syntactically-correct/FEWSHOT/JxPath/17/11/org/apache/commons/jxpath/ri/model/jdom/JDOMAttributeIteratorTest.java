package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyIterator() {
        NodeIterator it = new JDOMAttributeIterator(new NodePointer(null), null);
        assertNull(it.getNodePointer());
        assertEquals(-1, it.getPosition());
    }

    @Test
    public void testIteratorForSingleAttribute() {
        Element root = new Element("root");
        Attribute attr = new Attribute("name", "value", "prefix:uri");
        root.addAttribute(attr);

        NodeIterator it = new JDOMAttributeIterator(new NodePointer(root), null);
        assertNotNull(it.getNodePointer());
        assertSame(root, it.getNodePointer().getNode().getParentElement());
        assertEquals(0, it.getPosition());
        Attribute attrFromIterator = (Attribute) it.getNodePointer().getNode();
        assertEquals("name", attrFromIterator.getName());
        assertEquals("prefix:uri", attrFromIterator.getNamespace().getURI());
        assertEquals("value", attrFromIterator.getValue());
        assertNull(it.getNodePointer(1));
    }

    @Test
    public void testIteratorForMultipleAttributes() {
        Element root = new Element("root");
        Attribute attr1 = new Attribute("name1", "value1", "prefix1:uri");
        Attribute attr2 = new Attribute("name2", "value2", "prefix2:uri");
        root.addAttribute(attr1);
        root.addAttribute(attr2);

        NodeIterator it = new JDOMAttributeIterator(new NodePointer(root), null);
        assertNotNull(it.getNodePointer());
        assertSame(root, it.getNodePointer().getNode().getParentElement());
        assertEquals(0, it.getPosition());
        Attribute attr1FromIterator = (Attribute) it.getNodePointer().getNode();
        assertEquals("name1", attr1FromIterator.getName());
        assertEquals("prefix1:uri", attr1FromIterator.getNamespace().getURI());
        assertEquals("value1", attr1FromIterator.getValue());
        assertNotNull(it.getNodePointer(1));
        Attribute attr2FromIterator = (Attribute) it.getNodePointer(1).getNode();
        assertEquals("name2", attr2FromIterator.getName());
        assertEquals("prefix2:uri", attr2FromIterator.getNamespace().getURI());
        assertEquals("value2", attr2FromIterator.getValue());
    }

    @Test
    public void testIteratorForNamespaceOnlyName() {
        Element root = new Element("root");
        NodeIterator it = new JDOMAttributeIterator(new NodePointer(root), new QName(":*"));
        assertNotNull(it.getNodePointer());
        assertSame(root, it.getNodePointer().getNode().getParentElement());
        assertEquals(0, it.getPosition());
        Attribute attrFromIterator = (Attribute) it.getNodePointer().getNode();
        assertNull(attrFromIterator.getName());
        assertSame(Namespace.NO_NAMESPACE, attrFromIterator.getNamespace());
        assertNull(it.getNodePointer(1));
    }

    @Test
    public void testIteratorForNonExistingAttribute() {
        Element root = new Element("root");
        NodeIterator it = new JDOMAttributeIterator(new NodePointer(root), new QName("nonExistingName"));
        assertNull(it.getNodePointer());
        assertEquals(-1, it.getPosition());
    }

    @Test
    public void testIteratorForAttributesWithDifferentNamespaces() {
        Element root = new Element("root");
        Attribute attr1 = new Attribute("name1", "value1", new Namespace("prefix1", "uri1"));
        Attribute attr2 = new Attribute("name2", "value2", new Namespace("prefix2", "uri2"));
        root.addAttribute(attr1);
        root.addAttribute(attr2);

        NodeIterator it = new JDOMAttributeIterator(new NodePointer(root), null);
        assertNotNull(it.getNodePointer());
        assertSame(root, it.getNodePointer().getNode().getParentElement());
        assertEquals(0, it.getPosition());
        Attribute attr1FromIterator = (Attribute) it.getNodePointer().getNode();
        assertEquals("name1", attr1FromIterator.getName());
        assertSame(attr1.getNamespace(), attr1FromIterator.getNamespace());
        assertEquals("value1", attr1FromIterator.getValue());
        assertNotNull(it.getNodePointer(1));
        Attribute attr2FromIterator = (Attribute) it.getNodePointer(1).getNode();
        assertEquals("name2", attr2FromIterator.getName());
        assertSame(attr2.getNamespace(), attr2FromIterator.getNamespace());
        assertEquals("value2", attr2FromIterator.getValue());
    }

    @Test
    public void testIteratorForEmptyElement() {
        Element root = new Element("root");
        NodeIterator it = new JDOMAttributeIterator(new NodePointer(root), null);
        assertNull(it.getNodePointer());
        assertEquals(-1, it.getPosition());
    }

    @Test
    public void testIteratorForNullParent() {
        NodePointer parent = null;
        try {
            new JDOMAttributeIterator(parent, null);
        } catch (NullPointerException e) {
            // expected
        }
    }

}