import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class NamespaceStackTest {

    @Test
    public void testDefaultConstructor() {
        NamespaceStack stack = new NamespaceStack();
        assertEquals(2, stack.getScope().length);
        assertEquals(Namespace.NO_NAMESPACE, stack.getScope()[0]);
        assertEquals(Namespace.XML_NAMESPACE, stack.getScope()[1]);
        assertFalse(stack.isInScope(Namespace.getNamespace("example")));
    }
    
    @Test
    public void testCustomConstructor() {
        Namespace[] seed = new Namespace[] {Namespace.NO_NAMESPACE, Namespace.getNamespace("example")};
        NamespaceStack stack = new NamespaceStack(seed);
        assertEquals(2, stack.getScope().length);
        assertEquals(Namespace.NO_NAMESPACE, stack.getScope()[0]);
        assertEquals(Namespace.getNamespace("example"), stack.getScope()[1]);
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.getNamespace("example")));
        assertFalse(stack.isInScope(Namespace.getNamespace("test")));
    }
    
    @Test
    public void testPushElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("example", Namespace.NO_NAMESPACE);
        stack.push(element);
        assertEquals(1, stack.getScope().length);
        assertEquals(Namespace.NO_NAMESPACE, stack.getScope()[0]);
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertFalse(stack.isInScope(Namespace.getNamespace("example")));
    }
    
    @Test
    public void testPushElementWithAdditionalNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("example", Namespace.NO_NAMESPACE);
        Namespace ns1 = Namespace.getNamespace("example");
        Namespace ns2 = Namespace.getNamespace("test");
        element.addNamespaceDeclaration(ns1);
        element.addNamespaceDeclaration(ns2);
        stack.push(element);
        assertEquals(3, stack.getScope().length);
        assertEquals(Namespace.NO_NAMESPACE, stack.getScope()[0]);
        assertEquals(ns1, stack.getScope()[1]);
        assertEquals(ns2, stack.getScope()[2]);
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(ns1));
        assertTrue(stack.isInScope(ns2));
    }
    
    @Test
    public void testPushElementWithAttributes() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("example", Namespace.NO_NAMESPACE);
        Namespace ns1 = Namespace.getNamespace("example");
        Namespace ns2 = Namespace.getNamespace("test");
        element.setAttribute(new Attribute("attr1", "value1", ns1));
        element.setAttribute(new Attribute("attr2", "value2", ns2));
        stack.push(element);
        assertEquals(3, stack.getScope().length);
        assertEquals(Namespace.NO_NAMESPACE, stack.getScope()[0]);
        assertEquals(ns1, stack.getScope()[1]);
        assertEquals(ns2, stack.getScope()[2]);
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(ns1));
        assertTrue(stack.isInScope(ns2));
    }
    
    @Test
    public void testPushAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("attr1", "value1", Namespace.NO_NAMESPACE);
        stack.push(attribute);
        assertEquals(1, stack.getScope().length);
        assertEquals(Namespace.NO_NAMESPACE, stack.getScope()[0]);
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
    }
    
    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("example", Namespace.NO_NAMESPACE);
        stack.push(element);
        stack.pop();
        assertEquals(2, stack.getScope().length);
        assertEquals(Namespace.NO_NAMESPACE, stack.getScope()[0]);
        assertEquals(Namespace.XML_NAMESPACE, stack.getScope()[1]);
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
        assertFalse(stack.isInScope(Namespace.getNamespace("example")));
    }
    
    @Test
    public void testAddedForwardEmpty() {
        NamespaceStack stack = new NamespaceStack();
        Iterable<Namespace> iterable = stack.addedForward();
        assertFalse(iterable.iterator().hasNext());
    }
    
    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("example", Namespace.NO_NAMESPACE);
        Namespace ns1 = Namespace.getNamespace("example");
        Namespace ns2 = Namespace.getNamespace("test");
        element.addNamespaceDeclaration(ns1);
        element.addNamespaceDeclaration(ns2);
        stack.push(element);
        Iterable<Namespace> iterable = stack.addedForward();
        Iterator<Namespace> iterator = iterable.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(ns1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ns2, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testAddedReverseEmpty() {
        NamespaceStack stack = new NamespaceStack();
        Iterable<Namespace> iterable = stack.addedReverse();
        assertFalse(iterable.iterator().hasNext());
    }
    
    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("example", Namespace.NO_NAMESPACE);
        Namespace ns1 = Namespace.getNamespace("example");
        Namespace ns2 = Namespace.getNamespace("test");
        element.addNamespaceDeclaration(ns1);
        element.addNamespaceDeclaration(ns2);
        stack.push(element);
        Iterable<Namespace> iterable = stack.addedReverse();
        Iterator<Namespace> iterator = iterable.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(ns2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ns1, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("example", Namespace.NO_NAMESPACE);
        stack.push(element);
        Iterator<Namespace> iterator = stack.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testGetScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("example", Namespace.NO_NAMESPACE);
        stack.push(element);
        Namespace[] scope = stack.getScope();
        assertEquals(1, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
    }
    
    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("example", Namespace.NO_NAMESPACE);
        stack.push(element);
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertFalse(stack.isInScope(Namespace.getNamespace("example")));
    }

}