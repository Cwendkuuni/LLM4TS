package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testNewInstance() throws JsonProcessingException {
        JavaType fullType = mock(JavaType.class);
        ValueInstantiator inst = mock(ValueInstantiator.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);

        assertEquals(AtomicReferenceDeserializer.class, deserializer.getClass());
        assertEquals(fullType, deserializer.getContentType());
        assertEquals(inst, deserializer.getValueInstantiator());
        assertEquals(typeDeser, deserializer.getTypeDeserializer());
        assertEquals(deser, deserializer.getDeserializer());
    }

    @Test
    public void testWithResolved() {
        JavaType baseType = mock(JavaType.class);
        ValueInstantiator inst = mock(ValueInstantiator.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<?> valueDeser = mock(JsonDeserializer.class);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(baseType, inst, typeDeser, valueDeser);

        JavaType fullType = mock(JavaType.class);
        TypeDeserializer typeDeser2 = mock(TypeDeserializer.class);
        JsonDeserializer<?> deser2 = mock(JsonDeserializer.class);

        AtomicReferenceDeserializer newDeserializer = deserializer.withResolved(typeDeser2, deser2);

        assertEquals(deserializer.getClass(), newDeserializer.getClass());
        assertEquals(fullType, newDeserializer.getContentType());
        assertEquals(inst, newDeserializer.getValueInstantiator());
        assertEquals(typeDeser2, newDeserializer.getTypeDeserializer());
        assertEquals(deser2, newDeserializer.getDeserializer());
    }

    @Test
    public void testGetNullValue() throws JsonProcessingException {
        DeserializationContext context = mock(DeserializationContext.class);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);

        AtomicReference<Object> result = deserializer.getNullValue(context);

        assertNull(result);
    }

    @Test
    public void testGetEmptyValue() {
        DeserializationContext context = mock(DeserializationContext.class);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);

        Object result = deserializer.getEmptyValue(context);

        assertNull(result);
    }

    @Test
    public void testReferenceValue() throws JsonProcessingException {
        Object contents = "some contents";
        DeserializationContext context = mock(DeserializationContext.class);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);

        AtomicReference<Object> result = deserializer.referenceValue(contents);

        assertEquals(contents, result.get());
    }

    @Test
    public void testGetReferenced() throws JsonProcessingException {
        AtomicReference<Object> reference = new AtomicReference<Object>("initialized value");
        DeserializationContext context = mock(DeserializationContext.class);

        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);

        Object result = deserializer.getReferenced(reference);

        assertEquals("initialized value", result);
    }

    @Test
    public void testUpdateReference() throws JsonProcessingException {
        AtomicReference<Object> reference = new AtomicReference<Object>("initialized value");
        Object contents = "updated value";
        DeserializationContext context = mock(DeserializationContext.class);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);

        AtomicReference<Object> result = deserializer.updateReference(reference, contents);

        assertEquals(contents, result.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);

        Boolean result = deserializer.supportsUpdate(config);

        assertTrue(result);
    }
}