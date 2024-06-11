import org.junit.*;
import static org.junit.Assert.*;
import org.jdom2.*;
import java.util.*;

public class NamespaceStackTest {

    @Test
    public void testPush_Element_AddsNamespaceToStack() {
        Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
        NamespaceStack nsStack = new NamespaceStack();
        
        nsStack.push(element);
        
        Namespace[] expectedScope = { Namespace.getNamespace("", ""), Namespace.getNamespace("test", "http://www.example.com") };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }
    
    @Test
    public void testPush_Element_SameNamespaceAddedOnlyOnce() {
        Element element1 = new Element("testElement1", Namespace.getNamespace("test", "http://www.example.com"));
        Element element2 = new Element("testElement2", Namespace.getNamespace("test", "http://www.example.com"));
        NamespaceStack nsStack = new NamespaceStack();
        
        nsStack.push(element1);
        nsStack.push(element2);
        
        Namespace[] expectedScope = { Namespace.getNamespace("", ""), Namespace.getNamespace("test", "http://www.example.com") };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }
    
    @Test
    public void testPush_Element_AdditionalNamespacesAddedToStack() {
        Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
        element.addNamespaceDeclaration(Namespace.getNamespace("test2", "http://www.example2.com"));
        NamespaceStack nsStack = new NamespaceStack();
        
        nsStack.push(element);
        
        Namespace[] expectedScope = { Namespace.getNamespace("", ""), Namespace.getNamespace("test", "http://www.example.com"), Namespace.getNamespace("test2", "http://www.example2.com") };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }
    
    @Test
    public void testPush_Element_AttributesNamespaceAddedToStack() {
        Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
        element.setAttribute(new Attribute("attr1", "value1", Namespace.getNamespace("attr", "http://www.attribute.com")));
        NamespaceStack nsStack = new NamespaceStack();
        
        nsStack.push(element);
        
        Namespace[] expectedScope = { Namespace.getNamespace("", ""), Namespace.getNamespace("test", "http://www.example.com"), Namespace.getNamespace("attr", "http://www.attribute.com") };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }
    
    @Test
    public void testPush_Attribute_AddsNamespaceToStack() {
        Attribute attribute = new Attribute("attr1", "value1", Namespace.getNamespace("test", "http://www.example.com"));
        NamespaceStack nsStack = new NamespaceStack();
        
        nsStack.push(attribute);
        
        Namespace[] expectedScope = { Namespace.getNamespace("", ""), Namespace.getNamespace("test", "http://www.example.com") };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }
    
    @Test
    public void testPush_Attribute_SameNamespaceAddedOnlyOnce() {
        Attribute attribute1 = new Attribute("attr1", "value1", Namespace.getNamespace("test", "http://www.example.com"));
        Attribute attribute2 = new Attribute("attr2", "value2", Namespace.getNamespace("test", "http://www.example.com"));
        NamespaceStack nsStack = new NamespaceStack();
        
        nsStack.push(attribute1);
        nsStack.push(attribute2);
        
        Namespace[] expectedScope = { Namespace.getNamespace("", ""), Namespace.getNamespace("test", "http://www.example.com") };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }
    
    @Test
    public void testPop_RemovesTopStackEntry() {
        Element element1 = new Element("testElement1", Namespace.getNamespace("test", "http://www.example.com"));
        Element element2 = new Element("testElement2", Namespace.getNamespace("test2", "http://www.example2.com"));
        NamespaceStack nsStack = new NamespaceStack();
        
        nsStack.push(element1);
        nsStack.push(element2);
        nsStack.pop();
        
        Namespace[] expectedScope = { Namespace.getNamespace("", ""), Namespace.getNamespace("test", "http://www.example.com") };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPop_ThrowsExceptionWhenStackIsAlreadyEmpty() {
        NamespaceStack nsStack = new NamespaceStack();
        
        nsStack.pop();
    }
    
    @Test
    public void testAddedForward_ReturnsEmptyIterableWhenNoNamespacesAdded() {
        NamespaceStack nsStack = new NamespaceStack();
        
        Iterable<Namespace> iterable = nsStack.addedForward();
        Iterator<Namespace> iterator = iterable.iterator();
        
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testAddedForward_ReturnsIterableWithAddedNamespaces() {
        Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
        element.addNamespaceDeclaration(Namespace.getNamespace("test2", "http://www.example2.com"));
        NamespaceStack nsStack = new NamespaceStack();
        
        nsStack.push(element);
        
        Iterable<Namespace> iterable = nsStack.addedForward();
        Iterator<Namespace> iterator = iterable.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.getNamespace("test2", "http://www.example2.com"), iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testAddedReverse_ReturnsEmptyIterableWhenNoNamespacesAdded() {
        NamespaceStack nsStack = new NamespaceStack();
        
        Iterable<Namespace> iterable = nsStack.addedReverse();
        Iterator<Namespace> iterator = iterable.iterator();
        
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testAddedReverse_ReturnsIterableWithAddedNamespacesInReverseOrder() {
        Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
        element.addNamespaceDeclaration(Namespace.getNamespace("test2", "http://www.example2.com"));
        NamespaceStack nsStack = new NamespaceStack();
        
        nsStack.push(element);
        
        Iterable<Namespace> iterable = nsStack.addedReverse();
        Iterator<Namespace> iterator = iterable.iterator();
        
        assertTrue(iterator.hasNext());
        assertEquals(Namespace.getNamespace("test", "http://www.example.com"), iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testIterator_ReturnsIteratorOverNamespacesInStack() {
        Element element1 = new Element("testElement1", Namespace.getNamespace("test", "http://www.example.com"));
        Element element2 = new Element("testElement2", Namespace.getNamespace("test2", "http://www.example2.com"));
        NamespaceStack nsStack = new NamespaceStack();
        
        nsStack.push(element1);
        nsStack.push(element2);
        
        Namespace[] expectedNamespaces = { Namespace.getNamespace("", ""), Namespace.getNamespace("test", "http://www.example.com"), Namespace.getNamespace("test2", "http://www.example2.com") };
        Iterator<Namespace> iterator = nsStack.iterator();
        
        int index = 0;
        while (iterator.hasNext()) {
            assertEquals(expectedNamespaces[index], iterator.next());
            index++;
        }
        assertEquals(expectedNamespaces.length, index);
    }
    
    @Test
    public void testGetScope_ReturnsCopyOfCurrentScope() {
        Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
        NamespaceStack nsStack = new NamespaceStack();
        
        nsStack.push(element);
        Namespace[] scope = nsStack.getScope();
        scope[0] = Namespace.getNamespace("modified", "http://www.modified.com");
        
        Namespace[] expectedScope = { Namespace.getNamespace("", ""), Namespace.getNamespace("test", "http://www.example.com") };
        assertArrayEquals(expectedScope, nsStack.getScope());
    }
    
    @Test
    public void testIsInScope_ReturnsTrueForNamespaceInScope() {
        Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
        NamespaceStack nsStack = new NamespaceStack();
        
        nsStack.push(element);
        
        assertTrue(nsStack.isInScope(Namespace.getNamespace("test", "http://www.example.com")));
    }
    
    @Test
    public void testIsInScope_ReturnsFalseForNamespaceNotInScope() {
        Element element = new Element("testElement", Namespace.getNamespace("test", "http://www.example.com"));
        NamespaceStack nsStack = new NamespaceStack();
        
        nsStack.push(element);
        
        assertFalse(nsStack.isInScope(Namespace.getNamespace("test2", "http://www.example2.com")));
    }
}