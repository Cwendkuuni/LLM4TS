package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testWithResolved() {
        // Arrange
        JavaType fullType = mock(JavaType.class);
        ValueInstantiator inst = mock(ValueInstantiator.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<?> valueDeser = mock(JsonDeserializer.class);
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);

        // Act
        AtomicReferenceDeserializer resolvedDeserializer = deserializer.withResolved(typeDeser, valueDeser);

        // Assert
        assertNotNull(resolvedDeserializer);
        assertEquals(fullType, resolvedDeserializer.getFullType());
        assertEquals(inst, resolvedDeserializer.getValueInstantiator());
        assertEquals(typeDeser, resolvedDeserializer.getTypeDeserializer());
        assertEquals(valueDeser, resolvedDeserializer.getValueDeserializer());
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        // Arrange
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();

        // Act
        AtomicReference<Object> nullValue = deserializer.getNullValue(ctxt);

        // Assert
        assertNotNull(nullValue);
        assertNull(nullValue.get());
    }

    @Test
    public void testGetEmptyValue() {
        // Arrange
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();

        // Act
        Object emptyValue = deserializer.getEmptyValue(ctxt);

        // Assert
        assertNotNull(emptyValue);
        assertTrue(emptyValue instanceof AtomicReference);
        assertNull(((AtomicReference<?>) emptyValue).get());
    }
    
    @Test
    public void testReferenceValue() {
        // Arrange
        Object contents = new Object();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();

        // Act
        AtomicReference<Object> referenceValue = deserializer.referenceValue(contents);

        // Assert
        assertNotNull(referenceValue);
        assertEquals(contents, referenceValue.get());
    }

    @Test
    public void testGetReferenced() {
        // Arrange
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        
        // Act
        Object referenced = deserializer.getReferenced(reference);
        
        // Assert
        assertEquals(contents, referenced);
    }

    @Test
    public void testUpdateReference() {
        // Arrange
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>();
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        
        // Act
        AtomicReference<Object> updatedReference = deserializer.updateReference(reference, contents);
        
        // Assert
        assertEquals(contents, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        // Arrange
        DeserializationConfig config = mock(DeserializationConfig.class);
        
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer();
        
        // Act
        Boolean supportsUpdate = deserializer.supportsUpdate(config);
        
        // Assert
        assertTrue(supportsUpdate);
    }
}