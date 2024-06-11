package org.jdom2.util;

import org.junit.Test;
import org.jdom2.*;
import org.jdom2.internal.NamespaceStack;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.*;

public class NamespaceStackTest {

    @Test
    public void testEmptyConstructor() {
        NamespaceStack stack = new NamespaceStack();
        assertNotNull(stack);
    }

    @Test
    public void testSeedConstructor() {
        Namespace[] seed = new Namespace[] {
                Namespace.getNamespace("test", "http://www.example.com")
        };
        NamespaceStack stack = new NamespaceStack(seed);
        assertNotNull(stack);
    }

    @Test
    public void testPushElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", "http://www.example.com");
        stack.push(element);
        Namespace[] scope = stack.getScope();
        assertEquals(1, scope.length);
        assertEquals("http://www.example.com", scope[0].getURI());
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("test", "http://www.example.com");
        stack.push(attribute);
        Namespace[] scope = stack.getScope();
        assertEquals(1, scope.length);
        assertEquals("http://www.example.com", scope[0].getURI());
    }

    @Test(expected = IllegalStateException.class)
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", "http://www.example.com");
        stack.push(element);
        Iterable<Namespace> added = stack.addedForward();
        Iterator<Namespace> iterator = added.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("http://www.example.com", iterator.next().getURI());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", "http://www.example.com");
        stack.push(element);
        Iterable<Namespace> added = stack.addedReverse();
        Iterator<Namespace> iterator = added.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("http://www.example.com", iterator.next().getURI());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", "http://www.example.com");
        stack.push(element);
        Iterator<Namespace> iterator = stack.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("http://www.example.com", iterator.next().getURI());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", "http://www.example.com");
        stack.push(element);
        assertTrue(stack.isInScope(Namespace.getNamespace("test", "http://www.example.com")));
        assertFalse(stack.isInScope(Namespace.getNamespace("test", "http://www.test.com")));
    }
}