package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class CreatorCollectorTest {
    
    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        null.setPublic();
        creatorCollector.setDefaultCreator(null);
        assertEquals(null, creatorCollector._creators[0]);
    }
    
    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        null.setPublic();
        creatorCollector.addStringCreator(null, false);
        assertEquals(null, creatorCollector._creators[1]);
    }
    
    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        null.setPublic();
        creatorCollector.addIntCreator(null, false);
        assertEquals(null, creatorCollector._creators[2]);
    }
    
    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        null.setPublic();
        creatorCollector.addLongCreator(null, false);
        assertEquals(null, creatorCollector._creators[3]);
    }
    
    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        null.setPublic();
        creatorCollector.addDoubleCreator(null, false);
        assertEquals(null, creatorCollector._creators[4]);
    }
    
    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        null.setPublic();
        creatorCollector.addBooleanCreator(null, false);
        assertEquals(null, creatorCollector._creators[5]);
    }
    
    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        null.setPublic();
        creatorCollector.addDelegatingCreator(null, false, null);
        assertEquals(null, creatorCollector._creators[6]);
    }
    
    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        null.setPublic();
        creatorCollector.addPropertyCreator(null, false, null);
        assertEquals(null, creatorCollector._creators[7]);
    }
    
    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addIncompeteParameter(null);
        assertEquals(null, creatorCollector._incompleteParameter);
    }
    
    @Test
    public void testHasDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.hasDefaultCreator();
    }
    
}