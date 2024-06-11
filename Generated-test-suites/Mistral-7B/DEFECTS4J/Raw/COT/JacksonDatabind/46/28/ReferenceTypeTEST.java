package com.example.junit; // replace with your test package

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.JavaType;

import org.junit.Test;

public class ReferenceTypeTest {

 private JavaType refTypeMock;

 @Before
 public void setup() {
 refTypeMock = mock(JavaType.class);
 }

 // Test constructor
 @Test
 public void testReferenceTypeConstruction() {
 Class<?> cls = new Class<Object>() {};
 ReferenceType referenceType = ReferenceType.construct(cls, refTypeMock, null, null);
 assertEquals(cls, referenceType.getRawClass());
 assertEquals(refTypeMock, referenceType.getReferencedType());
 }

 // Test methods that return 'this'
 @Test
 public void testWithTypeHandler() {
 ReferenceType referenceType = new ReferenceType(Object.class, refTypeMock, null, null, false);
 ReferenceType newReferenceType = referenceType.withTypeHandler(mock(Object.class));
 assertEquals(referenceType, newReferenceType);
 }

 @Test
 public void testWithContentTypeHandler() {
 JavaType contentTypeHandlerMock = mock(JavaType.class);
 ReferenceType referenceType = new ReferenceType(Object.class, refTypeMock, null, null, false);
 ReferenceType newReferenceType = referenceType.withContentTypeHandler(contentTypeHandlerMock);
 assertEquals(referenceType, newReferenceType);
 }

 @Test
 public void testWithValueHandler() {
 Object valueHandlerMock = mock(Object.class);
 ReferenceType referenceType = new ReferenceType(Object.class, refTypeMock, null, null, false);
 ReferenceType newReferenceType = referenceType.withValueHandler(valueHandlerMock);
 assertEquals(referenceType, newReferenceType);
 }

 @Test
 public void testWithContentValueHandler() {
 Object contentValueHandlerMock = mock(Object.class);
 ReferenceType referenceType = new ReferenceType(Object.class, refTypeMock, null, null, false);
 ReferenceType newReferenceType = referenceType.withContentValueHandler(contentValueHandlerMock);
 assertEquals(referenceType, newReferenceType);
 }

 @Test
 public void testWithStaticTyping() {
 ReferenceType referenceType = new ReferenceType(Object.class, refTypeMock, null, null, false);
 ReferenceType newReferenceType = referenceType.withStaticTyping();
 assertFalse(referenceType.isStatic());
 assertTrue(newReferenceType.isStatic());
 }

 // Test methods that return new instances
 @Test
 public void testNarrow() {
 Class<?> subclass = String.class;
 ReferenceType referenceType = new ReferenceType(Object.class, refTypeMock, null, null, false);
 JavaType narrowedType = referenceType.narrow(subclass);
 assertEquals(new ReferenceType(subclass, refTypeMock, null, null, false), narrowedType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNarrow_InvalidSubtype() {
 Class<?> invalidSubtype = Integer.class;
 ReferenceType referenceType = new ReferenceType(Object.class, refTypeMock, null, null, false);
 JavaType narrowedType = referenceType.narrow(invalidSubtype);
 }

 @Test
 public void testGetReferencedType() {
 ReferenceType referenceType = new ReferenceType(Object.class, refTypeMock, null, null, false);
 assertEquals(refTypeMock, referenceType.getReferencedType());
 }

 @Test
 public void testIsReferenceType() {
 ReferenceType referenceType = new ReferenceType(Object.class, refTypeMock, null, null, false);
 assertTrue(referenceType.isReferenceType());
 }

 // Test methods that return the number of contained types
 @Test
 public void testContainedTypeCount() {
 ReferenceType referenceType = new ReferenceType(Object.class, refTypeMock, null, null, false);
 assertEquals(1, referenceType.containedTypeCount());
 }

 @Test
 public void testContainedType() {
 ReferenceType referenceType = new ReferenceType(Object.class, refTypeMock, null, null, false);
 JavaType containedType = referenceType.containedType(0);
 assertSame(refTypeMock, containedType);
 }

 @Test
 public void testContainedTypeName() {
 ReferenceType referenceType = new ReferenceType(Object.class, refTypeMock, null, null, false);
 assertNull(referenceType.containedTypeName(0));
 }

 @Test
 public void testGetParameterSource() {
 ReferenceType referenceType = new ReferenceType(Object.class, refTypeMock, null, null, false);
 assertEquals(Object.class, referenceType.getParameterSource());
 }

 @Test
 public void testGetErasedSignature() {
 ReferenceType referenceType = new ReferenceType(Object.class, refTypeMock, null, null, false);
 StringBuilder expected = new StringBuilder();
 expected.append("Ljava/lang/Object;");
 StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testGetGenericSignature() {
 ReferenceType referenceType = new ReferenceType(Object.class, refTypeMock, null, null, false);
 StringBuilder expected = new StringBuilder();
 expected.append("Ljava/lang/Object;<T:Ljava/lang/Object;>L");
 StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testToString() {
 ReferenceType referenceType = new ReferenceType(Object.class, refTypeMock, null, null, false);
 String expected = "[reference type, class [Ljava.lang.Object;<T:Ljava.lang.Object;>]";
 String actual = referenceType.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testEquals() {
 ReferenceType referenceType1 = new ReferenceType(Object.class, refTypeMock, null, null, false);
 ReferenceType referenceType2 = new ReferenceType(Object.class, refTypeMock, null, null, false);
 assertTrue(referenceType1.equals(referenceType2));
 }

 @Test
 public void testNotEqualDifferentClasses() {
 ReferenceType referenceType1 = new ReferenceType(Object.class, refTypeMock, null, null, false);
 ReferenceType referenceType2 = new ReferenceType(String.class, refTypeMock, null, null, false);
 assertFalse(referenceType1.equals(referenceType2));
 }

 @Test
 public void testNotEqualDifferentReferencedTypes() {
 ReferenceType referenceType1 = new ReferenceType(Object.class, refTypeMock, null, null, false);
 ReferenceType referenceType2 = new ReferenceType(Object.class, mock(JavaType.class), null, null, false);
 assertFalse(referenceType1.equals(referenceType2));
 }
}