package org.jdom2.util;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.util.NamespaceStack;

public class NamespaceStackTest {

    @Test
    public void testPushElement() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", Namespace.NO_NAMESPACE);

        stack.push(element);

        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
    }

    @Test
    public void testPushAttribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("test", "value", Namespace.NO_NAMESPACE);

        stack.push(attribute);

        assertTrue(stack.isInScope(Namespace.NO_NAMESPACE));
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        stack.push(element);

        stack.pop();

        assertFalse(stack.isInScope(Namespace.NO_NAMESPACE));
    }

    @Test
    public void testAddedForward() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        stack.push(element);

        Iterable<Namespace> added = stack.addedForward();

        assertNotNull(added);
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        stack.push(element);

        Iterable<Namespace> added = stack.addedReverse();

        assertNotNull(added);
    }

    @Test
    public void testIterator() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        stack.push(element);

        Iterable<Namespace> iterable = stack;
        assertNotNull(iterable.iterator());
    }

}