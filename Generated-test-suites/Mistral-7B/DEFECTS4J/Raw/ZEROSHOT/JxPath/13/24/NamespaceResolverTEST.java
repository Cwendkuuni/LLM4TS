package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link NamespaceResolver}.
 */
public class NamespaceResolverTest {

    private NamespaceResolver namespaceResolver;
    private NamespaceResolver parentNamespaceResolver;
    private NodePointer nodePointer;

    @Before
    public void setUp() {
        namespaceResolver = new NamespaceResolver();
        parentNamespaceResolver = new NamespaceResolver(namespaceResolver);
        nodePointer = new NodePointer("root", null);
    }

    @Test
    public void testCreateNewNamespaceResolver() {
        assertNull(namespaceResolver.getParent());
        assertNotNull(namespaceResolver.getNamespaceMap());
        assertNotNull(namespaceResolver.getReverseMap());
        assertNull(namespaceResolver.getPointer());
        assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testCreateNewNamespaceResolverWithParent() {
        assertSame(namespaceResolver, namespaceResolver.getParent());
        assertNull(parentNamespaceResolver.getParent());
        assertNotNull(namespaceResolver.getNamespaceMap());
        assertNotNull(namespaceResolver.getReverseMap());
        assertNull(namespaceResolver.getPointer());
        assertFalse(namespaceResolver.isSealed());
    }

    @Test
    public void testRegisterNamespace() {
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceResolver.getNamespaceURI("xsi"));
        assertEquals("xsi", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));

        namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));
        assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));

        namespaceResolver.registerNamespace("prefix1", "http://example.com/namespace1");
        assertEquals("http://example.com/namespace1", namespaceResolver.getNamespaceURI("prefix1"));
        assertEquals("prefix1", namespaceResolver.getPrefix("http://example.com/namespace1"));
    }

    @Test(expected = IllegalStateException.class)
    public void testCannotRegisterNamespaceOnSealedNamespaceresolver() {
        namespaceResolver.seal();
        namespaceResolver.registerNamespace("prefix2", "http://example.com/namespace2");
    }

    @Test
    public void testGetNamespaceURI() {
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", namespaceResolver.getNamespaceURI("xsi"));
        assertNull(namespaceResolver.getNamespaceURI("prefix1"));

        NodePointer nodePointer = new NodePointer("node", namespaceResolver);
        nodePointer.setNamespaceURI("http://example.com/namespace3", "prefix3");
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        assertEquals("http://example.com/namespace3", namespaceResolver.getNamespaceURI("prefix3"));
        assertNull(namespaceResolver.getNamespaceURI("xsi"));
    }

    @Test
    public void testGetPrefix() {
        namespaceResolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("xsi", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
        assertNull(namespaceResolver.getPrefix("prefix1"));

        NodePointer nodePointer = new NodePointer("node", namespaceResolver);
        nodePointer.setNamespaceURI("http://example.com/namespace3", "prefix3");
        namespaceResolver.setNamespaceContextPointer(nodePointer);
        assertEquals("prefix3", namespaceResolver.getPrefix("http://example.com/namespace3"));
        assertNull(namespaceResolver.getPrefix("xsi"));
    }

    @Test
    public void testLearnWhetherSealed() {
        assertFalse(namespaceResolver.isSealed());
        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
        assertFalse(parentNamespaceResolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver clonedNamespaceResolver = (NamespaceResolver) namespaceResolver.clone();
        assertNull(clonedNamespaceResolver.getParent());
        assertSame(namespaceResolver.getNamespaceMap(), clonedNamespaceResolver.getNamespaceMap());
        assertSame(namespaceResolver.getReverseMap(), clonedNamespaceResolver.getReverseMap());
        assertSame(namespaceResolver.getPointer(), clonedNamespaceResolver.getPointer());
        assertFalse(clonedNamespaceResolver.isSealed());
    }
}