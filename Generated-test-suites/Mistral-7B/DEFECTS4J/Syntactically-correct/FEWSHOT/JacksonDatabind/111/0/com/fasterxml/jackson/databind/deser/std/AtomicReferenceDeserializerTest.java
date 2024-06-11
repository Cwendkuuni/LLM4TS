package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.BaseParseTest;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class AtomicReferenceDeserializerTest extends BaseParseTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testAtomicReferenceDeserializer() throws Exception {
        // Test constructor
        JavaType fullType = MAPPER.getTypeFactory().constructType(AtomicReference.class, Object.class);
        ValueInstantiator valueInstantiator = MAPPER.getFactory().getValueInstantiator();
        TypeDeserializer typeDeserializer = MAPPER.getTypeDeserializer();
        JsonDeserializer<?> deserializer = MAPPER.findValueDeserializer(Object.class, null);
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeserializer, deserializer);
        assertNotNull(serializer);

        // Test withResolved method
        TypeDeserializer typeDeser = MAPPER.getTypeDeserializer();
        JsonDeserializer<?> valueDeser = MAPPER.findValueDeserializer(Object.class, null);
        AtomicReferenceDeserializer resolvedSerializer = serializer.withResolved(typeDeser, valueDeser);
        assertNotNull(resolvedSerializer);

        // Test getNullValue method
        DeserializationContext ctxt = new TestContext();
        AtomicReference<Object> nullValue = serializer.getNullValue(ctxt);
        assertNotNull(nullValue);
        assertNull(nullValue.get());

        // Test getEmptyValue method
        Object emptyValue = serializer.getEmptyValue(ctxt);
        assertNotNull(emptyValue);
        assertNotNull(emptyValue.getClass());
        assertNull(((AtomicReference<Object>) emptyValue).get());

        // Test referenceValue method
        Object contents = new Object();
        AtomicReference<Object> referenceValue = serializer.referenceValue(contents);
        assertNotNull(referenceValue);
        assertNotNull(referenceValue.get());
        assertEquals(contents, referenceValue.get());

        // Test getReferenced method
        Object referencedValue = referenceValue.get();
        AtomicReference<Object> deserializedReference = resolvedSerializer.getReferenced(referenceValue);
        assertNotNull(deserializedReference);
        assertEquals(referencedValue, deserializedReference.get());

        // Test updateReference method
        Object updatedContents = new Object();
        AtomicReference<Object> updatedReference = serializer.updateReference(referenceValue, updatedContents);
        assertNotNull(updatedReference);
        assertEquals(updatedContents, updatedReference.get());
        assertEquals(updatedContents, referenceValue.get());

        // Test supportsUpdate method
        boolean supportsUpdate = serializer.supportsUpdate(MAPPER.getConfig());
        assertTrue(supportsUpdate);
    }

    @Test
    public void testDeserializationWithValueDeserializer() throws Exception {
        // Test deserialization with a simple value deserializer
        String json = "{\"foo\": {\"$class\": \"com.fasterxml.jackson.databind.JsonNode\", \"@type\": \"com.fasterxml.jackson.databind.node.ObjectNode\", \"value\": {\"@class\": \"java.lang.Integer\", \"@\": null, \"value\": 42}} }";
        ObjectNode node = MAPPER.readValue(json, ObjectNode.class);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = new DefaultSerializerProvider();
        DeserializationContext ctxt = new TestContext(provider);
        AtomicReference<Object> result = mapper.readValue(node, AtomicReference.class);
        assertNotNull(result);
        assertNotNull(result.get());
        assertEquals(42, result.get());
    }

    @Test
    public void testDeserializationWithNullValue() throws Exception {
        // Test deserialization with null value
        ObjectNode node = MAPPER.createObjectNode();
        node.put("foo", null);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = new DefaultSerializerProvider();
        DeserializationContext ctxt = new TestContext(provider);
        AtomicReference<Object> result = mapper.readValue(node, AtomicReference.class);
        assertNotNull(result);
        assertNull(result.get());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidJson() throws Exception {
        // Test deserialization with invalid JSON
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = new DefaultSerializerProvider();
        DeserializationContext ctxt = new TestContext(provider);
        mapper.readValue("Invalid JSON", AtomicReference.class);
    }
}