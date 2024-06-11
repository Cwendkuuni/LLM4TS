package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DOMAttributeIteratorTest {
  
  private DOMAttributeIterator attributeIterator;
  
  @Before
  public void setUp() {
    DOMNamespaceResolver namespaceResolver = new DOMNamespaceResolver();
    DOMNodePointer parent = new DOMNodePointer(null, namespaceResolver);
    QName name = new QName("test");
    attributeIterator = new DOMAttributeIterator(parent, name);
  }
  
  @Test
  public void testGetNodePointer() {
    assertNull(attributeIterator.getNodePointer());
  }
  
  @Test
  public void testGetPosition() {
    assertEquals(0, attributeIterator.getPosition());
  }
  
  @Test
  public void testSetPosition() {
    assertFalse(attributeIterator.setPosition(-1));
    assertTrue(attributeIterator.setPosition(0));
    assertEquals(0, attributeIterator.getPosition());
    assertTrue(attributeIterator.setPosition(1));
    assertEquals(1, attributeIterator.getPosition());
    assertTrue(attributeIterator.setPosition(2));
    assertEquals(2, attributeIterator.getPosition());
    assertFalse(attributeIterator.setPosition(3));
  }
}