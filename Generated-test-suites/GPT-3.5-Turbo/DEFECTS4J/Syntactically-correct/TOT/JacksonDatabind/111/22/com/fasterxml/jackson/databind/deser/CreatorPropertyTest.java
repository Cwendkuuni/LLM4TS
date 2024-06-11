package com.fasterxml.jackson.databind.deser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorPropertyTest {

    private CreatorProperty creatorProperty;

    @Before
    public void setUp() {
        creatorProperty = new CreatorProperty(
                new PropertyName("name"),
                JavaType.INT_TYPE,
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
    public void testGetName() {
        assertEquals("name", creatorProperty.getName().getSimple());
    }

    @Test
    public void testGetType() {
        assertEquals(JavaType.INT_TYPE, creatorProperty.getType());
    }

    @Test
    public void testGetWrapperName() {
        assertNull(creatorProperty.getWrapperName());
    }

    @Test
    public void testGetTypeDeserializer() {
        assertNull(creatorProperty.getTypeDeserializer());
    }

    @Test
    public void testGetContextAnnotations() {
        assertNull(creatorProperty.getContextAnnotations());
    }

    @Test
    public void testGetMetadata() {
        assertNull(creatorProperty.getMetadata());
    }

    @Test
    public void testGetMember() {
        assertNull(creatorProperty.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        assertEquals(0, creatorProperty.getCreatorIndex());
    }

    @Test
    public void testGetInjectableValueId() {
        assertNull(creatorProperty.getInjectableValueId());
    }

    @Test
    public void testSetFallbackSetter() {
        CreatorProperty fallbackSetter = new CreatorProperty(
                new PropertyName("fallbackSetter"),
                JavaType.INT_TYPE,
                null,
                null,
                null,
                null,
                1,
                null,
                null
        );
        creatorProperty.setFallbackSetter(fallbackSetter);
        assertEquals(fallbackSetter, creatorProperty._fallbackSetter);
    }

    @Test
    public void testMarkAsIgnorable() {
        assertFalse(creatorProperty.isIgnorable());
        creatorProperty.markAsIgnorable();
        assertTrue(creatorProperty.isIgnorable());
    }

    @Test
    public void testDeserializeAndSet() {
        // TODO: Implement test case
    }

    @Test
    public void testDeserializeSetAndReturn() {
        // TODO: Implement test case
    }

    @Test
    public void testSet() {
        // TODO: Implement test case
    }

    @Test
    public void testSetAndReturn() {
        // TODO: Implement test case
    }

    @Test
    public void testFindInjectableValue() {
        // TODO: Implement test case
    }

    @Test
    public void testInject() {
        // TODO: Implement test case
    }

    @Test
    public void testGetAnnotation() {
        // TODO: Implement test case
    }

    @Test
    public void testWithNullProvider() {
        // TODO: Implement test case
    }

    @Test
    public void testFixAccess() {
        // TODO: Implement test case
    }

    @Test
    public void testWithNullValueProvider() {
        // TODO: Implement test case
    }

    @Test
    public void testWithNullValueProvider() {
        // TODO: Implement test case
    }

    @Test
    public void testToString() {
        assertEquals("[creator property, name 'name'; inject id 'null']", creatorProperty.toString());
    }

    @Test
    public void testVerifySetter() {
        // TODO: Implement test case
    }

    @Test
    public void testReportMissingSetter() {
        // TODO: Implement test case
    }

}