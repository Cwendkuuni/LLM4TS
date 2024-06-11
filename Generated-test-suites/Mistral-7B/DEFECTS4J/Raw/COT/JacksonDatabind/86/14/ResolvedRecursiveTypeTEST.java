package com.yourpackage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

 // Test constructor
 @Test
 public void testResolvedRecursiveTypeConstructor() {
 ResolvedRecursiveType actual = new ResolvedRecursiveType(TestClass.class, null);
 assertNotNull(actual);
 }

 // Test setReference method
 @Test
 public void testSetReferenceMethod() {
 ResolvedRecursiveType testObject = new ResolvedRecursiveType(TestClass.class, null);
 JavaType ref = new SimpleType(Integer.class);
 testObject.setReference(ref);
 JavaType expected = ref;
 JavaType actual = testObject.getSelfReferencedType();
 assertEquals(expected, actual);
 }

 // Test getSelfReferencedType method
 @Test
 public void testGetSelfReferencedTypeMethod() {
 ResolvedRecursiveType testObject = new ResolvedRecursiveType(TestClass.class, null);
 JavaType expected = null;
 JavaType actual = testObject.getSelfReferencedType();
 assertEquals(expected, actual);

 testObject.setReference(new SimpleType(Integer.class));
 expected = new SimpleType(Integer.class);
 actual = testObject.getSelfReferencedType();
 assertEquals(expected, actual);
 }

 // Test equals method
 @Test
 public void testEqualsMethod() {
 ResolvedRecursiveType testObject1 = new ResolvedRecursiveType(TestClass.class, null);
 ResolvedRecursiveType testObject2 = new ResolvedRecursiveType(TestClass.class, null);

 assertEquals(testObject1, testObject2);

 JavaType ref = new SimpleType(Integer.class);
 testObject1.setReference(ref);
 testObject2.setReference(ref);

 assertEquals(testObject1, testObject2);

 testObject1 = new ResolvedRecursiveType(TestClass.class, null);
 testObject2 = new ResolvedRecursiveType(Object.class, null);

 assertNotEquals(testObject1, testObject2);
 }

 // Test toString method
 @Test
 public void testToStringMethod() {
 ResolvedRecursiveType testObject = new ResolvedRecursiveType(TestClass.class, null);
 String expected = "[recursive type; UNRESOLVED]";
 String actual = testObject.toString();
 assertEquals(expected, actual);

 JavaType ref = new SimpleType(Integer.class);
 testObject.setReference(ref);
 expected = "[recursive type; " + ref.getRawClass().getName() + "]";
 actual = testObject.toString();
 assertEquals(expected, actual);
 }

 // Test equals with null object
 @Test
 public void testEqualsWithNullObject() {
 ResolvedRecursiveType testObject = new ResolvedRecursiveType(TestClass.class, null);
 assertNotEquals(testObject, null);
 }

 // Test equals with different classes
 @Test
 public void testEqualsWithDifferentClasses() {
 ResolvedRecursiveType testObject1 = new ResolvedRecursiveType(TestClass.class, null);
 Object otherObject = new Object();
 assertNotEquals(testObject1, otherObject);
 }

 // Test setReference multiple times
 @Test(expected = IllegalStateException.class)
 public void testSetReferenceMultipleTimes() {
 ResolvedRecursiveType testObject = new ResolvedRecursiveType(TestClass.class, null);
 JavaType ref1 = new SimpleType(Integer.class);
 testObject.setReference(ref1);
 JavaType ref2 = new SimpleType(Long.class);
 testObject.setReference(ref2);
 }

 // Test getGenericSignature method
 @Test
 public void testGetGenericSignatureMethod() {
 ResolvedRecursiveType testObject = new ResolvedRecursiveType(TestClass.class, null);
 StringBuilder expected = new StringBuilder();
 StringBuilder actual = testObject.getGenericSignature(expected);
 assertEquals("", actual.toString());

 JavaType ref = new SimpleType(Integer.class);
 testObject.setReference(ref);
 expected.append("T");
 actual = testObject.getGenericSignature(expected);
 assertEquals("T", actual.toString());
 }

 // Test getErasedSignature method
 @Test
 public void testGetErasedSignatureMethod() {
 ResolvedRecursiveType testObject = new ResolvedRecursiveType(TestClass.class, null);
 StringBuilder expected = new StringBuilder();
 StringBuilder actual = testObject.getErasedSignature(expected);
 assertEquals("", actual.toString());

 JavaType ref = new SimpleType(Integer.class);
 testObject.setReference(ref);
 expected.append(RefusedClass.class.getName());
 actual = testObject.getErasedSignature(expected);
 assertEquals(RefusedClass.class.getName(), actual.toString());
 }

 // Test withContentType method
 @Test
 public void testWithContentTypeMethod() {
 ResolvedRecursiveType testObject = new ResolvedRecursiveType(TestClass.class, null);
 JavaType contentType = new SimpleType(Integer.class);
 JavaType expected = testObject;
 JavaType actual = testObject.withContentType(contentType);
 assertEquals(expected, actual);
 }

 // Test withTypeHandler method
 @Test
 public void testWithTypeHandlerMethod() {
 ResolvedRecursiveType testObject = new ResolvedRecursiveType(TestClass.class, null);
 Object handler = new Object();
 JavaType expected = testObject;
 JavaType actual = testObject.withTypeHandler(handler);
 assertEquals(expected, actual);
 }

 // Test withContentTypeHandler method
 @Test
 public void testWithContentTypeHandlerMethod() {
 ResolvedRecursiveType testObject = new ResolvedRecursiveType(TestClass.class, null);
 Object handler = new Object();
 JavaType expected = testObject;
 JavaType actual = testObject.withContentTypeHandler(handler);
 assertEquals(expected, actual);
 }

 // Test withValueHandler method
 @Test
 public void testWithValueHandlerMethod() {
 ResolvedRecursiveType testObject = new ResolvedRecursiveType(TestClass.class, null);
 Object handler = new Object();
 JavaType expected = testObject;
 JavaType actual = testObject.withValueHandler(handler);
 assertEquals(expected, actual);
 }

 // Test withContentValueHandler method
 @Test
 public void testWithContentValueHandlerMethod() {
 ResolvedRecursiveType testObject = new ResolvedRecursiveType(TestClass.class, null);
 Object handler = new Object();
 JavaType expected = testObject;
 JavaType actual = testObject.withContentValueHandler(handler);
 assertEquals(expected, actual);
 }

 // Test withStaticTyping method
 @Test
 public void testWithStaticTypingMethod() {
 ResolvedRecursiveType testObject = new ResolvedRecursiveType(TestClass.class, null);
 JavaType expected = testObject;
 JavaType actual = testObject.withStaticTyping();
 assertEquals(expected, actual);
 }

 // Test _narrow method (Deprecated)
 @Test
 public void testNarrowMethod() {
 ResolvedRecursiveType testObject = new ResolvedRecursiveType(TestClass.class, null);
 Class<?> subclass = TestSubClass.class;
 JavaType expected = testObject;
 JavaType actual = testObject._narrow(subclass);
 assertEquals(expected, actual);
 }

 // Test refine method
 @Test
 public void testRefineMethod() {
 ResolvedRecursiveType testObject = new ResolvedRecursiveType(TestClass.class, null);
 Class<?> rawType = TestClassWithInterface.class;
 TypeBindings bindings = new TypeBindings();
 JavaType superClass = new SimpleType(TestSuperClass.class);
 JavaType[] superInterfaces = new JavaType[] { new SimpleType(TestInterface.class) };
 JavaType expected = null;
 JavaType actual = testObject.refine(rawType, bindings, superClass, superInterfaces);
 assertNull(actual);
 }

 // Test isContainerType method
 @Test
 public void testIsContainerTypeMethod() {
 ResolvedRecursiveType testObject = new ResolvedRecursiveType(TestClass.class, null);
 assertFalse(testObject.isContainerType());
 }
}

class TestClass {
}

class TestSubClass extends TestClass {
}

class TestClassWithInterface implements TestInterface {
}

class TestSuperClass {
}

class TestInterface {
}

class RefusedClass {
}