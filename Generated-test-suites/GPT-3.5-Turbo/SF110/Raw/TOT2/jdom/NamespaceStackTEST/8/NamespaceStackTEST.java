import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceStackTest {
    
    @Test
    public void testConstructor() {
        Namespace[] seed = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        NamespaceStack stack = new NamespaceStack(seed);
        
        assertNotNull(stack);
        assertEquals(stack.getScope().length, seed.length);
        assertEquals(stack.isInScope(Namespace.NO_NAMESPACE), true);
        assertEquals(stack.isInScope(Namespace.XML_NAMESPACE), true);
    }
    
    @Test
    public void testPush() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        
        stack.push(element);
        
        assertEquals(stack.getScope().length, 1);
        assertEquals(stack.isInScope(Namespace.NO_NAMESPACE), true);
    }
    
    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        
        stack.push(element);
        stack.pop();
        
        assertEquals(stack.getScope().length, 1);
        assertEquals(stack.isInScope(Namespace.NO_NAMESPACE), false);
    }
    
    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        
        stack.push(element);
        Iterable<Namespace> added = stack.addedForward();
        
        assertNotNull(added);
    }
    
    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        
        stack.push(element);
        assertNotNull(stack.iterator());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPopEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }
    
    // Add more test cases for edge cases and exceptions as needed
    
}