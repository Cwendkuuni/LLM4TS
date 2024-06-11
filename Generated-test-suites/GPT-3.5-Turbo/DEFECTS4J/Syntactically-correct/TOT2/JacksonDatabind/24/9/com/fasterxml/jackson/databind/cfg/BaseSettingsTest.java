package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        // Test typical use case
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings updatedSettings = baseSettings.withClassIntrospector(new ClassIntrospector());
        assertNotNull(updatedSettings.getClassIntrospector());
        assertNotEquals(baseSettings.getClassIntrospector(), updatedSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        // Test typical use case
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings updatedSettings = baseSettings.withAnnotationIntrospector(new AnnotationIntrospector());
        assertNotNull(updatedSettings.getAnnotationIntrospector());
        assertNotEquals(baseSettings.getAnnotationIntrospector(), updatedSettings.getAnnotationIntrospector());
    }

    // Additional test cases for other methods should be included here

}