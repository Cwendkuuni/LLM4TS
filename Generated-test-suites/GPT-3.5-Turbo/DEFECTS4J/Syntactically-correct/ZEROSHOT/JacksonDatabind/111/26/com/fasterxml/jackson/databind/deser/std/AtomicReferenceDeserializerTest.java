package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    private AtomicReferenceDeserializer atomicReferenceDeserializer;
    
    @Before
    public void setUp() {
        JavaType fullType = JavaType.empty();
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer() {
            // implement necessary methods for mock
        };
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>() {
            // implement necessary methods for mock
        };
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);
    }
    
    @Test
    public void testWithResolved() {
        TypeDeserializer typeDeser = new TypeDeserializer() {
            // implement necessary methods for mock
        };
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>() {
            // implement necessary methods for mock
        };
        AtomicReferenceDeserializer resolved = atomicReferenceDeserializer.withResolved(typeDeser, valueDeser);
        assertNotNull(resolved);
        assertEquals(atomicReferenceDeserializer._fullType, resolved._fullType);
        assertEquals(atomicReferenceDeserializer._valueInstantiator, resolved._valueInstantiator);
        assertEquals(typeDeser, resolved._typeDeserializer);
        assertEquals(valueDeser, resolved._valueDeserializer);
    }
    
    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = new DeserializationContext();
        AtomicReference<Object> reference = atomicReferenceDeserializer.getNullValue(ctxt);
        assertNotNull(reference);
        assertNull(reference.get());
    }
    
    @Test
    public void testGetEmptyValue() {
        DeserializationContext ctxt = new DeserializationContext();
        Object value = atomicReferenceDeserializer.getEmptyValue(ctxt);
        assertNotNull(value);
        assertTrue(value instanceof AtomicReference);
    }
    
    @Test
    public void testReferenceValue() {
        String contents = "test";
        AtomicReference<Object> reference = atomicReferenceDeserializer.referenceValue(contents);
        assertNotNull(reference);
        assertEquals(contents, reference.get());
    }
    
    @Test
    public void testGetReferenced() {
        String contents = "test";
        AtomicReference<Object> reference = new AtomicReference<Object>(contents);
        Object referenced = atomicReferenceDeserializer.getReferenced(reference);
        assertEquals(contents, referenced);
    }
    
    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>();
        String contents = "test";
        AtomicReference<Object> updated = atomicReferenceDeserializer.updateReference(reference, contents);
        assertNotNull(updated);
        assertEquals(contents, updated.get());
    }
    
    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig();
        Boolean supportsUpdate = atomicReferenceDeserializer.supportsUpdate(config);
        assertTrue(supportsUpdate);
    }
}