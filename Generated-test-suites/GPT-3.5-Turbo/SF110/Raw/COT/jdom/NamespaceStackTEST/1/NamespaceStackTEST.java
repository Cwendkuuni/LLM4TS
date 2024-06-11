
import org.junit.Before;
import org.junit.Test;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

import static org.junit.Assert.*;

public class NamespaceStackTest {
    
    private NamespaceStack namespaceStack;
    
    @Before
    public void setUp() {
        namespaceStack = new NamespaceStack();
    }
    
    @Test
    public void testPushElement() {
        Element testElement = new Element("testElement");
        namespaceStack.push(testElement);
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, testElement.getNamespace()};
        assertArrayEquals(expectedScope, namespaceStack.getScope());
    }
    
    @Test
    public void testPushAttribute() {
        Element testElement = new Element("testElement");
        Attribute testAttribute = new Attribute("testAttribute", "value");
        testElement.setAttribute(testAttribute);
        namespaceStack.push(testAttribute);
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, testAttribute.getNamespace()};
        assertArrayEquals(expectedScope, namespaceStack.getScope());
    }
    
    @Test
    public void testPop() {
        Element testElement = new Element("testElement");
        namespaceStack.push(testElement);
        namespaceStack.pop();
        Namespace[] expectedScope = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope, namespaceStack.getScope());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPopEmptyStack() {
        namespaceStack.pop();
    }
    
    @Test
    public void testIsInScope() {
        Element testElement = new Element("testElement");
        namespaceStack.push(testElement);
        assertTrue(namespaceStack.isInScope(testElement.getNamespace()));
    }
    
    @Test
    public void testIsNotInScope() {
        Element testElement = new Element("testElement");
        assertFalse(namespaceStack.isInScope(testElement.getNamespace()));
    }
}
