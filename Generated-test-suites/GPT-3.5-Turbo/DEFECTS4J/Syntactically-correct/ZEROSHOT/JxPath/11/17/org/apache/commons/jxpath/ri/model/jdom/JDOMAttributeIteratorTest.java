import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;

public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        parent = new JDOMNodePointer(element);
        iterator = new JDOMAttributeIterator(parent, new QName("*"));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull("NodePointer is null", nodePointer);

        Attribute attribute = (Attribute) nodePointer.getNode();
        assertNotNull("Attribute is null", attribute);

        assertEquals("Attribute name is not correct", "attr1", attribute.getName());

        assertTrue("Position is not correct", iterator.getPosition() == 1);
    }

    @Test
    public void testGetNodePointerWithPosition() {
        assertTrue("Position is not correct", iterator.setPosition(2));

        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull("NodePointer is null", nodePointer);

        Attribute attribute = (Attribute) nodePointer.getNode();
        assertNotNull("Attribute is null", attribute);

        assertEquals("Attribute name is not correct", "attr2", attribute.getName());

        assertTrue("Position is not correct", iterator.getPosition() == 2);
    }

    @Test
    public void testGetNodePointerWithInvalidPosition() {
        assertFalse("Invalid position should return false", iterator.setPosition(3));

        NodePointer nodePointer = iterator.getNodePointer();
        assertNull("NodePointer should be null for invalid position", nodePointer);
    }

    @Test
    public void testGetPosition() {
        assertTrue("Initial position is not correct", iterator.getPosition() == 0);

        assertTrue("Position is not correct", iterator.setPosition(1));
        assertTrue("Position is not correct", iterator.getPosition() == 1);

        assertTrue("Position is not correct", iterator.setPosition(2));
        assertTrue("Position is not correct", iterator.getPosition() == 2);

        assertFalse("Position is not correct", iterator.setPosition(3));
        assertTrue("Position is not correct", iterator.getPosition() == 0);
    }

    @Test
    public void testSetPosition() {
        assertFalse("Position should be invalid", iterator.setPosition(0));

        assertTrue("Position is not correct", iterator.setPosition(1));
        assertTrue("Position is not correct", iterator.getPosition() == 1);

        assertTrue("Position is not correct", iterator.setPosition(2));
        assertTrue("Position is not correct", iterator.getPosition() == 2);

        assertFalse("Position should be invalid", iterator.setPosition(3));
        assertTrue("Position is not correct", iterator.getPosition() == 0);
    }
}