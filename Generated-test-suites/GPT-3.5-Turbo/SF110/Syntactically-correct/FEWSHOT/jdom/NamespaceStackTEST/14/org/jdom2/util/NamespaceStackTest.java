package org.jdom2.util;

import org.jdom2.internal.*;
import org.jdom2.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testConstructor() {
        NamespaceStack stack = new NamespaceStack();
        assertNotNull(stack);
    }

    @Test
    public void testEmptyScope() {
        NamespaceStack stack = new NamespaceStack();
        Namespace[] scope = stack.getScope();
        assertEquals(2, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(Namespace.XML_NAMESPACE, scope[1]);
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        assertFalse(stack.isInScope(Namespace.getNamespace("prefix", "uri")));
    }

    @Test(expected = IllegalStateException.class)
    public void testPopEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

    @Test
    public void testPushElement() {
        NamespaceStack stack = new NamespaceStack();

        Element element = new Element("test", "namespace");
        stack.push(element);

        Namespace[] scope = stack.getScope();
        assertEquals(2, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(element.getNamespace(), scope[1]);
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack stack = new NamespaceStack();

        Element element = new Element("test", "namespace");
        Attribute attribute = new Attribute("attr", "value", Namespace.getNamespace("prefix", "uri"));
        element.setAttribute(attribute);
        stack.push(attribute);

        Namespace[] scope = stack.getScope();
        assertEquals(2, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(attribute.getNamespace(), scope[1]);
    }

    @Test
    public void testPushMultipleNamespaces() {
        NamespaceStack stack = new NamespaceStack();

        Element element = new Element("test", "namespace");
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix1", "uri1"));
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix2", "uri2"));
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix3", "uri3"));
        stack.push(element);

        Namespace[] scope = stack.getScope();
        assertEquals(2, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(element.getNamespace(), scope[1]);
    }

    @Test
    public void testIsInScopeWithElement() {
        NamespaceStack stack = new NamespaceStack();

        Element element = new Element("test", "namespace");
        stack.push(element);

        assertTrue(stack.isInScope(element.getNamespace()));
    }

    @Test
    public void testIsInScopeWithAttribute() {
        NamespaceStack stack = new NamespaceStack();

        Element element = new Element("test", "namespace");
        Attribute attribute = new Attribute("attr", "value", Namespace.getNamespace("prefix", "uri"));
        element.setAttribute(attribute);
        stack.push(element);

        assertTrue(stack.isInScope(attribute.getNamespace()));
    }

    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();

        Element element = new Element("test", "namespace");
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix1", "uri1"));
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix2", "uri2"));
        stack.push(element);

        Iterator<Namespace> iterator = stack.addedForward().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.getNamespace("prefix1", "uri1"), iterator.next());
        assertEquals(Namespace.getNamespace("prefix2", "uri2"), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();

        Element element = new Element("test", "namespace");
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix1", "uri1"));
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix2", "uri2"));
        stack.push(element);

        Iterator<Namespace> iterator = stack.addedReverse().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.getNamespace("prefix2", "uri2"), iterator.next());
        assertEquals(Namespace.getNamespace("prefix1", "uri1"), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();

        Element element = new Element("test", "namespace");
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix1", "uri1"));
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix2", "uri2"));
        stack.push(element);

        Iterator<Namespace> iterator = stack.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());
        assertEquals(element.getNamespace(), iterator.next());
        assertEquals(Namespace.getNamespace("prefix1", "uri1"), iterator.next());
        assertEquals(Namespace.getNamespace("prefix2", "uri2"), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();

        Element element = new Element("test", "namespace");
        stack.push(element);
        stack.pop();

        Namespace[] scope = stack.getScope();
        assertEquals(2, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(Namespace.XML_NAMESPACE, scope[1]);
    }
}