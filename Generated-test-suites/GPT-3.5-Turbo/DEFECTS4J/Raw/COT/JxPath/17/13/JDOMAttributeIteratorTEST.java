import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    
    private Element element;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        iterator = new JDOMAttributeIterator(new JDOMNodePointer(element), new QName("attr1"));
    }

    @Test
    public void testConstructor() {
        assertNotNull(iterator);
        assertTrue(iterator.getNodePointer() instanceof JDOMAttributePointer);
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testGetNodePointer() {
        JDOMAttributePointer pointer = (JDOMAttributePointer) iterator.getNodePointer();
        assertNotNull(pointer);
        assertEquals("attr1", pointer.getName());
        assertEquals("value1", pointer.getValue());
    }

    @Test
    public void testGetPosition() {
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(2));
        assertEquals(2, iterator.getPosition());
        JDOMAttributePointer pointer = (JDOMAttributePointer) iterator.getNodePointer();
        assertNotNull(pointer);
        assertEquals("attr2", pointer.getName());
        assertEquals("value2", pointer.getValue());
    }
}