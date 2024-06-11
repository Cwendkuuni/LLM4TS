import org.jdom2.*;
import org.jdom2.util.NamespaceStack;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class NamespaceStackTest {

    private NamespaceStack namespaceStack;

    @Before
    public void setup() {
        Namespace[] seed = {Namespace.getNamespace("prefix1", "uri1"), Namespace.getNamespace("prefix2", "uri2")};
        namespaceStack = new NamespaceStack(seed);
    }

    @Test
    public void testPushWithElement_WhenAdditionalNamespacesExists_ShouldAddToStack() {
        Element element = new Element("element", "namespace");
        Namespace additionalNamespace1 = Namespace.getNamespace("prefix3", "uri3");
        Namespace additionalNamespace2 = Namespace.getNamespace("prefix4", "uri4");
        element.addNamespaceDeclaration(additionalNamespace1);
        element.addNamespaceDeclaration(additionalNamespace2);

        namespaceStack.push(element);

        List<Namespace> expected = Arrays.asList(Namespace.getNamespace("prefix1", "uri1"), Namespace.getNamespace("prefix2", "uri2"), additionalNamespace1, additionalNamespace2);
        Assert.assertArrayEquals(expected.toArray(), namespaceStack.getScope());
    }

    @Test
    public void testPushWithElement_WhenAdditionalNamespacesDoNotExist_ShouldAddToStack() {
        Element element = new Element("element");

        namespaceStack.push(element);

        List<Namespace> expected = Arrays.asList(Namespace.getNamespace("prefix1", "uri1"), Namespace.getNamespace("prefix2", "uri2"));
        Assert.assertArrayEquals(expected.toArray(), namespaceStack.getScope());
    }

    @Test
    public void testPushWithAttribute_ShouldAddToStack() {
        Attribute attribute = new Attribute("attribute", "value", Namespace.getNamespace("attrPrefix", "attrUri"));

        namespaceStack.push(attribute);

        List<Namespace> expected = Arrays.asList(Namespace.getNamespace("prefix1", "uri1"), Namespace.getNamespace("prefix2", "uri2"), Namespace.getNamespace("attrPrefix", "attrUri"));
        Assert.assertArrayEquals(expected.toArray(), namespaceStack.getScope());
    }

    @Test
    public void testPop_WhenStackNotEmpty_ShouldRemoveFromStack() {
        namespaceStack.pop();

        List<Namespace> expected = Arrays.asList(Namespace.getNamespace("prefix1", "uri1"));
        Assert.assertArrayEquals(expected.toArray(), namespaceStack.getScope());
    }

    @Test(expected = IllegalStateException.class)
    public void testPop_WhenStackIsEmpty_ShouldThrowException() {
        namespaceStack.pop();
        namespaceStack.pop();
    }

    @Test
    public void testAddedForward_WhenAddedNamespacesExists_ShouldReturnIteratorForAddedNamespaces() {
        Element element = new Element("element", "namespace");
        Namespace additionalNamespace1 = Namespace.getNamespace("prefix3", "uri3");
        Namespace additionalNamespace2 = Namespace.getNamespace("prefix4", "uri4");
        element.addNamespaceDeclaration(additionalNamespace1);
        element.addNamespaceDeclaration(additionalNamespace2);
        
        namespaceStack.push(element);

        Assert.assertTrue(namespaceStack.addedForward().iterator().hasNext());
    }

    @Test
    public void testAddedForward_WhenAddedNamespacesDoNotExist_ShouldReturnEmptyIterator() {
        Assert.assertFalse(namespaceStack.addedForward().iterator().hasNext());
    }

    @Test
    public void testAddedReverse_WhenAddedNamespacesExists_ShouldReturnIteratorForAddedNamespaces() {
        Element element = new Element("element", "namespace");
        Namespace additionalNamespace1 = Namespace.getNamespace("prefix3", "uri3");
        Namespace additionalNamespace2 = Namespace.getNamespace("prefix4", "uri4");
        element.addNamespaceDeclaration(additionalNamespace1);
        element.addNamespaceDeclaration(additionalNamespace2);

        namespaceStack.push(element);

        Assert.assertTrue(namespaceStack.addedReverse().iterator().hasNext());
    }

    @Test
    public void testAddedReverse_WhenAddedNamespacesDoNotExist_ShouldReturnEmptyIterator() {
        Assert.assertFalse(namespaceStack.addedReverse().iterator().hasNext());
    }

    @Test
    public void testIterator_WhenNamespacesExistInStack_ShouldReturnIteratorForNamespaces() {
        Assert.assertTrue(namespaceStack.iterator().hasNext());
    }

    @Test
    public void testIterator_WhenNoNamespacesExistInStack_ShouldReturnEmptyIterator() {
        namespaceStack.pop();
        namespaceStack.pop();

        Assert.assertFalse(namespaceStack.iterator().hasNext());
    }

    @Test
    public void testGetScope_WhenCalled_ShouldReturnCopyOfCurrentScope() {
        Namespace[] actual = namespaceStack.getScope();
        Namespace[] expected = namespaceStack.getScope();
        expected[0] = Namespace.getNamespace("newPrefix", "newUri");

        Assert.assertNotEquals(expected[0], actual[0]);
    }

    @Test
    public void testIsInScope_WhenNamespaceIsInCurrentScope_ShouldReturnTrue() {
        Assert.assertTrue(namespaceStack.isInScope(Namespace.getNamespace("prefix1", "uri1")));
    }

    @Test
    public void testIsInScope_WhenNamespaceIsNotInCurrentScope_ShouldReturnFalse() {
        Assert.assertFalse(namespaceStack.isInScope(Namespace.getNamespace("prefix3", "uri3")));
    }

    @Test
    public void testIsInScope_WhenNamespaceIsFirstInCurrentScope_ShouldReturnTrue() {
        Assert.assertTrue(namespaceStack.isInScope(Namespace.getNamespace("newPrefix", "newUri")));
    }

    @Test
    public void testEmptyIterator_WhenNextIsCalled_ShouldThrowNoSuchElementException() {
        Iterator<Namespace> iterator = namespaceStack.EMPTYITER.iterator();

        Assert.assertThrows(NoSuchElementException.class, iterator::next);
    }
}