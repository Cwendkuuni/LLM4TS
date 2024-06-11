package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceDeserializerTest {

    @Test
    public void testAtomicReferenceDeserializerConstruction() {
        JavaType fullType = ObjectType.getDefaultInstance();
        ValueInstantiator valueInstantiator = Mockito.mock(ValueInstantiator.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);

        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeserializer, valueDeserializer);

        assertEquals(fullType, atomicReferenceDeserializer.getTargetType());
        assertEquals(valueInstantiator, atomicReferenceDeserializer._valueInstantiator);
        assertEquals(typeDeserializer, atomicReferenceDeserializer._typeDeserializer);
        assertEquals(valueDeserializer, atomicReferenceDeserializer._valueDeserializer);
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        DeserializationContext context = Mockito.mock(DeserializationContext.class);
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(JavaType.getDefaultInstance(), null, null, null);

        assertNotNull(atomicReferenceDeserializer.getNullValue(context));
        assertTrue(atomicReferenceDeserializer.getNullValue(context) instanceof AtomicReference);
    }

    @Test
    public void testGetEmptyValue() {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(JavaType.getDefaultInstance(), null, null, null);

        assertEquals(new AtomicReference<>(), atomicReferenceDeserializer.getEmptyValue(null));
    }

    @Test
    public void testReferenceValue() {
        Object contents = "foo";
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(JavaType.getDefaultInstance(), null, null, null);

        AtomicReference<Object> atomicReference = atomicReferenceDeserializer.referenceValue(contents);

        assertEquals(contents, atomicReference.get());
    }

    @Test
    public void testGetReferenced() throws JsonMappingException {
        AtomicReference<String> atomicReference = new AtomicReference<>("foo");
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(JavaType.getDefaultInstance(), null, null, null);

        assertEquals("foo", atomicReferenceDeserializer.getReferenced(atomicReference));
    }

    @Test
    public void testUpdateReference() throws JsonMappingException {
        AtomicReference<String> atomicReference = new AtomicReference<>("foo");
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(JavaType.getDefaultInstance(), null, null, null);

        Object contents = "bar";
        atomicReferenceDeserializer.updateReference(atomicReference, contents);

        assertEquals("bar", atomicReference.get());
    }

    @Test
    public void testSupportsUpdate() {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(JavaType.getDefaultInstance(), null, null, null);

        assertTrue(atomicReferenceDeserializer.supportsUpdate(null));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetReferencedWithNullAtomicReference() throws JsonMappingException {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(JavaType.getDefaultInstance(), null, null, null);
        AtomicReference<Object> nullAtomicReference = null;

        atomicReferenceDeserializer.getReferenced(nullAtomicReference);
    }

    @Test(expected = IllegalStateException.class)
    public void testUpdateReferenceWithNullAtomicReference() throws JsonMappingException {
        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(JavaType.getDefaultInstance(), null, null, null);
        AtomicReference<Object> nullAtomicReference = null;

        atomicReferenceDeserializer.updateReference(nullAtomicReference, "foo");
    }

    @Test
    public void testWithResolved() {
        JavaType fullType = JavaType.constructType(AtomicReferenceDeserializer.class, Object.class);
        ValueInstantiator valueInstantiator = Mockito.mock(ValueInstantiator.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        AtomicReferenceDeserializer original = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeserializer, valueDeserializer);
        TypeDeserializer typeDeserializer2 = Mockito.mock(TypeDeserializer.class);
        JsonDeserializer<?> valueDeserializer2 = Mockito.mock(JsonDeserializer.class);

        AtomicReferenceDeserializer updated = original.withResolved(typeDeserializer2, valueDeserializer2);

        assertNotSame(original, updated);
        assertEquals(fullType, updated.getTargetType());
        assertEquals(valueInstantiator, updated._valueInstantiator);
        assertEquals(typeDeserializer2, updated._typeDeserializer);
        assertEquals(valueDeserializer2, updated._valueDeserializer);
    }

    @Test
    public void testDeserialize() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType fullType = JavaType.constructType(AtomicReferenceDeserializer.class, Integer.class);
        ValueInstantiator valueInstantiator = Mockito.mock(ValueInstantiator.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        JsonDeserializer<Integer> valueDeserializer = Mockito.mock(JsonDeserializer.class);

        AtomicReferenceDeserializer atomicReferenceDeserializer = new AtomicReferenceDeserializer(fullType, valueInstantiator, typeDeserializer, valueDeserializer);

        ObjectNode node = mapper.createObjectNode();
        node.put("value", 42);

        AtomicReference<Integer> result = mapper.treeToValue(node.deepClone(), atomicReferenceDeserializer, null);

        assertEquals(42, result.get());
    }
}