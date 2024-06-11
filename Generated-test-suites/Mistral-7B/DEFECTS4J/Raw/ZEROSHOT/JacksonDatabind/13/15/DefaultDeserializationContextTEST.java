package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class DefaultDeserializationContextTest extends TestBase {

 private DefaultDeserializationContext context;
 private DefaultDeserializationContext copyContext;

 @Before
 public void setup() {
 ObjectMapper mapper = new ObjectMapper();
 context = mapper.getDeserializationContext();
 }

 @Test
 public void testDefaultDeserializationContextConstruction() {
 DefaultDeserializationContext defaultContext = new DefaultDeserializationContext(context.getDeserializerFactory(), context.getDeserializerCache());
 assertNotNull(defaultContext);
 }

 @Test
 public void testDefaultDeserializationContextCopy() {
 copyContext = context.copy();
 assertNotNull(copyContext);
 assertNotSame(context, copyContext);
 }

 @Test
 public void testFindObjectId() {
 ObjectIdGenerator idGen = new SimpleObjectIdGenerator();
 ObjectIdResolver idResolver = new SimpleObjectIdResolver();
 ReadableObjectId objectId = context.findObjectId(new Object(), idGen, idResolver);
 assertNotNull(objectId);
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testUnresolvedObjectId() {
 context.findObjectId(null, new SimpleObjectIdGenerator(), new SimpleObjectIdResolver());
 }

 @Test
 public void testDeserializerInstance() {
 Annotated ann = mock(Annotated.class);
 Object deserDef = mock(Object.class);
 JsonDeserializer deserializer = context.deserializerInstance(ann, deserDef);
 assertNotNull(deserializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidDeserializerDefinition() {
 Annotated ann = mock(Annotated.class);
 Object deserDef = new Integer(1);
 JsonDeserializer deserializer = context.deserializerInstance(ann, deserDef);
 fail("Should throw JsonMappingException");
 }

 @Test
 public void testKeyDeserializerInstance() {
 Annotated ann = mock(Annotated.class);
 Object deserDef = mock(Object.class);
 KeyDeserializer keyDeserializer = context.keyDeserializerInstance(ann, deserDef);
 assertNotNull(keyDeserializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidKeyDeserializerDefinition() {
 Annotated ann = mock(Annotated.class);
 Object deserDef = new Integer(1);
 KeyDeserializer keyDeserializer = context.keyDeserializerInstance(ann, deserDef);
 fail("Should throw JsonMappingException");
 }

 @Test
 public void testWith() {
 DefaultDeserializationContext withContext = context.with(new SimpleDeserializerFactory());
 assertNotNull(withContext);
 assertNotSame(context, withContext);
 }

 @Test
 public void testCreateInstance() {
 DeserializationConfig config = mapper.getConfig();
 JsonParser jsonParser = mapper.getFactory().createParser("{}");
 InjectableValues injectableValues = new InjectableValues();
 DefaultDeserializationContext createdContext = context.createInstance(config, jsonParser, injectableValues);
 assertNotNull(createdContext);
 assertNotSame(context, createdContext);
 }
}