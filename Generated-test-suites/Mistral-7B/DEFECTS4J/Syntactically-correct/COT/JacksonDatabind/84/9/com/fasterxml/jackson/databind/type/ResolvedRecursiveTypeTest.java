package com.yourpackage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 private JavaType createTestType() {
 return new ResolvedRecursiveType(String.class, new TypeBindings());
 }

 @Test
 public void testConstructor() {
 ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
 assertNotNull(type);
 }

 @Test
 public void testSetReference() {
 ResolvedRecursiveType type = createTestType();
 JavaType ref = new JavaType() {
 @Override
 public Class<?> getRawClass() {
 return String.class;
 }
 };
 type.setReference(ref);
 JavaType expected = ref;
 assertNotNull(type.getSelfReferencedType());
 assertEquals(expected, type.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType type = createTestType();
 JavaType ref1 = new JavaType() {
 @Override
 public Class<?> getRawClass() {
 return String.class;
 }
 };
 JavaType ref2 = new JavaType() {
 @Override
 public Class<?> getRawClass() {
 return String.class;
 }
 };
 type.setReference(ref1);
 type.setReference(ref2);
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = createTestType();
 assertNull(type.getSelfReferencedType());

 JavaType ref = new JavaType() {
 @Override
 public Class<?> getRawClass() {
 return String.class;
 }
 };
 type.setReference(ref);
 JavaType expected = ref;
 assertNotNull(type.getSelfReferencedType());
 assertEquals(expected, type.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = createTestType();
 StringBuilder sb = new StringBuilder();
 type.getGenericSignature(sb);
 assertNull(sb.toString());

 JavaType ref = new JavaType() {
 @Override
 public Class<?> getRawClass() {
 return String.class;
 }
 };
 type.setReference(ref);
 StringBuilderexpectedSb = new StringBuilder("[recursive type;");
 expectedSb.append(ref.getRawClass().getName());
 expectedSb.append("]");
 StringBuilder actualSb = new StringBuilder();
 type.getGenericSignature(actualSb);
 assertEquals(expectedSb.toString(), actualSb.toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = createTestType();
 StringBuilder sb = new StringBuilder();
 type.getErasedSignature(sb);
 assertNull(sb.toString());

 JavaType ref = new JavaType() {
 @Override
 public Class<?> getRawClass() {
 return String.class;
 }
 };
 type.setReference(ref);
 StringBuilder expectedSb = new StringBuilder("[recursive type;");
 expectedSb.append(ref.getRawClass().getName());
 expectedSb.append("]");
 StringBuilder actualSb = new StringBuilder();
 type.getErasedSignature(actualSb);
 assertEquals(expectedSb.toString(), actualSb.toString());
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = createTestType();
 JavaType contentType = new JavaType() {
 @Override
 public Class<?> getRawClass() {
 return String.class;
 }
 };
 JavaType expected = type;
 JavaType actual = type.withContentType(contentType);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType type = createTestType();
 Object handler = new Object() {};
 JavaType expected = type;
 JavaType actual = type.withTypeHandler(handler);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType type = createTestType();
 Object handler = new Object() {};
 JavaType expected = type;
 JavaType actual = type.withContentTypeHandler(handler);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType type = createTestType();
 Object handler = new Object() {};
 JavaType expected = type;
 JavaType actual = type.withValueHandler(handler);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType type = createTestType();
 Object handler = new Object() {};
 JavaType expected = type;
 JavaType actual = type.withContentValueHandler(handler);
 assertEquals(expected, actual);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType type = createTestType();
 JavaType expected = type;
 JavaType actual = type.withStaticTyping();
 assertEquals(expected, actual);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNarrow() {
 ResolvedRecursiveType type = createTestType();
 Class<?> subclass = Integer.class;
 JavaType expected = type;
 JavaType actual = type.narrow(subclass);
 assertEquals(expected, actual);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRefine() {
 ResolvedRecursiveType type = createTestType();
 Class<?> rawType = String.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = new JavaType() {
 @Override
 public Class<?> getRawClass() {
 return Object.class;
 }
 };
 JavaType[] superInterfaces = new JavaType[0];
 JavaType expected = null;
 JavaType actual = type.refine(rawType, bindings, superClass, superInterfaces);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType type = createTestType();
 assertFalse(type.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type = createTestType();
 String expected = "[recursive type; UNRESOLVED]";
 String actual = type.toString();
 assertEquals(expected, actual);

 JavaType ref = new JavaType() {
 @Override
 public Class<?> getRawClass() {
 return String.class;
 }
 };
 type.setReference(ref);
 String expectedToString = "[recursive type; " + ref.getRawClass().getName() + "]";
 String actualToString = type.toString();
 assertEquals(expectedToString, actualToString);
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = createTestType();
 ResolvedRecursiveType type2 = createTestType();
 assertFalse(type1.equals(type2));

 ResolvedRecursiveType type3 = new ResolvedRecursiveType(String.class, new TypeBindings());
 JavaType ref = new JavaType() {
 @Override
 public Class<?> getRawClass() {
 return String.class;
 }
 };
 type3.setReference(ref);
 assertTrue(type1.equals(type3));
 }

 @Test
 public void testHashCode() {
 ResolvedRecursiveType type1 = createTestType();
 ResolvedRecursiveType type2 = createTestType();
 JavaType ref1 = new JavaType() {
 @Override
 public Class<?> getRawClass() {
 return String.class;
 }
 };
 JavaType ref2 = new JavaType() {
 @Override
 public Class<?> getRawClass() {
 return String.class;
 }
 };
 type1.setReference(ref1);
 type2.setReference(ref2);
 int expectedHashCode = type1.hashCode();
 int actualHashCode = type2.hashCode();
 assertNotSame(expectedHashCode, actualHashCode);
 }
}