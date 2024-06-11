package org.jdom2.util;

import org.jdom2.Namespace;
import org.jdom2.Element;
import org.jdom2.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class NamespaceStackTest {

    @Test
    public void testPushElement() {
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        
        Namespace expectedNamespace = element.getNamespace();
        Namespace[] expectedScope = stack.getScope();
        
        assertEquals(expectedNamespace, stack.addedForward().iterator().next());
        assertEquals(Arrays.asList(expectedScope), Arrays.asList(stack.scope[stack.depth]));
    }
    
    @Test
    public void testPushAttribute() {
        Attribute attribute = new Attribute("testAttribute", "testValue", Namespace.NO_NAMESPACE);
        NamespaceStack stack = new NamespaceStack();
        stack.push(attribute);
        
        Namespace expectedNamespace = attribute.getNamespace();
        Namespace[] expectedScope = stack.getScope();
        
        assertEquals(expectedNamespace, stack.addedForward().iterator().next());
        assertEquals(Arrays.asList(expectedScope), Arrays.asList(stack.scope[stack.depth]));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPop_EmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }
    
    @Test
    public void testPop() {
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        stack.pop();
        
        Namespace[] expectedScope = stack.getScope();
        
        assertEquals(NamespaceStack.EMPTYITER, stack.addedForward());
        assertNull(stack.scope[stack.depth]);
        assertEquals(Arrays.asList(expectedScope), Arrays.asList(stack.scope[stack.depth - 1]));
    }
    
    @Test
    public void testIterator() {
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        
        Iterator<Namespace> iterator = stack.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(element.getNamespace(), iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testGetScope() {
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        
        Namespace[] expectedScope = stack.scope[stack.depth];
        
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test
    public void testIsInScope_True() {
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
    }
    
    @Test
    public void testIsInScope_False() {
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        
        assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
    }
}