package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CreatorCollectorTest {
    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addStringCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addIntCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addLongCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addDoubleCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addBooleanCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] injectables = new CreatorProperty[0];
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertEquals(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] properties = new CreatorProperty[0];
        creatorCollector.addPropertyCreator(creator, false, properties);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(null, false);
        AnnotatedParameter parameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }
}