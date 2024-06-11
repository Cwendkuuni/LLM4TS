import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.jdom2.Namespace;
import java.util.List;
import org.jdom2.Element;
import org.jdom2.Attribute;

public class NamespaceStackTest {

    // Test constructor with DEFAULTSEED argument
    @Test
    public void testConstructorWithDefaultSeed() {
        NamespaceStack ns = new NamespaceStack();
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expected, ns.getScope());
    }
    
    // Test constructor with seed argument
    @Test
    public void testConstructorWithSeed() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        Namespace[] seed = {ns1, ns2};
        NamespaceStack ns = new NamespaceStack(seed);
        Namespace[] expected = {ns1, ns2};
        assertArrayEquals(expected, ns.getScope());
    }
    
    // Test push method with Element argument
    @Test
    public void testPushWithElement() {
        Namespace ns1 = Namespace.NO_NAMESPACE;
        Namespace ns2 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns3 = Namespace.getNamespace("prefix2", "uri2");
        Namespace[] seed = {ns1};
        Element element = new Element("test", ns2);
        element.addNamespaceDeclaration(ns3);
        NamespaceStack ns = new NamespaceStack(seed);
        ns.push(element);
        Namespace[] expected = {ns3, ns2, ns1};
        assertArrayEquals(expected, ns.getScope());
    }
    
    // Test push method with Attribute argument
    @Test
    public void testPushWithAttribute() {
        Namespace ns1 = Namespace.NO_NAMESPACE;
        Namespace ns2 = Namespace.getNamespace("prefix1", "uri1");
        Namespace[] seed = {ns1};
        Attribute attribute = new Attribute("test", "value", ns2);
        NamespaceStack ns = new NamespaceStack(seed);
        ns.push(attribute);
        Namespace[] expected = {ns2, ns1};
        assertArrayEquals(expected, ns.getScope());
    }
    
    // Test pop method
    @Test
    public void testPop() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        Namespace[] seed = {ns1, ns2};
        NamespaceStack ns = new NamespaceStack(seed);
        ns.pop();
        Namespace[] expected = {ns1};
        assertArrayEquals(expected, ns.getScope());
    }
    
    // Test addedForward method with empty added array
    @Test
    public void testAddedForwardWithEmptyAddedArray() {
        NamespaceStack ns = new NamespaceStack();
        assertEquals(NamespaceStack.EMPTYITER, ns.addedForward());
    }
    
    // Test addedForward method with non-empty added array
    @Test
    public void testAddedForwardWithNonEmptyAddedArray() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        Namespace[] seed = {ns1, ns2};
        NamespaceStack ns = new NamespaceStack(seed);
        Iterable<Namespace> iter = ns.addedForward();
        Iterator<Namespace> itr = iter.iterator();
        assertEquals(ns2, itr.next());
        assertEquals(ns1, itr.next());
        assertFalse(itr.hasNext());
    }
    
    // Test addedReverse method with empty added array
    @Test
    public void testAddedReverseWithEmptyAddedArray() {
        NamespaceStack ns = new NamespaceStack();
        assertEquals(NamespaceStack.EMPTYITER, ns.addedReverse());
    }
    
    // Test addedReverse method with non-empty added array
    @Test
    public void testAddedReverseWithNonEmptyAddedArray() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        Namespace[] seed = {ns1, ns2};
        NamespaceStack ns = new NamespaceStack(seed);
        Iterable<Namespace> iter = ns.addedReverse();
        Iterator<Namespace> itr = iter.iterator();
        assertEquals(ns1, itr.next());
        assertEquals(ns2, itr.next());
        assertFalse(itr.hasNext());
    }
    
    // Test iterator method
    @Test
    public void testIterator() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        Namespace[] seed = {ns1, ns2};
        NamespaceStack ns = new NamespaceStack(seed);
        Iterable<Namespace> iter = ns.iterator();
        Iterator<Namespace> itr = iter.iterator();
        assertEquals(ns1, itr.next());
        assertEquals(ns2, itr.next());
        assertFalse(itr.hasNext());
    }
    
    // Test getScope method
    @Test
    public void testGetScope() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        Namespace[] seed = {ns1, ns2};
        NamespaceStack ns = new NamespaceStack(seed);
        Namespace[] expected = {ns1, ns2};
        assertArrayEquals(expected, ns.getScope());
    }
    
    // Test isInScope method with namespace in scope
    @Test
    public void testIsInScopeWithNamespaceInScope() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        Namespace[] seed = {ns1, ns2};
        NamespaceStack ns = new NamespaceStack(seed);
        assertTrue(ns.isInScope(ns1));
    }
    
    // Test isInScope method with namespace not in scope
    @Test
    public void testIsInScopeWithNamespaceNotInScope() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        Namespace ns3 = Namespace.getNamespace("prefix3", "uri3");
        Namespace[] seed = {ns1, ns2};
        NamespaceStack ns = new NamespaceStack(seed);
        assertFalse(ns.isInScope(ns3));
    }  
}