import static org.junit.Assert.*;
import java.lang.reflect.*;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    private Element element;
    private JDOMAttributeIterator iterator;
    private JDOMAttributePointer pointer;

    @Before
    public void setUp() {
        element = new Element("test");
        Attribute attribute = new Attribute("name", "value");
        element.setAttribute(attribute);
        iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("name"));
    }

    @Test
    public void testGetNodePointer() {
        pointer = (JDOMAttributePointer) iterator.getNodePointer();
        assertNotNull(pointer);
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(2));
    }
}