package org.jdom2.util;

import org.jdom2.*;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NamespaceStackTest {

    @Test
    public void testPush() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        namespaceStack.push(element);
        
        Iterator<Namespace> iterator = namespaceStack.iterator();
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testPush_SamePrefix() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element1 = new Element("test", Namespace.getNamespace("prefix", "uri1"));
        Element element2 = new Element("test2", Namespace.getNamespace("prefix", "uri2"));
        element2.addNamespaceDeclaration(Namespace.getNamespace("prefix", "uri3"));
        
        namespaceStack.push(element1);
        namespaceStack.push(element2);
        
        Iterator<Namespace> iterator = namespaceStack.iterator();
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());
        assertEquals(Namespace.getNamespace("prefix", "uri2"), iterator.next());
        assertEquals(Namespace.getNamespace("prefix", "uri3"), iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testPush_Attribute() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Attribute attribute = new Attribute("attr", "value", Namespace.getNamespace("prefix", "uri"));
        namespaceStack.push(attribute);
        
        Iterator<Namespace> iterator = namespaceStack.iterator();
        assertEquals(Namespace.getNamespace("prefix", "uri"), iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testPop() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element1 = new Element("test", Namespace.NO_NAMESPACE);
        Element element2 = new Element("test2", Namespace.getNamespace("prefix", "uri"));

        namespaceStack.push(element1);
        namespaceStack.push(element2);
        
        namespaceStack.pop();
        
        Iterator<Namespace> iterator = namespaceStack.iterator();
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalStateException.class)
    public void testPop_EmptyStack() {
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.pop();
    }
    
    @Test
    public void testAddedForward() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Namespace namespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "uri2");
        
        List<Namespace> namespaces = new ArrayList<>();
        namespaces.add(namespace1);
        namespaces.add(namespace2);
        namespaceStack.push(new Element("test", Namespace.NO_NAMESPACE));
        namespaceStack.push(new Element("test2", Namespace.NO_NAMESPACE).addContent(new Element("test3", namespaces)));
        
        Iterator<Namespace> iterator = namespaceStack.addedForward().iterator();
        assertEquals(namespace1, iterator.next());
        assertEquals(namespace2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAddedForward_Empty() {
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(new Element("test", Namespace.NO_NAMESPACE));
        
        Iterator<Namespace> iterator = namespaceStack.addedForward().iterator();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testAddedReverse() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Namespace namespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "uri2");
        
        List<Namespace> namespaces = new ArrayList<>();
        namespaces.add(namespace1);
        namespaces.add(namespace2);
        namespaceStack.push(new Element("test", Namespace.NO_NAMESPACE));
        namespaceStack.push(new Element("test2", Namespace.NO_NAMESPACE).addContent(new Element("test3", namespaces)));
        
        Iterator<Namespace> iterator = namespaceStack.addedReverse().iterator();
        assertEquals(namespace2, iterator.next());
        assertEquals(namespace1, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testAddedReverse_Empty() {
        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(new Element("test", Namespace.NO_NAMESPACE));
        
        Iterator<Namespace> iterator = namespaceStack.addedReverse().iterator();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIterator() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Namespace namespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "uri2");
        
        namespaceStack.push(new Element("test", Namespace.NO_NAMESPACE));
        namespaceStack.push(new Element("test2", namespace1).addContent(new Element("test3", namespace2)));
        
        Iterator<Namespace> iterator = namespaceStack.iterator();
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());
        assertEquals(namespace1, iterator.next());
        assertEquals(namespace2, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testGetScope() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Namespace namespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "uri2");
        
        List<Namespace> namespaces = new ArrayList<>();
        namespaces.add(namespace1);
        namespaces.add(namespace2);
        namespaceStack.push(new Element("test", Namespace.NO_NAMESPACE));
        namespaceStack.push(new Element("test2", Namespace.NO_NAMESPACE).addContent(new Element("test3", namespaces)));
        
        Namespace[] scope = namespaceStack.getScope();
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(namespace1, scope[1]);
        assertEquals(namespace2, scope[2]);
        assertEquals(3, scope.length);
    }
    
    @Test
    public void testIsInScope() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Namespace namespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "uri2");
        
        List<Namespace> namespaces = new ArrayList<>();
        namespaces.add(namespace1);
        namespaces.add(namespace2);
        namespaceStack.push(new Element("test", Namespace.NO_NAMESPACE));
        namespaceStack.push(new Element("test2", Namespace.NO_NAMESPACE).addContent(new Element("test3", namespaces)));
        
        assertTrue(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(namespaceStack.isInScope(namespace1));
        assertTrue(namespaceStack.isInScope(namespace2));
        assertFalse(namespaceStack.isInScope(Namespace.getNamespace("prefix3", "uri3")));
    }
}