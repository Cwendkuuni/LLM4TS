package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdIntgenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializerProvider.SerializerProviderFactory;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.type.ClassIdType;

/**
 * Test class for WritableObjectId.java
 */
public class WritableObjectIdTest {

 private ObjectMapper mapper;
 private ObjectIdGenerator<Object> idGenerator;
 private ObjectIdWriter idWriter;
 private WritableObjectId writableObjectId;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 idGenerator = new IdIntgenerator();
 idWriter = new ObjectIdWriter(mapper.getSerializerProvider());
 }

 @Test
 public void testWritableObjectIdConstruction() {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 assertNotNull(writableObjectId);
 assertEquals(idGenerator, writableObjectId.generator);
 }

 @Test
 public void testWriteAsId() throws JsonProcessingException, IOException {
 Object object = new Object();
 String expectedId = "1";
 idGenerator.generateId(object);
 writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.id = idGenerator.generateId(object);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);
 assertTrue(writableObjectId.writeAsId(gen, mapper.getSerializerProvider(), idWriter));
 assertEquals(expectedId, idWriter.propertyName.getValueAsString());
 }

 @Test
 public void testGenerateId() throws JsonProcessingException {
 Object object = new Object();
 Object expectedId = idGenerator.generateId(object);
 writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.id = writableObjectId.generateId(object);
 assertEquals(expectedId, writableObjectId.id);
 }

 @Test
 public void testWriteAsField() throws JsonProcessingException, IOException {
 Object object = new Object();
 String expectedId = "1";
 idGenerator.generateId(object);
 writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.id = idGenerator.generateId(object);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);
 idWriter.propertyName = new SerializableString("_id");
 idWriter.alwaysAsId = false;
 idWriter.serializer = new ToStringSerializer(Object.class);
 idWriter.serializer.setNullValueSerializer(new ToStringSerializer(Object.class));
 idWriter.serializer.setNullValueProperty("null");
 writableObjectId.writeAsField(gen, mapper.getSerializerProvider(), idWriter);
 assertEquals(expectedId, idWriter.propertyName.getValueAsString());
 }

 @Test
 public void testWriteAsIdWithAlwaysAsId() throws JsonProcessingException, IOException {
 Object object = new Object();
 String expectedId = "1";
 idGenerator.generateId(object);
 writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.id = idGenerator.generateId(object);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);
 idWriter.propertyName = new SerializableString("_id");
 idWriter.alwaysAsId = true;
 idWriter.serializer = new ToStringSerializer(Object.class);
 idWriter.serializer.setNullValueSerializer(new ToStringSerializer(Object.class));
 idWriter.serializer.setNullValueProperty("null");
 assertTrue(writableObjectId.writeAsId(gen, mapper.getSerializerProvider(), idWriter));
 gen.flush();
 // Verify that the id was written to the stream
 ObjectReader reader = mapper.readerFor(ClassIdType.construct(Object.class, "_id"));
 Object readObject = reader.readValueAs(Object.class, gen);
 assertEquals(expectedId, readObject);
 }

 @Test
 public void testWriteAsIdWithNativeObjectId() throws JsonProcessingException, IOException {
 Object object = new Object();
 idGenerator.generateId(object);
 writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.id = idGenerator.generateId(object);
 JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);
 assertTrue(writableObjectId.writeAsId(gen, mapper.getSerializerProvider(), idWriter));
 gen.flush();
 // Verify that the id was written as a native Object Id
 ObjectReader reader = mapper.readerFor(ClassIdType.construct(Object.class, null));
 Object readObject = reader.readValueAs(Object.class, gen);
 assertNull(readObject);
 ObjectIdReader objectIdReader = mapper.readerFor(ObjectId.class);
 ObjectId id = objectIdReader.readValue(ObjectId.class, gen);
 assertEquals(idGenerator.generateId(object), id.getId());
 }
}