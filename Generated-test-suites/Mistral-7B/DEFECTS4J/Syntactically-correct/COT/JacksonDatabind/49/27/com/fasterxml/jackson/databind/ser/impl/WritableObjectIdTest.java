package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdObjectIdGenerator;
import com.fasterxml.jackson.databind.ser.std.ObjectIdWriter;

/**
 * JUnit test file for WritableObjectId class
 */
public class WritableObjectIdTest {

 private ObjectIdGenerator<Object> idGenerator;
 private ObjectMapper mapper;
 private JsonGenerator gen;
 private SerializerProvider provider;
 private TypeSerializer typeSerializer;
 private ObjectIdWriter objectIdWriter;

 @Before
 public void setUp() throws Exception {
 idGenerator = new StdObjectIdGenerator();
 mapper = new ObjectMapper();
 gen = Mockito.mock(JsonGenerator.class);
 provider = Mockito.mock(SerializerProvider.class);
 typeSerializer = Mockito.mock(TypeSerializer.class);
 objectIdWriter = new ObjectIdWriter(null, null, null, null, typeSerializer);
 }

 @Test
 public void testConstructor() {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 assertNotNull(writableObjectId);
 }

 @Test
 public void testWriteAsId_NullGenerator() {
 try {
 new WritableObjectId(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testWriteAsId_NullId() {
 try {
 new WritableObjectId(idGenerator).writeAsId(gen, provider, objectIdWriter);
 fail("Expected IOException");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test
 public void testWriteAsId_NullJsonGenerator() {
 try {
 new WritableObjectId(idGenerator).writeAsId(null, provider, objectIdWriter);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testWriteAsId_NullSerializerProvider() {
 try {
 new WritableObjectId(idGenerator).writeAsId(gen, null, objectIdWriter);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testWriteAsId_NullObjectIdWriter() {
 try {
 new WritableObjectId(idGenerator).writeAsId(gen, provider, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testWriteAsId_NullObject() {
 Object obj = null;
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 assertFalse(writableObjectId.writeAsId(gen, provider, objectIdWriter));
 }

 @Test
 public void testWriteAsId_IdAlreadyWritten() {
 Object obj = new Object() {{}};
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.writeAsId(gen, provider, objectIdWriter);
 writableObjectId.idWritten = true;
 assertFalse(writableObjectId.writeAsId(gen, provider, objectIdWriter));
 }

 @Test
 public void testWriteAsId_AlwaysAsId() {
 Object obj = new Object() {{}};
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 Object id = idGenerator.generateId(obj);
 assertTrue(writableObjectId.writeAsId(gen, provider, objectIdWriter));
 assertEquals(id.toString(), gen.getCurrentValueAsString());
 }

 @Test
 public void testGenerateId_NullObject() {
 try {
 new WritableObjectId(idGenerator).generateId(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testGenerateId_NullId() {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 assertNull(writableObjectId.id);
 }

 @Test
 public void testWriteAsField_NullJsonGenerator() {
 try {
 new WritableObjectId(idGenerator).writeAsField(null, provider, objectIdWriter);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testWriteAsField_NullSerializerProvider() {
 try {
 new WritableObjectId(idGenerator).writeAsField(gen, null, objectIdWriter);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testWriteAsField_NullObjectIdWriter() {
 try {
 new WritableObjectId(idGenerator).writeAsField(gen, provider, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testWriteAsField() throws IOException {
 Object obj = new Object() {{}};
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 String fieldName = "testField";
 Object id = idGenerator.generateId(obj);

 // Mock writeObjectId method to write the id as a string
 Mockito.when(gen.writeObjectId(Mockito.anyString())).then(invocation -> invocation.getArgument(0));
 Mockito.when(gen.writeString(Mockito.anyString())).then(invocation -> {
 String value = (String) invocation.getArgument(0);
 gen.reset();
 gen.writeStartObject();
 gen.writeFieldName("id");
 gen.writeString(value);
 gen.writeEndObject();
 return null;
 });

 // Test writeAsField method
 new WritableObjectId(idGenerator).writeAsField(gen, provider, objectIdWriter);
 ObjectIdWriter mockObjectIdWriter = Mockito.spy(objectIdWriter);
 mockObjectIdWriter.propertyName = new SerializableString(fieldName);
 new WritableObjectId(idGenerator).writeAsField(gen, provider, mockObjectIdWriter);

 // Verify that the writeAsField method correctly writes the id as a field with the given name
 String expectedJson = String.format("{\"id\":\"%s\"}", id.toString());
 String actualJson = mapper.writeValueAsString(obj);
 assertEquals(expectedJson, actualJson);
 }
}