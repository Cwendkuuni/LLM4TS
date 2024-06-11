import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testConstructor() {
        Namespace[] seed = {Namespace.XML_NAMESPACE};
        NamespaceStack nsStack = new NamespaceStack(seed);
        assertNotNull(nsStack);
    }

    @Test
    public void testPushElement() {
        NamespaceStack nsStack = new NamespaceStack();
        Element element = new Element("test", Namespace.XML_NAMESPACE);
        nsStack.push(element);
        assertTrue(nsStack.isInScope(Namespace.XML_NAMESPACE));
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack nsStack = new NamespaceStack();
        Attribute attribute = new Attribute("test", "value", Namespace.XML_NAMESPACE);
        nsStack.push(attribute);
        assertTrue(nsStack.isInScope(Namespace.XML_NAMESPACE));
    }

    @Test(expected = IllegalStateException.class)
    public void testPopEmptyStack() {
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.pop();
    }

    @Test
    public void testAddedForward() {
        NamespaceStack nsStack = new NamespaceStack();
        Element element = new Element("test", Namespace.XML_NAMESPACE);
        nsStack.push(element);
        assertNotNull(nsStack.addedForward());
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack nsStack = new NamespaceStack();
        Element element = new Element("test", Namespace.XML_NAMESPACE);
        nsStack.push(element);
        assertNotNull(nsStack.addedReverse());
    }

    @Test
    public void testIterator() {
        NamespaceStack nsStack = new NamespaceStack();
        Element element = new Element("test", Namespace.XML_NAMESPACE);
        nsStack.push(element);
        assertNotNull(nsStack.iterator());
    }

}