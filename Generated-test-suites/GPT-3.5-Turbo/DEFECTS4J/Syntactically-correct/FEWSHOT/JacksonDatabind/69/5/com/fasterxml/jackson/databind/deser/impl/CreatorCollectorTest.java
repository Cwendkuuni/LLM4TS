package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CreatorCollectorTest {
    private CreatorCollector creatorCollector;
    
    @Before
    public void setup() {
        creatorCollector = new CreatorCollector(null, null);
    }
    
    @Test
    public void testSetDefaultCreator() {
        creatorCollector.setDefaultCreator(null);
        
        assertTrue(creatorCollector.hasDefaultCreator());
    }
    
    @Test
    public void testAddStringCreator() {
        creatorCollector.addStringCreator(null, false);
        
        assertTrue(creatorCollector.hasNonDefaultCreator());
    }
    
    @Test
    public void testAddIntCreator() {
        creatorCollector.addIntCreator(null, false);
        
        assertTrue(creatorCollector.hasNonDefaultCreator());
    }
    
    @Test
    public void testAddLongCreator() {
        creatorCollector.addLongCreator(null, false);
        
        assertTrue(creatorCollector.hasNonDefaultCreator());
    }
    
    @Test
    public void testAddDoubleCreator() {
        creatorCollector.addDoubleCreator(null, false);
        
        assertTrue(creatorCollector.hasNonDefaultCreator());
    }
    
    @Test
    public void testAddBooleanCreator() {
        creatorCollector.addBooleanCreator(null, false);
        
        assertTrue(creatorCollector.hasNonDefaultCreator());
    }
    
    @Test
    public void testAddDelegatingCreator() {
        creatorCollector.addDelegatingCreator(null, false, null);
        
        assertTrue(creatorCollector.hasDelegatingCreator());
    }
    
    @Test
    public void testAddPropertyCreator() {
        creatorCollector.addPropertyCreator(null, false, null);
        
        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }
    
    @Test
    public void testAddIncompeteParameter() {
        creatorCollector.addIncompeteParameter(null);
        
        assertNotNull(creatorCollector.getIncompleteParameter());
    }
}