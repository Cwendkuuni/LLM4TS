package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Before;
import org.junit.Test;

public class AtomicReferenceDeserializerTest {

    private DeserializationContext context;
    private ValueInstantiator instantiator;
    private TypeDeserializer typeDeserializer;
    private JsonDeserializer<Object> valueDeserializer;
    private AtomicReferenceDeserializer deserializer;

    @Before
    public void setUp() {
        context = mock(DeserializationContext.class);
        instantiator = mock(ValueInstantiator.class);
        typeDeserializer = mock(TypeDeserializer.class);
        valueDeserializer = mock(JsonDeserializer.class);
        when(instantiator.createValueInstance(context)).thenCallRealMethod();
        deserializer = new AtomicReferenceDeserializer(
                new BasicValueType(AtomicReference.class), instantiator, typeDeserializer, valueDeserializer);
    }

    @Test
    public void testNewInstance() {
        assertNotNull(deserializer);
    }

    @Test
    public void testWithResolved() {
        AtomicReferenceDeserializer newDeserializer = deserializer.withResolved(typeDeserializer, valueDeserializer);
        assertNotNull(newDeserializer);
        assertEquals(deserializer.getClass(), newDeserializer.getClass());
    }

    @Test
    public void testGetNullValue() {
        AtomicReference<Object> reference = deserializer.getNullValue(context);
        assertNotNull(reference);
        assertEquals(AtomicReference.class, reference.getClass());
        assertNull(reference.get());
    }

    @Test
    public void testGetEmptyValue() {
        Object emptyValue = deserializer.getEmptyValue(context);
        assertNotNull(emptyValue);
        assertEquals(AtomicReference.class, emptyValue.getClass());
        assertNull(((AtomicReference<Object>) emptyValue).get());
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReference<Object> reference = deserializer.referenceValue(contents);
        assertNotNull(reference);
        assertEquals(AtomicReference.class, reference.getClass());
        assertEquals(contents, reference.get());
    }

    @Test
    public void testGetReferenced() {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        Object referenced = deserializer.getReferenced(reference);
        assertNotNull(referenced);
        assertEquals(reference.get(), referenced);
    }

    @Test
    public void testUpdateReference() {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        Object newValue = new Object();
        deserializer.updateReference(reference, newValue);
        assertEquals(newValue, reference.get());
    }

    @Test
    public void testSupportsUpdate() {
        Boolean result = deserializer.supportsUpdate(mock(DeserializationConfig.class));
        assertTrue(result);
    }
}