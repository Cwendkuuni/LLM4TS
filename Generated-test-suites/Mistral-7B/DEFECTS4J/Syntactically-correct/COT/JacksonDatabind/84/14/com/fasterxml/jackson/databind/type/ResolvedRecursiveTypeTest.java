package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

 private ResolvedRecursiveType createTestObject(Class<?> erasedType) {
  TypeBindings bindings = mock(TypeBindings.class);
  return new ResolvedRecursiveType(erasedType, bindings);
 }

 @Test
 public void testConstructor() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);
  assertNotNull(obj);
 }

 @Test
 public void testSetReference() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);

  JavaType ref = mock(JavaType.class);
  obj.setReference(ref);

  JavaType expected = ref;
  JavaType actual = obj.getSelfReferencedType();
  assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);

  JavaType ref1 = mock(JavaType.class);
  JavaType ref2 = mock(JavaType.class);

  obj.setReference(ref1);
  obj.setReference(ref2);
 }

 @Test
 public void testGetSelfReferencedType() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);

  JavaType ref = mock(JavaType.class);
  obj.setReference(ref);
  JavaType expected = ref;
  JavaType actual = obj.getSelfReferencedType();
  assertEquals(expected, actual);
 }

 @Test
 public void testGetSelfReferencedTypeWhenNull() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);
  JavaType actual = obj.getSelfReferencedType();
  assertNull(actual);
 }

 @Test
 public void testGetGenericSignature() {
  Class<String> stringClass = String.class;
  StringBuilder sb = new StringBuilder();
  ResolvedRecursiveType obj = createTestObject(stringClass);

  obj.getGenericSignature(sb);
  String expected = "<type 'java.lang.String'>[recursive type; ";
  String actual = sb.toString();
  assertEquals(expected, actual);
 }

 @Test
 public void testGetErasedSignature() {
  Class<String> stringClass = String.class;
  StringBuilder sb = new StringBuilder();
  ResolvedRecursiveType obj = createTestObject(stringClass);

  obj.getErasedSignature(sb);
  String expected = "[Ljava.lang.String;[recursive type; ";
  String actual = sb.toString();
  assertEquals(expected, actual);
 }

 @Test
 public void testWithContentType() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);
  JavaType contentType = mock(JavaType.class);
  JavaType expected = obj;
  JavaType actual = obj.withContentType(contentType);
  assertEquals(expected, actual);
 }

 @Test
 public void testWithTypeHandler() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);
  Object handler = mock(Object.class);
  JavaType expected = obj;
  JavaType actual = obj.withTypeHandler(handler);
  assertEquals(expected, actual);
 }

 @Test
 public void testWithContentTypeHandler() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);
  Object handler = mock(Object.class);
  JavaType expected = obj;
  JavaType actual = obj.withContentTypeHandler(handler);
  assertEquals(expected, actual);
 }

 @Test
 public void testWithValueHandler() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);
  Object handler = mock(Object.class);
  JavaType expected = obj;
  JavaType actual = obj.withValueHandler(handler);
  assertEquals(expected, actual);
 }

 @Test
 public void testWithContentValueHandler() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);
  Object handler = mock(Object.class);
  JavaType expected = obj;
  JavaType actual = obj.withContentValueHandler(handler);
  assertEquals(expected, actual);
 }

 @Test
 public void testWithStaticTyping() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);
  JavaType expected = obj;
  JavaType actual = obj.withStaticTyping();
  assertEquals(expected, actual);
 }

 @Test
 public void testNarrow() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);
  Class<?> subclass = StringBuffer.class;
  JavaType expected = obj;
  JavaType actual = obj.withNarrow(subclass);
  assertEquals(expected, actual);
 }

 @Test(expected = DeprecatedException.class)
 public void testNarrowDeprecated() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);
  Class<?> subclass = StringBuffer.class;
  obj._narrow(subclass);
 }

 @Test
 public void testRefine() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);
  JavaType refinedType = mock(JavaType.class);
  TypeBindings bindings = mock(TypeBindings.class);
  JavaType superClass = mock(JavaType.class);
  JavaType[] superInterfaces = new JavaType[0];
  JavaType expected = refinedType;
  JavaType actual = obj.refine(stringClass, bindings, superClass, superInterfaces);
  assertEquals(expected, actual);
 }

 @Test
 public void testIsContainerType() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);
  boolean expected = false;
  boolean actual = obj.isContainerType();
  assertEquals(expected, actual);
 }

 @Test
 public void testToString() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);
  String expected = "[recursive type; UNRESOLVED]";
  String actual = obj.toString();
  assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
  Class<String> stringClass1 = String.class;
  Class<String> stringClass2 = String.class;
  ResolvedRecursiveType obj1 = createTestObject(stringClass1);
  ResolvedRecursiveType obj2 = createTestObject(stringClass2);

  JavaType ref1 = mock(JavaType.class);
  JavaType ref2 = mock(JavaType.class);
  obj1.setReference(ref1);
  obj2.setReference(ref2);

  boolean expected = true;
  boolean actual = obj1.equals(obj2);
  assertEquals(expected, actual);
 }

 @Test
 public void testEqualsDifferentClass() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);

  Object other = new Object();
  boolean expected = false;
  boolean actual = obj.equals(other);
  assertEquals(expected, actual);
 }

 @Test
 public void testEqualsNull() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);

  boolean expected = false;
  boolean actual = obj.equals(null);
  assertEquals(expected, actual);
 }

 @Test
 public void testEqualsSameInstance() {
  Class<String> stringClass = String.class;
  ResolvedRecursiveType obj = createTestObject(stringClass);

  boolean expected = true;
  boolean actual = obj.equals(obj);
  assertEquals(expected, actual);
 }

 @Test
 public void testEqualsDifferentInstanceWithSameRef() {
  Class<String> stringClass1 = String.class;
  Class<String> stringClass2 = String.class;
  ResolvedRecursiveType obj1 = createTestObject(stringClass1);
  ResolvedRecursiveType obj2 = createTestObject(stringClass2);

  JavaType ref = mock(JavaType.class);
  obj1.setReference(ref);
  obj2.setReference(ref);

  boolean expected = true;
  boolean actual = obj1.equals(obj2);
  assertEquals(expected, actual);
 }

 @Test
 public void testEqualsDifferentInstanceWithDifferentRefs() {
  Class<String> stringClass1 = String.class;
  Class<String> stringClass2 = String.class;
  ResolvedRecursiveType obj1 = createTestObject(stringClass1);
  ResolvedRecursiveType obj2 = createTestObject(stringClass2);

  JavaType ref1 = mock(JavaType.class);
  JavaType ref2 = mock(JavaType.class);
  obj1.setReference(ref1);
  obj2.setReference(ref2);

  boolean expected = false;
  boolean actual = obj1.equals(obj2);
  assertEquals(expected, actual);
 }
}