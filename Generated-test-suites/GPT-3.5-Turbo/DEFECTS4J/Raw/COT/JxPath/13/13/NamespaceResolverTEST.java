import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace_validPrefixAndNamespace() {
        namespaceResolver.registerNamespace("prefix", "http://example.com");
        assertEquals("http://example.com", namespaceResolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testRegisterNamespace_nullPrefixWithValidNamespace() {
        namespaceResolver.registerNamespace(null, "http://example.com");
        assertEquals("http://example.com", namespaceResolver.getNamespaceURI(null));
    }

    @Test
    public void testRegisterNamespace_validPrefixWithNullNamespace() {
        namespaceResolver.registerNamespace("prefix", null);
        assertNull(namespaceResolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testRegisterNamespace_blankPrefixWithValidNamespace() {
        namespaceResolver.registerNamespace("", "http://example.com");
        assertEquals("http://example.com", namespaceResolver.getNamespaceURI(""));
    }

    @Test
    public void testRegisterNamespace_validPrefixWithBlankNamespace() {
        namespaceResolver.registerNamespace("prefix", "");
        assertEquals("", namespaceResolver.getNamespaceURI("prefix"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespace_duplicatePrefixDifferentNamespace() {
        namespaceResolver.registerNamespace("prefix", "http://example.com");
        namespaceResolver.registerNamespace("prefix", "http://example.org");
    }

    @Test
    public void testSetAndGetNamespaceContextPointer_validPointer() {
        NodePointer pointer = new NodePointer();
        namespaceResolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, namespaceResolver.getNamespaceContextPointer());
    }

    @Test
    public void testSetAndGetNamespaceContextPointer_nullPointer() {
        namespaceResolver.setNamespaceContextPointer(null);
        assertNull(namespaceResolver.getNamespaceContextPointer());
    }

    @Test
    public void testSetAndGetNamespaceContextPointer_withNamespace() {
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("http://example.com");
        namespaceResolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, namespaceResolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI_registeredPrefix() {
        namespaceResolver.registerNamespace("prefix", "http://example.com");
        assertEquals("http://example.com", namespaceResolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetNamespaceURI_prefixNotRegistered() {
        assertNull(namespaceResolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetNamespaceURI_prefixNotRegisteredInCurrentResolver_butRegisteredInParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "http://example.com");
        namespaceResolver = new NamespaceResolver(parentResolver);
        assertNull(namespaceResolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetNamespaceURI_prefixNotRegisteredInCurrentResolver_norParentResolver() {
        assertNull(namespaceResolver.getNamespaceURI("prefix"));
    }

    @Test
    public void testGetPrefix_registeredNamespaceURI() {
        namespaceResolver.registerNamespace("prefix", "http://example.com");
        assertEquals("prefix", namespaceResolver.getPrefix("http://example.com"));
    }

    @Test
    public void testGetPrefix_namespaceURINotRegistered() {
        assertNull(namespaceResolver.getPrefix("http://example.com"));
    }

    @Test
    public void testGetPrefix_namespaceURINotRegisteredInCurrentResolver_butRegisteredInParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("prefix", "http://example.com");
        namespaceResolver = new NamespaceResolver(parentResolver);
        assertNull(namespaceResolver.getPrefix("http://example.com"));
    }

    @Test
    public void testGetPrefix_namespaceURINotRegisteredInCurrentResolver_norParentResolver() {
        assertNull(namespaceResolver.getPrefix("http://example.com"));
    }

    @Test
    public void testIsSealed_true() {
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testIsSealed_false() {
        assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testSeal_unsealedNamespaceResolver() {
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testSeal_sealedNamespaceResolver() {
        namespaceResolver.seal();
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) namespaceResolver.clone();
        assertEquals(namespaceResolver.isSealed(), clonedResolver.isSealed());
    }

    @Test
    public void testClone_unsealedClone() {
        NamespaceResolver clonedResolver = (NamespaceResolver) namespaceResolver.clone();
        assertFalse(clonedResolver.isSealed());
    }

}