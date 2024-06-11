package com.fasterxml.jackson.databind.deser;

import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

    @Test
    public void testGetName() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        assertNull(property.getName());
    }

    @Test
    public void testGetType() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        assertNull(property.getType());
    }

    @Test
    public void testWithNullProvider() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        CreatorProperty newProperty = property.withNullProvider(null);
        assertNotNull(newProperty);
    }

    @Test
    public void testSetFallbackSetter() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        property.setFallbackSetter(null);
        CreatorProperty fallbackSetter = property._fallbackSetter;
        assertNull(fallbackSetter);
    }

    @Test
    public void testMarkAsIgnorable() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        property.markAsIgnorable();
        assertTrue(property._ignorable);
    }

    @Test
    public void testFindInjectableValue() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        Object injectableValue = property.findInjectableValue(null, null);
        assertNull(injectableValue);
    }

    @Test
    public void testInject() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        property.inject(null, null);
        // No exception expected
    }

    @Test
    public void testGetAnnotation() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        assertNull(property.getAnnotation(Test.class));
    }

    @Test
    public void testGetMember() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        assertNull(property.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        assertEquals(0, property.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        property.deserializeAndSet(null, null, null);
        // No exception expected
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        Object value = property.deserializeSetAndReturn(null, null, null);
        assertNull(value);
    }

    @Test
    public void testSet() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        property.set(null, null);
        // No exception expected
    }

    @Test
    public void testSetAndReturn() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        Object value = property.setAndReturn(null, null);
        assertNull(value);
    }

    @Test
    public void testGetInjectableValueId() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        assertNull(property.getInjectableValueId());
    }

    @Test
    public void testToString() {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        String string = property.toString();
        assertNotNull(string);
    }

    @Test
    public void testVerifySetter() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        property._fallbackSetter = null;
        property._verifySetter();
    }

    @Test(expected = IOException.class)
    public void testReportMissingSetter() throws Exception {
        CreatorProperty property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        property._reportMissingSetter(null, null);
    }
}