import org.junit.*;
import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.util.*;

public class NamespaceStackTest {
    
    @Test
    public void testPush_Element() {
        NamespaceStack nsStack = new NamespaceStack();
        Element element = new Element("test", Namespace.getNamespace("http://www.example.com"));
        nsStack.push(element);
        Namespace[] expectedScope = {Namespace.getNamespace("http://www.example.com")};
        assertArrayEquals(expectedScope, nsStack.getScope());
        assertTrue(nsStack.isInScope(Namespace.getNamespace("http://www.example.com")));
        assertFalse(nsStack.isInScope(Namespace.getNamespace("http://www.example.org")));
    }
    
    @Test
    public void testPush_Element_AdditionalNamespaces() {
        NamespaceStack nsStack = new NamespaceStack();
        Element element = new Element("test", Namespace.getNamespace("http://www.example.com"));
        Namespace additionalNamespace = Namespace.getNamespace("additional", "http://www.example.org");
        element.addNamespaceDeclaration(additionalNamespace);
        nsStack.push(element);
        Namespace[] expectedScope = {Namespace.getNamespace("http://www.example.com"), Namespace.getNamespace("additional", "http://www.example.org")};
        assertArrayEquals(expectedScope, nsStack.getScope());
        assertTrue(nsStack.isInScope(Namespace.getNamespace("http://www.example.com")));
        assertTrue(nsStack.isInScope(Namespace.getNamespace("additional", "http://www.example.org")));
        assertFalse(nsStack.isInScope(Namespace.getNamespace("http://www.example.org")));
    }
    
    @Test
    public void testPush_Element_Attributes() {
        NamespaceStack nsStack = new NamespaceStack();
        Element element = new Element("test", Namespace.getNamespace("http://www.example.com"));
        Attribute attribute1 = new Attribute("attr1", "value1");
        Attribute attribute2 = new Attribute("attr2", "value2", Namespace.getNamespace("additional", "http://www.example.org"));
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);
        nsStack.push(element);
        Namespace[] expectedScope = {Namespace.getNamespace("http://www.example.com"), Namespace.getNamespace("additional", "http://www.example.org")};
        assertArrayEquals(expectedScope, nsStack.getScope());
        assertTrue(nsStack.isInScope(Namespace.getNamespace("http://www.example.com")));
        assertTrue(nsStack.isInScope(Namespace.getNamespace("additional", "http://www.example.org")));
        assertFalse(nsStack.isInScope(Namespace.getNamespace("http://www.example.org")));
    }
    
    @Test
    public void testPush_Attribute() {
        NamespaceStack nsStack = new NamespaceStack();
        Attribute attribute = new Attribute("attr1", "value1", Namespace.getNamespace("http://www.example.com"));
        nsStack.push(attribute);
        Namespace[] expectedScope = {Namespace.getNamespace("http://www.example.com")};
        assertArrayEquals(expectedScope, nsStack.getScope());
        assertTrue(nsStack.isInScope(Namespace.getNamespace("http://www.example.com")));
        assertFalse(nsStack.isInScope(Namespace.getNamespace("http://www.example.org")));
    }
    
    @Test(expected=IllegalStateException.class)
    public void testPop() {
        NamespaceStack nsStack = new NamespaceStack();
        nsStack.pop();
        fail("Expected IllegalStateException to be thrown");
    }
    
    @Test
    public void testIterator() {
        NamespaceStack nsStack = new NamespaceStack();
        Element element = new Element("test", Namespace.getNamespace("http://www.example.com"));
        nsStack.push(element);
        Namespace[] expectedScope = {Namespace.getNamespace("http://www.example.com")};
        assertArrayEquals(expectedScope, nsStack.getScope());
        
        StringBuilder result = new StringBuilder();
        for (Namespace ns : nsStack) {
            result.append(ns.getURI()).append(" ");
        }
        
        assertEquals("http://www.example.com ", result.toString());
    }
}