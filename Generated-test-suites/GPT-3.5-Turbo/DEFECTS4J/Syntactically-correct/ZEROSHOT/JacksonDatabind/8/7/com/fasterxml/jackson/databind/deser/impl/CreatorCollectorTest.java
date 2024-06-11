package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        // create an instance of CreatorCollector
        creatorCollector = new CreatorCollector(null, false);
    }

    @Test
    public void testSetDefaultCreator() {
        // create a dummy AnnotatedWithParams instance
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null, null);
        creatorCollector.setDefaultCreator(creator);
        
        // assert that the creator is set as default creator
        Assert.assertEquals(creator, creatorCollector._creators[0]);
    }

    @Test
    public void testAddStringCreator() {
        // create a dummy AnnotatedWithParams instance
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null, null);
        creatorCollector.addStringCreator(creator, true);
        
        // assert that the creator is added as string creator
        Assert.assertEquals(creator, creatorCollector._creators[1]);
        Assert.assertEquals(1, creatorCollector._explicitCreators & 0x1);
    }

    @Test
    public void testAddIntCreator() {
        // create a dummy AnnotatedWithParams instance
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null, null);
        creatorCollector.addIntCreator(creator, true);
        
        // assert that the creator is added as int creator
        Assert.assertEquals(creator, creatorCollector._creators[2]);
        Assert.assertEquals(2, creatorCollector._explicitCreators & 0x2);
    }

    @Test
    public void testAddLongCreator() {
        // create a dummy AnnotatedWithParams instance
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null, null);
        creatorCollector.addLongCreator(creator, true);
        
        // assert that the creator is added as long creator
        Assert.assertEquals(creator, creatorCollector._creators[3]);
        Assert.assertEquals(4, creatorCollector._explicitCreators & 0x4);
    }

    @Test
    public void testAddDoubleCreator() {
        // create a dummy AnnotatedWithParams instance
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null, null);
        creatorCollector.addDoubleCreator(creator, true);
        
        // assert that the creator is added as double creator
        Assert.assertEquals(creator, creatorCollector._creators[4]);
        Assert.assertEquals(8, creatorCollector._explicitCreators & 0x8);
    }

    @Test
    public void testAddBooleanCreator() {
        // create a dummy AnnotatedWithParams instance
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null, null);
        creatorCollector.addBooleanCreator(creator, true);
        
        // assert that the creator is added as boolean creator
        Assert.assertEquals(creator, creatorCollector._creators[5]);
        Assert.assertEquals(16, creatorCollector._explicitCreators & 0x10);
    }

    @Test
    public void testAddDelegatingCreator() {
        // create a dummy AnnotatedWithParams instance
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null, null);
        CreatorProperty[] injectables = new CreatorProperty[0];
        creatorCollector.addDelegatingCreator(creator, true, injectables);
        
        // assert that the creator is added as delegating creator
        Assert.assertEquals(creator, creatorCollector._creators[6]);
        Assert.assertEquals(32, creatorCollector._explicitCreators & 0x20);
        Assert.assertEquals(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        // create a dummy AnnotatedWithParams instance
        AnnotatedWithParams creator = new AnnotatedWithParams(null, null, null, null);
        CreatorProperty[] properties = new CreatorProperty[0];
        creatorCollector.addPropertyCreator(creator, true, properties);
        
        // assert that the creator is added as property creator
        Assert.assertEquals(creator, creatorCollector._creators[7]);
        Assert.assertEquals(64, creatorCollector._explicitCreators & 0x40);
        Assert.assertEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        // create a dummy AnnotatedParameter instance
        AnnotatedParameter parameter = new AnnotatedParameter(null, null, -1, null);
        creatorCollector.addIncompeteParameter(parameter);
        
        // assert that the parameter is added as incomplete parameter
        Assert.assertEquals(parameter, creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        // assert that there is no default creator initially
        Assert.assertFalse(creatorCollector.hasDefaultCreator());
        
        // add a default creator
        creatorCollector.setDefaultCreator(new AnnotatedWithParams(null, null, null, null));
        
        // assert that there is a default creator now
        Assert.assertTrue(creatorCollector.hasDefaultCreator());
    }
}