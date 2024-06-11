import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class NamespaceStackTest {
    
    private NamespaceStack stack;
    
    @Before
    public void setUp() {
        stack = new NamespaceStack();
    }
    
    @After
    public void tearDown() {
        stack = null;
    }
    
    @Test
    public void testPush_Element_WithAdditionalNamespaces() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        element.addNamespaceDeclaration(ns1);
        element.addNamespaceDeclaration(ns2);
        
        stack.push(element);
        
        assertEquals(3, stack.depth);
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE}, stack.added[0]);
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE}, stack.scope[0]);
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, ns1, ns2}, stack.added[1]);
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, ns1, ns2}, stack.scope[1]);
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, ns1, ns2}, stack.added[2]);
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, ns1, ns2}, stack.scope[2]);
    }
    
    @Test
    public void testPush_Element_WithAttributes() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        Attribute attribute = new Attribute("attr", "value", Namespace.getNamespace("prefix", "uri"));
        element.setAttribute(attribute);
        
        stack.push(element);
        
        assertEquals(2, stack.depth);
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE}, stack.added[0]);
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE}, stack.scope[0]);
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, attribute.getNamespace()}, stack.added[1]);
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, attribute.getNamespace()}, stack.scope[1]);
    }
    
    @Test
    public void testPush_Attribute() {
        Attribute attribute = new Attribute("attr", "value", Namespace.getNamespace("prefix", "uri"));
        
        stack.push(attribute);
        
        assertEquals(1, stack.depth);
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, attribute.getNamespace()}, stack.added[0]);
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, attribute.getNamespace()}, stack.scope[0]);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPop_EmptyStack() {
        stack.pop();
    }
    
    @Test
    public void testPop() {
        Element element1 = new Element("test1", Namespace.NO_NAMESPACE);
        Element element2 = new Element("test2", Namespace.NO_NAMESPACE);
        stack.push(element1);
        stack.push(element2);
        
        stack.pop();
        
        assertEquals(1, stack.depth);
        assertNull(stack.scope[1]);
        assertNull(stack.added[1]);
    }
    
    @Test
    public void testAddedForward_EmptyAddedStack() {
        stack.depth = 1;
        stack.added[1] = new Namespace[0];
        
        Iterable<Namespace> result = stack.addedForward();
        
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
    }
    
    @Test
    public void testAddedForward() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        stack.depth = 1;
        stack.added[1] = new Namespace[]{Namespace.NO_NAMESPACE, ns1, ns2};
        
        Iterable<Namespace> result = stack.addedForward();
        Iterator<Namespace> iterator = result.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ns1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ns2, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testAddedReverse_EmptyAddedStack() {
        stack.depth = 1;
        stack.added[1] = new Namespace[0];
        
        Iterable<Namespace> result = stack.addedReverse();
        
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
    }
    
    @Test
    public void testAddedReverse() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        stack.depth = 1;
        stack.added[1] = new Namespace[]{Namespace.NO_NAMESPACE, ns1, ns2};
        
        Iterable<Namespace> result = stack.addedReverse();
        Iterator<Namespace> iterator = result.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(ns2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ns1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIterator() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        stack.depth = 1;
        stack.scope[1] = new Namespace[]{Namespace.NO_NAMESPACE, ns1, ns2};
        
        Iterator<Namespace> iterator = stack.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ns1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ns2, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testGetScope() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        stack.depth = 1;
        stack.scope[1] = new Namespace[]{Namespace.NO_NAMESPACE, ns1, ns2};
        
        Namespace[] result = stack.getScope();
        
        assertArrayEquals(new Namespace[]{Namespace.NO_NAMESPACE, ns1, ns2}, result);
    }
    
    @Test
    public void testIsInScope_True() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        stack.depth = 1;
        stack.scope[1] = new Namespace[]{Namespace.NO_NAMESPACE, ns1, ns2};
        
        boolean result = stack.isInScope(ns1);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsInScope_False() {
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        stack.depth = 1;
        stack.scope[1] = new Namespace[]{Namespace.NO_NAMESPACE, ns1, ns2};
        
        boolean result = stack.isInScope(Namespace.getNamespace("prefix3", "uri3"));
        
        assertFalse(result);
    }
}