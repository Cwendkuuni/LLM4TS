package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.base.PropertyName;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AtomicReferenceDeserializerTest {

    private AtomicReferenceDeserializer deserializer;
    private Object emptyAtomicReference;
    private Object atomicReferenceWithValue;

    @Before
    public void setUp() throws Exception {
        JavaType fullType = ClassUtil.constructType(AtomicReference.class, Object.class);
        ValueInstantiator inst = context -> new AtomicReference<>();
        TypeDeserializer typeDeser = null;
        JsonDeserializer<?> valueDeser = null;
        deserializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, valueDeser);
        emptyAtomicReference = new AtomicReference<>();
        atomicReferenceWithValue = new Object();
    }

    @Test
    public void testGetNullValue() throws JsonProcessingException {
        assertEquals(deserializer.getNullValue(null), emptyAtomicReference);
    }

    @Test
    public void testGetEmptyValue() {
        assertEquals(deserializer.getEmptyValue(null), emptyAtomicReference);
    }

    @Test
    public void testReferenceValue() throws JsonProcessingException {
        Object content = new Object();
        AtomicReference<Object> reference = deserializer.referenceValue(content);
        assertEquals(content, deserializer.getReferenced(reference));
    }

    @Test
    public void testGetReferenced() throws JsonProcessingException {
        Object referencedValue = deserializer.getReferenced(atomicReferenceWithValue);
        assertEquals(atomicReferenceWithValue, referencedValue);
    }

    @Test
    public void testUpdateReference() throws JsonProcessingException {
        Object newValue = new Object();
        AtomicReference<Object> reference = deserializer.referenceValue(atomicReferenceWithValue);
        deserializer.updateReference(reference, newValue);
        assertEquals(newValue, deserializer.getReferenced(reference));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedUpdate() throws JsonProcessingException {
        AtomicReference<Object> reference = deserializer.referenceValue(atomicReferenceWithValue);
        deserializer.updateReference(reference, atomicReferenceWithValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testNullDeserializer() throws JsonProcessingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFullType() throws JsonProcessingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValueInstantiator() throws JsonProcessingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(null, null, null, null, null);
    }
}