package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.setDefaultCreator(null);
        assertTrue(creatorCollector.hasDefaultCreator());
    }
    
    @Test
    public void testStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addStringCreator(null, false);
        assertTrue(creatorCollector.hasStringCreator());
    }
    
    @Test
    public void testIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addIntCreator(null, false);
        assertTrue(creatorCollector.hasIntCreator());
    }
    
    @Test
    public void testLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addLongCreator(null, false);
        assertTrue(creatorCollector.hasLongCreator());
    }
    
    @Test
    public void testDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addDoubleCreator(null, false);
        assertTrue(creatorCollector.hasDoubleCreator());
    }
    
    @Test
    public void testBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addBooleanCreator(null, false);
        assertTrue(creatorCollector.hasBooleanCreator());
    }
    
    @Test
    public void testDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addDelegatingCreator(null, false, null);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testPropertyBasedCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addPropertyCreator(null, false, null);
        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }
    
    @Test
    public void testIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addIncompeteParameter(null);
        assertNotNull(creatorCollector.getIncompleteParameter());
    }
    
}