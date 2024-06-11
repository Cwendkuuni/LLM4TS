package com.fasterxml.jackson.databind.deser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorPropertyTest {

    private CreatorProperty creatorProperty;

    @Before
    public void setup() {
        creatorProperty = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
    }

    @Test
    public void testGetName() {
        assertNull(creatorProperty.getName());
    }

    @Test
    public void testGetValueDeserializer() {
        assertNull(creatorProperty.getValueDeserializer());
    }

    @Test
    public void testGetInjectableValueId() {
        assertNull(creatorProperty.getInjectableValueId());
    }

    @Test
    public void testFindInjectableValue() {
        // TODO: Add test case
    }

    @Test
    public void testInject() {
        // TODO: Add test case
    }

    @Test
    public void testGetAnnotation() {
        assertNull(creatorProperty.getAnnotation(null));
    }

    @Test
    public void testGetMember() {
        assertNull(creatorProperty.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        assertEquals(0, creatorProperty.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() {
        // TODO: Add test case
    }

    @Test
    public void testDeserializeSetAndReturn() {
        // TODO: Add test case
    }

    @Test
    public void testSet() {
        // TODO: Add test case
    }

    @Test
    public void testSetAndReturn() {
        // TODO: Add test case
    }

    @Test
    public void testToString() {
        assertEquals("[creator property, name 'null'; inject id 'null']", creatorProperty.toString());
    }

}