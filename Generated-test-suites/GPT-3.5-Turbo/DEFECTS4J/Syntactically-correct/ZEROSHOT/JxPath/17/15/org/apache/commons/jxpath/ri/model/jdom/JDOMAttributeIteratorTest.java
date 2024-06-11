import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        // Create a parent NodePointer
        NodePointer parent = new JDOMNodePointer(null, null);

        // Create an Element with attributes for testing
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");

        QName name = new QName(null, null, "attr1");
        iterator = new JDOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();

        assertNotNull(nodePointer);
        assertEquals(JDOMAttributePointer.class, nodePointer.getClass());

        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        assertEquals("attr1", attributePointer.getAttribute().getName());
        assertEquals("value1", attributePointer.getAttribute().getValue());
    }

    @Test
    public void testGetPosition() {
        int position = iterator.getPosition();

        assertEquals(0, position);

        iterator.setPosition(2);
        position = iterator.getPosition();

        assertEquals(2, position);
    }

    @Test
    public void testSetPosition() {
        boolean result = iterator.setPosition(6);

        assertFalse(result);
        assertEquals(0, iterator.getPosition());

        result = iterator.setPosition(2);

        assertTrue(result);
        assertEquals(2, iterator.getPosition());
    }

}