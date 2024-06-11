package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {
    private DOMAttributeIterator iterator;
    private NodePointer nodePointer;

    @Before
    public void setUp() {
        QName name = new QName("testPrefix", "testLocalName", "testNamespace");
        nodePointer = new DOMNodePointer();
        iterator = new DOMAttributeIterator(nodePointer, name);
    }

    @Test
    public void testConstructor() {
        // Test initialization of parent and name fields
        assertEquals(nodePointer, iterator.parent);
        QName expectedName = new QName("testPrefix", "testLocalName", "testNamespace");
        assertEquals(expectedName, iterator.name);
        // Test creation of empty attribute list
        assertEquals(new ArrayList<>(), iterator.attributes);
        // Test setting position to 0
        assertEquals(0, iterator.position);
    }

    @Test
    public void testTestAttr() {
        // Test when nodePrefix is "xmlns"
        Attr attr1 = createAttr("xmlns", "testLocalName", "testPrefix");
        assertFalse(iterator.testAttr(attr1));

        // Test when nodePrefix is null and nodeLocalName is "xmlns"
        Attr attr2 = createAttr(null, "xmlns", "testPrefix");
        assertFalse(iterator.testAttr(attr2));

        // Test when testLocalName is "*" and testPrefix equals nodePrefix
        QName name1 = new QName("testPrefix", "*", "testNamespace");
        Attr attr3 = createAttr("testPrefix", "testLocalName", "testNamespace");
        assertTrue(iterator.testAttr(attr3, name1));

        // Test when testLocalName is not "*" and testLocalName equals nodeLocalName
        QName name2 = new QName("testPrefix", "testLocalName", "testNamespace");
        Attr attr4 = createAttr("testPrefix", "testLocalName", "testNamespace");
        assertTrue(iterator.testAttr(attr4, name2));

        // Test when testLocalName is "testLocalName" and testPrefix is null
        QName name3 = new QName(null, "testLocalName", "testNamespace");
        Attr attr5 = createAttr("testPrefix", "testLocalName", "testNamespace");
        assertTrue(iterator.testAttr(attr5, name3));

        // Test when testLocalName is "*" and testPrefix is null
        QName name4 = new QName(null, "*", "testNamespace");
        Attr attr6 = createAttr(null, "testLocalName", "testNamespace");
        assertTrue(iterator.testAttr(attr6, name4));

        // Test when testLocalName is not "*" and testLocalName does not equal nodeLocalName
        QName name5 = new QName("testPrefix", "otherLocalName", "testNamespace");
        Attr attr7 = createAttr("testPrefix", "testLocalName", "testNamespace");
        assertFalse(iterator.testAttr(attr7, name5));

        // Test when testLocalName is "testLocalName" and testPrefix does not equal nodePrefix
        QName name6 = new QName("otherPrefix", "testLocalName", "testNamespace");
        Attr attr8 = createAttr("testPrefix", "testLocalName", "testNamespace");
        assertFalse(iterator.testAttr(attr8, name6));

        // Test when testLocalName is "*" and testNS does not equal nodeNS
        QName name7 = new QName("testPrefix", "*", "otherNamespace");
        Attr attr9 = createAttr("testPrefix", "testLocalName", "testNamespace");
        assertFalse(iterator.testAttr(attr9, name7));
    }

    @Test
    public void testEqualStrings() {
        assertTrue(iterator.equalStrings(null, null));
        assertFalse(iterator.equalStrings(null, "testString"));
        assertFalse(iterator.equalStrings("testString", null));
        assertTrue(iterator.equalStrings("testString", "testString"));
    }

    @Test
    public void testGetAttribute() {
        // Test when testPrefix is not null
        QName name1 = new QName("testPrefix", "testLocalName", "testNamespace");
        Element element1 = createTestElement("testPrefix:testLocalName=\"testValue\"");
        Attr attr1 = iterator.getAttribute(element1, name1);
        assertNotNull(attr1);
        assertEquals("testPrefix:testLocalName", attr1.getName());

        // Test when testNamespace is not null and element attribute is in a different namespace
        QName name2 = new QName("testPrefix", "testLocalName", "otherNamespace");
        Element element2 = createTestElement("testPrefix:testLocalName=\"testValue\"");
        Attr attr2 = iterator.getAttribute(element2, name2);
        assertNull(attr2);

        // Test when testPrefix and testNamespace are both null
        QName name3 = new QName(null, "testLocalName", null);
        Element element3 = createTestElement("testLocalName=\"testValue\"");
        Attr attr3 = iterator.getAttribute(element3, name3);
        assertNotNull(attr3);
        assertEquals("testLocalName", attr3.getName());

        // Test when testNamespace is null and element attribute is in a different namespace
        QName name4 = new QName(null, "testLocalName", "otherNamespace");
        Element element4 = createTestElement("testPrefix:testLocalName=\"testValue\"");
        Attr attr4 = iterator.getAttribute(element4, name4);
        assertNull(attr4);

        // Test when attribute with testNamespace and testLocalName does not exist and parser does not support NS
        QName name5 = new QName("testPrefix", "testLocalName", "testNamespace");
        Element element5 = createTestElement("otherPrefix:otherLocalName=\"testValue\"");
        Attr attr5 = iterator.getAttribute(element5, name5);
        assertNull(attr5);

        // Test when attribute with testLocalName exists and parser does not support NS
        QName name6 = new QName(null, "testLocalName", "testNamespace");
        Element element6 = createTestElement("testLocalName=\"testValue\"");
        Attr attr6 = iterator.getAttribute(element6, name6);
        assertNotNull(attr6);
        assertEquals("testLocalName", attr6.getName());
    }

    @Test
    public void testGetNodePointer() {
        // Test when position is 0
        assertNull(iterator.getNodePointer());

        // Test when position is 1 and there is one attribute in the list
        QName name1 = new QName("testPrefix", "testLocalName", "testNamespace");
        Element element1 = createTestElement("testPrefix:testLocalName=\"testValue\"");
        Attr attr1 = createAttr("testPrefix", "testLocalName", "testNamespace");
        iterator.attributes.add(attr1);
        iterator.position = 1;
        NodePointer nodePointer1 = iterator.getNodePointer();
        assertNotNull(nodePointer1);
        assertTrue(nodePointer1 instanceof DOMAttributePointer);
        DOMAttributePointer attributePointer1 = (DOMAttributePointer) nodePointer1;
        assertEquals(nodePointer, attributePointer1.parent);
        assertEquals(attr1, attributePointer1.attribute);

        // Test when position is greater than 1 and there is one attribute in the list
        iterator.position = 2;
        assertNull(iterator.getNodePointer());

        // Test when position is 1 and there are multiple attributes in the list
        QName name2 = new QName("testPrefix", "*", "testNamespace");
        Element element2 = createTestElement("testPrefix:testLocalName1=\"testValue1\" testPrefix:testLocalName2=\"testValue2\"");
        Attr attr2 = createAttr("testPrefix", "testLocalName1", "testNamespace");
        Attr attr3 = createAttr("testPrefix", "testLocalName2", "testNamespace");
        iterator.attributes.add(attr2);
        iterator.attributes.add(attr3);
        iterator.position = 1;
        NodePointer nodePointer2 = iterator.getNodePointer();
        assertNotNull(nodePointer2);
        assertTrue(nodePointer2 instanceof DOMAttributePointer);
        DOMAttributePointer attributePointer2 = (DOMAttributePointer) nodePointer2;
        assertEquals(nodePointer, attributePointer2.parent);
        assertEquals(attr2, attributePointer2.attribute);

        // Test when position is greater than 1 and there are multiple attributes in the list
        iterator.position = 2;
        NodePointer nodePointer3 = iterator.getNodePointer();
        assertNotNull(nodePointer3);
        assertTrue(nodePointer3 instanceof DOMAttributePointer);
        DOMAttributePointer attributePointer3 = (DOMAttributePointer) nodePointer3;
        assertEquals(nodePointer, attributePointer3.parent);
        assertEquals(attr3, attributePointer3.attribute);

        // Test when index is negative
        iterator.position = -1;
        NodePointer nodePointer4 = iterator.getNodePointer();
        assertNotNull(nodePointer4);
        assertTrue(nodePointer4 instanceof DOMAttributePointer);
        DOMAttributePointer attributePointer4 = (DOMAttributePointer) nodePointer4;
        assertEquals(nodePointer, attributePointer4.parent);
        assertEquals(attr2, attributePointer4.attribute);
    }

    @Test
    public void testGetPosition() {
        // Test when position is initially 0
        assertEquals(0, iterator.getPosition());
        iterator.position = 2;
        assertEquals(2, iterator.getPosition());

        // Test when position is initially -1
        iterator.position = -1;
        assertEquals(-1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        // Test when position is within valid range
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());

        // Test when position is 0
        assertFalse(iterator.setPosition(0));
        assertEquals(1, iterator.getPosition());

        // Test when position is greater than attributes list size
        assertFalse(iterator.setPosition(2));
        assertEquals(1, iterator.getPosition());
    }

    private Attr createAttr(String prefix, String localName, String namespaceURI) {
        Attr attr = mock(Attr.class);
        when(attr.getPrefix()).thenReturn(prefix);
        when(attr.getLocalName()).thenReturn(localName);
        when(attr.getNamespaceURI()).thenReturn(namespaceURI);
        return attr;
    }

    private Element createTestElement(String attributeString) {
        Element element = mock(Element.class);
        NamedNodeMap attributes = mock(NamedNodeMap.class);
        when(element.getAttributes()).thenReturn(attributes);

        String[] attributeParts = attributeString.split("=");
        String attributePrefix = attributeParts[0].split(":")[0];
        String attributeLocalName = attributeParts[0].split(":")[1].replaceAll("\"", "");
        String attributeNamespaceURI = "testNamespace";
        Attr attribute = createAttr(attributePrefix, attributeLocalName, attributeNamespaceURI);
        when(attributes.getLength()).thenReturn(1);
        when(attributes.item(anyInt())).thenReturn(attribute);

        return element;
    }
}