import org.junit.Assert;
import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testPushElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test");
        stack.push(element);
        Assert.assertTrue(stack.isInScope(element.getNamespace()));
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("test", "value");
        stack.push(attribute);
        Assert.assertTrue(stack.isInScope(attribute.getNamespace()));
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
        Assert.assertEquals(0, stack.getScope().length);
    }

    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Iterable<Namespace> added = stack.addedForward();
        Assert.assertNotNull(added);
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        Iterable<Namespace> added = stack.addedReverse();
        Assert.assertNotNull(added);
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Iterable<Namespace> iterator = stack;
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace = new Namespace("test", "http://www.example.com");
        stack.push(new Element("test", namespace));
        Assert.assertTrue(stack.isInScope(namespace));
    }
}