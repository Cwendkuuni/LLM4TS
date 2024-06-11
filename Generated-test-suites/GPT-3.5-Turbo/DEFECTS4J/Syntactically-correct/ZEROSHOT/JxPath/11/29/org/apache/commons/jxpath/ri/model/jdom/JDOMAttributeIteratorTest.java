import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {
    
    @Test
    public void testGetNodePointer() {
        // Test when attributes is null
        JDOMAttributeIterator iterator1 = new JDOMAttributeIterator(null, new QName("name"));
        assertNull(iterator1.getNodePointer());
        
        // Test when attributes is empty
        JDOMAttributeIterator iterator2 = new JDOMAttributeIterator(new NodePointer(), new QName("name"));
        assertNull(iterator2.getNodePointer());
        
        // Test when attributes has one attribute
        NodePointer parent = new NodePointer();
        Element element = new Element("element");
        element.setAttribute("attr", "value");
        parent.setNode(element);
        JDOMAttributeIterator iterator3 = new JDOMAttributeIterator(parent, new QName("attr"));
        JDOMAttributePointer expectedPointer = new JDOMAttributePointer(parent, element.getAttribute("attr"));
        assertEquals(expectedPointer, iterator3.getNodePointer());
        
        // Test when attributes has multiple attributes with the same namespace
        Element element2 = new Element("element");
        element2.setAttribute("attr1", "value1");
        element2.setAttribute("attr2", "value2");
        element2.setAttribute("attr3", "value3");
        parent.setNode(element2);
        JDOMAttributeIterator iterator4 = new JDOMAttributeIterator(parent, new QName("*"));
        JDOMAttributePointer expectedPointer2 = new JDOMAttributePointer(parent, element2.getAttribute("attr1"));
        assertEquals(expectedPointer2, iterator4.getNodePointer());
    }
    
    @Test
    public void testGetPosition() {
        NodePointer parent = new NodePointer();
        Element element = new Element("element");
        element.setAttribute("attr", "value");
        parent.setNode(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr"));
        assertEquals(0, iterator.getPosition());

        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        NodePointer parent = new NodePointer();
        Element element = new Element("element");
        element.setAttribute("attr", "value");
        parent.setNode(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr"));

        assertFalse(iterator.setPosition(0));
        assertTrue(iterator.setPosition(1));
        assertFalse(iterator.setPosition(2));
    }
}