package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.ObjectIdGenerator.IdIntGenerator;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Tests for WritableObjectId class
 */
public class WritableObjectIdTest {

 private static class TestObjectIdGenerator extends ObjectIdGenerator<Object> {
  private int counter = 0;

  @Override
  public Object generateId(Object forPojo) {
    return counter++;
  }
 }

 @Test
 public void testWritableObjectIdConstructor() {
  ObjectIdGenerator<Object> generator = new TestObjectIdGenerator();
  WritableObjectId writableObjectId = new WritableObjectId(generator);
  assertTrue(writableObjectId instanceof WritableObjectId);
  assertEquals(generator, writableObjectId.generator);
 }

 @Test
 public void testWriteAsIdFalse() throws IOException {
  ObjectIdGenerator<Object> generator = new TestObjectIdGenerator();
  WritableObjectId writableObjectId = new WritableObjectId(generator);
  JsonGenerator gen = createMockGenerator();
  SerializerProvider provider = createMockProvider();
  ObjectIdWriter w = createMockObjectIdWriter();

  assertFalse(writableObjectId.writeAsId(gen, provider, w));
 }

 @Test
 public void testWriteAsIdTrue() throws IOException {
  ObjectIdGenerator<Object> generator = new TestObjectIdGenerator();
  WritableObjectId writableObjectId = new WritableObjectId(generator);
  JsonGenerator gen = createMockGenerator();
  SerializerProvider provider = createMockProvider();
  ObjectIdWriter w = createMockObjectIdWriter();
  Object id = generator.generateId(null);

  writableObjectId.id = id;
  w.alwaysAsId = true;

  assertTrue(writableObjectId.writeAsId(gen, provider, w));
 }

 @Test
 public void testGenerateId() throws IOException {
  ObjectIdGenerator<Object> generator = new TestObjectIdGenerator();
  WritableObjectId writableObjectId = new WritableObjectId(generator);
  Object forPojo = new Object();

  Object id = writableObjectId.generateId(forPojo);
  assertEquals(0, id.getClass().hashCode());
  assertEquals(Long.class, id.getClass());
 }

 @Test
 public void testWriteAsField() throws IOException {
  ObjectIdGenerator<Object> generator = new TestObjectIdGenerator();
  WritableObjectId writableObjectId = new WritableObjectId(generator);
  JsonGenerator gen = createMockGenerator();
  SerializerProvider provider = createMockProvider();
  ObjectIdWriter w = createMockObjectIdWriter();

  SerializableString name = new SerializableString("testName");
  w.propertyName = name;

  writableObjectId.id = 1L;
  writableObjectId.writeAsField(gen, provider, w);

  // Verify that the field name was written
  verify(gen).writeFieldName(name);
  // Verify that the id was written
  verify(w.serializer).serialize(writableObjectId.id, gen, provider);
 }

 @Test(expected = IOException.class)
 public void testWriteAsFieldNullGenerator() throws IOException {
  ObjectIdGenerator<Object> generator = null;
  WritableObjectId writableObjectId = new WritableObjectId(generator);
  JsonGenerator gen = createMockGenerator();
  SerializerProvider provider = createMockProvider();
  ObjectIdWriter w = createMockObjectIdWriter();

  writableObjectId.id = 1L;
  writableObjectId.writeAsField(gen, provider, w);
 }

 @Test
 public void testWriteAsIdNullGenerator() throws IOException {
  ObjectIdGenerator<Object> generator = null;
  WritableObjectId writableObjectId = new WritableObjectId(generator);
  JsonGenerator gen = createMockGenerator();
  SerializerProvider provider = createMockProvider();
  ObjectIdWriter w = createMockObjectIdWriter();

  writableObjectId.id = 1L;
  assertFalse(writableObjectId.writeAsId(gen, provider, w));
 }

 @Test
 public void testWriteAsIdNullId() throws IOException {
  ObjectIdGenerator<Object> generator = new TestObjectIdGenerator();
  WritableObjectId writableObjectId = new WritableObjectId(generator);
  JsonGenerator gen = createMockGenerator();
  SerializerProvider provider = createMockProvider();
  ObjectIdWriter w = createMockObjectIdWriter();

  assertFalse(writableObjectId.writeAsId(gen, provider, w));
 }

 @Test
 public void testWriteAsIdNullIdWriter() throws IOException {
  ObjectIdGenerator<Object> generator = new TestObjectIdGenerator();
  WritableObjectId writableObjectId = new WritableObjectId(generator);
  JsonGenerator gen = createMockGenerator();
  SerializerProvider provider = createMockProvider();
  ObjectIdWriter w = null;

  assertFalse(writableObjectId.writeAsId(gen, provider, w));
 }

 @Test
 public void testWriteAsIdNullIdOrWriter() throws IOException {
  ObjectIdGenerator<Object> generator = new TestObjectIdGenerator();
  WritableObjectId writableObjectId = new WritableObjectId(generator);
  JsonGenerator gen = createMockGenerator();
  SerializerProvider provider = createMockProvider();
  ObjectIdWriter w = createMockObjectIdWriter();

  assertFalse(writableObjectId.writeAsId(gen, provider, w));
  assertFalse(writableObjectId.writeAsId(gen, provider, null));
 }

 @Test
 public void testWriteAsIdNullWriterSerializer() throws IOException {
  ObjectIdGenerator<Object> generator = new TestObjectIdGenerator();
  WritableObjectId writableObjectId = new WritableObjectId(generator);
  JsonGenerator gen = createMockGenerator();
  SerializerProvider provider = createMockProvider();
  ObjectIdWriter w = createMockObjectIdWriter();
  StdSerializer<Object> serializer = null;

  assertFalse(writableObjectId.writeAsId(gen, provider, w));
 }

 private JsonGenerator createMockGenerator() {
  return mock(JsonGenerator.class);
 }

 private SerializerProvider createMockProvider() {
  return mock(SerializerProvider.class);
 }

 private ObjectIdWriter createMockObjectIdWriter() {
  return mock(ObjectIdWriter.class);
 }

}