package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.DefaultObjectIdGenerator;
import com.fasterxml.jackson.databind.ser.ObjectIdWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * Test class for WritableObjectId
 */
public class WritableObjectIdTest {

 private ObjectIdGenerator<Object> generator;
 private ObjectIdWriter objectIdWriter;
 private SerializerProvider serializerProvider;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws Exception {
 generator = new DefaultObjectIdGenerator();
 objectIdWriter = new ObjectIdWriter(generator, null, null, null, null, null, null);
 serializerProvider = new SerializerProvider();
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testWritableObjectIdConstructor() {
 WritableObjectId writableObjectId = new WritableObjectId(generator);
 assertNotNull(writableObjectId);
 assertEquals(generator, writableObjectId.generator);
 }

 @Test
 public void testWriteAsId() throws IOException {
 Object id = new Object();
 WritableObjectId writableObjectId = new WritableObjectId(generator);
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out, serializerProvider);

 assertTrue(writableObjectId.writeAsId(gen, serializerProvider, objectIdWriter));
 gen.flush();
 System.out.flush();
 String json = new String(System.out.getBytes("iso-8859-1"), 0, (int) System.out.size());
 assertEquals("\"\",null", json.substring(json.lastIndexOf(":") + 1));

 id = null;
 writableObjectId.id = new Object();
 idWritten = true; // set idWritten before calling writeAsId
 assertTrue(writableObjectId.writeAsId(gen, serializerProvider, objectIdWriter));
 gen.flush();
 System.out.flush();
 json = new String(System.out.getBytes("iso-8859-1"), 0, (int) System.out.size());
 assertEquals("\"\".id", json.substring(json.lastIndexOf(":") + 1));

 idWritten = false; // reset idWritten
 assertFalse(writableObjectId.writeAsId(gen, serializerProvider, objectIdWriter));
 }

 @Test
 public void testGenerateId() {
 WritableObjectId writableObjectId = new WritableObjectId(generator);
 Object id = writableObjectId.generateId(new Object());
 assertNotNull(id);
 }

 @Test(expected = JsonMappingException.class)
 public void testGenerateIdWithNullArgument() {
 WritableObjectId writableObjectId = new WritableObjectId(generator);
 writableObjectId.generateId(null);
 }

 @Test
 public void testWriteAsField() throws IOException {
 Object id = new Object();
 WritableObjectId writableObjectId = new WritableObjectId(generator);
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out, serializerProvider);
 AnnotatedClass annotatedClass = objectMapper.getTypeFactory().constructType(Object.class);
 BeanPropertyWriter writer = objectMapper.getSerializationConfig().findPropertyWriter(annotatedClass, "id");

 assertEquals(false, writableObjectId.idWritten);
 writableObjectId.writeAsField(gen, serializerProvider, objectIdWriter);
 assertEquals(true, writableObjectId.idWritten);
 gen.flush();
 System.out.flush();
 String json = new String(System.out.getBytes("iso-8859-1"), 0, (int) System.out.size());
 assertEquals("\"id\"", json.substring(json.lastIndexOf(":") + 1));
 }

 @Test(expected = IOException.class)
 public void testWriteAsFieldWithNullJsonGenerator() {
 WritableObjectId writableObjectId = new WritableObjectId(generator);
 try {
 writableObjectId.writeAsField(null, serializerProvider, objectIdWriter);
 } catch (IOException e) {
 // Expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWriteAsFieldWithNullSerializerProvider() {
 WritableObjectId writableObjectId = new WritableObjectId(generator);
 try {
 writableObjectId.writeAsField(gen, null, objectIdWriter);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testWriteAsFieldWithNullObjectIdWriter() {
 WritableObjectId writableObjectId = new WritableObjectId(generator);
 try {
 writableObjectId.writeAsField(gen, serializerProvider, null);
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testWriteAsFieldWithEmptyPropertyName() throws IOException {
 Object id = new Object();
 WritableObjectId writableObjectId = new WritableObjectId(generator);
 JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out, serializerProvider);
 AnnotatedMethod method = objectMapper.getTypeFactory().constructMethodType(Void.TYPE, WritableObjectId.class, JsonGenerator.class, SerializerProvider.class, ObjectIdWriter.class);
 ObjectIdWriter emptyObjectIdWriter = new ObjectIdWriter(generator, null, null, null, null, null, method);

 assertEquals(false, writableObjectId.idWritten);
 writableObjectId.writeAsField(gen, serializerProvider, emptyObjectIdWriter);
 assertEquals(true, writableObjectId.idWritten);
 gen.flush();
 System.out.flush();
 String json = new String(System.out.getBytes("iso-8859-1"), 0, (int) System.out.size());
 assertFalse(json.contains(":"));
 }
}