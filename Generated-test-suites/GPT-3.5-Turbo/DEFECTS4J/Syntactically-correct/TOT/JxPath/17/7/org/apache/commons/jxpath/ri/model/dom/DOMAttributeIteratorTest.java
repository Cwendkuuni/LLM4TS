import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DOMAttributeIteratorTest {

    @Test
    public void testGetNodePointer_ElementNodeAndNonWildcardName_ValidNodePointerReturned() {
        // Arrange
        NodePointer parent = mock(NodePointer.class);
        QName name = new QName("attribute1");

        // Act
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();

        // Assert
        assertNotNull(nodePointer);
    }

    @Test
    public void testGetNodePointer_ElementNodeAndNonWildcardName_AttributePointerReturnedWithCorrectAttribute() {
        // Arrange
        NodePointer parent = mock(NodePointer.class);
        QName name = new QName("attribute2");

        // Act
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();

        // Assert
        assertNotNull(attributePointer);
        assertEquals("attribute2", attributePointer.getAttribute().getName());
    }

    @Test
    public void testGetNodePointer_ElementNodeAndWildcardName_AttributePointerReturnedWithFirstMatchingAttribute() {
        // Arrange
        NodePointer parent = mock(NodePointer.class);
        QName name = new QName("*");

        // Act
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();

        // Assert
        assertNotNull(attributePointer);
        assertEquals("attribute1", attributePointer.getAttribute().getName());
    }

    @Test
    public void testGetNodePointer_ElementNodeAndPrefixWildcardName_AttributePointerReturnedWithFirstMatchingAttribute() {
        // Arrange
        NodePointer parent = mock(NodePointer.class);
        QName name = new QName("prefix:*");

        // Act
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        DOMAttributePointer attributePointer = (DOMAttributePointer) iterator.getNodePointer();

        // Assert
        assertNotNull(attributePointer);
        assertEquals("prefix:attribute1", attributePointer.getAttribute().getName());
    }

    @Test
    public void testGetPosition_Initially_ReturnsZero() {
        // Arrange
        NodePointer parent = mock(NodePointer.class);
        QName name = new QName("attribute1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        // Act
        int position = iterator.getPosition();

        // Assert
        assertEquals(0, position);
    }

    @Test
    public void testSetPosition_InvalidPosition_ReturnsFalse() {
        // Arrange
        NodePointer parent = mock(NodePointer.class);
        QName name = new QName("attribute1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        // Act
        boolean result = iterator.setPosition(-1);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testSetPosition_ValidPosition_ReturnsTrue() {
        // Arrange
        NodePointer parent = mock(NodePointer.class);
        QName name = new QName("attribute1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        // Act
        boolean result = iterator.setPosition(1);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testSetPosition_ValidPosition_PositionUpdated() {
        // Arrange
        NodePointer parent = mock(NodePointer.class);
        QName name = new QName("attribute1");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        // Act
        iterator.setPosition(1);
        int position = iterator.getPosition();

        // Assert
        assertEquals(1, position);
    }
}