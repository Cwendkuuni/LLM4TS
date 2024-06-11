import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class NamespaceStackTest {
    
    private NamespaceStack stack;
    
    @Before
    public void setUp() {
        stack = new NamespaceStack();
    }
    
    @Test
    public void testPushElement() {
        Element element = new Element("test");
        
        stack.push(element);
        
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, element.getNamespace()};
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test
    public void testPushAttribute() {
        Attribute attribute = new Attribute("attribute", "value", Namespace.NO_NAMESPACE);
        
        stack.push(attribute);
        
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, attribute.getNamespace()};
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test
    public void testPop() {
        Element element = new Element("test");
        stack.push(element);
        
        stack.pop();
        
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPopEmpty() {
        stack.pop();
    }
    
    @Test
    public void testAddedForward() {
        Element element = new Element("test");
        stack.push(element);
        
        Iterator<Namespace> iterator = stack.addedForward().iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(element.getNamespace(), iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testAddedForwardEmpty() {
        Iterator<Namespace> iterator = stack.addedForward().iterator();
        
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testAddedReverse() {
        Element element = new Element("test");
        stack.push(element);
        
        Iterator<Namespace> iterator = stack.addedReverse().iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(element.getNamespace(), iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testAddedReverseEmpty() {
        Iterator<Namespace> iterator = stack.addedReverse().iterator();
        
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIterator() {
        Element element = new Element("test");
        stack.push(element);
        
        Iterator<Namespace> iterator = stack.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());
        assertEquals(Namespace.XML_NAMESPACE, iterator.next());
        assertEquals(element.getNamespace(), iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testGetScope() {
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test
    public void testIsInScope() {
        Element element = new Element("test");
        stack.push(element);
        
        assertTrue(stack.isInScope(element.getNamespace()));
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
    }
}