import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import org.jdom2.Element;
import org.jdom2.Attribute;
import org.jdom2.Namespace;
import org.jdom2.util.NamespaceStack;

import static org.junit.Assert.*;

public class NamespaceStackTest {
    
    @Test
    public void testPush_Element() {
        // Typical use case
        Element element = new Element("test", Namespace.getNamespace("prefix", "http://example.com"));
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix2", "http://example2.com"));
        element.setAttribute(new Attribute("attr", "value", Namespace.getNamespace("prefix3", "http://example3.com")));
        
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix", "http://example.com")));
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix2", "http://example2.com")));
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix3", "http://example3.com")));
    }
    
    @Test
    public void testPush_Element_Error() {
        // Error scenario
        Element element = new Element("test");
        NamespaceStack stack = new NamespaceStack();
        
        try {
            stack.push(element);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        assertFalse(stack.isInScope(Namespace.getNamespace("prefix", "http://example.com")));
    }
    
    @Test
    public void testPush_Attribute() {
        // Typical use case
        Attribute attribute = new Attribute("attr", "value", Namespace.getNamespace("prefix", "http://example.com"));
        
        NamespaceStack stack = new NamespaceStack();
        stack.push(attribute);
        
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix", "http://example.com")));
    }
    
    @Test
    public void testPush_Attribute_Error() {
        // Error scenario
        Attribute attribute = new Attribute("attr", "value");
        
        NamespaceStack stack = new NamespaceStack();
        
        try {
            stack.push(attribute);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
        
        assertFalse(stack.isInScope(Namespace.getNamespace("prefix", "http://example.com")));
    }
    
    @Test
    public void testPop() {
        // Typical use case
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("test", Namespace.getNamespace("prefix", "http://example.com")));
        stack.pop();
        
        assertFalse(stack.isInScope(Namespace.getNamespace("prefix", "http://example.com")));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPop_Error() {
        // Error scenario
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }
    
    @Test
    public void testAddedForward() {
        // Typical use case
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("test", Namespace.getNamespace("prefix1", "http://example1.com")));
        stack.push(new Element("test2", Namespace.getNamespace("prefix2", "http://example2.com")));
        
        Iterable<Namespace> addedForward = stack.addedForward();
        List<Namespace> addedForwardList = Arrays.asList(addedForward);
        
        assertTrue(addedForwardList.contains(Namespace.getNamespace("prefix1", "http://example1.com")));
        assertTrue(addedForwardList.contains(Namespace.getNamespace("prefix2", "http://example2.com")));
    }
    
    @Test
    public void testAddedReverse() {
        // Typical use case
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("test", Namespace.getNamespace("prefix1", "http://example1.com")));
        stack.push(new Element("test2", Namespace.getNamespace("prefix2", "http://example2.com")));
        
        Iterable<Namespace> addedReverse = stack.addedReverse();
        List<Namespace> addedReverseList = Arrays.asList(addedReverse);
        
        assertTrue(addedReverseList.contains(Namespace.getNamespace("prefix2", "http://example2.com")));
        assertTrue(addedReverseList.contains(Namespace.getNamespace("prefix1", "http://example1.com")));
    }
    
    @Test
    public void testIterator() {
        // Typical use case
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("test", Namespace.getNamespace("prefix1", "http://example1.com")));
        stack.push(new Element("test2", Namespace.getNamespace("prefix2", "http://example2.com")));
        
        Iterable<Namespace> iterator = stack.iterator();
        List<Namespace> iteratorList = Arrays.asList(iterator);
        
        assertTrue(iteratorList.contains(Namespace.getNamespace("prefix1", "http://example1.com")));
        assertTrue(iteratorList.contains(Namespace.getNamespace("prefix2", "http://example2.com")));
    }
    
    @Test
    public void testGetScope() {
        // Typical use case
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("test", Namespace.getNamespace("prefix1", "http://example1.com")));
        stack.push(new Element("test2", Namespace.getNamespace("prefix2", "http://example2.com")));
        
        Namespace[] scope = stack.getScope();
        
        assertEquals(Namespace.getNamespace("prefix1", "http://example1.com"), scope[0]);
        assertEquals(Namespace.getNamespace("prefix2", "http://example2.com"), scope[1]);
    }
    
    @Test
    public void testIsInScope() {
        // Typical use case
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("test", Namespace.getNamespace("prefix", "http://example.com")));
        
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix", "http://example.com")));
    }
    
    @Test
    public void testIsInScope_Error() {
        // Error scenario
        NamespaceStack stack = new NamespaceStack();
        
        assertFalse(stack.isInScope(Namespace.getNamespace("prefix", "http://example.com")));
    }
}