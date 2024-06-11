
package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    
    @Test
    public void testConstructor() {
        // Test with non-element NodePointer
        NodePointer parent = new DOMNodePointer(null, null);
        QName name = new QName("", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
        
        // Test with element NodePointer
        parent = new DOMNodePointer(null, null);
        name = new QName("", "*");
        iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
    }
    
    @Test
    public void testTestAttr() {
        // Test with matching attributes
        Attr attr1 = null;
        Attr attr2 = null;
        String testPrefix = "";
        String testLocalName = "";
        QName testName = new QName(testPrefix, testLocalName);
        DOMNodePointer parent = null;
        boolean result = false;
        
        // Test with matching local name and no prefix
        attr1 = createAttr("", "attribute1", "value1");
        attr2 = createAttr("", "attribute2", "value2");
        testName = new QName("", "attribute1");
        parent = new DOMNodePointer(null, null);
        result = new DOMAttributeIterator(parent, testName).testAttr(attr1, testName);
        assertTrue(result);
        result = new DOMAttributeIterator(parent, testName).testAttr(attr2, testName);
        assertFalse(result);
        
        // Test with matching local name and matching prefix
        attr1 = createAttr("", "attribute1", "value1");
        attr2 = createAttr("", "attribute2", "value2");
        testName = new QName("", "attribute1");
        parent = new DOMNodePointer(null, null);
        result = new DOMAttributeIterator(parent, testName).testAttr(attr1, testName);
        assertTrue(result);
        result = new DOMAttributeIterator(parent, testName).testAttr(attr2, testName);
        assertFalse(result);
        
        // Test with matching local name and non-matching prefix
        attr1 = createAttr("prefix1", "attribute1", "value1");
        attr2 = createAttr("prefix2", "attribute2", "value2");
        testName = new QName("", "attribute1");
        parent = new DOMNodePointer(null, null);
        result = new DOMAttributeIterator(parent, testName).testAttr(attr1, testName);
        assertTrue(result);
        result = new DOMAttributeIterator(parent, testName).testAttr(attr2, testName);
        assertFalse(result);
        
        // Test with matching local name, matching prefix and matching namespace
        attr1 = createAttr("prefix1", "attribute1", "value1");
        attr2 = createAttr("prefix2", "attribute2", "value2");
        testName = new QName("prefix1", "attribute1");
        parent = new DOMNodePointer(null, null);
        result = new DOMAttributeIterator(parent, testName).testAttr(attr1, testName);
        assertTrue(result);
        result = new DOMAttributeIterator(parent, testName).testAttr(attr2, testName);
        assertFalse(result);
        
        // Test with matching local name, matching prefix and non-matching namespace
        attr1 = createAttr("prefix1", "attribute1", "value1");
        attr2 = createAttr("prefix2", "attribute2", "value2");
        testName = new QName("prefix1", "attribute1");
        parent = new DOMNodePointer(null, "namespace1");
        result = new DOMAttributeIterator(parent, testName).testAttr(attr1, testName);
        assertTrue(result);
        result = new DOMAttributeIterator(parent, testName).testAttr(attr2, testName);
        assertFalse(result);
        
        // Test with matching local name, non-matching prefix and matching namespace
        attr1 = createAttr("prefix1", "attribute1", "value1");
        attr2 = createAttr("prefix2", "attribute2", "value2");
        testName = new QName("prefix1", "attribute1");
        parent = new DOMNodePointer(null, null);
        result = new DOMAttributeIterator(parent, testName).testAttr(attr1, testName);
        assertTrue(result);
        result = new DOMAttributeIterator(parent, testName).testAttr(attr2, testName);
        assertFalse(result);
        
        // Test with matching local name, non-matching prefix and non-matching namespace
        attr1 = createAttr("prefix1", "attribute1", "value1");
        attr2 = createAttr("prefix2", "attribute2", "value2");
        testName = new QName("prefix1", "attribute1");
        parent = new DOMNodePointer(null, "namespace1");
        result = new DOMAttributeIterator(parent, testName).testAttr(attr1, testName);
        assertTrue(result);
        result = new DOMAttributeIterator(parent, testName).testAttr(attr2, testName);
        assertFalse(result);
        
        // Test with non-matching local name
        attr1 = createAttr("", "attribute1", "value1");
        attr2 = createAttr("", "attribute2", "value2");
        testName = new QName("", "attribute3");
        parent = new DOMNodePointer(null, null);
        result = new DOMAttributeIterator(parent, testName).testAttr(attr1, testName);
        assertFalse(result);
        result = new DOMAttributeIterator(parent, testName).testAttr(attr2, testName);
        assertFalse(result);
        
        // Test with wildcard local name
        attr1 = createAttr("", "attribute1", "value1");
        attr2 = createAttr("", "attribute2", "value2");
        testName = new QName("", "*");
        parent = new DOMNodePointer(null, null);
        result = new DOMAttributeIterator(parent, testName).testAttr(attr1, testName);
        assertTrue(result);
        result = new DOMAttributeIterator(parent, testName).testAttr(attr2, testName);
        assertTrue(result);
    }
    
    @Test
    public void testEqualStrings() {
        // Test with equal strings
        String s1 = "string1";
        String s2 = "string1";
        boolean result = DOMAttributeIterator.equalStrings(s1, s2);
        assertTrue(result);
        
        // Test with not equal strings
        s1 = "string1";
        s2 = "string2";
        result = DOMAttributeIterator.equalStrings(s1, s2);
        assertFalse(result);
        
        // Test with equal null strings
        s1 = null;
        s2 = null;
        result = DOMAttributeIterator.equalStrings(s1, s2);
        assertTrue(result);
        
        // Test with equal string and null
        s1 = "string1";
        s2 = null;
        result = DOMAttributeIterator.equalStrings(s1, s2);
        assertFalse(result);
        
        // Test with null and equal string
        s1 = null;
        s2 = "string1";
        result = DOMAttributeIterator.equalStrings(s1, s2);
        assertFalse(result);
    }
    
    @Test
    public void testGetAttribute() {
        // Test with non-namespace attribute
        Element element = createElement("element1");
        QName name = new QName("", "attribute1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, null);
        Attr resultAttr = iterator.getAttribute(element, name);
        assertNotNull(resultAttr);
        assertEquals("attribute1", resultAttr.getName());
        
        // Test with namespace attribute
        element = createElementNS("namespace1", "element1");
        name = new QName("prefix1", "attribute1");
        iterator = new DOMAttributeIterator(null, null);
        resultAttr = iterator.getAttribute(element, name);
        assertNotNull(resultAttr);
        assertEquals("attribute1", resultAttr.getName());
    }
    
    @Test
    public void testGetNodePointer() {
        // Test with position less than 1
        NodePointer parent = new DOMNodePointer(null, null);
        QName name = new QName("", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        assertNull(iterator.getNodePointer());
        
        // Test with position at 1
        parent = new DOMNodePointer(null, null);
        name = new QName("", "attribute1");
        iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        iterator.setPosition(1);
        assertNotNull(iterator.getNodePointer());
        
        // Test with position greater than 1
        parent = new DOMNodePointer(null, null);
        name = new QName("", "attribute1");
        iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        iterator.setPosition(2);
        assertNotNull(iterator.getNodePointer());
    }
    
    @Test
    public void testGetPosition() {
        NodePointer parent = new DOMNodePointer(null, null);
        QName name = new QName("", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        assertEquals(0, iterator.getPosition());
        
        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        NodePointer parent = new DOMNodePointer(null, null);
        QName name = new QName("", "");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        
        // Test with valid position
        boolean result = iterator.setPosition(1);
        assertTrue(result);
        assertEquals(1, iterator.getPosition());
        
        // Test with position less than 1
        result = iterator.setPosition(0);
        assertFalse(result);
        assertEquals(0, iterator.getPosition());
        
        // Test with position greater than size
        result = iterator.setPosition(2);
        assertFalse(result);
        assertEquals(2, iterator.getPosition());
    }
    
    private Attr createAttr(String prefix, String localName, String value) {
        DOMNodePointer parent = new DOMNodePointer(null, null);
        return new DOMAttributeNode(null, prefix, localName, value, parent);
    }
    
    private Element createElement(String name) {
        return new MockElement(name);
    }
    
    private Element createElementNS(String namespaceURI, String name) {
        return new MockElementNS(namespaceURI, name);
    }
}
