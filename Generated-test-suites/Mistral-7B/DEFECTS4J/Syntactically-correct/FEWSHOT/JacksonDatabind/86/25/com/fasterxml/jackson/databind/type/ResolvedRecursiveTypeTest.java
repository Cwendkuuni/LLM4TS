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
 ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = mockType();

 tested.setReference(ref);
 JavaType expected = ref;
 assertSame(expected, tested.getSelfReferencedType());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetReferenceTwice() {
 ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
 JavaType ref1 = mockType();
 JavaType ref2 = mockType();

 tested.setReference(ref1);
 tested.setReference(ref2);
 }

 @Test
 public void testGetSelfReferencedType() {
 ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = mockType();
 tested.setReference(ref);

 JavaType expected = ref;
 assertSame(expected, tested.getSelfReferencedType());
 }

 @Test
 public void testGetSelfReferencedTypeNull() {
 ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);

 JavaType expected = null;
 assertSame(expected, tested.getSelfReferencedType());
 }

 @Test
 public void testGetGenericSignature() {
 ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
 StringBuilder sb = new StringBuilder();
 JavaType ref = mockType();
 tested.setReference(ref);
 when(ref.getGenericSignature(sb)).thenReturn(sb);

 StringBuilder expected = sb;
 assertSame(expected, tested.getGenericSignature(sb));
 }

 @Test
 public void testGetErasedSignature() {
 ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
 StringBuilder sb = new StringBuilder();
 JavaType ref = mockType();
 tested.setReference(ref);
 when(ref.getErasedSignature(sb)).thenReturn(sb);

 StringBuilder expected = sb;
 assertSame(expected, tested.getErasedSignature(sb));
 }

 @Test
 public void testWithContentType() {
 ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
 JavaType contentType = mockType();

 JavaType expected = tested;
 assertSame(expected, tested.withContentType(contentType));
 }

 @Test
 public void testWithTypeHandler() {
 ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
 Object handler = mock(Object.class);

 JavaType expected = tested;
 assertSame(expected, tested.withTypeHandler(handler));
 }

 @Test
 public void testWithContentTypeHandler() {
 ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
 Object handler = mock(Object.class);

 JavaType expected = tested;
 assertSame(expected, tested.withContentTypeHandler(handler));
 }

 @Test
 public void testWithValueHandler() {
 ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
 Object handler = mock(Object.class);

 JavaType expected = tested;
 assertSame(expected, tested.withValueHandler(handler));
 }

 @Test
 public void testWithContentValueHandler() {
 ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
 Object handler = mock(Object.class);

 JavaType expected = tested;
 assertSame(expected, tested.withContentValueHandler(handler));
 }

 @Test
 public void testWithStaticTyping() {
 ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);

 JavaType expected = tested;
 assertSame(expected, tested.withStaticTyping());
 }

 @Test
 public void testNarrow() {
 ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);

 Class<?> subclass = String.class;
 JavaType expected = tested;
 assertSame(expected, tested.withContentType(subclass.getClass()));
 JavaType result = tested._narrow(subclass);
 assertSame(expected, result);
 }

 @Test
 public void testRefine() {
 ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
 Class<?> rawType = Integer.class;
 TypeBindings bindings = mock(TypeBindings.class);
 JavaType superClass = mockType();
 JavaType[] superInterfaces = new JavaType[0];

 JavaType expected = null;
 assertNull(tested.refine(rawType, bindings, superClass, superInterfaces));
 assertSame(expected, tested.getSelfReferencedType());
 }

 @Test
 public void testIsContainerType() {
 ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);

 boolean expected = false;
 assertFalse(expected, tested.isContainerType());
 }

 @Test
 public void testToString() {
 ResolvedRecursiveType tested = new ResolvedRecursiveType(Object.class, null);
 JavaType ref = mockType();
 tested.setReference(ref);

 StringBuilder expected = new StringBuilder("[recursive type; ");
 expected.append(ref.getRawClass().getName());
 expected.append("]");

 String result = tested.toString();
 assertEquals(expected.toString(), result);
 }

 @Test
 public void testEquals() {
 ResolvedRecursiveType tested1 = new ResolvedRecursiveType(Object.class, null);
 JavaType ref1 = mockType();
 tested1.setReference(ref1);

 ResolvedRecursiveType tested2 = new ResolvedRecursiveType(Object.class, null);
 JavaType ref2 = mockType();
 tested2.setReference(ref2);

 assertFalse(tested1.equals(tested2));

 ResolvedRecursiveType tested3 = new ResolvedRecursiveType(Object.class, null);
 tested3.setReference(ref1);

 assertTrue(tested1.equals(tested3));
 }
}