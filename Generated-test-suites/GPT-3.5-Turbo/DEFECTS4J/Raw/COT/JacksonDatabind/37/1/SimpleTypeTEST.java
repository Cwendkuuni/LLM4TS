package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;

public class SimpleTypeTest {

  @Test
  public void testConstruct() {
    try {
      SimpleType.construct(Map.class);
      Assert.fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
    }

    try {
      SimpleType.construct(Collection.class);
      Assert.fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
    }

    try {
      SimpleType.construct(int[].class);
      Assert.fail("Should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      Assert.assertEquals("Can not construct SimpleType for an array (class: [I)", e.getMessage());
    }
  }

  // TODO: add more test cases

}