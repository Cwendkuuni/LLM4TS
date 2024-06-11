package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.setDefaultCreator(null);
        assertNotNull(creatorCollector._creators[0]);
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addStringCreator(null, false);
        assertNotNull(creatorCollector._creators[1]);
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addIntCreator(null, false);
        assertNotNull(creatorCollector._creators[2]);
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addLongCreator(null, false);
        assertNotNull(creatorCollector._creators[3]);
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addDoubleCreator(null, false);
        assertNotNull(creatorCollector._creators[4]);
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addBooleanCreator(null, false);
        assertNotNull(creatorCollector._creators[5]);
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addDelegatingCreator(null, false, null);
        assertNotNull(creatorCollector._creators[6]);
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addPropertyCreator(null, false, null);
        assertNotNull(creatorCollector._creators[7]);
    }

    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addIncompeteParameter(null);
        assertNotNull(creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.setDefaultCreator(null);
        assertTrue(creatorCollector.hasDefaultCreator());
    }

}