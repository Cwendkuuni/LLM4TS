package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.concurrent.atomic.AtomicReference;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AtomicReference.class})
public class AtomicReferenceDeserializerTest {

    @Test
    public void testNewInstance() throws Exception {
        JavaType fullType = Mockito.mock(JavaType.class);
        ValueInstantiator inst = Mockito.mock(ValueInstantiator.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);

        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
        assertNotNull(serializer);
    }

    @Test
    public void testWithResolved() throws Exception {
        JavaType fullType = Mockito.mock(JavaType.class);
        ValueInstantiator inst = Mockito.mock(ValueInstantiator.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        JsonDeserializer<?> valueDeser = Mockito.mock(JsonDeserializer.class);

        AtomicReferenceDeserializer serializer1 = new AtomicReferenceDeserializer(fullType, inst, typeDeser, deser);
        AtomicReferenceDeserializer serializer2 = serializer1.withResolved(typeDeser, valueDeser);

        assertSame(serializer1.getClass(), serializer2.getClass());
    }

    @Test
    public void testGetNullValue() throws Exception {
        DeserializationContext ctxt = PowerMockito.mock(DeserializationContext.class);
        AtomicReference<Object> result = AtomicReferenceDeserializer.getNullValue(ctxt);
        assertNotNull(result);
        assertNull(result.get());
    }

    @Test
    public void testGetEmptyValue() throws Exception {
        DeserializationContext ctxt = PowerMockito.mock(DeserializationContext.class);
        Object result = AtomicReferenceDeserializer.getEmptyValue(ctxt);
        assertNotNull(result);
        assertInstanceOf(AtomicReference.class, result);
        assertNull(((AtomicReference<Object>) result).get());
    }

    @Test
    public void testReferenceValue() throws Exception {
        Object contents = new Object();
        DeserializationContext ctxt = PowerMockito.mock(DeserializationContext.class);
        AtomicReference<Object> result = AtomicReferenceDeserializer.referenceValue(contents);
        assertNotNull(result);
        assertInstanceOf(AtomicReference.class, result);
        assertSame(contents, result.get());
    }

    @Test
    public void testGetReferenced() throws Exception {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        DeserializationContext ctxt = PowerMockito.mock(DeserializationContext.class);
        Object result = AtomicReferenceDeserializer.getReferenced(reference);
        assertNotNull(result);
        assertSame(reference.get(), result);
    }

    @Test
    public void testUpdateReference() throws Exception {
        AtomicReference<Object> reference = new AtomicReference<Object>(new Object());
        Object newValue = new Object();
        DeserializationContext ctxt = PowerMockito.mock(DeserializationContext.class);

        AtomicReference<Object> updatedReference = AtomicReferenceDeserializer.updateReference(reference, newValue);
        assertSame(reference, updatedReference);
        assertSame(newValue, reference.get());
    }

    @Test
    public void testSupportsUpdate() throws Exception {
        DeserializationConfig config = PowerMockito.mock(DeserializationConfig.class);
        assertTrue(AtomicReferenceDeserializer.supportsUpdate(config));
    }

    @Test
    public void testDeserialize() throws Exception {
        ObjectNode node = PowerMockito.mock(ObjectNode.class);
        JsonParser parser = PowerMockito.mock(JsonParser.class);
        JavaType fullType = PowerMockito.mock(JavaType.class);
        ValueInstantiator inst = PowerMockito.mock(ValueInstantiator.class);
        TypeDeserializer typeDeser = PowerMockito.mock(TypeDeserializer.class);
        JsonDeserializer<?> deser = PowerMockito.mock(JsonDeserializer.class);

        AtomicReference<Object> expected = new AtomicReference<Object>(new Object());
        when(deser.deserialize(any(JsonParser.class), any(DeserializationContext.class), any(TypeDeserializer.class), any(TypeReference.class)))
                .thenReturn(expected);

        AtomicReference<Object> result = AtomicReferenceDeserializer.deserialize(node, parser, fullType, inst, typeDeser, deser);
        assertSame(expected, result);
    }
}