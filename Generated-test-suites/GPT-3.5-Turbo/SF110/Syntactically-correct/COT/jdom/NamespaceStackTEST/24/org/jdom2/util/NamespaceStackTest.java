
import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceStackTest {
    
    @Test
    public void testConstructor() {
        NamespaceStack stack = new NamespaceStack();
        assertNotNull(stack);
    }
    
    @Test
    public void testPush_Element() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        stack.push(element);
        assertTrue(stack.isInScope(element.getNamespace()));
    }
    
    @Test
    public void testPush_Attribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("test", "value", Namespace.getNamespace("prefix", "uri"));
        stack.push(attribute);
        assertTrue(stack.isInScope(attribute.getNamespace()));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }
    
    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        stack.push(element);
        Iterable<Namespace> added = stack.addedForward();
        assertNotNull(added);
    }
    
    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        stack.push(element);
        Iterable<Namespace> added = stack.addedReverse();
        assertNotNull(added);
    }
    
    @Test
    public void testGetScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        stack.push(element);
        Namespace[] scope = stack.getScope();
        assertNotNull(scope);
        assertEquals(element.getNamespace(), scope[0]);
    }
    
    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        stack.push(element);
        assertTrue(stack.isInScope(element.getNamespace()));
    }
}
