package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class DefaultDeserializationContextTest {

 private DefaultDeserializationContext context;

 @Before
 public void setUp() {
 context = new DefaultDeserializationContext.Impl(new SimpleObjectMapper().getDefaultDeserializationContext());
 }

 @Test
 public void testFindObjectId() {
 Object id = new Object();
 ObjectIdGenerator<?> gen = new StandardObjectIdGenerator();
 ObjectIdResolver resolverType = new SimpleObjectIdResolver();
 ReadableObjectId expectedObjectId = new ReadableObjectId(new ObjectIdGenerator.IdKey(id.getClass()));
 expectedObjectId.setResolver(resolverType);

 ReadableObjectId actualObjectId = context.findObjectId(id, gen, resolverType);
 assertEquals(expectedObjectId, actualObjectId);
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testCheckUnresolvedObjectId() {
 Object id1 = new Object();
 Object id2 = new Object();
 context.findObjectId(id1, null, null);
 context.findObjectId(id2, null, null);
 context.checkUnresolvedObjectId();
 }

 @Test
 public void testDeserializerInstance() {
 Class<JsonDeserializer<String>> deserializerClass = JsonDeserializer.class;
 JsonDeserializer<String> expectedDeserializer = new JsonDeserializer<String>() {};
 HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {};
 JsonDeserializer<String> actualDeserializer = context.deserializerInstance(null, deserializerClass);
 assertEquals(expectedDeserializer, actualDeserializer);

 if (handlerInstantiator != null) {
 JsonDeserializer<String> deserializerInstance = handlerInstantiator.deserializerInstance(null, null, deserializerClass);
 JsonDeserializer<String> actualDeserializer = context.deserializerInstance(null, deserializerInstance);
 assertEquals(deserializerInstance, actualDeserializer);
 }
 }

 @Test
 public void testKeyDeserializerInstance() {
 Class<KeyDeserializer<String>> keyDeserializerClass = KeyDeserializer.class;
 KeyDeserializer<String> expectedKeyDeserializer = new KeyDeserializer<String>() {};
 HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {};
 KeyDeserializer<String> actualKeyDeserializer = context.keyDeserializerInstance(null, null);
 assertEquals(expectedKeyDeserializer, actualKeyDeserializer);

 if (handlerInstantiator != null) {
 KeyDeserializer<String> keyDeserializerInstance = handlerInstantiator.keyDeserializerInstance(null, null, keyDeserializerClass);
 KeyDeserializer<String> actualKeyDeserializer = context.keyDeserializerInstance(null, keyDeserializerInstance);
 assertEquals(keyDeserializerInstance, actualKeyDeserializer);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testCopy() {
 context.copy();
 }
}