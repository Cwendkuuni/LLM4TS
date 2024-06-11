import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    private NodePointer parent;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");

        parent = new NodePointer(element, null);
        iterator = new JDOMAttributeIterator(parent, new QName("*"));
    }

    @Test
    public void testGetPosition() {
        assertTrue(iterator.getPosition() == 0);
        iterator.setPosition(2);
        assertTrue(iterator.getPosition() == 2);
    }

    @Test
    public void testSetPosition() {
        assertFalse(iterator.setPosition(0));
        assertTrue(iterator.setPosition(1));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer.getNode() instanceof Attribute);
        Attribute attribute = (Attribute) nodePointer.getNode();
        assertTrue(attribute.getName().equals("attr1"));
    }

    @Test
    public void testGetNodePointer_withPosition() {
        iterator.setPosition(2);
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer.getNode() instanceof Attribute);
        Attribute attribute = (Attribute) nodePointer.getNode();
        assertTrue(attribute.getName().equals("attr2"));
    }

    @Test
    public void testGetNodePointer_withInvalidPosition() {
        iterator.setPosition(4);
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer == null);
    }

    @Test
    public void testGetNodePointer_withSpecificAttribute() {
        iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer.getNode() instanceof Attribute);
        Attribute attribute = (Attribute) nodePointer.getNode();
        assertTrue(attribute.getName().equals("attr1"));
        assertTrue(attribute.getValue().equals("value1"));
    }

    @Test
    public void testGetNodePointer_withNonExistingAttribute() {
        iterator = new JDOMAttributeIterator(parent, new QName("attr4"));
        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer == null);
    }

    @Test
    public void testGetNodePointer_withNamespace() {
        Element namespaceElement = new Element("namespaceElement", "testNamespace");
        namespaceElement.setAttribute("attr1", "value1");
        namespaceElement.setAttribute("attr2", "value2");

        parent = new NodePointer(namespaceElement, null);

        iterator = new JDOMAttributeIterator(parent, new QName("*"));
        NodePointer nodePointer = iterator.getNodePointer();

        assertTrue(nodePointer.getNode() instanceof Attribute);
        Attribute attribute = (Attribute) nodePointer.getNode();
        assertTrue(attribute.getName().equals("attr1"));
    }

    @Test
    public void testGetNodePointer_withInvalidNamespace() {
        Element namespaceElement = new Element("namespaceElement", "testNamespace");
        namespaceElement.setAttribute("attr1", "value1");

        parent = new NodePointer(namespaceElement, null);

        iterator = new JDOMAttributeIterator(parent, new QName("attr1"));
        NodePointer nodePointer = iterator.getNodePointer();

        assertTrue(nodePointer == null);
    }

    @Test
    public void testGetNodePointer_withWildcard() {
        iterator = new JDOMAttributeIterator(parent, new QName("*"));
        List<String> attributeNames = new ArrayList<>();
        while (iterator.setPosition(iterator.getPosition() + 1)) {
            NodePointer nodePointer = iterator.getNodePointer();
            assertTrue(nodePointer.getNode() instanceof Attribute);
            Attribute attribute = (Attribute) nodePointer.getNode();
            attributeNames.add(attribute.getName());
        }

        List<String> expectedAttributeNames = Arrays.asList("attr1", "attr2", "attr3");
        assertTrue(attributeNames.size() == 3);
        assertTrue(attributeNames.containsAll(expectedAttributeNames));
    }
}