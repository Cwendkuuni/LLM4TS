package org.jdom2.util;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.Attribute;

public class NamespaceStackTest {
    @Test
    public void testPush_elementWithNamespace() {
        Element element = new Element("test", Namespace.getNamespace("ns", "www.example.com"));
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        
        assertTrue(stack.isInScope(Namespace.getNamespace("ns", "www.example.com")));
        assertFalse(stack.isInScope(Namespace.getNamespace("other", "www.example.com")));
    }
    
    @Test
    public void testPush_elementWithAdditionalNamespaces() {
        Element element = new Element("test", Namespace.getNamespace("ns", "www.example.com"));
        element.addNamespaceDeclaration(Namespace.getNamespace("other", "www.example.com"));
        
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        
        assertTrue(stack.isInScope(Namespace.getNamespace("other", "www.example.com")));
        assertFalse(stack.isInScope(Namespace.getNamespace("another", "www.example.com")));
    }
    
    @Test
    public void testPush_elementWithAttributes() {
        Element element = new Element("test", Namespace.getNamespace("ns", "www.example.com"));
        element.setAttribute(new Attribute("attr1", "value1", Namespace.getNamespace("other", "www.example.com")));
        element.setAttribute(new Attribute("attr2", "value2", Namespace.NO_NAMESPACE));
        
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        
        assertTrue(stack.isInScope(Namespace.getNamespace("other", "www.example.com")));
        assertFalse(stack.isInScope(Namespace.getNamespace("another", "www.example.com")));
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
    }
    
    @Test
    public void testPush_attributeWithNamespace() {
        Attribute attribute = new Attribute("attr", "value", Namespace.getNamespace("ns", "www.example.com"));
        NamespaceStack stack = new NamespaceStack();
        stack.push(attribute);
        
        assertTrue(stack.isInScope(Namespace.getNamespace("ns", "www.example.com")));
        assertFalse(stack.isInScope(Namespace.getNamespace("other", "www.example.com")));
    }
    
    @Test
    public void testPop() {
        Element element1 = new Element("test1", Namespace.getNamespace("ns1", "www.example.com"));
        Element element2 = new Element("test2", Namespace.getNamespace("ns2", "www.example.com"));
        Element element3 = new Element("test3", Namespace.getNamespace("ns3", "www.example.com"));
        
        NamespaceStack stack = new NamespaceStack();
        stack.push(element1);
        stack.push(element2);
        stack.push(element3);
        
        stack.pop();
        
        assertTrue(stack.isInScope(Namespace.getNamespace("ns2", "www.example.com")));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPop_emptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }
    
    @Test
    public void testAddedForward() {
        Element element = new Element("test", Namespace.getNamespace("ns", "www.example.com"));
        element.addNamespaceDeclaration(Namespace.getNamespace("other", "www.example.com"));
        
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        
        List<Namespace> namespaces = (List<Namespace>) stack.addedForward();
        
        assertEquals(2, namespaces.size());
        assertEquals(Namespace.getNamespace("ns", "www.example.com"), namespaces.get(0));
        assertEquals(Namespace.getNamespace("other", "www.example.com"), namespaces.get(1));
    }
    
    @Test
    public void testAddedReverse() {
        Element element = new Element("test", Namespace.getNamespace("ns", "www.example.com"));
        element.addNamespaceDeclaration(Namespace.getNamespace("other", "www.example.com"));
        
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        
        List<Namespace> namespaces = (List<Namespace>) stack.addedReverse();
        
        assertEquals(2, namespaces.size());
        assertEquals(Namespace.getNamespace("other", "www.example.com"), namespaces.get(0));
        assertEquals(Namespace.getNamespace("ns", "www.example.com"), namespaces.get(1));
    }
    
    @Test
    public void testGetScope() {
        Element element1 = new Element("test1", Namespace.getNamespace("ns1", "www.example.com"));
        Element element2 = new Element("test2", Namespace.getNamespace("ns2", "www.example.com"));
        Element element3 = new Element("test3", Namespace.getNamespace("ns3", "www.example.com"));
        
        NamespaceStack stack = new NamespaceStack();
        stack.push(element1);
        stack.push(element2);
        stack.push(element3);
        
        Namespace[] scope = stack.getScope();
        
        assertEquals(3, scope.length);
        assertEquals(Namespace.getNamespace("ns1", "www.example.com"), scope[0]);
        assertEquals(Namespace.getNamespace("ns2", "www.example.com"), scope[1]);
        assertEquals(Namespace.getNamespace("ns3", "www.example.com"), scope[2]);
    }
    
    @Test
    public void testIsInScope() {
        Element element1 = new Element("test1", Namespace.getNamespace("ns1", "www.example.com"));
        Element element2 = new Element("test2", Namespace.getNamespace("ns2", "www.example.com"));
        
        NamespaceStack stack = new NamespaceStack();
        stack.push(element1);
        stack.push(element2);
        
        assertTrue(stack.isInScope(Namespace.getNamespace("ns1", "www.example.com")));
        assertTrue(stack.isInScope(Namespace.getNamespace("ns2", "www.example.com")));
        assertFalse(stack.isInScope(Namespace.getNamespace("ns3", "www.example.com")));
    }
}