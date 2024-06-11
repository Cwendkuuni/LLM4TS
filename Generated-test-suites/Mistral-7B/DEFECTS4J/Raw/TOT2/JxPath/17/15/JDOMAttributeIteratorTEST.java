package org.junit.jupiter.api.impl.testing.serialized.opentest4j.junit4.junit4_11.junit4_11_1;

import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_WhenNamespaceIsSpecified_ThenCorrectlyIteratesOverAttributesWithThatNamespace() {
        // Given
        Element root = new Element("root", "http://test.com");
        root.addAttribute("test1", "value1", "http://test.com");
        NodePointer parent = new NodePointer(root);
        QName attributeName = new QName("test1", "http://test.com");

        // When
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);

        // Then
        Attribute expectedAttribute = root.getAttribute("test1", "http://test.com");
        assertSame(expectedAttribute, iterator.getNodePointer().getNode());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());

        iterator.next();
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_WhenNamespaceIsNotSpecified_ThenCorrectlyIteratesOverAllAttributes() {
        // Given
        Element root = new Element("root");
        root.addAttribute("xml:test1", "value1");
        root.addAttribute("test2", "value2");
        NodePointer parent = new NodePointer(root);
        QName attributeName = new QName("*");

        // When
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);

        // Then
        Attribute expectedAttribute1 = root.getAttribute("xml:test1");
        assertSame(expectedAttribute1, iterator.getNodePointer().getNode());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());

        iterator.next();
        Attribute expectedAttribute2 = root.getAttribute("test2");
        assertSame(expectedAttribute2, iterator.getNodePointer().getNode());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());

        iterator.next();
        assertFalse(iterator.hasPrevious());
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_WhenParentNodeIsNotAnElement_ThenThrowsIllegalArgumentException() {
        // Given
        NodePointer parent = new NodePointer(new Element("root"));
        QName attributeName = new QName("test1", "http://test.com");

        // When / Then
        new JDOMAttributeIterator(parent, attributeName);
    }

    @Test
    public void testJDOMAttributeIterator_WhenAttributeIsNotFound_ThenReturnsNull() {
        // Given
        Element root = new Element("root");
        NodePointer parent = new NodePointer(root);
        QName attributeName = new QName("test1", "http://test.com");

        // When
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);

        // Then
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_WhenPositionIsInvalid_ThenThrowsIndexOutOfBoundsException() {
        // Given
        Element root = new Element("root");
        root.addAttribute("test1", "value1");
        NodePointer parent = new NodePointer(root);
        QName attributeName = new QName("test1", "http://test.com");

        // When / Then
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);
        iterator.next();

        try {
            iterator.setPosition(-1);
        } catch (IndexOutOfBoundsException e) {
            // Expected
            assertEquals("Position out of bounds: -1", e.getMessage());
        }
    }

    @Test
    public void testJDOMAttributeIterator_WhenNextIsCalledMultipleTimes_ThenIteratesOverAllAttributes() {
        // Given
        Element root = new Element("root");
        root.addAttribute("xml:test1", "value1");
        root.addAttribute("test2", "value2");
        NodePointer parent = new NodePointer(root);
        QName attributeName = new QName("*");

        // When
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, attributeName);

        // Then
        Attribute expectedAttribute1 = root.getAttribute("xml:test1");
        assertSame(expectedAttribute1, iterator.getNodePointer().getNode());
        assertTrue(iterator.hasNext());

        Attribute expectedAttribute2 = root.getAttribute("test2");
        assertSame(expectedAttribute2, iterator.getNodePointer().getNode());
        assertTrue(iterator.hasNext());

        assertFalse(iterator.hasPrevious());
        assertNull(iterator.getNodePointer());
    }

    // Add test cases for edge cases and error scenarios here
}