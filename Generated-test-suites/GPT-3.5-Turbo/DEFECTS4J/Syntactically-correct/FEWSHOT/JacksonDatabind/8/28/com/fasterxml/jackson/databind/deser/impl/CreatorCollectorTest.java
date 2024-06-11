package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {
  
    @Test
    public void testSetDefaultCreator() {
        // Test case 1
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.setDefaultCreator(null);
        assertNull(creatorCollector._creators[0]);
    }
    
    @Test
    public void testAddStringCreator() {
        // Test case 1
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addStringCreator(null, false);
        assertNull(creatorCollector._creators[1]);
    }
    
    @Test
    public void testAddIntCreator() {
        // Test case 1
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addIntCreator(null, false);
        assertNull(creatorCollector._creators[2]);
    }
    
    @Test
    public void testAddLongCreator() {
        // Test case 1
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addLongCreator(null, false);
        assertNull(creatorCollector._creators[3]);
    }
    
    @Test
    public void testAddDoubleCreator() {
        // Test case 1
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addDoubleCreator(null, false);
        assertNull(creatorCollector._creators[4]);
    }
    
    @Test
    public void testAddBooleanCreator() {
        // Test case 1
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addBooleanCreator(null, false);
        assertNull(creatorCollector._creators[5]);
    }
    
    @Test
    public void testAddDelegatingCreator() {
        // Test case 1
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addDelegatingCreator(null, false, null);
        assertNull(creatorCollector._creators[6]);
    }
    
    @Test
    public void testAddPropertyCreator() {
        // Test case 1
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addPropertyCreator(null, false, null);
        assertNull(creatorCollector._creators[7]);
    }
    
    @Test
    public void testAddIncompeteParameter() {
        // Test case 1
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addIncompeteParameter(null);
        assertNull(creatorCollector._incompleteParameter);
    }
  
}