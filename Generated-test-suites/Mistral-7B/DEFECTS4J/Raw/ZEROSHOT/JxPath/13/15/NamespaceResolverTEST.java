package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.NodeIterator;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIteratorTestDouble;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.NodePointerTestDouble;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for NamespaceResolver.
 */
public class NamespaceResolverTest {

    NamespaceResolver resolver;
    NodePointer nodePointer;
    NodeIteratorTestDouble nodeIterator;

    @Before
    public void setup() {
        nodeIterator = new NodeIteratorTestDouble();
        nodePointer = new NodePointerTestDouble();
        resolver = new NamespaceResolver();
    }

    @Test
    public void testNewInstance() {
        NamespaceResolver instance = new NamespaceResolver();
        assertEquals(null, instance.getNamespaceURI(""));
        assertEquals(null, instance.getPrefix(""));
        assertNull(instance.reverseMap);
        assertSame(instance, instance.clone());
    }

    @Test
    public void testNewInstanceWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver instance = new NamespaceResolver(parentResolver);
        assertSame(parentResolver, instance.getParent());
        assertNull(instance.getNamespaceURI(""));
        assertNull(instance.getPrefix(""));
        assertNull(instance.reverseMap);
    }

    @Test
    public void testRegisterNamespace() {
        resolver.registerNamespace("x", "http://example.com/namespace1");
        assertEquals("http://example.com/namespace1", resolver.getNamespaceURI("x"));
        assertEquals("x", resolver.getPrefix("http://example.com/namespace1"));
        assertEquals(1, resolver.reverseMap.size());
        assertSame("x", resolver.reverseMap.get("http://example.com/namespace1"));
        assertSame("http://example.com/namespace1", resolver.reverseMap.get("x"));
        assertNull(resolver.getPrefix("http://example.com/namespace2"));
        assertNull(resolver.getNamespaceURI("y"));

        resolver.registerNamespace("y", "http://example.com/namespace2");
        assertEquals(2, resolver.reverseMap.size());
        assertSame("y", resolver.reverseMap.get("http://example.com/namespace2"));
        assertSame("http://example.com/namespace2", resolver.reverseMap.get("y"));

        assertEquals(2, resolver.namespaceMap.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        resolver.registerNamespace("x", "http://example.com/namespace1");
        resolver.seal();
        resolver.registerNamespace("y", "http://example.com/namespace2");
    }

    @Test
    public void testRegisterNamespaceWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        parentResolver.registerNamespace("x", "http://example.com/namespace1");
        NamespaceResolver instance = new NamespaceResolver(parentResolver);
        assertEquals("http://example.com/namespace1", instance.getNamespaceURI("x"));
        assertEquals("x", instance.getPrefix("http://example.com/namespace1"));
        assertEquals(2, instance.reverseMap.size());
        assertSame("x", instance.reverseMap.get("http://example.com/namespace1"));
        assertSame("http://example.com/namespace1", instance.reverseMap.get("x"));
        assertNull(instance.getPrefix("http://example.com/namespace2"));
        assertNull(instance.getNamespaceURI("y"));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        resolver.setNamespaceContextPointer(nodePointer);
        assertEquals(nodePointer, resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceContextPointer() {
        resolver.setNamespaceContextPointer(nodePointer);
        assertEquals(nodePointer, resolver.getNamespaceContextPointer());
        assertNull(resolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("x", "http://example.com/namespace1");
        assertEquals("http://example.com/namespace1", resolver.getNamespaceURI("x"));
        assertEquals(null, resolver.getNamespaceURI("y"));
    }

    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("x", "http://example.com/namespace1");
        assertEquals("x", resolver.getPrefix("http://example.com/namespace1"));
        assertEquals(null, resolver.getPrefix("http://example.com/namespace2"));
    }

    @Test
    public void testSeal() {
        resolver.registerNamespace("x", "http://example.com/namespace1");
        resolver.seal();
        assertFalse(resolver.isSealed());
        resolver.registerNamespace("y", "http://example.com/namespace2");
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver original = new NamespaceResolver();
        original.registerNamespace("x", "http://example.com/namespace1");
        NamespaceResolver clone = (NamespaceResolver) original.clone();
        assertEquals("http://example.com/namespace1", clone.getNamespaceURI("x"));
        assertEquals(original.reverseMap, clone.reverseMap);
        assertSame(original, original.getParent());
        assertNull(clone.getParent());
    }

    @Test
    public void testNamespaceIterator() {
        resolver.registerNamespace("x", "http://example.com/namespace1");
        resolver.registerNamespace("y", "http://example.com/namespace2");
        NodePointer nodePointer = new NodePointerTestDouble();
        nodePointer.setNamespaceURI("http://example.com/namespace1");
        nodePointer.setName("node1");
        NodePointer nodePointer2 = new NodePointerTestDouble();
        nodePointer2.setNamespaceURI("http://example.com/namespace2");
        nodePointer2.setName("node2");
        NodeIteratorTestDouble nodeIteratorTestDouble = new NodeIteratorTestDouble();
        nodeIteratorTestDouble.setNodePointer(nodePointer);
        nodeIteratorTestDouble.setNextNodePointer(nodePointer2);
        nodeIterator = nodeIteratorTestDouble;
        resolver.setNamespaceContextPointer(nodePointer);
        assertEquals(nodePointer, resolver.getNamespaceContextPointer());
        Map<String, String> namespaces = new HashMap<>();
        namespaces.put("x", "http://example.com/namespace1");
        namespaces.put("y", "http://example.com/namespace2");
        Iterator<Map.Entry<String, String>> iterator = namespaces.entrySet().iterator();
        int position = 0;
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String prefix = entry.getKey();
            String namespaceURI = entry.getValue();
            String expected = resolver.getNamespaceURI(prefix);
            assertEquals(namespaceURI, expected);
            String actual = resolver.getPrefix(namespaceURI);
            assertEquals(prefix, actual);
            NodePointer nodePointer = resolver.getNamespaceContextPointer();
            NodePointer expectedNodePointer = nodeIterator.getNodePointer(position);
            assertEquals(expectedNodePointer, nodePointer);
            position++;
        }
        assertNull(resolver.getNamespaceURI("z"));
        assertNull(resolver.getPrefix("http://example.com/namespace3"));
        assertNull(resolver.getNamespaceContextPointer());
    }
}