package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import java.util.concurrent.atomic.AtomicReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    private AtomicReferenceDeserializer atomicReferenceDeserializer;

    @Test
    public void testConstructor() {
        JavaType fullType = new JavaType();
        ValueInstantiator inst = new ValueInstantiator();
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> deser = new JsonDeserializer<>();
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);

        assertNotNull(atomicReferenceDeserializer);
        assertEquals(fullType, atomicReferenceDeserializer._fullType);
        assertEquals(inst, atomicReferenceDeserializer._valueInstantiator);
        assertEquals(typeDeser, atomicReferenceDeserializer._valueDeserializer);
        assertEquals(deser, atomicReferenceDeserializer._jsonDeserializer);
    }

    @Test
    public void testWithResolved() {
        TypeDeserializer typeDeser = new TypeDeserializer();
        JsonDeserializer<?> valueDeser = new JsonDeserializer<>();
        AtomicReferenceDeserializer newDeserializer = atomicReferenceDeserializer.withResolved(typeDeser, valueDeser);

        assertNotNull(newDeserializer);
        assertEquals(atomicReferenceDeserializer._fullType, newDeserializer._fullType);
        assertEquals(atomicReferenceDeserializer._valueInstantiator, newDeserializer._valueInstantiator);
        assertEquals(typeDeser, newDeserializer._valueDeserializer);
        assertEquals(valueDeser, newDeserializer._jsonDeserializer);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReference<Object> nullValue = atomicReferenceDeserializer.getNullValue(new DeserializationContext());

        assertNotNull(nullValue);
        assertNull(nullValue.get());
    }

    @Test
    public void testGetEmptyValue() {
        Object emptyValue = atomicReferenceDeserializer.getEmptyValue(new DeserializationContext());

        assertNotNull(emptyValue);
        assertTrue(emptyValue instanceof AtomicReference);
        assertNull(((AtomicReference<?>) emptyValue).get());
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReference<Object> referenceValue = atomicReferenceDeserializer.referenceValue(contents);

        assertNotNull(referenceValue);
        assertEquals(contents, referenceValue.get());
    }

    @Test
    public void testGetReferenced() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>(contents);
        Object referenced = atomicReferenceDeserializer.getReferenced(reference);

        assertNotNull(referenced);
        assertEquals(contents, referenced);
    }

    @Test
    public void testUpdateReference() {
        Object contents = new Object();
        AtomicReference<Object> reference = new AtomicReference<>();
        AtomicReference<Object> updatedReference = atomicReferenceDeserializer.updateReference(reference, contents);

        assertNotNull(updatedReference);
        assertEquals(contents, updatedReference.get());
        assertEquals(reference, updatedReference);
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = new DeserializationConfig();
        Boolean supportsUpdate = atomicReferenceDeserializer.supportsUpdate(config);

        assertNotNull(supportsUpdate);
        assertEquals(Boolean.TRUE, supportsUpdate);
    }
}