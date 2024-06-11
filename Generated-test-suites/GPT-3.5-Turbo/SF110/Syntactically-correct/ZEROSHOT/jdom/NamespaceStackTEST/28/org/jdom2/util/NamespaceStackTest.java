package org.jdom2.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.Before;
import org.junit.Test;

public class NamespaceStackTest {

    private NamespaceStack stack;

    @Before
    public void setUp() {
        stack = new NamespaceStack();
    }

    @Test
    public void testPush_Element() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        stack.push(element);
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
    }

    @Test
    public void testPush_Element_AdditionalNamespaces() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        element.addNamespaceDeclaration(Namespace.XML_NAMESPACE);
        stack.push(element);
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
    }

    @Test
    public void testPush_Element_Attributes() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        element.setAttribute(new Attribute("attr", "value", Namespace.NO_NAMESPACE));
        stack.push(element);
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
    }

    @Test
    public void testPush_Attribute() {
        Attribute attribute = new Attribute("attr", "value", Namespace.NO_NAMESPACE);
        stack.push(attribute);
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
    }

    @Test
    public void testPop() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        stack.push(element);
        stack.pop();
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
    }

    @Test(expected = IllegalStateException.class)
    public void testPop_EmptyStack() {
        stack.pop();
    }

    @Test
    public void testAddedForward_EmptyStack() {
        Iterable<Namespace> iterable = stack.addedForward();
        assertFalse(iterable.iterator().hasNext());
    }

    @Test
    public void testAddedForward_WithNamespaces() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        element.setAttribute(new Attribute("attr", "value", new Namespace("prefix", "uri")));
        stack.push(element);
        Iterable<Namespace> iterable = stack.addedForward();
        assertTrue(iterable.iterator().hasNext());
    }

    @Test
    public void testAddedReverse_EmptyStack() {
        Iterable<Namespace> iterable = stack.addedReverse();
        assertFalse(iterable.iterator().hasNext());
    }

    @Test
    public void testAddedReverse_WithNamespaces() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        element.setAttribute(new Attribute("attr", "value", new Namespace("prefix", "uri")));
        stack.push(element);
        Iterable<Namespace> iterable = stack.addedReverse();
        assertTrue(iterable.iterator().hasNext());
    }

    @Test
    public void testIterator_EmptyStack() {
        Iterable<Namespace> iterable = stack;
        assertFalse(iterable.iterator().hasNext());
    }

    @Test
    public void testIterator_WithNamespaces() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        element.setAttribute(new Attribute("attr", "value", new Namespace("prefix", "uri")));
        stack.push(element);
        Iterable<Namespace> iterable = stack;
        assertTrue(iterable.iterator().hasNext());
    }

    @Test
    public void testGetScope() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        stack.push(element);
        Namespace[] scope = stack.getScope();
        assertNotNull(scope);
        assertEquals(1, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
    }

    @Test
    public void testIsInScope() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        stack.push(element);
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
    }

    @Test
    public void testBinarySearch() {
        Namespace[] data = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE };
        int result1 = NamespaceStack.binarySearch(data, 0, data.length, Namespace.NO_NAMESPACE);
        assertEquals(0, result1);
        int result2 = NamespaceStack.binarySearch(data, 0, data.length, Namespace.XML_NAMESPACE);
        assertEquals(1, result2);
        int result3 = NamespaceStack.binarySearch(data, 0, data.length, new Namespace("test", "uri"));
        assertEquals(-2, result3);
    }
}