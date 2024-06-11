
import org.junit.Before;
import org.junit.Test;
import org.jdom2.Namespace;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class NamespaceStackTest {
    private NamespaceStack stack;

    @Before
    public void setUp() {
        stack = new NamespaceStack();
    }

    @Test
    public void testEmptyStack() {
        assertEquals(0, stack.getScope().length);
        assertFalse(stack.iterator().hasNext());
    }

    @Test
    public void testPush() {
        Namespace ns1 = Namespace.getNamespace("test1", "http://www.test1.com");
        stack.push(new Element("element", ns1));

        Namespace ns2 = Namespace.getNamespace("test2", "http://www.test2.com");
        stack.push(new Element("element", ns2));

        Iterator<Namespace> iterator = stack.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(ns2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ns1, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalStateException.class)
    public void testPopFromEmptyStack() {
        stack.pop();
    }

    @Test
    public void testPop() {
        Namespace ns1 = Namespace.getNamespace("test1", "http://www.test1.com");
        stack.push(new Element("element", ns1));

        stack.pop();

        assertFalse(stack.iterator().hasNext());
    }

    @Test
    public void testAddedForward() {
        Namespace ns1 = Namespace.getNamespace("test1", "http://www.test1.com");
        stack.push(new Element("element", ns1));

        Namespace ns2 = Namespace.getNamespace("test2", "http://www.test2.com");
        stack.push(new Element("element", ns2));

        Iterator<Namespace> iterator = stack.addedForward().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(ns2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ns1, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAddedReverse() {
        Namespace ns1 = Namespace.getNamespace("test1", "http://www.test1.com");
        stack.push(new Element("element", ns1));

        Namespace ns2 = Namespace.getNamespace("test2", "http://www.test2.com");
        stack.push(new Element("element", ns2));

        Iterator<Namespace> iterator = stack.addedReverse().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(ns1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(ns2, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetScope() {
        Namespace ns1 = Namespace.getNamespace("test1", "http://www.test1.com");
        stack.push(new Element("element", ns1));

        Namespace ns2 = Namespace.getNamespace("test2", "http://www.test2.com");
        stack.push(new Element("element", ns2));

        Namespace[] scope = stack.getScope();
        assertEquals(ns2, scope[0]);
        assertEquals(ns1, scope[1]);
    }

    @Test
    public void testIsInScope() {
        Namespace ns1 = Namespace.getNamespace("test1", "http://www.test1.com");
        stack.push(new Element("element", ns1));

        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
        assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
        assertTrue(stack.isInScope(ns1));
    }
}

