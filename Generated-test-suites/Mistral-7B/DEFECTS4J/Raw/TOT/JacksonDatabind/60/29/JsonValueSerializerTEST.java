package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonValueSerializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testBasic() throws JsonMappingException, JsonProcessingException {
 Object input = new MyClass("Test");
 JsonValueSerializer serializer = mapper.findValueSerializer(input.getClass(), null);
 assertEquals("Test", serializer.getValue(input));

 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(input, gen, mapper.getSerializerProvider());
 assertEquals("\"Test\"", gen.getOutputText());
 }

 @Test
 public void testNullInput() throws JsonMappingException, JsonProcessingException {
 Object input = null;
 JsonValueSerializer serializer = mapper.findValueSerializer(input.getClass(), null);
 assertNull(serializer.getValue(input));

 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(input, gen, mapper.getSerializerProvider());
 mapper.getFactory().writeValueAsString(gen.getOutputStream(), null);
 }

 @Test
 public void testEnum() throws JsonMappingException, JsonProcessingException {
 EnumTest myEnum = EnumTest.RED;
 JsonValueSerializer serializer = mapper.findValueSerializer(myEnum.getClass(), null);
 assertEquals("RED", serializer.getValue(myEnum));

 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializer.serialize(myEnum, gen, mapper.getSerializerProvider());
 assertEquals("\"RED\"", gen.getOutputText());
 }

 @Test(expected = JsonMappingException.class)
 public void testException() throws JsonMappingException, JsonProcessingException {
 Object input = new MyClassWithException();
 JsonValueSerializer serializer = mapper.findValueSerializer(input.getClass(), null);
 assertThrows(JsonMappingException.class, () -> serializer.getValue(input));
 }

 @Test
 public void testTypeSerialization() throws JsonMappingException, JsonProcessingException {
 Object input = new MyClassWithType();
 JsonValueSerializer serializer = mapper.findValueSerializer(input.getClass(), null);
 TypeSerializer typeSerializer = mapper.getSerializerProvider().findTypeSerializer(input.getClass(), null);

 JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
 serializer.serializeWithType(input, gen, mapper.getSerializerProvider(), typeSerializer);
 assertNotNull(gen.getOutputText());
 }
}

class MyClass {
 @JsonValue
 public String getValue() {
 return "Test";
 }
}

enum EnumTest { RED, GREEN, BLUE }

class MyClassWithException {
 @JsonValue
 public void getValue() throws Exception {
 throw new RuntimeException("Test");
 }
}

class MyClassWithType {
 @JsonValue
 public int getValue() {
 return 42;
 }
}