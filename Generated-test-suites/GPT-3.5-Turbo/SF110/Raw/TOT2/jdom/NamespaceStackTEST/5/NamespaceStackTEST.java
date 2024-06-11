import org.junit.*;
import static org.junit.Assert.*;
import org.jdom2.util.NamespaceStack;
import org.jdom2.Namespace;

public class NamespaceStackTest {

    @Test
    public void testNamespaceStackConstructor() {
        NamespaceStack stack = new NamespaceStack();
        assertNotNull(stack);
        Namespace[] seed = {Namespace.NO_NAMESPACE};
        stack = new NamespaceStack(seed);
        assertNotNull(stack);
    }

    @Test
    public void testPush() {
        NamespaceStack stack = new NamespaceStack();
        Namespace ns = Namespace.getNamespace("prefix", "uri");
        stack.push(ns);
        assertTrue(stack.isInScope(ns));
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Namespace ns = Namespace.getNamespace("prefix", "uri");
        stack.push(ns);
        assertTrue(stack.isInScope(ns));
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Namespace ns = Namespace.getNamespace("prefix", "uri");
        stack.push(ns);
        stack.pop();
        assertFalse(stack.isInScope(ns));
    }

    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Iterable<Namespace> iterable = stack.addedForward();
        assertNotNull(iterable);
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        Iterable<Namespace> iterable = stack.addedReverse();
        assertNotNull(iterable);
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Iterator<Namespace> iterator = stack.iterator();
        assertNotNull(iterator);
    }

    @Test
    public void testGetScope() {
        NamespaceStack stack = new NamespaceStack();
        Namespace[] scope = stack.getScope();
        assertNotNull(scope);
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Namespace ns = Namespace.getNamespace("prefix", "uri");
        assertFalse(stack.isInScope(ns));
    }

    @Test(expected = IllegalStateException.class)
    public void testPopEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

}