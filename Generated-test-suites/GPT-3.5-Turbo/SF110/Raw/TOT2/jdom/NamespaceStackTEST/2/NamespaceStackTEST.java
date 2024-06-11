import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceStackTest {
    
    @Test
    public void testNamespaceStackConstructor() {
        NamespaceStack nsStack = new NamespaceStack();
        assertNotNull(nsStack);
        assertTrue(nsStack.getScope().length > 0);
    }
    
    @Test
    public void testPushElement() {
        NamespaceStack nsStack = new NamespaceStack();
        Element element = new Element("test");
        nsStack.push(element);
        assertTrue(nsStack.isInScope(element.getNamespace()));
    }
    
    @Test
    public void testPushAttribute() {
        NamespaceStack nsStack = new NamespaceStack();
        Attribute attribute = new Attribute("test", "value");
        nsStack.push(attribute);
        assertTrue(nsStack.isInScope(attribute.getNamespace()));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPop() {
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.pop();
    }
    
    @Test
    public void testAddedForward() {
        NamespaceStack nsStack = new NamespaceStack();
        assertFalse(nsStack.addedForward().iterator().hasNext());
    }
    
    @Test
    public void testAddedReverse() {
        NamespaceStack nsStack = new NamespaceStack();
        assertFalse(nsStack.addedReverse().iterator().hasNext());
    }

    @Test
    public void testIterator() {
        NamespaceStack nsStack = new NamespaceStack();
        assertFalse(nsStack.iterator().hasNext());
    }
    
    @Test
    public void testIsInScope() {
        NamespaceStack nsStack = new NamespaceStack();
        Namespace ns = new Namespace("test", "http://test.com");
        assertFalse(nsStack.isInScope(ns));
    }
    
    @Test
    public void testEmptyNamespaceComparison() {
        Namespace ns1 = Namespace.NO_NAMESPACE;
        Namespace ns2 = Namespace.XML_NAMESPACE;
        assertEquals(0, ns1.getPrefix().compareTo(ns2.getPrefix()));
    }
}