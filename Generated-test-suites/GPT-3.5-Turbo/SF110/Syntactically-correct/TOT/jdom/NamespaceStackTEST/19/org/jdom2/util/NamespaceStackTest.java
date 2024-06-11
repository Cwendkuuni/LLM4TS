import org.jdom2.*;
import org.jdom2.util.*;
import org.junit.*;

public class NamespaceStackTest {

    @Test
    public void testPushWithElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        Namespace namespace = Namespace.getNamespace("prefix", "http://www.example.com");

        element.setNamespace(namespace);

        stack.push(element);

        Assert.assertEquals(true, stack.isInScope(namespace));
    }

    @Test
    public void testPushWithAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("attr", "value", Namespace.getNamespace("prefix", "http://www.example.com"));

        stack.push(attribute);

        Assert.assertEquals(true, stack.isInScope(attribute.getNamespace()));
    }

    @Test(expected = IllegalStateException.class)
    public void testPopWithEmptyStack() {
        NamespaceStack stack = new NamespaceStack();

        stack.pop();
    }

    @Test(expected = NoSuchElementException.class)
    public void testAddedForwardWithEmptyStack() {
        NamespaceStack stack = new NamespaceStack();

        stack.addedForward().iterator().next();
    }

    @Test(expected = NoSuchElementException.class)
    public void testAddedReverseWithEmptyStack() {
        NamespaceStack stack = new NamespaceStack();

        stack.addedReverse().iterator().next();
    }

    @Test
    public void testIteratorWithEmptyStack() {
        NamespaceStack stack = new NamespaceStack();

        Assert.assertFalse(stack.iterator().hasNext());
    }

    @Test
    public void testGetScopeWithEmptyStack() {
        NamespaceStack stack = new NamespaceStack();

        Namespace[] scope = stack.getScope();

        Assert.assertEquals(0, scope.length);
    }

    @Test
    public void testIsInScopeWithEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace = Namespace.getNamespace("prefix", "http://www.example.com");

        boolean inScope = stack.isInScope(namespace);

        Assert.assertFalse(inScope);
    }

    @Test
    public void testPushWithAdditionalNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");

        Namespace namespace1 = Namespace.getNamespace("prefix1", "http://www.example1.com");
        Namespace namespace2 = Namespace.getNamespace("prefix2", "http://www.example2.com");

        element.setNamespace(namespace1);
        element.addNamespaceDeclaration(namespace2);

        stack.push(element);

        Assert.assertTrue(stack.isInScope(namespace1));
        Assert.assertTrue(stack.isInScope(namespace2));
    }

    @Test
    public void testPushWithAttributes() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");

        Attribute attribute1 = new Attribute("attr1", "value1", Namespace.getNamespace("prefix1", "http://www.example1.com"));
        Attribute attribute2 = new Attribute("attr2", "value2", Namespace.getNamespace("prefix2", "http://www.example2.com"));

        element.setAttribute(attribute1);
        element.setAttribute(attribute2);

        stack.push(element);

        Assert.assertTrue(stack.isInScope(attribute1.getNamespace()));
        Assert.assertTrue(stack.isInScope(attribute2.getNamespace()));
    }

    @Test
    public void testPushAndPop() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        Namespace namespace = Namespace.getNamespace("prefix", "http://www.example.com");

        element.setNamespace(namespace);

        stack.push(element);
        stack.pop();

        Assert.assertFalse(stack.isInScope(namespace));
    }

}