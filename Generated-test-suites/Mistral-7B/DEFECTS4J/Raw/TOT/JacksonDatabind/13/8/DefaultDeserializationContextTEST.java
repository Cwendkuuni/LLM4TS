package com.example.jackson.test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.SimpleObjectIdResolver;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DefaultDeserializationContextTest {

 private DefaultDeserializationContext context;
 private JsonParser parser;

 @Before
 public void setUp() throws JsonProcessingException {
 context = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());
 parser = new MapperFactory().getParser();
 }

 @Test
 public void testFindObjectId() throws JsonProcessingException {
 ObjectIdGenerator idGen = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return IdKey.STRING;
 }

 @Override
 public ObjectIdGenerator.Id getId(Object id) {
 return new ObjectId(id.toString());
 }
 };
 ObjectIdResolver resolver = new SimpleObjectIdResolver();

 Object id = new Object();
 ReadableObjectId roid = context.findObjectId(id, idGen, resolver);
 assertEquals(id.toString(), roid.getId().toString());
 assertEquals(resolver, roid.getResolver());
 }

 @Test
 public void testFindObjectIdNullId() throws JsonProcessingException {
 ObjectIdGenerator idGen = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return IdKey.STRING;
 }

 @Override
 public ObjectIdGenerator.Id getId(Object id) {
 return null;
 }
 };
 ObjectIdResolver resolver = new SimpleObjectIdResolver();

 Object id = new Object();
 ReadableObjectId roid = context.findObjectId(id, idGen, resolver);
 assertNull(roid);
 }

 @Test
 public void testCheckUnresolvedObjectId() throws JsonProcessingException {
 ObjectIdGenerator idGen = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return IdKey.STRING;
 }

 @Override
 public ObjectIdGenerator.Id getId(Object id) {
 return new ObjectId(id.toString());
 }
 };
 Object id1 = new Object();
 Object id2 = new Object();
 context.findObjectId(id1, idGen, null);
 context.findObjectId(id2, idGen, null);
 context.checkUnresolvedObjectId();
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testCheckUnresolvedObjectIdUnresolved() throws JsonProcessingException {
 ObjectIdGenerator idGen = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return IdKey.STRING;
 }

 @Override
 public ObjectIdGenerator.Id getId(Object id) {
 return new ObjectId(id.toString());
 }
 };
 Object id = new Object();
 context.findObjectId(id, idGen, null);
 context.checkUnresolvedObjectId();
 }
}