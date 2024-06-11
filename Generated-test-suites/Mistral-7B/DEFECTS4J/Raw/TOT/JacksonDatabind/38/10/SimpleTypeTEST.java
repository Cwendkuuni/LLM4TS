package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 Class<?> cls = String.class;
 SimpleType simpleType = new SimpleType(cls);
 assertInstanceOf(SimpleType.class, simpleType);
 assertNull(simpleType.getSuperClass());
 assertNull(simpleType.getSuperInterfaces());
 assertEquals(cls, simpleType.getType());
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 Class<?> cls = String.class;
 SimpleType originalSimpleType = new SimpleType(cls);
 SimpleType copySimpleType = new SimpleType(originalSimpleType);
 assertEquals(originalSimpleType, copySimpleType);
 }

 @Test
 public void testSimpleTypeUnsafeConstruction() {
 Class<?> cls = String[].class;
 SimpleType simpleType = SimpleType.constructUnsafe(cls);
 assertInstanceOf(SimpleType.class, simpleType);
 assertNull(simpleType.getSuperClass());
 assertNull(simpleType.getSuperInterfaces());
 assertEquals(cls.getComponentType(), simpleType.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeInvalidConstruction() {
 Class<?> cls = Map.class;
 SimpleType simpleType = new SimpleType(cls);
 fail("Should not reach this point");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeDeprecatedConstructMethod() {
 Class<?> cls = String.class;
 SimpleType simpleType = SimpleType.construct(cls);
 fail("Should not reach this point");
 }

 @Test
 public void testSimpleTypeNarrowing() {
 Class<?> subclass = String[].class;
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType narrowedType = simpleType.narrow(subclass);
 assertNotNull(narrowedType);
 assertInstanceOf(SimpleType.class, narrowedType);
 assertNotSame(simpleType, narrowedType);
 assertEquals(subclass, narrowedType.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeInvalidNarrowing() {
 Class<?> subclass = Object.class;
 SimpleType simpleType = new SimpleType(String.class);
 simpleType.narrow(subclass);
 fail("Should not reach this point");
 }

 @Test
 public void testSimpleTypeWithTypeHandler() {
 Object handler = new Object() {};
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType typeWithHandler = simpleType.withTypeHandler(handler);
 assertNotNull(typeWithHandler);
 assertInstanceOf(SimpleType.class, typeWithHandler);
 assertEquals(simpleType, typeWithHandler);
 assertNotSame(simpleType, typeWithHandler);
 assertNull(typeWithHandler.getTypeHandler());
 typeWithHandler = simpleType.withTypeHandler(handler);
 assertEquals(handler, typeWithHandler.getTypeHandler());
 }

 @Test
 public void testSimpleTypeWithValueHandler() {
 Object handler = new Object() {};
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType typeWithHandler = simpleType.withValueHandler(handler);
 assertNotNull(typeWithHandler);
 assertInstanceOf(SimpleType.class, typeWithHandler);
 assertEquals(simpleType, typeWithHandler);
 assertNotSame(simpleType, typeWithHandler);
 assertNull(typeWithHandler.getValueHandler());
 typeWithHandler = simpleType.withValueHandler(handler);
 assertEquals(handler, typeWithHandler.getValueHandler());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleTypeInvalidWithValueHandler() {
 Object handler = new Object() {};
 SimpleType simpleType = new SimpleType(String.class);
 simpleType.withValueHandler(handler);
 fail("Should not reach this point");
 }

 @Test
 public void testSimpleTypeEquality() {
 Class<?> cls = String.class;
 SimpleType simpleType1 = new SimpleType(cls);
 SimpleType simpleType2 = new SimpleType(cls);
 SimpleType simpleType3 = new SimpleType(int.class);
 assertTrue(simpleType1.equals(simpleType1));
 assertTrue(simpleType1.equals(simpleType2));
 assertFalse(simpleType1.equals(simpleType3));
 }

 @Test
 public void testSimpleTypeToString() {
 Class<?> cls = String.class;
 SimpleType simpleType = new SimpleType(cls);
 assertEquals("[simple type, class java.lang.String]", simpleType.toString());
 }
}