package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        creatorCollector = new CreatorCollector(null, null);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = null;
        creatorCollector.setDefaultCreator(creator);
        // add assertion here
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addStringCreator(creator, explicit);
        // add assertion here
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addIntCreator(creator, explicit);
        // add assertion here
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addLongCreator(creator, explicit);
        // add assertion here
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addDoubleCreator(creator, explicit);
        // add assertion here
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        creatorCollector.addBooleanCreator(creator, explicit);
        // add assertion here
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        SettableBeanProperty[] injectables = null;
        creatorCollector.addDelegatingCreator(creator, explicit, injectables);
        // add assertion here
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        SettableBeanProperty[] properties = null;
        creatorCollector.addPropertyCreator(creator, explicit, properties);
        // add assertion here
    }

    @Test
    public void testAddIncompleteParameter() {
        AnnotatedParameter parameter = null;
        creatorCollector.addIncompeteParameter(parameter);
        // add assertion here
    }

    @Test
    public void testHasDefaultCreator() {
        boolean result = creatorCollector.hasDefaultCreator();
        // add assertion here
    }

    @Test
    public void testHasDelegatingCreator() {
        boolean result = creatorCollector.hasDelegatingCreator();
        // add assertion here
    }

    @Test
    public void testHasPropertyBasedCreator() {
        boolean result = creatorCollector.hasPropertyBasedCreator();
        // add assertion here
    }
}