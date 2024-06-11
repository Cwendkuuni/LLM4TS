package org.jdom2.util;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jdom2.internal.NamespaceStack;
import org.jdom2.Namespace;
import org.jdom2.Element;
import org.jdom2.Attribute;

import java.util.Iterator;

public class NamespaceStackTest {

    @Test
    public void testPush() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", ns1);
        element.addNamespaceDeclaration(ns2);
        
        stack.push(element);
        
        Namespace[] expectedScope = new Namespace[]{ns2, ns1};
        Namespace[] actualScope = stack.getScope();
        assertArrayEquals(expectedScope, actualScope);
    }

    @Test
    public void testPushWithAttributes() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", ns1);
        element.addNamespaceDeclaration(ns2);
        element.setAttribute(new Attribute("attr1", "value1", ns1));
        element.setAttribute(new Attribute("attr2", "value2", ns2));
        
        stack.push(element);
        
        Namespace[] expectedScope = new Namespace[]{ns2, ns1};
        Namespace[] actualScope = stack.getScope();
        assertArrayEquals(expectedScope, actualScope);
    }

    @Test
    public void testPushWithAttributesAndAdditionalNamespaces() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", ns1);
        element.addNamespaceDeclaration(ns2);
        element.setAttribute(new Attribute("attr1", "value1", ns1));
        element.setAttribute(new Attribute("attr2", "value2", ns2));
        element.addAdditionalNamespace(Namespace.getNamespace("extra", "uri3"));
        
        stack.push(element);
        
        Namespace[] expectedScope = new Namespace[]{ns2, ns1, Namespace.getNamespace("extra", "uri3")};
        Namespace[] actualScope = stack.getScope();
        assertArrayEquals(expectedScope, actualScope);
    }

    @Test
    public void testPop() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        NamespaceStack stack = new NamespaceStack();
        Element element1 = new Element("test1", ns1);
        Element element2 = new Element("test2", ns2);
        element1.addNamespaceDeclaration(ns2);
        element2.addNamespaceDeclaration(ns1);
        
        stack.push(element1);
        stack.push(element2);
        
        stack.pop();
        
        Namespace[] expectedScope = new Namespace[]{ns2, ns1};
        Namespace[] actualScope = stack.getScope();
        assertArrayEquals(expectedScope, actualScope);
    }

    @Test
    public void testAddedForward() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", ns1);
        element.addNamespaceDeclaration(ns2);
        
        stack.push(element);
        
        Iterator<Namespace> iterator = stack.addedForward().iterator();
        assertEquals(ns2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAddedReverse() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", ns1);
        element.addNamespaceDeclaration(ns2);
        
        stack.push(element);
        
        Iterator<Namespace> iterator = stack.addedReverse().iterator();
        assertEquals(ns2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        NamespaceStack stack = new NamespaceStack();
        Element element1 = new Element("test1", ns1);
        Element element2 = new Element("test2", ns2);
        element1.addNamespaceDeclaration(ns2);
        element2.addNamespaceDeclaration(ns1);
        
        stack.push(element1);
        stack.push(element2);
        
        Iterator<Namespace> iterator = stack.iterator();
        assertEquals(ns2, iterator.next());
        assertEquals(ns1, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIsInScope() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        Namespace ns3 = Namespace.getNamespace("prefix3", "uri3");
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", ns1);
        element.addNamespaceDeclaration(ns2);
        
        stack.push(element);
        
        assertTrue(stack.isInScope(ns1));
        assertTrue(stack.isInScope(ns2));
        assertFalse(stack.isInScope(ns3));
    }
}