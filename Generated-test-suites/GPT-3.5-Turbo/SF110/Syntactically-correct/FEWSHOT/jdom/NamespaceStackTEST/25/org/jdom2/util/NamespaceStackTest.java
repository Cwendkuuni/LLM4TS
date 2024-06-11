package org.jdom2.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class NamespaceStackTest {

    @Test
    public void testPush() {
        // Test push stack
        NamespaceStack stack = new NamespaceStack();
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");
        Namespace ns3 = Namespace.getNamespace("prefix3", "uri3");
        stack.push(ns1);
        stack.push(ns2);
        stack.push(ns3);

        Namespace[] expectedScope = {ns3, ns2, ns1};
        assertArrayEquals(expectedScope, stack.getScope());

        // Test push with element
        Element element = new Element("element", "namespace_uri");
        element.addNamespaceDeclaration(ns2);
        element.addNamespaceDeclaration(ns1);
        stack.push(element);

        Namespace[] expectedScope2 = {ns3, ns2, ns1, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope2, stack.getScope());
    }

    @Test
    public void testPop() {
        NamespaceStack stack = new NamespaceStack();
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");

        // Test pop with element
        Element element = new Element("element", "namespace_uri");
        element.addNamespaceDeclaration(ns1);
        stack.push(element);
        stack.push(ns2);
        stack.pop();

        Namespace[] expectedScope = {ns1, Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope, stack.getScope());

        // Test pop stack
        stack.pop();
        Namespace[] expectedScope2 = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expectedScope2, stack.getScope());
    }

    @Test
    public void testIsInScope() {
        NamespaceStack stack = new NamespaceStack();
        Namespace ns1 = Namespace.getNamespace("prefix1", "uri1");
        Namespace ns2 = Namespace.getNamespace("prefix2", "uri2");

        stack.push(ns1);
        stack.push(ns2);

        assertTrue(stack.isInScope(ns1));
        assertFalse(stack.isInScope(Namespace.getNamespace("prefix3", "uri3")));

        stack.pop();

        assertFalse(stack.isInScope(ns1));
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix3", "uri3")));
    }
}