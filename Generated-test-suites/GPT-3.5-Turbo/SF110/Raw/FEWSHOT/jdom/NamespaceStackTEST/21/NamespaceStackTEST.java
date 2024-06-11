package org.jdom2.util;

import org.jdom2.Namespace;
import org.jdom2.Element;
import org.jdom2.Attribute;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class NamespaceStackTest {
    private Namespace[] seed;
    private NamespaceStack stack;

    @Before
    public void setup() {
        seed = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE };
        stack = new NamespaceStack(seed);
    }

    @Test
    public void testPushWithElement() {
        Element element = new Element("testElement");
        Namespace namespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "uri2");
        element.addNamespaceDeclaration(namespace1);
        element.addNamespaceDeclaration(namespace2);

        stack.push(element);

        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        assertTrue(stack.isInScope(namespace1));
        assertTrue(stack.isInScope(namespace2));
    }

    @Test
    public void testPushWithAttribute() {
        Attribute attribute1 = new Attribute("testAttribute1", "value1", Namespace.getNamespace("prefix1", "uri1"));
        Attribute attribute2 = new Attribute("testAttribute2", "value2", Namespace.getNamespace("prefix2", "uri2"));

        stack.push(attribute1);
        stack.push(attribute2);

        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        assertTrue(stack.isInScope(attribute1.getNamespace()));
        assertTrue(stack.isInScope(attribute2.getNamespace()));
    }

    @Test
    public void testPop() {
        stack.pop();

        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
    }

    @Test(expected = IllegalStateException.class)
    public void testOverPopStack() {
        stack.pop();
        stack.pop();
    }

    @Test
    public void testAddedForward() {
        Element element = new Element("testElement");
        Namespace namespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "uri2");
        element.addNamespaceDeclaration(namespace1);
        element.addNamespaceDeclaration(namespace2);
        stack.push(element);

        List<Namespace> namespaces = new ArrayList<>();
        for (Namespace ns : stack.addedForward()) {
            namespaces.add(ns);
        }

        assertEquals(2, namespaces.size());
        assertTrue(namespaces.contains(namespace1));
        assertTrue(namespaces.contains(namespace2));
    }

    @Test
    public void testAddedReverse() {
        Element element = new Element("testElement");
        Namespace namespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "uri2");
        element.addNamespaceDeclaration(namespace1);
        element.addNamespaceDeclaration(namespace2);
        stack.push(element);

        List<Namespace> namespaces = new ArrayList<>();
        for (Namespace ns : stack.addedReverse()) {
            namespaces.add(ns);
        }

        assertEquals(2, namespaces.size());
        assertTrue(namespaces.contains(namespace2));
        assertTrue(namespaces.contains(namespace1));
    }

    @Test
    public void testIterator() {
        Element element = new Element("testElement");
        Namespace namespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "uri2");
        element.addNamespaceDeclaration(namespace1);
        element.addNamespaceDeclaration(namespace2);
        stack.push(element);

        List<Namespace> namespaces = new ArrayList<>();
        for (Namespace ns : stack) {
            namespaces.add(ns);
        }

        assertEquals(4, namespaces.size());
        assertTrue(namespaces.contains(Namespace.NO_NAMESPACE));
        assertTrue(namespaces.contains(Namespace.XML_NAMESPACE));
        assertTrue(namespaces.contains(namespace1));
        assertTrue(namespaces.contains(namespace2));
    }

    @Test
    public void testGetScope() {
        Namespace[] scope = stack.getScope();

        assertEquals(2, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(Namespace.XML_NAMESPACE, scope[1]);
    }

    @Test
    public void testIsInScope() {
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
    }
}