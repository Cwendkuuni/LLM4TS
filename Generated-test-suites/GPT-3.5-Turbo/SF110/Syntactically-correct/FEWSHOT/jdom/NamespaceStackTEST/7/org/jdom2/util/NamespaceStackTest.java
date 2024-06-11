
package org.jdom2.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jdom2.Namespace;
import org.junit.Before;
import org.junit.Test;

public class NamespaceStackTest {
    
    private NamespaceStack nsStack;
    private Namespace[] seed;
    
    @Before
    public void setUp() {
        seed = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE };
        nsStack = new NamespaceStack(seed);
    }
    
    @Test
    public void testPush_Element() {
        Element element = new Element("testElement", "http://test.com");
        Namespace expectedMns = element.getNamespace();
        nsStack.push(element);
        NamespaceStack.NamespaceIterable addedForward = (NamespaceStack.NamespaceIterable) nsStack.addedForward();
        NamespaceStack.NamespaceIterable addedReverse = (NamespaceStack.NamespaceIterable) nsStack.addedReverse();
        assertTrue(nsStack.isInScope(expectedMns));
        assertEquals(seed.length + 1, nsStack.getScope().length);
        assertTrue(addedForward.iterator().hasNext());
        assertTrue(addedReverse.iterator().hasNext());
        assertEquals(expectedMns, addedForward.iterator().next());
        assertEquals(expectedMns, addedReverse.iterator().next());
    }
    
    @Test
    public void testPush_Attribute() {
        Attribute attribute = new Attribute("testAttribute", "value", Namespace.getNamespace("http://test.com"));
        Namespace expectedMns = attribute.getNamespace();
        nsStack.push(attribute);
        NamespaceStack.NamespaceIterable addedForward = (NamespaceStack.NamespaceIterable) nsStack.addedForward();
        NamespaceStack.NamespaceIterable addedReverse = (NamespaceStack.NamespaceIterable) nsStack.addedReverse();
        assertTrue(nsStack.isInScope(expectedMns));
        assertEquals(seed.length + 1, nsStack.getScope().length);
        assertTrue(addedForward.iterator().hasNext());
        assertTrue(addedReverse.iterator().hasNext());
        assertEquals(expectedMns, addedForward.iterator().next());
        assertEquals(expectedMns, addedReverse.iterator().next());
    }
    
    @Test
    public void testPop() {
        nsStack.pop();
        Namespace[] emptyScope = nsStack.getScope();
        assertEquals(0, emptyScope.length);
        assertFalse(nsStack.addedForward().iterator().hasNext());
        assertFalse(nsStack.addedReverse().iterator().hasNext());
    }
    
    @Test
    public void testIterator() {
        Iterator<Namespace> iterator = nsStack.iterator();
        assertTrue(iterator.hasNext());
        assertArrayEquals(seed, nsStack.getScope());
        for (Namespace ns : seed)  {
            assertEquals(ns, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIsInScope() {
        Namespace nsIn = Namespace.NO_NAMESPACE;
        assertTrue(nsStack.isInScope(nsIn));
        Namespace nsNotIn = Namespace.getNamespace("http://another.com");
        assertFalse(nsStack.isInScope(nsNotIn));
    }
    
}
