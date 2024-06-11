package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicReference;

import org.jmock.lib.legacy.ClassImposteriser;
import org.jmock.lib.legacy.MockObjectTestCase;
import org.jmock.lib.legacy.Mock;
import org.jmock.lib.legacy.Rule;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class AtomicReferenceDeserializerTest extends MockObjectTestCase {

    private AtomicReference<Object> ref;
    private DeserializationContext ctxt;
    private TypeDeserializer typeDeser;
    private ValueInstantiator inst;
    private JsonDeserializer<?> deser;
    private TypeDeserializer typeDeserSpy;
    private JsonDeserializer<?> valueDeserSpy;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ClassImposteriser.install(getClass());

        ref = new AtomicReference<Object>();
        ctxt = mock(DeserializationContext.class);
        typeDeser = mock(TypeDeserializer.class);
        inst = mock(ValueInstantiator.class);
        deser = mock(JsonDeserializer.class);

        typeDeserSpy = mockTypeDeserializerSpy(typeDeser);
        valueDeserSpy = mockValueDeserializerSpy(valueDeser);

        AtomicReferenceDeserializer testee = new AtomicReferenceDeserializer(
                getTypeFactory().constructType(AtomicReference.class), inst, typeDeserSpy, valueDeserSpy);
    }

    @SuppressWarnings("unchecked")
    private TypeDeserializer mockTypeDeserializerSpy(TypeDeserializer typeDeser) {
        TypeDeserializer mock = mock(TypeDeserializer.class);
        when(mock. acceptType(mock(Class.class))).thenReturn(typeDeser);
        return (TypeDeserializer) mock;
    }

    @SuppressWarnings("unchecked")
    private JsonDeserializer<?> mockValueDeserializerSpy(JsonDeserializer<?> valueDeser) {
        JsonDeserializer<?> mock = mock(JsonDeserializer.class);
        when(mock.getContainerType(anyObject())).thenReturn(null);
        return (JsonDeserializer<?>) mock;
    }

    // Test constructors
    @Test
    public void testConstructor() {
        AtomicReferenceDeserializer testee = new AtomicReferenceDeserializer(
                getTypeFactory().constructType(AtomicReference.class), inst, typeDeserSpy, valueDeserSpy);
        assertNotNull(testee);
    }

    // Test withResolved method
    @Test
    public void testWithResolved() {
        AtomicReferenceDeserializer testee1 = new AtomicReferenceDeserializer(
                getTypeFactory().constructType(AtomicReference.class), inst, typeDeserSpy, valueDeserSpy);
        AtomicReferenceDeserializer testee2 = testee1.withResolved(typeDeserSpy, valueDeserSpy);
        assertEquals(testee1, testee2);
    }

    // Test getNullValue method
    @Test
    public void testGetNullValue() throws JsonMappingException {
        AtomicReference<Object> result = testee.getNullValue(ctxt);
        assertInstanceOf(AtomicReference.class, result);
        assertEquals(result, ref);
    }

    // Test getEmptyValue method
    @Test
    public void testGetEmptyValue() {
        Object result = testee.getEmptyValue(ctxt);
        assertInstanceOf(AtomicReference.class, result);
        assertEquals(result, ref);
    }

    // Test referenceValue method
    @Test
    public void testReferenceValue() {
        Object contents = mock(Object.class);
        AtomicReference<Object> result = testee.referenceValue(contents);
        assertInstanceOf(AtomicReference.class, result);
        assertEquals(result, ref);
        assertEquals(contents, result.get());
    }

    // Test getReferenced method
    @Test
    public void testGetReferenced() throws Exception {
        Object containedValue = new Object();
        ref.set(containedValue);

        Object result = testee.getReferenced(ref);
        assertEquals(result, containedValue);
    }

    // Test updateReference method
    @Test
    public void testUpdateReference() throws Exception {
        Object newValue = new Object();

        ref.set(null);
        Object result = testee.updateReference(ref, newValue);
        assertInstanceOf(AtomicReference.class, result);
        assertEquals(newValue, result.get());

        assertEquals(newValue, ref.get());
    }

    // Test supportsUpdate method
    @Test
    public void testSupportsUpdate() {
        assertTrue(testee.supportsUpdate(ctxt.getConfig()));
    }

    // Test edge cases

    // Test getNullValue method with null ctxt
    @Test(expected = IllegalArgumentException.class)
    public void testGetNullValueWithNullContext() throws JsonMappingException {
        testee.getNullValue(null);
    }

    // Test getEmptyValue method with null ctxt
    @Test(expected = IllegalArgumentException.class)
    public void testGetEmptyValueWithNullContext() {
        testee.getEmptyValue(null);
    }

    // Test referenceValue method with null contents
    @Test(expected = IllegalArgumentException.class)
    public void testReferenceValueWithNullContents() {
        testee.referenceValue(null);
    }

    // Test getReferenced method with null ref
    @Test(expected = IllegalArgumentException.class)
    public void testGetReferencedWithNullReference() throws Exception {
        testee.getReferenced(null);
    }

    // Test updateReference method with null ref
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateReferenceWithNullReference() throws Exception {
        testee.updateReference(null, new Object());
    }

    // Test updateReference method with non-AtomicReference type
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateReferenceWithNonAtomicReferenceType() throws Exception {
        testee = new AtomicReferenceDeserializer(
                getTypeFactory().constructType(Integer.class), inst, typeDeserSpy, valueDeserSpy);

        testee.updateReference(ref, 42);
    }

    // Test updateReference method with null new value
    @Test(expected = IllegalArgumentException.class)
    public void testUpdateReferenceWithNullNewValue() throws Exception {
        testee.updateReference(ref, null);
    }

    // Test exception handling

    // Test JsonMappingException when getNullValue fails
    @Test(expected = JsonMappingException.class)
    public void testGetNullValueThrowsJsonMappingException() throws JsonMappingException {
        when(typeDeserSpy.acceptType(anyObject()))
                .thenThrow(new JsonMappingException("Expected exception"));

        testee = new AtomicReferenceDeserializer(
                getTypeFactory().constructType(AtomicReference.class), inst, typeDeserSpy, valueDeserSpy);

        testee.getNullValue(ctxt);
    }

    // Test JsonMappingException when referenceValue fails
    @Test(expected = JsonMappingException.class)
    public void testReferenceValueThrowsJsonMappingException() throws JsonMappingException {
        when(inst.findValueDeserializer(anyObject(), anyObject()))
                .thenThrow(new JsonMappingException("Expected exception"));

        testee = new AtomicReferenceDeserializer(
                getTypeFactory().constructType(AtomicReference.class), inst, typeDeserSpy, valueDeserSpy);

        testee.referenceValue(mock(Object.class));
    }

    // Test JsonMappingException when getReferenced fails
    @Test(expected = JsonMappingException.class)
    public void testGetReferencedThrowsJsonMappingException() throws Exception {
        when(ref.get()).thenThrow(new JsonMappingException("Expected exception"));

        testee = new AtomicReferenceDeserializer(
                getTypeFactory().constructType(AtomicReference.class), inst, typeDeserSpy, valueDeserSpy);

        testee.getReferenced(ref);
    }

    // Test JsonMappingException when updateReference fails
    @Test(expected = JsonMappingException.class)
    public void testUpdateReferenceThrowsJsonMappingException() throws Exception {
        when(ref.set(anyObject())).thenThrow(new JsonMappingException("Expected exception"));

        testee = new AtomicReferenceDeserializer(
                getTypeFactory().constructType(AtomicReference.class), inst, typeDeserSpy, valueDeserSpy);

        testee.updateReference(ref, new Object());
    }
}