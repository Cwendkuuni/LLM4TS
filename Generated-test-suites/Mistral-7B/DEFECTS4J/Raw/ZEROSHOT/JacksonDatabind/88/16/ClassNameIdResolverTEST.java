package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

 private ClassNameIdResolver resolver;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws Exception {
 ObjectFactory factory = new ObjectMapper().getFactory();
 TypeFactory typeFactory = factory.getTypeFactory();
 JavaType baseType = typeFactory.constructType(String.class);
 resolver = new ClassNameIdResolver(baseType, typeFactory);
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testIdFromValue() throws Exception {
 Object obj = new Object();
 String id = resolver.idFromValue(obj);
 assertEquals("java.lang.Object", id);

 Enum enumValue = Enum.values()[0];
 String enumId = resolver.idFromValue(enumValue);
 assertEquals("java.lang.Class", id.getClass().getName());
 assertEquals("java.lang.Enum", ClassUtil.getRawClass(enumId).getName());
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 Object obj = new Object();
 String id = resolver.idFromValueAndType(obj, Object.class);
 assertEquals("java.lang.Object", id);

 Enum enumValue = Enum.values()[0];
 String enumId = resolver.idFromValueAndType(enumValue, Enum.class);
 assertEquals("java.lang.Class", id.getClass().getName());
 assertEquals("java.lang.Enum", ClassUtil.getRawClass(enumId).getName());
 }

 @Test
 public void testTypeFromId() throws Exception {
 String id = "java.util.HashMap";
 JavaType javaType = resolver.typeFromId(objectMapper.getDeserializationContext(), id);
 assertEquals(Map.class, javaType.getRawClass());

 id = "java.util.ArrayList<java.lang.Integer>";
 JavaType javaTypeListInt = resolver.typeFromId(objectMapper.getDeserializationContext(), id);
 assertEquals(List.class.getName(), javaTypeListInt.getRawType().getCanonicalName());
 assertEquals(Integer.class, javaTypeListInt.getGenericTypes().get(0).getRawType().getCanonicalName());
 }
}