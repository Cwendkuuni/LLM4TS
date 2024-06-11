import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceStackTest {
    
    @Test
    public void testConstructor() {
        NamespaceStack ns = new NamespaceStack();
        assertNotNull(ns);
        Namespace[] seed = { new Namespace("test", "http://test.com") };
        NamespaceStack ns2 = new NamespaceStack(seed);
        assertNotNull(ns2);
    }
    
    @Test
    public void testPushElement() {
        NamespaceStack ns = new NamespaceStack();
        Element element = new Element("test");
        ns.push(element);
        assertTrue(ns.isInScope(element.getNamespace()));
    }
    
    @Test
    public void testPushAttribute() {
        NamespaceStack ns = new NamespaceStack();
        Attribute attrib = new Attribute("test", "value");
        ns.push(attrib);
        assertTrue(ns.isInScope(attrib.getNamespace()));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testOverPop() {
        NamespaceStack ns = new NamespaceStack();
        ns.pop();
    }
    
    @Test
    public void testIterator() {
        NamespaceStack ns = new NamespaceStack();
        assertNotNull(ns.iterator());
    }
    
    @Test
    public void testAddedForward() {
        NamespaceStack ns = new NamespaceStack();
        assertNotNull(ns.addedForward());
    }
    
    @Test
    public void testAddedReverse() {
        NamespaceStack ns = new NamespaceStack();
        assertNotNull(ns.addedReverse());
    }
    
    @Test
    public void testGetScope() {
        NamespaceStack ns = new NamespaceStack();
        assertNotNull(ns.getScope());
    }
    
    @Test
    public void testIsInScope() {
        NamespaceStack ns = new NamespaceStack();
        Namespace ns1 = new Namespace("test1", "http://test1.com");
        ns.push(new Element("test1"));
        assertTrue(ns.isInScope(ns1));
    }
}