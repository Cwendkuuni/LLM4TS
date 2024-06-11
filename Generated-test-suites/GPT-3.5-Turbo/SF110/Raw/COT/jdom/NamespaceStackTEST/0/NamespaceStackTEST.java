
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class NamespaceStackTest {
    
    @Test
    public void testConstructor() {
        Namespace[] seed = new Namespace[] {new Namespace("prefix", "uri")};
        NamespaceStack stack = new NamespaceStack(seed);
        Namespace[] expectedScope = stack.getScope();
        assertEquals(seed.length, expectedScope.length);
        assertTrue(Arrays.equals(seed, expectedScope));
    }
    
    @Test
    public void testPushWithElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("element");
        element.setNamespace(Namespace.NO_NAMESPACE);
        stack.push(element);
        Namespace[] expectedScope = stack.getScope();
        assertEquals(2, expectedScope.length);
        assertEquals(Namespace.NO_NAMESPACE, expectedScope[0]);
    }
    
    @Test
    public void testPushWithAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("attribute", "value", Namespace.NO_NAMESPACE);
        stack.push(attribute);
        Namespace[] expectedScope = stack.getScope();
        assertEquals(2, expectedScope.length);
        assertEquals(Namespace.NO_NAMESPACE, expectedScope[0]);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }
    
    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Iterable<Namespace> added = stack.addedForward();
        assertFalse(added.iterator().hasNext());
    }
    
    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        Iterable<Namespace> added = stack.addedReverse();
        assertFalse(added.iterator().hasNext());
    }
    
    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Iterator<Namespace> iterator = stack.iterator();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace = Namespace.NO_NAMESPACE;
        assertTrue(stack.isInScope(namespace));
    }
}
