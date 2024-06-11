package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CreatorPropertyTest {

    @Test
    public void testConstructor() {
        // Test case 1
        CreatorProperty property = new CreatorProperty(
            new PropertyName("name"), 
            JavaType.INT, 
            null, 
            null, 
            null, 
            null, 
            0, 
            null, 
            null
        );
        assertEquals("name", property.getName().getSimpleName());
        assertEquals(JavaType.INT, property.getType());

        // Test case 2
        property = new CreatorProperty(
            new PropertyName("name"), 
            JavaType.BOOLEAN, 
            null, 
            null, 
            null, 
            null, 
            1, 
            null, 
            null
        );
        assertEquals("name", property.getName().getSimpleName());
        assertEquals(JavaType.BOOLEAN, property.getType());
    }

    @Test
    public void testFindInjectableValue() {
        CreatorProperty property = new CreatorProperty(
            new PropertyName("name"), 
            JavaType.INT, 
            null, 
            null, 
            null, 
            null, 
            0, 
            "value", 
            null
        );
        
        // Test case 1
        try {
            property.findInjectableValue(null, null);
        } catch (Exception e) {
            assertEquals(
                "Property 'name' (type com.fasterxml.jackson.databind.deser.CreatorProperty) has no injectable value id configured", 
                e.getMessage()
            );
        }

        // Test case 2
        try {
            property.findInjectableValue(null, new Object());
        } catch (Exception e) {
            assertEquals(
                "Property 'name' (type com.fasterxml.jackson.databind.deser.CreatorProperty) has no injectable value id configured", 
                e.getMessage()
            );
        }
    }

    @Test
    public void testInject() {
        CreatorProperty property = new CreatorProperty(
            new PropertyName("name"), 
            JavaType.INT, 
            null, 
            null, 
            null, 
            null, 
            0, 
            "value", 
            null
        );
        
        // Test case 1
        try {
            property.inject(null, null);
        } catch (Exception e) {
            assertEquals(
                "Property 'name' (type com.fasterxml.jackson.databind.deser.CreatorProperty) has no injectable value id configured", 
                e.getMessage()
            );
        }

        // Test case 2
        try {
            property.inject(null, new Object());
        } catch (Exception e) {
            assertEquals(
                "Property 'name' (type com.fasterxml.jackson.databind.deser.CreatorProperty) has no injectable value id configured", 
                e.getMessage()
            );
        }
    }
}