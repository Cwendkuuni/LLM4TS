package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        creatorCollector = new CreatorCollector(null, false);
    }

    @Test
    public void testConstructor() {
        assertNotNull(creatorCollector);
    }

    @Test
    public void testCanInstantiate() {
        assertTrue(creatorCollector.canInstantiate());
    }

    @Test
    public void testCanCreateUsingDefault() {
        assertTrue(creatorCollector.canCreateUsingDefault());
    }

    @Test
    public void testCreateUsingDefault() {
        assertNull(creatorCollector.createUsingDefault(null));
    }

    @Test
    public void testGetValueTypeDesc() {
        assertEquals(Object.class.getName(), creatorCollector.getValueTypeDesc());
    }

    @Test
    public void testHasDefaultCreator() {
        assertFalse(creatorCollector.hasDefaultCreator());
    }

}