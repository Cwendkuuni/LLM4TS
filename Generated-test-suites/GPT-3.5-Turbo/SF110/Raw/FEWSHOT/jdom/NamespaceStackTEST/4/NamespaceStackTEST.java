package org.jdom2.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceStackTest {
    
    @Test
    public void testPush_Element() {
        Element element = new Element("testElement");
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.push(element);
        assertTrue(nsStack.isInScope(Namespace.NO_NAMESPACE));
    }
    
    @Test
    public void testPush_Attribute() {
        Attribute attribute = new Attribute("testAttribute", "value");
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.push(attribute);
        assertTrue(nsStack.isInScope(Namespace.NO_NAMESPACE));
    }
    
    @Test
    public void testPop() {
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.pop();
        assertNull(nsStack.getScope());
    }
    
    @Test
    public void testAddedForward() {
        NamespaceStack nsStack = new NamespaceStack();
        Iterable<Namespace> addedForward = nsStack.addedForward();
        assertNotNull(addedForward.iterator());
    }
    
    @Test
    public void testAddedReverse() {
        NamespaceStack nsStack = new NamespaceStack();
        Iterable<Namespace> addedReverse = nsStack.addedReverse();
        assertNotNull(addedReverse.iterator());
    }
    
    @Test
    public void testIterator() {
        NamespaceStack nsStack = new NamespaceStack();
        assertNotNull(nsStack.iterator());
    }
    
    @Test
    public void testGetScope() {
        NamespaceStack nsStack = new NamespaceStack();
        assertNotNull(nsStack.getScope());
    }
    
    @Test
    public void testIsInScope() {
        NamespaceStack nsStack = new NamespaceStack();
        assertTrue(nsStack.isInScope(Namespace.NO_NAMESPACE));
    }
}