package com.example.test.jackson;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializerFactory;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.*;
import java.util.Map.Entry;

public class DefaultDeserializationContextTest {

 @Test
 public void testDefaultDeserializationContextConstructor() {
 DefaultDeserializationContext context = new DefaultDeserializationContext(new DeserializerFactory(), null);
 assertNull(context._objectIds);
 assertNull(context._objectIdResolvers);
 }

 @Test
 public void testDefaultDeserializationContextCopy() {
 // Given
 DefaultDeserializationContext context = new DefaultDeserializationContext(new DeserializerFactory(), null);

 // When
 DefaultDeserializationContext copiedContext = context.copy();

 // Then
 assertSame(context, copiedContext);
 }

 @Test(expected = IllegalStateException.class)
 public void testDefaultDeserializationContextCopyEmptySubclass() {
 // Given
 DefaultDeserializationContext context = new DefaultDeserializationContext(new DefaultDeserializationContext());

 // When, Then
 context.copy();
 }

 @Test
 public void testDefaultDeserializationContextWith() {
 // Given
 DefaultDeserializationContext context = new DefaultDeserializationContext(new DeserializerFactory(), null);
 ObjectMapper objectMapper = new ObjectMapper();
 DeserializerFactory newFactory = objectMapper.getDeserializerFactory();

 // When
 DefaultDeserializationContext newContext = context.with(newFactory);

 // Then
 assertSame(newFactory, newContext.getDeserializerFactory());
 }

 @Test
 public void testDefaultDeserializationContextCreateInstance() {
 // Given
 DefaultDeserializationContext context = new DefaultDeserializationContext(new DeserializerFactory(), null);
 DeserializationConfig config = new DeserializationConfig(new ObjectMapper());
 JsonParser jp = null;
 InjectableValues values = null;

 // When
 Impl createdContext = (Impl) context.createInstance(config, jp, values);

 // Then
 assertNotNull(createdContext._objectIds);
 assertNotNull(createdContext._objectIdResolvers);
 }

 @Test
 public void testFindObjectId() {
 // Given
 DefaultDeserializationContext context = new DefaultDeserializationContext(new DeserializerFactory(), null);
 ObjectIdGenerator<?> gen = new ObjectIdGenerator() {};
 ObjectIdResolver resolverType = new ObjectIdResolver() {};
 Object id = new Object();

 // When
 ReadableObjectId result = context.findObjectId(id, gen, resolverType);

 // Then
 assertNotNull(result);
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testCheckUnresolvedObjectId() {
 // Given
 DefaultDeserializationContext context = new DefaultDeserializationContext(new DeserializerFactory(), null);
 ObjectIdGenerator<?> gen = new ObjectIdGenerator() {};
 ObjectIdResolver resolverType = new ObjectIdResolver() {};
 Object id = new Object();

 // When
 context.findObjectId(id, gen, resolverType);
 context.findObjectId(id, gen, resolverType);
 context.checkUnresolvedObjectId();
 }

 @Test
 public void testDeserializerInstance() {
 // Given
 DefaultDeserializationContext context = new DefaultDeserializationContext(new DeserializerFactory(), null);
 Annotated ann = null;
 Object deserDef = new ObjectDeserializer();

 // When
 JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);

 // Then
 assertNotNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializerInstanceNullDeserDef() {
 // Given
 DefaultDeserializationContext context = new DefaultDeserializationContext(new DeserializerFactory(), null);
 Annotated ann = null;
 Object deserDef = null;

 // When, Then
 try {
 context.deserializerInstance(ann, deserDef);
 } catch (JsonMappingException e) {
 // Expected
 }
}

 @Test
 public void testDeserializerInstanceClass() {
 // Given
 DefaultDeserializationContext context = new DefaultDeserializationContext(new DeserializerFactory(), null);
 Annotated ann = null;
 Class<JsonDeserializer<Object>> deserDef = JsonDeserializer.class;

 // When
 JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);

 // Then
 assertNotNull(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testDeserializerInstanceInvalidDeserDef() {
 // Given
 DefaultDeserializationContext context = new DefaultDeserializationContext(new DeserializerFactory(), null);
 Annotated ann = null;
 Object deserDef = new Object();

 // When, Then
 try {
 context.deserializerInstance(ann, deserDef);
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testKeyDeserializerInstance() {
 // Given
 DefaultDeserializationContext context = new DefaultDeserializationContext(new DeserializerFactory(), null);
 Annotated ann = null;
 Object deserDef = new ObjectKeyDeserializer();

 // When
 KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);

 // Then
 assertNotNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testKeyDeserializerInstanceNullDeserDef() {
 // Given
 DefaultDeserializationContext context = new DefaultDeserializationContext(new DeserializerFactory(), null);
 Annotated ann = null;
 Object deserDef = null;

 // When, Then
 try {
 context.keyDeserializerInstance(ann, deserDef);
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testKeyDeserializerInstanceClass() {
 // Given
 DefaultDeserializationContext context = new DefaultDeserializationContext(new DeserializerFactory(), null);
 Annotated ann = null;
 Class<KeyDeserializer> deserDef = KeyDeserializer.class;

 // When
 KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);

 // Then
 assertNotNull(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testKeyDeserializerInstanceInvalidDeserDef() {
 // Given
 DefaultDeserializationContext context = new DefaultDeserializationContext(new DeserializerFactory(), null);
 Annotated ann = null;
 Object deserDef = new Object();

 // When, Then
 try {
 context.keyDeserializerInstance(ann, deserDef);
 } catch (IllegalStateException e) {
 // Expected
 }
 }
}