
import org.junit.Assert;
import org.junit.Test;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class NamespaceStackTest {

    @Test
    public void testNamespaceStackConstructor() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};

        Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
    }

    @Test
    public void testNamespaceStackPushWithElement() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("element", Namespace.getNamespace("new_namespace"));

        namespaceStack.push(element);
        Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.getNamespace("new_namespace")};

        Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
    }

    @Test
    public void testNamespaceStackPushWithAttribute() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Attribute attribute = new Attribute("attribute", "value", Namespace.getNamespace("new_namespace"));

        namespaceStack.push(attribute);
        Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE, Namespace.getNamespace("new_namespace")};

        Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());
    }

    @Test(expected = IllegalStateException.class)
    public void testNamespaceStackPop() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("element", Namespace.getNamespace("new_namespace"));
        namespaceStack.push(element);

        namespaceStack.pop();

        Namespace[] expectedScope = new Namespace[]{Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        Assert.assertArrayEquals(expectedScope, namespaceStack.getScope());

        namespaceStack.pop(); // should throw IllegalStateException
    }

    @Test
    public void testNamespaceStackIsInScope() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("element", Namespace.getNamespace("new_namespace"));
        namespaceStack.push(element);

        Assert.assertTrue(namespaceStack.isInScope(Namespace.getNamespace("new_namespace")));
        Assert.assertFalse(namespaceStack.isInScope(Namespace.getNamespace("another_namespace")));
    }

    @Test
    public void testAddedForward() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("element", Namespace.getNamespace("new_namespace"));
        namespaceStack.push(element);

        List<Namespace> expectedNamespaces = new ArrayList<>();
        expectedNamespaces.add(Namespace.NO_NAMESPACE);
        expectedNamespaces.add(Namespace.XML_NAMESPACE);
        expectedNamespaces.add(Namespace.getNamespace("new_namespace"));

        int i = 0;
        for (Namespace ns : namespaceStack.addedForward()) {
            Assert.assertEquals(expectedNamespaces.get(i), ns);
            i++;
        }
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack namespaceStack = new NamespaceStack();
        Element element = new Element("element", Namespace.getNamespace("new_namespace"));
        namespaceStack.push(element);

        List<Namespace> expectedNamespaces = new ArrayList<>();
        expectedNamespaces.add(Namespace.getNamespace("new_namespace"));
        expectedNamespaces.add(Namespace.XML_NAMESPACE);
        expectedNamespaces.add(Namespace.NO_NAMESPACE);

        int i = 0;
        for (Namespace ns : namespaceStack.addedReverse()) {
            Assert.assertEquals(expectedNamespaces.get(i), ns);
            i++;
        }
    }
}
