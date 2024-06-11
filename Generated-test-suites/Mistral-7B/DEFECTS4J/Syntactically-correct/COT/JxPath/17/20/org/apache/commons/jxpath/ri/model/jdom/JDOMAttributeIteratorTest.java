package org.apache.commons.jxpath.ri.model.jdom.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer;
import org.apache.commons.jxpath.ri.QName;

/**
 * Test class for JDOMAttributeIterator.
 */
public class JDOMAttributeIteratorTest {

    // Test data
    private static final String XML_NS = "http://www.w3.org/XML/1998/namespace";
    private static final String NS_PREFIX = "xml";

    private static final String PARENT_NAME = "parentElement";
    private static final String ATTRIBUTE_NAME_1 = "attr1";
    private static final String ATTRIBUTE_NAME_2 = "attr2";
    private static final String ATTRIBUTE_NAME_3 = "attr3";

    private static final String VALUE_1 = "value1";
    private static final String VALUE_2 = "value2";
    private static final String VALUE_3 = "value3";

    private static final String XPATH_EXPRESSION = "*/@*[local-name()='attr1']";

    private Element parentElement;

    @org.junit.Before
    public void setUp() {
        parentElement = new Element(PARENT_NAME);
        parentElement.addAttribute(new Attribute(NS_PREFIX, ATTRIBUTE_NAME_1, VALUE_1));
        parentElement.addAttribute(new Attribute(NS_PREFIX, ATTRIBUTE_NAME_2, VALUE_2));
        parentElement.addAttribute(new Attribute(NS_PREFIX, ATTRIBUTE_NAME_3, VALUE_3));
    }

    @org.junit.Test
    public void testConstructorWithWildcardQName() {
        // Test case
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parentElement), new QName("*"));

        // Verifications
        assertSame(parentElement, iterator.parent.getNode());
        assertEquals(3, iterator.attributes.size());
        assertTrue(iterator.hasNext());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullParent() {
        // Test case
        new JDOMAttributeIterator(null, new QName(PARENT_NAME));

        // Expected exception
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullQName() {
        // Test case
        new JDOMAttributeIterator(new JDOMNodePointer(parentElement), null);

        // Expected exception
    }

    @org.junit.Test
    public void testConstructorWithSpecificQName() {
        // Test case
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parentElement), new QName(NS_PREFIX, ATTRIBUTE_NAME_1));

        // Verifications
        assertSame(parentElement, iterator.parent.getNode());
        assertEquals(1, iterator.attributes.size());
        assertTrue(iterator.hasNext());
    }

    @org.junit.Test
    public void testConstructorWithPrefixAndSpecificQName() {
        // Test case
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parentElement), new QName(NS_PREFIX, ATTRIBUTE_NAME_2));

        // Verifications
        assertSame(parentElement, iterator.parent.getNode());
        assertEquals(1, iterator.attributes.size());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testConstructorWithEmptyListOfAttributes() {
        // Test case
        Element emptyElement = new Element(PARENT_NAME);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(emptyElement), new QName(NS_PREFIX, ATTRIBUTE_NAME_1));

        // Expected exception
    }

    @org.junit.Test
    public void testHasNext() {
        // Test case
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parentElement), new QName(NS_PREFIX, ATTRIBUTE_NAME_1));

        // Verifications
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasNext());
    }

    @org.junit.Test
    public void testNext() {
        // Test case
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parentElement), new QName(NS_PREFIX, ATTRIBUTE_NAME_1));
        NodePointer nextNodePointer = iterator.next();

        // Verifications
        assertSame(parentElement, nextNodePointer.getParent().getNode());
        assertSame(parentElement.getAttribute(new QName(NS_PREFIX, ATTRIBUTE_NAME_1)), nextNodePointer.getNode());
    }

    @org.junit.Test
    public void testGetPosition() {
        // Test case
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parentElement), new QName(NS_PREFIX, ATTRIBUTE_NAME_1));
        iterator.next();

        // Verifications
        assertEquals(1, iterator.getPosition());
    }

    @org.junit.Test
    public void testSetPosition() {
        // Test case
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parentElement), new QName(NS_PREFIX, ATTRIBUTE_NAME_1));

        // Verifications
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertFalse(iterator.setPosition(4));
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithNegativeIndex() {
        // Test case
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parentElement), new QName(NS_PREFIX, ATTRIBUTE_NAME_1));

        // Expected exception
        iterator.setPosition(-1);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithIndexGreaterThanSize() {
        // Test case
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parentElement), new QName(NS_PREFIX, ATTRIBUTE_NAME_1));

        // Expected exception
        iterator.setPosition(5);
    }

    @org.junit.Test
    public void testSetPositionWithValidIndex() {
        // Test case
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parentElement), new QName(NS_PREFIX, ATTRIBUTE_NAME_1));

        // Verifications
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertSame(parentElement.getAttribute(new QName(NS_PREFIX, ATTRIBUTE_NAME_2)), iterator.next().getNode());
    }

    @org.junit.Test
    public void testGetNodePointer() {
        // Test case
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parentElement), new QName(NS_PREFIX, ATTRIBUTE_NAME_1));

        // Verifications
        NodePointer nodePointer = iterator.next();
        assertSame(parentElement, nodePointer.getParent().getNode());
        assertSame(parentElement.getAttribute(new QName(NS_PREFIX, ATTRIBUTE_NAME_1)), nodePointer.getNode());
    }

    @org.junit.Test(expected = NoSuchElementException.class)
    public void testGetNodePointerWithEmptyIterator() {
        // Test case
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parentElement), new QName(NS_PREFIX, ATTRIBUTE_NAME_4));

        // Expected exception
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.getNodePointer();
    }

    @org.junit.Test
    public void testGetNodePointerWithValidIterator() {
        // Test case
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(new JDOMNodePointer(parentElement), new QName(NS_PREFIX, ATTRIBUTE_NAME_1));

        // Verifications
        NodePointer nodePointer = iterator.next();
        assertSame(parentElement, nodePointer.getParent().getNode());
        assertSame(parentElement.getAttribute(new QName(NS_PREFIX, ATTRIBUTE_NAME_1)), nodePointer.getNode());
        nodePointer = iterator.next();
        assertSame(parentElement, nodePointer.getParent().getNode());
        assertSame(parentElement.getAttribute(new QName(NS_PREFIX, ATTRIBUTE_NAME_2)), nodePointer.getNode());
        nodePointer = iterator.next();
        assertSame(parentElement, nodePointer.getParent().getNode());
        assertSame(parentElement.getAttribute(new QName(NS_PREFIX, ATTRIBUTE_NAME_3)), nodePointer.getNode());
        assertNull(iterator.next().getNode());
        nodePointer = iterator.getNodePointer();
        assertSame(parentElement.getAttribute(new QName(NS_PREFIX, ATTRIBUTE_NAME_3)), nodePointer.getNode());
    }
}