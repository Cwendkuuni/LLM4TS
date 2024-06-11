package org.jdom2.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.Namespace;
import org.jdom2.Attribute;
import org.jdom2.Element;
import java.util.ArrayList;
import java.util.List;

public class NamespaceStackTest {

    @Test
    public void testConstructor() {
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        NamespaceStack stack = new NamespaceStack();
        assertArrayEquals(expected, stack.getScope());

        Namespace[] seed = {Namespace.getNamespace("prefix1", "uri1"), Namespace.getNamespace("prefix2", "uri2")};
        stack = new NamespaceStack(seed);
        assertArrayEquals(seed, stack.getScope());
    }

    @Test
    public void testPush_Element() {
        NamespaceStack stack = new NamespaceStack();

        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("prefix", "uri"));
        stack.push(element);
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix", "uri")));

        element = new Element("test");
        element.setNamespace(Namespace.NO_NAMESPACE);
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix", "uri"));
        stack.push(element);
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix", "uri")));

        element = new Element("test");
        element.setNamespace(Namespace.NO_NAMESPACE);
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix", "uri1"));
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix", "uri2"));
        stack.push(element);
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix", "uri1")));
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix", "uri2")));
    }

    @Test
    public void testPush_Attribute() {
        NamespaceStack stack = new NamespaceStack();

        Attribute attribute = new Attribute("test", "value", Namespace.getNamespace("prefix", "uri"));
        stack.push(attribute);
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix", "uri")));

        attribute = new Attribute("test", "value", Namespace.NO_NAMESPACE);
        attribute.setNamespace(Namespace.getNamespace("prefix", "uri"));
        stack.push(attribute);
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix", "uri")));
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("test"));
        stack.pop();
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
    }

    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("test"));
        Iterable<Namespace> iterable = stack.addedForward();
        assertNotNull(iterable);
        assertTrue(iterable.iterator().hasNext());
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("test"));
        Iterable<Namespace> iterable = stack.addedReverse();
        assertNotNull(iterable);
        assertTrue(iterable.iterator().hasNext());
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Iterator<Namespace> iterator = stack.iterator();
        assertNotNull(iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetScope() {
        NamespaceStack stack = new NamespaceStack();
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expected, stack.getScope());
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
    }

}