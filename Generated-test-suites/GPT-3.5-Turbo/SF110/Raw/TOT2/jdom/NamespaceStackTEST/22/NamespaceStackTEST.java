
import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceStackTest {
    
    @Test
    public void testPushElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        stack.push(element);
        assertTrue(stack.isInScope(element.getNamespace()));
    }
    
    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        stack.push(element);
        stack.pop();
        assertFalse(stack.isInScope(element.getNamespace()));
    }
    
    @Test
    public void testPushAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("test", "value");
        stack.push(attribute);
        assertTrue(stack.isInScope(attribute.getNamespace()));
    }
}
