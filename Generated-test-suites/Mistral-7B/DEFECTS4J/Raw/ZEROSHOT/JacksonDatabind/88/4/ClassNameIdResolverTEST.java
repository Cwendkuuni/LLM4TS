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

 private TypeIdResolver _resolver;
 private ObjectMapper _mapper;

 @Before
 public void setup() throws Exception {
 ObjectCar car = new ObjectCar();
 _mapper = new ObjectMapper();
 _mapper.registerValueHandlerForMethods(ObjectCar.class);
 _mapper.registerTypeIdResolver(ObjectCar.class, new ClassNameIdResolver(_mapper.getTypeFactory().constructType(ObjectCar.class), _mapper.getTypeFactory()));
 _resolver = _mapper.getTypeFactory().constructValueType(TypeIdResolver.class, _mapper.getTypeFactory().constructType(ClassNameIdResolver.class));
 }

 @Test
 public void testIdFromValue() throws IOException {
 ObjectCar car = new ObjectCar();
 assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ObjectCar", _resolver.idFromValue(car));
 }

 @Test
 public void testIdFromValueAndType() throws IOException {
 String id = "com.fasterxml.jackson.databind.jsontype.impl.ObjectCar";
 JavaType javaType = _mapper.getTypeFactory().constructType(ObjectCar.class);
 assertEquals(javaType, _resolver.typeFromId(_mapper.getDeserializationContext(), id));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidTypeId() throws IOException {
 String id = "invalidTypeId";
 _resolver.typeFromId(_mapper.getDeserializationContext(), id);
 }

 @Test
 public void testEnumClass() throws IOException {
 List<Color> colors = Arrays.asList(Color.RED, Color.GREEN, Color.BLUE);
 assertEquals("java.util.ArrayList", _resolver.idFromValue(colors));
 }

 @Test
 public void testEnumMap() throws IOException {
 Map<Color, Integer> colorMap = new EnumMap<>(Color.class);
 colorMap.put(Color.RED, 1);
 colorMap.put(Color.GREEN, 2);
 colorMap.put(Color.BLUE, 3);
 assertEquals("java.util.EnumMap<com.fasterxml.jackson.databind.test.model.Color, java.lang.Integer>", _resolver.idFromValue(colorMap));
 }

 @Test
 public void testInnerClass() throws IOException {
 Class<?> outer = Object.class;
 Class<?> inner = OuterClass.InnerClass.class;
 assertEquals("java.lang.Object", _resolver.idFromValue(new OuterClass()));
 assertEquals("com.fasterxml.jackson.databind.jsontype.impl.OuterClass$InnerClass", _resolver.idFromValue(new OuterClass().new InnerClass()));
 }

 @Test
 public void testSubclassOfEnum() throws IOException {
 Class<?> subclass = SubclassOfEnum.class;
 assertEquals("com.fasterxml.jackson.databind.test.model.SubclassOfEnum", _resolver.idFromValue(new SubclassOfEnum()));
 }

 @Test
 public void testUnknownClassId() throws IOException {
 String id = "com.fasterxml.jackson.databind.test.model.UnknownClass";
 JavaType javaType = _mapper.getTypeFactory().constructType(UnknownClass.class);
 assertNotNull(javaType);
 assertEquals(javaType, _resolver.typeFromId(_mapper.getDeserializationContext(), id));
 }

 @Test
 public void testUnknownClassId2() throws IOException {
 String id = "com.fasterxml.jackson.databind.test.model.UnknownClass2";
 assertNull(_resolver.typeFromId(_mapper.getDeserializationContext(), id));
 }

 @Test
 public void testUnknownTypeId() throws IOException {
 Object obj = new Object();
 String id = _resolver.idFromValue(obj);
 assertNotNull(id);
 assertNull(_resolver.typeFromId(_mapper.getDeserializationContext(), id));
 }
}

class ObjectCar {
}

enum Color {
 RED, GREEN, BLUE
}

class OuterClass {
 static class InnerClass {
 }
}

class SubclassOfEnum extends Enum {
 @Override
 public String toString() {
 return "SubclassOfEnum";
 }
}

class UnknownClass {
}

class UnknownClass2 {
}