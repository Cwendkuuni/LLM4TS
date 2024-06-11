import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        // Test with valid arguments
        NodePointer parent = new NodePointer();
        QName name = new QName("test");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        
        // Test with null parent
        JDOMAttributeIterator iterator2 = new JDOMAttributeIterator(null, name);
        assertNotNull(iterator2);
        
        // Test with null name
        JDOMAttributeIterator iterator3 = new JDOMAttributeIterator(parent, null);
        assertNotNull(iterator3);
    }
    
    @Test
    public void testGetNodePointer() {
        // Test with valid position
        NodePointer parent = new NodePointer();
        QName name = new QName("test");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        
        // Test with invalid position
        iterator.setPosition(0);
        assertNull(iterator.getNodePointer());
    }
    
    @Test
    public void testGetPosition() {
        NodePointer parent = new NodePointer();
        QName name = new QName("test");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        // Test with initial position
        assertEquals(0, iterator.getPosition());
        
        // Test with updated position
        iterator.setPosition(3);
        assertEquals(3, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        NodePointer parent = new NodePointer();
        QName name = new QName("test");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        // Test with valid position
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        
        // Test with invalid position
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(-1));
        assertFalse(iterator.setPosition(10));
        assertEquals(1, iterator.getPosition());
    }
    
    @Test
    public void testEmptyAttributeList() {
        NodePointer parent = new NodePointer(new Element("test"));
        QName name = new QName("test");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertFalse(iterator.setPosition(1));
        assertNull(iterator.getNodePointer());
    }
    
    @Test
    public void testWildcardName() {
        NodePointer parent = new NodePointer(new Element("test"));
        QName name = new QName("*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertFalse(iterator.setPosition(1));
        assertNull(iterator.getNodePointer());
        
        parent.setValue(new Attribute("name", "value"));
        
        assertTrue(iterator.setPosition(1));
        assertNotNull(iterator.getNodePointer());
    }
    
    @Test
    public void testNullParentNode() {
        QName name = new QName("test");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, name);
        
        assertFalse(iterator.setPosition(1));
        assertNull(iterator.getNodePointer());
    }
    
    @Test
    public void testNamespaceXmlPrefix() {
        NodePointer parent = new NodePointer(new Element("test"));
        QName name = new QName("xml:attr");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertTrue(iterator.setPosition(1));
        assertNotNull(iterator.getNodePointer());
        assertEquals(Namespace.XML_NAMESPACE, iterator.getNodePointer().getAttribute().getNamespace());
    }
    
    @Test
    public void testNamespaceNotXmlPrefix() {
        NodePointer parent = new NodePointer(new Element("test"));
        QName name = new QName("custom:attr");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertFalse(iterator.setPosition(1));
        assertNull(iterator.getNodePointer());
    }
    
    @Test
    public void testNullNameArgument() {
        NodePointer parent = new NodePointer(new Element("test"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.setPosition(1));
        assertNull(iterator.getNodePointer());
    }
    
}