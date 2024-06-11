
package org.jdom2.util;

import org.jdom2.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class NamespaceStackTest {

    @Test
    public void testPush_singleNamespace() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("element", Namespace.getNamespace("urn:test"));
        stack.push(element);
        Namespace[] expectedScope = {Namespace.getNamespace("urn:test")};
        Assert.assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test
    public void testPush_multipleNamespaces() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("element", Namespace.getNamespace("urn:test"));
        element.addNamespaceDeclaration(Namespace.getNamespace("x", "urn:x"));
        element.addNamespaceDeclaration(Namespace.getNamespace("y", "urn:y"));
        stack.push(element);
        Namespace[] expectedScope = {
                Namespace.getNamespace("urn:test"),
                Namespace.getNamespace("x", "urn:x"),
                Namespace.getNamespace("y", "urn:y")
        };
        Assert.assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test
    public void testPush_attributeNamespace() {
        NamespaceStack stack = new NamespaceStack();
        Attribute attribute = new Attribute("name", "value", Namespace.getNamespace("urn:test"));
        stack.push(attribute);
        Namespace[] expectedScope = {Namespace.getNamespace("urn:test")};
        Assert.assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test(expected = IllegalStateException.class)
    public void testPop_emptyStack() {
        NamespaceStack stack = new NamespaceStack();
        stack.pop();
    }

    @Test
    public void testPop_nonEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("element", Namespace.getNamespace("urn:test"));
        stack.push(element);
        stack.pop();
        Namespace[] expectedScope = {};
        Assert.assertArrayEquals(expectedScope, stack.getScope());
    }

    @Test
    public void testAddedForward_emptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Iterable<Namespace> addedNamespaces = stack.addedForward();
        Assert.assertFalse(addedNamespaces.iterator().hasNext());
    }

    @Test
    public void testAddedForward_nonEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("element", Namespace.getNamespace("urn:test"));
        element.addNamespaceDeclaration(Namespace.getNamespace("x", "urn:x"));
        element.addNamespaceDeclaration(Namespace.getNamespace("y", "urn:y"));
        stack.push(element);
        Iterable<Namespace> addedNamespaces = stack.addedForward();
        Iterator<Namespace> iterator = addedNamespaces.iterator();
        List<Namespace> actualNamespaces = new ArrayList<>();
        while (iterator.hasNext()) {
            actualNamespaces.add(iterator.next());
        }
        List<Namespace> expectedNamespaces = new ArrayList<>();
        expectedNamespaces.add(Namespace.getNamespace("x", "urn:x"));
        expectedNamespaces.add(Namespace.getNamespace("y", "urn:y"));
        Assert.assertEquals(expectedNamespaces, actualNamespaces);
    }

    @Test
    public void testAddedReverse_emptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Iterable<Namespace> addedNamespaces = stack.addedReverse();
        Assert.assertFalse(addedNamespaces.iterator().hasNext());
    }

    @Test
    public void testAddedReverse_nonEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("element", Namespace.getNamespace("urn:test"));
        element.addNamespaceDeclaration(Namespace.getNamespace("x", "urn:x"));
        element.addNamespaceDeclaration(Namespace.getNamespace("y", "urn:y"));
        stack.push(element);
        Iterable<Namespace> addedNamespaces = stack.addedReverse();
        Iterator<Namespace> iterator = addedNamespaces.iterator();
        List<Namespace> actualNamespaces = new ArrayList<>();
        while (iterator.hasNext()) {
            actualNamespaces.add(iterator.next());
        }
        List<Namespace> expectedNamespaces = new ArrayList<>();
        expectedNamespaces.add(Namespace.getNamespace("y", "urn:y"));
        expectedNamespaces.add(Namespace.getNamespace("x", "urn:x"));
        Assert.assertEquals(expectedNamespaces, actualNamespaces);
    }

    @Test
    public void testIterator_emptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Iterator<Namespace> iterator = stack.iterator();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator_nonEmptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("element", Namespace.getNamespace("urn:test"));
        element.addNamespaceDeclaration(Namespace.getNamespace("x", "urn:x"));
        element.addNamespaceDeclaration(Namespace.getNamespace("y", "urn:y"));
        stack.push(element);
        Iterator<Namespace> iterator = stack.iterator();
        List<Namespace> actualNamespaces = new ArrayList<>();
        while (iterator.hasNext()) {
            actualNamespaces.add(iterator.next());
        }
        List<Namespace> expectedNamespaces = new ArrayList<>();
        expectedNamespaces.add(Namespace.getNamespace("urn:test"));
        expectedNamespaces.add(Namespace.getNamespace("x", "urn:x"));
        expectedNamespaces.add(Namespace.getNamespace("y", "urn:y"));
        Assert.assertEquals(expectedNamespaces, actualNamespaces);
    }

    @Test
    public void testIsInScope_true() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("element", Namespace.getNamespace("urn:test"));
        stack.push(element);
        Assert.assertTrue(stack.isInScope(Namespace.getNamespace("urn:test")));
    }

    @Test
    public void testIsInScope_false() {
        NamespaceStack stack = new NamespaceStack();
        Element element = new Element("element", Namespace.getNamespace("urn:test"));
        stack.push(element);
        Assert.assertFalse(stack.isInScope(Namespace.getNamespace("urn:fake")));
    }

    @Test
    public void testIsInScope_false_emptyStack() {
        NamespaceStack stack = new NamespaceStack();
        Assert.assertFalse(stack.isInScope(Namespace.getNamespace("urn:test")));
    }

    @Test
    public void testBinarySearch_match() {
        Namespace[] data = {Namespace.getNamespace("a", "urn:a"), Namespace.getNamespace("b", "urn:b")};
        int expectedIndex = 0;
        int actualIndex = NamespaceStack.binarySearch(data, 0, data.length, Namespace.getNamespace("a", "urn:a"));
        Assert.assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testBinarySearch_noMatch() {
        Namespace[] data = {Namespace.getNamespace("a", "urn:a"), Namespace.getNamespace("b", "urn:b")};
        int expectedIndex = -1;
        int actualIndex = NamespaceStack.binarySearch(data, 0, data.length,
                Namespace.getNamespace("c", "urn:c"));
        Assert.assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testBinarySearch_matchInMiddle() {
        Namespace[] data = {
                Namespace.getNamespace("a", "urn:a"),
                Namespace.getNamespace("b", "urn:b"),
                Namespace.getNamespace("c", "urn:c"),
                Namespace.getNamespace("d", "urn:d")
        };
        int expectedIndex = 2;
        int actualIndex = NamespaceStack.binarySearch(data, 0, data.length,
                Namespace.getNamespace("c", "urn:c"));
        Assert.assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testBinarySearch_matchInFirstHalf() {
        Namespace[] data = {
                Namespace.getNamespace("a", "urn:a"),
                Namespace.getNamespace("b", "urn:b"),
                Namespace.getNamespace("c", "urn:c"),
                Namespace.getNamespace("d", "urn:d")
        };
        int expectedIndex = 1;
        int actualIndex = NamespaceStack.binarySearch(data, 0, data.length,
                Namespace.getNamespace("b", "urn:b"));
        Assert.assertEquals(expectedIndex, actualIndex);
    }

    @Test
    public void testBinarySearch_matchInSecondHalf() {
        Namespace[] data = {
                Namespace.getNamespace("a", "urn:a"),
                Namespace.getNamespace("b", "urn:b"),
                Namespace.getNamespace("c", "urn:c"),
                Namespace.getNamespace("d", "urn:d")
        };
        int expectedIndex = 3;
        int actualIndex = NamespaceStack.binarySearch(data, 0, data.length,
                Namespace.getNamespace("d", "urn:d"));
        Assert.assertEquals(expectedIndex, actualIndex);
    }
}
