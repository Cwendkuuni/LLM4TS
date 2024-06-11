import org.junit.Test;
import static org.junit.Assert.*;
import org.jdom2.*;

public class NamespaceStackTest {

    @Test
    public void testPush_Element() {
        NamespaceStack stack = new NamespaceStack();
        Element element1 = new Element("element", "http://example.com");
        stack.push(element1);

        assertTrue(stack.isInScope(Namespace.getNamespace("http://example.com")));
    }

    @Test
    public void testPush_Attribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute1 = new Attribute("attribute", "value", Namespace.getNamespace("http://example.com"));
        stack.push(attribute1);

        assertTrue(stack.isInScope(Namespace.getNamespace("http://example.com")));
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Element element1 = new Element("element", "http://example.com");
        stack.push(element1);
        stack.pop();

        assertFalse(stack.isInScope(Namespace.getNamespace("http://example.com")));
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element element1 = new Element("element", "http://example.com");
        stack.push(element1);
        stack.pop();

        assertFalse(stack.iterator().hasNext());
    }

    @Test
    public void testGetScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element1 = new Element("element", "http://example.com");
        stack.push(element1);

        assertEquals(stack.getScope(), new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.getNamespace("http://example.com") });
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element1 = new Element("element", "http://example.com");
        stack.push(element1);

        assertTrue(stack.isInScope(Namespace.getNamespace("http://example.com")));
        assertFalse(stack.isInScope(Namespace.getNamespace("http://example.org")));
    }
}