import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.Pointer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

public class NamespaceResolverTest {

    private static final String PREFIX = "prefix";
    private static final String NAMESPACE_URI = "http://example.com";

    private NamespaceResolver resolver;
    private NamespaceResolver parentResolver;
    private NodePointer pointer;

    @Before
    public void setup() {
        parentResolver = new NamespaceResolver();
        resolver = new NamespaceResolver(parentResolver);
        pointer = new NodePointer();
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace(PREFIX, NAMESPACE_URI);
        assertEquals(NAMESPACE_URI, resolver.getNamespaceURI(PREFIX));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace(PREFIX, NAMESPACE_URI);
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace(PREFIX, NAMESPACE_URI);
        assertEquals(NAMESPACE_URI, resolver.getNamespaceURI(PREFIX));
        assertEquals(NAMESPACE_URI, parentResolver.getNamespaceURI(PREFIX));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace(PREFIX, NAMESPACE_URI);
        assertEquals(PREFIX, resolver.getPrefix(NAMESPACE_URI));
        assertEquals(PREFIX, parentResolver.getPrefix(NAMESPACE_URI));
    }

    @Test
    public void testIsSealed() {
        assertFalse(resolver.isSealed());
        assertFalse(parentResolver.isSealed());

        resolver.seal();
        assertTrue(resolver.isSealed());
        assertTrue(parentResolver.isSealed());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        assertFalse(clone.isSealed());
        assertNotSame(resolver, clone);
    }
}