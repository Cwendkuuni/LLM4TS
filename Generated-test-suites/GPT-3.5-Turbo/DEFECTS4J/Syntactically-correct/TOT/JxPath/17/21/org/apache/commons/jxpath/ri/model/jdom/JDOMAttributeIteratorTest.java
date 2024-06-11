import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    private NodePointer parent;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        Element element = new Element("root");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");

        parent = new JDOMNodePointer(element);

        attributeIterator = new JDOMAttributeIterator(parent, new QName("attr"));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = attributeIterator.getNodePointer();
        assertNotNull(nodePointer);

        // Check the node pointer type
        assertTrue(nodePointer instanceof JDOMAttributePointer);

        // Check the attribute value
        Attribute attribute = (Attribute) nodePointer.getNode();
        assertEquals("value1", attribute.getValue());

        // Move to the next attribute
        attributeIterator.setPosition(2);

        // Check the node pointer type
        assertTrue(nodePointer instanceof JDOMAttributePointer);

        // Check the attribute value
        attribute = (Attribute) nodePointer.getNode();
        assertEquals("value2", attribute.getValue());

        // Move to the third attribute
        attributeIterator.setPosition(3);

        // Check the node pointer type
        assertTrue(nodePointer instanceof JDOMAttributePointer);

        // Check the attribute value
        attribute = (Attribute) nodePointer.getNode();
        assertEquals("value3", attribute.getValue());

        // Try to move to an invalid position
        attributeIterator.setPosition(4);
        assertNull(attributeIterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, attributeIterator.getPosition());

        attributeIterator.setPosition(1);
        assertEquals(1, attributeIterator.getPosition());

        attributeIterator.setPosition(2);
        assertEquals(2, attributeIterator.getPosition());

        attributeIterator.setPosition(3);
        assertEquals(3, attributeIterator.getPosition());

        attributeIterator.setPosition(4);
        assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertTrue(attributeIterator.setPosition(1));
        assertEquals("value1", ((Attribute) attributeIterator.getNodePointer().getNode()).getValue());

        assertTrue(attributeIterator.setPosition(2));
        assertEquals("value2", ((Attribute) attributeIterator.getNodePointer().getNode()).getValue());

        assertTrue(attributeIterator.setPosition(3));
        assertEquals("value3", ((Attribute) attributeIterator.getNodePointer().getNode()).getValue());

        assertFalse(attributeIterator.setPosition(4));
        assertFalse(attributeIterator.setPosition(0));
    }
}