import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

    @org.junit.Before
    public void setUp() {
        // Set up your test data here if needed
    }

    @org.junit.Test
    public void testDOMAttributeIteratorConstructor() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint("dummy"), new QName("*"));
        assertNotNull(iterator);
    }

    @org.junit.Test
    public void testDOMAttributeIteratorConstructorWithName() {
        Element element = new Element("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint("root"), new QName("test:attribute"));
        assertNotNull(iterator);
        assertEquals(element, iterator.getParent().getNode());
    }

    @org.junit.Test
    public void testGetNodePointer() {
        Element element = new Element("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint("root"), new QName("test:attribute"));
        Attr attribute = element.setAttributeNode(new Attr("test:attribute", "test"));
        iterator.next();
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals(attribute, attributePointer.getNode());
    }

    @org.junit.Test
    public void testGetPosition() {
        Element element = new Element("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint("root"), new QName("test:attribute"));
        iterator.next();
        iterator.next();
        int position = iterator.getPosition();
        assertEquals(2, position);
    }

    @org.junit.Test
    public void testSetPosition() {
        Element element = new Element("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint("root"), new QName("test:attribute"));
        iterator.next();
        iterator.next();
        boolean setPositionSuccess = iterator.setPosition(1);
        assertTrue(setPositionSuccess);
        int position = iterator.getPosition();
        assertEquals(1, position);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionOutOfBounds() {
        Element element = new Element("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint("root"), new QName("test:attribute"));
        iterator.next();
        iterator.next();
        iterator.setPosition(3);
    }

    @org.junit.Test
    public void testNext() {
        Element element = new Element("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint("root"), new QName("test:attribute"));
        Attr attribute1 = element.setAttributeNode(new Attr("test:attribute1", "test1"));
        Attr attribute2 = element.setAttributeNode(new Attr("test:attribute2", "test2"));
        iterator.next();
        DOMAttributePointer attributePointer1 = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals(attribute1, attributePointer1.getNode());
        iterator.next();
        DOMAttributePointer attributePointer2 = (DOMAttributePointer) iterator.getNodePointer();
        assertEquals(attribute2, attributePointer2.getNode());
    }

    @org.junit.Test
    public void testHasNext() {
        Element element = new Element("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint("root"), new QName("test:attribute"));
        Attr attribute1 = element.setAttributeNode(new Attr("test:attribute1", "test1"));
        Attr attribute2 = element.setAttributeNode(new Attr("test:attribute2", "test2"));
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test(expected = NoSuchElementException.class)
    public void testNoSuchElementException() {
        Element element = new Element("root");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint("root"), new QName("test:attribute"));
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @org.junit.Test
    public void testTestAttr() {
        String prefix = "xmlns";
        String localName = "xmlns";
        assertFalse(DOMAttributeIterator.testAttr(new Attr("xmlns:test", "test")));
        assertFalse(DOMAttributeIterator.testAttr(new Attr("xmlns", "test")));
        assertFalse(DOMAttributeIterator.testAttr(new Attr("test", "test")));
        Attr attr = new Attr("test:attribute", "test");
        String testPrefix = "test";
        String testLocalName = "attribute";
        assertTrue(DOMAttributeIterator.testAttr(attr));
        String testPrefixNS = "test";
        String testNS = "http://test.com";
        attr.setNamespaceURI(testNS);
        assertTrue(DOMAttributeIterator.testAttr(attr));
    }

    @org.junit.Test
    public void testGetAttribute() {
        Element element = new Element("root");
        Attr attribute = element.setAttributeNode(new Attr("test:attribute1", "test1"));
        QName name = new QName("test:attribute1");
        Attr attributeReturned = DOMAttributeIterator.getAttribute(element, name);
        assertEquals(attribute, attributeReturned);
    }

}