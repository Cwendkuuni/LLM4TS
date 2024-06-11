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

public class NamespaceStackTest {

    private NamespaceStack namespaceStack;

    @Before
    public void setup() {
        namespaceStack = new NamespaceStack();
    }

    @Test
    public void testPushWithElement() {
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        namespaceStack.push(element);

        assertFalse(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
        assertEquals(2, namespaceStack.getScope().length);
    }

    @Test
    public void testPushWithAttribute() {
        Attribute attribute = new Attribute("testAttribute", "testValue", Namespace.NO_NAMESPACE);
        namespaceStack.push(attribute);

        assertFalse(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
        assertEquals(2, namespaceStack.getScope().length);
    }

    @Test
    public void testPop() {
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        namespaceStack.push(element);

        namespaceStack.pop();

        assertNull(namespaceStack.getScope());
    }

    @Test
    public void testAddedForward() {
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        namespaceStack.push(element);

        Iterable<Namespace> addedNS = namespaceStack.addedForward();

        assertTrue(addedNS.iterator().hasNext());
        assertEquals(0, namespaceStack.addedReverse().iterator().next().compareTo(addedNS.iterator().next()));
    }

    @Test
    public void testAddedReverse() {
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        namespaceStack.push(element);

        Iterable<Namespace> addedNS = namespaceStack.addedReverse();

        assertTrue(addedNS.iterator().hasNext());
        assertEquals(0, namespaceStack.addedForward().iterator().next().compareTo(addedNS.iterator().next()));
    }

    @Test
    public void testIterator() {
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        namespaceStack.push(element);

        Iterable<Namespace> scope = namespaceStack;

        assertTrue(scope.iterator().hasNext());
        assertTrue(scope.iterator().next().equals(Namespace.NO_NAMESPACE));
    }

    @Test
    public void testGetScope() {
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        namespaceStack.push(element);

        Namespace[] scope = namespaceStack.getScope();

        assertEquals(2, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(Namespace.XML_NAMESPACE, scope[1]);
    }

    @Test
    public void testIsInScope() {
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        namespaceStack.push(element);

        assertTrue(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
        assertFalse(namespaceStack.isInScope(Namespace.getNamespace("test")));
    }
}