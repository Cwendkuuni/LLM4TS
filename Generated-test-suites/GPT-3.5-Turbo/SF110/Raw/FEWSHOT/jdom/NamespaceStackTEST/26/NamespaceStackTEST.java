package org.jdom2.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceStackTest {
    
    @Test
    public void testPushElement() {
        NamespaceStack nsStack = new NamespaceStack();
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("test", "http://www.example.com"));
        
        nsStack.push(element);
        
        assertTrue(nsStack.isInScope(Namespace.getNamespace("test", "http://www.example.com")));
    }
    
    @Test
    public void testPushAttribute() {
        NamespaceStack nsStack = new NamespaceStack();
        Attribute attribute = new Attribute("test", "value", Namespace.getNamespace("test", "http://www.example.com"));
        
        nsStack.push(attribute);
        
        assertTrue(nsStack.isInScope(Namespace.getNamespace("test", "http://www.example.com")));
    }
    
    @Test
    public void testPop() {
        NamespaceStack nsStack = new NamespaceStack();
        Element element1 = new Element("test1");
        element1.setNamespace(Namespace.getNamespace("test1", "http://www.example.com"));
        Element element2 = new Element("test2");
        element2.setNamespace(Namespace.getNamespace("test2", "http://www.example.com"));
        
        nsStack.push(element1);
        nsStack.push(element2);
        nsStack.pop();
        
        assertFalse(nsStack.isInScope(Namespace.getNamespace("test2", "http://www.example.com")));
        assertTrue(nsStack.isInScope(Namespace.getNamespace("test1", "http://www.example.com")));
    }
    
    @Test
    public void testAddedForward() {
        NamespaceStack nsStack = new NamespaceStack();
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("test", "http://www.example.com"));
        
        nsStack.push(element);
        
        boolean found = false;
        for (Namespace ns : nsStack.addedForward()) {
            if (ns == Namespace.getNamespace("test", "http://www.example.com")) {
                found = true;
                break;
            }
        }
        
        assertTrue(found);
    }
    
    @Test
    public void testAddedReverse() {
        NamespaceStack nsStack = new NamespaceStack();
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("test", "http://www.example.com"));
        
        nsStack.push(element);
        
        boolean found = false;
        for (Namespace ns : nsStack.addedReverse()) {
            if (ns == Namespace.getNamespace("test", "http://www.example.com")) {
                found = true;
                break;
            }
        }
        
        assertTrue(found);
    }
    
    @Test
    public void testIterator() {
        NamespaceStack nsStack = new NamespaceStack();
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("test", "http://www.example.com"));
        
        nsStack.push(element);
        
        boolean found = false;
        for (Namespace ns : nsStack) {
            if (ns == Namespace.getNamespace("test", "http://www.example.com")) {
                found = true;
                break;
            }
        }
        
        assertTrue(found);
    }
    
    @Test
    public void testGetScope() {
        NamespaceStack nsStack = new NamespaceStack();
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("test", "http://www.example.com"));
        
        nsStack.push(element);
        
        assertTrue(nsStack.getScope().length == 1);
        assertEquals(nsStack.getScope()[0], Namespace.getNamespace("test", "http://www.example.com"));
    }
    
    @Test
    public void testIsInScope() {
        NamespaceStack nsStack = new NamespaceStack();
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("test", "http://www.example.com"));
        
        nsStack.push(element);
        
        assertTrue(nsStack.isInScope(Namespace.getNamespace("test", "http://www.example.com")));
        assertFalse(nsStack.isInScope(Namespace.getNamespace("test2", "http://www.example.com")));
    }
}