package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIteratorEmpty() {
        Element element = getElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(element), new QName(""));
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        iterator.close();
    }

    @Test
    public void testDOMAttributeIteratorSingleAttribute() {
        Element element = getElement();
        Attr attribute = element.createAttribute("test");
        element.setAttributeNode(attribute);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(element), new QName("test"));
        assertTrue(iterator.hasNext());
        DOMAttributePointer next = (DOMAttributePointer) iterator.next();
        assertTrue(next.getNode().getNodeValue().equals(""));
        iterator.close();
    }

    @Test
    public void testDOMAttributeIteratorMultipleAttributes() {
        Element element = getElement();
        Attr attribute1 = element.createAttribute("test1");
        Attr attribute2 = element.createAttribute("test2");
        attribute1.setValue("value1");
        attribute2.setValue("value2");
        element.setAttributeNode(attribute1);
        element.setAttributeNode(attribute2);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(element), new QName("test"));
        assertTrue(iterator.hasNext());
        DOMAttributePointer next1 = (DOMAttributePointer) iterator.next();
        assertTrue(next1.getNode().getNodeValue().equals("value1"));
        assertTrue(iterator.hasNext());
        DOMAttributePointer next2 = (DOMAttributePointer) iterator.next();
        assertTrue(next2.getNode().getNodeValue().equals("value2"));
        iterator.close();
    }

    @Test
    public void testDOMAttributeIteratorNamespace() {
        Document doc = getDocument();
        Element element = doc.createElement("root");
        Attr attribute = doc.createAttributeNS("http://test.com", "test");
        attribute.setValue("value");
        element.setAttributeNodeNS(attribute, "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(element), new QName("{http://test.com}test"));
        assertTrue(iterator.hasNext());
        DOMAttributePointer next = (DOMAttributePointer) iterator.next();
        assertTrue(next.getNode().getNodeValue().equals("value"));
        iterator.close();
    }

    @Test
    public void testDOMAttributeIteratorNoMatch() {
        Element element = getElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(element), new QName("nonexistent"));
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    @Test
    public void testDOMAttributeIteratorNullName() {
        Element element = getElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMNodePointer(element), null);
        assertTrue(iterator.hasNext());
        assertNull(iterator.next());
        iterator.close();
    }

    private Element getElement() {
        Document doc = getDocument();
        return (Element) doc.getDocumentElement();
    }

    private Document getDocument() {
        return (Document) new DOMParser().parseFromString("<root></root>", null);
    }

}