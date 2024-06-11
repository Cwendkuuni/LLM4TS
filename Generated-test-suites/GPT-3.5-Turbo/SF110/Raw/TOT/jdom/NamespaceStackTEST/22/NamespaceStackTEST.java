import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.Namespace;
import org.jdom2.Element;
import org.jdom2.Attribute;

public class NamespaceStackTest {
    
    // Test push method with Element parameter
    @Test
    public void testPushElement() {
        Namespace ns1 = Namespace.getNamespace("ns1", "http://www.namespace1.com");
        Namespace ns2 = Namespace.getNamespace("ns2", "http://www.namespace2.com");
        Element element = new Element("element", ns1);
        element.addNamespaceDeclaration(ns2);
        
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        
        assertTrue(stack.isInScope(ns1));
        assertTrue(stack.isInScope(ns2));
    }
    
    // Test push method with Attribute parameter
    @Test
    public void testPushAttribute() {
        Namespace ns1 = Namespace.getNamespace("ns1", "http://www.namespace1.com");
        Attribute attribute = new Attribute("attribute", "value", ns1);
        
        NamespaceStack stack = new NamespaceStack();
        stack.push(attribute);
        
        assertTrue(stack.isInScope(ns1));
    }
    
    // Test pop method when stack is not empty
    @Test
    public void testPop() {
        Namespace ns1 = Namespace.getNamespace("ns1", "http://www.namespace1.com");
        Namespace ns2 = Namespace.getNamespace("ns2", "http://www.namespace2.com");
        Element element = new Element("element", ns1);
        element.addNamespaceDeclaration(ns2);
        
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        stack.pop();
        
        assertFalse(stack.isInScope(ns1));
        assertFalse(stack.isInScope(ns2));
    }
    
    // Test pop method when stack is empty
    @Test(expected = IllegalStateException.class)
    public void testPopEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }
    
    // Test addedForward method when stack is empty
    @Test
    public void testAddedForwardEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        
        assertFalse(stack.addedForward().iterator().hasNext());
    }
    
    // Test addedReverse method when stack is empty
    @Test
    public void testAddedReverseEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        
        assertFalse(stack.addedReverse().iterator().hasNext());
    }
    
    // Test iterator method when stack is empty
    @Test
    public void testIteratorEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        
        assertFalse(stack.iterator().hasNext());
    }
    
    // Test getScope method when stack is empty
    @Test
    public void testGetScopeEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        
        assertEquals(0, stack.getScope().length);
    }
    
    // Test isInScope method when namespace is in scope
    @Test
    public void testIsInScopeTrue() {
        Namespace ns1 = Namespace.getNamespace("ns1", "http://www.namespace1.com");
        Element element = new Element("element", ns1);
        
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        
        assertTrue(stack.isInScope(ns1));
    }
    
    // Test isInScope method when namespace is not in scope
    @Test
    public void testIsInScopeFalse() {
        Namespace ns1 = Namespace.getNamespace("ns1", "http://www.namespace1.com");
        Namespace ns2 = Namespace.getNamespace("ns2", "http://www.namespace2.com");
        Element element = new Element("element", ns1);
        
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        
        assertFalse(stack.isInScope(ns2));
    }
}