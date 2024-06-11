
package com.fasterxml.jackson.databind.deser;

import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorPropertyTest {

    // Test cases for CreatorProperty constructor

    @Test
    public void testConstructor() {
        // Test with valid parameters
        CreatorProperty property = new CreatorProperty(
                new PropertyName("name"), JavaType.INT_TYPE, null,
                null, null, null, 0, null,
                PropertyMetadata.STD_REQUIRED);
        assertNotNull(property);
        assertEquals("name", property.getName().getSimpleName());
        assertEquals(JavaType.INT_TYPE, property.getType());
        assertNull(property.getWrapperName());
        assertNull(property.getTypeDeserializer());
        assertNull(property.getContextualAnnotations());
        assertNull(property.getAnnotated());
        assertEquals(0, property.getCreatorIndex());
        assertNull(property.getInjectableValueId());
        assertEquals(PropertyMetadata.STD_REQUIRED, property.getMetadata());
    }

    // Test cases for withName method

    @Test
    public void testWithName() {
        CreatorProperty property = new CreatorProperty(
                new PropertyName("name"), JavaType.INT_TYPE, null,
                null, null, null, 0, null,
                PropertyMetadata.STD_REQUIRED);

        // Test with new name
        CreatorProperty newProperty = property.withName(new PropertyName("newName"));
        assertNotNull(newProperty);
        assertEquals("newName", newProperty.getName().getSimpleName());
        assertEquals(JavaType.INT_TYPE, newProperty.getType());
        assertNull(newProperty.getWrapperName());
        assertNull(newProperty.getTypeDeserializer());
        assertNull(newProperty.getContextualAnnotations());
        assertNull(newProperty.getAnnotated());
        assertEquals(0, newProperty.getCreatorIndex());
        assertNull(newProperty.getInjectableValueId());
        assertEquals(PropertyMetadata.STD_REQUIRED, newProperty.getMetadata());
    }

    // Test cases for withValueDeserializer method

    @Test
    public void testWithValueDeserializer() {
        CreatorProperty property = new CreatorProperty(
                new PropertyName("name"), JavaType.INT_TYPE, null,
                null, null, null, 0, null,
                PropertyMetadata.STD_REQUIRED);

        // Test with the same deserializer
        CreatorProperty sameProperty = property.withValueDeserializer(property.getValueDeserializer());
        assertSame(property, sameProperty);

        // Test with a new deserializer
        JsonDeserializer<?> newDeserializer = new CustomDeserializer();
        CreatorProperty newProperty = property.withValueDeserializer(newDeserializer);
        assertNotSame(property, newProperty);
        assertEquals(newDeserializer, newProperty.getValueDeserializer());
    }

    private static class CustomDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}
