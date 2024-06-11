package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;

public class AtomicReferenceDeserializerTest {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testNewInstance() {
        JavaType fullType = mapper.getTypeFactory().constructType(AtomicReference.class, Object.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createValueInstance(DeserializationContext ctxt) throws JsonMappingException, InstantiationException, IllegalAccessException {
                return new AtomicReference<Object>();
            }
        };
        TypeDeserializer typeDeser = new AtomicReferenceDeserializer.NullValueSerializer(fullType, valueInstantiator, null, null);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeser, null);
        assertNotNull(deserializer);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
        AtomicReference<Object> reference = deserializer.getNullValue(null);
        assertNotNull(reference);
        assertEquals(null, reference.get());
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
        Object emptyValue = deserializer.getEmptyValue(null);
        assertNotNull(emptyValue);
        assertTrue(emptyValue instanceof AtomicReference);
        assertEquals(null, ((AtomicReference<Object>) emptyValue).get());
    }

    @Test
    public void testReferenceValue() throws JsonMappingException {
        Object contents = new Object();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
        AtomicReference<Object> reference = deserializer.referenceValue(contents);
        assertEquals(contents, reference.get());
    }

    @Test
    public void testGetReferenced() throws JsonMappingException {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
        Object referenced = deserializer.getReferenced(reference);
        assertEquals(reference.get(), referenced);
    }

    @Test
    public void testUpdateReference() throws JsonMappingException {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        Object newValue = new Object();
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, newValue);
        assertEquals(reference, updatedReference);
        assertEquals(newValue, reference.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(mapper.getTypeFactory().constructType(AtomicReference.class, Object.class), null, null, null);
        assertTrue(deserializer.supportsUpdate(null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNullValueSerializerNullValueDeserializer() {
        JavaType fullType = mapper.getTypeFactory().constructType(AtomicReference.class, Object.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createValueInstance(DeserializationContext ctxt) throws JsonMappingException, InstantiationException, IllegalAccessException {
                return null;
            }
        };
        TypeDeserializer typeDeser = new AtomicReferenceDeserializer.NullValueSerializer(fullType, valueInstantiator, null, null);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeser, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testUnknownTypeDeserializer() throws JsonMappingException {
        JavaType fullType = mapper.getTypeFactory().constructType(AtomicReference.class, Integer.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createValueInstance(DeserializationContext ctxt) throws JsonMappingException, InstantiationException, IllegalAccessException {
                return new AtomicReference<Integer>(0);
            }
        };
        TypeDeserializer typeDeser = new AtomicReferenceDeserializer.NullValueSerializer(fullType, valueInstantiator, null, null);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, valueInstantiator, null, null);
        String json = "{\"foo\": 1}";
        deserializer.deserialize(mapper.readTree(json), null);
    }

    @Test(expected = JsonMappingException.class)
    public void testUnknownValueDeserializer() throws JsonMappingException, JsonProcessingException {
        JavaType fullType = mapper.getTypeFactory().constructType(AtomicReference.class, Object.class);
        ValueInstantiator valueInstantiator = new ValueInstantiator() {
            @Override
            public Object createValueInstance(DeserializationContext ctxt) throws JsonMappingException, InstantiationException, IllegalAccessException {
                return new Object();
            }
        };
        JsonDeserializer<?> valueDeser = new StdDeserializer<Object>(Object.class) {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws JsonMappingException, IOException {
                throw new UnsupportedOperationException();
            }
        };
        TypeDeserializer typeDeser = new AtomicReferenceDeserializer.NullValueSerializer(fullType, valueInstantiator, null, valueDeser);
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeser, null);
        String json = "{\"foo\": \"bar\"}";
        deserializer.deserialize(mapper.readTree(json), null);
    }
}