package org.jdom2.util;

import org.junit.Before;
import org.junit.Test;
import org.jdom2.*;

import static org.junit.Assert.*;

public class NamespaceStackTest {

    private NamespaceStack stack;

    @Before
    public void setUp() {
        stack = new NamespaceStack();
    }

    @Test
    public void testPush_ElementWithNoAdditionalNamespaces() {
        Element element = new Element("test");
        stack.push(element);

        Namespace[] expectedScope = new Namespace[2];
        expectedScope[0] = Namespace.NO_NAMESPACE;
        expectedScope[1] = Namespace.XML_NAMESPACE;

        assertArrayEquals(expectedScope, stack.getScope());
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        assertFalse(stack.isInScope(Namespace.getNamespace("prefix", "uri")));
    }

    @Test
    public void testPush_ElementWithAdditionalNamespaces() {
        Namespace additionalNamespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace additionalNamespace2 = Namespace.getNamespace("prefix2", "uri2");

        Element element = new Element("test")
                .addNamespaceDeclaration(additionalNamespace1)
                .addNamespaceDeclaration(additionalNamespace2);

        stack.push(element);

        Namespace[] expectedScope = new Namespace[4];
        expectedScope[0] = Namespace.NO_NAMESPACE;
        expectedScope[1] = Namespace.XML_NAMESPACE;
        expectedScope[2] = additionalNamespace1;
        expectedScope[3] = additionalNamespace2;

        assertArrayEquals(expectedScope, stack.getScope());
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        assertTrue(stack.isInScope(additionalNamespace1));
        assertTrue(stack.isInScope(additionalNamespace2));
        assertFalse(stack.isInScope(Namespace.getNamespace("prefix3", "uri3")));
    }

    @Test
    public void testPush_ElementWithAttributes() {
        Namespace attributeNamespace = Namespace.getNamespace("prefix", "uri");

        Attribute attribute = new Attribute("test", "value", attributeNamespace);

        Element element = new Element("test")
                .setAttribute(attribute);

        stack.push(element);

        Namespace[] expectedScope = new Namespace[3];
        expectedScope[0] = Namespace.NO_NAMESPACE;
        expectedScope[1] = Namespace.XML_NAMESPACE;
        expectedScope[2] = attributeNamespace;

        assertArrayEquals(expectedScope, stack.getScope());
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        assertTrue(stack.isInScope(attributeNamespace));
        assertFalse(stack.isInScope(Namespace.getNamespace("prefix2", "uri2")));
    }

    @Test(expected = IllegalStateException.class)
    public void testPop_EmptyStack() {
        stack.pop();
    }

    @Test
    public void testPop() {
        Element element1 = new Element("test1");
        Element element2 = new Element("test2");

        stack.push(element1);
        stack.push(element2);

        Namespace[] expectedScope1 = new Namespace[2];
        expectedScope1[0] = Namespace.NO_NAMESPACE;
        expectedScope1[1] = Namespace.XML_NAMESPACE;

        Namespace[] expectedScope2 = new Namespace[2];
        expectedScope2[0] = Namespace.NO_NAMESPACE;
        expectedScope2[1] = Namespace.XML_NAMESPACE;

        assertArrayEquals(expectedScope2, stack.getScope());
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));

        stack.pop();

        assertArrayEquals(expectedScope1, stack.getScope());
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));

        stack.pop();

        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
    }

    @Test
    public void testAddedForward_EmptyStack() {
        assertFalse(stack.addedForward().iterator().hasNext());
    }

    @Test
    public void testAddedForward() {
        Namespace additionalNamespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace additionalNamespace2 = Namespace.getNamespace("prefix2", "uri2");

        Element element = new Element("test")
                .addNamespaceDeclaration(additionalNamespace1)
                .addNamespaceDeclaration(additionalNamespace2);

        stack.push(element);

        assertEquals(additionalNamespace1, stack.addedForward().iterator().next());
        assertEquals(additionalNamespace2, stack.addedForward().iterator().next());
        assertFalse(stack.addedForward().iterator().hasNext());
    }

    @Test
    public void testAddedReverse_EmptyStack() {
        assertFalse(stack.addedReverse().iterator().hasNext());
    }

    @Test
    public void testAddedReverse() {
        Namespace additionalNamespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace additionalNamespace2 = Namespace.getNamespace("prefix2", "uri2");

        Element element = new Element("test")
                .addNamespaceDeclaration(additionalNamespace1)
                .addNamespaceDeclaration(additionalNamespace2);

        stack.push(element);

        assertEquals(additionalNamespace2, stack.addedReverse().iterator().next());
        assertEquals(additionalNamespace1, stack.addedReverse().iterator().next());
        assertFalse(stack.addedReverse().iterator().hasNext());
    }

    @Test
    public void testGetScope_EmptyStack() {
        Namespace[] expectedScope = new Namespace[2];
        expectedScope[0] = Namespace.NO_NAMESPACE;
        expectedScope[1] = Namespace.XML_NAMESPACE;

        assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test
    public void testIterator_EmptyStack() {
        assertFalse(stack.iterator().hasNext());
    }

    @Test
    public void testIterator() {
        Namespace additionalNamespace = Namespace.getNamespace("prefix", "uri");

        Element element = new Element("test")
                .addNamespaceDeclaration(additionalNamespace);

        stack.push(element);

        assertEquals(Namespace.NO_NAMESPACE, stack.iterator().next());
        assertEquals(Namespace.XML_NAMESPACE, stack.iterator().next());
        assertEquals(additionalNamespace, stack.iterator().next());
        assertFalse(stack.iterator().hasNext());
    }

    @Test
    public void testIsInScope_EmptyStack() {
        assertFalse(stack.isInScope(Namespace.getNamespace("prefix", "uri")));
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
    }

    @Test
    public void testIsInScope() {
        Namespace additionalNamespace = Namespace.getNamespace("prefix", "uri");

        Element element = new Element("test")
                .addNamespaceDeclaration(additionalNamespace);

        stack.push(element);

        assertFalse(stack.isInScope(Namespace.getNamespace("prefix1", "uri1")));
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        assertTrue(stack.isInScope(additionalNamespace));
    }
}