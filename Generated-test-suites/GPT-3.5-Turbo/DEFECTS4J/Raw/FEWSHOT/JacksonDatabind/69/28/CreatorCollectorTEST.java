package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Member;

import org.junit.Test;

import com.fasterxml.jackson.databind.AnnotatedMember;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;

public class CreatorCollectorTest {
    @Test
    public void testSetDefaultCreator() {
        CreatorCollector cc = new CreatorCollector(null, null);
        AnnotatedMember creator = null;
        cc.setDefaultCreator(creator);
        assertTrue(cc.hasDefaultCreator());
    }

    @Test
    public void testAddStringCreator() {
        CreatorCollector cc = new CreatorCollector(null, null);
        AnnotatedMember creator = null;
        cc.addStringCreator(creator, true);
        assertTrue(cc.hasDelegatingCreator());
    }

    @Test
    public void testAddIntCreator() {
        CreatorCollector cc = new CreatorCollector(null, null);
        AnnotatedMember creator = null;
        cc.addIntCreator(creator, true);
        assertTrue(cc.hasDelegatingCreator());
    }

    @Test
    public void testAddLongCreator() {
        CreatorCollector cc = new CreatorCollector(null, null);
        AnnotatedMember creator = null;
        cc.addLongCreator(creator, true);
        assertTrue(cc.hasDelegatingCreator());
    }

    @Test
    public void testAddDoubleCreator() {
        CreatorCollector cc = new CreatorCollector(null, null);
        AnnotatedMember creator = null;
        cc.addDoubleCreator(creator, true);
        assertTrue(cc.hasDelegatingCreator());
    }

    @Test
    public void testAddBooleanCreator() {
        CreatorCollector cc = new CreatorCollector(null, null);
        AnnotatedMember creator = null;
        cc.addBooleanCreator(creator, true);
        assertTrue(cc.hasDelegatingCreator());
    }

    @Test
    public void testAddDelegatingCreator() {
        CreatorCollector cc = new CreatorCollector(null, null);
        AnnotatedMember creator = null;
        SettableBeanProperty[] injectables = null;
        cc.addDelegatingCreator(creator, true, injectables);
        assertTrue(cc.hasDelegatingCreator());
    }

    @Test
    public void testAddPropertyCreator() {
        CreatorCollector cc = new CreatorCollector(null, null);
        AnnotatedMember creator = null;
        SettableBeanProperty[] properties = null;
        cc.addPropertyCreator(creator, true, properties);
        assertTrue(cc.hasPropertyBasedCreator());
    }

    @Test
    public void testAddIncompeteParameter() {
        CreatorCollector cc = new CreatorCollector(null, null);
        AnnotatedMember creator = null;
        SettableBeanProperty[] properties = null;
        cc.addPropertyCreator(creator, false, properties);

        AnnotatedMember property = null;
        cc.addIncompeteParameter(property);
        assertEquals(property, cc.impl()._incompleteParameter);
    }
}