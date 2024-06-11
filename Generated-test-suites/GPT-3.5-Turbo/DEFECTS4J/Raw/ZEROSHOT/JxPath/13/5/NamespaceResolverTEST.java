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
        resolver.registerNamespace("prefix2", "uri2");
        assertEquals("uri1", resolver.getNamespaceURI("prefix1"));
        assertEquals("uri2", resolver.getNamespaceURI("prefix2"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix", "uri");
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix", "uri");
        resolver.setNamespaceContextPointer(null);
        assertEquals("uri", resolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetPrefix() {
        resolver.setNamespaceContextPointer(null);
        assertEquals(null, resolver.getPrefix("uri"));
        resolver.registerNamespace("prefix", "uri");
        assertEquals("prefix", resolver.getPrefix("uri"));
    }

    @Test
    public void testIsSealed() {
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        assertNotSame(clone, resolver);
        assertEquals(clone.isSealed(), resolver.isSealed());
    }

}