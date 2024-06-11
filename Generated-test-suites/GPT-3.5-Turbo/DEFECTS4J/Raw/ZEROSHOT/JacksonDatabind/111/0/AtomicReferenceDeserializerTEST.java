package com.fasterxml.jackson.databind.deser.std;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AtomicReferenceDeserializerTest {

    @Mock
    DeserializationContext deserializationContextMock;

    @Mock
    JavaType javaTypeMock;

    @Mock
    ValueInstantiator valueInstantiatorMock;

    @Mock
    TypeDeserializer typeDeserializerMock;

    @Mock
    JsonDeserializer<?> jsonDeserializerMock;

    @Mock
    AtomicReference<Object> atomicReferenceMock;

    @Test
    public void testWithResolved() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(javaTypeMock, valueInstantiatorMock,
                typeDeserializerMock, jsonDeserializerMock);
        AtomicReferenceDeserializer resolvedDeserializer = deserializer.withResolved(typeDeserializerMock, jsonDeserializerMock);
        assertNotNull(resolvedDeserializer);
        assertEquals(javaTypeMock, resolvedDeserializer._fullType);
        assertEquals(valueInstantiatorMock, resolvedDeserializer._valueInstantiator);
        assertEquals(typeDeserializerMock, resolvedDeserializer._valueTypeDeserializer);
        assertEquals(jsonDeserializerMock, resolvedDeserializer._valueDeserializer);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(javaTypeMock, valueInstantiatorMock,
                typeDeserializerMock, jsonDeserializerMock);
        AtomicReference<Object> nullValue = deserializer.getNullValue(deserializationContextMock);
        assertNotNull(nullValue);
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(javaTypeMock, valueInstantiatorMock,
                typeDeserializerMock, jsonDeserializerMock);
        Object emptyValue = deserializer.getEmptyValue(deserializationContextMock);
        assertNotNull(emptyValue);
    }

    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(javaTypeMock, valueInstantiatorMock,
                typeDeserializerMock, jsonDeserializerMock);
        Object contents = new Object();
        AtomicReference<Object> referenceValue = deserializer.referenceValue(contents);
        assertNotNull(referenceValue);
        assertEquals(contents, referenceValue.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(javaTypeMock, valueInstantiatorMock,
                typeDeserializerMock, jsonDeserializerMock);
        Object contents = new Object();
        when(atomicReferenceMock.get()).thenReturn(contents);
        Object referencedValue = deserializer.getReferenced(atomicReferenceMock);
        assertNotNull(referencedValue);
        assertEquals(contents, referencedValue);
    }

    @Test
    public void testUpdateReference() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(javaTypeMock, valueInstantiatorMock,
                typeDeserializerMock, jsonDeserializerMock);
        Object contents = new Object();
        AtomicReference<Object> updatedReference = deserializer.updateReference(atomicReferenceMock, contents);
        assertNotNull(updatedReference);
        Mockito.verify(atomicReferenceMock).set(contents);
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(javaTypeMock, valueInstantiatorMock,
                typeDeserializerMock, jsonDeserializerMock);
        Boolean supportsUpdate = deserializer.supportsUpdate(deserializationConfigMock);
        assertEquals(Boolean.TRUE, supportsUpdate);
    }
}