package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

 JavaType mockType() {
 return mock(JavaType.class);
 }

 @Test
 public void testSetReference() {
 ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = mockType();

 actual.setReference(ref);
 JavaType expected = ref;

 assertEquals(expected, actual.getSelfReferencedType());
 }

 @Test(expected = IllegialStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, null);
 JavaType ref1 = mockType();
 JavaType ref2 = mockType();

 actual.setReference(ref1);
 actual.setReference(ref2);
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, null);
 JavaType expected = null;

 assertNull(actual.getSelfReferencedType());
 }

 @Test
 public void testGetSelfReferencedTypeWithReference() {
 ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = mockType();
 actual.setReference(ref);
 JavaType expected = ref;

 assertEquals(expected, actual.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, null);
 StringBuilder expected = new StringBuilder();

 assertSame(expected, actual.getGenericSignature(expected));
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, null);
 StringBuilder expected = new StringBuilder();

 assertSame(expected, actual.getErasedSignature(expected));
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, null);
 JavaType contentType = mockType();

 assertEquals(actual, actual.withContentType(contentType));
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, null);
 Object handler = mock(Object.class);

 assertEquals(actual, actual.withTypeHandler(handler));
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, null);
 Object handler = mock(Object.class);

 assertEquals(actual, actual.withContentTypeHandler(handler));
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, null);
 Object handler = mock(Object.class);

 assertEquals(actual, actual.withValueHandler(handler));
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, null);
 Object handler = mock(Object.class);

 assertEquals(actual, actual.withContentValueHandler(handler));
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, null);

 assertEquals(actual, actual.withStaticTyping());
 }

 @Test
 public void testNarrow() {
 ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, null);

 assertSame(actual, actual.withContentType(mockType()));
 assertSame(actual, actual.withTypeHandler(mock(Object.class)));
 assertSame(actual, actual.withContentTypeHandler(mock(Object.class)));
 assertSame(actual, actual.withValueHandler(mock(Object.class)));
 assertSame(actual, actual.withContentValueHandler(mock(Object.class)));
 assertSame(actual, actual.withStaticTyping());
 }

 @Test
 public void testRefine() {
 ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, null);
 Class<?> rawType = String.class;
 TypeBindings bindings = mock(TypeBindings.class);
 JavaType superClass = mockType();
 JavaType[] superInterfaces = new JavaType[] { mockType() };

 assertEquals(null, actual.refine(rawType, bindings, superClass, superInterfaces));
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, null);

 assertFalse(actual.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType actual = new ResolvedRecursiveType(Object.class, null);
 StringBuilder expected = new StringBuilder();

 assertEquals("[recursive type; UNRESOLVED]", actual.toString());

 JavaType ref = mockType();
 actual.setReference(ref);
 expected.append("[recursive type; ");
 expected.append(ref.getRawClass().getName());
 expected.append("]");

 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType actual1 = new ResolvedRecursiveType(Object.class, null);
 ResolvedRecursiveType actual2 = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = mockType();

 assertFalse(actual1.equals(actual2));

 ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, null);
 other.setReference(ref);

 assertTrue(actual1.equals(other));
 }
}