package com.fasterxml.jackson.databind.cfg;

import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings updatedSettings = baseSettings.withClassIntrospector(ci);
        assertEquals(ci, updatedSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings updatedSettings = baseSettings.withAnnotationIntrospector(ai);
        assertEquals(ai, updatedSettings.getAnnotationIntrospector());
    }

    // Additional test cases for other public methods in BaseSettings class can be added here

}