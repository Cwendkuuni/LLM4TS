
package com.fasterxml.jackson.databind.deser.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class CreatorCollectorTest {
    
    @Test
    public void testDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.setDefaultCreator(null);
    }
    
    @Test
    public void testStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addStringCreator(null, true);
    }
    
    @Test
    public void testIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addIntCreator(null, true);
    }
    
    @Test
    public void testLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addLongCreator(null, true);
    }
    
    @Test
    public void testDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addDoubleCreator(null, true);
    }
    
    @Test
    public void testBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addBooleanCreator(null, true);
    }
    
    @Test
    public void testDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addDelegatingCreator(null, true, null);
    }
    
    @Test
    public void testPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addPropertyCreator(null, true, null);
    }
    
    @Test
    public void testIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.addIncompeteParameter(null);
    }
    
    @Test
    public void testHasDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        creatorCollector.setDefaultCreator(null);
        boolean result = creatorCollector.hasDefaultCreator();
        Assert.assertTrue(result);
    }
}
