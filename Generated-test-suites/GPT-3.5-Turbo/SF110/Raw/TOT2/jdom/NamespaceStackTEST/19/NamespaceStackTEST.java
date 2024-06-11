import org.jdom2.util.NamespaceStack;
import org.jdom2.Namespace;
import org.jdom2.Element;
import org.jdom2.Attribute;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

    @Test
    public void testPushElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement");
        stack.push(element);
        assertTrue(stack.isInScope(element.getNamespace()));
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("testAttribute", "value");
        stack.push(attribute);
        assertTrue(stack.isInScope(attribute.getNamespace()));
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement");
        stack.push(element);
        stack.pop();
        assertFalse(stack.isInScope(element.getNamespace()));
    }

    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement");
        stack.push(element);
        assertNotNull(stack.addedForward());
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement");
        stack.push(element);
        assertNotNull(stack.addedReverse());
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement");
        stack.push(element);
        assertNotNull(stack.iterator());
    }

    @Test
    public void testGetScope() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement");
        stack.push(element);
        assertNotNull(stack.getScope());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testPopEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

    @Test
    public void testIsInScopeFalse() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("testElement");
        assertFalse(stack.isInScope(element.getNamespace()));
    }
}