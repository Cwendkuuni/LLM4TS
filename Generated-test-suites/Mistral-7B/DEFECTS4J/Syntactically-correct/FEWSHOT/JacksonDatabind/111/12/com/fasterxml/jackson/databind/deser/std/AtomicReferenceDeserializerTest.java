package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyValueProvider;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testNewInstance() {
        JavaType fullType = mock(JavaType.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);

        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeserializer, valueDeserializer);

        assertNotNull(atomicReferenceDeserializer);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext context = mock(DeserializationContext.class);
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(mock(JavaType.class), mock(ValueInstantiator.class), mock(TypeDeserializer.class), mock(JsonDeserializer.class));

        AtomicReference<Object> nullValue = atomicReferenceDeserializer.getNullValue(context);

        assertNotNull(nullValue);
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(mock(JavaType.class), mock(ValueInstantiator.class), mock(TypeDeserializer.class), mock(JsonDeserializer.class));

        Object emptyValue = atomicReferenceDeserializer.getEmptyValue(mock(DeserializationContext.class));

        assertNotNull(emptyValue);
        EmptyValueProvider emptyValueProvider = (EmptyValueProvider) atomicReferenceDeserializer.getTypeDeserializer();
        assertNull(emptyValueProvider.createEmptyValue(mock(DeserializationContext.class)));
    }

    @Test
    public void testReferenceValue() {
        Object contents = new Object();
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(mock(JavaType.class), mock(ValueInstantiator.class), mock(TypeDeserializer.class), mock(JsonDeserializer.class));

        AtomicReference<Object> referenceValue = atomicReferenceDeserializer.referenceValue(contents);

        assertNotNull(referenceValue);
        assertEquals(contents, referenceValue.get());
    }

    @Test
    public void testGetReferenced() {
        Object referencedObject = new Object();
        AtomicReference<Object> reference = new AtomicReference<Object>(referencedObject);
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(mock(JavaType.class), mock(ValueInstantiator.class), mock(TypeDeserializer.class), mock(JsonDeserializer.class));

        Object getReferenced = atomicReferenceDeserializer.getReferenced(reference);

        assertNotNull(getReferenced);
        assertEquals(referencedObject, getReferenced);
    }

    @Test
    public void testUpdateReference() {
        Object newValue = new Object();
        AtomicReference<Object> reference = new AtomicReference<Object>(mock(Object.class));
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(mock(JavaType.class), mock(ValueInstantiator.class), mock(TypeDeserializer.class), mock(JsonDeserializer.class));

        AtomicReference<Object> updatedReference = atomicReferenceDeserializer.updateReference(reference, newValue);

        assertNotNull(updatedReference);
        assertEquals(reference, updatedReference);
        assertEquals(newValue, reference.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(mock(JavaType.class), mock(ValueInstantiator.class), mock(TypeDeserializer.class), mock(JsonDeserializer.class));

        assertTrue(atomicReferenceDeserializer.supportsUpdate(mock(DeserializationConfig.class)));
    }
}