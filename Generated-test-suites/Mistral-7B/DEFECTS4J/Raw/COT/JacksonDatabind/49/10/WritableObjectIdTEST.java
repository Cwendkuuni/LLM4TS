package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdGeneratorTest;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProvider.BaseSerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;

/**
 * Test class for WritableObjectId.
 */
public class WritableObjectIdTest {

 private ObjectIdGenerator<?> generator;
 private JsonGenerator gen;
 private SerializerProvider provider;
 private ObjectIdWriter writer;
 private TypeSerializer typeSerializer;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 generator = new ObjectIdGenerator() {
 @Override
 public SerializableString idFromValueAndType(Object idValue, Class<?> idType) {
 return new SerializableString("testId");
 }
 };
 writer = new ObjectIdWriter(null, null, null);
 typeSerializer = new StubTypeSerializer();
 gen = new BaseJsonGenerator();
 provider = new BaseSerializerProvider();
 provider.setTypeSerializer(typeSerializer);
 }

 @Test
 public void testWritableObjectIdConstructor() {
 WritableObjectId writableObjectId = new WritableObjectId(generator);
 assertEquals(generator, writableObjectId.generator);
 assertNull(writableObjectId.id);
 assertFalse(writableObjectId.idWritten);
 }

 @Test
 public void testWriteAsId() throws IOException {
 Object id = generator.generateId(new Object());
 WritableObjectId writableObjectId = new WritableObjectId(generator);
 assertTrue(writableObjectId.writeAsId(gen, provider, writer));
 assertTrue(writer.writtenAsId);
 if (gen.canWriteObjectId()) {
 gen.flush(); // Verify Object Id written as reference
 assertEquals("testId", gen.getOutputBuffer().toString());
 } else {
 assertEquals("testId", writer.serializedId);
 }
 }

 @Test(expected = IOException.class)
 public void testWriteAsIdWithNullIdGenerator() throws IOException {
 WritableObjectId writableObjectId = new WritableObjectId(null);
 writableObjectId.writeAsId(gen, provider, writer);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testWriteAsIdWithNullJsonGenerator() throws IOException {
 WritableObjectId writableObjectId = new WritableObjectId(generator);
 writableObjectId.writeAsId(null, provider, writer);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testWriteAsIdWithNullSerializerProvider() throws IOException {
 WritableObjectId writableObjectId = new WritableObjectId(generator);
 writableObjectId.writeAsId(gen, null, writer);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testWriteAsIdWithNullObjectIdWriter() throws IOException {
 WritableObjectId writableObjectId = new WritableObjectId(generator);
 writableObjectId.writeAsId(gen, provider, null);
 fail("Expected IOException");
 }

 @Test
 public void testGenerateId() {
 Object result = writableObjectId.generateId(new Object());
 assertNotNull(result);
 assertEquals("testId", result.toString());
 }

 @Test
 public void testWriteAsField() throws IOException {
 Object id = generator.generateId(new Object());
 WritableObjectId writableObjectId = new WritableObjectId(generator);
 writableObjectId.writeAsField(gen, provider, writer);
 assertTrue(writableObjectId.idWritten);
 if (gen.canWriteObjectId()) {
 gen.flush(); // Verify Object Id written as field
 assertEquals("testId", gen.getOutputBuffer().toString());
 } else {
 assertEquals("testId", writer.serializedId);
 }
 }

 @Test(expected = IOException.class)
 public void testWriteAsFieldWithNullJsonGenerator() throws IOException {
 WritableObjectId writableObjectId = new WritableObjectId(generator);
 writableObjectId.writeAsField(null, provider, writer);
 fail("Expected IOException");
 }
}

 class StubTypeSerializer implements TypeSerializer {
 @Override
 public void writeTypePrefixForScalar(JsonGenerator gen, TypeSerializer contentSerializer, TypeSerializer valueSerializer) throws IOException {
 // empty
 }

 @Override
 public void writeTypePrefixForArray(JsonGenerator gen, TypeSerializer contentSerializer) throws IOException {
 // empty
 }

 @Override
 public void writeTypePrefixForValue(JsonGenerator gen, TypeSerializer contentSerializer) throws IOException {
 // empty
 }

 @Override
 public void writeTypeSuffixForArray(JsonGenerator gen, TypeSerializer contentSerializer) throws IOException {
 // empty
 }

 @Override
 public void writeTypeSuffixForScalar(JsonGenerator gen, TypeSerializer contentSerializer) throws IOException {
 // empty
 }

 @Override
 public void writeTypeSuffixForValue(JsonGenerator gen, TypeSerializer contentSerializer) throws IOException {
 // empty
 }

 @Override
 public void writeEmptyArray(JsonGenerator gen, TypeSerializer contentTypeSerializer) throws IOException {
 // empty
 }

 @Override
 public void writeArrayElement(JsonGenerator gen, TypeSerializer elementSerializer, Object value) throws IOException {
 // empty
 }

 @Override
 public void writeValue(JsonGenerator gen, TypeSerializer vfs, Object value) throws IOException {
 // empty
 }
}