import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Text;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        Element element = new Element("test");
        Element parent = new Element("parent");
        parent.addContent(element);
        NodePointer parentPointer = new NodePointer(parent);

        QName name = new QName("test");
        JDOMAttributeIterator attrIterator = new JDOMAttributeIterator(parentPointer, name);
        
        assertNotNull(attrIterator);
    }

    @Test
    public void testConstructorWithEmptyElement() {
        Element element = new Element("test");
        Element parent = new Element("parent");
        parent.addContent(element);
        NodePointer parentPointer = new NodePointer(parent);

        QName name = new QName("nonexistent");
        JDOMAttributeIterator attrIterator = new JDOMAttributeIterator(parentPointer, name);
        
        assertNotNull(attrIterator);
        assertFalse(attrIterator.setPosition(1));
    }
    
    @Test
    public void testGetNodePointer() {
        Element element = new Element("test");
        element.setAttribute(new Attribute("attr1", "value1"));
        Element parent = new Element("parent");
        parent.addContent(element);
        NodePointer parentPointer = new NodePointer(parent);

        QName name = new QName("attr1");
        JDOMAttributeIterator attrIterator = new JDOMAttributeIterator(parentPointer, name);
        
        NodePointer nodePointer = attrIterator.getNodePointer();
        
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        
        Attribute attribute = ((JDOMAttributePointer) nodePointer).getAttribute();
        assertNotNull(attribute);
        assertEquals("attr1", attribute.getName());
        assertEquals("value1", attribute.getValue());
    }
    
    @Test
    public void testGetNodePointerWithPrefix() {
        Element element = new Element("test1");
        element.setNamespace(Namespace.getNamespace("pre", "uri"));
        
        Element parent = new Element("parent");
        parent.addContent(element);
        NodePointer parentPointer = new NodePointer(parent);

        QName name = new QName("pre:test1");
        JDOMAttributeIterator attrIterator = new JDOMAttributeIterator(parentPointer, name);
        
        NodePointer nodePointer = attrIterator.getNodePointer();
        
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        
        Attribute attribute = ((JDOMAttributePointer) nodePointer).getAttribute();
        assertNotNull(attribute);
        assertEquals("test1", attribute.getName());
    }
    
    @Test
    public void testGetPosition() {
        Element element = new Element("test");
        element.setAttribute(new Attribute("attr1", "value1"));
        element.setAttribute(new Attribute("attr2", "value2"));
        Element parent = new Element("parent");
        parent.addContent(element);
        NodePointer parentPointer = new NodePointer(parent);

        QName name = new QName("*");
        JDOMAttributeIterator attrIterator = new JDOMAttributeIterator(parentPointer, name);
        
        attrIterator.setPosition(2);
        
        int position = attrIterator.getPosition();
        
        assertEquals(2, position);
    }
    
    @Test
    public void testSetPosition() {
        Element element = new Element("test");
        element.setAttribute(new Attribute("attr1", "value1"));
        element.setAttribute(new Attribute("attr2", "value2"));
        Element parent = new Element("parent");
        parent.addContent(element);
        NodePointer parentPointer = new NodePointer(parent);

        QName name = new QName("*");
        JDOMAttributeIterator attrIterator = new JDOMAttributeIterator(parentPointer, name);
        
        assertTrue(attrIterator.setPosition(2));
        assertFalse(attrIterator.setPosition(0));
        assertFalse(attrIterator.setPosition(3));
    }
}