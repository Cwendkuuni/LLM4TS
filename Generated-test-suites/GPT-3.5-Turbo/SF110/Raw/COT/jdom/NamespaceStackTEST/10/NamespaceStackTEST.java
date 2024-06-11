import org.jdom2.util.NamespaceStack;
import org.jdom2.Namespace;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {
    private NamespaceStack nStack;
    
    @Before
    public void setup() {
        nStack = new NamespaceStack();
    }
    
    @Test
    public void testPushElement() {
        Namespace namespace1 = Namespace.getNamespace("prefix1");
        Namespace[] expectedScope = { namespace1 };
        nStack.push(new Element("element1", namespace1));
        assertArrayEquals(expectedScope, nStack.getScope());
    }
    
    @Test
    public void testPushAttribute() {
        Namespace namespace1 = Namespace.getNamespace("prefix1");
        Namespace[] expectedScope = { namespace1 };
        nStack.push(new Attribute("attribute1", "value1", namespace1));
        assertArrayEquals(expectedScope, nStack.getScope());
    }
    
    @Test(expected=IllegalStateException.class)
    public void testPopThrowsExceptionWhenEmpty() {
        nStack.pop();
    }

    @Test
    public void testIsInScope() {
        Namespace namespace1 = Namespace.getNamespace("prefix1");
        nStack.push(new Element("element1", namespace1));
        assertTrue(nStack.isInScope(namespace1));
    }
    
    @Test
    public void testIsNotInScope() {
        Namespace namespace1 = Namespace.getNamespace("prefix1");
        Namespace namespace2 = Namespace.getNamespace("prefix2");
        nStack.push(new Element("element1", namespace1));
        assertFalse(nStack.isInScope(namespace2));
    }
}