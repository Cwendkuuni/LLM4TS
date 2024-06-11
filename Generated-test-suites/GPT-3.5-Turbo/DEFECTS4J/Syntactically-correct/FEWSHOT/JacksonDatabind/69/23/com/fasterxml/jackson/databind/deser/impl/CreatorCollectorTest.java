package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CreatorCollectorTest {
    @Test
    public void testConstructValueInstantiator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        ValueInstantiator result = creatorCollector.constructValueInstantiator(null);
        assertFalse(result.canCreateUsingDefault());
        assertFalse(result.canInstantiate());
        assertEquals("", result.getValueTypeDesc());
    }

    @Test
    public void testSetDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.setDefaultCreator(null);
        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addStringCreator(null, false);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addIntCreator(null, false);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addLongCreator(null, false);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addDoubleCreator(null, false);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addBooleanCreator(null, false);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addDelegatingCreator(null, false, null);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addPropertyCreator(null, false, null);
        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }

    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(null, null);
        creatorCollector.addIncompeteParameter(null);
        assertNull(creatorCollector._incompleteParameter);
    }
}