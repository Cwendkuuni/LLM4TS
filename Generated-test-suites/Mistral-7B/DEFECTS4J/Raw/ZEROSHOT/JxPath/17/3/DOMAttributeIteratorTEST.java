import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

    @org.junit.Test
    public void testEmptyIterator() {
        NodePointer parentPointer = new DOMPoint(null, 0, Node.ELEMENT_NODE);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testSingleAttributeIterator() {
        String xml = "<root xmlns:x='http://test.com/x' myattr='value'/>";
        Document document = JAXPHelper.newDocument(xml);
        NodePointer rootPointer = new DOMPoint(document.getDocumentElement(), 0, Node.ELEMENT_NODE);
        DOMAttributeIterator iterator = new DOMAttributeIterator(rootPointer, new QName("*:myattr"));
        assertTrue(iterator.hasNext());
        DOMAttributePointer next = (DOMAttributePointer) iterator.next();
        assertEquals("myattr", next.getName().getName());
        assertEquals("value", next.getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testMultipleAttributesIterator() {
        String xml = "<root xmlns:x='http://test.com/x' myattr1='value1' myattr2='value2'/>";
        Document document = JAXPHelper.newDocument(xml);
        NodePointer rootPointer = new DOMPoint(document.getDocumentElement(), 0, Node.ELEMENT_NODE);
        DOMAttributeIterator iterator = new DOMAttributeIterator(rootPointer, new QName("*:myattr"));
        assertTrue(iterator.hasNext());
        DOMAttributePointer next1 = (DOMAttributePointer) iterator.next();
        assertEquals("myattr1", next1.getName().getName());
        assertEquals("value1", next1.getNodeValue());
        assertTrue(iterator.hasNext());
        DOMAttributePointer next2 = (DOMAttributePointer) iterator.next();
        assertEquals("myattr2", next2.getName().getName());
        assertEquals("value2", next2.getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testNamespaceAttributeIterator() {
        String xml = "<root xmlns:x='http://test.com/x' xmlns:y='http://test.com/y' xmlns='http://test.com' myattr1='value1' myattr2='value2' x:myattr3='value3'/>";
        Document document = JAXPHelper.newDocument(xml);
        NodePointer rootPointer = new DOMPoint(document.getDocumentElement(), 0, Node.ELEMENT_NODE);
        DOMAttributeIterator iterator = new DOMAttributeIterator(rootPointer, new QName("*:myattr", "http://test.com/x"));
        assertTrue(iterator.hasNext());
        DOMAttributePointer next1 = (DOMAttributePointer) iterator.next();
        assertEquals("myattr1", next1.getName().getName());
        assertFalse(next1.getName().getNamespaceURI().equals("http://test.com/x"));
        assertTrue(iterator.hasNext());
        DOMAttributePointer next2 = (DOMAttributePointer) iterator.next();
        assertEquals("myattr3", next2.getName().getName());
        assertTrue(next2.getName().getNamespaceURI().equals("http://test.com/x"));
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testNamespaceNoPrefixIterator() {
        String xml = "<root xmlns:x='http://test.com/x' xmlns='http://test.com' myattr1='value1' myattr2='value2' x:myattr3='value3'/>";
        Document document = JAXPHelper.newDocument(xml);
        NodePointer rootPointer = new DOMPoint(document.getDocumentElement(), 0, Node.ELEMENT_NODE);
        DOMAttributeIterator iterator = new DOMAttributeIterator(rootPointer, new QName("*:myattr"));
        assertTrue(iterator.hasNext());
        DOMAttributePointer next1 = (DOMAttributePointer) iterator.next();
        assertEquals("myattr1", next1.getName().getName());
        assertNull(next1.getName().getNamespaceURI());
        assertTrue(iterator.hasNext());
        DOMAttributePointer next2 = (DOMAttributePointer) iterator.next();
        assertEquals("myattr2", next2.getName().getName());
        assertNull(next2.getName().getNamespaceURI());
        assertTrue(iterator.hasNext());
        DOMAttributePointer next3 = (DOMAttributePointer) iterator.next();
        assertEquals("myattr3", next3.getName().getName());
        assertEquals("http://test.com/x", next3.getName().getNamespaceURI());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testIteratorWithNoMatchingAttributes() {
        String xml = "<root xmlns:x='http://test.com/x' myattr1='value1' myattr2='value2'/>";
        Document document = JAXPHelper.newDocument(xml);
        NodePointer rootPointer = new DOMPoint(document.getDocumentElement(), 0, Node.ELEMENT_NODE);
        DOMAttributeIterator iterator = new DOMAttributeIterator(rootPointer, new QName("*:myattr4"));
        assertFalse(iterator.hasNext());
    }
}