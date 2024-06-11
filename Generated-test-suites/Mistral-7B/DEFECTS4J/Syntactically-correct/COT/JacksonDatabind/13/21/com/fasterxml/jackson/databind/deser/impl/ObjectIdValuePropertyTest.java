package com.example.objectidvaluepropertytest;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

 private ObjectIdReader objectIdReader;
 private ObjectIdValueProperty objectIdValueProperty;

 @Before
 public void setUp() throws Exception {
 ObjectIdReader.Std objectIdReaderStd = new ObjectIdReader.Std(new PropertyName("id"), Long.class);
 AnnotatedMember propertyMember = AnnotationIntrospector.findAnnotated(this, "objectIdValueProperty");
 BeanPropertyDefinition propertyDefinition = BeanPropertyDefinition.construct(propertyMember);
 objectIdReader = new ObjectIdReader(propertyDefinition, null);
 objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 }

 @Test
 public void testConstructor() {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 assertEquals(propertyName("id"), objectIdValueProperty.getName());
 assertEquals(Long.class, objectIdValueProperty.getType());
 assertNull(objectIdValueProperty.getValueDeserializer());
 assertNull(objectIdValueProperty._objectIdReader);
 }

 @Test
 public void testDeprecatedConstructor() {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdValueProperty, new PropertyName("newName"));
 assertEquals(propertyName("id"), objectIdValueProperty.getName());
 assertEquals(Long.class, objectIdValueProperty.getType());
 assertNotNull(objectIdValueProperty._objectIdReader);
 assertEquals(objectIdReader, objectIdValueProperty._objectIdReader);
 }

 @Test
 public void testWithName() {
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withName(new PropertyName("newName"));
 assertEquals(propertyName("newName"), newObjectIdValueProperty.getName());
 assertEquals(Long.class, newObjectIdValueProperty.getType());
 assertSame(objectIdReader, newObjectIdValueProperty._objectIdReader);
 }

 @Test
 public void testWithValueDeserializer() {
 JsonDeserializer<Object> jsonDeserializer = new StdSerializer<Object>(Object.class) {};
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdValueProperty newObjectIdValueProperty = objectIdValueProperty.withValueDeserializer(jsonDeserializer);
 assertEquals(jsonDeserializer, newObjectIdValueProperty.getValueDeserializer());
 assertSame(objectIdReader, newObjectIdValueProperty._objectIdReader);
 }

 @Test
 public void testGetAnnotation() throws Exception {
 assertNull(objectIdValueProperty.getAnnotation(Annotation.class));
 }

 @Test
 public void testGetMember() {
 assertNull(objectIdValueProperty.getMember());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testSetNullId() throws Exception {
 Object obj = new Object();
 objectIdValueProperty.set(obj, null);
 fail("should throw UnsupportedOperationException");
 }

 @Test
 public void testSetId() throws Exception {
 Object obj = new Object();
 Object id = new Long(1L);
 objectIdValueProperty.set(obj, id);
 ObjectIdReader.Std objectIdReaderStd = (ObjectIdReader.Std) objectIdReader;
 assertEquals(id, objectIdReaderStd.generator.getId(obj));
 }

 @Test
 public void testDeserializeAndSet() throws Exception {
 ObjectParser parseOnlyParser = new ObjectMapper().parserForObjectId(Object.class);
 JsonParser jsonParser = parseOnlyParser.getParser();
 Object obj = new Object();
 jsonParser.nextValue(); // consume [null]
 jsonParser.nextValue(); // consume tag [START_ARRAY]
 jsonParser.nextValue(); // consume "id"
 jsonParser.nextValue(); // consume null
 jsonParser.nextValue(); // consume end of array
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 JsonDeserializer<Object> jsonDeserializer = new StdSerializer<Object>(Object.class) {};
 objectIdValueProperty.setValueDeserializer(jsonDeserializer);
 ObjectIdReader.Std objectIdReaderStd = (ObjectIdReader.Std) objectIdReader;
when(objectIdReaderStd.resolver.resolveId(any(JsonParser.class), anyInt(), anyInt(), any(), any()))
 .thenReturn(new ObjectId(1L));
 when(objectIdReaderStd.generator.getId(eq(obj)))
 .thenReturn(1L);
 Object id = new Long(1L);
 Object result = objectIdValueProperty.deserializeAndSet(jsonParser, new TestContext(), obj);
 assertEquals(id, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetNullId() throws Exception {
 ObjectParser parseOnlyParser = new ObjectMapper().parserForObjectId(Object.class);
 JsonParser jsonParser = parseOnlyParser.getParser();
 Object obj = new Object();
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdReader.Std objectIdReaderStd = (ObjectIdReader.Std) objectIdReader;
 when(objectIdReaderStd.resolver.resolveId(any(JsonParser.class), anyInt(), anyInt(), any(), any()))
 .thenReturn(null);
 jsonParser.nextValue(); // consume [null]
 jsonParser.nextValue(); // consume tag [START_ARRAY]
 jsonParser.nextValue(); // consume "id"
 jsonParser.nextValue(); // consume null
 jsonParser.nextValue(); // consume end of array
 try {
 Object result = objectIdValueProperty.deserializeAndSet(jsonParser, new TestContext(), obj);
 fail("should throw IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test(expected = IOException.class)
 public void testDeserializeAndSetTypeMismatch() throws Exception {
 ObjectParser parseOnlyParser = new ObjectMapper().parserForObjectId(Object.class);
 JsonParser jsonParser = parseOnlyParser.getParser();
 Object obj = new Object();
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdReader.Std objectIdReaderStd = (ObjectIdReader.Std) objectIdReader;
 when(objectIdReaderStd.resolver.resolveId(any(JsonParser.class), anyInt(), anyInt(), any(), any()))
 .thenReturn(new ObjectId(1L));
 when(objectIdReaderStd.generator.getId(eq(obj)))
 .thenReturn(1L);
 jsonParser.nextValue(); // consume [null]
 jsonParser.nextValue(); // consume tag [START_ARRAY]
 jsonParser.nextValue(); // consume "id"
 jsonParser.nextValue(); // consume "string"
 jsonParser.nextValue(); // consume end of array
 try {
 Object result = objectIdValueProperty.deserializeAndSet(jsonParser, new TestContext(), obj);
 fail("should throw IOException");
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testDeserializeSetAndReturn() throws Exception {
 ObjectParser parseOnlyParser = new ObjectMapper().parserForObjectId(Object.class);
 JsonParser jsonParser = parseOnlyParser.getParser();
 Object obj = new Object();
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 JsonDeserializer<Object> jsonDeserializer = new StdSerializer<Object>(Object.class) {};
 objectIdValueProperty.setValueDeserializer(jsonDeserializer);
 ObjectIdReader.Std objectIdReaderStd = (ObjectIdReader.Std) objectIdReader;
 when(objectIdReaderStd.resolver.resolveId(any(JsonParser.class), anyInt(), anyInt(), any(), any()))
 .thenReturn(new ObjectId(1L));
 when(objectIdReaderStd.generator.getId(eq(obj)))
 .thenReturn(1L);
 Object id = new Long(1L);
 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, new TestContext(), obj);
 assertEquals(id, result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeSetAndReturnNullId() throws Exception {
 ObjectParser parseOnlyParser = new ObjectMapper().parserForObjectId(Object.class);
 JsonParser jsonParser = parseOnlyParser.getParser();
 Object obj = new Object();
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 ObjectIdReader.Std objectIdReaderStd = (ObjectIdReader.Std) objectIdReader;
 when(objectIdReaderStd.resolver.resolveId(any(JsonParser.class), anyInt(), anyInt(), any(), any()))
 .thenReturn(null);
 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, new TestContext(), obj);
 fail("should throw IOException");
 }

 @Test(expected = IOException.class)
 public void testDeserializeSetAndReturnTypeMismatch() throws Exception {
 ObjectParser parseOnlyParser = new ObjectMapper().parserForObjectId(Object.class);
 JsonParser jsonParser = parseOnlyParser.getParser();
 Object obj = new Object();
 ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
 JsonDeserializer<Object> jsonDeserializer = new StdSerializer<Object>(Object.class) {};
 objectIdValueProperty.setValueDeserializer(jsonDeserializer);
 ObjectIdReader.Std objectIdReaderStd = (ObjectIdReader.Std) objectIdReader;
 when(objectIdReaderStd.resolver.resolveId(any(JsonParser.class), anyInt(), anyInt(), any(), any()))
 .thenReturn(new ObjectId(1L));
 when(objectIdReaderStd.generator.getId(eq(obj)))
 .thenReturn(1L);
 jsonParser.nextValue(); // consume [null]
 jsonParser.nextValue(); // consume tag [START_ARRAY]
 jsonParser.nextValue(); // consume "id"
 jsonParser.nextValue(); // consume "string"
 jsonParser.nextValue(); // consume end of array
 try {
 Object result = objectIdValueProperty.deserializeSetAndReturn(jsonParser, new TestContext(), obj);
 fail("should throw IOException");
 } catch (IOException e) {
 // expected
 }
 }
}