package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMAttributeIteratorTest {
    
    private DOMAttributeIterator attributeIterator;
    private NodePointer parent;
    private QName name;
    
    @Before
    public void setUp() {
        parent = new DOMNodePointer(element);
        name = new QName("attributeName");
        attributeIterator = new DOMAttributeIterator(parent, name);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(parent, attributeIterator.parent);
        assertEquals(name, attributeIterator.name);
        assertNotNull(attributeIterator.attributes);
        assertEquals(0, attributeIterator.attributes.size());
        assertEquals(0, attributeIterator.position);
    }
    
    @Test
    public void testGetAttribute() {
        Element element = createElement();
        Attr attr = attributeIterator.getAttribute(element, name);
        assertNull(attr);
    }
    
    @Test
    public void testTestAttr() {
        Attr attr = createElement().getAttributeNode("attributeName");
        boolean result = attributeIterator.testAttr(attr, name);
        assertTrue(result);
    }
    
    @Test
    public void testEqualStrings() {
        boolean result1 = attributeIterator.equalStrings("str1", "str1");
        assertTrue(result1);
        
        boolean result2 = attributeIterator.equalStrings(null, null);
        assertTrue(result2);
        
        boolean result3 = attributeIterator.equalStrings("str1", "str2");
        assertFalse(result3);
        
        boolean result4 = attributeIterator.equalStrings("str1", null);
        assertFalse(result4);
        
        boolean result5 = attributeIterator.equalStrings(null, "str2");
        assertFalse(result5);
    }
    
    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = attributeIterator.getNodePointer();
        assertNull(nodePointer);
    }
    
    @Test
    public void testGetPosition() {
        int position = attributeIterator.getPosition();
        assertEquals(0, position);
    }
    
    @Test
    public void testSetPosition() {
        boolean result1 = attributeIterator.setPosition(1);
        assertFalse(result1);
        
        boolean result2 = attributeIterator.setPosition(0);
        assertFalse(result2);
    }
}