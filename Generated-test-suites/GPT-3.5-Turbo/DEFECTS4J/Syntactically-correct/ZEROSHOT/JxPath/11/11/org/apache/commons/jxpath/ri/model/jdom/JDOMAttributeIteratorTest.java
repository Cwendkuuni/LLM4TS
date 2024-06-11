import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Element;
import org.jdom.Attribute;
import org.jdom.Namespace;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {
    private Element element;

    @Before
    public void setup() {
        element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));

        assertEquals("Attribute name should be attr1", "attr1", iterator.getNodePointer().getName().getName());
        assertEquals("Attribute value should be value1", "value1", ((Attribute) iterator.getNodePointer().getNode()).getValue());
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));

        assertEquals("Initial position should be 0", 0, iterator.getPosition());

        iterator.setPosition(1);
        assertEquals("Position should be set to 1", 1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));

        assertTrue("Set position to 1 should return true", iterator.setPosition(1));
        assertFalse("Set position to 0 should return false", iterator.setPosition(0));
        assertFalse("Set position to -1 should return false", iterator.setPosition(-1));
        assertFalse("Set position to 10 should return false", iterator.setPosition(10));
    }
}