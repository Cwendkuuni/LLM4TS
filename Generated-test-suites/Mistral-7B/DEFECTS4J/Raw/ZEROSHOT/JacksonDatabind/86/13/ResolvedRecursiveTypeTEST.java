package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class ResolvedRecursiveTypeTest {

 private JavaType createType(Class<?> erasedType) {
 return new ResolvedRecursiveType(erasedType, new TypeBindings());
 }

 @Test
 public void testNewInstance() {
 ResolvedRecursiveType type = createType(String.class);
 assertNotNull(type);
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceMultipleTimes() {
 ResolvedRecursiveType type = createType(String.class);
 type.setReference(new SimpleType(Integer.class));
 type.setReference(new SimpleType(Double.class));
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType type = createType(ResolvedRecursiveType.class);
 type.setReference(new SimpleType(Integer.class));
 JavaType referencedType = type.getSelfReferencedType();
 assertNotNull(referencedType);
 assertInstanceOf(SimpleType.class, referencedType);
 }

 @Test
 public void testGetSelfReferencedTypeNull() {
 ResolvedRecursiveType type = createType(String.class);
 assertNull(type.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType type = createType(ResolvedRecursiveType.class);
 type.setReference(new SimpleType(Integer.class));
 StringBuilder sb = new StringBuilder();
 type.getGenericSignature(sb);
 assertNotNull(sb.toString());
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType type = createType(ResolvedRecursiveType.class);
 type.setReference(new SimpleType(Integer.class));
 StringBuilder sb = new StringBuilder();
 type.getErasedSignature(sb);
 assertNotNull(sb.toString());
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType type = createType(ResolvedRecursiveType.class);
 JavaType contentType = new SimpleType(Integer.class);
 JavaType result = type.withContentType(contentType);
 assertNotNull(result);
 assertSame(type, result);
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType type = createType(ResolvedRecursiveType.class);
 Object handler = new Object();
 JavaType result = type.withTypeHandler(handler);
 assertNotNull(result);
 assertSame(type, result);
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType type = createType(ResolvedRecursiveType.class);
 Object handler = new Object();
 JavaType result = type.withContentTypeHandler(handler);
 assertNotNull(result);
 assertSame(type, result);
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType type = createType(ResolvedRecursiveType.class);
 Object handler = new Object();
 JavaType result = type.withValueHandler(handler);
 assertNotNull(result);
 assertSame(type, result);
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType type = createType(ResolvedRecursiveType.class);
 Object handler = new Object();
 JavaType result = type.withContentValueHandler(handler);
 assertNotNull(result);
 assertSame(type, result);
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType type = createType(ResolvedRecursiveType.class);
 JavaType result = type.withStaticTyping();
 assertNotNull(result);
 assertSame(type, result);
 }

 @Test
 public void testNarrow() {
 ResolvedRecursiveType type = createType(ResolvedRecursiveType.class);
 Class<?> subclass = Object.class;
 JavaType result = type._narrow(subclass);
 assertNull(result);
 }

 @Test
 public void testRefine() {
 ResolvedRecursiveType type = createType(ResolvedRecursiveType.class);
 Class<?> rawType = Object.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = new SimpleType(Object.class);
 JavaType[] superInterfaces = new JavaType[0];
 JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
 assertNull(result);
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType type = createType(ResolvedRecursiveType.class);
 assertFalse(type.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType type = createType(ResolvedRecursiveType.class);
 StringBuilder sb = new StringBuilder();
 String result = type.toString();
 assertNotNull(result);
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType type1 = createType(ResolvedRecursiveType.class);
 ResolvedRecursiveType type2 = createType(ResolvedRecursiveType.class);
 assertFalse(type1.equals(type2));

 JavaType referencedType1 = new SimpleType(Integer.class);
 JavaType referencedType2 = new SimpleType(Integer.class);
 type1.setReference(referencedType1);
 type2.setReference(referencedType2);

 assertFalse(type1.equals(type2));

 JavaType referencedType3 = new SimpleType(Double.class);
 type1.setReference(referencedType3);
 assertFalse(type1.equals(type2));

 assertFalse(type1.equals(null));
 }
}