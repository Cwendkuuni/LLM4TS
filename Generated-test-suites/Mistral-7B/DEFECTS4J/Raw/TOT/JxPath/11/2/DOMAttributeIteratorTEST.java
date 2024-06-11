package org.apache.commons.jxpath.ri.model.dom;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class DOMAttributeIteratorTest {

    private Element testElement;
    private Node testParent;

    @Before
    public void setUp() {
        testElement = new Element.Builder("testElement").namespaceURI("http://test.com").build();
        testParent = new Element.Builder("parent").namespaceURI("http://parent.com").build();
        testParent.appendChild(testElement);
        testElement.setAttribute("testAttr1", "value1");
        testElement.setAttributeNS("xmlns:x", "x:testAttr2", "value2");
        testElement.setAttribute("testAttr3", "value3");
    }

    @Test
    public void testDOMAttributeIterator_WhenNewIteratorIsCreated_ShouldReturnFirstAttribute() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(testParent, testElement), new QName("testAttr1"));
        Attr expected = testElement.getAttributeNode("testAttr1");
        Attr actual = iterator.getNodePointer().getNode();
        assertTrue(equalNodes(expected, actual));
    }

    @Test
    public void testDOMAttributeIterator_WhenIteratorIsMovedToNextAttribute_ShouldReturnNextAttribute() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(testParent, testElement), new QName("testAttr1"));
        iterator.next();
        Attr expected = testElement.getAttributeNode("testAttr2");
        Attr actual = iterator.getNodePointer().getNode();
        assertTrue(equalNodes(expected, actual));
    }

    @Test
    public void testDOMAttributeIterator_WhenIteratorIsMovedPastLastAttribute_ShouldReturnNull() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(testParent, testElement), new QName("testAttr1"));
        while (iterator.hasNext()) {
            iterator.next();
        }
        assertNull(iterator.getNodePointer().getNode());
    }

    @Test
    public void testDOMAttributeIterator_WhenIteratorIsCreatedWithWildcard_ShouldReturnAllAttributes() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(testParent, testElement), new QName("*"));
        int count = 0;
        while (iterator.hasNext()) {
            Attr attribute = iterator.getNodePointer().getNode();
            assertTrue(testElement.hasAttributeNode(attribute.getNodeName()));
            iterator.next();
            count++;
        }
        assertTrue(count == 3);
    }

    @Test
    public void testDOMAttributeIterator_WhenIteratorIsCreatedWithNamespace_ShouldReturnAttributeWithMatchingNamespace() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(testParent, testElement), new QName("x:testAttr2"));
        Attr expected = testElement.getAttributeNodeNS("http://test.com", "x:testAttr2");
        Attr actual = iterator.getNodePointer().getNode();
        assertTrue(equalNodes(expected, actual));
    }

    @Test
    public void testDOMAttributeIterator_WhenIteratorIsCreatedWithNonMatchingNamespace_ShouldReturnNull() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(testParent, testElement), new QName("y:testAttr2"));
        assertNull(iterator.getNodePointer().getNode());
    }

    @Test
    public void testDOMAttributeIterator_WhenIteratorIsCreatedWithNullQName_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DOMAttributeIterator(new DOMPoint(testParent, testElement), null));
    }

    private boolean equalNodes(Node expected, Node actual) {
        if (expected == null && actual == null) {
            return true;
        }
        if (expected == null || actual == null) {
            return false;
        }
        if (expected.getNodeType() != actual.getNodeType()) {
            return false;
        }
        if (expected instanceof Attr && actual instanceof Attr) {
            return expected.getNodeName().equals(actual.getNodeName())
                    && equalStrings(expected.getNodeValue(), actual.getNodeValue())
                    && equalStrings(expected.getNamespaceURI(), actual.getNamespaceURI())
                    && equalStrings(expected.getPrefix(), actual.getPrefix());
        }
        return false;
    }

    private static String equalStrings(String s1, String s2) {
        return s1 == s2 || (s1 != null && s1.equals(s2));
    }
}