package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

 JavaType referencedTypeMock;
 ResolvedRecursiveType testedInstance;

 @Before
 public void setUp() {
 referencedTypeMock = mock(JavaType.class);
 testedInstance = new ResolvedRecursiveType(Object.class, null) {
 @Override
 public void setReference(JavaType ref) {
 super.setReference(ref);
 setReference(referencedTypeMock);
 }
 };
 }

 @Test
 public void testSetReferenceWithNullValue() {
 testedInstance.setReference(null);
 assertNull(testedInstance.getSelfReferencedType());
 }

 @Test
 public void testSetReferenceWithValidJavaType() {
 testedInstance.setReference(referencedTypeMock);
 assertSame(referencedTypeMock, testedInstance.getSelfReferencedType());
 }

 @Test
 public void testGetSelfReferencedTypeWithNullValue() {
 assertNull(testedInstance.getSelfReferencedType());
 }

 @Test
 public void testGetSelfReferencedTypeWithMockedJavaType() {
 when(referencedTypeMock.getRawClass()).thenReturn(String.class);
 testedInstance.setReference(referencedTypeMock);
 assertEquals(String.class, testedInstance.getSelfReferencedType().getRawClass());
 }

 @Test
 public void testGetGenericSignatureWithMockedJavaType() {
 StringBuilder sbMock = mock(StringBuilder.class);
 when(referencedTypeMock.getGenericSignature(sbMock)).thenReturn(sbMock);
 StringBuilder result = testedInstance.getGenericSignature(new StringBuilder());
 assertSame(sbMock, result);
 }

 @Test
 public void testGetErasedSignatureWithMockedJavaType() {
 StringBuilder sbMock = mock(StringBuilder.class);
 when(referencedTypeMock.getErasedSignature(sbMock)).thenReturn(sbMock);
 StringBuilder result = testedInstance.getErasedSignature(new StringBuilder());
 assertSame(sbMock, result);
 }

 @Test
 public void testWithContentType() {
 JavaType contentTypeMock = mock(JavaType.class);
 testedInstance.withContentType(contentTypeMock);
 assertSame(testedInstance, testedInstance.withContentType(contentTypeMock));
 }

 @Test
 public void testWithTypeHandler() {
 Object handlerMock = mock(Object.class);
 testedInstance.withTypeHandler(handlerMock);
 assertSame(testedInstance, testedInstance.withTypeHandler(handlerMock));
 }

 @Test
 public void testWithContentTypeHandler() {
 Object handlerMock = mock(Object.class);
 testedInstance.withContentTypeHandler(handlerMock);
 assertSame(testedInstance, testedInstance.withContentTypeHandler(handlerMock));
 }

 @Test
 public void testWithValueHandler() {
 Object handlerMock = mock(Object.class);
 testedInstance.withValueHandler(handlerMock);
 assertSame(testedInstance, testedInstance.withValueHandler(handlerMock));
 }

 @Test
 public void testWithContentValueHandler() {
 Object handlerMock = mock(Object.class);
 testedInstance.withContentValueHandler(handlerMock);
 assertSame(testedInstance, testedInstance.withContentValueHandler(handlerMock));
 }

 @Test
 public void testWithStaticTyping() {
 testedInstance.withStaticTyping();
 assertSame(testedInstance, testedInstance.withStaticTyping());
 }

 @Test
 public void testNarrowWithClass() {
 Class<?> subclass = String.class;
 assertSame(testedInstance, testedInstance.withContentType(subclass));
 assertSame(testedInstance, testedInstance.withTypeHandler(subclass));
 assertSame(testedInstance, testedInstance.withContentValueHandler(subclass));
 assertSame(testedInstance, testedInstance.narrow(subclass));
 }

 @Test
 public void testRefineWithNullBindings() {
 TypeBindings bindingsMock = mock(TypeBindings.class);
 assertNull(testedInstance.refine(Object.class, bindingsMock, null, new JavaType[0]));
 }

 @Test
 public void testIsContainerType() {
 assertFalse(testedInstance.isContainerType());
 }

 @Test
 public void testToString() {
 StringBuilder sbMock = mock(StringBuilder.class);
 when(sbMock.toString()).thenReturn("Expected toString result");
 when(referencedTypeMock.getRawClass()).thenReturn(String.class);
 testedInstance.setReference(referencedTypeMock);
 assertEquals("ExpectedtoStringresult[recursivetype;String]", testedInstance.toString());
 }

 @Test
 public void testEqualsWithSameInstance() {
 assertEquals(testedInstance, testedInstance);
 }

 @Test
 public void testEqualsWithDifferentClass() {
 ResolvedRecursiveType otherInstance = new ResolvedRecursiveType(Integer.class, null);
 assertNotEquals(testedInstance, otherInstance);
 }

 @Test
 public void testEqualsWithDifferentReferencedType() {
 JavaType otherReferencedTypeMock = mock(JavaType.class);
 testedInstance.setReference(referencedTypeMock);
 ResolvedRecursiveType otherInstance = new ResolvedRecursiveType(Object.class, null) {
 @Override
 public void setReference(JavaType ref) {
 super.setReference(ref);
 setReference(otherReferencedTypeMock);
 }
 };
 assertNotEquals(testedInstance, otherInstance);
 }
}