package com.fasterxml.jackson.databind.deser.impl;

import org.junit.*;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.setDefaultCreator(null);
        assertNull(creatorCollector._creators[0]);
    }
    
    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addStringCreator(null, false);
        assertNull(creatorCollector._creators[1]);
    }
    
    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addIntCreator(null, false);
        assertNull(creatorCollector._creators[2]);
    }
    
    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addLongCreator(null, false);
        assertNull(creatorCollector._creators[3]);
    }
    
    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addDoubleCreator(null, false);
        assertNull(creatorCollector._creators[4]);
    }
    
    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addBooleanCreator(null, false);
        assertNull(creatorCollector._creators[5]);
    }
    
    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addDelegatingCreator(null, false, null);
        assertNull(creatorCollector._creators[6]);
    }
    
    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addPropertyCreator(null, false, null);
        assertNull(creatorCollector._creators[7]);
    }
    
    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addIncompeteParameter(null);
        assertNull(creatorCollector._incompleteParameter);
    }
}