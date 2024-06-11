package org.jdom2.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceStackTest {
    @Test
    public void testConstructor() {
        NamespaceStack stack = new NamespaceStack();
        assertEquals(0, stack.getScope().length);
    }
    
    @Test
    public void testPush() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("element");
        stack.push(element);
        assertEquals(1, stack.getScope().length);
        
        Attribute attribute = new Attribute("attribute", "value");
        stack.push(attribute);
        assertEquals(2, stack.getScope().length);
    }
    
    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("element");
        stack.push(element);
        stack.pop();
        assertEquals(0, stack.getScope().length);
    }
    
    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Namespace ns = Namespace.getNamespace("prefix", "uri");
        
        Element element = new Element("element", ns);
        stack.push(element);
        assertTrue(stack.isInScope(ns));
        
        stack.pop();
        assertFalse(stack.isInScope(ns));
    }
    
    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        Namespace ns3 = Namespace.getNamespace("prefix3", "uri3");
        Element element = new Element("element", ns1);
        element.addNamespaceDeclaration(ns2);
        stack.push(element);
        stack.push(new Attribute("attribute", "value", ns3));
        for (Namespace ns : stack.addedForward()) {
            assertTrue(ns == ns1 || ns == ns2 || ns == ns3);
        }
    }
    
    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        Namespace ns3 = Namespace.getNamespace("prefix3", "uri3");
        Element element = new Element("element", ns1);
        element.addNamespaceDeclaration(ns2);
        stack.push(element);
        stack.push(new Attribute("attribute", "value", ns3));
        for (Namespace ns : stack.addedReverse()) {
            assertTrue(ns == ns1 || ns == ns2 || ns == ns3);
        }
    }
}