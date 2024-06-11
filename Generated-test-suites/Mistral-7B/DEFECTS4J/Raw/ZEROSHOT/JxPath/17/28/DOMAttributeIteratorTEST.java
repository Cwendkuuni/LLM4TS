import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

    @org.junit.Test
    public void testDOMAttributeIterator_Empty() {
        NodePointer parent = new DOMPointer("", null, null, null, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(""));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_SingleAttribute() {
        String xml = "<root xmlns:x='http://example.com' xmlns:y='http://example.org'><a x:a='123' /></root>";
        NodePointer parent = new DOMPointer(xml, "root", "http://example.com", null, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("x:a"));
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertNotNull(next);
        assertEquals("123", next.getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_SingleAttributeWithNS() {
        String xml = "<root xmlns:x='http://example.com' xmlns:y='http://example.org'><a x:a='123' /></root>";
        NodePointer parent = new DOMPointer(xml, "root", "http://example.com", null, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("x:a", "http://example.com"));
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertNotNull(next);
        assertEquals("123", next.getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_MultipleAttributes() {
        String xml = "<root xmlns:x='http://example.com' xmlns:y='http://example.org'><a x:a='123' x:b='456' /></root>";
        NodePointer parent = new DOMPointer(xml, "root", "http://example.com", null, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("x:*"));
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertNotNull(next);
        assertEquals("123", next.getNodeValue());
        assertTrue(iterator.hasNext());
        next = iterator.next();
        assertNotNull(next);
        assertEquals("456", next.getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_MultipleAttributesWithNS() {
        String xml = "<root xmlns:x='http://example.com' xmlns:y='http://example.org'><a x:a='123' y:b='456' /></root>";
        NodePointer parent = new DOMPointer(xml, "root", "http://example.com", null, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("*", "http://example.org"));
        assertTrue(iterator.hasNext());
        NodePointer next = iterator.next();
        assertNotNull(next);
        assertEquals("456", next.getNodeValue());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_ElementWithoutAttributes() {
        String xml = "<root></root>";
        NodePointer parent = new DOMPointer(xml, "root", null, null, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(""));
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_ElementWithEmptyAttribute() {
        String xml = "<root xmlns:x='http://example.com' xmlns:y='http://example.org'><a x:a='' /></root>";
        NodePointer parent = new DOMPointer(xml, "root", "http://example.com", null, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("x:a"));
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testDOMAttributeIterator_ElementWithNullAttribute() {
        String xml = "<root xmlns:x='http://example.com' xmlns:y='http://example.org'><a x:a='null' /></root>";
        NodePointer parent = new DOMPointer(xml, "root", "http://example.com", null, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName("x:a"));
        assertFalse(iterator.hasNext());
    }
}