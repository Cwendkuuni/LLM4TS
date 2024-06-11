import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
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
    private NodePointer parent;
    private QName name;

    @Before
    public void setUp() {
        parent = new NodePointer();
        name = new QName();
        attributeIterator = new JDOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer_PositiveCase() {
        Element element = new Element("element");
        Attribute attribute = new Attribute("attribute", "value");
        element.setAttribute(attribute);
        parent.setNode(element);

        NodePointer nodePointer = attributeIterator.getNodePointer();

        assertNotNull(nodePointer);
        assertEquals(attribute, nodePointer.getNode());
    }

    @Test
    public void testGetNodePointer_NullCase() {
        Element element = new Element("element");
        parent.setNode(element);

        NodePointer nodePointer = attributeIterator.getNodePointer();

        assertNull(nodePointer);
    }

    @Test
    public void testGetPosition() {
        int position = attributeIterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testSetPosition_PositiveCase() {
        List<Attribute> attributes = new ArrayList<>();
        Attribute attribute1 = new Attribute("attribute1", "value1");
        Attribute attribute2 = new Attribute("attribute2", "value2");
        attributes.add(attribute1);
        attributes.add(attribute2);
        attributeIterator.attributes = attributes;

        boolean result = attributeIterator.setPosition(1);

        assertTrue(result);
        assertEquals(1, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_OutOfBounds() {
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("attribute1", "value1"));
        attributes.add(new Attribute("attribute2", "value2"));
        attributeIterator.attributes = attributes;

        boolean result = attributeIterator.setPosition(3);

        assertFalse(result);
        assertEquals(0, attributeIterator.getPosition());
    }
}