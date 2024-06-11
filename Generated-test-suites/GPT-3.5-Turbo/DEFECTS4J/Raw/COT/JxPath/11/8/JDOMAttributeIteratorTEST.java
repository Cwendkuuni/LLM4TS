import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.jdom.*;

public class JDOMAttributeIteratorTest {

    @Test
    public void testGetNodePointer() {
        // Test with an element that has no attributes
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(null), new QName("attribute"));
        assertNull(iterator.getNodePointer());
        
        // Test with an element that has attributes
        ElementWithAttributes element = new ElementWithAttributes();
        element.addAttribute(new Attribute("attribute1", "value1"));
        element.addAttribute(new Attribute("attribute2", "value2"));
        iterator = new JDOMAttributeIterator(new JDOMNodePointer(element), new QName("attribute"));
        assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        // Test with an element that has no attributes
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(null), new QName("attribute"));
        assertEquals(0, iterator.getPosition());
        
        // Test with an element that has attributes
        ElementWithAttributes element = new ElementWithAttributes();
        element.addAttribute(new Attribute("attribute1", "value1"));
        element.addAttribute(new Attribute("attribute2", "value2"));
        iterator = new JDOMAttributeIterator(new JDOMNodePointer(element), new QName("attribute"));
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        // Test with an element that has no attributes
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(null), new QName("attribute"));
        assertFalse(iterator.setPosition(1));
        
        // Test with an element that has attributes
        ElementWithAttributes element = new ElementWithAttributes();
        element.addAttribute(new Attribute("attribute1", "value1"));
        element.addAttribute(new Attribute("attribute2", "value2"));
        iterator = new JDOMAttributeIterator(new JDOMNodePointer(element), new QName("attribute"));
        assertTrue(iterator.setPosition(1));
    }

    @Test
    public void testConstructore() {
        // Test with an element that has no attributes
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(null), new QName("attribute"));
        assertEquals(0, iterator.attributes.size());
        
        // Test with an element that has attributes
        ElementWithAttributes element = new ElementWithAttributes();
        element.addAttribute(new Attribute("attribute1", "value1"));
        element.addAttribute(new Attribute("attribute2", "value2"));
        iterator = new JDOMAttributeIterator(new JDOMNodePointer(element), new QName("attribute"));
        assertEquals(2, iterator.attributes.size());
    }
}