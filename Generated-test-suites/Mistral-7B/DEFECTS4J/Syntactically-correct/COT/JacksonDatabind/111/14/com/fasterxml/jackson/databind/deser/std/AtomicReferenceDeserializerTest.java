package com.example.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.base.JsonSubType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.NoneTypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AtomicReferenceDeserializerTest {

 private ObjectMapper mapper;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 // Test constructor
 @Test
 public void testAtomicReferenceDeserializerConstruction() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>("com.test.AtomicReference"),
 new StubValueInstantiator(),
 new EmptyTypeDeserializer(),
 new NoneTypeDeserializer());
 assertEquals(AtomicReferenceDeserializer.class, atomicReferenceDeserializer.getClass());
 }

 // Test withResolved()
 @Test
 public void testWithResolved() throws JsonMappingException {
 AtomicReferenceDeserializer atomicReferenceDeserializer1 = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>("com.test.AtomicReference"),
 new StubValueInstantiator(),
 new EmptyTypeDeserializer(),
 new NoneTypeDeserializer());
 AtomicReferenceDeserializer atomicReferenceDeserializer2 = atomicReferenceDeserializer1.withResolved(new EmptyTypeDeserializer(), new NoneTypeDeserializer());
 assertEquals(atomicReferenceDeserializer1.getClass(), atomicReferenceDeserializer2.getClass());
 }

 // Test getNullValue()
 @Test
 public void testGetNullValue() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>("com.test.AtomicReference"),
 new StubValueInstantiator(),
 new EmptyTypeDeserializer(),
 new NoneTypeDeserializer());
 AtomicReference<Object> expectedNull = new AtomicReference<>();
 AtomicReference<Object> actualNull = atomicReferenceDeserializer.getNullValue(new TestContext());
 assertEquals(expectedNull, actualNull);
 }

 // Test getEmptyValue()
 @Test
 public void testGetEmptyValue() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>("com.test.AtomicReference"),
 new StubValueInstantiator(),
 new EmptyTypeDeserializer(),
 new NoneTypeDeserializer());
 AtomicReference<Object> expectedEmpty = new AtomicReference<>();
 AtomicReference<Object> actualEmpty = atomicReferenceDeserializer.getEmptyValue(new TestContext());
 assertEquals(expectedEmpty, actualEmpty);
 }

 // Test referenceValue()
 @Test
 public void testReferenceValue() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>("com.test.AtomicReference"),
 new StubValueInstantiator(),
 new EmptyTypeDeserializer(),
 new NoneTypeDeserializer());
 AtomicReference<Object> expectedReference = new AtomicReference<>("Test Value");
 Object actualReference = atomicReferenceDeserializer.referenceValue(new String("Test Value"));
 assertEquals(expectedReference.getClass(), actualReference.getClass());
 assertEquals(expectedReference, actualReference);
 }

 // Test getReferenced()
 @Test
 public void testGetReferenced() {
 AtomicReference<Object> atomicReference = new AtomicReference<Object>("Test Value");
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>("com.test.AtomicReference"),
 new StubValueInstantiator(),
 new EmptyTypeDeserializer(),
 new NoneTypeDeserializer());
 Object expectedValue = "Test Value";
 Object actualValue = atomicReferenceDeserializer.getReferenced(atomicReference);
 assertEquals(expectedValue, actualValue);
 }

 // Test updateReference()
 @Test
 public void testUpdateReference() throws JsonMappingException {
 AtomicReference<Object> atomicReference = new AtomicReference<Object>("Test Value");
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>("com.test.AtomicReference"),
 new StubValueInstantiator(),
 new EmptyTypeDeserializer(),
 new NoneTypeDeserializer());
 Object newValue = new String("New Test Value");
 ObjectNode node = mapper.createObjectNode();
 node.put("value", newValue);
 atomicReferenceDeserializer.updateReference(atomicReference, node);
 assertEquals(newValue, atomicReference.get());
 }

 // Test supportsUpdate()
 @Test
 public void testSupportsUpdate() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>("com.test.AtomicReference"),
 new StubValueInstantiator(),
 new EmptyTypeDeserializer(),
 new NoneTypeDeserializer());
 assertTrue(atomicReferenceDeserializer.supportsUpdate(new TestContext()));
 }

 // Edge cases and exceptions testing

 // Test constructor with invalid arguments
 @Test(expected = IllegalArgumentException.class)
 public void testAtomicReferenceDeserializerConstructionInvalidArguments() {
 new AtomicReferenceDeserializer(null, new StubValueInstantiator(), new EmptyTypeDeserializer(), new NoneTypeDeserializer());
 }

 // Test getNullValue() with null context
 @Test(expected = JsonMappingException.class)
 public void testGetNullValueNullContext() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>("com.test.AtomicReference"),
 new StubValueInstantiator(),
 new EmptyTypeDeserializer(),
 new NoneTypeDeserializer());
 atomicReferenceDeserializer.getNullValue(null);
 }

 // Test getEmptyValue() with null context
 @Test
 public void testGetEmptyValueNullContext() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>("com.test.AtomicReference"),
 new StubValueInstantiator(),
 new EmptyTypeDeserializer(),
 new NoneTypeDeserializer());
 AtomicReference<Object> actualEmpty = atomicReferenceDeserializer.getEmptyValue(null);
 assertNull(actualEmpty);
 }

 // Test referenceValue() with null argument
 @Test(expected = JsonMappingException.class)
 public void testReferenceValueNullArgument() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>("com.test.AtomicReference"),
 new StubValueInstantiator(),
 new EmptyTypeDeserializer(),
 new NoneTypeDeserializer());
 atomicReferenceDeserializer.referenceValue(null);
 }

 // Test getReferenced() with null reference
 @Test(expected = JsonMappingException.class)
 public void testGetReferencedNullReference() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>("com.test.AtomicReference"),
 new StubValueInstantiator(),
 new EmptyTypeDeserializer(),
 new NoneTypeDeserializer());
 atomicReferenceDeserializer.getReferenced(null);
 }

 // Test updateReference() with null context and null reference
 @Test(expected = JsonMappingException.class)
 public void testUpdateReferenceNullContextAndNullReference() {
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>("com.test.AtomicReference"),
 new StubValueInstantiator(),
 new EmptyTypeDeserializer(),
 new NoneTypeDeserializer());
 AtomicReference<Object> atomicReference = null;
 atomicReferenceDeserializer.updateReference(atomicReference, mapper.createObjectNode());
 }

 // Test updateReference() with null value
 @Test(expected = JsonMappingException.class)
 public void testUpdateReferenceNullValue() throws JsonMappingException {
 AtomicReference<Object> atomicReference = new AtomicReference<Object>("Test Value");
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>("com.test.AtomicReference"),
 new StubValueInstantiator(),
 new EmptyTypeDeserializer(),
 new NoneTypeDeserializer());
 ObjectNode node = mapper.createObjectNode();
 atomicReferenceDeserializer.updateReference(atomicReference, node);
 assertNull(atomicReference.get());
 }

 // Test updateReference() with non-AtomicReference type
 @Test(expected = JsonMappingException.class)
 public void testUpdateReferenceNonAtomicReferenceType() throws JsonMappingException {
 AtomicReference<Object> atomicReference = new AtomicReference<Object>("Test Value");
 AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(
 new ValueTypeReference<AtomicReference<Object>>("com.test.AtomicReference"),
 new StubValueInstantiator(),
 new EmptyTypeDeserializer(),
 new NoneTypeDeserializer());
 ObjectNode node = mapper.valueToTree(new String("Test Value"));
 atomicReferenceDeserializer.updateReference(atomicReference, node);
 assertNull(atomicReference.get());
 }
}

 // Helper classes

 class TestContext extends DeserializationContext {
 @Override
 public void handleUnexpectedValue(Object value, Class<?> valueType, JsonParser parser, DeserializationContext ctxt) {
 throw new JsonMappingException("Unexpected value: " + valueType + ": " + value);
 }

 @Override
 public void handleUnexpectedValueFully(Object value, Class<?> valueType, JsonParser parser, DeserializationContext ctxt) {
 throw new JsonMappingException("Unexpected value: " + valueType + ": " + value);
 }

 @Override
 public void handleUnexpectedToken(JsonParser parser, DeserializationContext ctxt) {
 throw new JsonMappingException("Unexpected token: " + parser.getCurrentName());
 }

 @Override
 public void handleMissingValueException(DeserializationContext ctxt) {
 throw new JsonMappingException("Missing value");
 }

 @Override
 public void handleMissingValueException(DeserializationContext ctxt, Class<?> type, AttributeDescriptor attribute) {
 throw new JsonMappingException("Missing value for " + type.getName() + " attribute " + attribute.getName());
 }

 @Override
 public void handleNumberFormat(JsonParser p, DeserializationContext ctxt) throws JsonMappingException {
 throw new JsonMappingException("Invalid number format: " + p.getText());
 }

 @Override
 public void handleInvalidToken(JsonParser jp, DeserializationContext ctxt) throws JsonMappingException {
 throw new JsonMappingException("Invalid token: " + jp.getText());
 }

 @Override
 public void handleUnmappableValue(Object value, Class<?> valueType, JsonParser parser, DeserializationContext ctxt) throws JsonMappingException {
 throw new JsonMappingException("Unable to deserialize value of type " + valueType + " as an instance of " + valueType.getName() + ". Value: " + value);
 }

 @Override
 public void handleInvalidTypeId(int id, Class<?> expectedTypeId, int actualTypeId, DeserializationContext ctxt) throws JsonMappingException {
 throw new JsonMappingException("Invalid type ID: expected " + expectedTypeId.getName() + ", but was " + actualTypeId);
 }

 @Override
 public void handleparsingError(JsonParser p, DeserializationContext ctxt) throws JsonMappingException {
 throw new JsonMappingException("Parsing error: " + p.getText());
 }

 @Override
 public void reportError(JsonParser p, DeserializationContext ctxt, JsonProcessingException e) throws JsonMappingException {
 throw new JsonMappingException("Error parsing: " + e.getMessage());
 }

 @Override
 public void reportError(DeserializationContext ctxt, JsonProcessingException e) throws JsonMappingException {
 throw new JsonMappingException("Error deserializing: " + e.getMessage());
 }

 @Override
 public void reportError(DeserializationContext ctxt, String msg) {
 throw new JsonMappingException(msg);
 }

 @Override
 public void reportError(DeserializationContext ctxt, String msg, Throwable t) {
 throw new JsonMappingException(msg, t);
 }

 @Override
 public SerializerProvider getSerializerProvider() {
 return new TestSerializerProvider();
 }

 @Override
 public ObjectWriter getObjectWriter() {
 return null;
 }

 @Override
 public void close() throws IOException {
 // empty
 }
}

 class StubValueInstantiator implements ValueInstantiator {
 @SuppressWarnings("unchecked")
 @Override
 public Object createValueInstance(DeserializationContext ctxt, BeanDescription valueTypeDesc) throws InstantiationException, IllegalAccessException {
 return new AtomicReference<Object>();
 }
}

 class TestSerializerProvider extends SerializerProvider {
 @Override
 public void serializeValue(Object value, JsonSerializer serializer, JsonGenerator generator) throws IOException, JsonProcessingException {
 // empty
 }
}