import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        Element element = new Element("testElement");
        element.setAttribute(new Attribute("attribute1", "value1"));
        element.setAttribute(new Attribute("attribute2", "value2"));
        iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("", "attribute1"));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals("value1", nodePointer.getValue());
    }

    @Test
    public void testGetPosition() {
        int position = iterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testSetPosition_validPosition_returnsTrue() {
        assertTrue(iterator.setPosition(1));
    }

    @Test
    public void testSetPosition_positionLessThan0_returnsFalse() {
        assertFalse(iterator.setPosition(-1));
    }

    @Test
    public void testSetPosition_positionGreaterThanSize_returnsFalse() {
        assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testSetPosition_positionWithinRange_returnsTrue() {
        assertTrue(iterator.setPosition(2));
    }

    @Test
    public void testSetPosition_invalidPosition_returnsFalse() {
        assertFalse(iterator.setPosition(0));
    }

}