package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.AtomicReferenceDeserializer;
import com.fasterxml.jackson.databind.deser.std.ReferenceTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AtomicReferenceDeserializerTest {

    private AtomicReferenceDeserializer atomicReferenceDeserializer;

    @Mock
    private JavaType javaType;
    
    @Mock
    private ValueInstantiator valueInstantiator;

    @Mock
    private TypeDeserializer typeDeserializer;

    @Mock
    private JsonDeserializer<?> jsonDeserializer;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private AtomicReference<Object> atomicReference;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(javaType, valueInstantiator, typeDeserializer, jsonDeserializer);
    }

    @Test
    public void testWithResolved() {
        TypeDeserializer newTypeDeserializer = mock(TypeDeserializer.class);
        JsonDeserializer<?> newValueDeserializer = mock(JsonDeserializer.class);

        AtomicReferenceDeserializer newAtomicReferenceDeserializer = atomicReferenceDeserializer.withResolved(newTypeDeserializer, newValueDeserializer);

        assertSame(javaType, newAtomicReferenceDeserializer._fullType);
        assertSame(valueInstantiator, newAtomicReferenceDeserializer._valueInstantiator);
        assertSame(newTypeDeserializer, newAtomicReferenceDeserializer._valueInstantiator);
        assertSame(newValueDeserializer, newAtomicReferenceDeserializer._jsonDeserializer);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReference<Object> nullValue = atomicReferenceDeserializer.getNullValue(deserializationContext);

        assertNotNull(nullValue);
    }

    @Test
    public void testGetEmptyValue() {
        Object emptyValue = atomicReferenceDeserializer.getEmptyValue(deserializationContext);

        assertNotNull(emptyValue);
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();

        AtomicReference<Object> referenceValue = atomicReferenceDeserializer.referenceValue(contents);

        assertNotNull(referenceValue);
        assertSame(contents, referenceValue.get());
    }

    @Test
    public void testGetReferenced() {
        Object contents = new Object();

        Object referenced = atomicReferenceDeserializer.getReferenced(new AtomicReference<>(contents));

        assertSame(contents, referenced);
    }

    @Test
    public void testUpdateReference() {
        Object contents = new Object();

        AtomicReference<Object> reference = new AtomicReference<>();

        AtomicReference<Object> updatedReference = atomicReferenceDeserializer.updateReference(reference, contents);

        assertNotNull(updatedReference);
        assertSame(contents, updatedReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        assertTrue(atomicReferenceDeserializer.supportsUpdate(null));
    }
}