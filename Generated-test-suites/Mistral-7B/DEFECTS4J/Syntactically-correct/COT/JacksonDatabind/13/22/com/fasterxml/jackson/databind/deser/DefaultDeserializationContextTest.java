package com.example.test.jackson;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

 private DefaultDeserializationContext context;

 @Test
 public void testDefaultDeserializationContextConstructor() {
 context = new DefaultDeserializationContext(null, null);
 assertNotNull(context);
 }

 @Test
 public void testDefaultDeserializationContextCopy() {
 context = new DefaultDeserializationContext(null, null);
 DefaultDeserializationContext copy = context.copy();
 assertNotNull(copy);
 assertNotSame(context, copy);
 }

 @Test
 public void testFindObjectId() {
 context = new DefaultDeserializationContext(null, null);
 ObjectIdGenerator idGenerator = new StdSerializer<ObjectIdGenerator>(ObjectIdGenerator.class) {};
 ObjectIdResolver objectIdResolver = new SimpleObjectIdResolver();
 Object id = new Object();
 ReadableObjectId expected = new ReadableObjectId(new ObjectIdGenerator.IdKey("#"));

 ReadableObjectId result = context.findObjectId(id, idGenerator, objectIdResolver);
 assertEquals(expected, result);
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testCheckUnresolvedObjectId() {
 context = new DefaultDeserializationContext(null, null);
 ObjectIdGenerator idGenerator = new StdSerializer<ObjectIdGenerator>(ObjectIdGenerator.class) {};
 ObjectIdResolver objectIdResolver = new SimpleObjectIdResolver();
 Object id = new Object();
 Object id2 = new Object();
 ReadableObjectId id1 = context.findObjectId(id, idGenerator, objectIdResolver);
 ReadableObjectId id2Obj = context.findObjectId(id2, idGenerator, objectIdResolver);
 id1.setResolver(id2Obj.getResolver());
 context.checkUnresolvedObjectId();
 }

 @Test
 public void testDeserializerInstance() {
 context = new DefaultDeserializationContext(null, null);
 Annotated ann = new AnnotatedClassTest();
 Object deserDef = new JsonDeserializer<Object>() {};
 JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);
 assertNotNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializerInstanceInvalidDefinition() {
 context = new DefaultDeserializationContext(null, null);
 Annotated ann = new AnnotatedClassTest();
 Object deserDef = new Object() {};
 JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testKeyDeserializerInstance() {
 context = new DefaultDeserializationContext(null, null);
 Annotated ann = new AnnotatedClassTest();
 Object deserDef = new KeyDeserializer() {};
 KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);
 assertNotNull(result);
 }

 @Test(expected = JsonMappingException.class)
 public void testKeyDeserializerInstanceInvalidDefinition() {
 context = new DefaultDeserializationContext(null, null);
 Annotated ann = new AnnotatedClassTest();
 Object deserDef = new Object() {};
 KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);
 fail("Expected JsonMappingException");
 }

}