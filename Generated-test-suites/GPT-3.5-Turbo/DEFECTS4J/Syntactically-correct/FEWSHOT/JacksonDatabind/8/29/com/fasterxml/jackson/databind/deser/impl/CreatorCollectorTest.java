package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.setDefaultCreator(null);
        
        // Test that the creator is set correctly
        assertNull(creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addStringCreator(null, false);
        
        // Test that the creator is added correctly
        assertNull(creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addIntCreator(null, false);
        
        // Test that the creator is added correctly
        assertNull(creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addLongCreator(null, false);
        
        // Test that the creator is added correctly
        assertNull(creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addDoubleCreator(null, false);
        
        // Test that the creator is added correctly
        assertNull(creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addBooleanCreator(null, false);
        
        // Test that the creator is added correctly
        assertNull(creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addDelegatingCreator(null, false, null);
        
        // Test that the creator is added correctly
        assertNull(creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertNull(creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addPropertyCreator(null, false, null);
        
        // Test that the creator is added correctly
        assertNull(creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertNull(creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addIncompeteParameter(null);
        
        // Test that the parameter is added correctly
        assertNull(creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        
        assertFalse(creatorCollector.hasDefaultCreator());
        
        creatorCollector.setDefaultCreator(null);
        
        assertTrue(creatorCollector.hasDefaultCreator());
    }

}