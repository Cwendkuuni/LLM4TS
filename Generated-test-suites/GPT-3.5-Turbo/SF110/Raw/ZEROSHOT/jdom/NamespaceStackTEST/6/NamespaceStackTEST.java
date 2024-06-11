package org.jdom2.util;

import org.jdom2.*;
import org.junit.Assert;
import org.junit.Test;
import java.util.*;

public class NamespaceStackTest {

    @Test
    public void testPush_Element() {
        NamespaceStack namespaceStack = new NamespaceStack(); // Create instance of NamespaceStack
        Element element = new Element("test", Namespace.XML_NAMESPACE); // Create test Element
        namespaceStack.push(element); // Push Element to stack
        Assert.assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE)); // Check if Namespace is in scope
        Assert.assertFalse(namespaceStack.isInScope(Namespace.NO_NAMESPACE)); // Check if Namespace is not in scope
    }

    @Test
    public void testPush_Attribute() {
        NamespaceStack namespaceStack = new NamespaceStack(); // Create instance of NamespaceStack
        Attribute attribute = new Attribute("test", "value", Namespace.XML_NAMESPACE); // Create test Attribute
        namespaceStack.push(attribute); // Push Attribute to stack
        Assert.assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE)); // Check if Namespace is in scope
        Assert.assertFalse(namespaceStack.isInScope(Namespace.NO_NAMESPACE)); // Check if Namespace is not in scope
    }

    @Test
    public void testPop() {
        NamespaceStack namespaceStack = new NamespaceStack(); // Create instance of NamespaceStack
        Element element = new Element("test", Namespace.XML_NAMESPACE); // Create test Element
        namespaceStack.push(element); // Push Element to stack
        namespaceStack.pop(); // Pop Element from stack
        Assert.assertFalse(namespaceStack.isInScope(Namespace.XML_NAMESPACE)); // Check if Namespace is not in scope
    }

    @Test
    public void testAddedForward() {
        NamespaceStack namespaceStack = new NamespaceStack(); // Create instance of NamespaceStack
        Element element = new Element("test", Namespace.XML_NAMESPACE); // Create test Element
        namespaceStack.push(element); // Push Element to stack
        Iterable<Namespace> added = namespaceStack.addedForward(); // Get added Namespaces in forward order
        List<Namespace> addedList = new ArrayList<>(); // Create list to store Namespaces
        for (Namespace ns : added) {
            addedList.add(ns); // Add Namespace to list
        }
        Assert.assertEquals(2, addedList.size()); // Check if there are 2 added Namespaces
        Assert.assertEquals(Namespace.NO_NAMESPACE, addedList.get(0)); // Check if first Namespace is the default Namespace
        Assert.assertEquals(Namespace.XML_NAMESPACE, addedList.get(1)); // Check if second Namespace is the XML Namespace
    }

    @Test
    public void testAddedReverse() {
        NamespaceStack namespaceStack = new NamespaceStack(); // Create instance of NamespaceStack
        Element element = new Element("test", Namespace.XML_NAMESPACE); // Create test Element
        namespaceStack.push(element); // Push Element to stack
        Iterable<Namespace> added = namespaceStack.addedReverse(); // Get added Namespaces in reverse order
        List<Namespace> addedList = new ArrayList<>(); // Create list to store Namespaces
        for (Namespace ns : added) {
            addedList.add(ns); // Add Namespace to list
        }
        Assert.assertEquals(2, addedList.size()); // Check if there are 2 added Namespaces
        Assert.assertEquals(Namespace.XML_NAMESPACE, addedList.get(0)); // Check if first Namespace is the XML Namespace
        Assert.assertEquals(Namespace.NO_NAMESPACE, addedList.get(1)); // Check if second Namespace is the default Namespace
    }

    @Test
    public void testIterator() {
        NamespaceStack namespaceStack = new NamespaceStack(); // Create instance of NamespaceStack
        Element element = new Element("test", Namespace.XML_NAMESPACE); // Create test Element
        namespaceStack.push(element); // Push Element to stack
        Iterator<Namespace> iterator = namespaceStack.iterator(); // Get Namespace iterator
        List<Namespace> scope = new ArrayList<>(); // Create list to store in-scope Namespaces
        while (iterator.hasNext()) {
            scope.add(iterator.next()); // Add Namespace to list
        }
        Assert.assertEquals(2, scope.size()); // Check if there are 2 in-scope Namespaces
        Assert.assertEquals(Namespace.NO_NAMESPACE, scope.get(0)); // Check if first Namespace is the default Namespace
        Assert.assertEquals(Namespace.XML_NAMESPACE, scope.get(1)); // Check if second Namespace is the XML Namespace
    }

    @Test
    public void testGetScope() {
        NamespaceStack namespaceStack = new NamespaceStack(); // Create instance of NamespaceStack
        Element element = new Element("test", Namespace.XML_NAMESPACE); // Create test Element
        namespaceStack.push(element); // Push Element to stack
        Namespace[] scope = namespaceStack.getScope(); // Get current scope
        Assert.assertEquals(2, scope.length); // Check if scope has 2 Namespaces
        Assert.assertEquals(Namespace.NO_NAMESPACE, scope[0]); // Check if first Namespace is the default Namespace
        Assert.assertEquals(Namespace.XML_NAMESPACE, scope[1]); // Check if second Namespace is the XML Namespace
    }

    @Test
    public void testIsInScope() {
        NamespaceStack namespaceStack = new NamespaceStack(); // Create instance of NamespaceStack
        Element element = new Element("test", Namespace.XML_NAMESPACE); // Create test Element
        namespaceStack.push(element); // Push Element to stack
        Assert.assertTrue(namespaceStack.isInScope(Namespace.XML_NAMESPACE)); // Check if Namespace is in scope
        Assert.assertFalse(namespaceStack.isInScope(Namespace.NO_NAMESPACE)); // Check if Namespace is not in scope
    }
}