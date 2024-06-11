import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jdom2.*;
import java.util.*;

public class NamespaceStackTest {
    
    private NamespaceStack namespaceStack;
    private Element element;
    private Attribute attribute;
    
    @Before
    public void setUp() {
        namespaceStack = new NamespaceStack();
        element = new Element("test", Namespace.NO_NAMESPACE);
        attribute = new Attribute("testAttr", "value", Namespace.NO_NAMESPACE);
    }
    
    @Test
    public void testPush_Element() {
        namespaceStack.push(element);
        Namespace[] scope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(scope, namespaceStack.getScope());
        assertTrue(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
    }
    
    @Test
    public void testPush_Attribute() {
        namespaceStack.push(attribute);
        Namespace[] scope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(scope, namespaceStack.getScope());
        assertTrue(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
    }
    
    @Test
    public void testPop() {
        namespaceStack.push(element);
        namespaceStack.push(attribute);
        namespaceStack.pop();
        Namespace[] scope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(scope, namespaceStack.getScope());
    }
    
    @Test
    public void testAddedForward_Empty() {
        Iterable<Namespace> iterable = namespaceStack.addedForward();
        assertFalse(iterable.iterator().hasNext());
    }
    
    @Test
    public void testAddedReverse_Empty() {
        Iterable<Namespace> iterable = namespaceStack.addedReverse();
        assertFalse(iterable.iterator().hasNext());
    }
    
    @Test
    public void testIterator() {
        Iterator<Namespace> iterator = namespaceStack.iterator();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIsInScope_True() {
        namespaceStack.push(element);
        assertTrue(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
    }
    
    @Test
    public void testIsInScope_False() {
        assertFalse(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
    }
}