import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        Element element = new Element("testElement");
        Namespace namespace = Namespace.getNamespace("testNamespace");
        element.setAttribute("attr1", "value1", namespace);
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMAttributePointer(element), new QName("attr1"));
        
        assertNotNull(iterator);
        assertEquals(1, iterator.attributes.size());
    }

    @Test
    public void testGetNodePointer() {
        Element element = new Element("testElement");
        Namespace namespace = Namespace.getNamespace("testNamespace");
        element.setAttribute("attr1", "value1", namespace);
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMAttributePointer(element), new QName("attr1"));
        
        JDOMAttributePointer pointer = (JDOMAttributePointer) iterator.getNodePointer();
        
        assertNotNull(pointer);
        assertEquals("attr1", pointer.node.getName());
        assertEquals("value1", pointer.node.getValue());
    }

    @Test
    public void testSetPosition() {
        Element element = new Element("testElement");
        Namespace namespace = Namespace.getNamespace("testNamespace");
        element.setAttribute("attr1", "value1", namespace);
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMAttributePointer(element), new QName("attr1"));
        
        assertTrue(iterator.setPosition(1));
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(2));
    }
}