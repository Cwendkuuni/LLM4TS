package org.jdom2.util;

import org.jdom2.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class NamespaceStackTest {

    @Test
    public void testNamespaceStack() {
        NamespaceStack stack = new NamespaceStack();
        assertNotNull(stack);
    }

    @Test
    public void testNamespaceStackWithSeed() {
        Namespace[] seed = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        NamespaceStack stack = new NamespaceStack(seed);
        assertNotNull(stack);
    }

    @Test(expected = IllegalStateException.class)
    public void testPopEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

    @Test
    public void testPushElement() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace = Namespace.getNamespace("prefix", "uri");
        Element element = new Element("test", namespace);
        stack.push(element);
        assertTrue(stack.isInScope(namespace));
        assertArrayEquals(new Namespace[]{namespace}, stack.getScope());
    }

    @Test
    public void testPushElementWithAdditionalNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "uri2");
        Namespace namespace3 = Namespace.getNamespace("prefix3", "uri3");
        Namespace namespace4 = Namespace.getNamespace("prefix4", "uri4");
        Element element = new Element("test");
        element.addNamespaceDeclaration(namespace1);
        element.addAdditionalNamespaces(Arrays.asList(namespace2, namespace3));
        element.setAttribute(new Attribute("attr", "value", namespace4));
        stack.push(element);
        assertTrue(stack.isInScope(namespace1));
        assertTrue(stack.isInScope(namespace2));
        assertTrue(stack.isInScope(namespace3));
        assertTrue(stack.isInScope(namespace4));
        assertArrayEquals(new Namespace[]{namespace1, namespace2, namespace3, namespace4}, stack.getScope());
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace = Namespace.getNamespace("prefix", "uri");
        Attribute attribute = new Attribute("test", "value", namespace);
        stack.push(attribute);
        assertTrue(stack.isInScope(namespace));
        assertArrayEquals(new Namespace[]{namespace}, stack.getScope());
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace = Namespace.getNamespace("prefix", "uri");
        Element element = new Element("test", namespace);
        stack.push(element);
        stack.pop();
        assertFalse(stack.isInScope(namespace));
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE}, stack.getScope());
    }

    @Test
    public void testAddedForwardWithEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        List<Namespace> addedNamespaces = Arrays.asList(stack.addedForward().iterator());
        assertTrue(addedNamespaces.isEmpty());
    }

    @Test
    public void testAddedForwardWithNonEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "uri2");
        Element element = new Element("test", namespace1);
        element.addNamespaceDeclaration(namespace2);
        stack.push(element);

        List<Namespace> addedNamespaces = Arrays.asList(stack.addedForward().iterator());
        assertEquals(2, addedNamespaces.size());
        assertTrue(addedNamespaces.contains(namespace1));
        assertTrue(addedNamespaces.contains(namespace2));
    }

    @Test
    public void testAddedReverseWithEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        List<Namespace> addedNamespaces = Arrays.asList(stack.addedReverse().iterator());
        assertTrue(addedNamespaces.isEmpty());
    }

    @Test
    public void testAddedReverseWithNonEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "uri2");
        Element element = new Element("test", namespace1);
        element.addNamespaceDeclaration(namespace2);
        stack.push(element);

        List<Namespace> addedNamespaces = Arrays.asList(stack.addedReverse().iterator());
        assertEquals(2, addedNamespaces.size());
        assertTrue(addedNamespaces.contains(namespace2));
        assertTrue(addedNamespaces.contains(namespace1));
    }

    @Test
    public void testIteratorWithEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        List<Namespace> namespaces = Arrays.asList(stack.iterator());
        assertEquals(0, namespaces.size());
    }

    @Test
    public void testIteratorWithNonEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "uri2");
        Element element = new Element("test", namespace1);
        element.addNamespaceDeclaration(namespace2);
        stack.push(element);

        List<Namespace> namespaces = Arrays.asList(stack.iterator());
        assertEquals(2, namespaces.size());
        assertTrue(namespaces.contains(namespace1));
        assertTrue(namespaces.contains(namespace2));
    }

    @Test
    public void testIsInScopeWithNamespaceInScope() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace = Namespace.getNamespace("prefix", "uri");
        Element element = new Element("test", namespace);
        stack.push(element);
        boolean inScope = stack.isInScope(namespace);
        assertTrue(inScope);
    }

    @Test
    public void testIsInScopeWithNamespaceNotInScope() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace = Namespace.getNamespace("prefix", "uri");
        boolean inScope = stack.isInScope(namespace);
        assertFalse(inScope);
    }
}