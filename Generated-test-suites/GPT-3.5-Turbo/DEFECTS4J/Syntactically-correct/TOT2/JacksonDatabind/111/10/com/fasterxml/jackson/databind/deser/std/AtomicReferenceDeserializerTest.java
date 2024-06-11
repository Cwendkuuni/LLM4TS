package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.atomic.AtomicReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;

public class AtomicReferenceDeserializerTest {

    private AtomicReferenceDeserializer deserializer;

    @Before
    public void setUp() {
        deserializer = new AtomicReferenceDeserializer(
            TypeFactory.defaultInstance().constructType(AtomicReference.class),
            mock(ValueInstantiator.class),
            mock(TypeDeserializer.class),
            mock(JsonDeserializer.class)
        );
    }

    @Test
    public void testWithResolved() {
        AtomicReferenceDeserializer resolved = deserializer.withResolved(
            mock(TypeDeserializer.class),
            mock(JsonDeserializer.class)
        );

        assertNotNull(resolved);
        assertNotEquals(deserializer, resolved);
        // Add more assertions for edge cases
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReference<Object> nullValue = deserializer.getNullValue(mock(DeserializationContext.class));

        assertNotNull(nullValue);
        assertNull(nullValue.get());
        // Add more assertions for edge cases
    }

    @Test
    public void testGetEmptyValue() {
        Object emptyValue = deserializer.getEmptyValue(mock(DeserializationContext.class));

        assertNotNull(emptyValue);
        assertTrue(emptyValue instanceof AtomicReference);
        // Add more assertions for edge cases
    }

    @Test
    public void testReferenceValue() {
        AtomicReference<Object> referenceValue = deserializer.referenceValue("test");

        assertNotNull(referenceValue);
        assertEquals("test", referenceValue.get());
        // Add more assertions for edge cases
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<>("test");
        Object referenced = deserializer.getReferenced(reference);

        assertNotNull(referenced);
        assertEquals("test", referenced);
        // Add more assertions for edge cases
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>();
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, "test");

        assertNotNull(updatedReference);
        assertEquals("test", updatedReference.get());
        // Add more assertions for edge cases
    }

    @Test
    public void testSupportsUpdate() {
        Boolean supportsUpdate = deserializer.supportsUpdate(mock(DeserializationConfig.class));

        assertNotNull(supportsUpdate);
        assertTrue(supportsUpdate);
        // Add more assertions for edge cases
    }
}