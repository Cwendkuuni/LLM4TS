import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {

    private NamespaceResolver resolver;

    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
        resolver.registerNamespace("p1", "http://example.com/prefix1");
        resolver.registerNamespace("p2", "http://example.com/prefix2");
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("p3", "http://example.com/prefix3");
        assertEquals("http://example.com/prefix3", resolver.getNamespaceURI("p3"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        resolver.seal();
        resolver.registerNamespace("p4", "http://example.com/prefix4");
    }

    @Test
    public void testGetNamespaceURI() {
        assertEquals("http://example.com/prefix1", resolver.getNamespaceURI("p1"));
        assertNull(resolver.getNamespaceURI("p3"));
    }

    @Test
    public void testGetPrefix() {
        assertEquals("p1", resolver.getPrefix("http://example.com/prefix1"));
        assertNull(resolver.getPrefix("http://example.com/prefix3"));
    }

    @Test
    public void testGetPrefixInParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("p5", "http://example.com/prefix5");
        resolver = new NamespaceResolver(parentResolver);
        assertEquals("p5", resolver.getPrefix("http://example.com/prefix5"));
    }

    @Test
    public void testGetNamespaceContextPointer() {
        Pointer pointer = JXPathContext.newContext(new Object()).getPointer("/");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceContextPointerInParentResolver() {
        Pointer pointer = JXPathContext.newContext(new Object()).getPointer("/");
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.setNamespaceContextPointer(pointer);
        resolver = new NamespaceResolver(parentResolver);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testSeal() {
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals(resolver.getNamespaceURI("p1"), clonedResolver.getNamespaceURI("p1"));
        assertEquals(resolver.getNamespaceURI("p2"), clonedResolver.getNamespaceURI("p2"));
    }

    @Test
    public void testCloneSealedResolver() {
        resolver.seal();
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertFalse(clonedResolver.isSealed());
    }

    @Test
    public void testReverseMap() {
        Pointer pointer = JXPathContext.newContext(new Object()).getPointer("/");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("p1", resolver.getPrefix("http://example.com/prefix1"));
        assertEquals("p2", resolver.getPrefix("http://example.com/prefix2"));
    }
}