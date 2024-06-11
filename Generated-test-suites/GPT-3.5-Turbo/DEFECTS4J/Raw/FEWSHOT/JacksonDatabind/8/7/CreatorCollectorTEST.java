package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.setDefaultCreator(creator);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addStringCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addIntCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addLongCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addDoubleCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addBooleanCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[0];
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertSame(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[0];
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addPropertyCreator(creator, false, properties);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertSame(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = Mockito.mock(AnnotatedParameter.class);
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        creatorCollector.addIncompeteParameter(parameter);
        assertSame(parameter, creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        assertFalse(creatorCollector.hasDefaultCreator());
        creatorCollector._creators[CreatorCollector.C_DEFAULT] = Mockito.mock(AnnotatedWithParams.class);
        assertTrue(creatorCollector.hasDefaultCreator());
    }
}