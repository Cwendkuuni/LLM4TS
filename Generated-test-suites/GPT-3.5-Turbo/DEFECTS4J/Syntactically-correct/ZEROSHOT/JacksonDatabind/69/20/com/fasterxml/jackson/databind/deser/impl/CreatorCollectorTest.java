package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    @Test
    public void testConstructValueInstantiator() {
        DeserializationConfig config = null;
        CreatorCollector creator = new CreatorCollector(null, config);

        ValueInstantiator result = creator.constructValueInstantiator(config);

        assertNotNull(result);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = null;
        CreatorCollector creatorCollector = new CreatorCollector(null, null);

        creatorCollector.setDefaultCreator(creator);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        CreatorCollector creatorCollector = new CreatorCollector(null, null);

        creatorCollector.addStringCreator(creator, explicit);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        CreatorCollector creatorCollector = new CreatorCollector(null, null);

        creatorCollector.addIntCreator(creator, explicit);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        CreatorCollector creatorCollector = new CreatorCollector(null, null);

        creatorCollector.addLongCreator(creator, explicit);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        CreatorCollector creatorCollector = new CreatorCollector(null, null);

        creatorCollector.addDoubleCreator(creator, explicit);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        CreatorCollector creatorCollector = new CreatorCollector(null, null);

        creatorCollector.addBooleanCreator(creator, explicit);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        SettableBeanProperty[] injectables = null;
        CreatorCollector creatorCollector = new CreatorCollector(null, null);

        creatorCollector.addDelegatingCreator(creator, explicit, injectables);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = null;
        boolean explicit = false;
        SettableBeanProperty[] properties = null;
        CreatorCollector creatorCollector = new CreatorCollector(null, null);

        creatorCollector.addPropertyCreator(creator, explicit, properties);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = null;
        CreatorCollector creatorCollector = new CreatorCollector(null, null);

        creatorCollector.addIncompeteParameter(parameter);
    }

    @Test
    public void testHasDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);

        boolean result = creatorCollector.hasDefaultCreator();

        assertFalse(result);
    }

    @Test
    public void testHasDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);

        boolean result = creatorCollector.hasDelegatingCreator();

        assertFalse(result);
    }

    @Test
    public void testHasPropertyBasedCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);

        boolean result = creatorCollector.hasPropertyBasedCreator();

        assertFalse(result);
    }
}