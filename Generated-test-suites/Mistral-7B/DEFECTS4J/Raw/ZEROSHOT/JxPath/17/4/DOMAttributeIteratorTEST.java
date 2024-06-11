package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.test.JxPathTest;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Test class for {@link DOMAttributeIterator}
 */
public class DOMAttributeIteratorTest extends JxPathTest {

    @SuppressWarnings("unchecked")
    private DOMAttributeIterator createAttributeIterator(Element element) {
        return new DOMAttributeIterator(new DOMPoint(element), new QName("test:attr"));
    }

    /**
     * Test constructor with empty list
     */
    @org.junit.Test
    public void testEmptyConstructor() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, null);
        assertFalse(iterator.hasNext());
    }

    /**
     * Test constructor with single attribute
     */
    @org.junit.Test
    public void testSingleAttributeConstructor() {
        Element element = createTestElement("root", "xmlns:test=\"http://test.com\"", "t=\"value\"");
        DOMAttributeIterator iterator = createAttributeIterator(element);
        assertNotNull(iterator.next());
        assertEquals(1, iterator.getPosition());
        iterator.close();
    }

    /**
     * Test constructor with multiple attributes
     */
    @org.junit.Test
    public void testMultipleAttributesConstructor() {
        Element element = createTestElement("root", "xmlns:test=\"http://test.com\"", "t1=\"value1\"", "t2=\"value2\"");
        DOMAttributeIterator iterator = createAttributeIterator(element);
        assertNotNull(iterator.next());
        assertNotNull(iterator.next());
        assertEquals(2, iterator.getPosition());
        iterator.close();
    }

    /**
     * Test next method with empty list
     */
    @org.junit.Test
    public void testNextEmptyList() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, null);
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    /**
     * Test next method with single attribute
     */
    @org.junit.Test
    public void testNextSingleAttribute() {
        Element element = createTestElement("root", "xmlns:test=\"http://test.com\"", "t=\"value\"");
        DOMAttributeIterator iterator = createAttributeIterator(element);
        assertTrue(iterator.hasNext());
        Attr next = iterator.next();
        assertEquals("t", next.getName().getName());
        assertEquals("value", next.getValue());
        iterator.close();
    }

    /**
     * Test next method with multiple attributes
     */
    @org.junit.Test
    public void testNextMultipleAttributes() {
        Element element = createTestElement("root", "xmlns:test=\"http://test.com\"", "t1=\"value1\"", "t2=\"value2\"");
        DOMAttributeIterator iterator = createAttributeIterator(element);
        assertTrue(iterator.hasNext());
        Attr first = iterator.next();
        assertEquals("t1", first.getName().getName());
        assertEquals("value1", first.getValue());
        assertTrue(iterator.hasNext());
        Attr second = iterator.next();
        assertEquals("t2", second.getName().getName());
        assertEquals("value2", second.getValue());
        iterator.close();
    }

    /**
     * Test hasNext method with empty list
     */
    @org.junit.Test
    public void testHasNextEmptyList() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, null);
        assertFalse(iterator.hasNext());
    }

    /**
     * Test hasNext method with single attribute
     */
    @org.junit.Test
    public void testHasNextSingleAttribute() {
        Element element = createTestElement("root", "xmlns:test=\"http://test.com\"", "t=\"value\"");
        DOMAttributeIterator iterator = createAttributeIterator(element);
        assertTrue(iterator.hasNext());
    }

    /**
     * Test hasNext method with multiple attributes
     */
    @org.junit.Test
    public void testHasNextMultipleAttributes() {
        Element element = createTestElement("root", "xmlns:test=\"http://test.com\"", "t1=\"value1\"", "t2=\"value2\"");
        DOMAttributeIterator iterator = createAttributeIterator(element);
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
    }

    /**
     * Test getPosition method with empty list
     */
    @org.junit.Test
    public void testGetPositionEmptyList() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, null);
        assertEquals(0, iterator.getPosition());
    }

    /**
     * Test getPosition method with single attribute
     */
    @org.junit.Test
    public void testGetPositionSingleAttribute() {
        Element element = createTestElement("root", "xmlns:test=\"http://test.com\"", "t=\"value\"");
        DOMAttributeIterator iterator = createAttributeIterator(element);
        assertEquals(1, iterator.getPosition());
        iterator.next();
        assertEquals(1, iterator.getPosition());
        iterator.close();
    }

    /**
     * Test getPosition method with multiple attributes
     */
    @org.junit.Test
    public void testGetPositionMultipleAttributes() {
        Element element = createTestElement("root", "xmlns:test=\"http://test.com\"", "t1=\"value1\"", "t2=\"value2\"");
        DOMAttributeIterator iterator = createAttributeIterator(element);
        assertEquals(1, iterator.getPosition());
        iterator.next();
        iterator.next();
        assertEquals(3, iterator.getPosition());
        iterator.close();
    }

    /**
     * Test setPosition method with empty list
     */
    @org.junit.Test
    public void testSetPositionEmptyList() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, null);
        assertFalse(iterator.setPosition(2));
        assertFalse(iterator.hasNext());
    }

    /**
     * Test setPosition method with single attribute
     */
    @org.junit.Test
    public void testSetPositionSingleAttribute() {
        Element element = createTestElement("root", "xmlns:test=\"http://test.com\"", "t=\"value\"");
        DOMAttributeIterator iterator = createAttributeIterator(element);
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    /**
     * Test setPosition method with multiple attributes
     */
    @org.junit.Test
    public void testSetPositionMultipleAttributes() {
        Element element = createTestElement("root", "xmlns:test=\"http://test.com\"", "t1=\"value1\"", "t2=\"value2\"");
        DOMAttributeIterator iterator = createAttributeIterator(element);
        assertTrue(iterator.setPosition(3));
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    /**
     * Test setPosition method with invalid position
     */
    @org.junit.Test
    public void testSetPositionInvalidPosition() {
        Element element = createTestElement("root", "xmlns:test=\"http://test.com\"", "t1=\"value1\"", "t2=\"value2\"");
        DOMAttributeIterator iterator = createAttributeIterator(element);
        assertFalse(iterator.setPosition(4));
        assertFalse(iterator.hasNext());
        iterator.close();
    }

    /**
     * Test close method
     */
    @org.junit.Test
    public void testClose() {
        Element element = createTestElement("root", "xmlns:test=\"http://test.com\"", "t1=\"value1\"", "t2=\"value2\"");
        DOMAttributeIterator iterator = createAttributeIterator(element);
        iterator.next();
        iterator.next();
        iterator.close();
        assertFalse(iterator.hasNext());
    }

    /**
     * Test equals method
     */
    @org.junit.Test
    public void testEquals() {
        Element element = createTestElement("root", "xmlns:test=\"http://test.com\"", "t1=\"value1\"", "t2=\"value2\"");
        DOMAttributeIterator iterator1 = createAttributeIterator(element);
        DOMAttributeIterator iterator2 = createAttributeIterator(element);
        DOMAttributeIterator iterator3 = new DOMAttributeIterator(null, null);
        DOMAttributeIterator iterator4 = createAttributeIterator(createElement("root"));

        assertTrue(iterator1.equals(iterator1));
        assertTrue(iterator1.equals(iterator2));
        assertFalse(iterator1.equals(iterator3));
        assertFalse(iterator1.equals(iterator4));
    }

    /**
     * Test hashCode method
     */
    @org.junit.Test
    public void testHashCode() {
        Element element = createTestElement("root", "xmlns:test=\"http://test.com\"", "t1=\"value1\"", "t2=\"value2\"");
        DOMAttributeIterator iterator1 = createAttributeIterator(element);
        DOMAttributeIterator iterator2 = createAttributeIterator(element);
        DOMAttributeIterator iterator3 = new DOMAttributeIterator(null, null);
        DOMAttributeIterator iterator4 = createAttributeIterator(createElement("root"));

        assertEquals(iterator1.hashCode(), iterator1.hashCode());
        assertEquals(iterator1.hashCode(), iterator2.hashCode());
        assertNotSame(iterator1.hashCode(), iterator3.hashCode());
        assertNotSame(iterator1.hashCode(), iterator4.hashCode());
    }

    /**
     * Test toString method
     */
    @org.junit.Test
    public void testToString() {
        Element element = createTestElement("root", "xmlns:test=\"http://test.com\"", "t1=\"value1\"", "t2=\"value2\"");
        DOMAttributeIterator iterator = createAttributeIterator(element);

        String string = iterator.toString();
        assertTrue(string.contains("[DOMAttributeIterator]"));
        assertTrue(string.contains("position=" + iterator.getPosition()));
        assertTrue(string.contains("next=" + iterator.hasNext()));
    }

    private Element createTestElement(String tagName, String namespace, String... attributeStrings) {
        Element element = document.createElement(tagName);
        element.setNamespaceURI(namespace);
        for (String attributeString : attributeStrings) {
            String[] parts = attributeString.split("=");
            String prefix = parts[0];
            String localName = parts[1].substring(1, parts[1].length() - 1);
            String value = parts[2];
            Attr attr = document.createAttribute(prefix + ":" + localName);
            attr.setValue(value);
            element.setAttributeNode(attr);
        }
        return element;
    }

    private Node createTestDocument() {
        Document doc = document();
        doc.setXmlVersion("1.0");
        doc.setDocumentType(documentType("root", "html", null));
        Element root = doc.createElement("root");
        doc.appendChild(root);
        return doc;
    }

}