package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class SimpleTypeTest {

   @Test
   public void testSimpleTypeConstruction() {
      Class<Integer> intType = Integer.class;
      SimpleType simpleType = new SimpleType(intType);
      assertEquals(intType, simpleType.getRawClass());
      assertTrue(simpleType.isContainerType()); // should be false
      assertEquals(intType.getName(), simpleType.getTypeName());
   }

   @Test
   public void testSimpleTypeWithBindings() {
      Class<Map<String, List<Integer>>> mapListIntType = Map.class.cast(Arrays.asList(List.class, Integer.class).getClass());
      TypeBindings bindings = TypeBindings.mapBindings(List.class, Integer.class);
      SimpleType simpleType = new SimpleType(mapListIntType, bindings, null, null);
      assertEquals(mapListIntType, simpleType.getRawClass());
      assertTrue(simpleType.isContainerType()); // should be false
      assertEquals(mapListIntType.getName(), simpleType.getTypeName());
      assertEquals(bindings, simpleType.getBindings());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testInvalidTypeConstruction() {
      Class<Map<String, String>> mapStringType = Map.class.cast(Arrays.asList(String.class, String.class).getClass());
      new SimpleType(mapStringType);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testInvalidTypeConstruction2() {
      Class<List<String>> listStringType = List.class.cast(Arrays.asList(String.class).getClass());
      new SimpleType(listStringType, TypeBindings.emptyBindings(), null, new JavaType[] { String.class });
   }

   @Test
   public void testNarrowingType() {
      Class<Number> numberType = Number.class;
      SimpleType simpleType1 = new SimpleType(numberType);
      SimpleType simpleType2 = simpleType1.narrow(Integer.class);
      assertEquals(simpleType2, simpleType1.narrow(Integer.class));
      assertEquals(simpleType2.getRawClass(), Integer.class);
      assertFalse(simpleType1.isContainerType());
      assertFalse(simpleType2.isContainerType());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testNarrowingTypeWithInvalidType() {
      Class<Number> numberType = Number.class;
      SimpleType simpleType = new SimpleType(numberType);
      simpleType.narrow(String.class);
   }

   @Test
   public void testTypeWithValueHandler() {
      Class<String> stringType = String.class;
      SimpleType simpleType = new SimpleType(stringType, TypeBindings.emptyBindings(), null, null);
      Object valueHandler = new Object();
      SimpleType simpleTypeWithValueHandler = simpleType.withValueHandler(valueHandler);
      assertEquals(simpleType, simpleTypeWithValueHandler.withoutValueHandler());
      assertEquals(valueHandler, simpleTypeWithValueHandler.getValueHandler());
   }

   @Test
   public void testTypeWithTypeHandler() {
      Class<String> stringType = String.class;
      SimpleType simpleType = new SimpleType(stringType, TypeBindings.emptyBindings(), null, null);
      Object typeHandler = new Object();
      SimpleType simpleTypeWithTypeHandler = simpleType.withTypeHandler(typeHandler);
      assertEquals(simpleType, simpleTypeWithTypeHandler.withoutTypeHandler());
      assertEquals(typeHandler, simpleTypeWithTypeHandler.getTypeHandler());
   }

   @Test
   public void testTypeWithStaticTyping() {
      Class<String> stringType = String.class;
      SimpleType simpleType = new SimpleType(stringType, TypeBindings.emptyBindings(), null, null);
      SimpleType simpleTypeWithStaticTyping = simpleType.withStaticTyping();
      assertFalse(simpleType.isStaticTyping());
      assertTrue(simpleTypeWithStaticTyping.isStaticTyping());
   }

   @Test
   public void testTypeEquals() {
      Class<String> stringType1 = String.class;
      Class<String> stringType2 = String.class;
      SimpleType simpleType1 = new SimpleType(stringType1, TypeBindings.emptyBindings(), null, null);
      SimpleType simpleType2 = new SimpleType(stringType2, TypeBindings.emptyBindings(), null, null);
      SimpleType simpleType3 = new SimpleType(stringType1, TypeBindings.emptyBindings(), null, null);
      assertTrue(simpleType1.equals(simpleType1));
      assertTrue(simpleType1.equals(simpleType3));
      assertTrue(simpleType1.equals(simpleType2));
      assertTrue(simpleType2.equals(simpleType1));
      assertTrue(simpleType2.equals(simpleType3));
      assertTrue(simpleType3.equals(simpleType1));
      assertTrue(simpleType3.equals(simpleType2));
      assertFalse(simpleType1.equals(null));
      assertFalse(simpleType1.equals(""));
      assertFalse(simpleType1.equals(new Object()));
   }

   @Test
   public void testToString() {
      Class<String> stringType = String.class;
      SimpleType simpleType = new SimpleType(stringType, TypeBindings.emptyBindings(), null, null);
      String expected = "[simple type, class java.lang.String]";
      assertEquals(expected, simpleType.toString());
   }
}