
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceStackTest {

    private NamespaceStack stack;

    @Before
    public void setUp() {
        stack = new NamespaceStack();
    }

    @Test
    public void testPush_ElementWithNoAdditionalNamespaces() {
        Element element = new Element("test");
        stack.push(element);

        Namespace[] expectedScope = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE };
        Namespace[] actualScope = stack.getScope();

        assertArrayEquals(expectedScope, actualScope);
    }

    @Test
    public void testPush_ElementWithAdditionalNamespaces() {
        Element element = new Element("test");
        Namespace additionalNamespace1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace additionalNamespace2 = Namespace.getNamespace("prefix2", "uri2");
        element.addNamespaceDeclaration(additionalNamespace1);
        element.addNamespaceDeclaration(additionalNamespace2);

        stack.push(element);

        Namespace[] expectedScope = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, additionalNamespace1, additionalNamespace2 };
        Namespace[] actualScope = stack.getScope();

        assertArrayEquals(expectedScope, actualScope);
    }

    @Test
    public void testPush_ElementWithAttributes() {
        Element element = new Element("test");
        Attribute attribute = new Attribute("attr_name", "attr_value", Namespace.getNamespace("attr_prefix", "attr_uri"));
        element.setAttribute(attribute);

        stack.push(element);

        Namespace[] expectedScope = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, attribute.getNamespace() };
        Namespace[] actualScope = stack.getScope();

        assertArrayEquals(expectedScope, actualScope);
    }

    @Test(expected = IllegalStateException.class)
    public void testPop_EmptyStack() {
        stack.pop();
    }

    @Test
    public void testPop_NonEmptyStack() {
        Element element = new Element("test");
        stack.push(element);

        stack.pop();

        Namespace[] expectedScope = new Namespace[] { Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE };
        Namespace[] actualScope = stack.getScope();

        assertArrayEquals(expectedScope, actualScope);
    }

    @Test
    public void testIsInScope_True() {
        Element element = new Element("test");
        stack.push(element);

        boolean isInScope = stack.isInScope(element.getNamespace());

        assertTrue(isInScope);
    }

    @Test
    public void testIsInScope_False() {
        Element element = new Element("test");

        boolean isInScope = stack.isInScope(element.getNamespace());

        assertFalse(isInScope);
    }
}

