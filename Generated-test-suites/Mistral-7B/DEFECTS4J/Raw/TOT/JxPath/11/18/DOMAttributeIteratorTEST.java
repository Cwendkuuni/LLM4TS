package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test DOMAttributeIterator class.
 */
public class DOMAttributeIteratorTest {

    private Document doc;
    private Element root;
    private NodePointer parentPointer;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() throws Exception {
        doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        root = doc.createElement("root");
        doc.appendChild(root);

        Element child1 = doc.createElement("child");
        child1.setAttribute("name1", "value1");
        child1.setAttribute("xml:foo", "bar");
        root.appendChild(child1);

        Element child2 = doc.createElement("child");
        child2.setAttribute("name2", "value2");
        child2.setAttribute("xmlns:baz", "http://example.com/baz");
        child2.setAttribute("name3", "value3");
        child2.setAttribute("xmlns:qux", "http://example.com/qux");
        root.appendChild(child2);

        parentPointer = new DOMPointNodePointer(doc, root);
        iterator = new DOMAttributeIterator(parentPointer, new QName("*"));
    }

    @Test
    public void testGetFirstAttribute() {
        NodePointer attrPointer = iterator.getNodePointer();
        assertNotNull(attrPointer);
        assertEquals("name1", attrPointer.getNodeName().getName());
        assertEquals("value1", attrPointer.getNodeValue());
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testGetNextAttribute() {
        iterator.next();
        NodePointer attrPointer = iterator.getNodePointer();
        assertEquals("xml:foo", attrPointer.getNodeName().getName());
        assertEquals("bar", attrPointer.getNodeValue());
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testGetNextAttributeNull() {
        iterator.next();
        iterator.next();
        NodePointer attrPointer = iterator.getNodePointer();
        assertNull(attrPointer);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetAttributeByName() {
        NodePointer attrPointer = iterator.getNodePointer();
        assertEquals("name1", attrPointer.getNodeName().getName());

        iterator.next();
        attrPointer = iterator.getNodePointer();
        assertEquals("xml:foo", attrPointer.getNodeName().getName());

        NodePointer attrPointerByName = new DOMAttributePointer(parentPointer, new QName("xml:foo"));
        NodePointer nextAttrByName = iterator.getNodePointer(attrPointerByName);
        assertEquals("xml:foo", nextAttrByName.getNodeName().getName());
        assertEquals("bar", nextAttrByName.getNodeValue());

        attrPointerByName = new DOMAttributePointer(parentPointer, new QName("name2"));
        assertNull(iterator.getNodePointer(attrPointerByName));
    }

    @Test
    public void testGetAttributeByPrefixAndLocalName() {
        NodePointer attrPointer = iterator.getNodePointer();
        assertEquals("name1", attrPointer.getNodeName().getName());

        iterator.next();
        attrPointer = iterator.getNodePointer();
        assertEquals("xml:foo", attrPointer.getNodeName().getName());

        NodePointer attrPointerByPrefixAndLocalName = new DOMAttributePointer(parentPointer, new QName("xml", "foo"));
        NodePointer nextAttrByPrefixAndLocalName = iterator.getNodePointer(attrPointerByPrefixAndLocalName);
        assertEquals("xml:foo", nextAttrByPrefixAndLocalName.getNodeName().getName());
        assertEquals("bar", nextAttrByPrefixAndLocalName.getNodeValue());

        attrPointerByPrefixAndLocalName = new DOMAttributePointer(parentPointer, new QName("xmlns", "qux"));
        assertNull(iterator.getNodePointer(attrPointerByPrefixAndLocalName));
    }

    @Test
    public void testGetAttributeByNamespaceURIAndLocalName() {
        NodePointer attrPointer = iterator.getNodePointer();
        assertEquals("name1", attrPointer.getNodeName().getName());

        iterator.next();
        attrPointer = iterator.getNodePointer();
        assertEquals("xml:foo", attrPointer.getNodeName().getName());

        NodePointer attrPointerByNamespaceURIAndLocalName = new DOMAttributePointer(parentPointer, new QName("http://example.com/baz", "foo"));
        NodePointer nextAttrByNamespaceURIAndLocalName = iterator.getNodePointer(attrPointerByNamespaceURIAndLocalName);
        assertEquals("xml:foo", nextAttrByNamespaceURIAndLocalName.getNodeName().getName());
        assertEquals("bar", nextAttrByNamespaceURIAndLocalName.getNodeValue());

        attrPointerByNamespaceURIAndLocalName = new DOMAttributePointer(parentPointer, new QName("http://example.com/qux", "bar"));
        assertNull(iterator.getNodePointer(attrPointerByNamespaceURIAndLocalName));
    }

    @Test
    public void testGetNonExistentAttribute() {
        NodePointer attrPointer = new DOMAttrributePointer(parentPointer, new QName("xmlns", "nonExistent"));
        assertNull(iterator.getNodePointer(attrPointer));
    }

    @Test
    public void testGetNextAttributeWithEmptyIterate() {
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        NodePointer attrPointer = iterator.getNodePointer();
        assertNull(attrPointer);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetNextAttributeWithRemove() {
        iterator.next();
        iterator.next();

        NodePointer attrToRemove = iterator.getNodePointer();
        attrToRemove.removeNodeFromParent();
        attrToRemove = new DOMAttributePointer(parentPointer, new QName("xml:foo"));

        iterator.next();
        NodePointer nextAttr = iterator.getNodePointer();
        assertEquals("name2", nextAttr.getNodeName().getName());
        assertEquals("value2", nextAttr.getNodeValue());
    }

    @Test
    public void testGetNextAttributeWithAdd() {
        iterator.next();
        iterator.next();

        QName newAttrName = new QName("xmlns", "newAttr");
        Element newAttrElement = doc.createElement("newAttribute");
        newAttrElement.setAttributeNS(newAttrName.getNamespaceURI(), newAttrName.getLocalPart(), "value");
        root.appendChild(newAttrElement);
        parentPointer = new DOMPointNodePointer(doc, root);

        iterator.next();
        NodePointer nextAttr = iterator.getNodePointer();
        assertEquals("xmlns:newAttr", nextAttr.getNodeName().getNamespaceURI() + ":" + nextAttr.getNodeName().getLocalName());
        assertEquals("value", nextAttr.getNodeValue());
    }
}