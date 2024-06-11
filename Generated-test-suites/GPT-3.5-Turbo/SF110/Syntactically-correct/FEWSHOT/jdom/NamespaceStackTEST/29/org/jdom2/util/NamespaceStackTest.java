package org.jdom2.util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

public class NamespaceStackTest {
    @Test
    public void testConstructor() {
        NamespaceStack ns = new NamespaceStack();
        assertNotNull(ns);
        
        Namespace[] seed = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE };
        ns = new NamespaceStack(seed);
        assertNotNull(ns);
    }
    
    @Test
    public void testPushElement() {
        // TODO: Write test cases to test the push(Element element) method
    }
    
    @Test
    public void testPushAttribute() {
        // TODO: Write test cases to test the push(Attribute att) method
    }
    
    @Test
    public void testPop() {
        // TODO: Write test cases to test the pop() method
    }
    
    @Test
    public void testAddedForward() {
        // TODO: Write test cases to test the addedForward() method
    }
    
    @Test
    public void testAddedReverse() {
        // TODO: Write test cases to test the addedReverse() method
    }
    
    @Test
    public void testIterator() {
        // TODO: Write test cases to test the iterator() method
    }
    
    @Test
    public void testGetScope() {
        // TODO: Write test cases to test the getScope() method
    }
    
    @Test
    public void testIsInScope() {
        // TODO: Write test cases to test the isInScope() method
    }
}