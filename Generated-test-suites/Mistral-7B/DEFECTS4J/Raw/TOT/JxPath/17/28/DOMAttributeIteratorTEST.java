package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Attr;

import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

    private NodePointer root;
    private Element element;
    private DOMAttributeIterator iterator;

    @Before
    public void setup() {
        Document doc = createDocument();
        element = doc.createElement("root");
        doc.getDocumentElement().appendChild(element);

        Attr attr1 = createAttribute("xmlns:x", "http://www.example.com/x");
        element.setAttributeNode(attr1);

        Attr attr2 = createAttribute("name1", "value1");
        element.setAttributeNode(attr2);

        Attr attr3 = createAttribute("name2", "value2");
        element.setAttributeNode(attr3);

        root = new DOMAttrNodePointer(doc, doc.getDocumentElement());
        iterator = new DOMAttributeIterator(root, new QName("*"));
    }

    @Test
    public void testEmptyIterator() {
        DOMAttributeIterator emptyIterator = new DOMAttributeIterator(null, new QName("*"));
        assertNull(emptyIterator.getNodePointer());
    }

    @Test
    public void testIteratorNext() {
        Attr nextAttr = iterator.getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer();
        for (int i = 0; i < 20 && nextAttr != null; i++) {
            nextAttr = iterator.getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer().getNodePointer();
            iterator.setPosition(iterator.getPosition() + 1);
        }
        assertNull(nextAttr);
    }

    @Test
    public void testIteratorFirst() {
        DOMAttributePointer firstAttr = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals("xmlns:x", firstAttr.getNodeName().getLocalPart());
        assertEquals("http://www.example.com/x", firstAttr.getNodeName().getNamespaceURI());
        assertEquals("xmlns:x", firstAttr.getNodeName().getPrefix());
    }

    @Test
    public void testIteratorLast() {
        iterator.setPosition(iterator.getPosition() + 15);
        DOMAttributePointer lastAttr = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals("name2", lastAttr.getNodeName().getLocalPart());
        assertEquals(null, lastAttr.getNodeName().getNamespaceURI());
        assertEquals(null, lastAttr.getNodeName().getPrefix());
    }

    @Test
    public void testIteratorPrefixMatch() {
        iterator.setPosition(iterator.getPosition() + 1);
        DOMAttributePointer attr1 = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals("name1", attr1.getNodeName().getLocalPart());
        assertEquals(null, attr1.getNodeName().getNamespaceURI());
        assertEquals("", attr1.getNodeName().getPrefix());
    }

    @Test
    public void testIteratorNamespaceMatch() {
        iterator.setPosition(iterator.getPosition() + 1);
        DOMAttributePointer attr2 = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals("name1", attr2.getNodeName().getLocalPart());
        assertEquals("", attr2.getNodeName().getPrefix());
        assertEquals("", attr2.getNodeName().getNamespaceURI());
        iterator.setPosition(iterator.getPosition() + 1);
        DOMAttributePointer attr3 = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals("name2", attr3.getNodeName().getLocalPart());
        assertEquals("", attr3.getNodeName().getPrefix());
        assertEquals("http://www.example.com/x", attr3.getNodeName().getNamespaceURI());
    }

    @Test
    public void testIteratorLocalNameMatch() {
        iterator.setPosition(iterator.getPosition() + 1);
        DOMAttributePointer attr2 = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals("name1", attr2.getNodeName().getLocalPart());
        assertEquals(null, attr2.getNodeName().getPrefix());
        assertEquals(null, attr2.getNodeName().getNamespaceURI());
        iterator.setPosition(iterator.getPosition() + 1);
        DOMAttributePointer attr3 = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals("name2", attr3.getNodeName().getLocalPart());
        assertEquals("", attr3.getNodeName().getPrefix());
        assertEquals("", attr3.getNodeName().getNamespaceURI());
        iterator.setPosition(iterator.getPosition() + 1);
        DOMAttributePointer attr4 = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals("name1", attr4.getNodeName().getLocalPart());
        assertEquals(null, attr4.getNodeName().getPrefix());
        assertEquals(null, attr4.getNodeName().getNamespaceURI());
    }

    @Test
    public void testIteratorNonMatchingPrefix() {
        iterator.setPosition(iterator.getPosition() + 1);
        DOMAttributePointer attr2 = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals("name1", attr2.getNodeName().getLocalPart());
        assertEquals(null, attr2.getNodeName().getPrefix());
        assertEquals(null, attr2.getNodeName().getNamespaceURI());
        iterator.setPosition(iterator.getPosition() + 1);
        DOMAttributePointer attr3 = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals("name2", attr3.getNodeName().getLocalPart());
        assertEquals("", attr3.getNodeName().getPrefix());
        assertEquals("http://www.example.com/x", attr3.getNodeName().getNamespaceURI());
        iterator.setPosition(iterator.getPosition() + 1);
        DOMAttributePointer attr4 = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals("name1", attr4.getNodeName().getLocalPart());
        assertEquals("xmlns", attr4.getNodeName().getPrefix());
        assertEquals("http://www.w3.org/2000/xmlns/", attr4.getNodeName().getNamespaceURI());
        assertFalse(equalStrings(attr4.getNodeName().getPrefix(), "xmlns:x"));
    }

    @Test(expected = IllegalStateException.class)
    public void testIteratorNegativePosition() {
        iterator.setPosition(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIteratorBeyondLast() {
        iterator.setPosition(iterator.getPosition() + 4);
    }

    private Document createDocument() {
        Document doc = null;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return doc;
    }

    private Attr createAttribute(String prefix, String localName) {
        Attr attr = doc.createAttribute(localName);
        if (prefix != null) {
            attr.setPrefix(prefix);
        }
        return attr;
    }
}