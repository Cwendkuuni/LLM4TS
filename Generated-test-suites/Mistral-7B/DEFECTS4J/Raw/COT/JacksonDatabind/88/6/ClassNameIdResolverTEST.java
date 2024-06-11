package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

/**
 * Test class for ClassNameIdResolver
 */
public class ClassNameIdResolverTest {

 private final ObjectMapper mapper = new ObjectMapper();
 private final TypeFactory typeFactory = mapper.getTypeFactory();

 @Test
 public void testConstructor() {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, typeFactory);
 assertNotNull(resolver);
 }

 @Test
 public void testGetMechanism() {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, typeFactory);
 assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
 }

 @Test
 public void testIdFromValue() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, typeFactory);
 String id = resolver.idFromValue("HelloWorld");
 assertEquals("java.lang.String", id);

 id = resolver.idFromValue(new Object());
 assertNull(id);
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, typeFactory);
 String id = resolver.idFromValueAndType("HelloWorld", String.class);
 assertEquals("java.lang.String", id);

 id = resolver.idFromValueAndType(new Object(), Object.class);
 assertNull(id);
 }

 @Test
 public void testTypeFromId() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, typeFactory);
 JavaType javaType = resolver.typeFromId(mapper.getDeserializationContext(), "java.lang.String");
 assertEquals(String.class, javaType.getRawClass());

 javaType = resolver.typeFromId(mapper.getDeserializationContext(), "java.util.ArrayList");
 assertEquals(ArrayList.class, javaType.getRawClass());

 javaType = resolver.typeFromId(mapper.getDeserializationContext(), "com.test.MyClass");
 assertEquals(MyClass.class, javaType.getRawClass());

 // Test for unknown types
 javaType = resolver.typeFromId(mapper.getDeserializationContext(), "UnknownType");
 assertNull(javaType);
 }

 @Test
 public void testIdFromWithEnum() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, typeFactory);
 Enum myEnum = Enum.valueOf(MyEnum.class, "VALUE1");
 String id = resolver.idFromValue(myEnum);
 assertEquals("com.test.MyEnum", id);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIdFromWithInvalidTypeId() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, typeFactory);
 String id = resolver.idFromValue("InvalidTypeId");
 fail("Should not reach here");
 }

 @Test
 public void testTypeFromIdWithCollectionTypes() throws Exception {
 TypeFactory tf = mapper.getTypeFactory();
 JavaType javaType = tf.constructCollectionType(EnumSet.class, MyEnum.class);
 String id = resolver.idFromValue(EnumSet.of(MyEnum.VALUE1, MyEnum.VALUE2));
 assertEquals(javaType.toCanonical(), id);

 id = resolver.idFromValue((Map<String, MyEnum>) Map.of("key1", MyEnum.VALUE1, "key2", MyEnum.VALUE2));
 assertEquals(tf.constructMapType(EnumMap.class, MyEnum.class, Object.class).toCanonical(), id);
 }

 @Test
 public void testTypeFromIdWithArrays() throws Exception {
 TypeFactory tf = mapper.getTypeFactory();
 Object[] array = new Object[] { MyEnum.VALUE1, MyEnum.VALUE2 };
 String id = resolver.idFromValue(array);
 assertEquals(tf.constructArrayType(MyEnum.class).toCanonical(), id);
 }

 @Test
 public void testTypeFromIdWithInnerClasses() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, typeFactory);
 Class innerClass = MyOuterClass.class.getDeclaredInnerClass("MyInnerClass");
 innerClass.setAccessible(true);
 Object innerInstance = innerClass.newInstance();
 String id = resolver.idFromValue(innerInstance);
 assertEquals("com.test.MyOuterClass$MyInnerClass", id);
 }

 @Test
 public void testTypeFromIdWithCustomTypeId() throws Exception {
 TypeFactory tf = mapper.getTypeFactory();
 JavaType javaType = tf.constructType(CustomType.class);
 String id = resolver.idFromValue(new CustomType());
 assertEquals(javaType.toCanonical(), id);
 }

 @Test(expected = java.lang.ClassNotFoundException.class)
 public void testTypeFromIdWithUnknownClass() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, typeFactory);
 String id = resolver.idFromValue("UnknownClass");
 fail("Should throw ClassNotFoundException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTypeFromIdWithInvalidId() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(Object.class, typeFactory);
 String id = resolver.idFromValue("InvalidId");
 fail("Should throw IllegalArgumentException");
 }
}