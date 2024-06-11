package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Test;

public class CreatorCollectorTest {
    @Test
    public void testSetDefaultCreator() {
        CreatorCollector collector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        collector.setDefaultCreator(creator);
        assertEquals(creator, collector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector collector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        collector.addStringCreator(creator, false);
        assertEquals(creator, collector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector collector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        collector.addIntCreator(creator, false);
        assertEquals(creator, collector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector collector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        collector.addLongCreator(creator, false);
        assertEquals(creator, collector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector collector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        collector.addDoubleCreator(creator, false);
        assertEquals(creator, collector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector collector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        collector.addBooleanCreator(creator, false);
        assertEquals(creator, collector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector collector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] injectables = {};
        collector.addDelegatingCreator(creator, false, injectables);
        assertEquals(creator, collector._creators[CreatorCollector.C_DELEGATE]);
        assertEquals(injectables, collector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorCollector collector = new CreatorCollector(null, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] properties = {};
        collector.addPropertyCreator(creator, false, properties);
        assertEquals(creator, collector._creators[CreatorCollector.C_PROPS]);
        assertEquals(properties, collector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector collector = new CreatorCollector(null, true);
        AnnotatedParameter parameter = new AnnotatedParameter();
        collector.addIncompeteParameter(parameter);
        assertEquals(parameter, collector._incompleteParameter);
    }
}