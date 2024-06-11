import org.jdom2.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class NamespaceStackTest {

    @Test
    public void testConstructor() {
        NamespaceStack stack = new NamespaceStack();
        Namespace[] expectedScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE };
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test
    public void testPushWithElement() {
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("test", "http://www.example.com"));
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        Namespace[] expectedScope = { Namespace.getNamespace("test", "http://www.example.com") };
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test
    public void testPushWithAttribute() {
        Attribute attribute = new Attribute("test", "value", Namespace.getNamespace("test", "http://www.example.com"));
        NamespaceStack stack = new NamespaceStack();
        stack.push(attribute);
        Namespace[] expectedScope = { Namespace.getNamespace("test", "http://www.example.com") };
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
        Namespace[] expectedScope = { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE };
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        stack.push(new Element("test"));
        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
    }
    
    @Test(expected=IllegalStateException.class)
    public void testPopWhenEmpty() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
        stack.pop();
    }

    // Additional test cases for edge cases and exceptions
    
    @Test
    public void testPushWithElement_WithAdditionalNamespaces() {
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("test", "http://www.example.com"));
        element.addNamespaceDeclaration(Namespace.getNamespace("example", "http://www.example.com"));
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        Namespace[] expectedScope = { Namespace.getNamespace("test", "http://www.example.com"), Namespace.getNamespace("example", "http://www.example.com") };
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test
    public void testPushWithElement_WithAttributes() {
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("test", "http://www.example.com"));
        element.setAttribute(new Attribute("attr", "value", Namespace.getNamespace("example", "http://www.example.com")));
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        Namespace[] expectedScope = { Namespace.getNamespace("test", "http://www.example.com"), Namespace.getNamespace("example", "http://www.example.com") };
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test
    public void testPushWithElement_SameNamespaceAsTopOfStack() {
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("test", "http://www.example.com"));
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        stack.push(element);
        Namespace[] expectedScope = { Namespace.getNamespace("test", "http://www.example.com") };
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test
    public void testPushWithAttribute_SameNamespaceAsTopOfStack() {
        Attribute attribute = new Attribute("test", "value", Namespace.getNamespace("test", "http://www.example.com"));
        NamespaceStack stack = new NamespaceStack();
        stack.push(attribute);
        stack.push(attribute);
        Namespace[] expectedScope = { Namespace.getNamespace("test", "http://www.example.com") };
        assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test
    public void testAddedForward_EmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        assertFalse(stack.addedForward().iterator().hasNext());
    }

    @Test
    public void testAddedReverse_EmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        assertFalse(stack.addedReverse().iterator().hasNext());
    }

    @Test
    public void testIterator_EmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        assertFalse(stack.iterator().hasNext());
    }

    @Test
    public void testIterator_NotEmptyStack() {
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("test", "http://www.example.com"));
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        assertTrue(stack.iterator().hasNext());
    }
    
    @Test
    public void testPushElement_AdditionalNamespacesSorted() {
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("test", "http://www.example.com"));
        element.addNamespaceDeclaration(Namespace.getNamespace("example", "http://www.example.com"));
        element.addNamespaceDeclaration(Namespace.getNamespace("abc", "http://www.example.com"));
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        Namespace[] expectedScope = { Namespace.getNamespace("abc", "http://www.example.com"), Namespace.getNamespace("example", "http://www.example.com"), Namespace.getNamespace("test", "http://www.example.com") };
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
    @Test
    public void testPushElement_AdditionalNamespacesWithSamePrefix() {
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("test", "http://www.example.com"));
        element.addNamespaceDeclaration(Namespace.getNamespace("test", "http://www.example2.com"));
        element.addNamespaceDeclaration(Namespace.getNamespace("test", "http://www.example3.com"));
        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        Namespace[] expectedScope = { Namespace.getNamespace("test", "http://www.example2.com"), Namespace.getNamespace("test", "http://www.example.com") };
        assertArrayEquals(expectedScope, stack.getScope());
    }
    
}

