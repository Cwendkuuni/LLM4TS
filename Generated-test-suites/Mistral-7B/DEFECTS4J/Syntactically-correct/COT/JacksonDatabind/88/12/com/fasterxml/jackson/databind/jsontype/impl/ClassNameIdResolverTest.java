package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

public class ClassNameIdResolverTest extends BaseTest {

 private ClassNameIdResolver _resolver;
 private TypeFactory _typeFactory;
 private JavaType _javaType;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 _typeFactory = mapper().getTypeFactory();
 _javaType = mapper().constructType(HashMap.class);
 _resolver = new ClassNameIdResolver(_javaType, _typeFactory);
 }

 @Test
 public void testGetMechanism() {
 assertEquals(JsonTypeInfo.Id.CLASS, _resolver.getMechanism());
 }

 @Test
 public void testRegisterSubtype() {
 // Not testing this method as it's not used with class name - based resolvers
 }

 @Test
 public void testIdFromValue() throws Exception {
 Object value = new HashMap<>();
 assertEquals("java.util.HashMap", _resolver.idFromValue(value));
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 Object value = new HashMap<>();
 Class<?> type = HashMap.class;
 assertEquals("java.util.HashMap", _resolver.idFromValueAndType(value, type));
 }

 @Test
 public void testTypeFromId() throws Exception {
 String id = "java.util.HashMap";
 DatabindContext context = new JacksonAnnotationIntrospector().findAnnotationIntrospector(Object.class).findRootValueAnnotation(Object.class);
 JavaType javaType = _resolver.typeFromId(context, id);
 assertEquals(HashMap.class, javaType.getRawClass());
 }

 @Test(expected = IOException.class)
 public void testTypeFromIdWithInvalidId() throws Exception {
 String id = "com.nonexistent.class.Name";
 DatabindContext context = new JacksonAnnotationIntrospector().findAnnotationIntrospector(Object.class).findRootValueAnnotation(Object.class);
 _resolver.typeFromId(context, id);
 }

 @Test
 public void testTypeFromIdWithGenericId() throws Exception {
 String id = "java.util.ArrayList<java.lang.String>";
 DatabindContext context = new JacksonAnnotationIntrospector().findAnnotationIntrospector(Object.class).findRootValueAnnotation(Object.class);
 JavaType javaType = _resolver.typeFromId(context, id);
 assertEquals(new TypeReference<ArrayList<String>>(){}.getType(), javaType);
 }

 @Test
 public void testIdFromWithEnum() {
 Enum testEnum = Enum.valueOf(MyEnum.class, "TestValue");
 assertEquals("com.mypackage.MyEnum", _resolver.idFromValue(testEnum));
 }

 @Test
 public void testIdFromWithEnumSubclass() {
 Enum subEnum = Enum.valueOf(SubMyEnum.class, "SubTestValue");
 assertEquals("com.mypackage.SubMyEnum", _resolver.idFromValue(subEnum));
 }

 @Test
 public void testIdFromWithInnerClass() {
 Object outer = new OuterClass();
 assertEquals("com.mypackage.OuterClass$InnerClass", _resolver.idFromValue(outer.new InnerClass()));
 }

 @Test
 public void testIdFromWithArray() {
 Object[] value = {1, 2, 3};
 assertEquals("java.lang.Object[]", _resolver.idFromValue(value));
 }

 @Test
 public void testIdFromWithMap() {
 Map<String, Object> value = new HashMap<>();
 Class<?> valueClass = Object.class;
 assertEquals("java.util.Map<java.lang.String, java.lang.Object>", _resolver.idFromValueAndType(value, valueClass));
 }

 @Test
 public void testIdFromWithArrayOfMap() {
 Map<String, Object>[] value = new Map[1];
 value[0] = new HashMap<>();
 assertEquals("java.util.Map[]", _resolver.idFromValue(value));
 }

 @Test
 public void testGetDescForKnownTypeIds() {
 assertEquals("class name used as type id", _resolver.getDescForKnownTypeIds());
 }
}