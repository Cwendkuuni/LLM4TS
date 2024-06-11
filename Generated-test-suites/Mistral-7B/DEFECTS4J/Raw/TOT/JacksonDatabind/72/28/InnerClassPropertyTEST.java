package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class InnerClassPropertyTest {

 private ObjectMapper objectMapper;
 private InnerClassProperty innerClassProperty;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 try {
 Class<?> innerClass = new Class<InnerClassTest>() {}.getDeclaredInnerClass("InnerClass");
 AnnotatedClass annotatedClass = objectMapper.getTypeFactory()
 .constructType(InnerClassTest.class);
 AnnotatedConstructor annotatedConstructor = annotatedClass.getDeclaredConstructors()[0];
 innerClassProperty = new InnerClassProperty(null, annotatedConstructor);
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testDeserializeAndSet() throws IOException {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("innerClass", "someValue");
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser(jsonNode.getRawValue());
 Object instance = new InnerClassTest();
 innerClassProperty.deserializeAndSet(jsonParser, null, instance);
 assertEquals("someValue", instance.innerClass.getValue());
 }

 @Test
 public void testDeserializeSetAndReturn() throws IOException {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("innerClass", "someValue");
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser(jsonNode.getRawValue());
 Object instance = new InnerClassTest();
 Object result = innerClassProperty.deserializeSetAndReturn(jsonParser, null, instance);
 assertEquals("someValue", result);
 assertEquals("someValue", instance.innerClass.getValue());
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeAndSetNullValue() throws IOException {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser(jsonNode.getRawValue());
 Object instance = new InnerClassTest();
 innerClassProperty.deserializeAndSet(jsonParser, null, instance);
 assertNull(instance.innerClass);
 }

 @Test(expected = JsonProcessingException.class)
 public void testDeserializeAndSetWithTypeDeserializer() throws IOException {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("innerClass", "someValue");
 ObjectNode typeNode = objectMapper.createObjectNode();
 typeNode.put("class", "com.fasterxml.jackson.databind.deser.impl.InnerClassTest$InnerClass$1");
 jsonNode.set("$type", typeNode);
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser(jsonNode.getRawValue());
 Object instance = new InnerClassTest();
 innerClassProperty.deserializeAndSet(jsonParser, null, instance);
 assertNotNull(instance.innerClass);
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetWithInvalidType() throws IOException {
 ObjectNode jsonNode = objectMapper.createObjectNode();
 jsonNode.put("innerClass", "someValue");
 ObjectNode typeNode = objectMapper.createObjectNode();
 typeNode.put("class", "com.fasterxml.jackson.databind.DeserializationProblem");
 jsonNode.set("$type", typeNode);
 JsonParser jsonParser = objectMapper.getFactory()
 .createParser(jsonNode.getRawValue());
 Object instance = new InnerClassTest();
 try {
 innerClassProperty.deserializeAndSet(jsonParser, null, instance);
 fail();
 } catch (JsonMappingException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadResolve() throws IOException {
 Object deserializedInstance = objectMapper.readValue(new String("{\"innerClass\":{\"class\":\"com.fasterxml.jackson.databind.deser.impl.InnerClassProperty\"," +
 "\"_creator\":{\"class\":\"java.lang.reflect.Constructor\",\"_annotations\":[{\"class\":\"com.fasterxml.jackson.annotation.JsonCreator\"}]," +
 "\"_annotations\":[{\"class\":\"com.fasterxml.jackson.annotation.JsonProperty\"}]}}"), InnerClassTest.class);
 Object readResolveInstance = deserializedInstance.getClass().cast(deserializedInstance).readResolve();
 assertNotSame(deserializedInstance, readResolveInstance);
 assertEquals(deserializedInstance.getClass().getDeclaredField("innerClassProperty"), readResolveInstance.getClass().getDeclaredField("innerClassProperty"));
}

 @Test
 public void testWriteReplace() throws IOException {
 Object instance = new InnerClassTest();
 Object replaceInstance = instance.getClass().cast(instance).writeReplace();
 assertNotSame(instance, replaceInstance);
 assertNotNull(replaceInstance.getClass().getDeclaredField("innerClassProperty"));
}
}

class InnerClassTest {
 private InnerClass innerClass;

 public InnerClass getInnerClass() {
 return innerClass;
 }

 public static class InnerClass {
 private String value;

 public InnerClass(String value) {
 this.value = value;
 }

 public String getValue() {
 return value;
 }
}

 private static class InnerClassTest_InnerClass {
 public InnerClassTest$InnerClass() {
 }
}

 static class InnerClassTest_InnerClass$1 extends InnerClass {
 public InnerClassTest$InnerClass$1() {
 super("InnerClass1");
 }
}
}