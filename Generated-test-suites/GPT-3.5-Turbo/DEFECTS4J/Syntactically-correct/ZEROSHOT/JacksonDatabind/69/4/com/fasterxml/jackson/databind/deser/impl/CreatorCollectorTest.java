package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setup() {
        creatorCollector = new CreatorCollector();
    }

    @Test
    public void testSetDefaultCreator() {
        creatorCollector.setDefaultCreator(null);
        // TODO: add assertions
    }

    @Test
    public void testAddStringCreator() {
        creatorCollector.addStringCreator(null, false);
        // TODO: add assertions
    }

    @Test
    public void testAddIntCreator() {
        creatorCollector.addIntCreator(null, false);
        // TODO: add assertions
    }

    @Test
    public void testAddLongCreator() {
        creatorCollector.addLongCreator(null, false);
        // TODO: add assertions
    }

    @Test
    public void testAddDoubleCreator() {
        creatorCollector.addDoubleCreator(null, false);
        // TODO: add assertions
    }

    @Test
    public void testAddBooleanCreator() {
        creatorCollector.addBooleanCreator(null, false);
        // TODO: add assertions
    }

    @Test
    public void testAddDelegatingCreator() {
        creatorCollector.addDelegatingCreator(null, false, null);
        // TODO: add assertions
    }

    @Test
    public void testAddPropertyCreator() {
        creatorCollector.addPropertyCreator(null, false, null);
        // TODO: add assertions
    }

    @Test
    public void testAddIncompeteParameter() {
        creatorCollector.addIncompeteParameter(null);
        // TODO: add assertions
    }

    @Test
    public void testHasDefaultCreator() {
        boolean result = creatorCollector.hasDefaultCreator();
        // TODO: add assertions
    }

    @Test
    public void testHasDelegatingCreator() {
        boolean result = creatorCollector.hasDelegatingCreator();
        // TODO: add assertions
    }

    @Test
    public void testHasPropertyBasedCreator() {
        boolean result = creatorCollector.hasPropertyBasedCreator();
        // TODO: add assertions
    }
}