import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");

        NodePointer parent = new JDOMNodePointer(element);
        QName name = new QName("", "*");

        attributeIterator = new JDOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = attributeIterator.getNodePointer();

        assertNotNull(nodePointer);

        assertTrue(nodePointer instanceof JDOMAttributePointer);
    }

    @Test
    public void testGetPosition() {
        int position = attributeIterator.getPosition();

        assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        assertFalse(attributeIterator.setPosition(0));
        assertTrue(attributeIterator.setPosition(1));
        assertEquals(1, attributeIterator.getPosition());
        assertTrue(attributeIterator.setPosition(3));
        assertEquals(3, attributeIterator.getPosition());
        assertFalse(attributeIterator.setPosition(4));
    }
}