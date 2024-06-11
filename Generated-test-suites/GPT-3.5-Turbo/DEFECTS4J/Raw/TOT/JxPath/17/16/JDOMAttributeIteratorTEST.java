import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    private NodePointer parent;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        parent = new NodePointer();
        // Create a dummy parent node
        Element element = new Element("dummy");
        // Create a dummy attribute with no namespace
        Attribute attribute1 = new Attribute("attr1", "value1");
        element.setAttribute(attribute1);
        // Create a dummy attribute with a namespace
        Attribute attribute2 = new Attribute("attr2", "value2", "testNamespace");
        element.setAttribute(attribute2);
        parent.setNode(element);
        attributeIterator = new JDOMAttributeIterator(parent, new QName(null, "*"));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer1 = attributeIterator.getNodePointer();
        assertNotNull(nodePointer1);
        assertEquals("attr1", ((Attribute) nodePointer1.getNode()).getName());
        assertEquals("value1", ((Attribute) nodePointer1.getNode()).getValue());

        assertTrue(attributeIterator.setPosition(2));
        NodePointer nodePointer2 = attributeIterator.getNodePointer();
        assertNotNull(nodePointer2);
        assertEquals("attr2", ((Attribute) nodePointer2.getNode()).getName());
        assertEquals("value2", ((Attribute) nodePointer2.getNode()).getValue());
        assertEquals("testNamespace", ((Attribute) nodePointer2.getNode()).getNamespace().getPrefix());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, attributeIterator.getPosition());

        assertTrue(attributeIterator.setPosition(2));
        assertEquals(2, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertFalse(attributeIterator.setPosition(0));
        assertTrue(attributeIterator.setPosition(2));
        assertFalse(attributeIterator.setPosition(3));
    }

    @Test
    public void testSetPositionAndGetNodePointer() {
        assertTrue(attributeIterator.setPosition(1));
        NodePointer nodePointer1 = attributeIterator.getNodePointer();
        assertNotNull(nodePointer1);
        assertEquals("attr1", ((Attribute) nodePointer1.getNode()).getName());
        assertEquals("value1", ((Attribute) nodePointer1.getNode()).getValue());

        assertTrue(attributeIterator.setPosition(2));
        NodePointer nodePointer2 = attributeIterator.getNodePointer();
        assertNotNull(nodePointer2);
        assertEquals("attr2", ((Attribute) nodePointer2.getNode()).getName());
        assertEquals("value2", ((Attribute) nodePointer2.getNode()).getValue());
        assertEquals("testNamespace", ((Attribute) nodePointer2.getNode()).getNamespace().getPrefix());
    }

}