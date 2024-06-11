package org.jdom2.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class NamespaceStackTest {
  
  private NamespaceStack nsStack;

  @Before
  public void setup() {
    nsStack = new NamespaceStack();
  }

  @Test
  public void testPush() {
    Element element = new Element("test");
    nsStack.push(element);

    Iterator<Namespace> iterator = nsStack.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(element.getNamespace(), iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test
  public void testPushWithAdditionalNamespaces() {
    Element element = new Element("test");
    Namespace ns1 = Namespace.getNamespace("test1");
    Namespace ns2 = Namespace.getNamespace("test2");
    element.addNamespaceDeclaration(ns1);
    element.addNamespaceDeclaration(ns2);

    nsStack.push(element);

    Iterator<Namespace> iterator = nsStack.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(element.getNamespace(), iterator.next());
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().equals(ns1) || iterator.next().equals(ns2));
    assertTrue(iterator.hasNext());
    assertTrue(iterator.next().equals(ns1) || iterator.next().equals(ns2));
    assertFalse(iterator.hasNext());
  }

  @Test
  public void testPushWithAttributes() {
    Element element = new Element("test");
    Attribute attribute = new Attribute("attribute", "value");
    element.setAttribute(attribute);

    nsStack.push(element);

    Iterator<Namespace> iterator = nsStack.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(element.getNamespace(), iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test
  public void testPop() {
    Element element = new Element("test");
    nsStack.push(element);

    nsStack.pop();

    Iterator<Namespace> iterator = nsStack.iterator();
    assertFalse(iterator.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void testPopEmptyStack() {
    nsStack.pop();
  }

  @Test
  public void testAddedForward() {
    Element element = new Element("test");
    nsStack.push(element);

    Iterable<Namespace> added = nsStack.addedForward();

    Iterator<Namespace> iterator = added.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(element.getNamespace(), iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test
  public void testAddedReverse() {
    Element element = new Element("test");
    nsStack.push(element);

    Iterable<Namespace> added = nsStack.addedReverse();

    Iterator<Namespace> iterator = added.iterator();
    assertTrue(iterator.hasNext());
    assertEquals(element.getNamespace(), iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test
  public void testGetScope() {
    Element element = new Element("test");
    nsStack.push(element);

    Namespace[] scope = nsStack.getScope();

    assertNotNull(scope);
    assertEquals(element.getNamespace(), scope[0]);
  }

  @Test
  public void testIsInScope() {
    Element element = new Element("test");
    Namespace ns = element.getNamespace();
    nsStack.push(element);

    assertTrue(nsStack.isInScope(ns));
  }

  @Test
  public void testIsNotInScope() {
    Element element = new Element("test");
    Namespace ns = Namespace.getNamespace("not-in-scope");
    nsStack.push(element);

    assertFalse(nsStack.isInScope(ns));
  }

}