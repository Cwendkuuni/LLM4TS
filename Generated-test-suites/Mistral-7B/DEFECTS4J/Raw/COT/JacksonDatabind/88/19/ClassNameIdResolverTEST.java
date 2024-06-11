package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

public class ClassNameIdResolverTest {

 private ObjectMapper objectMapper;
 private TypeFactory typeFactory;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 typeFactory = objectMapper.getTypeFactory();
 }

 /**
 * Test method for {@link ClassNameIdResolver#ClassNameIdResolver(JavaType, TypeFactory)}
 */
 @Test
 public void testClassNameIdResolverConstructor() {
 // Create a test ClassNameIdResolver instance
 ClassNameIdResolver classResolver = new ClassNameIdResolver(typeFactory.constructClass("java.lang.String"), typeFactory);
 // Check if the constructor was called correctly
 assertEquals("java.lang.String", classResolver.getBaseType().toString());
 }

 /**
 * Test method for {@link ClassNameIdResolver#getMechanism()}
 */
 @Test
 public void testGetMechanism() {
 ClassNameIdResolver classResolver = new ClassNameIdResolver(typeFactory.constructClass("java.lang.String"), typeFactory);
 assertEquals(JsonTypeInfo.Id.CLASS, classResolver.getMechanism());
 }

 /**
 * Test method for {@link ClassNameIdResolver#registerSubtype(Class, String)}
 */
 @Test(expected = UnsupportedOperationException.class)
 public void testRegisterSubtype() {
 ClassNameIdResolver classResolver = new ClassNameIdResolver(typeFactory.constructClass("java.lang.String"), typeFactory);
 classResolver.registerSubtype(typeFactory.constructClass("java.lang.Integer"), "Integer");
 }

 /**
 * Test method for {@link ClassNameIdResolver#idFromValue(Object)}
 */
 @Test
 public void testIdFromValue() throws IOException {
 // Test with a simple Java object
 String id = new ClassNameIdResolver(typeFactory.constructClass("java.lang.String"), typeFactory).idFromValue(new String("Hello World"));
 assertEquals("java.lang.String", id);

 // Test with a Java enum
 Enum e = Enum.valueOf(Enum.class, "TEST");
 id = new ClassNameIdResolver(typeFactory.constructClass(Enum.class), typeFactory).idFromValue(e);
 assertEquals("java.lang.Enum", id);

 // Test with a Java collection
 List<String> list = Arrays.asList("Test1", "Test2", "Test3");
 id = new ClassNameIdResolver(typeFactory.constructCollectionType(ArrayList.class, typeFactory.constructClass("java.lang.String")), typeFactory).idFromValue(list);
 assertEquals("java.util.ArrayList", id);

 // Test with a Java Map
 Map<String, Integer> map = new HashMap<>();
 map.put("Test1", 1);
 map.put("Test2", 2);
 id = new ClassNameIdResolver(typeFactory.constructMapType(HashMap.class, typeFactory.constructClass("java.lang.String"), typeFactory.constructClass("java.lang.Integer")), typeFactory).idFromValue(map);
 assertEquals("java.util.HashMap", id);
}

 /**
 * Test method for {@link ClassNameIdResolver#idFromValueAndType(Object, Class)}
 */
 @Test
 public void testIdFromValueAndType() throws IOException {
 // Test with a simple Java object
 String id = new ClassNameIdResolver(typeFactory.constructClass("java.lang.String"), typeFactory).idFromValueAndType(new String("Hello World"), String.class);
 assertEquals("java.lang.String", id);

 // Test with a Java enum
 Enum e = Enum.valueOf(Enum.class, "TEST");
 id = new ClassNameIdResolver(typeFactory.constructClass(Enum.class), typeFactory).idFromValueAndType(e, Enum.class);
 assertEquals("java.lang.Enum", id);
}

 /**
 * Test method for {@link ClassNameIdResolver#typeFromId(DatabindContext, String)}
 */
 @Test
 public void testTypeFromId() throws IOException {
 // Test with a simple Java class
 JavaType javaType = new ClassNameIdResolver(typeFactory.constructClass("java.lang.String"), typeFactory).typeFromId(objectMapper.getDeserializationContext(), "java.lang.String");
 assertEquals(typeFactory.constructClass("java.lang.String"), javaType);

 // Test with a Java collection
 JavaType javaTypeList = new ClassNameIdResolver(typeFactory.constructCollectionType(ArrayList.class, typeFactory.constructClass("java.lang.String")), typeFactory).typeFromId(objectMapper.getDeserializationContext(), "java.util.ArrayList");
 assertEquals(typeFactory.constructCollectionType(ArrayList.class, typeFactory.constructClass("java.lang.String")), javaTypeList);

 // Test with a custom class
 JavaType customType = new ClassNameIdResolver(typeFactory.constructClass("com.example.MyClass"), typeFactory).typeFromId(objectMapper.getDeserializationContext(), "com.example.MyClass");
 assertEquals(typeFactory.constructClass("com.example.MyClass"), customType);
}

 /**
 * Test method for {@link ClassNameIdResolver#typeFromId(DatabindContext, String)} with non-existent class.
 */
 @Test(expected = IOException.class)
 public void testTypeFromIdIOException() throws IOException {
 new ClassNameIdResolver(typeFactory.constructClass("java.lang.String"), typeFactory).typeFromId(objectMapper.getDeserializationContext(), "com.nonexistent.Package.NonExistentClass");
}

 /**
 * Test method for {@link ClassNameIdResolver#typeFromId(DatabindContext, String)} with generic class.
 */
 @Test
 public void testTypeFromIdGenericClass() throws IOException {
 JavaType javaTypeList = new ClassNameIdResolver(typeFactory.constructCollectionType(ArrayList.class, typeFactory.constructClass("java.lang.String")), typeFactory).typeFromId(objectMapper.getDeserializationContext(), "java.util.List<java.lang.String>");
 assertEquals(typeFactory.constructCollectionType(ArrayList.class, typeFactory.constructClass("java.lang.String")), javaTypeList);
}
}