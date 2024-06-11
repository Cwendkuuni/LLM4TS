package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        creatorCollector = new CreatorCollector(new BeanDescription(), new MapperConfig());
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);
        assertEquals(creator, creatorCollector._creators[0]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addStringCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[1]);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addIntCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[2]);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addLongCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[3]);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addDoubleCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[4]);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addBooleanCreator(creator, true);
        assertEquals(creator, creatorCollector._creators[5]);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        SettableBeanProperty[] injectables = new SettableBeanProperty[3];
        creatorCollector.addDelegatingCreator(creator, true, injectables);
        assertEquals(creator, creatorCollector._creators[6]);
        assertEquals(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        SettableBeanProperty[] properties = new SettableBeanProperty[2];
        creatorCollector.addPropertyCreator(creator, true, properties);
        assertEquals(creator, creatorCollector._creators[7]);
        assertEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        assertFalse(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testHasDelegatingCreator() {
        assertFalse(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testHasPropertyBasedCreator() {
        assertFalse(creatorCollector.hasPropertyBasedCreator());
    }

}