package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class ClassNameIdResolverTest {

 private ClassNameIdResolver resolver;
 private ObjectMapper mapper;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 resolver = new ClassNameIdResolver(mapper.getTypeFactory().constructType(Object.class), mapper.getTypeFactory());
 }

 @Test
 public void testIdFromValue() throws Exception {
 Object obj = new Object();
 String id = resolver.idFromValue(obj);
 assertEquals("java.lang.Object", id);
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 Object obj = new Object();
 String id = resolver.idFromValueAndType(obj, Object.class);
 assertEquals("java.lang.Object", id);
 }

 @Test
 public void testIdFromValueEnum() throws Exception {
 Enum e = Enum.values()[0];
 String id = resolver.idFromValue(e);
 String expected = EnumSet.class.getName() + "<" + e.getClass().getName() + ">";
 assertEquals(expected, id);
 }

 @Test
 public void testIdFromValueEnumMap() throws Exception {
 Enum e = Enum.values()[0];
 Map<String, Object> map = new HashMap<>();
 map.put("key", e);
 String id = resolver.idFromValue(map);
 String expected = EnumMap.class.getName() + "<java.lang.String,?" + e.getClass().getName() + ">";
 assertEquals(expected, id);
 }

 @Test
 public void testIdFromValueArray() throws Exception {
 Object[] array = new Object[0];
 String id = resolver.idFromValueAndType(array, Object[].class);
 assertEquals("java.lang.Object[]", id);
 }

 @Test
 public void testIdFromValueCollection() throws Exception {
 List<Object> list = Arrays.asList(new Object[0]);
 String id = resolver.idFromValueAndType(list, List.class);
 assertEquals("java.util.ArrayList", id);
 }

 @Test
 public void testIdFromValueArrayEnum() throws Exception {
 Enum[] array = new Enum[0];
 String id = resolver.idFromValueAndType(array, Enum[].class);
 assertEquals("java.lang.Enum[]", id);
 }

 @Test
 public void testIdFromValueCollectionEnum() throws Exception {
 List<Enum> list = Arrays.asList(Enum.values());
 String id = resolver.idFromValueAndType(list, List.class);
 String expected = ArrayList.class.getName() + "<java.lang.Enum>";
 assertEquals(expected, id);
 }

 @Test
 public void testTypeFromId() throws Exception {
 String id = "java.lang.String";
 JavaType type = resolver.typeFromId(mapper.getDataSource(), id);
 assertEquals(String.class, type.getRawClass());
 }

 @Test
 public void testTypeFromIdIgnoreClassNotFound() throws Exception {
 String id = "com.nonexistent.Class";
 JavaType type = resolver.typeFromId(mapper.getDataSource(), id);
 assertNull(type);
 }

 @Test
 public void testTypeFromIdEnum() throws Exception {
 String id = EnumSet.class.getName() + "<java.lang.String,?" + Enum.class.getName() + ">";
 JavaType type = resolver.typeFromId(mapper.getDataSource(), id);
 assertEquals(EnumSet.class, type.getRawClass());
 }

 @Test
 public void testTypeFromIdCollection() throws Exception {
 String id = ArrayList.class.getName() + "<java.lang.String>";
 JavaType type = resolver.typeFromId(mapper.getDataSource(), id);
 assertEquals(ArrayList.class, type.getRawClass());
 }

 @Test
 public void testTypeFromIdCollectionEnumMap() throws Exception {
 String id = EnumMap.class.getName() + "<java.lang.String,?" + Enum.class.getName() + ">";
 JavaType type = resolver.typeFromId(mapper.getDataSource(), id);
 assertEquals(EnumMap.class, type.getRawClass());
 }

 @Test
 public void testTypeFromIdArray() throws Exception {
 String id = "java.lang.Object[]";
 JavaType type = resolver.typeFromId(mapper.getDataSource(), id);
 assertEquals(Object[].class, type.getRawClass());
 }

 @Test
 public void testTypeFromIdArrayEnum() throws Exception {
 String id = "java.lang.Enum[]";
 JavaType type = resolver.typeFromId(mapper.getDataSource(), id);
 assertEquals(Enum[].class, type.getRawClass());
 }

 @Test
 public void testTypeFromIdEnumWithHandlers() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(mapper.getTypeFactory().constructType(Object.class), mapper.getTypeFactory()) {
 @Override
 public JavaType typeFromId(DatabindContext context, String id) throws IOException {
 if (id.equals("com.test.MyEnum")) {
 return mapper.getTypeFactory().constructType(MyEnum.class);
 }
 return super.typeFromId(context, id);
 }
 };
 String id = "com.test.MyEnum";
 JavaType type = resolver.typeFromId(mapper.getDataSource(), id);
 assertEquals(MyEnum.class, type.getRawClass());
 }
}

class MyEnum {
 @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.EXISTING_PROPERTY, property="@class")
 public enum MyEnum {
 VALUE1;
}
}