package com.fasterxml.jackson.databind.deser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorPropertyTest {

    private CreatorProperty property;

    @Before
    public void setUp() {
        property = new CreatorProperty(
                new PropertyName("name"),
                null,
                null,
                null,
                null,
                null,
                0,
                null,
                null
        );
    }

    @Test
    public void testName() {
        assertEquals("name", property.getName().getSimpleName());
    }

    @Test
    public void testType() {
        assertNull(property.getType());
    }

    @Test
    public void testWrapperName() {
        assertNull(property.getWrapperName());
    }

    @Test
    public void testTypeDeserializer() {
        assertNull(property.getTypeDeserializer());
    }

    @Test
    public void testContextAnnotations() {
        assertNull(property.getContextAnnotations());
    }

    @Test
    public void testParam() {
        assertNull(property.getParam());
    }

    @Test
    public void testIndex() {
        assertEquals(0, property.getCreatorIndex());
    }

    @Test
    public void testInjectableValueId() {
        assertNull(property.getInjectableValueId());
    }

    @Test
    public void testMetadata() {
        assertNull(property.getMetadata());
    }

    @Test
    public void testWithNullProvider() {
        assertNull(property.withNullProvider(null));
    }

    @Test
    public void testFixAccess() {
        property.fixAccess(null);
    }

    @Test
    public void testInject() {
        property.inject(null, null);
    }

    @Test
    public void testWithAnnotation() {
        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testSetFallbackSetter() {
        property.setFallbackSetter(null);
    }

    @Test
    public void testMarkAsIgnorable() {
        assertFalse(property.isIgnorable());
        property.markAsIgnorable();
        assertTrue(property.isIgnorable());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        property.deserializeAndSet(null, null, null);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        assertNull(property.deserializeSetAndReturn(null, null, null));
    }

    @Test
    public void testSet() throws Exception {
        property.set(null, null);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        assertNull(property.setAndReturn(null, null));
    }

    @Test
    public void testToString() {
        assertEquals("[creator property, name 'name'; inject id 'null']", property.toString());
    }

}