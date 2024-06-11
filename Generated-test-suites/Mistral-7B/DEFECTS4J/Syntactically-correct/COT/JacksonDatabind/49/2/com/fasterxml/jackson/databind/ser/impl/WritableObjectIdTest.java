package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIntGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.ObjectIdWriter;

/**
 * Test class for WritableObjectId
 */
public class WritableObjectIdTest {

 private WritableObjectId writableObjectId;
 private ObjectIdWriter objectIdWriter;
 private SerializerProvider serializerProvider;
 private JsonGenerator jsonGenerator;
 private ObjectMapper objectMapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 ObjectIdGenerator<Object> idGenerator = new IdIntGenerator();
 writableObjectId = new WritableObjectId(idGenerator);
 objectIdWriter = new ObjectIdWriter(null, null, null, null, null);
 serializerProvider = new SerializerProvider() {
 @Override
 public void close() throws IOException {
 // do nothing
 }
 };
 jsonGenerator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), new ObjectMapper.DefaultSerializerProvider());
 objectMapper = new JsonMapper();
 }

 @Test
 public void testWriteAsIdWithNullGenerator() {
 try {
 new WritableObjectId(null);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // pass
 }
 }

 @Test
 public void testWriteAsIdWithoutIdAndWithoutAlwaysAsId() throws IOException {
 assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
 }

 @Test
 public void testWriteAsIdWithNullId() throws IOException {
 writableObjectId.id = null;
 assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
 }

 @Test
 public void testWriteAsIdWithIdAndWithoutAlwaysAsId() throws IOException {
 Object id = new Object();
 writableObjectId.id = id;
 assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));

 jsonGenerator.writeStartObject();
 writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
 jsonGenerator.writeEndObject();

 // Check that no Object Id was written
 assertEquals(0, jsonGenerator.getOutputSize());
 }

 @Test
 public void testWriteAsIdWithIdAndWithAlwaysAsId() throws IOException {
 Object id = new Object();
 writableObjectId.id = id;
 objectIdWriter.alwaysAsId = true;
 assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));

 jsonGenerator.writeStartObject();
 writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
 jsonGenerator.writeEndObject();

 // Check that Object Id was written
 String expectedId = String.valueOf(id.hashCode());
 String actualId = new String(jsonGenerator.getOutputBuffer().readAllBytes());
 assertTrue(actualId.startsWith(expectedId));
 }

 @Test
 public void testGenerateIdWithNullForPojo() {
 try {
 writableObjectId.generateId(null);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // pass
 }
 }

 @Test
 public void testGenerateIdWithoutId() {
 Object generatedId = writableObjectId.generateId(new Object());
 assertNotNull(generatedId);
 }

 @Test
 public void testWriteAsFieldWithNullGenerator() {
 try {
 writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
 fail("Should throw NullPointerException");
 } catch (NullPointerException e) {
 // pass
 }
 }

 @Test
 public void testWriteAsFieldWithNullId() throws IOException {
 writableObjectId.id = null;
 writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
 jsonGenerator.writeEndObject();

 // Check that no Object Id was written
 assertEquals(0, jsonGenerator.getOutputSize());
 }

 @Test
 public void testWriteAsFieldWithId() throws IOException {
 Object id = new Object();
 writableObjectId.id = id;
 writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
 jsonGenerator.writeEndObject();

 // Check that Object Id was written as field name value
 String expectedId = String.valueOf(id.hashCode());
 String actualId = jsonGenerator.getOutputBuffer().readLine();
 assertTrue(actualId.startsWith("\"_id\""));
 assertTrue(actualId.contains(expectedId));
 }

 @Test(expected = IOException.class)
 public void testWriteAsFieldWithNullPropertyName() throws IOException {
 Object id = new Object();
 writableObjectId.id = id;
 writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
 jsonGenerator.writeEndObject();

 // Check that no Object Id was written as field name
 assertEquals(0, jsonGenerator.getOutputSize());

 ObjectIdWriter nullWriter = new ObjectIdWriter(null, null, null, null, null);
 writableObjectId.writeAsField(jsonGenerator, serializerProvider, nullWriter);
 }
}