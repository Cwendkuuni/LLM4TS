package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistrar;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.w3c.dom.ls.LSSerializer;

public class DOMAttributeIteratorTest {

    private static final String XML_NS = "http://www.w3.org/2000/xmlns/";
    private static final String XPATH_NS = "http://www.w3.org/2005/xpath";

    private NodeIterator iterator;
    private Document doc;

    @Before
    public void setUp() {
        DOMImplementationRegistrar.registerDocumentBuilder();
        doc = (Document) new org.apache.xerces.parsers.DOMParser().parseFromString("<root xmlns:x=\"foo:bar\"><el attr1=\"val1\" attr2=\"val2\"/><el attr3=\"val3\"/></root>", null);
        iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName("*"));
    }

    @Test
    public void testNext() {
        assertSame(iterator.getNodePointer().getNode(), iterator.getNodePointer().getParent().getNode().getAttributeNode("xmlns:x"));
        iterator.next();
        assertSame(iterator.getNodePointer().getNode(), iterator.getNodePointer().getParent().getNode().getAttributeNode("attr1"));
        iterator.next();
        assertSame(iterator.getNodePointer().getNode(), iterator.getNodePointer().getParent().getNode().getAttributeNode("attr2"));
        iterator.next();
        assertSame(iterator.getNodePointer().getNode(), iterator.getNodePointer().getParent().getNode().getAttributeNode("attr3"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNext() {
        assertFalse(iterator.hasNext());
        iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName("attr1"));
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName("*"));
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());

        iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName("foo:bar"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSetPosition() {
        iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName("*"));
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.setPosition(1);
        assertSame(iterator.getNodePointer().getNode(), iterator.getNodePointer().getParent().getNode().getAttributeNode("attr1"));
    }

    @Test
    public void testGetNodePointer() {
        iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName("*"));
        Attr attr = iterator.getNodePointer().getNode();
        iterator.next();
        attr = iterator.getNodePointer().getNode();
        iterator.next();
        attr = iterator.getNodePointer().getNode();
        assertNull(iterator.getNodePointer().getNode());
    }

    @Test
    public void testGetPosition() {
        iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName("*"));
        iterator.next();
        iterator.next();
        iterator.next();
        assertEquals(3, iterator.getPosition());
    }

    @Test
    public void testIteratorWithEmptyElement() {
        Node emptyElement = doc.createElement("empty");
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(emptyElement), new QName("*"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithEmptyDocument() {
        Document emptyDoc = (Document) new org.apache.xerces.parsers.DOMParser().parseFromString("", null);
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(emptyDoc.getDocumentElement()), new QName("*"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithNullDocument() {
        NodeIterator iterator = new DOMAttributeIterator(null, new QName("*"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithNullParent() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(null), new QName("*"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithNullElement() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithNullQName() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), null);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithInvalidPrefix() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName("z:foo"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithInvalidLocalName() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName("foo:z"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithNamespaceUriMismatch() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName("x:foo", "http://example.com/test"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithXmlnsAttribute() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName("xmlns", XML_NS));
        assertSame(iterator.getNodePointer().getNode(), doc.getDocumentElement().getAttributeNode("xmlns:x"));
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithXPathNamespace() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName("xmlns", XPATH_NS));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithEmptyNamespacePrefix() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName(""));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithEmptyLocalName() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName(""));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithEmptyQName() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithSamePrefixAndLocalName() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName("x", "foo:foo"));
        assertSame(iterator.getNodePointer().getNode(), iterator.getNodePointer().getParent().getNode().getAttributeNode("attr1"));
        iterator.next();
        assertSame(iterator.getNodePointer().getNode(), iterator.getNodePointer().getParent().getNode().getAttributeNode("attr2"));
        iterator.next();
        assertSame(iterator.getNodePointer().getNode(), iterator.getNodePointer().getParent().getNode().getAttributeNode("attr3"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithDifferentPrefixAndLocalName() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName("y", "foo:foo"));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithDifferentPrefixAndEmptyLocalName() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName("y", ""));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithDifferentPrefixAndEmptyQName() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName("y", ""));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithSamePrefixAndEmptyLocalName() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName("x", ""));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithSamePrefixAndNullLocalName() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName("x", null));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithSamePrefixAndNullQName() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName(null, "x:foo"));
        assertSame(iterator.getNodePointer().getNode(), doc.getDocumentElement().getAttributeNode("xmlns:x"));
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithSamePrefixAndNullQName2() {
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(doc.getDocumentElement()), new QName(null, null));
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSerializer() {
        LSResourceResolver resolver = new LSResourceResolver() {
            @Override
            public LSInput createInput(String systemId, String baseURI, String encoding) throws Exception {
                return new LSInput() {
                    @Override
                    public Node getNode() {
                        return doc;
                    }

                    @Override
                    public String getStringData() throws Exception {
                        return "";
                    }
                };
            }
        };
        LSSerializer serializer = new org.apache.xerces.impl.xs.XMLSchemaFactory().createExternalXMLSerializer();
        serializer.setOutputProperty("omit-xml-declaration", "yes");
        serializer.setOutputProperty("indent-number", "2");
        StringWriter writer = new StringWriter();
        serializer.serialize(doc, writer, resolver);
        String xmlString = writer.toString();
        Document parsed = new org.apache.xerces.parsers.DOMParser().parseFromString(xmlString);
        NodeIterator iterator = new DOMAttributeIterator(new DOMPointer(parsed.getDocumentElement()), new QName("*"));
        assertEquals(3, iterator.getPosition());
    }
}