package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
   @Test
   public void testSimpleTypeConstructorWithClass() {
      SimpleType simpleType = new SimpleType(String.class);
      assertEquals("java.lang.String", simpleType.getCanonicalName());
      assertFalse(simpleType.isContainerType());
   }

   @Test
   public void testSimpleTypeCopyConstructor() {
      SimpleType originalSimpleType = new SimpleType(String.class);
      SimpleType copiedSimpleType = new SimpleType(originalSimpleType);
      assertEquals(originalSimpleType.getCanonicalName(), copiedSimpleType.getCanonicalName());
      assertFalse(copiedSimpleType.isContainerType());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSimpleTypeConstructUnsafe() {
      SimpleType simpleType = SimpleType.constructUnsafe(String[].class);
      fail("Illegal argument exception expected but not thrown");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSimpleTypeConstruct() {
      SimpleType simpleType = SimpleType.construct(String[].class);
      fail("Illegal argument exception expected but not thrown");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSimpleTypeNarrowWithSubclass() {
      SimpleType parentType = new SimpleType(String.class);
      SimpleType childType = new SimpleType(StringBuffer.class);
      SimpleType narrowedType = parentType._narrow(childType._class);
      fail("Illegal argument exception expected but not thrown");
   }

   @Test
   public void testSimpleTypeWithTypeHandler() {
      SimpleType simpleType = new SimpleType(String.class);
      SimpleType typeWithHandler = new SimpleType(simpleType._class, simpleType._bindings, simpleType._superClass, simpleType._superInterfaces, new MyStringTypeHandler(), simpleType._valueHandler, simpleType._asStatic);
      assertEquals(simpleType.getCanonicalName(), typeWithHandler.getCanonicalName());
      assertFalse(typeWithHandler.isContainerType());
   }
}

class MyStringTypeHandler {}

