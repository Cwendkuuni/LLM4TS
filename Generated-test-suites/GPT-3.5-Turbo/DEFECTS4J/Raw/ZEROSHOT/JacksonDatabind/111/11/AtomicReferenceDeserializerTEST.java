
package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AtomicReferenceDeserializerTest {

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private DeserializationConfig deserializationConfig;

    @Mock
    private JavaType javaType;

    @Mock
    private ValueInstantiator valueInstantiator;

    @Mock
    private TypeDeserializer typeDeserializer;

    @Mock
    private JsonDeserializer<?> jsonDeserializer;

    private AtomicReferenceDeserializer atomicReferenceDeserializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        atomicReferenceDeserializer = new AtomicReferenceDeserializer(javaType, valueInstantiator, typeDeserializer, jsonDeserializer);
    }

    @Test
    public void testWithResolved() {
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<?> valueDeser = mock(JsonDeserializer.class);
        AtomicReferenceDeserializer result = atomicReferenceDeserializer.withResolved(typeDeser, valueDeser);
        assertEquals(javaType, result._fullType);
        assertEquals(valueInstantiator, result._valueInstantiator);
        assertEquals(typeDeser, result._valueTypeDeserializer);
        assertEquals(valueDeser, result._valueDeserializer);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReference<Object> result = atomicReferenceDeserializer.getNullValue(deserializationContext);
        AtomicReference<Object> expected = new AtomicReference<>();
        assertEquals(expected, result);
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReference<Object> result = atomicReferenceDeserializer.getEmptyValue(deserializationContext);
        AtomicReference<Object> expected = new AtomicReference<>();
        assertEquals(expected, result);
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReference<Object> result = atomicReferenceDeserializer.referenceValue(contents);
        AtomicReference<Object> expected = new AtomicReference<>(contents);
        assertEquals(expected, result);
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<>(new Object());
        Object result = atomicReferenceDeserializer.getReferenced(reference);
        Object expected = reference.get();
        assertEquals(expected, result);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<>();
        Object contents = new Object();
        AtomicReference<Object> result = atomicReferenceDeserializer.updateReference(reference, contents);
        AtomicReference<Object> expected = reference;
        expected.set(contents);
        assertEquals(expected, result);
    }

    @Test
    public void testSupportsUpdate() {
        Boolean result = atomicReferenceDeserializer.supportsUpdate(deserializationConfig);
        assertEquals(Boolean.TRUE, result);
    }
}
