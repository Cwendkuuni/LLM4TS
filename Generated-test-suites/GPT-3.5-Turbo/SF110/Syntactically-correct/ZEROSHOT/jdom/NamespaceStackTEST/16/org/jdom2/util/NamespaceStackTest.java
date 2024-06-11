import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class NamespaceStackTest {

    @Test
    public void testPush() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        stack.push(element);
        assertEquals(1, stack.getScope().length);
        assertEquals(Namespace.NO_NAMESPACE, stack.getScope()[0]);
        stack.pop();
    }

    @Test
    public void testPushWithAdditionalNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix", "uri"));
        stack.push(element);
        assertEquals(2, stack.getScope().length);
        assertEquals(Namespace.NO_NAMESPACE, stack.getScope()[0]);
        assertEquals("prefix", stack.getScope()[1].getPrefix());
        assertEquals("uri", stack.getScope()[1].getURI());
        stack.pop();
    }

    @Test
    public void testPushWithAttributes() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        element.setAttribute("attribute", "value", Namespace.getNamespace("prefix", "uri"));
        stack.push(element);
        assertEquals(2, stack.getScope().length);
        assertEquals(Namespace.NO_NAMESPACE, stack.getScope()[0]);
        assertEquals("prefix", stack.getScope()[1].getPrefix());
        assertEquals("uri", stack.getScope()[1].getURI());
        stack.pop();
    }

    @Test(expected = IllegalStateException.class)
    public void testPopEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix", "uri"));
        stack.push(element);
        Iterator<Namespace> iterator = stack.addedForward().iterator();
        assertTrue(iterator.hasNext());
        assertEquals("prefix", iterator.next().getPrefix());
        assertFalse(iterator.hasNext());
        stack.pop();
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix", "uri"));
        stack.push(element);
        Iterator<Namespace> iterator = stack.addedReverse().iterator();
        assertTrue(iterator.hasNext());
        assertEquals("prefix", iterator.next().getPrefix());
        assertFalse(iterator.hasNext());
        stack.pop();
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix", "uri"));
        stack.push(element);
        Iterator<Namespace> iterator = stack.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("", iterator.next().getPrefix());
        assertTrue(iterator.hasNext());
        assertEquals("prefix", iterator.next().getPrefix());
        assertFalse(iterator.hasNext());
        stack.pop();
    }

    @Test
    public void testGetScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        stack.push(element);
        Namespace[] scope = stack.getScope();
        assertNotNull(scope);
        assertEquals(1, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        stack.pop();
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        element.addNamespaceDeclaration(Namespace.getNamespace("prefix", "uri"));
        stack.push(element);
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix", "uri")));
        stack.pop();
    }
}