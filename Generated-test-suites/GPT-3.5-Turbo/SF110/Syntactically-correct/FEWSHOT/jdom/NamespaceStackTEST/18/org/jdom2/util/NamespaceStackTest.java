package org.jdom2.util;

import org.jdom2.Namespace;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class NamespaceStackTest {
    private NamespaceStack namespaceStack;

    @Before
    public void setup() {
        namespaceStack = new NamespaceStack();
    }

    @Test
    public void testConstructor() {
        Namespace[] seed = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        NamespaceStack namespaceStack = new NamespaceStack(seed);
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expected, namespaceStack.getScope());
    }

    @Test
    public void testPushWithElement() {
        Element element = new Element("test");
        Namespace namespace = Namespace.getNamespace("test", "www.test.com");
        element.setNamespace(namespace);

        List<Namespace> addedNamespaces = new ArrayList<>();
        addedNamespaces.add(namespace);

        namespaceStack.push(element);

        assertEquals(namespace, namespaceStack.getScope()[0]);
        assertArrayEquals(addedNamespaces.toArray(), namespaceStack.addedForward().toArray());
        assertArrayEquals(addedNamespaces.toArray(), namespaceStack.addedReverse().toArray());
        assertTrue(namespaceStack.isInScope(namespace));
    }

    @Test
    public void testPushWithAttribute() {
        Element element = new Element("test");
        Attribute attribute = new Attribute("attr", "value", Namespace.getNamespace("attr", "www.attr.com"));
        element.setAttribute(attribute);

        List<Namespace> addedNamespaces = new ArrayList<>();
        addedNamespaces.add(attribute.getNamespace());

        namespaceStack.push(attribute);

        assertEquals(attribute.getNamespace(), namespaceStack.getScope()[0]);
        assertArrayEquals(addedNamespaces.toArray(), namespaceStack.addedForward().toArray());
        assertArrayEquals(addedNamespaces.toArray(), namespaceStack.addedReverse().toArray());
        assertTrue(namespaceStack.isInScope(attribute.getNamespace()));
    }

    @Test(expected = IllegalStateException.class)
    public void testPop() {
        namespaceStack.pop();
        assertNull(namespaceStack.getScope());
    }

    @Test
    public void testIterator() {
        Namespace namespace1 = Namespace.getNamespace("test1", "www.test1.com");
        Namespace namespace2 = Namespace.getNamespace("test2", "www.test2.com");
        Namespace[] seed = {namespace1, namespace2};

        NamespaceStack namespaceStack = new NamespaceStack(seed);

        Iterator<Namespace> iterator = namespaceStack.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(namespace1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(namespace2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIsInScope() {
        Namespace namespace1 = Namespace.getNamespace("test1", "www.test1.com");
        Namespace namespace2 = Namespace.getNamespace("test2", "www.test2.com");
        Namespace[] seed = {namespace1, namespace2};

        NamespaceStack namespaceStack = new NamespaceStack(seed);

        assertTrue(namespaceStack.isInScope(namespace1));
        assertTrue(namespaceStack.isInScope(namespace2));
    }
}