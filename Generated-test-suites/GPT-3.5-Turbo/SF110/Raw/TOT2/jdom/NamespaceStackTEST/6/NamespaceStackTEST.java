import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testConstructor() {
        NamespaceStack stack = new NamespaceStack();
        assertNotNull(stack);
        assertEquals(1, stack.getScope().length);
    }

    @Test
    public void testPushElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        stack.push(element);
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("testAttribute", "value", Namespace.NO_NAMESPACE);
        stack.push(attribute);
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        stack.push(element);
        stack.pop();
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
    }

    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        stack.push(element);
        assertEquals(1, stack.addedForward().spliterator().getExactSizeIfKnown());
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        stack.push(element);
        assertEquals(1, stack.addedReverse().spliterator().getExactSizeIfKnown());
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        stack.push(element);
        assertTrue(stack.iterator().hasNext());
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        stack.push(element);
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
    }
}