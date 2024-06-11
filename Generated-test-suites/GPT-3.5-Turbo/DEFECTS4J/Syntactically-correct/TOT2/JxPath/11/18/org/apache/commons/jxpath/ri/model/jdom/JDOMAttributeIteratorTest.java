import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        Element element = new Element("testElement");
        NodePointer parent = new JDOMNodePointer(element);
        QName name = new QName("testName");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        Element element = new Element("testElement");
        Attribute attribute = new Attribute("testAttribute", "value");
        element.setAttribute(attribute);
        
        NodePointer parent = new JDOMNodePointer(element);
        QName name = new QName("testAttribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertEquals(attribute, ((JDOMAttributePointer) iterator.getNodePointer()).getAttribute());
    }
  
    @Test
    public void testSetPosition() {
        Element element = new Element("testElement");
        Attribute attribute1 = new Attribute("testAttribute1", "value1");
        Attribute attribute2 = new Attribute("testAttribute2", "value2");
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);
        
        NodePointer parent = new JDOMNodePointer(element);
        QName name = new QName("*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
        assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testEdgeCases() {
        Element element = new Element("testElement");
        Attribute attribute = new Attribute("testAttribute", "value");
        element.setAttribute(attribute);
        
        NodePointer parent = new JDOMNodePointer(element);
        QName name = new QName("invalidAttribute");
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testExceptionHandling() {
        Element element = new Element("testElement");
        NodePointer parent = new JDOMNodePointer(element);
        QName name = new QName("testAttribute");
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }
}