package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testNewInstance() throws InstantiationException, IllegalAccessException {
        JavaType fullType = new TypeReference<AtomicReference<Object>>(){}.getType();
        ValueInstantiator inst = new ValueInstantiator() {
            @Override
            public Object createValueInstance(DeserializationConfig config, BeanDescription beanDesc) throws InstantiationException, IllegalAccessException {
                return new AtomicReference<Object>();
            }
        };
        TypeDeserializer typeDeser = new StdTypeDeserializer();
        JsonDeserializer<?> deser = null;
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
        assertNotNull(serializer);
        assertEquals(AtomicReferenceDeserializer.class, serializer.getClass());
    }

    @Test
    public void testWithResolved() {
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(null, null, null, null);
        TypeDeserializer typeDeser = new StdTypeDeserializer();
        JsonDeserializer<?> valueDeser = null;
        AtomicReferenceDeserializer newSerializer = serializer.withResolved(typeDeser, valueDeser);
        assertEquals(serializer.getClass(), newSerializer.getClass());
        assertNotSame(serializer, newSerializer);
    }

    @Test
    public void testGetNullValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> nullValue = serializer.getNullValue(mapper.getContext());
        assertNotNull(nullValue);
        assertTrue(nullValue instanceof AtomicReference);
        assertEquals(null, nullValue.get());
    }

    @Test
    public void testGetEmptyValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object emptyValue = serializer.getEmptyValue(mapper.getContext());
        assertNotNull(emptyValue);
        assertTrue(emptyValue instanceof AtomicReference);
        assertEquals(null, emptyValue);
    }

    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object contents = new Object();
        AtomicReference<Object> referenceValue = serializer.referenceValue(contents);
        assertEquals(contents, referenceValue.get());
        assertSame(contents, referenceValue);
    }

    @Test
    public void testGetReferenced() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(null, null, null, null);
        Object referenced = serializer.getReferenced(reference);
        assertSame(reference.get(), referenced);
    }

    @Test
    public void testUpdateReference() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        Object newContents = new Object();
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(null, null, null, null);
        AtomicReference<Object> updatedReference = serializer.updateReference(reference, newContents);
        assertEquals(updatedReference, reference);
        assertEquals(newContents, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(null, null, null, null);
        assertTrue(serializer.supportsUpdate(mapper.getConfig()));
    }
}