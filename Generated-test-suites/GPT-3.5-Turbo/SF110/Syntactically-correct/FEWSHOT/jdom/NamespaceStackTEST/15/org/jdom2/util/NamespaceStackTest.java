import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;

public class NamespaceStackTest {
    private NamespaceStack namespaceStack;
    private Element element;
    private Attribute attribute;
    
    @Before
    public void setup() {
        namespaceStack = new NamespaceStack();
        element = new Element("testElement");
        attribute = new Attribute("testAttribute", "testValue", Namespace.NO_NAMESPACE);
    }
    
    @Test
    public void testPushWithElement() {
        namespaceStack.push(element);
        
        assertTrue(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
        assertFalse(namespaceStack.isInScope(new Namespace("testPrefix", "testNamespace")));
    }
    
    @Test
    public void testPushWithAttribute() {
        namespaceStack.push(attribute);
        
        assertTrue(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
        assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
        assertFalse(namespaceStack.isInScope(new Namespace("testPrefix", "testNamespace")));
    }
    
    @Test
    public void testPop() {
        namespaceStack.push(element);
        namespaceStack.pop();
        
        assertFalse(namespaceStack.isInScope(Namespace.NO_NAMESPACE));
        assertFalse(namespaceStack.isInScope(Namespace.XML_NAMESPACE));
        assertFalse(namespaceStack.isInScope(new Namespace("testPrefix", "testNamespace")));
    }
    
    @Test
    public void testAddedForward() {
        namespaceStack.push(element);
        Iterator<Namespace> iterator = namespaceStack.addedForward().iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.XML_NAMESPACE, iterator.next());
    }
    
    @Test
    public void testAddedReverse() {
        namespaceStack.push(element);
        Iterator<Namespace> iterator = namespaceStack.addedReverse().iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.XML_NAMESPACE, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());
    }
    
    @Test
    public void testIterator() {
        Namespace customNamespace = new Namespace("testPrefix", "testNamespace");
        namespaceStack.push(element);
        namespaceStack.push(new Attribute("testAttribute", "testValue", customNamespace));
        Iterator<Namespace> iterator = namespaceStack.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.NO_NAMESPACE, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.XML_NAMESPACE, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(customNamespace, iterator.next());
    }
    
    // Add more test cases to comprehensively test all methods
    
}