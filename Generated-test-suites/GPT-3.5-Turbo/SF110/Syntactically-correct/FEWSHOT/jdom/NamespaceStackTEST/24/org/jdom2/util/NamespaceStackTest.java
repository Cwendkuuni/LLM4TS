package org.jdom2.util;

import static org.junit.Assert.*;
import org.junit.Test;

import org.jdom2.*;
import java.util.*;

public class NamespaceStackTest {
    
    @Test
    public void testPushWithElement() {
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("ns1", "http://www.example.com/ns1"));
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, element.getNamespace()};
        assertArrayEquals(expectedScope, stack.getScope());
        assertTrue(stack.isInScope(element.getNamespace()));
    }

    @Test
    public void testPushWithElementAndAdditionalNamespaces() {
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("ns1", "http://www.example.com/ns1"));
        element.addNamespaceDeclaration(Namespace.getNamespace("ns2", "http://www.example.com/ns2"));
        element.addNamespaceDeclaration(Namespace.getNamespace("ns3", "http://www.example.com/ns3"));
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE,
            element.getNamespace(), element.getAdditionalNamespaces().get(0),
            element.getAdditionalNamespaces().get(1)};
        assertArrayEquals(expectedScope, stack.getScope());
        assertTrue(stack.isInScope(element.getNamespace()));
        assertTrue(stack.isInScope(element.getAdditionalNamespaces().get(0)));
        assertTrue(stack.isInScope(element.getAdditionalNamespaces().get(1)));
    }
    
    @Test
    public void testPushWithAttributes() {
        Attribute attribute1 = new Attribute("attr1", "value1", Namespace.getNamespace("ns1", "http://www.example.com/ns1"));
        Attribute attribute2 = new Attribute("attr2", "value2", Namespace.getNamespace("ns2", "http://www.example.com/ns2"));
        Element element = new Element("test");
        element.setAttributes(Arrays.asList(attribute1, attribute2));
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, 
            Namespace.XML_NAMESPACE, 
            attribute1.getNamespace(), 
            attribute2.getNamespace()};
        assertArrayEquals(expectedScope, stack.getScope());
        assertTrue(stack.isInScope(attribute1.getNamespace()));
        assertTrue(stack.isInScope(attribute2.getNamespace()));
    }
    
    @Test
    public void testPop() {
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("ns1", "http://www.example.com/ns1"));
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        stack.pop();
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope, stack.getScope());
        assertFalse(stack.isInScope(element.getNamespace()));
    }
    
    @Test
    public void testAddedForward() {
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("ns1", "http://www.example.com/ns1"));
        element.addNamespaceDeclaration(Namespace.getNamespace("ns2", "http://www.example.com/ns2"));
        element.addNamespaceDeclaration(Namespace.getNamespace("ns3", "http://www.example.com/ns3"));
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        Iterable<Namespace> added = stack.addedForward();
        Iterator<Namespace> iterator = added.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(element.getAdditionalNamespaces().get(0), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(element.getAdditionalNamespaces().get(1), iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testAddedReverse() {
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("ns1", "http://www.example.com/ns1"));
        element.addNamespaceDeclaration(Namespace.getNamespace("ns2", "http://www.example.com/ns2"));
        element.addNamespaceDeclaration(Namespace.getNamespace("ns3", "http://www.example.com/ns3"));
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        Iterable<Namespace> added = stack.addedReverse();
        Iterator<Namespace> iterator = added.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(element.getAdditionalNamespaces().get(1), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(element.getAdditionalNamespaces().get(0), iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIterator() {
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("ns1", "http://www.example.com/ns1"));
        element.addNamespaceDeclaration(Namespace.getNamespace("ns2", "http://www.example.com/ns2"));
        element.addNamespaceDeclaration(Namespace.getNamespace("ns3", "http://www.example.com/ns3"));
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        Iterator<Namespace> iterator = stack.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.XML_NAMESPACE, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(element.getNamespace(), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(element.getAdditionalNamespaces().get(0), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(element.getAdditionalNamespaces().get(1), iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIsInScope() {
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("ns1", "http://www.example.com/ns1"));
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        assertTrue(stack.isInScope(element.getNamespace()));
        assertFalse(stack.isInScope(Namespace.getNamespace("ns2", "http://www.example.com/ns2")));
    }

}