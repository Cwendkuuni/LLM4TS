import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testBinarySearch() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Namespace[] data = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        int result = NamespaceStack.binarySearch(data, 0, data.length, Namespace.NO_NAMESPACE);
        assertEquals(0, result);
    }
    
    @Test
    public void testPushElement() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("test");
        namespaceStack.push(element);
        assertEquals(1, namespaceStack.getScope().length);
    }
    
    @Test
    public void testPushAttribute() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Attribute attribute = new Attribute("test", "value");
        namespaceStack.push(attribute);
        assertEquals(1, namespaceStack.getScope().length);
    }
    
    @Test
    public void testPop() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("test");
        namespaceStack.push(element);
        namespaceStack.pop();
        assertEquals(false, namespaceStack.isInScope(Namespace.NO_NAMESPACE));
    }
    
    @Test
    public void testAddedForward() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("test");
        namespaceStack.push(element);
        assertNotNull(namespaceStack.addedForward());
    }
    
    @Test
    public void testAddedReverse() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("test");
        namespaceStack.push(element);
        assertNotNull(namespaceStack.addedReverse());
    }
    
    @Test
    public void testIterator() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("test");
        namespaceStack.push(element);
        assertNotNull(namespaceStack.iterator());
    }

}