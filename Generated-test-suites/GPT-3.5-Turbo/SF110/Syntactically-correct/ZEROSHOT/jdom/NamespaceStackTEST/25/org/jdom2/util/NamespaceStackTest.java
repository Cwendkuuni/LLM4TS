import org.jdom2.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class NamespaceStackTest {
    
    @Test
    public void testPushWithElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        Namespace ns1 = Namespace.getNamespace("ns1", "uri1");
        Namespace ns2 = Namespace.getNamespace("ns2", "uri2");
        List<Namespace> additionalNamespaces = new ArrayList<Namespace>();
        additionalNamespaces.add(ns2);
        
        element.setNamespace(ns1);
        element.setAdditionalNamespaces(additionalNamespaces);
        
        stack.push(element);
        
        assertEquals(2, stack.addedForward().spliterator().getExactSizeIfKnown());
        assertEquals(ns1, stack.addedForward().iterator().next());
        assertEquals(ns2, stack.addedForward().iterator().next());
        
        assertEquals(2, stack.getScope().length);
        assertEquals(ns1, stack.getScope()[0]);
        assertEquals(ns2, stack.getScope()[1]);
        
        assertTrue(stack.isInScope(ns1));
        assertTrue(stack.isInScope(ns2));
    }
    
    @Test
    public void testPushWithAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("test", "value", Namespace.getNamespace("ns1", "uri1"));
        
        stack.push(attribute);
        
        assertEquals(1, stack.addedForward().spliterator().getExactSizeIfKnown());
        assertEquals(attribute.getNamespace(), stack.addedForward().iterator().next());
        
        assertEquals(1, stack.getScope().length);
        assertEquals(attribute.getNamespace(), stack.getScope()[0]);
        
        assertTrue(stack.isInScope(attribute.getNamespace()));
    }
    
    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        Namespace ns1 = Namespace.getNamespace("ns1", "uri1");
        
        element.setNamespace(ns1);
        stack.push(element);
        
        stack.pop();
        
        assertEquals(0, stack.addedForward().spliterator().getExactSizeIfKnown());
        
        assertEquals(0, stack.getScope().length);
        
        assertFalse(stack.isInScope(ns1));
    }
    
    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        Namespace ns1 = Namespace.getNamespace("ns1", "uri1");
        Namespace ns2 = Namespace.getNamespace("ns2", "uri2");
        
        element.setNamespace(ns1);
        stack.push(element);
        stack.push(ns2);
        
        Iterator<Namespace> iterator = stack.iterator();
        
        assertEquals(ns2, iterator.next());
        assertEquals(ns1, iterator.next());
        assertFalse(iterator.hasNext());
    }
}