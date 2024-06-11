// Junit 4 test file for NamespaceResolver

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NamespaceResolverTest {

    // initialize the NamespaceResolver object
    private NamespaceResolver namespaceResolver;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("prefix", "http://www.namespace.com");
        String namespaceURI = namespaceResolver.getNamespaceURI("prefix");
        assertEquals("http://www.namespace.com", namespaceURI);
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespace_SealedResolver() {
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("prefix", "http://www.namespace.com");
    }

    @Test
    public void testGetNamespaceURI_RegisteredPrefix() {
        namespaceResolver.registerNamespace("prefix", "http://www.namespace.com");
        String namespaceURI = namespaceResolver.getNamespaceURI("prefix");
        assertEquals("http://www.namespace.com", namespaceURI);
    }

    @Test
    public void testGetNamespaceURI_UndefinedPrefix() {
        String namespaceURI = namespaceResolver.getNamespaceURI("prefix");
        assertNull(namespaceURI);
    }

    @Test
    public void testGetNamespaceURI_ExternalPrefix() {
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("prefix", "http://www.namespace.com");
        namespaceResolver.setNamespaceContextPointer(pointer);
        String namespaceURI = namespaceResolver.getNamespaceURI("prefix");
        assertEquals("http://www.namespace.com", namespaceURI);
    }

    @Test
    public void testGetNamespaceURI_ParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "http://www.namespace.com");
        namespaceResolver = new NamespaceResolver(parentResolver);
        String namespaceURI = namespaceResolver.getNamespaceURI("prefix");
        assertEquals("http://www.namespace.com", namespaceURI);
    }

    @Test
    public void testGetPrefix_RegisteredNamespaceURI() {
        namespaceResolver.registerNamespace("prefix", "http://www.namespace.com");
        String prefix = namespaceResolver.getPrefix("http://www.namespace.com");
        assertEquals("prefix", prefix);
    }

    @Test
    public void testGetPrefix_UndefinedNamespaceURI() {
        String prefix = namespaceResolver.getPrefix("http://www.namespace.com");
        assertNull(prefix);
    }

    @Test
    public void testGetPrefix_ExternalNamespaceURI() {
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("prefix", "http://www.namespace.com");
        namespaceResolver.setNamespaceContextPointer(pointer);
        String prefix = namespaceResolver.getPrefix("http://www.namespace.com");
        assertEquals("prefix", prefix);
    }

    @Test
    public void testGetPrefix_ParentResolver() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "http://www.namespace.com");
        namespaceResolver = new NamespaceResolver(parentResolver);
        String prefix = namespaceResolver.getPrefix("http://www.namespace.com");
        assertEquals("prefix", prefix);
    }

    @Test
    public void testIsSealed() {
        assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testSeal() {
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver cloneResolver = (NamespaceResolver) namespaceResolver.clone();
        assertNotSame(namespaceResolver, cloneResolver);
        assertFalse(cloneResolver.isSealed());
    }
}