package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdGeneratorTest;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider.SerializerProviderImpl;

public class WritableObjectIdTest {

 private ObjectIdGenerator<?> generator;
 private WritableObjectId writableObjectId;
 private JsonGenerator jsonGenerator;
 private SerializerProvider serializerProvider;
 private ObjectIdWriter objectIdWriter;

 @Before
 public void setUp() throws Exception {
 generator = Mockito.spy(ObjectIdGeneratorTest.SimpleObjectIdGenerator.class);
 jsonGenerator = Mockito.mock(JsonGenerator.class);
 serializerProvider = new SerializerProviderImpl();
 objectIdWriter = new ObjectIdWriter(jsonGenerator, serializerProvider);
 writableObjectId = new WritableObjectId(generator);
 }

 @Test
 public void testWriteAsIdWithNullGenerator() {
 writableObjectId = new WritableObjectId(null);
 assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
 }

 @Test
 public void testWriteAsIdWithNullId() {
 id = null;
 assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
 }

 @Test
 public void testWriteAsIdWithIdWrittenAndAlwaysAsIdFalse() throws IOException {
 id = new Long(1L);
 writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
 assertFalse(idWritten);
 }

 @Test
 public void testWriteAsIdWithIdWrittenAndAlwaysAsIdTrue() throws IOException {
 id = new Long(1L);
 objectIdWriter.alwaysAsId = true;
 assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
 assertTrue(idWritten);
 }

 @Test
 public void testGenerateIdWithNullForPojo() {
 id = writableObjectId.generateId(null);
 assertNotNull(id);
 }

 @Test(expected = NullPointerException.class)
 public void testGenerateIdWithNullGenerator() {
 writableObjectId = new WritableObjectId(null);
 writableObjectId.generateId(new Object());
 }

 @Test
 public void testWriteAsFieldWithNullGenerator() {
 writableObjectId = new WritableObjectId(null);
 writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
 }

 @Test(expected = NullPointerException.class)
 public void testWriteAsFieldWithNullJsonGenerator() {
 writableObjectId = new WritableObjectId(generator);
 writableObjectId.writeAsField(null, serializerProvider, objectIdWriter);
 }

 @Test(expected = NullPointerException.class)
 public void testWriteAsFieldWithNullSerializerProvider() {
 writableObjectId = new WritableObjectId(generator);
 writableObjectId.writeAsField(jsonGenerator, null, objectIdWriter);
 }

 @Test(expected = NullPointerException.class)
 public void testWriteAsFieldWithNullObjectIdWriter() {
 writableObjectId = new WritableObjectId(generator);
 writableObjectId.writeAsField(jsonGenerator, serializerProvider, null);
 }

 @Test
 public void testWriteAsFieldWithNullPropertyName() throws IOException {
 SerializableString name = null;
 writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
 assertTrue(idWritten);
 }

 @Test
 public void testWriteAsFieldWithStringPropertyName() throws IOException {
 SerializableString name = new SerializableString("name");
 writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
 assertTrue(idWritten);
 }

 @Test
 public void testWriteAsFieldWithLongId() throws IOException {
 id = new Long(1L);
 writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
 String idString = jsonGenerator.getOutputFinisher().getEncodedBuffer().toString();
 assertTrue(idString.startsWith("\"id\":"));
 }

 @Test
 public void testWriteAsFieldWithIntegerId() throws IOException {
 id = new Integer(1);
 writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
 String idString = jsonGenerator.getOutputFinisher().getEncodedBuffer().toString();
 assertTrue(idString.startsWith("\"id\":"));
 }

}