import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Text;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@DisplayName("JDOMAttributeIterator Test")
public class JDOMAttributeIteratorTest {

    private NodePointer parent;
    private QName qName;

    @BeforeEach
    public void setup() {
        Element parentElement = new Element("ParentElement");
        parentElement.setText("Sample Text");
        Attribute attribute1 = new Attribute("attr1", "value1");
        Attribute attribute2 = new Attribute("attr2", "value2");
        parentElement.setAttribute(attribute1);
        parentElement.setAttribute(attribute2);

        parent = new NodePointer(parentElement);
        qName = new QName("attr1");
    }

    @Test
    @DisplayName("Test getNodePointer() - Positive Case")
    public void testGetNodePointer() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        NodePointer nodePointer = iterator.getNodePointer();

        Assertions.assertNotNull(nodePointer);
        Assertions.assertTrue(nodePointer instanceof JDOMAttributePointer);
        Assertions.assertEquals("attr1", ((Attribute) nodePointer.getNode()).getName());
    }

    @Test
    @DisplayName("Test getNodePointer() - Null Case")
    public void testGetNodePointer_Null() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr3"));

        NodePointer nodePointer = iterator.getNodePointer();

        Assertions.assertNull(nodePointer);
    }

    @Test
    @DisplayName("Test getPosition() - Positive Case")
    public void testGetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        int position = iterator.getPosition();

        Assertions.assertEquals(0, position);
    }

    @Test
    @DisplayName("Test setPosition() - Positive Case")
    public void testSetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        boolean success = iterator.setPosition(1);

        Assertions.assertTrue(success);
        Assertions.assertEquals(1, iterator.getPosition());
    }

    @Test
    @DisplayName("Test setPosition() - Invalid Position")
    public void testSetPosition_InvalidPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        boolean success = iterator.setPosition(3);

        Assertions.assertFalse(success);
    }

    @Test
    @DisplayName("Test setPosition() - Null Attributes")
    public void testSetPosition_NullAttributes() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(null, qName);

        boolean success = iterator.setPosition(1);

        Assertions.assertFalse(success);
    }

    @Test
    @DisplayName("Test setPosition() - Zero Position")
    public void testSetPosition_ZeroPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        boolean success = iterator.setPosition(0);

        Assertions.assertTrue(success);
        Assertions.assertEquals(0, iterator.getPosition());
    }

    @Test
    @DisplayName("Test setPosition() - Negative Position")
    public void testSetPosition_NegativePosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        boolean success = iterator.setPosition(-1);

        Assertions.assertFalse(success);
    }

    @Test
    @DisplayName("Test setPosition() - Empty Attributes")
    public void testSetPosition_EmptyAttributes() {
        Element parentElement = new Element("EmptyParent");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(parentElement), qName);

        boolean success = iterator.setPosition(1);

        Assertions.assertFalse(success);
    }

    @Test
    @DisplayName("Test setPosition() - Out of Bound Position")
    public void testSetPosition_OutOfBoundPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        boolean success = iterator.setPosition(5);

        Assertions.assertFalse(success);
    }

    @Test
    @DisplayName("Test setPosition() - Negative Attributes")
    public void testGetPosition_NegativeAttributes() {
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("attr1", "value1"));
        attributes.add(new Attribute("attr2", "value2"));

        Element parentElement = new Element("NegativeParent");
        parentElement.setAttributes(attributes);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new NodePointer(parentElement), qName);

        int position = iterator.getPosition();

        Assertions.assertEquals(0, position);
    }

    @Test
    @DisplayName("Test setPosition() - Null Attribute Name")
    public void testSetPosition_NullAttributeName() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(null));

        boolean success = iterator.setPosition(1);

        Assertions.assertFalse(success);
    }

    @Test
    @DisplayName("Test setPosition() - Empty Attribute Name")
    public void testSetPosition_EmptyAttributeName() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(""));

        boolean success = iterator.setPosition(1);

        Assertions.assertFalse(success);
    }
}