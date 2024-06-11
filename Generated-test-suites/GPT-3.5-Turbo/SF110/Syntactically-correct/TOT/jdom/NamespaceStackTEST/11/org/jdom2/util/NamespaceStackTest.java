import org.junit.Test;
import org.jdom2.Namespace;
import org.jdom2.Element;
import org.jdom2.Attribute;

import static org.junit.Assert.*;

public class NamespaceStackTest {
    
    @Test
    public void testPushMethod() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement", "http://www.example.com");
        
        stack.push(element);
        
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE,
                                Namespace.getNamespace("http://www.example.com")};
        assertArrayEquals(expected, stack.getScope());
    }
    
    @Test
    public void testPushMethod_withAdditionalNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement", "http://www.example.com");
        element.addNamespaceDeclaration(Namespace.getNamespace("ex", "http://www.example.com/ex"));
        
        stack.push(element);
        
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE,
                                Namespace.getNamespace("ex", "http://www.example.com/ex"),
                                Namespace.getNamespace("http://www.example.com")};
        assertArrayEquals(expected, stack.getScope());
    }
    
    @Test
    public void testPushMethod_withAttributes() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement", "http://www.example.com");
        element.setAttribute(new Attribute("attribute1", "value1", Namespace.getNamespace("attr", "http://www.example.com/attr")));
        
        stack.push(element);
        
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE,
                                Namespace.getNamespace("http://www.example.com"),
                                Namespace.getNamespace("attr", "http://www.example.com/attr")};
        assertArrayEquals(expected, stack.getScope());
    }
    
    @Test
    public void testPushMethod_withNoNamespace() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement");
        
        stack.push(element);
        
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expected, stack.getScope());
    }
    
    @Test
    public void testPushMethod_withAttributeWithoutNamespace() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement", "http://www.example.com");
        element.setAttribute(new Attribute("attribute1", "value1"));
        
        stack.push(element);
        
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE,
                                Namespace.getNamespace("http://www.example.com")};
        assertArrayEquals(expected, stack.getScope());
    }
    
    @Test
    public void testPushMethod_withNamespaceAlreadyInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement", "http://www.example.com");
        element.setAttribute(new Attribute("attribute1", "value1", Namespace.getNamespace("http://www.example.com")));
        
        stack.push(element);
        
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE,
                                Namespace.getNamespace("http://www.example.com")};
        assertArrayEquals(expected, stack.getScope());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPopMethod_withEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        
        stack.pop();
    }
    
    @Test
    public void testAddedForwardMethod_withEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        
        assertFalse(stack.addedForward().iterator().hasNext());
    }
    
    @Test
    public void testAddedForwardMethod_withNonEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Element element1 = new Element("testElement1", "http://www.example1.com");
        Element element2 = new Element("testElement2", "http://www.example2.com");
        element1.addNamespaceDeclaration(Namespace.getNamespace("ex1", "http://www.example1.com/ex1"));
        element2.addNamespaceDeclaration(Namespace.getNamespace("ex2", "http://www.example2.com/ex2"));
        
        stack.push(element1);
        stack.push(element2);
        
        Namespace[] expected = {Namespace.getNamespace("ex1", "http://www.example1.com/ex1"),
                                Namespace.getNamespace("ex2", "http://www.example2.com/ex2")};
        
        int i = 0;
        for (Namespace ns : stack.addedForward()) {
            assertEquals(expected[i++], ns);
        }
    }
    
    @Test
    public void testAddedReverseMethod_withEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        
        assertFalse(stack.addedReverse().iterator().hasNext());
    }
    
    @Test
    public void testAddedReverseMethod_withNonEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Element element1 = new Element("testElement1", "http://www.example1.com");
        Element element2 = new Element("testElement2", "http://www.example2.com");
        element1.addNamespaceDeclaration(Namespace.getNamespace("ex1", "http://www.example1.com/ex1"));
        element2.addNamespaceDeclaration(Namespace.getNamespace("ex2", "http://www.example2.com/ex2"));
        
        stack.push(element1);
        stack.push(element2);
        
        Namespace[] expected = {Namespace.getNamespace("ex2", "http://www.example2.com/ex2"),
                                Namespace.getNamespace("ex1", "http://www.example1.com/ex1")};
        
        int i = 0;
        for (Namespace ns : stack.addedReverse()) {
            assertEquals(expected[i++], ns);
        }
    }
    
    @Test
    public void testIsEmptyMethod_withEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        
        assertTrue(stack.isEmpty());
    }
    
    @Test
    public void testIsEmptyMethod_withNonEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement", "http://www.example.com");
        
        stack.push(element);
        
        assertFalse(stack.isEmpty());
    }
    
    @Test
    public void testIsInScopeMethod_withNamespaceInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement", "http://www.example.com");
        
        stack.push(element);
        
        assertTrue(stack.isInScope(Namespace.getNamespace("http://www.example.com")));
    }
    
    @Test
    public void testIsInScopeMethod_withNamespaceNotInScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement", "http://www.example.com");
        
        stack.push(element);
        
        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
    }

}