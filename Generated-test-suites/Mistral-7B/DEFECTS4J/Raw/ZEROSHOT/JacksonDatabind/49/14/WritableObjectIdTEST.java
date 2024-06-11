package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProvider.BaseSerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

/**
 * Tests for WritableObjectId class
 */
public class WritableObjectIdTest {

 private ObjectMapper objectMapper;
 private ObjectIdGenerator<Object> idGenerator;
 private JsonGenerator jsonGenerator;
 private SerializerProvider serializerProvider;
 private Object id;
 private WritableObjectId writableObjectId;

 @Before
 public void setUp() throws Exception {
 ObjectIdGenerator.Static generator = new ObjectIdGenerator.Static() {
 @Override
 public Object generateId(Object bean) {
 id = System.currentTimeMillis();
 return id;
 }
 };
 idGenerator = new ObjectIdGenerator<Object>() {
 @Override
 public ObjectIdGenerator<Object> getInstance(ObjectIdGenerator<?> generator) {
 return generator;
 }

 @Override
 public ObjectGenerator<?> getBaseGenerator(AnnotationMetadata metadata) {
 return generator;
 }

 @Override
 public Object generateId(Object bean) {
 return generator.generateId(bean);
 }
 };

 objectMapper = new ObjectMapper();
 jsonGenerator = objectMapper.getFactory().createGenerator();
 serializerProvider = new BaseSerializerProvider() {
 @Override
 public SerializableString findValueSerializer(Class<?> type) {
 return new StdSerializer<String>(String.class) {
 @Override
 public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
 gen.writeString(value);
 }
 };
 }
};
 }

 @Test
 public void testGenerateId() throws Exception {
 Object pojo = new Object();
 writableObjectId = new WritableObjectId(idGenerator);
 Object id = writableObjectId.generateId(pojo);
 assertNotNull("Id should not be null", id);
 assertEquals("Generated ids should be equal", id, writableObjectId.id);
 }

 @Test
 public void testWriteAsId() throws Exception {
 Object pojo = new Object();
 writableObjectId = new WritableObjectId(idGenerator);
 AnnotatedClass ac = new AnnotatedClass(Object.class);
 ObjectIdWriter writer = new ObjectIdWriter() {
 @Override
 public SerializerProvider serializerProvider() {
 return serializerProvider;
 }

 @Override
 public AnnotatedMember propertyName() {
 return ac.findMember("id");
 }
 };
 jsonGenerator.setObjectIdGenerator(idGenerator);
 writableObjectId.writeAsId(jsonGenerator, serializerProvider, writer);
 assertTrue("Id should have been written as ObjectId", jsonGenerator.canWriteObjectId());
 jsonGenerator.flush();
 ObjectMapper mapper = new ObjectMapper();
 Object readPojo = mapper.readValue(jsonGenerator.getOutputStream(), Object.class);
 assertNotNull("Deserialized pojo should not be null", readPojo);
 assertEquals("Generated ids should be equal", id, readPojo.getClass().getDeclaredField("id").get(readPojo));
 }

 @Test
 public void testWriteAsField() throws Exception {
 Object pojo = new Object() {{
 id = 1L;
 }};
 writableObjectId = new WritableObjectId(idGenerator);
 AnnotatedClass ac = new AnnotatedClass(Object.class);
 ObjectIdWriter writer = new ObjectIdWriter() {
 @Override
 public SerializerProvider serializerProvider() {
 return serializerProvider;
 }

 @Override
 public AnnotatedMember propertyName() {
 return ac.findMember("id");
 }

 @Override
 public String getName() {
 return "id";
 }

 @Override
 public NameTransformer getPropertyNameTransformer() {
 return new NameTransformer() {
 @Override
 public String transform(String name, JsonSerializer serializer) {
 return name;
 }
 };
 }
 };
 jsonGenerator.setObjectIdGenerator(idGenerator);
 jsonGenerator.writeStartObject();
 jsonGenerator.writeFieldName("id");
 jsonGenerator.writeObjectId(String.valueOf(id));
 jsonGenerator.writeEndObject();
 jsonGenerator.flush();
 ObjectMapper mapper = new ObjectMapper();
 Object readPojo = mapper.readValue(jsonGenerator.getOutputStream(), Object.class);
 assertNotNull("Deserialized pojo should not be null", readPojo);
 assertEquals("Generated ids should be equal", id, readPojo.getClass().getDeclaredField("id").get(readPojo));
 }

 @Test(expected = JsonGenerationException.class)
 public void testWriteAsIdWithNullGenerator() throws Exception {
 Object pojo = new Object();
 WritableObjectId writableObjectId = new WritableObjectId(null);
 AnnotatedClass ac = new AnnotatedClass(Object.class);
 ObjectIdWriter writer = new ObjectIdWriter() {
 @Override
 public SerializerProvider serializerProvider() {
 return serializerProvider;
 }

 @Override
 public AnnotatedMember propertyName() {
 return ac.findMember("id");
 }
 };
 jsonGenerator.setObjectIdGenerator(null);
 try {
 writableObjectId.writeAsId(jsonGenerator, serializerProvider, writer);
 fail("Expected JsonGenerationException to be thrown");
 } catch (JsonGenerationException e) {
 // Expected
 }
 }

 @Test(expected = JsonProcessingException.class)
 public void testWriteAsIdWithNullID() throws Exception {
 Object pojo = new Object();
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 AnnotatedClass ac = new AnnotatedClass(Object.class);
 ObjectIdWriter writer = new ObjectIdWriter() {
 @Override
 public SerializerProvider serializerProvider() {
 return serializerProvider;
 }

 @Override
 public AnnotatedMember propertyName() {
 return ac.findMember("id");
 }
 };
 jsonGenerator.setObjectIdGenerator(idGenerator);
 try {
 writableObjectId.writeAsId(jsonGenerator, serializerProvider, writer);
 writableObjectId.id = null;
 writableObjectId.writeAsId(jsonGenerator, serializerProvider, writer);
 fail("Expected JsonProcessingException to be thrown");
 } catch (JsonProcessingException e) {
 // Expected
 }
 }
}