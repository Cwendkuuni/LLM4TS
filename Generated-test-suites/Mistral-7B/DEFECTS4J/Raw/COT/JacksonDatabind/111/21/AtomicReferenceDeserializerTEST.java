package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.base.JsonLocation;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.NameTransformer;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;

public class AtomicReferenceDeserializerTest {

    private ObjectMapper objectMapper;
    private DeserializationContext deserializationContext;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        deserializationContext = objectMapper.getDeserializationContext();
        deserializationContext.setFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setNameTransformer(new NameTransformer() {
            @Override
            public String transformName(String name, JsonLocation loc) {
                return name;
            }
        });
    }

    // Test method for constructor
    @Test
    public void testAtomicReferenceDeserializerConstruction() {
        JavaType fullType = TypeFactory.defaultInstance().constructType(AtomicReference.class, Object.class);
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(fullType, null, null, null);
        assertNotNull(serializer);
    }

    // Test method for withResolved
    @Test
    public void testWithResolved() throws JsonMappingException {
        JavaType fullType = TypeFactory.defaultInstance().constructType(AtomicReference.class, Object.class);
        ValueInstantiator instantiator = null;
        TypeDeserializer typeDeserializer = null;
        JsonDeserializer<?> valueDeserializer = null;
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(fullType, instantiator, typeDeserializer, valueDeserializer);
        AtomicReferenceDeserializer newSerializer = serializer.withResolved(typeDeserializer, valueDeserializer);
        assertNotSame(serializer, newSerializer);
    }

    // Test method for getNullValue
    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReference<Object> nullValue = AtomicReferenceDeserializer.getNullValue(deserializationContext);
        assertNotNull(nullValue);
        assertNull(nullValue.get());
    }

    // Test method for getEmptyValue
    @Test
    public void testGetEmptyValue() {
        AtomicReference<Object> emptyValue = AtomicReferenceDeserializer.getEmptyValue(deserializationContext);
        assertNotNull(emptyValue);
        assertNull(emptyValue.get());
    }

    // Test method for referenceValue
    @Test
    public void testReferenceValue() throws JsonMappingException, IOException {
        Object contents = "Test Contents";
        AtomicReference<Object> referenceValue = AtomicReferenceDeserializer.referenceValue(contents);
        assertEquals(contents, referenceValue.get());
    }

    // Test method for getReferenced
    @Test
    public void testGetReferenced() throws JsonMappingException {
        AtomicReference<Object> reference = new AtomicReference<Object>("Test Value");
        Object referencedValue = AtomicReferenceDeserializer.getReferenced(reference);
        assertEquals("Test Value", referencedValue);
    }

    // Test method for updateReference
    @Test
    public void testUpdateReference() throws JsonMappingException {
        AtomicReference<Object> reference = new AtomicReference<Object>("Test Value");
        Object newValue = "New Test Value";
        AtomicReference<Object> updatedReference = AtomicReferenceDeserializer.updateReference(reference, newValue);
        assertEquals(newValue, updatedReference.get());
        assertEquals(reference, updatedReference);
    }

    // Test method for supportsUpdate
    @Test
    public void testSupportsUpdate() {
        assertTrue(AtomicReferenceDeserializer.supportsUpdate(deserializationContext.getDefaultConfig()));
    }

    // Edge case tests

    // Test method for null JavaType in constructor
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullJavaType() {
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(null, null, null, null);
    }

    // Test method for null DeserializationContext in getNullValue
    @Test(expected = NullPointerException.class)
    public void testGetNullValueWithNullDeserializationContext() throws JsonMappingException {
        AtomicReferenceDeserializer.getNullValue(null);
    }

    // Test method for null DeserializationContext in getEmptyValue
    @Test(expected = NullPointerException.class)
    public void testGetEmptyValueWithNullDeserializationContext() {
        AtomicReferenceDeserializer.getEmptyValue(null);
    }

    // Test method for null Object in referenceValue
    @Test(expected = NullPointerException.class)
    public void testReferenceValueWithNullObject() throws JsonMappingException {
        AtomicReferenceDeserializer.referenceValue(null);
    }

    // Test method for null reference in getReferenced
    @Test(expected = NullPointerException.class)
    public void testGetReferencedWithNullReference() throws JsonMappingException {
        AtomicReferenceDeserializer.getReferenced(null);
    }
}