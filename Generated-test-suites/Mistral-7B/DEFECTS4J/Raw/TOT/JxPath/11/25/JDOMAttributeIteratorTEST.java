import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.JDOMException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class for JDOMAttributeIterator
 */
public class JDOMAttributeIteratorTest {

    private Document document;
    private Element rootElement;

    @Before
    public void setup() throws JDOMException {
        document = new Document();
        rootElement = new Element("root");
        rootElement.addAttribute("attr1", "value1");
        rootElement.addAttribute("attr2", "value2");
        rootElement.addNamespace("ns1", "http://example.com/ns1");
        rootElement.addNamespace("ns2", "http://example.com/ns2");
        document.setRootElement(rootElement);
    }

    /**
     * Test method for {@link JDOMAttributeIterator#JDOMAttributeIterator(NodePointer, QName)} with specific attribute name.
     */
    @Test
    public void testIteratorWithSpecificAttributeName() {
        NodePointer parentPointer = new NodePointer(document, rootElement);
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("ns1:attr1"));
        NodePointer actualNode = iterator.getNodePointer();

        Assert.assertTrue("Expected iterator to return attribute node", actualNode instanceof JDOMAttributePointer);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) actualNode;
        Assert.assertEquals("Unexpected attribute name", "attr1", attributePointer.getAttribute().getName());
        Assert.assertEquals("Unexpected attribute value", "value1", attributePointer.getAttribute().getValue());
    }

    /**
     * Test method for {@link JDOMAttributeIterator#JDOMAttributeIterator(NodePointer, QName)} with wildcard attribute name.
     */
    @Test
    public void testIteratorWithWildcardAttributeName() {
        NodePointer parentPointer = new NodePointer(document, rootElement);
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName(null, "*"));
        NodePointer actualNode = iterator.getNodePointer();

        Assert.assertTrue("Expected iterator to return attribute node", actualNode instanceof JDOMAttributePointer);

        // Test for the first attribute node
        JDOMAttributePointer attributePointer1 = (JDOMAttributePointer) actualNode;
        Assert.assertEquals("Unexpected attribute name for the first node", "attr1", attributePointer1.getAttribute().getName());
        Assert.assertEquals("Unexpected attribute value for the first node", "value1", attributePointer1.getAttribute().getValue());

        // Test for the second attribute node
        if (iterator.hasNext()) {
            actualNode = iterator.nextNodePointer();
            JDOMAttributePointer attributePointer2 = (JDOMAttributePointer) actualNode;
            Assert.assertEquals("Unexpected attribute name for the second node", "attr2", attributePointer2.getAttribute().getName());
            Assert.assertEquals("Unexpected attribute value for the second node", "value2", attributePointer2.getAttribute().getValue());
        }

        // Test for the end of the iteration
        if (iterator.hasNext()) {
            actualNode = iterator.nextNodePointer();
            Assert.assertNull("Unexpected attribute node after the end of the iteration", actualNode);
        }
    }

    /**
     * Test method for {@link JDOMAttributeIterator#JDOMAttributeIterator(NodePointer, QName)} with non-existent attribute name.
     */
    @Test
    public void testIteratorWithNonExistentAttributeName() {
        NodePointer parentPointer = new NodePointer(document, rootElement);
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName("ns1:nonExistentAttribute"));
        NodePointer actualNode = iterator.getNodePointer();

        Assert.assertNull("Expected iterator to return null for non-existent attribute name", actualNode);
    }

    /**
     * Test method for {@link JDOMAttributeIterator#JDOMAttributeIterator(NodePointer, QName)} with empty list of attributes.
     */
    @Test
    public void testIteratorWithEmptyListOfAttributes() {
        NodePointer parentPointer = new NodePointer(document, new Element("empty"));
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName(null, "*"));
        NodePointer actualNode = iterator.getNodePointer();

        Assert.assertNull("Expected iterator to return null for empty list of attributes", actualNode);
    }

    /**
     * Test method for {@link JDOMAttributeIterator#getNodePointer()} with iterator at the beginning.
     */
    @Test
    public void testGetNodePointerWithIteratorAtBeginning() {
        NodePointer parentPointer = new NodePointer(document, rootElement);
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName(null, "*"));
        NodePointer actualNode = iterator.getNodePointer();

        Assert.assertTrue("Expected iterator to return the first attribute node", actualNode != null);
        Assert.assertTrue("Expected first attribute node to be an instance of JDOMAttributePointer", actualNode instanceof JDOMAttributePointer);
    }

    /**
     * Test method for {@link JDOMAttributeIterator#getNodePointer()} with iterator at the end.
     */
    @Test
    public void testGetNodePointerWithIteratorAtEnd() {
        NodePointer parentPointer = new NodePointer(document, rootElement);
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName(null, "*"));
        while (iterator.hasNext()) {
            iterator.nextNodePointer();
        }
        NodePointer actualNode = iterator.getNodePointer();

        Assert.assertNull("Expected iterator to return null for the end", actualNode);
    }

    /**
     * Test method for {@link JDOMAttributeIterator#getPosition()} with iterator at the beginning.
     */
    @Test
    public void testGetPositionWithIteratorAtBeginning() {
        NodePointer parentPointer = new NodePointer(document, rootElement);
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName(null, "*"));
        NodePointer actualNode = iterator.getNodePointer();

        Assert.assertEquals("Expected position to be 0 for the beginning", 0, iterator.getPosition());
    }

    /**
     * Test method for {@link JDOMAttributeIterator#getPosition()} with iterator at the end.
     */
    @Test
    public void testGetPositionWithIteratorAtEnd() {
        NodePointer parentPointer = new NodePointer(document, rootElement);
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName(null, "*"));
        while (iterator.hasNext()) {
            iterator.nextNodePointer();
        }

        Assert.assertEquals("Expected position to be -1 for the end", -1, iterator.getPosition());
    }

    /**
     * Test method for {@link JDOMAttributeIterator#setPosition(int)} with position before the beginning.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithPositionBeforeBeginning() {
        NodePointer parentPointer = new NodePointer(document, rootElement);
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName(null, "*"));
        iterator.setPosition(-1);
    }

    /**
     * Test method for {@link JDOMAttributeIterator#setPosition(int)} with position at the beginning.
     */
    @Test
    public void testSetPositionWithPositionAtBeginning() {
        NodePointer parentPointer = new NodePointer(document, rootElement);
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName(null, "*"));
        iterator.setPosition(0);
        NodePointer actualNode = iterator.getNodePointer();

        Assert.assertTrue("Expected iterator to return the first attribute node after setting position", actualNode != null);
        Assert.assertTrue("Expected first attribute node to be an instance of JDOMAttributePointer", actualNode instanceof JDOMAttributePointer);
    }

    /**
     * Test method for {@link JDOMAttributeIterator#setPosition(int)} with position at the end.
     */
    @Test
    public void testSetPositionWithPositionAtEnd() {
        NodePointer parentPointer = new NodePointer(document, rootElement);
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName(null, "*"));
        while (iterator.hasNext()) {
            iterator.nextNodePointer();
        }
        iterator.setPosition(-1);

        NodePointer actualNode = iterator.getNodePointer();
        Assert.assertNull("Expected iterator to return null after setting position to -1", actualNode);
    }

    /**
     * Test method for {@link JDOMAttributeIterator#setPosition(int)} with position in the middle.
     */
    @Test
    public void testSetPositionWithPositionInTheMiddle() {
        NodePointer parentPointer = new NodePointer(document, rootElement);
        NodeIterator iterator = new JDOMAttributeIterator(parentPointer, new QName(null, "*"));
        iterator.nextNodePointer();
        iterator.setPosition(1);
        NodePointer actualNode = iterator.getNodePointer();

        Assert.assertTrue("Expected iterator to return the second attribute node after setting position", actualNode != null);
        Assert.assertTrue("Expected second attribute node to be an instance of JDOMAttributePointer", actualNode instanceof JDOMAttributePointer);
    }
}