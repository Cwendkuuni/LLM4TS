import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NamespaceResolverTest {
    
    private NamespaceResolver resolver;
    
    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
    }
    
    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("prefix1", "uri1");
        assertNotNull(resolver.getNamespaceURI("prefix1"));
        assertEquals("uri1", resolver.getNamespaceURI("prefix1"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix2", "uri2");
    }
    
    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix3", "uri3");
        assertEquals("uri3", resolver.getNamespaceURI("prefix3"));
        assertNull(resolver.getNamespaceURI("prefix4"));
    }
    
    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix5", "uri5");
        assertEquals("prefix5", resolver.getPrefix("uri5"));
        assertNull(resolver.getPrefix("uri6"));
    }
    
    @Test
    public void testIsSealed() {
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }
    
    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertNotSame(resolver, clonedResolver);
        assertEquals(resolver.isSealed(), clonedResolver.isSealed());
    }
}