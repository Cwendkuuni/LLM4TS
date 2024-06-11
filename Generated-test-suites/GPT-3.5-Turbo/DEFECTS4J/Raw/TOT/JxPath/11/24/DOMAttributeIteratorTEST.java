
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

    private NodePointer nodePointer;

    @Before
    public void setup() {
        // Create a test DOM Node for testing the DOMAttributeIterator
        Document document = TestUtils.buildTestDocument();
        Element element = TestUtils.createElement(document, "testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "");
        element.setAttribute("attr4", "true");
        element.setAttribute("attr5", "false");
        element.setAttribute("attr6", "123");
        element.setAttribute("attr7", "123.45");
        element.setAttribute("attr8", null);
        Text textNode = document.createTextNode("testTextNode");
        element.appendChild(textNode);

        nodePointer = new DOMAttributePointer(null, element);
    }

    @Test
    public void testGetNodePointer_AttributesExist_ReturnsFirstAttribute() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
        NodePointer attributeNodePointer = iterator.getNodePointer();
        assertNotNull(attributeNodePointer);
        assertEquals("attr1", attributeNodePointer.asString());
    }

    @Test
    public void testGetNodePointer_AttributesNotExist_ReturnsNull() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("nonexistent"));
        NodePointer attributeNodePointer = iterator.getNodePointer();
        assertNull(attributeNodePointer);
    }

    @Test
    public void testGetPosition_AttributesExist_ReturnsCorrectPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
        iterator.setPosition(2);
        int position = iterator.getPosition();
        assertEquals(2, position);
    }

    @Test
    public void testGetPosition_AttributesNotExist_ReturnsZero() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("nonexistent"));
        int position = iterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testSetPosition_ValidPositionExists_ReturnsTrue() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
        boolean result = iterator.setPosition(3);
        assertTrue(result);
    }

    @Test
    public void testSetPosition_InvalidPosition_ReturnsFalse() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
        boolean result = iterator.setPosition(7);
        assertFalse(result);
    }

    @Test
    public void testSetPosition_ValidPositionLessThanOne_ReturnsFalse() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
        boolean result = iterator.setPosition(0);
        assertFalse(result);
    }

    @Test
    public void testSetPosition_ValidPositionGreaterThanCount_ReturnsFalse() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
        boolean result = iterator.setPosition(5);
        assertFalse(result);
    }

    @Test
    public void testSetPosition_ValidPositionEqualToCount_ReturnsTrue() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
        boolean result = iterator.setPosition(4);
        assertTrue(result);
    }

    @Test
    public void testSetPosition_ValidPositionOne_ReturnsTrue() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
        boolean result = iterator.setPosition(1);
        assertTrue(result);
    }
}
