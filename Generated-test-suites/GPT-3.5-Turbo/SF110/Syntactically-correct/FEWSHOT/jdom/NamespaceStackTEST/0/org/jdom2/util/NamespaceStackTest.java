
package org.jdom2.util;

import org.junit.Test;
import org.jdom2.Element;
import org.jdom2.Attribute;
import org.jdom2.Namespace;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class NamespaceStackTest {
    
    @Test
    public void testConstructorWithDefaultSeed() {
        NamespaceStack stack = new NamespaceStack();
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test
    public void testConstructorWithSeed() {
        Namespace[] seed = {Namespace.getNamespace("prefix1", "uri1"), Namespace.getNamespace("prefix2", "uri2")};
        NamespaceStack stack = new NamespaceStack(seed);
        Namespace[] expectedScope = {Namespace.getNamespace("prefix1", "uri1"), Namespace.getNamespace("prefix2", "uri2")};
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test
    public void testPushWithElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("name", Namespace.getNamespace("prefix1", "uri1"));
        stack.push(element);
        Namespace[] expectedScope = {Namespace.getNamespace("prefix1", "uri1"), Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test
    public void testPushWithElement_WithAdditionalNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("name", Namespace.getNamespace("prefix1", "uri1"));
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix2", "uri2"));
        stack.push(element);
        Namespace[] expectedScope = {Namespace.getNamespace("prefix1", "uri1"), Namespace.getNamespace("prefix2", "uri2"), Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test
    public void testPushWithElement_WithAttributes() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("name", Namespace.getNamespace("prefix1", "uri1"));
        element.setAttribute(new Attribute("attr1", "value1", Namespace.getNamespace("prefix2", "uri2")));
        stack.push(element);
        Namespace[] expectedScope = {Namespace.getNamespace("prefix1", "uri1"), Namespace.getNamespace("prefix2", "uri2"), Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test
    public void testPushWithAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("attr1", "value1", Namespace.getNamespace("prefix1", "uri1"));
        stack.push(attribute);
        Namespace[] expectedScope = {Namespace.getNamespace("prefix1", "uri1"), Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPop_WithOneDepth() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }
    
    @Test
    public void testPop_WithMultipleDepth() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("name", Namespace.getNamespace("prefix1", "uri1")));
        stack.push(new Element("name", Namespace.getNamespace("prefix2", "uri2")));
        stack.pop();
        Namespace[] expectedScope = {Namespace.getNamespace("prefix1", "uri1"), Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("name", Namespace.getNamespace("prefix1", "uri1")));
        stack.push(new Element("name", Namespace.getNamespace("prefix2", "uri2")));
        
        List<Namespace> namespaces = new ArrayList<Namespace>();
        for (Namespace ns : stack) {
            namespaces.add(ns);
        }
        Namespace[] expectedNamespaces = {Namespace.getNamespace("prefix1", "uri1"), Namespace.getNamespace("prefix2", "uri2")};
        assertArrayEquals(expectedNamespaces, namespaces.toArray());
    }
    
    @Test
    public void testIsInScope_WithNamespaceInScope() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("name", Namespace.getNamespace("prefix1", "uri1")));
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix1", "uri1")));
    }
    
    @Test
    public void testIsInScope_WithNamespaceNotInScope() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("name", Namespace.getNamespace("prefix1", "uri1")));
        assertFalse(stack.isInScope(Namespace.getNamespace("prefix2", "uri2")));
    }

}
