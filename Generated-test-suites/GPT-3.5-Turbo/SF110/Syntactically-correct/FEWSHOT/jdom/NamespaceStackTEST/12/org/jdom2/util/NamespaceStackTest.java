import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceStackTest {
    
    @Test
    public void testConstructor() {
        NamespaceStack stack = new NamespaceStack();
        assertNotNull(stack);
        Namespace[] scope = stack.getScope();
        assertEquals(2, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(Namespace.XML_NAMESPACE, scope[1]);
    }
    
    @Test
    public void testPush_withElement() {
        Element element = new Element("test", "http://www.test.com");
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        Namespace[] scope = stack.getScope();
        assertEquals(3, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(Namespace.XML_NAMESPACE, scope[1]);
        assertEquals("http://www.test.com", scope[2].getURI());
    }
    
    @Test
    public void testPush_withAttribute() {
        Attribute attribute = new Attribute("test", "value", Namespace.getNamespace("http://www.test.com"));
        NamespaceStack stack = new NamespaceStack();
        stack.push(attribute);
        Namespace[] scope = stack.getScope();
        assertEquals(3, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(Namespace.XML_NAMESPACE, scope[1]);
        assertEquals("http://www.test.com", scope[2].getURI());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPop_withEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }
    
    @Test
    public void testPop_withNonEmptyStack() {
        Element element = new Element("test1", "http://www.test1.com");
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        stack.pop();
        Namespace[] scope = stack.getScope();
        assertEquals(2, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(Namespace.XML_NAMESPACE, scope[1]);
    }
    
    @Test
    public void testAddedForward_withEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Iterable<Namespace> iterable = stack.addedForward();
        assertNull(iterable.iterator().next());
    }
    
    @Test
    public void testAddedForward_withNonEmptyStack() {
        Element element = new Element("test1", "http://www.test1.com");
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        Iterable<Namespace> iterable = stack.addedForward();
        assertEquals("http://www.test1.com", iterable.iterator().next().getURI());
    }
    
    @Test
    public void testAddedReverse_withEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Iterable<Namespace> iterable = stack.addedReverse();
        assertNull(iterable.iterator().next());
    }
    
    @Test
    public void testAddedReverse_withNonEmptyStack() {
        Element element = new Element("test1", "http://www.test1.com");
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        Iterable<Namespace> iterable = stack.addedReverse();
        assertEquals("http://www.test1.com", iterable.iterator().next().getURI());
    }
    
    @Test
    public void testIterator_withEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        assertEquals(Namespace.NO_NAMESPACE, stack.iterator().next());
    }
    
    @Test
    public void testIterator_withNonEmptyStack() {
        Element element = new Element("test1", "http://www.test1.com");
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        Namespace ns1 = stack.iterator().next();
        Namespace ns2 = stack.iterator().next();
        assertEquals(Namespace.NO_NAMESPACE, ns1);
        assertEquals("http://www.test1.com", ns2.getURI());
    }
    
    @Test
    public void testGetScope() {
        NamespaceStack stack = new NamespaceStack();
        Namespace[] scope = stack.getScope();
        assertEquals(2, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(Namespace.XML_NAMESPACE, scope[1]);
    }
    
    @Test
    public void testIsInScope_withMatchingNamespace() {
        Element element = new Element("test1", "http://www.test1.com");
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        assertTrue(stack.isInScope(Namespace.getNamespace("http://www.test1.com")));
    }
    
    @Test
    public void testIsInScope_withoutMatchingNamespace() {
        Element element = new Element("test1", "http://www.test1.com");
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        assertFalse(stack.isInScope(Namespace.getNamespace("http://www.test2.com")));
    }
}