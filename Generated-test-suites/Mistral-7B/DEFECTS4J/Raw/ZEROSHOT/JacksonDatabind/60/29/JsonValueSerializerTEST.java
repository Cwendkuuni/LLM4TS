package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer.ValueTypeSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.ser.BeanPropertySerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.visitor.JsonContractProperties;

public class JsonValueSerializerTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testSerializeObjectWithJsonValueAnnotation() throws IOException, JsonMappingException {
 MyClass myClass = new MyClass();
 myClass.setValue(42);

 AnnotatedMethod method = mock(AnnotatedMethod.class);
 when(method.getName()).thenReturn("getValue");
 when(method.getDeclaringClass()).thenReturn(MyClass.class);
 when(method.getType()).thenReturn(Integer.TYPE);

 JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);

 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(method, valueSerializer);
 BeanProperty property = mock(BeanProperty.class);
 JsonSerializer<?> ser = jsonValueSerializer.createContextual(objectMapper.getSerializerProvider(), property);

 ObjectWriter writer = objectMapper.writer().withDefaultPrettyPrinter();
 String json = writer.writeValueAsString(myClass);

 assertEquals("42", json);
 }

 @Test
 public void testSerializeObjectWithJsonValueAnnotationAndCustomSerializer() throws IOException, JsonMappingException {
 MyClass myClass = new MyClass();
 myClass.setValue(new CustomClass());

 AnnotatedMethod method = mock(AnnotatedMethod.class);
 when(method.getName()).thenReturn("getValue");
 when(method.getDeclaringClass()).thenReturn(MyClass.class);
 when(method.getType()).thenReturn(CustomClass.class);

 JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
 when(valueSerializer.serializeValuesUsing(any(TypeSerializer.class))).thenReturn(true);

 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(method, valueSerializer);
 BeanProperty property = mock(BeanProperty.class);
 JsonSerializer<?> ser = jsonValueSerializer.createContextual(objectMapper.getSerializerProvider(), property);

 ObjectWriter writer = objectMapper.writer().withDefaultPrettyPrinter();
 String json = writer.writeValueAsString(myClass);

 assertEquals("{}", json); // Custom classes are not serialized by default
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeObjectWithJsonValueAnnotationAndNullValue() throws IOException, JsonMappingException {
 MyClass myClass = new MyClass();

 AnnotatedMethod method = mock(AnnotatedMethod.class);
 when(method.getName()).thenReturn("getValue");
 when(method.getDeclaringClass()).thenReturn(MyClass.class);
 when(method.getType()).thenReturn(Integer.TYPE);

 JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
 when(valueSerializer.serializeValuesUsing(any(TypeSerializer.class))).thenReturn(true);
 when(valueSerializer.serialize(any(Object.class), any(JsonGenerator.class), any(SerializerProvider.class)))
 .thenThrow(new IOException("Simulated error"));

 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(method, valueSerializer);
 BeanProperty property = mock(BeanProperty.class);
 JsonSerializer<?> ser = jsonValueSerializer.createContextual(objectMapper.getSerializerProvider(), property);

 MyClass object = new MyClass();
 ObjectWriter writer = objectMapper.writer().withDefaultPrettyPrinter();
 String json = writer.writeValueAsString(object);

 assertEquals("null", json);
 }

 @Test
 public void testSerializeObjectWithJsonValueAnnotationAndNaturalType() throws IOException, JsonMappingException {
 MyClass myClass = new MyClass();
 myClass.setValue(true);

 AnnotatedMethod method = mock(AnnotatedMethod.class);
 when(method.getName()).thenReturn("getValue");
 when(method.getDeclaringClass()).thenReturn(MyClass.class);
 when(method.getType()).thenReturn(Boolean.TYPE);

 JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
 when(valueSerializer.isEnabled(anyInt()))
 .thenReturn(false);
 when(valueSerializer.serializeValuesUsing(any(TypeSerializer.class))).thenReturn(true);
 when(valueSerializer.serialize(any(Object.class), any(JsonGenerator.class), any(SerializerProvider.class)))
 .thenReturn(null);

 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(method, valueSerializer);
 BeanProperty property = mock(BeanProperty.class);
 JsonSerializer<?> ser = jsonValueSerializer.createContextual(objectMapper.getSerializerProvider(), property);

 ObjectWriter writer = objectMapper.writer().withDefaultPrettyPrinter();
 String json = writer.writeValueAsString(myClass);

 assertEquals("true", json);
 }

 @Test
 public void testGetSchema() throws JsonMappingException {
 MyClass myClass = new MyClass();
 myClass.setValue(42);

 AnnotatedMethod method = mock(AnnotatedMethod.class);
 when(method.getName()).thenReturn("getValue");
 when(method.getDeclaringClass()).thenReturn(MyClass.class);
 when(method.getType()).thenReturn(Integer.TYPE);

 JsonSerializer<Object> valueSerializer = mock(JsonSerializer.class);
 when(valueSerializer.getSchema(any(SerializerProvider.class), any(Type.class)))
 .thenReturn(mock(JsonSchema.StdSchema.class));

 JsonValueSerializer jsonValueSerializer = new JsonValueSerializer(method, valueSerializer);
 JavaType typeHint = TypeFactory.defaultInstance().constructType(MyClass.class);
 JsonNode schema = jsonValueSerializer.getSchema(objectMapper.getSerializerProvider(), typeHint);

 assertEquals("{\"type\":\"number\",\"items\":{\"type\":\"integer\"}}", schema.asText());
 }
}

class MyClass {
 @JsonValue
 public int getValue() {
 return value;
 }

 public void setValue(int value) {
 this.value = value;
 }

 private int value;
}

class CustomClass {

}