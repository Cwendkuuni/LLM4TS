import org.junit.Test;
import org.jdom2.*;

public class NamespaceStackTest {

    @Test
    public void testPush_Element() {
        // Typical Use Case
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("testNamespace"));

        // Edge Case
        element.addNamespaceDeclaration(Namespace.getNamespace("testPrefix", "testNamespace"));

        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
    }

    @Test
    public void testPush_Element_EmptyNamespace() {
        // Error Scenario
        Element element = new Element("test");

        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
    }

    @Test
    public void testPush_Element_NoAdditionalNamespaces() {
        // Typical Use Case
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("testNamespace"));

        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
    }

    @Test
    public void testPush_Element_NoAttributes() {
        // Typical Use Case
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("testNamespace"));

        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
    }

    @Test
    public void testPush_Element_EmptyAttributeNamespace() {
        // Error Scenario
        Element element = new Element("test");
        element.setAttribute("attribute", "value", Namespace.NO_NAMESPACE);

        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
    }

    @Test
    public void testPop() {
        // Typical Use Case
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("testNamespace"));

        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        stack.pop();
    }

    @Test(expected = IllegalStateException.class)
    public void testPop_Underflow() {
        // Error Scenario
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

    @Test
    public void testIsInScope_True() {
        // Typical Use Case
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("testNamespace"));

        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        stack.isInScope(Namespace.getNamespace("testNamespace"));
    }

    @Test
    public void testIsInScope_False() {
        // Typical Use Case
        Element element = new Element("test");
        element.setNamespace(Namespace.getNamespace("testNamespace"));

        NamespaceStack stack = new NamespaceStack();
        stack.push(element);
        stack.isInScope(Namespace.getNamespace("otherNamespace"));
    }

    @Test
    public void testIterator() {
        // Typical Use Case
        Element element1 = new Element("test1");
        element1.setNamespace(Namespace.getNamespace("testNamespace1"));

        Element element2 = new Element("test2");
        element2.setNamespace(Namespace.getNamespace("testNamespace2"));

        NamespaceStack stack = new NamespaceStack();
        stack.push(element1);
        stack.push(element2);

        for (Namespace ns : stack) {
            System.out.println(ns);
        }
    }

    @Test
    public void testAddedForward() {
        // Typical Use Case
        Element element1 = new Element("test1");
        element1.setNamespace(Namespace.getNamespace("testNamespace1"));

        Element element2 = new Element("test2");
        element2.setNamespace(Namespace.getNamespace("testNamespace2"));

        NamespaceStack stack = new NamespaceStack();
        stack.push(element1);
        stack.push(element2);

        for (Namespace ns : stack.addedForward()) {
            System.out.println(ns);
        }
    }

    @Test
    public void testAddedReverse() {
        // Typical Use Case
        Element element1 = new Element("test1");
        element1.setNamespace(Namespace.getNamespace("testNamespace1"));

        Element element2 = new Element("test2");
        element2.setNamespace(Namespace.getNamespace("testNamespace2"));

        NamespaceStack stack = new NamespaceStack();
        stack.push(element1);
        stack.push(element2);

        for (Namespace ns : stack.addedReverse()) {
            System.out.println(ns);
        }
    }
}