
package org.jdom2.util;

import static org.junit.Assert.*;
import org.jdom2.Namespace;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;

public class NamespaceStackTest {
    private NamespaceStack stack;
    
    @Before
    public void setup() {
        stack = new NamespaceStack();
    }
    
    @Test
    public void testPush_Element_WithEmptyElement_ReturnOnlyMainNamespace() {
        Element element = new Element("test");
        stack.push(element);
        
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        Namespace[] actualScope = stack.getScope();
        
        assertArrayEquals(expectedScope, actualScope);
    }

    @Test
    public void testPush_Element_WithAdditionalNamespaces_ReturnNamespaceScopeInOrder() {
        Element element = new Element("test");
        Namespace ns1 = Namespace.getNamespace("ns1", "http://www.example.com/ns1");
        Namespace ns2 = Namespace.getNamespace("ns2", "http://www.example.com/ns2");
        element.addNamespaceDeclaration(ns1);
        element.addNamespaceDeclaration(ns2);
        stack.push(element);
        
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, ns1, ns2};
        Namespace[] actualScope = stack.getScope();
        
        assertArrayEquals(expectedScope, actualScope);
    }
    
    @Test
    public void testPush_Element_WithAttributes_ReturnUpdatedNamespaceScope() {
        Element element = new Element("test");
        Namespace ns1 = Namespace.getNamespace("ns1", "http://www.example.com/ns1");
        element.addNamespaceDeclaration(ns1);
        Attribute attribute = new Attribute("attr1", "value1", ns1);
        element.setAttribute(attribute);
        stack.push(element);
        
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, ns1};
        Namespace[] actualScope = stack.getScope();
        
        assertArrayEquals(expectedScope, actualScope);
    }
    
    @Test
    public void testPush_Attribute_ReturnUpdatedNamespaceScope() {
        Attribute attribute = new Attribute("attr1", "value1", Namespace.NO_NAMESPACE);
        stack.push(attribute);
        
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        Namespace[] actualScope = stack.getScope();
        
        assertArrayEquals(expectedScope, actualScope);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPop_WhenDepthIsZero_ThrowIllegalStateException() {
        stack.pop();
    }
    
    @Test
    public void testAddedForward_WithEmptyStack_ReturnEmptyIterator() {
        Iterator<Namespace> iterator = stack.addedForward().iterator();
        
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testAddedForward_WithNonEmptyStack_ReturnIteratorInForwardOrder() {
        Element element = new Element("test");
        Namespace ns1 = Namespace.getNamespace("ns1", "http://www.example.com/ns1");
        Namespace ns2 = Namespace.getNamespace("ns2", "http://www.example.com/ns2");
        element.addNamespaceDeclaration(ns1);
        element.addNamespaceDeclaration(ns2);
        stack.push(element);
        
        Iterator<Namespace> iterator = stack.addedForward().iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(ns1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ns2, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testAddedReverse_WithEmptyStack_ReturnEmptyIterator() {
        Iterator<Namespace> iterator = stack.addedReverse().iterator();
        
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testAddedReverse_WithNonEmptyStack_ReturnIteratorInReverseOrder() {
        Element element = new Element("test");
        Namespace ns1 = Namespace.getNamespace("ns1", "http://www.example.com/ns1");
        Namespace ns2 = Namespace.getNamespace("ns2", "http://www.example.com/ns2");
        element.addNamespaceDeclaration(ns1);
        element.addNamespaceDeclaration(ns2);
        stack.push(element);
        
        Iterator<Namespace> iterator = stack.addedReverse().iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(ns2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ns1, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIterator_ReturnNamespaceIteratorInScopeOrder() {
        Element element = new Element("test");
        Namespace ns1 = Namespace.getNamespace("ns1", "http://www.example.com/ns1");
        Namespace ns2 = Namespace.getNamespace("ns2", "http://www.example.com/ns2");
        element.addNamespaceDeclaration(ns1);
        element.addNamespaceDeclaration(ns2);
        stack.push(element);
        
        Iterator<Namespace> iterator = stack.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.XML_NAMESPACE, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ns1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ns2, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testGetScope_ReturnCurrentNamespaceScopeCopy() throws Exception {
        Element element = new Element("test");
        Namespace ns1 = Namespace.getNamespace("ns1", "http://www.example.com/ns1");
        element.addNamespaceDeclaration(ns1);
        stack.push(element);
        
        Namespace[] scope = stack.getScope();
        scope[0] = null;
        
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, ns1};
        Namespace[] actualScope = stack.getScope();
        
        assertArrayEquals(expectedScope, actualScope);
    }
    
    @Test
    public void testIsInScope_WithNamespaceInScope_ReturnTrue() {
        Element element = new Element("test");
        Namespace ns1 = Namespace.getNamespace("ns1", "http://www.example.com/ns1");
        element.addNamespaceDeclaration(ns1);
        stack.push(element);
        
        boolean isInScope = stack.isInScope(ns1);
        
        assertTrue(isInScope);
    }
    
    @Test
    public void testIsInScope_WithNamespaceNotInScope_ReturnFalse() {
        Element element = new Element("test");
        Namespace ns1 = Namespace.getNamespace("ns1", "http://www.example.com/ns1");
        element.addNamespaceDeclaration(ns1);
        stack.push(element);
        
        Namespace ns2 = Namespace.getNamespace("ns2", "http://www.example.com/ns2");
        boolean isInScope = stack.isInScope(ns2);
        
        assertFalse(isInScope);
    }
    
    @Test
    public void testBinarySearch_WithExistingKey_ReturnKeyIndex() throws Exception {
        Namespace[] data = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        Field nscompField = NamespaceStack.class.getDeclaredField("NSCOMP");
        nscompField.setAccessible(true);
        Comparator<Namespace> nscomp = (Comparator<Namespace>) nscompField.get(stack);
        
        int index = NamespaceStack.binarySearch(data, 0, data.length, Namespace.XML_NAMESPACE);        
        
        assertEquals(1, index);
    }
    
    @Test
    public void testBinarySearch_WithNonExistingKey_ReturnNegativeInsertionPoint() throws Exception {
        Namespace[] data = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        Field nscompField = NamespaceStack.class.getDeclaredField("NSCOMP");
        nscompField.setAccessible(true);
        Comparator<Namespace> nscomp = (Comparator<Namespace>) nscompField.get(stack);
        
        int index = NamespaceStack.binarySearch(data, 0, data.length, Namespace.getNamespace("ns1", "http://www.example.com/ns1"));
        
        assertEquals(-1, index);
    }
    
    
    @Test
    public void testPrivateConstructor() throws Exception {
        Constructor<?>[] constructors = NamespaceStack.class.getDeclaredConstructors();
        Constructor<?> constructor = constructors[0];
        constructor.setAccessible(true);
        
        try {
            constructor.newInstance();
            fail("Private constructor should throw IllegalAccessException");
        } catch (IllegalAccessException e) {
            // Success
        }
    }
    
    @Test
    public void testPrivateStaticVariables() throws Exception {
        Field emptyField = NamespaceStack.class.getDeclaredField("EMPTY");
        Field emptyIterField = NamespaceStack.class.getDeclaredField("EMPTYITER");
        Field nscompField = NamespaceStack.class.getDeclaredField("NSCOMP");
        Field defaultSeedField = NamespaceStack.class.getDeclaredField("DEFAULTSEED");
        
        emptyField.setAccessible(true);
        emptyIterField.setAccessible(true);
        nscompField.setAccessible(true);
        defaultSeedField.setAccessible(true);
        
        Namespace[] empty = (Namespace[]) emptyField.get(null);
        Iterable<Namespace> emptyIter = (Iterable<Namespace>) emptyIterField.get(null);
        Comparator<Namespace> nscomp = (Comparator<Namespace>) nscompField.get(null);
        Namespace[] defaultSeed = (Namespace[]) defaultSeedField.get(null);
        
        assertNotNull(empty);
        assertNotNull(emptyIter);
        assertNotNull(nscomp);
        assertNotNull(defaultSeed);
        assertEquals(0, empty.length);
        assertFalse(emptyIter.iterator().hasNext());
        assertNotNull(nscomp);
        assertEquals(2, defaultSeed.length);
    }
}

