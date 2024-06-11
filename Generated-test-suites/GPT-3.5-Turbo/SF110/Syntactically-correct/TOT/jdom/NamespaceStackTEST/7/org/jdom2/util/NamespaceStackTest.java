
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

    private NamespaceStack stack;

    @Before
    public void setUp() {
        stack = new NamespaceStack();
    }

    @Test
    public void testPush_WithElement_AddsNamespaceToStack() {
        Element element = new Element("testElement");
        element.setNamespace(Namespace.getNamespace("ns", "http://example.com"));

        stack.push(element);
        assertTrue(stack.isInScope(Namespace.getNamespace("ns", "http://example.com")));
    }

    @Test
    public void testPush_WithElementWithAdditionalNamespaces_AddsAllNamespacesToStack() {
        Element element = new Element("testElement");
        element.setNamespace(Namespace.getNamespace("ns", "http://example.com"));
        element.addNamespaceDeclaration(Namespace.getNamespace("ns1", "http://example1.com"));
        element.addNamespaceDeclaration(Namespace.getNamespace("ns2", "http://example2.com"));

        stack.push(element);
        assertTrue(stack.isInScope(Namespace.getNamespace("ns", "http://example.com")));
        assertTrue(stack.isInScope(Namespace.getNamespace("ns1", "http://example1.com")));
        assertTrue(stack.isInScope(Namespace.getNamespace("ns2", "http://example2.com")));
    }

    @Test
    public void testPush_WithAttribute_AddsNamespaceToStack() {
        Element element = new Element("testElement");
        element.setAttribute("testAttribute", "value", Namespace.getNamespace("ns", "http://example.com"));

        stack.push(element.getAttribute("testAttribute").getNamespace());
        assertTrue(stack.isInScope(Namespace.getNamespace("ns", "http://example.com")));
    }

    @Test
    public void testPop_WhenStackNotEmpty_RemovesTopElementFromStack() {
        Element element = new Element("testElement");
        element.setNamespace(Namespace.getNamespace("ns", "http://example.com"));
        stack.push(element);

        stack.pop();
        assertFalse(stack.isInScope(Namespace.getNamespace("ns", "http://example.com")));
    }

    @Test(expected = IllegalStateException.class)
    public void testPop_WhenStackEmpty_ThrowsIllegalStateException() {
        stack.pop();
    }

    @Test
    public void testAddedForward_WhenNoNamespacesInCurrentDepth_ReturnsEmptyIterable() {
        Iterable<Namespace> addedNamespaces = stack.addedForward();
        assertFalse(addedNamespaces.iterator().hasNext());
    }

    @Test
    public void testAddedForward_WhenNamespacesInCurrentDepth_ReturnsIterable() {
        Element element = new Element("testElement");
        element.addNamespaceDeclaration(Namespace.getNamespace("ns1", "http://example1.com"));
        element.addNamespaceDeclaration(Namespace.getNamespace("ns2", "http://example2.com"));
        stack.push(element);

        Iterable<Namespace> addedNamespaces = stack.addedForward();
        assertNotNull(addedNamespaces.iterator().next());
    }

    @Test
    public void testAddedReverse_WhenNoNamespacesInCurrentDepth_ReturnsEmptyIterable() {
        Iterable<Namespace> addedNamespaces = stack.addedReverse();
        assertFalse(addedNamespaces.iterator().hasNext());
    }

    @Test
    public void testAddedReverse_WhenNamespacesInCurrentDepth_ReturnsIterable() {
        Element element = new Element("testElement");
        element.addNamespaceDeclaration(Namespace.getNamespace("ns1", "http://example1.com"));
        element.addNamespaceDeclaration(Namespace.getNamespace("ns2", "http://example2.com"));
        stack.push(element);

        Iterable<Namespace> addedNamespaces = stack.addedReverse();
        assertNotNull(addedNamespaces.iterator().next());
    }

    @Test
    public void testIterator_WhenStackEmpty_ReturnsEmptyIterator() {
        assertFalse(stack.iterator().hasNext());
    }

    @Test
    public void testIterator_WhenStackNotEmpty_ReturnsIterator() {
        Element element = new Element("testElement");
        element.setNamespace(Namespace.getNamespace("ns", "http://example.com"));
        stack.push(element);

        assertTrue(stack.iterator().hasNext());
    }

    @Test
    public void testGetScope_WhenStackEmpty_ReturnsEmptyArray() {
        Namespace[] scope = stack.getScope();
        assertEquals(0, scope.length);
    }

    @Test
    public void testGetScope_WhenStackNotEmpty_ReturnsArrayWithNamespaces() {
        Element element = new Element("testElement");
        element.setNamespace(Namespace.getNamespace("ns", "http://example.com"));
        stack.push(element);

        Namespace[] scope = stack.getScope();
        assertEquals(1, scope.length);
        assertEquals("ns", scope[0].getPrefix());
        assertEquals("http://example.com", scope[0].getURI());
    }

    @Test
    public void testIsInScope_WithNamespaceInScope_ReturnsTrue() {
        Element element = new Element("testElement");
        element.setNamespace(Namespace.getNamespace("ns", "http://example.com"));
        stack.push(element);

        assertTrue(stack.isInScope(Namespace.getNamespace("ns", "http://example.com")));
    }

    @Test
    public void testIsInScope_WithNamespaceNotInScope_ReturnsFalse() {
        Element element = new Element("testElement");
        element.setNamespace(Namespace.getNamespace("ns", "http://example.com"));
        stack.push(element);

        assertFalse(stack.isInScope(Namespace.getNamespace("ns1", "http://example1.com")));
    }
}

