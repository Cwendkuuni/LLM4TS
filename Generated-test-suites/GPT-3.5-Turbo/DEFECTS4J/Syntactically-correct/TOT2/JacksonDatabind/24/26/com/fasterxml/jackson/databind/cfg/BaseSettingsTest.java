package com.fasterxml.jackson.databind.cfg;

import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        // Test initial values
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings baseSettings = new BaseSettings(ci, null, null, null, null, null, null, null, null, null, null);
        assertEquals(ci, baseSettings.getClassIntrospector());

        // Test setting new ClassIntrospector
        ClassIntrospector newCi = new ClassIntrospector();
        BaseSettings updatedSettings = baseSettings.withClassIntrospector(newCi);
        assertEquals(newCi, updatedSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        // Test initial values
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, ai, null, null, null, null, null, null, null, null, null);
        assertEquals(ai, baseSettings.getAnnotationIntrospector());

        // Test setting new AnnotationIntrospector
        AnnotationIntrospector newAi = new AnnotationIntrospector();
        BaseSettings updatedSettings = baseSettings.withAnnotationIntrospector(newAi);
        assertEquals(newAi, updatedSettings.getAnnotationIntrospector());
    }

    // Add more test methods for other public methods...

}