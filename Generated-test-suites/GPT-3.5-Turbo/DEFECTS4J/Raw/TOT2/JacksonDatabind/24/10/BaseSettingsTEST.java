package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        // Typical use case
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withClassIntrospector(ci);
        
        assertNotNull(newSettings);
        assertEquals(ci, newSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        // Typical use case
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withAnnotationIntrospector(ai);
        
        assertNotNull(newSettings);
        assertEquals(ai, newSettings.getAnnotationIntrospector());
    }

    // Add more test cases for other methods as needed

}