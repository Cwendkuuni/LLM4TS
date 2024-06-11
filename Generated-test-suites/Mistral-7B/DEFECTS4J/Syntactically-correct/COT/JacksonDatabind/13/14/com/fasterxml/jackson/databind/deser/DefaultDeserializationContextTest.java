package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
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

 private DefaultDeserializationContext ctx;

 @Before
 public void setUp() {
 ctx = new DefaultDeserializationContext.Impl(new StandardDeserializerProvider());
 }

 @Test
 public void testFindObjectId() {
 Object key = new Object();
 ObjectIdGenerator<?> gen = new StubObjectIdGenerator();
 ObjectIdResolver resolver = new StubObjectIdResolver();

 // Test with null id
 assertEquals(null, ctx.findObjectId(null, gen, resolver));

 // Test with valid id
 ObjectIdGenerator.IdKey idKey = gen.key(key);
 ReadableObjectId roid = ctx.findObjectId(key, gen, resolver);
 assertNotNull(roid);
 assertEquals(idKey, roid.getKey());

 // Test with ObjectIdGenerator.None
 assertEquals(null, ctx.findObjectId(key, null, resolver));

 // Test with SimpleObjectIdResolver
 assertEquals(null, ctx.findObjectId(key, gen, new SimpleObjectIdResolver()));
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testCheckUnresolvedObjectId() {
 Object key1 = new Object();
 Object key2 = new Object();
 Object key3 = new Object();

 ctx._objectIds = new LinkedHashMap<>();
 ctx._objectIdResolvers = new ArrayList<>();

 ReadableObjectId roid1 = new ReadableObjectId(ObjectIdGenerator.IdKey.CLASS, key1);
 ctx._objectIds.put(ObjectIdGenerator.IdKey.CLASS, roid1);

 ReadableObjectId roid2 = new ReadableObjectId(ObjectIdGenerator.IdKey.PROPERTY, key2, "testProperty");
 roid2.setReferringProperties(Arrays.asList(new Referring(ObjectIdGenerator.IdKey.CLASS, key3)));
 ctx._objectIds.put(ObjectIdGenerator.IdKey.PROPERTY, roid2);

 ctx.checkUnresolvedObjectId();
 }

 @Test
 public void testDeserializerInstance() {
 Annotated ann = mock(Annotated.class);
 Object deserDef = new JsonDeserializer<Object>() {};
 JsonDeserializer<Object> deser = ctx.deserializerInstance(ann, deserDef);
 assertNotNull(deser);
 assertEquals(deserDef.getClass(), deser.getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializerInstance_AnnotatedNull() {
 Object deserDef = new JsonDeserializer<Object>() {};
 JsonDeserializer<Object> deser = ctx.deserializerInstance(null, deserDef);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializerInstance_DeserDefNull() {
 Annotated ann = mock(Annotated.class);
 JsonDeserializer<Object> deser = ctx.deserializerInstance(ann, null);
 fail("Expected JsonMappingException");
 }

 @Test
 public void testKeyDeserializerInstance() {
 Annotated ann = mock(Annotated.class);
 Object deserDef = new StubKeyDeserializer() {};
 KeyDeserializer deser = ctx.keyDeserializerInstance(ann, deserDef);
 assertNotNull(deser);
 assertEquals(deserDef.getClass(), deser.getClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testKeyDeserializerInstance_AnnotatedNull() {
 Object deserDef = new StubKeyDeserializer() {};
 KeyDeserializer deser = ctx.keyDeserializerInstance(null, deserDef);
 fail("Expected JsonMappingException");
 }

 @Test(expected = JsonMappingException.class)
 public void testKeyDeserializerInstance_DeserDefNull() {
 Annotated ann = mock(Annotated.class);
 KeyDeserializer deser = ctx.keyDeserializerInstance(ann, null);
 fail("Expected JsonMappingException");
 }
}

class StubObjectIdGenerator implements ObjectIdGenerator<Object> {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) { return ObjectIdGenerator.IdKey.CLASS; }
}

class StubObjectIdResolver implements ObjectIdResolver {
 @Override
 public ObjectIdGenerator.IdKey idType(DeserializationContext ctxt) { return ObjectIdGenerator.IdKey.CLASS; }

 @Override
 public ObjectIdGenerator.IdGenerator<?> idGenerator(DeserializationContext ctxt) { return new StubObjectIdGenerator(); }
}

class StubKeyDeserializer implements KeyDeserializer {
 @Override
 public Object deserializeKey(DeserializationContext ctxt, JsonParser p, Object id) throws IOException { return null; }
}