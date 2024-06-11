package com.example.test.jackson;

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
 private ObjectMapper mapper;

 @Before
 public void setup() throws Exception {
 mapper = new ObjectMapper();
 context = mapper.getDeserializationContext();
 }

 @Test
 public void testFindObjectId() throws Exception {
 ObjectIdGenerator gen = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return new ObjectIdGenerator.IdKey(getIdClass(), id);
 }

 @Override
 public ObjectIdGenerator.IdKey key(Class<?> idType) {
 return new ObjectIdGenerator.IdKey(getIdClass(), idType);
 }

 @Override
 public ObjectIdGenerator.IdKey key(Annotated idAnnotation) {
 return new ObjectIdGenerator.IdKey(getIdClass(), idAnnotation);
 }

 private static Class<?> getIdClass() {
 return Integer.class;
 }
 };
 ObjectIdResolver resolver = new SimpleObjectIdResolver();

 ObjectId id = context.findObjectId(1, gen, resolver);
 assertEquals(1, id.getId());

 id = context.findObjectId("id", gen, resolver);
 fail("Expected UnresolvedForwardReference"); // test for missing id
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testFindObjectIdMissing() throws Exception {
 ObjectIdGenerator gen = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return new ObjectIdGenerator.IdKey(getIdClass(), id);
 }

 @Override
 public ObjectIdGenerator.IdKey key(Class<?> idType) {
 return new ObjectIdGenerator.IdKey(getIdClass(), idType);
 }

 @Override
 public ObjectIdGenerator.IdKey key(Annotated idAnnotation) {
 return new ObjectIdGenerator.IdKey(getIdClass(), idAnnotation);
 }

 private static Class<?> getIdClass() {
 return Integer.class;
 }
 };
 ObjectIdResolver resolver = new SimpleObjectIdResolver();
 ObjectId id = context.findObjectId(new Object(), gen, resolver);
 fail("Expected UnresolvedForwardReference"); // test for unresolved id
 }

 @Test
 public void testFindObjectIdDeprecated() throws Exception {
 ObjectIdGenerator gen = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return new ObjectIdGenerator.IdKey(getIdClass(), id);
 }

 @Override
 public ObjectIdGenerator.IdKey key(Class<?> idType) {
 return new ObjectIdGenerator.IdKey(getIdClass(), idType);
 }

 @Override
 public ObjectIdGenerator.IdKey key(Annotated idAnnotation) {
 return new ObjectIdGenerator.IdKey(getIdClass(), idAnnotation);
 }

 private static Class<?> getIdClass() {
 return Integer.class;
 }
 };
 ObjectId id = context.findObjectId(1, gen);
 assertEquals(1, id.getId());
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testFindObjectIdDeprecatedMissing() throws Exception {
 ObjectIdGenerator gen = new ObjectIdGenerator() {
 @Override
 public ObjectIdGenerator.IdKey key(Object id) {
 return new ObjectIdGenerator.IdKey(getIdClass(), id);
 }

 @Override
 public ObjectIdGenerator.IdKey key(Class<?> idType) {
 return new ObjectIdGenerator.IdKey(getIdClass(), idType);
 }

 @Override
 public ObjectIdGenerator.IdKey key(Annotated idAnnotation) {
 return new ObjectIdGenerator.IdKey(getIdClass(), idAnnotation);
 }

 private static Class<?> getIdClass() {
 return Integer.class;
 }
 };
 ObjectId id = context.findObjectId(new Object(), gen);
 fail("Expected UnresolvedForwardReference"); // test for missing id
 }

 @Test
 public void testCheckUnresolvedObjectId() throws Exception {
 context._objectIds = new LinkedHashMap<>();
 context._objectIds.put(new ObjectIdGenerator.IdKey(Integer.class, 1), new ReadableObjectId(1));
 context._objectIds.put(new ObjectIdGenerator.IdKey(Integer.class, "id"), new ReadableObjectId("id"));

 context.checkUnresolvedObjectId();

 context._objectIds.remove(new ObjectIdGenerator.IdKey(Integer.class, 1));
 context.checkUnresolvedObjectId();
 }

 @Test(expected = UnresolvedForwardReference.class)
 public void testCheckUnresolvedObjectIdMissing() throws Exception {
 context._objectIds = new LinkedHashMap<>();
 context._objectIds.put(new ObjectIdGenerator.IdKey(Integer.class, 1), new ReadableObjectId(1));

 context.checkUnresolvedObjectId();
 context._objectIds.remove(new ObjectIdGenerator.IdKey(Integer.class, 1));

 context._objectIds.put(new ObjectIdGenerator.IdKey(Integer.class, "id"), new ReadableObjectId("id"));
 context.checkUnresolvedObjectId();
 }

 @Test
 public void testDeserializerInstance() throws Exception {
 JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return null;
 }
 };
 Annotated ann = mapper.findConcreteAnnotated(String.class, mapper.getFactory().findValueTypeAnnotation(String.class));
 Object deser = context.deserializerInstance(ann, deserializer);
 assertNotNull(deser);
 assertEquals(deserializer, deser);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializerInstanceWrongType() throws Exception {
 JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return null;
 }
 };
 Object deser = context.deserializerInstance(new AnnotatedClassWrapper<>(Integer.class), deserializer);
 fail("Expected JsonMappingException"); // test for wrong type
 }

 @Test
 public void testDeserializerInstanceNoClass() throws Exception {
 JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return null;
 }
 };
 Object deser = context.deserializerInstance(deserializer);
 assertNull(deser);
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializerInstanceMissingFactory() throws Exception {
 JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
 @Override
 public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return null;
 }
 };
 Object deser = context.deserializerInstance(new AnnotatedClassWrapper<>(String.class), deserializer);
 fail("Expected JsonMappingException"); // test for missing factory
 }

 @Test
 public void testDeserializerInstanceClass() throws Exception {
 HandlerInstantiator hi = mapper.getFactory().getHandlerInstantiator();
 JsonDeserializer<String> deserializer = hi.deserializerInstance(mapper.getConfig(), new AnnotatedClassWrapper<>(String.class), JsonDeserializer.class);
 Object deser = context.deserializerInstance(new AnnotatedClassWrapper<>(String.class), deserializer);
 assertNotNull(deser);
 assertEquals(deserializer, deser);
 }

 @Test
 public void testDeserializerInstanceClassWithFactory() throws Exception {
 HandlerInstantiator hi = mapper.getFactory().getHandlerInstantiator();
 JsonDeserializer<String> deserializer = hi.deserializerInstance(mapper.getConfig(), new AnnotatedClassWrapper<>(String.class), JsonDeserializer.class);
 Object deser = context.deserializerInstance(deserializer);
 assertNotNull(deser);
 assertEquals(deserializer, deser);
 }

 @Test(expected = IllegalStateException.class)
 public void testCopy() throws Exception {
 context = context.copy();
 fail("Expected IllegalStateException"); // test for not overriding copy()
 }
}