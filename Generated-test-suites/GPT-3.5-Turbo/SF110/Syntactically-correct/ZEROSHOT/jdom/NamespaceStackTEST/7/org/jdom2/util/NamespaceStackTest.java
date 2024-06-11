import org.jdom2.*;
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
    public void testPush_Element() {
        Element element = new Element("test");
        stack.push(element);
        assertTrue(stack.isInScope(element.getNamespace()));
    }

    @Test
    public void testPush_Attribute() {
        Attribute attribute = new Attribute("test", "value");
        stack.push(attribute);
        assertTrue(stack.isInScope(attribute.getNamespace()));
    }

    @Test
    public void testPop() {
        stack.pop();
        assertEquals(null, stack.getScope());
    }

    @Test
    public void testAddedForward() {
        Iterable<Namespace> iterable = stack.addedForward();
        assertNotNull(iterable);
    }

    @Test
    public void testAddedReverse() {
        Iterable<Namespace> iterable = stack.addedReverse();
        assertNotNull(iterable);
    }

    @Test
    public void testIterator() {
        stack.push(new Element("test"));
        stack.push(new Element("test2"));
        stack.push(new Element("test3"));
        assertNotNull(stack.iterator());
    }

    @Test
    public void testGetScope() {
        Namespace[] scope = stack.getScope();
        assertNotNull(scope);
    }

    @Test
    public void testIsInScope() {
        Namespace namespace = Namespace.getNamespace("test", "http://www.test.com");
        stack.push(new Element("test").setNamespace(namespace));
        assertTrue(stack.isInScope(namespace));
    }
}