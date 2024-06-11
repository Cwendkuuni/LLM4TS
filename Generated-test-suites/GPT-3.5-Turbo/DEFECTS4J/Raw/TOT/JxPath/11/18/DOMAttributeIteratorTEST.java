import org.junit.Test;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

    @Test
    public void testGetNodePointer() {
        // Create a sample DOM hierarchy
        Document document = createSampleDom();

        // Create a NodePointer for the parent node
        NodePointer parentPointer = new DOMNodePointer(document);

        // Create a QName for the attribute name to be tested
        QName attributeName = new QName(null, "attr", "test-prefix");

        // Create a DOMAttributeIterator with the parent pointer and attribute name
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, attributeName);

        // Get the NodePointer from the iterator
        NodePointer nodePointer = iterator.getNodePointer();

        // Assert the retrieved NodePointer is not null
        assertNotNull(nodePointer);

        // Assert the NodePointer is of type DOMAttributePointer
        assertTrue(nodePointer instanceof DOMAttributePointer);

        // Assert that the name of the attribute matches the expected value
        assertEquals("test-value", ((DOMAttributePointer) nodePointer).getAttribute().getValue());
    }

    @Test
    public void testGetPosition() {
        // Create a sample DOM hierarchy
        Document document = createSampleDom();

        // Create a NodePointer for the parent node
        NodePointer parentPointer = new DOMNodePointer(document);

        // Create a QName for the attribute name to be tested
        QName attributeName = new QName(null, "attr", "test-prefix");

        // Create a DOMAttributeIterator with the parent pointer and attribute name
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, attributeName);

        // Assert initial position is 0
        assertEquals(0, iterator.getPosition());

        // Move the position to 1
        iterator.setPosition(1);

        // Assert the updated position is 1
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        // Create a sample DOM hierarchy
        Document document = createSampleDom();

        // Create a NodePointer for the parent node
        NodePointer parentPointer = new DOMNodePointer(document);

        // Create a QName for the attribute name to be tested
        QName attributeName = new QName(null, "attr", "test-prefix");

        // Create a DOMAttributeIterator with the parent pointer and attribute name
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, attributeName);

        // Set the position to an invalid negative value
        assertFalse(iterator.setPosition(-1));

        // Set the position to a valid value within the range
        assertTrue(iterator.setPosition(2));

        // Set the position to an invalid value greater than the size of attributes list
        assertFalse(iterator.setPosition(5));
    }

    private Document createSampleDom() {
        // Create a new DOM Document
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        // Create a root element
        Element rootElement = document.createElement("root");
        document.appendChild(rootElement);

        // Create an element with attributes
        Element element = document.createElement("test-element");
        element.setAttribute("attr", "test-value");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");
        rootElement.appendChild(element);

        return document;
    }
}