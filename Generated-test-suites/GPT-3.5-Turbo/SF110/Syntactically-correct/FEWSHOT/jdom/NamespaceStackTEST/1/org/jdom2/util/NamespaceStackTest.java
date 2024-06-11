package org.jdom2.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class NamespaceStackTest {

    private NamespaceStack stack;

    @Before
    public void setUp() {
        stack = new NamespaceStack();
    }

    @Test
    public void testConstructor() {
        NamespaceStack stack = new NamespaceStack();
        assertTrue(stack.getScope().length == 2);
        assertEquals(Namespace.NO_NAMESPACE, stack.getScope()[0]);
        assertEquals(Namespace.XML_NAMESPACE, stack.getScope()[1]);
    }

    @Test
    public void testConstructorWithSeed() {
        NamespaceStack stack = new NamespaceStack(new Namespace[]{Namespace.getNamespace("prefix", "uri")});
        assertTrue(stack.getScope().length == 1);
        assertEquals(Namespace.getNamespace("prefix", "uri"), stack.getScope()[0]);
    }

    @Test
    public void testPushWithElement() {
        Element element = new Element("test", Namespace.getNamespace("prefix", "uri"));
        stack.push(element);
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix", "uri")));
        assertFalse(stack.isInScope(Namespace.getNamespace("prefix2", "uri2")));
    }

    @Test
    public void testPushWithElementWithoutAdditionalNamespaces() {
        Element element = new Element("test", Namespace.getNamespace("prefix", "uri"));
        stack.push(element);
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix", "uri")));
        assertFalse(stack.isInScope(Namespace.getNamespace("prefix2", "uri2")));
    }

    @Test
    public void testPushWithElementWithAdditionalNamespaces() {
        Element element = new Element("test", Namespace.getNamespace("prefix", "uri"));
        Namespace additionalNamespace = Namespace.getNamespace("prefix2", "uri2");
        element.addNamespaceDeclaration(additionalNamespace);
        stack.push(element);
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix", "uri")));
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix2", "uri2")));
        assertFalse(stack.isInScope(Namespace.getNamespace("prefix3", "uri3")));
    }

    @Test
    public void testPushWithElementWithAttributes() {
        Element element = new Element("test", Namespace.getNamespace("prefix", "uri"));
        Namespace additionalNamespace = Namespace.getNamespace("prefix2", "uri2");
        Attribute attribute = new Attribute("attr", "value", additionalNamespace);
        element.setAttribute(attribute);
        stack.push(element);
        assertFalse(stack.isInScope(Namespace.getNamespace("prefix", "uri")));
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix2", "uri2")));
        assertFalse(stack.isInScope(Namespace.getNamespace("prefix3", "uri3")));
    }

    @Test
    public void testPushWithAttribute() {
        Attribute attribute = new Attribute("attr", "value", Namespace.getNamespace("prefix", "uri"));
        stack.push(attribute);
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix", "uri")));
        assertFalse(stack.isInScope(Namespace.getNamespace("prefix2", "uri2")));
    }

    @Test
    public void testPop() {
        stack.pop();
        assertEquals(1, stack.getScope().length);
        assertEquals(Namespace.NO_NAMESPACE, stack.getScope()[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWhenDepthIsZero() {
        stack.pop();
        stack.pop();
    }

    @Test
    public void testAddedForward() {
        Iterator<Namespace> iterator = stack.addedForward().iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAddedReverse() {
        Iterator<Namespace> iterator = stack.addedReverse().iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetScope() {
        Namespace[] scope = stack.getScope();
        assertTrue(scope.length == 2);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(Namespace.XML_NAMESPACE, scope[1]);
    }

    @Test
    public void testIsInScope() {
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        assertFalse(stack.isInScope(Namespace.getNamespace("prefix", "uri")));
    }

    
}