import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testBinarySearch() {
        Namespace[] data = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertEquals(0, NamespaceStack.binarySearch(data, 0, data.length, Namespace.NO_NAMESPACE));
        assertEquals(1, NamespaceStack.binarySearch(data, 0, data.length, Namespace.XML_NAMESPACE));
        assertEquals(-1, NamespaceStack.binarySearch(data, 0, data.length, new Namespace("example", "http://example.com")));
    }

    @Test
    public void testPush() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        stack.push(element);
        assertTrue(stack.isInScope(element.getNamespace()));
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        stack.push(element);
        stack.pop();
        assertFalse(stack.isInScope(element.getNamespace()));
    }

    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        stack.push(element);
        assertNotNull(stack.addedForward());
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        stack.push(element);
        assertNotNull(stack.addedReverse());
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        assertNotNull(stack.iterator());
    }

    @Test
    public void testGetScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        stack.push(element);
        assertNotNull(stack.getScope());
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        stack.push(element);
        assertTrue(stack.isInScope(element.getNamespace()));
    }
}