package org.jdom2.util;

import org.jdom2.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class NamespaceStackTest {

    private NamespaceStack stack;

    @Before
    public void setUp() {
        stack = new NamespaceStack();
    }

    @Test
    public void testPushElement() {
        Element element = new Element("test");
        stack.push(element);
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expected, stack.getScope());
    }

    @Test
    public void testPushAttribute() {
        Attribute attribute = new Attribute("test", "value");
        stack.push(attribute);
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expected, stack.getScope());
    }

    @Test(expected = IllegalStateException.class)
    public void testPop() {
        stack.pop();
    }

    @Test
    public void testIsInScope() {
        Namespace ns = Namespace.getNamespace("test");
        assertFalse(stack.isInScope(ns));
        Element element = new Element("test", ns);
        stack.push(element);
        assertTrue(stack.isInScope(ns));
    }

    // Additional test cases can be added for other methods in the class

}