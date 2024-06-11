package com.example.junit.test.jackson;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class DefaultDeserializationContextTest extends TestBase {

 private DefaultDeserializationContext context;
 private DefaultDeserializationContext.Impl impl;

 @Before
 public void setUp() throws Exception {
 context = new DefaultDeserializationContext.DefaultDeserializationContext(null, null);
 impl = new DefaultDeserializationContext.Impl(context);
 }

 @Test
 public void testFindObjectId() {
 ObjectIdGenerator<?> idGenerator = new StubObjectIdGenerator();
 ObjectIdResolver idResolver = new StubObjectIdResolver();

 Object id = new Object();
 ReadableObjectId expectedResult = new ReadableObjectId(ObjectIdGenerator.IdKey.STANDARD);

 ReadableObjectId result = context.findObjectId(id, idGenerator, idResolver);
 assertEquals(expectedResult, result);
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testCheckUnresolvedObjectId() {
 ObjectIdGenerator<?> idGenerator = new StubObjectIdGenerator();
 ObjectIdResolver idResolver = new StubObjectIdResolver();
 ReadableObjectId id1 = context.findObjectId(new Object(), idGenerator, idResolver);
 ReadableObjectId id2 = context.findObjectId(new Object(), idGenerator, idResolver);

 context.checkUnresolvedObjectId();
 }

 @Test
 public void testDeserializerInstance() {
 Annotated ann = mock(Annotated.class);
 Object deserDef = new StubDeserializer();
 JsonDeserializer<Object> expectedResult = new StubDeserializer();

 JsonDeserializer<Object> result = context.deserializerInstance(ann, deserDef);
 assertEquals(expectedResult, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializerInstanceWithInvalidType() {
 Annotated ann = mock(Annotated.class);
 Object deserDef = new String();

 context.deserializerInstance(ann, deserDef);
 }

 @Test
 public void testKeyDeserializerInstance() {
 Annotated ann = mock(Annotated.class);
 Object deserDef = new StubKeyDeserializer();
 KeyDeserializer expectedResult = new StubKeyDeserializer();

 KeyDeserializer result = context.keyDeserializerInstance(ann, deserDef);
 assertEquals(expectedResult, result);
 }

 @Test(expected = JsonMappingException.class)
 public void testKeyDeserializerInstanceWithInvalidType() {
 Annotated ann = mock(Annotated.class);
 Object deserDef = new String();

 context.keyDeserializerInstance(ann, deserDef);
 }

 @Test
 public void testWith() {
 DefaultDeserializationContext newContext = context.with(mock(DeserializerFactory.class));
 assertNotSame(context, newContext);
 }

 @Test
 public void testCreateInstance() {
 DeserializationConfig config = new DeserializationConfig();
 JsonParser jp = mock(JsonParser.class);
 InjectableValues values = mock(InjectableValues.class);
 DefaultDeserializationContext newContext = impl.createInstance(config, jp, values);
 assertNotSame(impl, newContext);
 }

 @Test
 public void testCopy() {
 DefaultDeserializationContext newContext = impl.copy();
 assertNotSame(impl, newContext);
 }

}

class StubObjectIdGenerator implements ObjectIdGenerator<Object> {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return ObjectIdGenerator.IdKey.STANDARD;
 }
}

class StubObjectIdResolver implements ObjectIdResolver {
 @Override
 public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
 return new StubObjectIdResolver();
 }

 @Override
 public ReadableObjectId idFromObject(Object id) {
 return null;
 }
}

class StubDeserializer implements JsonDeserializer<Object> {
 @Override
 public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
 return null;
 }
}

class StubKeyDeserializer implements KeyDeserializer {
 @Override
 public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
 return null;
 }
}