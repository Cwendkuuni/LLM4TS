import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {
    private NamespaceResolver resolver;
    
    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
    }
    
    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix1", "namespaceURI1");
        resolver.registerNamespace("prefix2", "namespaceURI2");
        assertEquals("namespaceURI1", resolver.getNamespaceURI("prefix1"));
        assertEquals("namespaceURI2", resolver.getNamespaceURI("prefix2"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCannotRegisterOnSealedResolver() {
        resolver.seal();
        resolver.registerNamespace("prefix3", "namespaceURI3");
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }
    
    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix4", "namespaceURI4");
        resolver.registerNamespace("prefix5", "namespaceURI5");
        assertEquals("namespaceURI4", resolver.getNamespaceURI("prefix4"));
        assertEquals("namespaceURI5", resolver.getNamespaceURI("prefix5"));
        assertNull(resolver.getNamespaceURI("prefix6"));
    }
    
    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix7", "namespaceURI7");
        resolver.registerNamespace("prefix8", "namespaceURI8");
        assertEquals("prefix7", resolver.getPrefix("namespaceURI7"));
        assertEquals("prefix8", resolver.getPrefix("namespaceURI8"));
        assertNull(resolver.getPrefix("namespaceURI9"));
    }
    
    @Test
    public void testSeal() {
        resolver.seal();
        assertEquals(true, resolver.isSealed());
    }
    
    @Test
    public void testClone() {
        resolver.registerNamespace("prefix10", "namespaceURI10");
        NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
        assertEquals("namespaceURI10", cloned.getNamespaceURI("prefix10"));
        assertEquals(false, cloned.isSealed());
    }
}