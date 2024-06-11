package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CreatorPropertyTest {
    @Test
    public void testConstructor() {
        CreatorProperty property = new CreatorProperty(new PropertyName("name"), null, null, null, null, null, 0, null, null);
        
        assertEquals("name", property.getName().getSimpleName());
        assertNull(property.getType());
        assertNull(property.getWrapperName());
        assertNull(property.getTypeDeserializer());
        assertNull(property.getContextAnnotations());
        assertNull(property.getInjectableValueId());
        assertEquals(0, property.getCreatorIndex());
        assertNull(property.getNullValueProvider());
        assertNull(property.getValueDeserializer());
        assertNull(property.getPropertyMetadata());
        assertNull(property.getMember());
        assertNull(property.getContextAnnotations());
        assertNull(property.getFallbackSetter());
        assertEquals(false, property.isIgnorable());
    }

    @Test
    public void testSettersAndGetters() {
        CreatorProperty property = new CreatorProperty(new PropertyName("name"), null, null, null, null, null, 0, null, null);
        property.setName(new PropertyName("newName"));
        property.setType(null);
        property.setWrapperName(null);
        property.setTypeDeserializer(null);
        property.setContextAnnotations(null);
        property.setInjectableValueId(null);
        property.setNullValueProvider(null);
        property.setValueDeserializer(null);
        property.setPropertyMetadata(null);
        property.setMember(null);
        property.setFallbackSetter(null);
        property.setIgnorable(true);
        
        assertEquals("newName", property.getName().getSimpleName());
        assertNull(property.getType());
        assertNull(property.getWrapperName());
        assertNull(property.getTypeDeserializer());
        assertNull(property.getContextAnnotations());
        assertNull(property.getInjectableValueId());
        assertEquals(0, property.getCreatorIndex());
        assertNull(property.getNullValueProvider());
        assertNull(property.getValueDeserializer());
        assertNull(property.getPropertyMetadata());
        assertNull(property.getMember());
        assertNull(property.getContextAnnotations());
        assertNull(property.getFallbackSetter());
        assertEquals(true, property.isIgnorable());
    }

    @Test
    public void testFindInjectableValue() {
        CreatorProperty property = new CreatorProperty(new PropertyName("name"), null, null, null, null, null, 0, "id", null);
        
        // Test with null injectable value id
        try {
            property.findInjectableValue(null, null);
        } catch (Exception e) {
            assertEquals("com.fasterxml.jackson.databind.deser.CreatorProperty", e.getMessage());
        }
    }

    @Test
    public void testSetAndReturn() {
        CreatorProperty property = new CreatorProperty(new PropertyName("name"), null, null, null, null, null, 0, null, null);
        property.setAndReturn(null, null);
    }

    @Test
    public void testToString() {
        CreatorProperty property = new CreatorProperty(new PropertyName("name"), null, null, null, null, null, 0, null, null);
        
        assertEquals("[creator property, name 'name'; inject id 'null']", property.toString());
    }
}