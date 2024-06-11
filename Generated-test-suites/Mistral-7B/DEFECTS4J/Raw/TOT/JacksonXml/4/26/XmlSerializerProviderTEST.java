package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Objects;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonRootName;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertySerializerBase;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

 private XmlSerializerProvider xmlSerializerProvider;
 private DefaultSerializerProvider defaultSerializerProvider;
 private SerializationConfig config;
 private SerializerFactory serializerFactory;
 private XmlRootNameLookup rootNameLookup;

 @Before
 public void setUp() {
 config = new SerializationConfig();
 serializerFactory = new SimpleModule().addSerializer(String.class, new StringSerializer())
 .build();
 rootNameLookup = new XmlRootNameLookup();
 xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
 defaultSerializerProvider = new DefaultSerializerProvider();
 }

 @Test
 public void testSerializeNull() throws IOException {
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
 when(gen.isEnabled(SerializationFeature.WRITE_NULL_JSON_AS_NULL)).thenReturn(false);

 xmlSerializerProvider.serializeValue(gen, null);

 TokenBuffer tokenBuffer = (TokenBuffer) gen;
 assertEquals(TokenType.START_OBJECT, tokenBuffer.getCurrentToken().getClass());
 assertEquals("null", tokenBuffer.getCurrentName());
 assertEquals(TokenType.VALUE_NULL, tokenBuffer.nextToken());
 assertEquals(TokenType.END_OBJECT, tokenBuffer.nextToken().getClass());
 }

 @Test
 public void testSerializeNullRootName() throws IOException {
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
 when(gen.isEnabled(SerializationFeature.WRITE_NULL_JSON_AS_NULL)).thenReturn(false);
 when(gen.getRootName()).thenReturn(null);

 QName rootName = XmlSerializerProvider.ROOT_NAME_FOR_NULL;
 xmlSerializerProvider._initWithRootName((ToXmlGenerator) gen, rootName);
 xmlSerializerProvider.serializeValue(gen, null);

 TokenBuffer tokenBuffer = (TokenBuffer) gen;
 assertEquals(TokenType.START_OBJECT, tokenBuffer.getCurrentToken().getClass());
 assertEquals(rootName.getLocalPart(), tokenBuffer.getCurrentName());
 assertEquals(TokenType.VALUE_NULL, tokenBuffer.nextToken());
 assertEquals(TokenType.END_OBJECT, tokenBuffer.nextToken().getClass());
 }

 @Test
 public void testSerializeString() throws IOException {
 String str = "test";
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
 when(gen.getRootName()).thenReturn(null);
 when(gen.writeString(str)).thenReturn(str);
 when(gen.isEnabled(SerializationFeature.WRITE_NULL_JSON_AS_NULL)).thenReturn(false);

 JsonSerializer<String> stringSerializer = new SimpleBeanPropertySerializerBase<String>(String.class, null);
 stringSerializer.setSerializerProvider(defaultSerializerProvider);
 when(xmlSerializerProvider.findValueSerializer(String.class, null, stringSerializer)).thenReturn(stringSerializer);

 xmlSerializerProvider.serializeValue(gen, str);

 TokenBuffer tokenBuffer = (TokenBuffer) gen;
 assertEquals(TokenType.START_OBJECT, tokenBuffer.getCurrentToken().getClass());
 assertEquals(null, tokenBuffer.getCurrentName());
 assertEquals(TokenType.FIELD_NAME, tokenBuffer.nextToken().getClass());
 assertEquals("", tokenBuffer.getCurrentName());
 assertEquals(TokenType.VALUE_STRING, tokenBuffer.nextToken().getClass());
 assertEquals(str, tokenBuffer.getCurrentValueAsString());
 assertEquals(TokenType.END_OBJECT, tokenBuffer.nextToken().getClass());
 }

 @Test
 public void testSerializeArrayOfStrings() throws IOException {
 String[] strings = new String[]{"test1", "test2", "test3"};
 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
 when(gen.getRootName()).thenReturn(null);
 when(gen.writeStartArray()).thenReturn(null);
 when(gen.writeValue(strings[0])).thenReturn(strings[0]);
 when(gen.writeValue(strings[1])).thenReturn(strings[1]);
 when(gen.writeValue(strings[2])).thenReturn(strings[2]);
 when(gen.writeEndArray()).thenReturn(null);
 when(gen.isEnabled(SerializationFeature.WRITE_NULL_JSON_AS_NULL)).thenReturn(false);

 JsonSerializer<String[]> stringArraySerializer = new StringArraySerializer();
 stringArraySerializer.setSerializerProvider(defaultSerializerProvider);
 when(xmlSerializerProvider.findValueSerializer(String[].class, null, stringArraySerializer)).thenReturn(stringArraySerializer);

 xmlSerializerProvider.serializeValue(gen, strings);

 TokenBuffer tokenBuffer = (TokenBuffer) gen;
 assertEquals(TokenType.START_OBJECT, tokenBuffer.getCurrentToken().getClass());
 assertEquals(null, tokenBuffer.getCurrentName());
 assertEquals(TokenType.FIELD_NAME, tokenBuffer.nextToken().getClass());
 assertEquals("", tokenBuffer.getCurrentName());
 assertEquals(TokenType.START_ARRAY, tokenBuffer.nextToken().getClass());
 for (int i = 0; i < strings.length; i++) {
 assertEquals(TokenType.VALUE_STRING, tokenBuffer.nextToken().getClass());
 assertEquals(strings[i], tokenBuffer.getCurrentValueAsString());
 }
 assertEquals(TokenType.END_ARRAY, tokenBuffer.nextToken().getClass());
 assertEquals(TokenType.END_OBJECT, tokenBuffer.nextToken().getClass());
 }

 @Test
 public void testSerializeObjectWithRootName() throws IOException {
 @JsonRootName("test")
 static class TestObject {
 String name;
 }

 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
 when(gen.getRootName()).thenReturn(new QName("test:root"));
 when(gen.writeStartObject()).thenReturn(null);
 when(gen.writeFieldName("name")).thenReturn(null);
 when(gen.writeValue("test")).thenReturn("test");
 when(gen.writeEndObject()).thenReturn(null);
 when(gen.isEnabled(SerializationFeature.WRITE_NULL_JSON_AS_NULL)).thenReturn(false);

 JsonSerializer<TestObject> testObjectSerializer = new TestObjectSerializer();
 testObjectSerializer.setSerializerProvider(defaultSerializerProvider);
 when(xmlSerializerProvider.findValueSerializer(TestObject.class, null, testObjectSerializer)).thenReturn(testObjectSerializer);

 TestObject testObject = new TestObject();
 testObject.name = "test";
 xmlSerializerProvider.serializeValue(gen, testObject);

 TokenBuffer tokenBuffer = (TokenBuffer) gen;
 assertEquals(TokenType.START_OBJECT, tokenBuffer.getCurrentToken().getClass());
 assertEquals("test:root", tokenBuffer.getCurrentName());
 assertEquals(TokenType.FIELD_NAME, tokenBuffer.nextToken().getClass());
 assertEquals("name", tokenBuffer.getCurrentName());
 assertEquals(TokenType.VALUE_STRING, tokenBuffer.nextToken().getClass());
 assertEquals("test", tokenBuffer.getCurrentValueAsString());
 assertEquals(TokenType.END_OBJECT, tokenBuffer.nextToken().getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeObjectWithNullRootName() throws IOException {
 @JsonRootName("test")
 static class TestObject {
 String name;
 }

 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
 when(gen.getRootName()).thenReturn(null);
 when(gen.writeStartObject()).thenReturn(null);
 when(gen.writeFieldName("name")).thenReturn(null);
 when(gen.writeValue("test")).thenReturn("test");
 when(gen.writeEndObject()).thenReturn(null);
 when(gen.isEnabled(SerializationFeature.WRITE_NULL_JSON_AS_NULL)).thenReturn(false);

 JsonSerializer<TestObject> testObjectSerializer = new TestObjectSerializer();
 testObjectSerializer.setSerializerProvider(defaultSerializerProvider);
 when(xmlSerializerProvider.findValueSerializer(TestObject.class, null, testObjectSerializer)).thenReturn(testObjectSerializer);

 TestObject testObject = new TestObject();
 testObject.name = "test";
 xmlSerializerProvider.serializeValue(gen, testObject);
 }

 @Test
 public void testSerializeObjectWithRootNameFromConfig() throws IOException {
 @JsonRootName("test")
 static class TestObject {
 String name;
 }

 JsonGenerator gen = mock(JsonGenerator.class);
 when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
 when(gen.getRootName()).thenReturn(null);
 when(gen.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(false);

 JsonSerializer<TestObject> testObjectSerializer = new TestObjectSerializer();
 testObjectSerializer.setSerializerProvider(defaultSerializerProvider);
 when(xmlSerializerProvider.findValueSerializer(TestObject.class, null, testObjectSerializer)).thenReturn(testObjectSerializer);

 TestObject testObject = new TestObject();
 config.setRootValueSeparator("");
 config.setRootName("test");
 xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup, config, serializerFactory);
 xmlSerializerProvider.serializeValue(gen, testObject);

 TokenBuffer tokenBuffer = (TokenBuffer) gen;
 assertEquals(TokenType.START_OBJECT, tokenBuffer.getCurrentToken().getClass());
 assertEquals("test", tokenBuffer.getCurrentName());
 assertEquals(TokenType.FIELD_NAME, tokenBuffer.nextToken().getClass());
 assertEquals("name", tokenBuffer.getCurrentName());
 assertEquals(TokenType.VALUE_STRING, tokenBuffer.nextToken().getClass());
 assertEquals("test", tokenBuffer.getCurrentValueAsString());
 assertEquals(TokenType.END_OBJECT, tokenBuffer.nextToken().getClass());
 }
}

static class StringSerializer extends JsonSerializer<String> {
 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeString(value);
 }
}

static class StringArraySerializer extends JsonSerializer<String[]> {
 @Override
 public void serialize(String[] values, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeStartArray();
 for (String value : values) {
 gen.writeValue(value);
 }
 gen.writeEndArray();
 }
}

static class TestObjectSerializer extends JsonSerializer<TestObject> {
 @Override
 public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeFieldName("name");
 gen.writeValue(value.name);
 }
}