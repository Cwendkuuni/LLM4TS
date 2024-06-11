package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

 private final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testIdFromValue() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(JsonType.StandardClassIdResolver.instance.findType(String.class), mapper.getTypeFactory());
 String id = resolver.idFromValue("hello");
 assertEquals("java.lang.String", id);

 id = resolver.idFromValue(new Enum("TEST"));
 assertEquals("java.lang.Enum", id);

 Class<?> clazz = ClassUtil.findEnumType(Enum.class);
 id = resolver.idFromValue(EnumSet.of(Enum.VAL1, Enum.VAL2));
 assertEquals(typeFromId(mapper, "java.util.EnumSet", EnumSet.class, clazz), id);

 id = resolver.idFromValue(new HashMap<String, String>());
 assertEquals("java.util.Map", id);
 }

 @Test
 public void testIdFromValueAndType() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(JsonType.StandardClassIdResolver.instance.findType(String.class), mapper.getTypeFactory());
 String id = resolver.idFromValueAndType("hello", String.class);
 assertEquals("java.lang.String", id);

 id = resolver.idFromValueAndType(new Enum("TEST"), Enum.class);
 assertEquals("java.lang.Enum", id);

 Class<?> clazz = ClassUtil.findEnumType(Enum.class);
 id = resolver.idFromValueAndType(EnumSet.of(Enum.VAL1, Enum.VAL2), EnumSet.class, clazz);
 assertEquals("java.util.EnumSet", id);

 id = resolver.idFromValueAndType(new HashMap<String, String>(), Map.class);
 assertEquals("java.util.HashMap", id);
 }

 @Test
 public void testTypeFromId() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(mapper.constructType(String.class), mapper.getTypeFactory());
 TypeIdResolver baseResolver = JsonType.StandardClassIdResolver.instance;
 DatabindContext ctxt = mapper.getDeserializationConfig().getContext();

 JavaType stringType = resolver.typeFromId(ctxt, "java.lang.String");
 assertNotNull(stringType);
 assertEquals(String.class, stringType.getRawClass());

 JavaType enumSetType = resolver.typeFromId(ctxt, "java.util.EnumSet");
 assertNotNull(enumSetType);
 assertEquals(EnumSet.class, enumSetType.getRawClass());

 JavaType enumMapType = resolver.typeFromId(ctxt, "java.util.EnumMap");
 assertNotNull(enumMapType);
 assertEquals(EnumMap.class, enumMapType.getRawClass());

 JavaType arrayListType = resolver.typeFromId(ctxt, "java.util.ArrayList");
 assertNotNull(arrayListType);
 assertEquals(ArrayList.class, arrayListType.getRawClass());

 JavaType outerClassType = resolver.typeFromId(ctxt, "com.example.OuterClass$InnerClass");
 assertNotNull(outerClassType);
 assertEquals(InnerClass.class, outerClassType.getRawClass());

 String id = resolver.idFromValueAndType("hello", String.class);
 JavaType type = resolver.typeFromId(ctxt, id);
 assertEquals(String.class, type.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypeId() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(mapper.constructType(String.class), mapper.getTypeFactory());
 TypeIdResolver baseResolver = JsonType.StandardClassIdResolver.instance;
 DatabindContext ctxt = mapper.getDeserializationConfig().getContext();

 String id = "invalidTypeId";
 resolver.typeFromId(ctxt, id);
 }

 @Test(expected = IOException.class)
 public void testClassNotFound() throws Exception {
 ClassNameIdResolver resolver = new ClassNameIdResolver(mapper.constructType(String.class), mapper.getTypeFactory());
 TypeIdResolver baseResolver = JsonType.StandardClassIdResolver.instance;
 DatabindContext ctxt = mapper.getDeserializationConfig().getContext();

 String id = "com.example.nonExistentClass";
 resolver.typeFromId(ctxt, id);
 }

 private JavaType typeFromId(ObjectMapper mapper, String id, Class<?> expectedType, Class<?>... argumentTypes) throws IOException {
 TypeFactory typeFactory = mapper.getTypeFactory();
 JavaType type = typeFromId(mapper, id, mapper.getDeserializationConfig().getContext());
 assertEquals(expectedType, type.getRawClass());
 if (argumentTypes != null) {
 for (Class<?> argType : argumentTypes) {
 assertEquals(argType, type.getRawTypeArguments().get(0));
 }
 }
 return type;
 }

 private JavaType typeFromId(ObjectMapper mapper, String id, DatabindContext context) throws IOException {
 return new ClassNameIdResolver(mapper.constructType(String.class), mapper.getTypeFactory()).typeFromId(context, id);
 }
}