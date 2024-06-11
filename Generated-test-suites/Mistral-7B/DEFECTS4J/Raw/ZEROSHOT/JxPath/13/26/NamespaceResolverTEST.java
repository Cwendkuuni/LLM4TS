package org.apache.commons.jxpath.ri;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.NamespaceResolver;

/**
 * Tests for {@link NamespaceResolver} class.
 */
public class NamespaceResolverTest {

    @SuppressWarnings("unchecked")
    private NamespaceResolver createResolver(NamespaceResolver parent) {
        return new NamespaceResolver(parent);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getParent());
        assertEquals(0, resolver.getNamespaceMap().size());
        assertEquals(0, resolver.getReverseMap().size());
        assertEquals(false, resolver.isSealed());

        NamespaceResolver parent = new NamespaceResolver();
        NamespaceResolver resolver1 = createResolver(parent);
        assertEquals(parent, resolver1.getParent());
        assertEquals(0, resolver1.getNamespaceMap().size());
        assertEquals(0, resolver1.getReverseMap().size());
        assertEquals(false, resolver1.isSealed());
    }

    /**
     * Test registerNamespace method.
     */
    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        assertEquals("x", resolver.getPrefix("http://example.com/x"));

        resolver.registerNamespace("y", "http://example.com/y");
        assertEquals("http://example.com/y", resolver.getNamespaceURI("y"));
        assertEquals("y", resolver.getPrefix("http://example.com/y"));

        assertEquals(2, resolver.getNamespaceMap().size());
        assertEquals(2, resolver.getReverseMap().size());

        resolver.seal();
        try {
            resolver.registerNamespace("z", "http://example.com/z");
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
    }

    /**
     * Test getNamespaceURI method.
     */
    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertEquals(null, resolver.getNamespaceURI("x"));
        assertEquals(null, resolver.getNamespaceURI("y"));

        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        assertEquals(null, resolver.getNamespaceURI("y"));

        resolver.registerNamespace("y", "http://example.com/y");
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
        assertEquals("http://example.com/y", resolver.getNamespaceURI("y"));

        NamespaceResolver parent = new NamespaceResolver();
        NamespaceResolver resolver1 = createResolver(parent);
        resolver1.registerNamespace("z", "http://example.com/z");
        assertEquals(null, resolver.getNamespaceURI("z"));
        assertEquals("http://example.com/z", resolver1.getNamespaceURI("z"));
    }

    /**
     * Test getPrefix method.
     */
    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertEquals(null, resolver.getPrefix("http://example.com/x"));
        assertEquals(null, resolver.getPrefix("http://example.com/y"));

        resolver.registerNamespace("x", "http://example.com/x");
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
        assertEquals(null, resolver.getPrefix("http://example.com/y"));

        resolver.registerNamespace("y", "http://example.com/y");
        assertEquals("x", resolver.getPrefix("http://example.com/x"));
        assertEquals("y", resolver.getPrefix("http://example.com/y"));

        NamespaceResolver parent = new NamespaceResolver();
        NamespaceResolver resolver1 = createResolver(parent);
        resolver1.registerNamespace("z", "http://example.com/z");
        assertEquals(null, resolver.getPrefix("http://example.com/z"));
        assertEquals("z", resolver1.getPrefix("http://example.com/z"));
    }

    /**
     * Test isSealed method.
     */
    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertEquals(false, resolver.isSealed());

        resolver.seal();
        assertEquals(true, resolver.isSealed());

        NamespaceResolver parent = new NamespaceResolver();
        NamespaceResolver resolver1 = createResolver(parent);
        assertEquals(false, resolver1.isSealed());

        resolver1.seal();
        assertEquals(true, resolver1.isSealed());
        assertEquals(true, parent.isSealed());
    }

    /**
     * Test clone method.
     */
    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/x");
        NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
        assertEquals("http://example.com/x", cloned.getNamespaceURI("x"));

        resolver.registerNamespace("y", "http://example.com/y");
        assertEquals(1, resolver.getNamespaceMap().size());
        assertEquals(1, resolver.getReverseMap().size());
        assertEquals(1, cloned.getNamespaceMap().size());
        assertEquals(1, cloned.getReverseMap().size());

        assertEquals(false, resolver.isSealed());
        assertEquals(false, cloned.isSealed());
    }

    /**
     * Test setNamespaceContextPointer method.
     */
    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertEquals(null, resolver.getNamespaceURI("x"));

        Pointer namespaceContext = new NodePointer("root", new String[]{"x:root"}, null);
        resolver.setNamespaceContextPointer(namespaceContext);
        assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));

        assertEquals(1, resolver.getNamespaceMap().size());
        assertEquals(1, resolver.getReverseMap().size());

        NamespaceResolver parent = new NamespaceResolver();
        NamespaceResolver resolver1 = createResolver(parent);
        parent.registerNamespace("x", "http://example.com/x");
        resolver1.setNamespaceContextPointer(namespaceContext);
        assertEquals("http://example.com/x", resolver1.getNamespaceURI("x"));
        assertEquals(1, resolver1.getNamespaceMap().size());
        assertEquals(1, resolver1.getReverseMap().size());

        assertEquals(false, resolver.isSealed());
        assertEquals(false, resolver1.isSealed());
    }

    /**
     * Test getNamespaceContextPointer method.
     */
    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceContextPointer());

        Pointer namespaceContext = new NodePointer("root", new String[]{"x:root"}, null);
        resolver.setNamespaceContextPointer(namespaceContext);
        assertEquals(namespaceContext, resolver.getNamespaceContextPointer());

        NamespaceResolver parent = new NamespaceResolver();
        NamespaceResolver resolver1 = createResolver(parent);
        parent.registerNamespace("x", "http://example.com/x");
        resolver1.setNamespaceContextPointer(namespaceContext);
        assertEquals(namespaceContext, resolver1.getNamespaceContextPointer());

        NamespaceResolver resolver2 = createResolver(resolver1);
        assertNull(resolver2.getNamespaceContextPointer());
    }

    /**
     * Test learnWhetherThisNamespaceResolverHasBeenSealed method.
     */
    @Test
    public void testLearnWhetherThisNamespaceResolverHasBeenSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertEquals(false, resolver.isSealed());

        resolver.seal();
        assertEquals(true, resolver.isSealed());

        NamespaceResolver parent = new NamespaceResolver();
        NamespaceResolver resolver1 = createResolver(parent);
        resolver1.seal();
        assertEquals(true, resolver1.isSealed());
        assertEquals(true, parent.isSealed());
    }

    /**
     * Test learnWhetherThisNamespaceResolverHasBeenSealed method with cloned resolver.
     */
    @Test
    public void testLearnWhetherThisNamespaceResolverHasBeenSealedWithClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        NamespaceResolver cloned = (NamespaceResolver) resolver.clone();
        assertEquals(true, resolver.isSealed());
        assertEquals(true, cloned.isSealed());
    }
}