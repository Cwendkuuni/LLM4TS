import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

    private NamespaceStack stack;

    @Before
    public void setUp() {
        stack = new NamespaceStack();
    }

    // Test push(Element) method
    @Test
    public void testPushElement() {
        Element element = new Element("test", "http://www.example.com");
        stack.push(element);

        assertTrue(stack.isInScope(element.getNamespace()));
    }

    // Test push(Attribute) method
    @Test
    public void testPushAttribute() {
        Attribute attribute = new Attribute("name", "value", Namespace.getNamespace("http://www.example.com"));
        stack.push(attribute);

        assertTrue(stack.isInScope(attribute.getNamespace()));
    }

    // Test pushStack() method with empty toadd
    @Test
    public void testPushStackWithEmptyToAdd() {
        Element element = new Element("test", "http://www.example.com");
        stack.push(element);

        stack.pop();
        Element element2 = new Element("test2", "http://www.example.com");
        stack.push(element2);

        assertTrue(stack.isInScope(element2.getNamespace()));
    }

    // Test pushStack() method with non-empty toadd
    @Test
    public void testPushStackWithNonEmptyToAdd() {
        Element element = new Element("test", "http://www.example.com");
        stack.push(element);

        stack.pop();

        Element element2 = new Element("test2", "http://www.example.com");
        element2.addNamespaceDeclaration("prefix", "http://www.example2.com");
        stack.push(element2);

        assertTrue(stack.isInScope(element2.getNamespace("prefix")));
    }

    // Test pop() method
    @Test
    public void testPop() {
        Element element = new Element("test", "http://www.example.com");
        stack.push(element);

        stack.pop();

        assertFalse(stack.isInScope(element.getNamespace()));
    }

    // Test addedForward() method with empty added array
    @Test
    public void testAddedForwardWithEmptyAddedArray() {
        Iterable<Namespace> iterable = stack.addedForward();

        assertFalse(iterable.iterator().hasNext());
    }

    // Test addedForward() method with non-empty added array
    @Test
    public void testAddedForwardWithNonEmptyAddedArray() {
        Element element = new Element("test", "http://www.example.com");
        stack.push(element);

        Iterable<Namespace> iterable = stack.addedForward();

        assertTrue(iterable.iterator().hasNext());
    }

    // Test addedReverse() method with empty added array
    @Test
    public void testAddedReverseWithEmptyAddedArray() {
        Iterable<Namespace> iterable = stack.addedReverse();

        assertFalse(iterable.iterator().hasNext());
    }

    // Test addedReverse() method with non-empty added array
    @Test
    public void testAddedReverseWithNonEmptyAddedArray() {
        Element element = new Element("test", "http://www.example.com");
        stack.push(element);

        Iterable<Namespace> iterable = stack.addedReverse();

        assertTrue(iterable.iterator().hasNext());
    }

    // Test iterator() method
    @Test
    public void testIterator() {
        Element element = new Element("test", "http://www.example.com");
        stack.push(element);

        assertTrue(stack.iterator().hasNext());
    }

    // Test getScope() method
    @Test
    public void testGetScope() {
        Element element = new Element("test", "http://www.example.com");
        stack.push(element);

        Namespace[] scope = stack.getScope();

        assertNotNull(scope);
        assertEquals(element.getNamespace(), scope[0]);
    }

    // Test isInScope() method with namespace in scope
    @Test
    public void testIsInScopeWithNamespaceInScope() {
        Element element = new Element("test", "http://www.example.com");
        stack.push(element);

        assertTrue(stack.isInScope(element.getNamespace()));
    }

    // Test isInScope() method with namespace not in scope
    @Test
    public void testIsInScopeWithNamespaceNotInScope() {
        Element element = new Element("test", "http://www.example.com");
        stack.push(element);

        Namespace namespace = Namespace.getNamespace("http://www.example2.com");

        assertFalse(stack.isInScope(namespace));
    }
}