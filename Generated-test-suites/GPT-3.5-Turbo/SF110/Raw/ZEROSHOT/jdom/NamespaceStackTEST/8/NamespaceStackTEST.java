package org.jdom2.util;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

public class NamespaceStackTest {

  @Test
  public void testPushElement() {
    NamespaceStack stack = new NamespaceStack();
    Element element = new Element("test");
    stack.push(element);
    assertEquals(stack.getScope().length, 3);
  }

  @Test
  public void testPushAttribute() {
    NamespaceStack stack = new NamespaceStack();
    Attribute attribute = new Attribute("attribute", "value");
    stack.push(attribute);
    assertEquals(stack.getScope().length, 3);
  }

  @Test(expected = IllegalStateException.class)
  public void testPop() {
    NamespaceStack stack = new NamespaceStack();
    stack.pop();
    assertEquals(stack.getScope().length, 2);
  }

  @Test
  public void testIsInScope() {
    NamespaceStack stack = new NamespaceStack();
    Namespace ns = new Namespace("prefix", "uri");
    boolean isInScope = stack.isInScope(ns);
    assertFalse(isInScope);
  }

  @Test
  public void testIterator() {
    NamespaceStack stack = new NamespaceStack();
    Iterator<Namespace> iterator = stack.iterator();
    assertNotNull(iterator);
  }
}