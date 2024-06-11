import org.jdom2.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class NamespaceStackTest {

    @Test
    public void testEmptyNamespaceStack() {
        NamespaceStack stack = new NamespaceStack();
        Assert.assertEquals(2, stack.getScope().length);
        Assert.assertTrue(stack.getScope()[0] == Namespace.NO_NAMESPACE);
        Assert.assertTrue(stack.getScope()[1] == Namespace.XML_NAMESPACE);
    }

    @Test
    public void testNamespaceStackWithSeed() {
        Namespace[] seed = new Namespace[]{Namespace.getNamespace("abc", "def")};
        NamespaceStack stack = new NamespaceStack(seed);
        Assert.assertEquals(2, stack.getScope().length);
        Assert.assertTrue(stack.getScope()[0] == Namespace.NO_NAMESPACE);
        Assert.assertTrue(stack.getScope()[1] == Namespace.XML_NAMESPACE);

        Iterator<Namespace> iterator = stack.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next() == Namespace.getNamespace("abc", "def"));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testPushWithElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", Namespace.getNamespace("abc", "def"));
        stack.push(element);

        Assert.assertEquals(3, stack.getScope().length);
        Assert.assertTrue(stack.getScope()[0] == Namespace.getNamespace("abc", "def"));
        Assert.assertTrue(stack.getScope()[1] == Namespace.NO_NAMESPACE);
        Assert.assertTrue(stack.getScope()[2] == Namespace.XML_NAMESPACE);

        Iterator<Namespace> iterator = stack.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next() == Namespace.getNamespace("abc", "def"));
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next() == Namespace.NO_NAMESPACE);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testPushWithAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("test", "value", Namespace.getNamespace("abc", "def"));
        stack.push(attribute);

        Assert.assertEquals(3, stack.getScope().length);
        Assert.assertTrue(stack.getScope()[0] == Namespace.getNamespace("abc", "def"));
        Assert.assertTrue(stack.getScope()[1] == Namespace.NO_NAMESPACE);
        Assert.assertTrue(stack.getScope()[2] == Namespace.XML_NAMESPACE);

        Iterator<Namespace> iterator = stack.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next() == Namespace.getNamespace("abc", "def"));
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next() == Namespace.NO_NAMESPACE);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testPushWithElementAndAdditionalNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", Namespace.getNamespace("abc", "def"));
        element.addNamespaceDeclaration(Namespace.getNamespace("ghi", "jkl"));
        stack.push(element);

        Assert.assertEquals(4, stack.getScope().length);
        Assert.assertTrue(stack.getScope()[0] == Namespace.getNamespace("abc", "def"));
        Assert.assertTrue(stack.getScope()[1] == Namespace.NO_NAMESPACE);
        Assert.assertTrue(stack.getScope()[2] == Namespace.getNamespace("ghi", "jkl"));
        Assert.assertTrue(stack.getScope()[3] == Namespace.XML_NAMESPACE);

        Iterator<Namespace> iterator = stack.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next() == Namespace.getNamespace("abc", "def"));
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next() == Namespace.NO_NAMESPACE);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next() == Namespace.getNamespace("ghi", "jkl"));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testPushWithElementAndAttributes() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", Namespace.getNamespace("abc", "def"));
        element.addNamespaceDeclaration(Namespace.getNamespace("ghi", "jkl"));
        element.setAttribute(new Attribute("attr1", "value1", Namespace.getNamespace("mno", "pqr")));
        element.setAttribute(new Attribute("attr2", "value2", Namespace.getNamespace("stu", "vwx")));
        stack.push(element);

        Assert.assertEquals(6, stack.getScope().length);
        Assert.assertTrue(stack.getScope()[0] == Namespace.getNamespace("abc", "def"));
        Assert.assertTrue(stack.getScope()[1] == Namespace.NO_NAMESPACE);
        Assert.assertTrue(stack.getScope()[2] == Namespace.getNamespace("ghi", "jkl"));
        Assert.assertTrue(stack.getScope()[3] == Namespace.getNamespace("mno", "pqr"));
        Assert.assertTrue(stack.getScope()[4] == Namespace.getNamespace("stu", "vwx"));
        Assert.assertTrue(stack.getScope()[5] == Namespace.XML_NAMESPACE);

        Iterator<Namespace> iterator = stack.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next() == Namespace.getNamespace("abc", "def"));
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next() == Namespace.NO_NAMESPACE);
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next() == Namespace.getNamespace("ghi", "jkl"));
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next() == Namespace.getNamespace("mno", "pqr"));
        Assert.assertTrue(iterator.hasNext());
        Assert.assertTrue(iterator.next() == Namespace.getNamespace("stu", "vwx"));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Assert.assertEquals(2, stack.getScope().length);

        stack.pop();
        Assert.assertEquals(2, stack.getScope().length);

        Element element = new Element("test", Namespace.getNamespace("abc", "def"));
        stack.push(element);
        Assert.assertEquals(3, stack.getScope().length);
        
        stack.pop();
        Assert.assertEquals(2, stack.getScope().length);
    }

    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Element element1 = new Element("test", Namespace.getNamespace("abc", "def"));
        Element element2 = new Element("test", Namespace.getNamespace("ghi", "jkl"));
        stack.push(element1);
        stack.push(element2);

        List<Namespace> addedNamespaces = new ArrayList<>();
        for (Namespace namespace : stack.addedForward()) {
            addedNamespaces.add(namespace);
        }

        Assert.assertEquals(2, addedNamespaces.size());
        Assert.assertTrue(addedNamespaces.get(0) == Namespace.getNamespace("abc", "def"));
        Assert.assertTrue(addedNamespaces.get(1) == Namespace.getNamespace("ghi", "jkl"));
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        Element element1 = new Element("test", Namespace.getNamespace("abc", "def"));
        Element element2 = new Element("test", Namespace.getNamespace("ghi", "jkl"));
        stack.push(element1);
        stack.push(element2);

        List<Namespace> addedNamespaces = new ArrayList<>();
        for (Namespace namespace : stack.addedReverse()) {
            addedNamespaces.add(namespace);
        }

        Assert.assertEquals(2, addedNamespaces.size());
        Assert.assertTrue(addedNamespaces.get(0) == Namespace.getNamespace("ghi", "jkl"));
        Assert.assertTrue(addedNamespaces.get(1) == Namespace.getNamespace("abc", "def"));
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element element1 = new Element("test", Namespace.getNamespace("abc", "def"));
        Element element2 = new Element("test", Namespace.getNamespace("ghi", "jkl"));
        stack.push(element1);
        stack.push(element2);

        List<Namespace> namespaces = new ArrayList<>();
        for (Namespace namespace : stack) {
            namespaces.add(namespace);
        }

        Assert.assertEquals(3, namespaces.size());
        Assert.assertTrue(namespaces.get(0) == Namespace.getNamespace("ghi", "jkl"));
        Assert.assertTrue(namespaces.get(1) == Namespace.NO_NAMESPACE);
        Assert.assertTrue(namespaces.get(2) == Namespace.getNamespace("abc", "def"));
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Namespace namespace1 = Namespace.getNamespace("abc", "def");
        Namespace namespace2 = Namespace.getNamespace("ghi", "jkl");

        stack.push(new Element("test1", namespace1));
        stack.push(new Element("test2", namespace2));

        Assert.assertTrue(stack.isInScope(namespace1));
        Assert.assertTrue(stack.isInScope(namespace2));
        Assert.assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
        Assert.assertFalse(stack.isInScope(Namespace.XML_NAMESPACE));
    }
}