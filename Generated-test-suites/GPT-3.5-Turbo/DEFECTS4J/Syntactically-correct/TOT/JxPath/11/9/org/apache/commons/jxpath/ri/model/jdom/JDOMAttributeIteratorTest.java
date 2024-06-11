import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    @Test
    public void testSingleNodePointer() {
        // Create a single element with a single attribute
        Element element = new Element("element");
        Attribute attribute = new Attribute("attr", "value");
        element.setAttribute(attribute);

        // Create a node pointer for the element
        NodePointer pointer = new JDOMNodePointer(element);

        // Create a JDOMAttributeIterator with the node pointer
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(pointer, new QName("attr"));

        // Check that the position is initially 1
        assertEquals(1, iterator.getPosition());

        // Get the first node pointer
        NodePointer nodePointer = iterator.getNodePointer();

        // Check that the node pointer is not null and its attribute is the one we created
        assertNotNull(nodePointer);
        assertEquals(attribute, ((JDOMAttributePointer) nodePointer).getAttribute());

        // Check that the position has been updated to 0
        assertEquals(0, iterator.getPosition());

        // Move to the next position
        assertTrue(iterator.setPosition(1));

        // Check that the position is now 1
        assertEquals(1, iterator.getPosition());

        // Get the second node pointer
        nodePointer = iterator.getNodePointer();

        // Check that the node pointer is null, as there are no more attributes
        assertNull(nodePointer);

        // Check that the position has been updated to 2
        assertEquals(2, iterator.getPosition());

        // Try to set an invalid position
        assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testMultipleNodePointers() {
        // Create an element with multiple attributes
        Element element = new Element("element");
        Attribute attribute1 = new Attribute("attr1", "value1");
        Attribute attribute2 = new Attribute("attr2", "value2");
        Attribute attribute3 = new Attribute("attr3", "value3");
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);
        element.setAttribute(attribute3);

        // Create a node pointer for the element
        NodePointer pointer = new JDOMNodePointer(element);

        // Create a JDOMAttributeIterator with the node pointer
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(pointer, new QName("*"));

        // Get the first node pointer
        NodePointer nodePointer = iterator.getNodePointer();

        // Check that the node pointer is not null and its attribute is attribute1
        assertNotNull(nodePointer);
        assertEquals(attribute1, ((JDOMAttributePointer) nodePointer).getAttribute());

        // Move to the next position
        assertTrue(iterator.setPosition(2));

        // Get the second node pointer
        nodePointer = iterator.getNodePointer();

        // Check that the node pointer is not null and its attribute is attribute2
        assertNotNull(nodePointer);
        assertEquals(attribute2, ((JDOMAttributePointer) nodePointer).getAttribute());

        // Move to the next position
        assertTrue(iterator.setPosition(3));

        // Get the third node pointer
        nodePointer = iterator.getNodePointer();

        // Check that the node pointer is not null and its attribute is attribute3
        assertNotNull(nodePointer);
        assertEquals(attribute3, ((JDOMAttributePointer) nodePointer).getAttribute());

        // Move to an invalid position
        assertFalse(iterator.setPosition(4));

        // Check that the position is still 3
        assertEquals(3, iterator.getPosition());

        // Try to get the next node pointer, which should be null
        nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }

    @Test
    public void testNoAttributes() {
        // Create an element with no attributes
        Element element = new Element("element");

        // Create a node pointer for the element
        NodePointer pointer = new JDOMNodePointer(element);

        // Create a JDOMAttributeIterator with the node pointer
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(pointer, new QName("attr"));

        // Get the node pointer, which should be null
        NodePointer nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);

        // Check that the position is 0
        assertEquals(0, iterator.getPosition());

        // Try to set an invalid position
        assertFalse(iterator.setPosition(1));
    }

}