package org.jdom2.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.*;

public class NamespaceStackTest {
    @Test
    public void testConstructor_DefaultSeed() {
        NamespaceStack stack = new NamespaceStack();
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expected, stack.getScope());
    }
    
    @Test
    public void testConstructor_CustomSeed() {
        Namespace[] seed = {Namespace.getNamespace("prefix", "uri")};
        NamespaceStack stack = new NamespaceStack(seed);
        Namespace[] expected = {Namespace.getNamespace("prefix", "uri")};
        assertArrayEquals(expected, stack.getScope());
    }
    
    @Test
    public void testPush_Element() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("element", Namespace.getNamespace("prefix", "uri"));
        stack.push(element);
        Namespace[] expected = {
            Namespace.NO_NAMESPACE,
            Namespace.getNamespace("prefix", "uri")
        };
        assertArrayEquals(expected, stack.getScope());
    }
    
    @Test
    public void testPush_Element_AdditionalNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Namespace additionalNamespace = Namespace.getNamespace("additional", "uri");
        Element element = new Element("element", Namespace.getNamespace("prefix", "uri"));
        element.addNamespaceDeclaration(additionalNamespace);
        stack.push(element);
        Namespace[] expected = {
            Namespace.NO_NAMESPACE,
            Namespace.getNamespace("additional", "uri"),
            Namespace.getNamespace("prefix", "uri")
        };
        assertArrayEquals(expected, stack.getScope());
    }
    
    @Test
    public void testPush_Element_Attributes() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("attr", "value", Namespace.getNamespace("prefix", "uri"));
        Element element = new Element("element");
        element.setAttribute(attribute);
        stack.push(element);
        Namespace[] expected = {
            Namespace.NO_NAMESPACE,
            Namespace.getNamespace("prefix", "uri")
        };
        assertArrayEquals(expected, stack.getScope());
    }
    
    @Test
    public void testPush_Attribute() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("attr", "value", Namespace.getNamespace("prefix", "uri"));
        stack.push(attribute);
        Namespace[] expected = {Namespace.getNamespace("prefix", "uri")};
        assertArrayEquals(expected, stack.getScope());
    }
    
    @Test
    public void testPop() {
        Namespace[] seed = {Namespace.getNamespace("prefix", "uri")};
        NamespaceStack stack = new NamespaceStack(seed);
        stack.pop();
        Namespace[] expected = {Namespace.NO_NAMESPACE, Namespace.XML_NAMESPACE};
        assertArrayEquals(expected, stack.getScope());
    }
    
    @Test
    public void testAddedForward() {
        Namespace[] seed = {Namespace.getNamespace("prefix1", "uri1"), Namespace.getNamespace("prefix2", "uri2")};
        NamespaceStack stack = new NamespaceStack(seed);
        Iterable<Namespace> added = stack.addedForward();
        Namespace[] expected = {
            Namespace.getNamespace("prefix1", "uri1"),
            Namespace.getNamespace("prefix2", "uri2")
        };
        int index = 0;
        for (Namespace ns : added) {
            assertEquals(expected[index++], ns);
        }
        assertEquals(2, index);
    }
    
    @Test
    public void testAddedReverse() {
        Namespace[] seed = {Namespace.getNamespace("prefix1", "uri1"), Namespace.getNamespace("prefix2", "uri2")};
        NamespaceStack stack = new NamespaceStack(seed);
        Iterable<Namespace> added = stack.addedReverse();
        Namespace[] expected = {
            Namespace.getNamespace("prefix2", "uri2"),
            Namespace.getNamespace("prefix1", "uri1")
        };
        int index = 0;
        for (Namespace ns : added) {
            assertEquals(expected[index++], ns);
        }
        assertEquals(2, index);
    }
    
    @Test
    public void testIterator() {
        Namespace[] seed = {Namespace.getNamespace("prefix1", "uri1"), Namespace.getNamespace("prefix2", "uri2")};
        NamespaceStack stack = new NamespaceStack(seed);
        Iterable<Namespace> stackNamespaces = stack;
        Namespace[] expected = {
            Namespace.getNamespace("prefix1", "uri1"),
            Namespace.getNamespace("prefix2", "uri2")
        };
        int index = 0;
        for (Namespace ns : stackNamespaces) {
            assertEquals(expected[index++], ns);
        }
        assertEquals(2, index);
    }
    
    @Test
    public void testIsInScope_True() {
        Namespace[] seed = {Namespace.getNamespace("prefix", "uri")};
        NamespaceStack stack = new NamespaceStack(seed);
        assertTrue(stack.isInScope(Namespace.getNamespace("prefix", "uri")));
    }
    
    @Test
    public void testIsInScope_False() {
        Namespace[] seed = {Namespace.getNamespace("prefix", "uri")};
        NamespaceStack stack = new NamespaceStack(seed);
        assertFalse(stack.isInScope(Namespace.getNamespace("other", "uri")));
    }
}