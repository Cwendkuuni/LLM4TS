package org.jdom2.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.Namespace;
import org.jdom2.Element;
import org.jdom2.Attribute;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class NamespaceStackTest {
  
  @Test
  public void testConstructorWithoutArgs() {
    NamespaceStack stack = new NamespaceStack();
    Namespace[] expectedScope = NamespaceStack.DEFAULTSEED;
    assertArrayEquals(expectedScope, stack.getScope());
  }
  
  @Test
  public void testConstructorWithSeed() {
    Namespace ns1 = Namespace.getNamespace("test1");
    Namespace ns2 = Namespace.getNamespace("test2");
    Namespace[] seed = {ns1, ns2};
    NamespaceStack stack = new NamespaceStack(seed);
    Namespace[] expectedScope = Arrays.copyOf(seed, seed.length);
    assertArrayEquals(expectedScope, stack.getScope());
  }
  
  @Test
  public void testPushWithElement() {
    Element element = new Element("test");
    Namespace mns = element.getNamespace();
    Namespace[] seed = {mns};
    NamespaceStack stack = new NamespaceStack(seed);
    stack.push(element);
    Namespace[] expectedScope = {Namespace.NO_NAMESPACE, mns};
    assertArrayEquals(expectedScope, stack.getScope());
  }
  
  @Test
  public void testPushWithAttribute() {
    Element element = new Element("test");
    Attribute attribute = new Attribute("attr", "value");
    element.setAttribute(attribute);
    Namespace mns = attribute.getNamespace();
    Namespace[] seed = {mns};
    NamespaceStack stack = new NamespaceStack(seed);
    stack.push(attribute);
    Namespace[] expectedScope = {Namespace.NO_NAMESPACE, mns};
    assertArrayEquals(expectedScope, stack.getScope());
  }
  
  @Test(expected = IllegalStateException.class)
  public void testPopWhenStackIsEmpty() {
    NamespaceStack stack = new NamespaceStack();
    stack.pop();
  }
  
  @Test
  public void testPop() {
    Namespace ns1 = Namespace.getNamespace("test1");
    Namespace ns2 = Namespace.getNamespace("test2");
    NamespaceStack stack = new NamespaceStack(new Namespace[]{ns1, ns2});
    stack.pop();
    Namespace[] expectedScope = {ns2};
    assertArrayEquals(expectedScope, stack.getScope());
  }
  
  @Test
  public void testIsInScopeWhenNamespaceInScope() {
    Namespace ns1 = Namespace.getNamespace("test1");
    NamespaceStack stack = new NamespaceStack(new Namespace[]{ns1});
    assertTrue(stack.isInScope(ns1));
  }
  
  @Test
  public void testIsInScopeWhenNamespaceNotInScope() {
    Namespace ns1 = Namespace.getNamespace("test1");
    Namespace ns2 = Namespace.getNamespace("test2");
    NamespaceStack stack = new NamespaceStack(new Namespace[]{ns1});
    assertFalse(stack.isInScope(ns2));
  }
  
  @Test
  public void testAddedForwardWhenEmptyAddedNamespace() {
    NamespaceStack stack = new NamespaceStack();
    Iterable<Namespace> iterable = stack.addedForward();
    Iterator<Namespace> iterator = iterable.iterator();
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testAddedForward() {
    Namespace ns1 = Namespace.getNamespace("test1");
    Namespace ns2 = Namespace.getNamespace("test2");
    Namespace[] added = {ns1, ns2};
    NamespaceStack stack = new NamespaceStack(new Namespace[]{ns1});
    stack.push(new Attribute("attr1", "value"));
    stack.push(new Attribute("attr2", "value"));
    Iterable<Namespace> iterable = stack.addedForward();
    Iterator<Namespace> iterator = iterable.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(ns2, iterator.next());
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testAddedReverseWhenEmptyAddedNamespace() {
    NamespaceStack stack = new NamespaceStack();
    Iterable<Namespace> iterable = stack.addedReverse();
    Iterator<Namespace> iterator = iterable.iterator();
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testAddedReverse() {
    Namespace ns1 = Namespace.getNamespace("test1");
    Namespace ns2 = Namespace.getNamespace("test2");
    Namespace[] added = {ns1, ns2};
    NamespaceStack stack = new NamespaceStack(new Namespace[]{ns1});
    stack.push(new Attribute("attr1", "value"));
    stack.push(new Attribute("attr2", "value"));
    Iterable<Namespace> iterable = stack.addedReverse();
    Iterator<Namespace> iterator = iterable.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(ns1, iterator.next());
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testGetScope() {
    Namespace ns1 = Namespace.getNamespace("test1");
    Namespace ns2 = Namespace.getNamespace("test2");
    NamespaceStack stack = new NamespaceStack(new Namespace[]{ns1, ns2});
    Namespace[] expectedScope = {ns1, ns2};
    assertArrayEquals(expectedScope, stack.getScope());
  }
  
  // The following private methods do not have public test cases as they are used internally by
  // the public methods. However, they are necessary for the functionality of the class.
  
  // binarySearch() method
  
  // checkNamespace() method
  
  // push() method
  
  // pushStack() method
  
  // iterator() method
  
  // isInScope() method
  
  // Comparator class
}