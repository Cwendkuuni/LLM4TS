package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.ObjectIdWriter;

/**
 * Test class for WritableObjectId
 */
public class WritableObjectIdTest {

 private ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator() {
 @Override
 public Object generateId(Object bean) {
 return bean.hashCode();
 }
 };

 @Test
 public void testWritableObjectIdConstructor() {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 assertEquals("generator should not be null", idGenerator, writableObjectId.generator);
 }

 @Test
 public void testWriteAsId() throws IOException {
 Object id = "testId";
 SerializerProvider mockProvider = new SerializerProvider() {};
 JsonGenerator mockJsonGenerator = new ObjectMapper().getFactory().createGenerator(null);
 ObjectIdWriter mockObjectIdWriter = new ObjectIdWriter(null, null, null);
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

 // Test when id is null
 assertFalse(writableObjectId.writeAsId(mockJsonGenerator, mockProvider, mockObjectIdWriter));

 // Test when idWritten is true or alwaysAsId is true
 id = new Object();
 writableObjectId.id = id;
 assertTrue(writableObjectId.writeAsId(mockJsonGenerator, mockProvider, mockObjectIdWriter));

 // Test when canWriteObjectId is true
 mockJsonGenerator.setObjectIdEnabled(true);
 assertTrue(writableObjectId.writeAsId(mockJsonGenerator, mockProvider, mockObjectIdWriter));

 // Test when canWriteObjectId is false and propertyName is null
 mockJsonGenerator.setObjectIdEnabled(false);
 assertFalse(writableObjectId.writeAsId(mockJsonGenerator, mockProvider, mockObjectIdWriter));

 // Test when canWriteObjectId is false and propertyName is not null
 SerializableString propertyName = new SerializableString("testPropertyName");
 mockObjectIdWriter.propertyName = propertyName;
 assertFalse(writableObjectId.writeAsId(mockJsonGenerator, mockProvider, mockObjectIdWriter));
 }

 @Test
 public void testGenerateId() {
 Object id = new Object();
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 Object newId = writableObjectId.generateId(id);
 assertEquals("Generated id should match input", id.hashCode(), newId.hashCode());
 }

 @Test(expected = IOException.class)
 public void testGenerateIdWithException() throws IOException {
 Object id = new Object();
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 ObjectMapper objectMapper = new ObjectMapper();
 SerializerProvider mockProvider = new SerializerProvider() {
 @Override
 public void serializeValue(SerializerSerializer serializer, Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 throw new IOException("IOException in serializeValue method");
 }
 };
 Object newId = writableObjectId.generateId(id);
 fail("generateId should throw IOException when SerializerProvider's serializeValue method throws it");
 }

 @Test
 public void testWriteAsField() throws IOException {
 Object id = new Object();
 SerializerProvider mockProvider = new SerializerProvider() {};
 JsonGenerator mockJsonGenerator = new ObjectMapper().getFactory().createGenerator(null);
 ObjectIdWriter mockObjectIdWriter = new ObjectIdWriter(null, null, null);
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

 // Test when id is null
 assertFalse(writableObjectId.writeAsField(mockJsonGenerator, mockProvider, mockObjectIdWriter));

 // Test when id is not null and canWriteObjectId is true
 mockJsonGenerator.setObjectIdEnabled(true);
 // Need to assume String(ified) ids, for now...
 mockJsonGenerator.writeObjectId(String.valueOf(id));
 assertTrue(writableObjectId.writeAsField(mockJsonGenerator, mockProvider, mockObjectIdWriter));

 // Test when id is not null and canWriteObjectId is false and propertyName is null
 mockJsonGenerator.setObjectIdEnabled(false);
 assertFalse(writableObjectId.writeAsField(mockJsonGenerator, mockProvider, mockObjectIdWriter));

 // Test when id is not null and canWriteObjectId is false and propertyName is not null
 SerializableString propertyName = new SerializableString("testPropertyName");
 mockObjectIdWriter.propertyName = propertyName;
 mockJsonGenerator.writeFieldName(propertyName);
 // Need to assume String(ified) ids, for now...
 mockJsonGenerator.writeString(String.valueOf(id));
 assertTrue(writableObjectId.writeAsField(mockJsonGenerator, mockProvider, mockObjectIdWriter));
 }
}