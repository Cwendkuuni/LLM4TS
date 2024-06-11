package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
                mock(ValueInstantiator.class), mock(TypeDeserializer.class), mock(JsonDeserializer.class));
        assertSame(new AtomicReference<Object>(), deserializer.getNullValue(mock(DeserializationContext.class)));
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
                mock(ValueInstantiator.class), mock(TypeDeserializer.class), mock(JsonDeserializer.class));
        assertSame(new AtomicReference<Object>(), deserializer.getEmptyValue(mock(DeserializationContext.class)));
    }

    @Test
    public void testReferenceValue() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
                mock(ValueInstantiator.class), mock(TypeDeserializer.class), mock(JsonDeserializer.class));

        Object contents = mock(Object.class);
        AtomicReference<Object> reference = new AtomicReference<Object>(contents);

        AtomicReference<Object> result = deserializer.referenceValue(contents);

        assertEquals(reference, result);
    }

    @Test
    public void testGetReferenced() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
                mock(ValueInstantiator.class), mock(TypeDeserializer.class), mock(JsonDeserializer.class));

        Object contents = mock(Object.class);
        AtomicReference<Object> reference = new AtomicReference<Object>(contents);

        when(deserializer.getReferenced(reference)).thenReturn(contents);

        assertEquals(contents, deserializer.getReferenced(reference));
    }

    @Test
    public void testUpdateReference() throws JsonMappingException {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
                mock(ValueInstantiator.class), mock(TypeDeserializer.class), mock(JsonDeserializer.class));

        Object newContents = mock(Object.class);
        AtomicReference<Object> reference = new AtomicReference<Object>(mock(Object.class));

        deserializer.updateReference(reference, newContents);

        assertEquals(newContents, reference.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer deserializer = new AtomicReferenceDeserializer(
                TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
                mock(ValueInstantiator.class), mock(TypeDeserializer.class), mock(JsonDeserializer.class));

        assertTrue(deserializer.supportsUpdate(mock(DeserializationConfig.class)));
    }

    @Test
    public void testWithResolved() {
        AtomicReferenceDeserializer originalDeserializer = new AtomicReferenceDeserializer(
                TypeFactory.defaultInstance.constructType(AtomicReference.class, Object.class),
                mock(ValueInstantiator.class), mock(TypeDeserializer.class), mock(JsonDeserializer.class));

        TypeDeserializer newTypeDeserializer = mock(TypeDeserializer.class);
        JsonDeserializer<Object> newValueDeserializer = mock(JsonDeserializer.class);

        AtomicReferenceDeserializer newDeserializer = originalDeserializer.withResolved(newTypeDeserializer, newValueDeserializer);

        assertSame(originalDeserializer.getTypeDeserializer(), newDeserializer.getTypeDeserializer());
        assertSame(originalDeserializer.getValueInstantiator(), newDeserializer.getValueInstantiator());
        assertSame(newTypeDeserializer, newDeserializer.getTypeDeserializer());
        assertSame(newValueDeserializer, newDeserializer.getValueDeserializer());
    }
}