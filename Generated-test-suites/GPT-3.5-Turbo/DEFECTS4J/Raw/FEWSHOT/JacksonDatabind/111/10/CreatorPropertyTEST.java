package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CreatorPropertyTest {
    @Test
    public void testConstructor() {
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("name"), 
                JavaType.construct(String.class), 
                PropertyName.construct("wrapper"), 
                null, 
                null, 
                null, 
                0, 
                null, 
                null
        );
        
        assertNotNull(property);
        assertEquals("name", property.getName().getSimpleName());
        assertEquals(String.class, property.getType().getRawClass());
        assertEquals("wrapper", property.getWrapperName().getSimpleName());
        assertNull(property.getTypeDeserializer());
        assertNull(property.getContextAnnotations());
        assertNull(property.getMember());
        assertEquals(0, property.getCreatorIndex());
        assertNull(property.getInjectableValueId());
        assertNull(property.getMetadata());
    }

    @Test
    public void testSetAndGetFallbackSetter() {
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("name"), 
                JavaType.construct(String.class), 
                PropertyName.construct("wrapper"), 
                null, 
                null, 
                null, 
                0, 
                null, 
                null
        );
        
        SettableBeanProperty fallbackSetter = new SettableBeanProperty(
                PropertyName.construct("setter"), 
                JavaType.construct(String.class), 
                PropertyName.construct("wrapper"), 
                null, 
                null, 
                null
        );
        
        property.setFallbackSetter(fallbackSetter);
        assertEquals(fallbackSetter, property.getFallbackSetter());
    }

    @Test
    public void testMarkAsIgnorable() {
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("name"), 
                JavaType.construct(String.class), 
                PropertyName.construct("wrapper"), 
                null, 
                null, 
                null, 
                0, 
                null, 
                null
        );
        
        assertFalse(property.isIgnorable());
        property.markAsIgnorable();
        assertTrue(property.isIgnorable());
    }

    @Test
    public void testFindInjectableValue() throws JsonMappingException {
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("name"), 
                JavaType.construct(String.class), 
                PropertyName.construct("wrapper"), 
                null, 
                null, 
                null, 
                0, 
                "id", 
                null
        );
        
        DeserializationContext context = new DeserializationContext(null, null);
        Object beanInstance = new Object();
        
        try {
            property.findInjectableValue(context, beanInstance);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("Property 'name' (type com.fasterxml.jackson.databind.deser.CreatorProperty) has no injectable value id configured", e.getMessage());
        }
    }

    @Test
    public void testInject() throws IOException {
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("name"), 
                JavaType.construct(String.class), 
                PropertyName.construct("wrapper"), 
                null, 
                null, 
                null, 
                0, 
                "id", 
                null
        );
        
        DeserializationContext context = new DeserializationContext(null, null);
        Object beanInstance = new Object();
        
        try {
            property.inject(context, beanInstance);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("No fallback setter/field defined for creator property 'name'", e.getMessage());
        }
    }

    @Test
    public void testWithMethods() {
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("name"), 
                JavaType.construct(String.class), 
                PropertyName.construct("wrapper"), 
                null, 
                null, 
                null, 
                0, 
                null, 
                null
        );
        
        CreatorProperty newProperty = property.withName(PropertyName.construct("newName"));
        assertEquals("newName", newProperty.getName().getSimpleName());
        
        CreatorProperty newProperty2 = property.withValueDeserializer(null);
        assertNull(newProperty2.getValueDeserializer());
        
        CreatorProperty newProperty3 = property.withNullProvider(null);
        assertNull(newProperty3.getNullProvider());
    }
}