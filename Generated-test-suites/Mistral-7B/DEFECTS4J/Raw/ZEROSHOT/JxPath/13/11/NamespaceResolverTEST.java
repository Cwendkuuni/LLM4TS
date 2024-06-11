package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NamespaceContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for NamespaceResolver.
 */
public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;
    private NamespaceResolver namespaceResolverParent;
    private NodePointer nodePointer;
    private Pointer pointer;

    @Before
    public void setUp() {
        namespaceResolverParent = new NamespaceResolver();
        namespaceResolver = new NamespaceResolver(namespaceResolverParent);
        nodePointer = new NodePointer();
        pointer = new Pointer();
    }

    @After
    public void tearDown() {
        namespaceResolver = null;
        namespaceResolverParent = null;
        nodePointer = null;
        pointer = null;
    }

    @Test
    public void testCreateNewNamespaceResolver() {
        // Default constructor
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        assertNull(namespaceResolver.getParent());
    }

    @Test
    public void testCreateNewNamespaceResolverWithParent() {
        NamespaceResolver namespaceResolver = new NamespaceResolver(namespaceResolverParent);
        assertEquals(namespaceResolverParent, namespaceResolver.getParent());
    }

    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("test:prefix", "test:namespaceURI");
        String prefix = namespaceResolver.getNamespaceURI("test:prefix");
        assertEquals("test:namespaceURI", prefix);
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedNamespaceResolver() {
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("test:prefix", "test:namespaceURI");
    }

    @Test
    public void testGetNamespaceURI() {
        namespaceResolver.registerNamespace("test:prefix", "test:namespaceURI");
        String prefix = namespaceResolver.getNamespaceURI("test:prefix");
        assertEquals("test:namespaceURI", prefix);
        String prefix2 = namespaceResolver.getNamespaceURI("test2:prefix");
        assertNull(prefix2);
    }

    @Test
    public void testGetNamespaceURIWithParent() {
        namespaceResolverParent.registerNamespace("parent:prefix", "parent:namespaceURI");
        namespaceResolver.registerNamespace("test:prefix", "test:namespaceURI");
        String prefix = namespaceResolver.getNamespaceURI("test:prefix");
        assertEquals("test:namespaceURI", prefix);
        String prefixParent = namespaceResolver.getNamespaceURI("parent:prefix");
        assertEquals("parent:namespaceURI", prefixParent);
    }

    @Test
    public void testGetPrefix() {
        namespaceResolver.registerNamespace("test:prefix", "test:namespaceURI");
        String prefix = namespaceResolver.getPrefix("test:namespaceURI");
        assertEquals("test:prefix", prefix);
        String prefix2 = namespaceResolver.getPrefix("test2:namespaceURI");
        assertNull(prefix2);
    }

    @Test
    public void testGetPrefixWithParent() {
        namespaceResolverParent.registerNamespace("parent:prefix", "parent:namespaceURI");
        namespaceResolver.registerNamespace("test:prefix", "test:namespaceURI");
        String prefix = namespaceResolver.getPrefix("test:namespaceURI");
        assertEquals("test:prefix", prefix);
        String prefixParent = namespaceResolver.getPrefix("parent:namespaceURI");
        assertEquals("parent:prefix", prefixParent);
    }

    @Test
    public void testIsSealed() {
        namespaceResolver = new NamespaceResolver();
        assertFalse(namespaceResolver.isSealed());
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver original = new NamespaceResolver();
        NamespaceResolver clone = (NamespaceResolver) original.clone();
        assertFalse(original.equals(clone));
        assertEquals(original.getParent(), clone.getParent());
        assertEquals(original.getNamespaceMap(), clone.getNamespaceMap());
        assertEquals(original.isSealed(), clone.isSealed());
    }

    @Test
    public void testGetNamespaceMap() {
        Map<String, String> namespaceMap = new HashMap<String, String>();
        namespaceMap.put("test:prefix", "test:namespaceURI");
        namespaceResolver.setNamespaceMap(namespaceMap);
        Map<String, String> namespaceMapResult = namespaceResolver.getNamespaceMap();
        assertEquals(namespaceMap, namespaceMapResult);
    }

    @Test
    public void testSetNamespaceContextPointer() {
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        Pointer result = namespaceResolver.getNamespaceContextPointer();
        assertEquals(nodePointer, result);
        namespaceResolver.setNamespaceContextPointer(pointer);
        result = namespaceResolver.getNamespaceContextPointer();
        assertEquals(pointer, result);
    }

    @Test
    public void testGetNamespaceContextPointer() {
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        Pointer result = namespaceResolver.getNamespaceContextPointer();
        assertEquals(nodePointer, result);
        namespaceResolver.setNamespaceContextPointer(pointer);
        result = namespaceResolver.getNamespaceContextPointer();
        assertEquals(pointer, result);
    }

    @Test
    public void testSetNamespaceContextPointerWithParent() {
        namespaceResolverParent.setNamespaceContextPointer(nodePointer);
        namespaceResolver.setNamespaceContextPointer(pointer);
        Pointer result = namespaceResolver.getNamespaceContextPointer();
        assertEquals(pointer, result);
        Pointer resultParent = namespaceResolverParent.getNamespaceContextPointer();
        assertEquals(nodePointer, resultParent);
    }
}