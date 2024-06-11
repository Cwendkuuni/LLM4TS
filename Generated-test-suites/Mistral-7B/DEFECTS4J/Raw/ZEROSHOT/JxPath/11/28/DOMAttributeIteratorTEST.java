import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class DOMAttributeIteratorTest {

    @org.junit.Test
    public void testDOMAttributeIterator_EmptyNode_EmptyIterator() {
        NodePointer parent = new DOMPointer(null, 0, null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("", "*"));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_SingleAttribute_SingleNode() {
        String xml = "<root attr='value'/>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            document = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Node root = document.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(document, root), new QName("", "attr"));
        assertTrue(iterator.hasNext());
        Attr next = (Attr) iterator.next();
        assertEquals("value", next.getValue());
        assertEquals("attr", next.getNodeName());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_MultipleAttributes_MultipleNodes() {
        String xml = "<root xmlns:x='http://example.com/' attr1='value1' attr2='value2' attr3='value3' xmlns:y='http://example.org/'/>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            document = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Node root = document.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(document, root), new QName("", "attr1"));
        assertTrue(iterator.hasNext());
        Attr next = (Attr) iterator.next();
        assertEquals("value1", next.getValue());
        assertEquals("attr1", next.getNodeName());
        assertTrue(iterator.hasNext());
        next = (Attr) iterator.next();
        assertEquals("value2", next.getValue());
        assertEquals("attr2", next.getNodeName());
        assertTrue(iterator.hasNext());
        next = (Attr) iterator.next();
        assertEquals("value3", next.getValue());
        assertEquals("attr3", next.getNodeName());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_NamespacePrefix_SingleNode() {
        String xml = "<root xmlns:x='http://example.com/' attr:x='value'/>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            document = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Node root = document.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(document, root), new QName(null, "attr", "x"));
        assertTrue(iterator.hasNext());
        Attr next = (Attr) iterator.next();
        assertEquals("value", next.getValue());
        assertEquals("attr", next.getNodeName());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_NamespaceURI_SingleNode() {
        String xml = "<root xmlns='http://example.com/' attr='value'/>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            document = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Node root = document.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(document, root), new QName("", "attr"));
        assertTrue(iterator.hasNext());
        Attr next = (Attr) iterator.next();
        assertEquals("value", next.getValue());
        assertEquals("attr", next.getNodeName());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_NamespaceURI_MultipleNodes() {
        String xml = "<root xmlns='http://example.com/' attr1='value1' xmlns:x='http://example.org/' attr2='value2' attr3='value3'/>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            document = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Node root = document.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(document, root), new QName("", "attr1"));
        assertTrue(iterator.hasNext());
        Attr next = (Attr) iterator.next();
        assertEquals("value1", next.getValue());
        assertEquals("attr1", next.getNodeName());
        assertTrue(iterator.hasNext());
        next = (Attr) iterator.next();
        assertEquals("value2", next.getValue());
        assertEquals("attr2", next.getNodeName());
        assertTrue(iterator.hasNext());
        next = (Attr) iterator.next();
        assertEquals("value3", next.getValue());
        assertEquals("attr3", next.getNodeName());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_NamespacesPrefixAndURI_MultipleNodes() {
        String xml = "<root xmlns:x='http://example.com/' xmlns:y='http://example.org/' attr1:x='value1' attr2:y='value2' attr3='value3'/>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            document = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Node root = document.getDocumentElement();
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointer(document, root), new QName("x", "attr1"));
        assertTrue(iterator.hasNext());
        Attr next = (Attr) iterator.next();
        assertEquals("value1", next.getValue());
        assertEquals("attr1", next.getNodeName());
        assertTrue(iterator.hasNext());
        next = (Attr) iterator.next();
        assertEquals("value2", next.getValue());
        assertEquals("attr2", next.getNodeName());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_NullName_EmptyIterator() {
        NodePointer parent = new DOMPointer(null, 0, null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_NullName_NullParent() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, new QName("", ""));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_NullName_EmptyParent() {
        NodePointer parent = new DOMPointer(null, 0, null, null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }
}

class DOMPointer {
    // Implementation of DOMPointer for the test
}