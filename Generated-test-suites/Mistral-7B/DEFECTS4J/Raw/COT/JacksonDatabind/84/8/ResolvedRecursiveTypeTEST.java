package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeBindings.TypeSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ResolvedRecursiveTypeTest {

 private JavaType _referencedTypeMock;
 private TypeBindings _bindingsMock;
 private ResolvedRecursiveType _sut;

 @Before
 public void setUp() {
 _referencedTypeMock = mock(JavaType.class);
 _bindingsMock = mock(TypeBindings.class);
 _sut = new ResolvedRecursiveType(Object.class, _bindingsMock) {
 @Override
 public JavaType getSelfReferencedType() {
 return _referencedTypeMock;
 }
 };
 }

 // Test constructor
 @Test
 public void testConstructor() {
 assertNull(_sut.getSelfReferencedType());
 }

 // Test setReference method
 @Test
 public void testSetReference() {
 JavaType ref = mock(JavaType.class);
 _sut.setReference(ref);
 assertSame(ref, _sut.getSelfReferencedType());
 }

 // Test getSelfReferencedType method
 @Test
 public void testGetSelfReferencedType() {
 when(_referencedTypeMock.getRawClass()).thenReturn(String.class);
 _sut.setReference(_referencedTypeMock);
 Type type = new TypeReference("java.lang.String") {}.getType();
 assertEquals(type, _sut.getSelfReferencedType());
 }

 // Test getGenericSignature method
 @Test
 public void testGetGenericSignature() {
 StringBuilder sb = new StringBuilder();
 _sut.getGenericSignature(sb);
 assertEquals("[Ljava.lang.Class;", sb.toString());
 }

 // Test getErasedSignature method
 @Test
 public void testGetErasedSignature() {
 StringBuilder sb = new StringBuilder();
 _sut.getErasedSignature(sb);
 assertEquals("java.lang.Object", sb.toString());
 }

 // Test withContentType method
 @Test
 public void testWithContentType() {
 JavaType contentType = mock(JavaType.class);
 assertEquals(_sut, _sut.withContentType(contentType));
 }

 // Test withTypeHandler method
 @Test
 public void testWithTypeHandler() {
 Object handler = mock(Object.class);
 assertEquals(_sut, _sut.withTypeHandler(handler));
 }

 // Test withContentTypeHandler method
 @Test
 public void testWithContentTypeHandler() {
 Object handler = mock(Object.class);
 assertEquals(_sut, _sut.withContentTypeHandler(handler));
 }

 // Test withValueHandler method
 @Test
 public void testWithValueHandler() {
 Object handler = mock(Object.class);
 assertEquals(_sut, _sut.withValueHandler(handler));
 }

 // Test withContentValueHandler method
 @Test
 public void testWithContentValueHandler() {
 Object handler = mock(Object.class);
 assertEquals(_sut, _sut.withContentValueHandler(handler));
 }

 // Test withStaticTyping method
 @Test
 public void testWithStaticTyping() {
 assertEquals(_sut, _sut.withStaticTyping());
 }

 // Test isContainerType method
 @Test
 public void testIsContainerType() {
 assertFalse(_sut.isContainerType());
 }

 // Test toString method
 @Test
 public void testToString() {
 String expected = "[recursive type; UNRESOLVED]";
 assertEquals(expected, _sut.toString());
 }

 // Test equals method
 @Test
 public void testEquals() {
 ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, _bindingsMock);
 assertFalse(_sut.equals(other));
 }

 // Test equals with same self-referenced type
 @Test
 public void testEqualsWithSameSelfReferencedType() {
 when(_referencedTypeMock.equals(_referencedTypeMock)).thenReturn(true);
 ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, _bindingsMock) {
 @Override
 public JavaType getSelfReferencedType() {
 return _referencedTypeMock;
 }
 };
 assertTrue(_sut.equals(other));
 }

 // Test illegal state exception when trying to set self reference multiple times
 @Test
 public void testIllegalStateExceptionWhenSettingSelfReferenceMultipleTimes() {
 _sut.setReference(_referencedTypeMock);
 assertThrows(IllegalStateException.class, () -> _sut.setReference(_referencedTypeMock));
 }
}