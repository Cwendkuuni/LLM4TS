package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CreatorPropertyTest {
    @Test
    public void testConstructor() {
        // Test with null parameters
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        assertNull(property.getName());
        assertNull(property.getType());
        assertNull(property.getWrapperName());
        assertNull(property.getTypeDeserializer());
        assertNull(property.getContextAnnotations());
        assertNull(property.getMember());
        assertEquals(0, property.getCreatorIndex());
        assertNull(property.getInjectableValueId());
        assertNull(property.getNullProvider());
        assertNull(property.getValueDeserializer());

        // Test with non-null parameters
        property = new CreatorProperty(new PropertyName("name"), null, new PropertyName("wrapper"), null, null, null, 1, "id", null);
        assertEquals("name", property.getName().getSimpleName());
        assertEquals("wrapper", property.getWrapperName().getSimpleName());
        assertEquals(1, property.getCreatorIndex());
        assertEquals("id", property.getInjectableValueId());

        // Test with null deserializer and value provider
        property = new CreatorProperty(null, null, "id");
        assertNull(property.getValueDeserializer());
        assertNull(property.getNullProvider());
    }

    @Test
    public void testWithMethods() {
        CreatorProperty property = new CreatorProperty(null, null, "id");
        CreatorProperty newProperty = property.withName(new PropertyName("name"));
        assertEquals("name", newProperty.getName().getSimpleName());

        newProperty = property.withValueDeserializer(null);
        assertNull(newProperty.getValueDeserializer());

        newProperty = property.withNullProvider(null);
        assertNull(newProperty.getNullProvider());
    }

    @Test
    public void testFindInjectableValue() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, "id", null);
        assertNull(property.findInjectableValue(null, null));
    }

    @Test
    public void testInject() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, "id", null);
        property.inject(null, null);
    }

    @Test
    public void testGetAnnotation() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, "id", null);
        assertNull(property.getAnnotation(null));
    }

    @Test
    public void testGetCreatorIndex() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, "id", null);
        assertEquals(0, property.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        property.deserializeAndSet(null, null, null);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        assertNull(property.deserializeSetAndReturn(null, null, null));
    }

    @Test
    public void testSet() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        property.set(null, null);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        assertNull(property.setAndReturn(null, null));
    }

    @Test
    public void testGetInjectableValueId() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, "id", null);
        assertEquals("id", property.getInjectableValueId());
    }

    @Test
    public void testToString() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, "id", null);
        assertEquals("[creator property, name 'null'; inject id 'id']", property.toString());
    }
}