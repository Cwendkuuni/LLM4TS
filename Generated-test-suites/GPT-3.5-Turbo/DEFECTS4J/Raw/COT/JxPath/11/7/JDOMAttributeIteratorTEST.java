
import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class JDOMAttributeIteratorTest {
    private static Element element;

    @BeforeClass
    public static void setUp() {
        element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parentPointer = new NodePointer(null, element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", "*"));
        NodePointer attributePointer = iterator.getNodePointer();

        assertNotNull(attributePointer);
        assertTrue(attributePointer instanceof JDOMAttributePointer);
        assertEquals(attributePointer.getName(), "attr1");
        assertEquals(((Attribute) attributePointer.getNode()).getValue(), "value1");
    }

    @Test
    public void testGetPosition() {
        NodePointer parentPointer = new NodePointer(null, element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", "*"));

        int position = iterator.getPosition();

        assertEquals(position, 0);
    }

    @Test
    public void testSetPositionValid() {
        NodePointer parentPointer = new NodePointer(null, element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", "*"));

        boolean success = iterator.setPosition(1);

        assertTrue(success);
        assertEquals(iterator.getPosition(), 1);
    }

    @Test
    public void testSetPositionInvalid() {
        NodePointer parentPointer = new NodePointer(null, element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("", "*"));

        boolean success = iterator.setPosition(0);

        assertFalse(success);
        assertEquals(iterator.getPosition(), 0);
    }
}

