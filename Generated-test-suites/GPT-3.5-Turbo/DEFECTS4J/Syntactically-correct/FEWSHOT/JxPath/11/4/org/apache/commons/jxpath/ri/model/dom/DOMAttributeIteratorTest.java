package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator attributeIterator;
    
    @Before
    public void setUp() {
        // Create a dummy parent NodePointer
        parent = new DummyNodePointer();
        name = new QName(null, "test");
        attributeIterator = new DOMAttributeIterator(parent, name);
    }
    
    @Test
    public void testConstructor() {
        // Verify attributes list is initialized
        assertTrue(attributeIterator.getAttributes().isEmpty());
        
        // Verify position is initialized to 0
        assertEquals(0, attributeIterator.getPosition());
    }
    
    @Test
    public void testGetNodePointer() {
        // Verify getNodePointer returns null at position 0
        assertNull(attributeIterator.getNodePointer());
        
        // Add attributes to attributes list
        Element element = createDummyElement();
        Attr attr1 = element.getAttributeNode("attr1");
        Attr attr2 = element.getAttributeNode("attr2");
        attributeIterator.getAttributes().add(attr1);
        attributeIterator.getAttributes().add(attr2);
        
        // Verify getNodePointer returns the correct NodePointer at position 1
        NodePointer nodePointer1 = attributeIterator.getNodePointer();
        assertTrue(nodePointer1 instanceof DOMAttributePointer);
        assertEquals(attr1, ((DOMAttributePointer) nodePointer1).getAttribute());
        
        // Verify getNodePointer returns the correct NodePointer at position 2
        attributeIterator.setPosition(2);
        NodePointer nodePointer2 = attributeIterator.getNodePointer();
        assertTrue(nodePointer2 instanceof DOMAttributePointer);
        assertEquals(attr2, ((DOMAttributePointer) nodePointer2).getAttribute());
    }
    
    @Test
    public void testGetPosition() {
        // Verify initial position is 0
        assertEquals(0, attributeIterator.getPosition());
        
        // Set position to 1 and verify it is updated correctly
        attributeIterator.setPosition(1);
        assertEquals(1, attributeIterator.getPosition());
        
        // Set position to 2 and verify it is updated correctly
        attributeIterator.setPosition(2);
        assertEquals(2, attributeIterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        // Set position to an invalid value and verify it returns false
        assertFalse(attributeIterator.setPosition(0));
        
        // Set position to 1 and verify it returns true
        assertTrue(attributeIterator.setPosition(1));
        
        // Set position to a value greater than the size of the attributes list and verify it returns false
        assertFalse(attributeIterator.setPosition(3));
    }
    
    private Element createDummyElement() {
        Element element = new DummyElement();
        NamedNodeMap attributes = new DummyNamedNodeMap();
        element.setAttributes(attributes);
        return element;
    }
    
    public class DummyNodePointer extends NodePointer {
        @Override
        public Object getImmediateNode() {
            return null;
        }

        @Override
        public QName getName() {
            return null;
        }

        // Implement other abstract methods if needed
    }
    
    public class DummyElement implements Element {
        @Override
        public String getAttribute(String name) {
            return null;
        }

        @Override
        public void setAttribute(String name, String value) {
        }

        // Implement other interface methods if needed
    }
    
    public class DummyNamedNodeMap implements NamedNodeMap {
        @Override
        public Node getNamedItem(String name) {
            return null;
        }

        // Implement other interface methods if needed
    }
}