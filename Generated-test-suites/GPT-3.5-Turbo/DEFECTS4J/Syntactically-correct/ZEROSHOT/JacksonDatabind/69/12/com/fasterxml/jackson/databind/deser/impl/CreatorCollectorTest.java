package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testHasDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        assertFalse(creatorCollector.hasDefaultCreator());
        creatorCollector.setDefaultCreator(null);
        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testHasDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        assertFalse(creatorCollector.hasDelegatingCreator());
        creatorCollector.addDelegatingCreator(null, null, null);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testHasPropertyBasedCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        assertFalse(creatorCollector.hasPropertyBasedCreator());
        creatorCollector.addPropertyCreator(null, null, null);
        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }

}