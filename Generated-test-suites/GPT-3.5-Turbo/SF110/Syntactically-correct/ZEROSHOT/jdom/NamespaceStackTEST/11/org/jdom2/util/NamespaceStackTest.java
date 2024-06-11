import org.jdom2.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class NamespaceStackTest {

    @Test
    public void testPushElement() {
        Element element = new Element("test");
        Namespace namespace = Namespace.getNamespace("prefix", "http://www.example.com");
        element.setNamespace(namespace);

        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(element);

        Namespace[] scope = namespaceStack.getScope();
        assertEquals(2, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(namespace, scope[1]);

        assertTrue(namespaceStack.isInScope(namespace));
        assertFalse(namespaceStack.isInScope(Namespace.getNamespace("another_prefix", "http://www.example.com")));
    }

    @Test
    public void testPushAttribute() {
        Attribute attribute = new Attribute("test", "value", Namespace.getNamespace("prefix", "http://www.example.com"));

        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(attribute);

        Namespace[] scope = namespaceStack.getScope();
        assertEquals(2, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(attribute.getNamespace(), scope[1]);

        assertTrue(namespaceStack.isInScope(attribute.getNamespace()));
        assertFalse(namespaceStack.isInScope(Namespace.getNamespace("another_prefix", "http://www.example.com")));
    }

    @Test
    public void testPop() {
        Namespace namespace = Namespace.getNamespace("prefix", "http://www.example.com");

        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(new Element("test", namespace));
        namespaceStack.pop();

        Namespace[] scope = namespaceStack.getScope();
        assertEquals(2, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(Namespace.XML_NAMESPACE, scope[1]);
    }

    @Test
    public void testAddedForward() {
        Element element = new Element("test");
        Namespace namespace1 = Namespace.getNamespace("prefix1", "http://www.example.com");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "http://www.example.com");
        element.setNamespace(namespace1);
        element.addNamespaceDeclaration(namespace2);

        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(element);

        Iterable<Namespace> addedForward = namespaceStack.addedForward();
        Iterator<Namespace> iterator = addedForward.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(namespace2, iterator.next());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAddedReverse() {
        Element element = new Element("test");
        Namespace namespace1 = Namespace.getNamespace("prefix1", "http://www.example.com");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "http://www.example.com");
        element.setNamespace(namespace1);
        element.addNamespaceDeclaration(namespace2);

        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(element);

        Iterable<Namespace> addedReverse = namespaceStack.addedReverse();
        Iterator<Namespace> iterator = addedReverse.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(namespace2, iterator.next());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator() {
        Element element = new Element("test");
        Namespace namespace1 = Namespace.getNamespace("prefix1", "http://www.example.com");
        element.setNamespace(namespace1);

        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(element);

        Iterator<Namespace> iterator = namespaceStack.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());

        assertTrue(iterator.hasNext());
        assertEquals(namespace1, iterator.next());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetScope() {
        Element element = new Element("test");
        Namespace namespace = Namespace.getNamespace("prefix", "http://www.example.com");
        element.setNamespace(namespace);

        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(element);

        Namespace[] scope = namespaceStack.getScope();
        assertEquals(2, scope.length);
        assertEquals(Namespace.NO_NAMESPACE, scope[0]);
        assertEquals(namespace, scope[1]);
    }

    @Test
    public void testIsInScope() {
        Element element = new Element("test");
        Namespace namespace = Namespace.getNamespace("prefix", "http://www.example.com");
        element.setNamespace(namespace);

        NamespaceStack namespaceStack = new NamespaceStack();
        namespaceStack.push(element);

        assertTrue(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(namespaceStack.isInScope(namespace));
        assertFalse(namespaceStack.isInScope(Namespace.getNamespace("another_prefix", "http://www.example.com")));
    }
}