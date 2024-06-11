package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {
    @Test
    public void testConstructor() throws IOException {
        JavaType fullType = new TypeReference<AtomicReference<String>>(){}.getType();
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createValueInstance() {
                return new AtomicReference<String>();
            }
        };
        TypeDeserializer typeDeser = new JacksonAnnotationIntrospector().findValueDeserializer(Null.class, new SimpleModule());
        JsonDeserializer<?> deser = new StdDeserializer<>();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeser, deser);
        assertNotNull(deserializer);
    }

    @Test
    public void testWithResolved() throws IOException {
        JavaType fullType = new TypeReference<AtomicReference<String>>(){}.getType();
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createValueInstance() {
                return new AtomicReference<String>();
            }
        };
        TypeDeserializer typeDeser = new JacksonAnnotationIntrospector().findValueDeserializer(Null.class, new SimpleModule());
        JsonDeserializer<?> deser = new StdDeserializer<>();
        AtomicReferenceDeserializer deserializer1 = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeser, deser);
        AtomicReferenceDeserializer deserializer2 = deserializer1.withResolved(typeDeser, deser);
        assertEquals(deserializer1, deserializer2);
    }

    @Test
    public void testGetNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new TypeReference<AtomicReference<String>>(){}.getType(), null, null, null);
        AtomicReference<String> result = deserializer.getNullValue(mapper.getDeserializationConfig());
        assertNotNull(result);
        assertTrue(result instanceof AtomicReference);
        assertEquals(result.getClass(), AtomicReference.class);
        assertNull(result.get());
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new TypeReference<AtomicReference<String>>(){}.getType(), null, null, null);
        Object result = deserializer.getEmptyValue(null);
        assertNotNull(result);
        assertTrue(result instanceof AtomicReference);
        assertEquals(result.getClass(), AtomicReference.class);
        assertNull(result.get());
    }

    @Test
    public void testReferenceValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object contents = "content";
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new TypeReference<AtomicReference<String>>(){}.getType(), null, null, null);
        AtomicReference<String> result = deserializer.referenceValue(contents);
        assertNotNull(result);
        assertTrue(result instanceof AtomicReference);
        assertEquals(result.getClass(), AtomicReference.class);
        assertEquals(result.get(), contents);
    }

    @Test
    public void testGetReferenced() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReference<String> reference = new AtomicReference<>("content");
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new TypeReference<AtomicReference<String>>(){}.getType(), null, null, null);
        Object result = deserializer.getReferenced(reference);
        assertNotNull(result);
        assertEquals(result, reference.get());
    }

    @Test
    public void testUpdateReference() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AtomicReference<String> reference = new AtomicReference<>("content");
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new TypeReference<AtomicReference<String>>(){}.getType(), null, null, null);
        Object newContents = "newContent";
        deserializer.updateReference(reference, newContents);
        assertEquals(newContents, reference.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(new TypeReference<AtomicReference<String>>(){}.getType(), null, null, null);
        assertTrue(deserializer.supportsUpdate(null));
    }
}