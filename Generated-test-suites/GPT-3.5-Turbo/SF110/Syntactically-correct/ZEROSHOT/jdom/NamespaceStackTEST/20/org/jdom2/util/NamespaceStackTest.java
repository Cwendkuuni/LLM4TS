package org.jdom2.util;

import static org.junit.Assert.*;

import org.jdom2.*;
import org.junit.Test;
import java.util.*;

public class NamespaceStackTest {

    @Test
    public void testPush() {
        NamespaceStack stack = new NamespaceStack();

        // Test when element does not have additional namespaces or attributes
        Element element1 = new Element("element1");
        stack.push(element1);
        assertFalse(stack.addedForward().iterator().hasNext());

        // Test when element has additional namespaces
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        element1.addNamespaceDeclaration(ns1);
        stack.push(element1);
        Iterator<Namespace> addedNamespaces = stack.addedForward().iterator();
        assertTrue(addedNamespaces.hasNext());
        assertEquals(ns1, addedNamespaces.next());
        assertFalse(addedNamespaces.hasNext());

        // Test when element has attributes with namespaces
        Attribute att1 = new Attribute("att1", "value1", Namespace.getNamespace("prefix2", "uri2"));
        element1.setAttribute(att1);
        stack.push(element1);
        addedNamespaces = stack.addedForward().iterator();
        assertTrue(addedNamespaces.hasNext());
        assertEquals(att1.getNamespace(), addedNamespaces.next());
        assertFalse(addedNamespaces.hasNext());
        
        // Test when element has duplicate namespaces
        Element element2 = new Element("element2");
        Namespace ns2 = Namespace.getNamespace("prefix1", "uri1");
        element2.addNamespaceDeclaration(ns2);
        stack.push(element2);
        addedNamespaces = stack.addedForward().iterator();
        assertFalse(addedNamespaces.hasNext());
        
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();

        // Test pop when stack is empty
        try {
            stack.pop();
            fail("Expected an IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Cannot over-pop the stack.", e.getMessage());
        }

        // Test pop when stack is not empty
        Element element1 = new Element("element1");
        stack.push(element1);
        stack.pop();
        assertFalse(stack.addedForward().iterator().hasNext());
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();

        // Test iterator when stack is empty
        assertFalse(stack.iterator().hasNext());

        // Test iterator when stack is not empty
        Element element1 = new Element("element1");
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        element1.addNamespaceDeclaration(ns1);
        stack.push(element1);
        Iterator<Namespace> iterator = stack.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(ns1, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetScope() {
        NamespaceStack stack = new NamespaceStack();

        // Test getScope when stack is empty
        Namespace[] expected = {};
        assertArrayEquals(expected, stack.getScope());

        // Test getScope when stack is not empty
        Element element1 = new Element("element1");
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        element1.addNamespaceDeclaration(ns1);
        stack.push(element1);
        expected = new Namespace[] {ns1};
        assertArrayEquals(expected, stack.getScope());
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();

        // Test isInScope when stack is empty
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        assertFalse(stack.isInScope(ns1));

        // Test isInScope when stack is not empty
        Element element1 = new Element("element1");
        stack.push(element1);
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertFalse(stack.isInScope(ns1));
    }
}