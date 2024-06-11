package com.fasterxml.jackson.databind.deser.std;

import static com.fasterxml.jackson.core.util.TestUtils.convertObjectToValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyValueProvider;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testConstructor() {
        JavaType fullType = mock(JavaType.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeser, deser);
        assertNotNull(serializer);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(mock(JavaType.class), null, null, null);
        assertNotNull(serializer.getNullValue(ctxt));
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(mock(JavaType.class), null, null, null);
        assertEquals(new AtomicReference<Object>(), serializer.getEmptyValue(null));
    }

    @Test
    public void testReferenceValue() {
        Object contents = "test";
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(mock(JavaType.class), null, null, null);
        AtomicReference<Object> atomicReference = serializer.referenceValue(contents);
        assertEquals(contents, atomicReference.get());
    }

    @Test
    public void testGetReferenced() throws IOException {
        AtomicReference<Object> atomicReference = new AtomicReference<Object>("test");
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(mock(JavaType.class), null, null, null);
        assertEquals("test", serializer.getReferenced(atomicReference));
    }

    @Test
    public void testUpdateReference() throws IOException {
        AtomicReference<Object> atomicReference = new AtomicReference<Object>("test");
        Object newValue = "newTest";
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(mock(JavaType.class), null, null, null);
        serializer.updateReference(atomicReference, newValue);
        assertEquals(newValue, atomicReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(mock(JavaType.class), null, null, null);
        when(serializer.supportsUpdate(config)).thenReturn(true);
        assertTrue(serializer.supportsUpdate(mock(DeserializationContext.class)));
    }

    @Test
    public void testWithResolved() {
        JavaType fullType = mock(JavaType.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeser, deser);
        AtomicReferenceDeserializer newSerializer = serializer.withResolved(mock(TypeDeserializer.class), mock(JsonDeserializer.class));
        assertEquals(serializer.getClass(), serializer.getClass());
        assertEquals(newSerializer.getClass(), newSerializer.getClass());
        assertEquals(serializer._fullType, newSerializer._fullType);
        assertEquals(serializer._valueInstantiator, newSerializer._valueInstantiator);
        assertEquals(serializer._typeDeser, newSerializer._typeDeser);
        assertEquals(serializer._valueDeser, newSerializer._valueDeser);
    }

    @Test
    public void testCreateEmptyInstance() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        DeserializationConfig config = mock(DeserializationConfig.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(mock(JavaType.class), typeDeser, deser, null);
        AtomicReference<Object> result = serializer.createEmptyValue(ctxt, config);
        assertEquals(new AtomicReference<Object>(), result);
    }

    @Test
    public void testCreateValue() throws IOException {
        JavaType valueType = mock(JavaType.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        DeserializationConfig config = mock(DeserializationConfig.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        Object value = "test";
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(valueType, typeDeser, deser, null);
        AtomicReference<Object> result = serializer.createValue(ctxt, config, value, null, null, null, null);
        assertEquals(value, result.get());
    }

    @Test
    public void testCreateValueWithValueDeserializer() throws IOException {
        JavaType valueType = mock(JavaType.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        DeserializationConfig config = mock(DeserializationConfig.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        JsonDeserializer<Object> valueDeser = mock(JsonDeserializer.class);
        Object value = "test";
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(valueType, typeDeser, deser, valueDeser);
        AtomicReference<Object> result = serializer.createValue(ctxt, config, value, null, null, null, null);
        assertEquals(value, result.get());
    }

    @Test
    public void testCreateValueWithEmptyValueProvider() throws IOException {
        JavaType valueType = mock(JavaType.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        DeserializationConfig config = mock(DeserializationConfig.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        EmptyValueProvider emptyValueProvider = mock(EmptyValueProvider.class);
        AtomicReferenceDeserializer serializer = new AtomicReferenceDeserializer(valueType, typeDeser, deser, null);
        AtomicReference<Object> result = serializer.createValue(ctxt, config, null, emptyValueProvider, null, null, null);
        assertNull(result.get());
    }
}