package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIntGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Test class for WritableObjectId
 */
public class WritableObjectIdTest {

 private static final ObjectIdGenerator<Object> idGenerator = new IdIntGenerator();

 @Test
 public void testWritableObjectIdConstruction() {
 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 assertNotNull(writableObjectId);
 }

 @Test
 public void testWriteAsId() throws JsonProcessingException, IOException {
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = new StandardSerializerProvider();
 ObjectIdWriter w = new ObjectIdWriter() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 fail("Should not call this method in test");
 }

 @Override
 public SerializableString propertyName() {
 return new SerializableString("testPropertyName");
 }

 @Override
 public boolean alwaysAsId() {
 return false;
 }
 };

 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.generateId(new Object());
 writableObjectId.writeAsId(gen, provider, w);

 String actual = gen.getOutputContent();
 String expected = "\"" + "testPropertyName" + "\":";

 assertEquals(expected, actual);
 }

 @Test
 public void testWriteAsIdWithAlwaysAsIdTrue() throws JsonProcessingException, IOException {
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = new StandardSerializerProvider();
 ObjectIdWriter w = new ObjectIdWriter() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 fail("Should not call this method in test");
 }

 @Override
 public SerializableString propertyName() {
 return null;
 }

 @Override
 public boolean alwaysAsId() {
 return true;
 }
 };

 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.generateId(new Object());
 writableObjectId.writeAsId(gen, provider, w);

 assertTrue(gen.canWriteObjectId());
 gen.writeObjectId(String.valueOf(writableObjectId.id));
 }

 @Test(expected = JsonMappingException.class)
 public void testWriteAsIdWhenIdIsNull() throws JsonProcessingException, IOException {
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = new StandardSerializerProvider();
 ObjectIdWriter w = new ObjectIdWriter() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 fail("Should not call this method in test");
 }

 @Override
 public SerializableString propertyName() {
 return new SerializableString("testPropertyName");
 }

 @Override
 public boolean alwaysAsId() {
 return false;
 }
 };

 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.writeAsId(gen, provider, w);
 }

 @Test
 public void testGenerateId() {
 Object id = WritableObjectId.this.generateId(new Object());
 assertNotNull(id);
 }

 @Test
 public void testGenerateIdTwiceForSameObject() {
 Object id1 = WritableObjectId.this.generateId(new Object());
 Object id2 = WritableObjectId.this.generateId(new Object());
 assertSame(id1, id2);
 }

 @Test
 public void testWriteAsField() throws JsonProcessingException, IOException {
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = new StandardSerializerProvider();
 ObjectIdWriter w = new ObjectIdWriter() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 fail("Should not call this method in test");
 }

 @Override
 public SerializableString propertyName() {
 return new SerializableString("testPropertyName");
 }
 };

 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.generateId(new Object());
 writableObjectId.writeAsField(gen, provider, w);

 String actual = gen.getOutputContent();
 String expected = "\"" + "testPropertyName" + "\":";

 assertEquals(expected, actual);
 }

 @Test
 public void testWriteAsFieldWithNullPropertyName() throws JsonProcessingException, IOException {
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = new StandardSerializerProvider();
 ObjectIdWriter w = new ObjectIdWriter() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 fail("Should not call this method in test");
 }

 @Override
 public SerializableString propertyName() {
 return null;
 }

 @Override
 public boolean alwaysAsId() {
 return false;
 }
 };

 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.generateId(new Object());
 writableObjectId.writeAsField(gen, provider, w);

 assertFalse(gen.canWriteObjectId());
 }

 @Test
 public void testWriteAsFieldWithAlwaysAsIdTrue() throws JsonProcessingException, IOException {
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = new StandardSerializerProvider();
 ObjectIdWriter w = new ObjectIdWriter() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 fail("Should not call this method in test");
 }

 @Override
 public SerializableString propertyName() {
 return null;
 }

 @Override
 public boolean alwaysAsId() {
 return true;
 }
 };

 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.generateId(new Object());
 writableObjectId.writeAsField(gen, provider, w);

 assertTrue(gen.canWriteObjectId());
 gen.writeObjectId(String.valueOf(writableObjectId.id));
 }

 @Test
 public void testIdWrittenFlag() throws JsonProcessingException, IOException {
 JsonGenerator gen = createJsonGenerator();
 SerializerProvider provider = new StandardSerializerProvider();
 ObjectIdWriter w = new ObjectIdWriter() {
 @Override
 public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
 fail("Should not call this method in test");
 }

 @Override
 public SerializableString propertyName() {
 return null;
 }

 @Override
 public boolean alwaysAsId() {
 return false;
 }
 };

 WritableObjectId writableObjectId = new WritableObjectId(idGenerator);
 writableObjectId.generateId(new Object());
 writableObjectId.writeAsId(gen, provider, w);
 writableObjectId.writeAsField(gen, provider, w);

 assertTrue(writableObjectId.idWritten);
 }

 private JsonGenerator createJsonGenerator() throws IOException {
 JsonMapper mapper = new JsonMapper();
 return mapper.getFactory().createGenerator();
 }

 private static class StandardSerializerProvider extends SerializerProvider {
 @Override
 public void serializeValue(Serializer serializer, JsonGenerator gen, Object value) throws IOException {
 fail("Should not call this method in test");
 }
 }

 private static class ObjectIdWriter {
 private String propertyName;
 private StdSerializer<Object> serializer;
 private boolean alwaysAsId;

 public String propertyName() {
 return propertyName;
 }

 public void setPropertyName(String propertyName) {
 this.propertyName = propertyName;
 }

 public boolean alwaysAsId() {
 return alwaysAsId;
 }

 public void setAlwaysAsId(boolean alwaysAsId) {
 this.alwaysAsId = alwaysAsId;
 }

 public StdSerializer<Object> getSerializer() {
 return serializer;
 }

 public void setSerializer(StdSerializer<Object> serializer) {
 this.serializer = serializer;
 }
 }
}