import org.junit.Test;
import org.jdom2.Namespace;
import org.jdom2.util.NamespaceStack;

import static org.junit.Assert.*;

public class NamespaceStackTest {

    @Test
    public void testPush_Element() {
        NamespaceStack stack = new NamespaceStack();

        stack.push(new Element("root"));

        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
    }
    
    @Test
    public void testPushWithAdditionalNamespaces_Element() {
        NamespaceStack stack = new NamespaceStack();

        Element element = new Element("root");
        element.addNamespaceDeclaration(Namespace.getNamespace("test", "http://www.example.com"));

        stack.push(element);

        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.getNamespace("test", "http://www.example.com")));
    }
    
    @Test
    public void testPushWithAttributes_Element() {
        NamespaceStack stack = new NamespaceStack();

        Element element = new Element("root");
        element.setAttribute("attribute", "value", Namespace.getNamespace("test", "http://www.example.com"));

        stack.push(element);

        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.getNamespace("test", "http://www.example.com")));
    }

    @Test
    public void testPushWithExistingNamespaces_Element() {
        NamespaceStack stack = new NamespaceStack();

        Element element1 = new Element("root", Namespace.getNamespace("ns1", "http://www.example.com"));
        Element element2 = new Element("child", Namespace.getNamespace("ns1", "http://www.example.com"));

        stack.push(element1);
        stack.push(element2);

        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.getNamespace("ns1", "http://www.example.com")));
    }

    @Test
    public void testPush_Attribute() {
        NamespaceStack stack = new NamespaceStack();

        stack.push(new Attribute("attribute", "value"));

        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
    }

    @Test
    public void testPop_NonEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root"));

        stack.pop();

        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
        assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
    }

    @Test(expected = IllegalStateException.class)
    public void testPop_EmptyStack() {
        NamespaceStack stack = new NamespaceStack();

        stack.pop();
    }

    @Test
    public void testGetScope() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root"));

        Namespace[] scope = stack.getScope();

        assertEquals(2, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(Namespace.XML_NAMESPACE, scope[1]);
    }

    @Test
    public void testAddedForward_NonEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root"));

        Iterable<Namespace> added = stack.addedForward();

        assertNotNull(added);
        assertTrue(added.iterator().hasNext());
        assertEquals(Namespace.NO_NAMESPACE, added.iterator().next());
    }

    @Test
    public void testAddedForward_EmptyStack() {
        NamespaceStack stack = new NamespaceStack();

        Iterable<Namespace> added = stack.addedForward();

        assertNotNull(added);
        assertFalse(added.iterator().hasNext());
    }

    @Test
    public void testAddedReverse_NonEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root"));

        Iterable<Namespace> added = stack.addedReverse();

        assertNotNull(added);
        assertTrue(added.iterator().hasNext());
        assertEquals(Namespace.NO_NAMESPACE, added.iterator().next());
    }

    @Test
    public void testAddedReverse_EmptyStack() {
        NamespaceStack stack = new NamespaceStack();

        Iterable<Namespace> added = stack.addedReverse();

        assertNotNull(added);
        assertFalse(added.iterator().hasNext());
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root"));
        stack.push(new Element("child"));

        Iterator<Namespace> iterator = stack.iterator();

        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.XML_NAMESPACE, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("root"));

        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.XML_NAMESPACE));
        assertFalse(stack.isInScope(Namespace.getNamespace("test", "http://www.example.com")));
    }
}