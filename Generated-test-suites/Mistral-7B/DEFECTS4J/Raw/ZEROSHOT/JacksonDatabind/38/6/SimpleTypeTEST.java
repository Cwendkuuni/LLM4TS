package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class SimpleTypeTest {

 @Test
 public void testSimpleTypeConstruction() {
 SimpleType simpleType = new SimpleType(String.class);
 assertNotNull(simpleType);
 assertEquals(String.class, simpleType.getRawClass());
 assertNull(simpleType.getSuperClass());
 assertArrayEquals(new Class[0], simpleType.getSuperTypes());
 }

 @Test
 public void testSimpleTypeCopyConstructor() {
 SimpleType original = new SimpleType(String.class);
 SimpleType copy = new SimpleType(original);
 assertNotSame(original, copy);
 assertEquals(original.getRawClass(), copy.getRawClass());
 assertEquals(original.getClass(), copy.getClass());
 }

 @Test
 public void testSimpleTypeWithContentTypeHandler() {
 try {
 SimpleType simpleType = new SimpleType(String.class).withContentTypeHandler(new Object() {});
 fail("Should not be able to call withContentTypeHandler() on SimpleType");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSimpleTypeWithValueHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType withValueHandler = simpleType.withValueHandler(new Object());
 assertNotSame(simpleType, withValueHandler);
 assertEquals(simpleType.getRawClass(), withValueHandler.getRawClass());
 }

 @Test
 public void testSimpleTypeWithTypeHandler() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType withTypeHandler = simpleType.withTypeHandler(new Object());
 assertNotSame(simpleType, withTypeHandler);
 assertEquals(simpleType.getRawClass(), withTypeHandler.getRawClass());
 }

 @Test
 public void testSimpleTypeWithContentValueHandler() {
 try {
 SimpleType simpleType = new SimpleType(String.class).withContentValueHandler(new Object() {});
 fail("Should not be able to call withContentValueHandler() on SimpleType");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testSimpleTypeWithStaticTyping() {
 SimpleType simpleType = new SimpleType(String.class);
 SimpleType staticTyping = simpleType.withStaticTyping();
 assertTrue(staticTyping.isStatic());
 }

 @Test
 public void testSimpleTypeWithRefine() {
 SimpleType simpleType = new SimpleType(String.class);
 JavaType refinedType = simpleType.refine(Integer.class, TypeBindings.emptyBindings(), null, new Class[0]);
 assertNull(refinedType);
 }

 @Test
 public void testSimpleTypeWithNarrow() {
 SimpleType stringType = new SimpleType(String.class);
 SimpleType objectType = SimpleType.construct(Object.class);
 SimpleType narrowedType = stringType._narrow(Object.class);
 assertNotNull(narrowedType);
 assertSame(objectType, narrowedType);
 }

 @Test
 public void testSimpleTypeWithCanonicalName() {
 SimpleType stringType = new SimpleType(String.class);
 String canonicalName = stringType.buildCanonicalName();
 assertEquals("java.lang.String", canonicalName);
 }

 @Test
 public void testSimpleTypeWithGenericSignature() {
 SimpleType stringType = new SimpleType(String[].class);
 StringBuilder genericSignature = new StringBuilder();
 stringType.getGenericSignature(genericSignature);
 assertEquals("java.lang.String[]", genericSignature.toString());
 }

 @Test
 public void testSimpleTypeWithErasedSignature() {
 SimpleType stringType = new SimpleType(String[].class);
 StringBuilder erasedSignature = new StringBuilder();
 stringType.getErasedSignature(erasedSignature);
 assertEquals("[Ljava.lang.String;", erasedSignature.toString());
 }
}