package org.jdom2.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.*;
import org.jdom2.Attribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.jdom2.internal.*;

public class NamespaceStackTest {
    NamespaceStack nsStack;

    @Test
    public void testNamespaceStackConstructor() {
        nsStack = new NamespaceStack();
        Namespace[] expectedScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }

    @Test
    public void testNamespaceStackConstructorWithSeed() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        Namespace[] seed = { ns1, ns2 };
        nsStack = new NamespaceStack(seed);
        Namespace[] expectedScope = { ns1, ns2 };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }

    @Test
    public void testPushWithElement() {
        nsStack = new NamespaceStack();
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        nsStack.push(element);
        Namespace[] expectedScope = { Namespace.NO_NAMESPACE };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }

    @Test
    public void testPushWithElementWithAdditionalNamespaces() {
        nsStack = new NamespaceStack();
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        List<Namespace> additionalNamespaces = new ArrayList<Namespace>();
        additionalNamespaces.add(ns1);
        additionalNamespaces.add(ns2);
        element.setAdditionalNamespaces(additionalNamespaces);
        nsStack.push(element);
        Namespace[] expectedScope = { ns1, ns2, Namespace.NO_NAMESPACE };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }

    @Test
    public void testPushWithElementWithAttributes() {
        nsStack = new NamespaceStack();
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        Attribute att1 = new Attribute("attribute1", "value1", Namespace.NO_NAMESPACE);
        Attribute att2 = new Attribute("attribute2", "value2", Namespace.XML_NAMESPACE);
        Attribute att3 = new Attribute("attribute3", "value3", Namespace.getNamespace("prefix3", "uri3"));
        Attribute att4 = new Attribute("attribute4", "value4", Namespace.getNamespace("prefix4", "uri4"));
        element.setAttribute(att1);
        element.setAttribute(att2);
        element.setAttribute(att3);
        element.setAttribute(att4);
        nsStack.push(element);
        Namespace[] expectedScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.getNamespace("prefix3", "uri3"), Namespace.getNamespace("prefix4", "uri4") };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }

    @Test
    public void testPushWithAttribute() {
        nsStack = new NamespaceStack();
        Attribute att = new Attribute("test", "value", Namespace.NO_NAMESPACE);
        nsStack.push(att);
        Namespace[] expectedScope = { Namespace.NO_NAMESPACE };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }

    @Test
    public void testPop() {
        nsStack = new NamespaceStack();
        nsStack.pop();
        Namespace[] expectedScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }

    @Test
    public void testAddedForward() {
        nsStack = new NamespaceStack();
        Iterable<Namespace> addedNamespaces = nsStack.addedForward();
        assertFalse(addedNamespaces.iterator().hasNext());
    }

    @Test
    public void testAddedReverse() {
        nsStack = new NamespaceStack();
        Iterable<Namespace> addedNamespaces = nsStack.addedReverse();
        assertFalse(addedNamespaces.iterator().hasNext());
    }

    @Test
    public void testIterator() {
        nsStack = new NamespaceStack();
        Namespace[] expectedScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }

    @Test
    public void testIsInScope() {
        nsStack = new NamespaceStack();
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        assertTrue(nsStack.isInScope(ns1));
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        assertFalse(nsStack.isInScope(ns2));
    }
}