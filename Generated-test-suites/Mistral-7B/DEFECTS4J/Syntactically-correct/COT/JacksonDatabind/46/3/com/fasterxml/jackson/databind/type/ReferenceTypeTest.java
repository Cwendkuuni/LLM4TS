package com.example.junit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

 private JavaType refType = new ClassType(String.class);
 private JavaType refTypeWithHandler = new ClassType(Integer.class);
 private JavaType refTypeWithStaticTyping = new ClassType(Boolean.class);

 @Test
 public void testConstruct() {
 ReferenceType referenceType = ReferenceType.construct(String.class, refType, null, null);
 assertEquals(String.class, referenceType.getRawClass());
 assertEquals(refType, referenceType.getContentType());
 assertNull(referenceType.getValueHandler());
 assertNull(referenceType.getTypeHandler());
 assertFalse(referenceType.isStatic());
 }

 @Test
 public void testWithTypeHandler() {
 JavaType typeHandler = mock(JavaType.class);
 ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
 ReferenceType newReferenceType = referenceType.withTypeHandler(typeHandler);
 assertSame(referenceType, newReferenceType); // No change if same handler
 assertNotSame(referenceType, new ReferenceType(String.class, refType, null, typeHandler, false)); // Different handler
 }

 @Test
 public void testWithContentTypeHandler() {
 JavaType valueHandler = mock(JavaType.class);
 JavaType typeHandler = mock(JavaType.class);
 ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
 ReferenceType newReferenceType = referenceType.withContentTypeHandler(typeHandler);
 assertSame(referenceType, newReferenceType); // No change if same handler
 assertNotSame(referenceType, new ReferenceType(String.class, refType, null, null, true)); // Static vs non-static
 assertNotSame(referenceType, new ReferenceType(String.class, refTypeWithHandler, null, null, false)); // Different contentType
 }

 @Test
 public void testWithValueHandler() {
 JavaType valueHandler = mock(JavaType.class);
 JavaType typeHandler = mock(JavaType.class);
 ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
 ReferenceType newReferenceType = referenceType.withValueHandler(valueHandler);
 assertSame(referenceType, newReferenceType); // No change if same handler
 assertNotSame(referenceType, new ReferenceType(String.class, refType, valueHandler, null, false)); // Different handler
 }

 @Test
 public void testWithContentValueHandler() {
 JavaType valueHandler = mock(JavaType.class);
 JavaType typeHandler = mock(JavaType.class);
 ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
 ReferenceType newReferenceType = referenceType.withContentValueHandler(valueHandler);
 assertSame(referenceType, newReferenceType); // No change if same handler
 assertNotSame(referenceType, new ReferenceType(String.class, refType, null, null, true)); // Static vs non-static
 assertNotSame(referenceType, new ReferenceType(String.class, refType, null, valueHandler, false)); // Different contentValueHandler
 }

 @Test
 public void testWithStaticTyping() {
 JavaType typeHandler = mock(JavaType.class);
 ReferenceType referenceType = new ReferenceType(String.class, refType, null, null, false);
 ReferenceType newReferenceType = referenceType.withStaticTyping();
 assertFalse(referenceType.isStatic());
 assertTrue(newReferenceType.isStatic());
 }

 @Test
 public void testGetReferencedType() {
 JavaType referencedType = new ClassType(Integer.class);
 ReferenceType referenceType = new ReferenceType(String.class, referencedType, null, null, false);
 assertEquals(referencedType, referenceType.getReferencedType());
 }

 @Test
 public void testIsReferenceType() {
 assertTrue(new ReferenceType(String.class, refType, null, null, false).isReferenceType());
 }

 @Test
 public void testContainedTypeCount() {
 assertEquals(1, new ReferenceType(String.class, refType, null, null, false).containedTypeCount());
 }

 @Test
 public void testContainedType() {
 assertEquals(refType, new ReferenceType(String.class, refType, null, null, false).containedType(0));
 }

 @Test
 public void testContainedTypeName() {
 assertEquals("T", new ReferenceType(String.class, refType, null, null, false).containedTypeName(0));
 }

 @Test
 public void testGetParameterSource() {
 assertEquals(String.class, new ReferenceType(String.class, refType, null, null, false).getParameterSource());
 }

 @Test
 public void testGetErasedSignature() {
 StringBuilder expected = new StringBuilder();
 _classSignature(String.class, expected, true);
 StringBuilder actual = new ReferenceType(String.class, refType, null, null, false).getErasedSignature(new StringBuilder());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testGetGenericSignature() {
 StringBuilder expected = new StringBuilder();
 _classSignature(String.class, expected, false);
 expected.append('<');
 expected = refType.getGenericSignature(expected);
 expected.append(';');
 StringBuilder actual = new ReferenceType(String.class, refType, null, null, false).getGenericSignature(new StringBuilder());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testNarrow() {
 Class<?> subclass = Integer.class;
 JavaType narrowed = new ReferenceType(String.class, refType, null, null, false).narrow(subclass);
 assertEquals(new ClassType(String.class), narrowed.getClass());
 assertEquals(refType, narrowed.getContentType());
 }

 @Test
 public void testToString() {
 String expected = "[reference type, class [Ljava.lang.String;<T>], class java.lang.String, class com.fasterxml.jackson.core.type.TypeReferenceType@1b6d7a9d]";
 assertEquals(expected, new ReferenceType(String[].class, refType, null, null, false).toString());
 }

 @Test
 public void testEquals() {
 ReferenceType refType1 = new ReferenceType(String.class, refType, null, null, false);
 ReferenceType refType2 = new ReferenceType(String.class, refType, null, null, false);
 assertEquals(refType1, refType2);

 ReferenceType refType3 = new ReferenceType(String.class, refTypeWithHandler, null, null, false);
 assertNotEquals(refType1, refType3);

 ReferenceType refType4 = new ReferenceType(Integer.class, refType, null, null, false);
 assertNotEquals(refType1, refType4);
 }

 private void _classSignature(Class<?> clazz, StringBuilder sb, boolean erased) {
 sb.append("[");
 if (clazz.isArray()) {
 int dimension = 0;
 while (clazz.isArray()) {
 sb.append("[");
 clazz = clazz.getComponentType();
 dimension++;
 }
 sb.append(clazz.getSimpleName());
 if (dimension > 0) {
 sb.append("[]");
 for (int i = 0; i < dimension; i++) {
 sb.append("[]");
 }
 }
 } else {
 sb.append(clazz.getSimpleName());
 }
 if (erased) {
 sb.append("<T>");
 }
 }
}