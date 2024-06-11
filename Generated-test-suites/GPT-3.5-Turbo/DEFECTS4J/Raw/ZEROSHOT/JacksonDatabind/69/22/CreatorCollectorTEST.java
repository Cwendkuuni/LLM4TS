package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        creatorCollector = new CreatorCollector(null, null);
    }

    @Test
    public void testSetDefaultCreator() {
        creatorCollector.setDefaultCreator(null);
        assertNotNull(creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        creatorCollector.addStringCreator(null, false);
        assertNotNull(creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        creatorCollector.addIntCreator(null, false);
        assertNotNull(creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        creatorCollector.addLongCreator(null, false);
        assertNotNull(creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        creatorCollector.addDoubleCreator(null, false);
        assertNotNull(creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        creatorCollector.addBooleanCreator(null, false);
        assertNotNull(creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        creatorCollector.addDelegatingCreator(null, false, null);
        assertNotNull(creatorCollector._creators[CreatorCollector.C_DELEGATE]);
    }

    @Test
    public void testAddPropertyCreator() {
        creatorCollector.addPropertyCreator(null, false, null);
        assertNotNull(creatorCollector._creators[CreatorCollector.C_PROPS]);
    }

    @Test
    public void testAddIncompeteParameter() {
        creatorCollector.addIncompeteParameter(null);
        assertNotNull(creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        creatorCollector._creators[CreatorCollector.C_DEFAULT] = null;
        assertFalse(creatorCollector.hasDefaultCreator());
        creatorCollector._creators[CreatorCollector.C_DEFAULT] = new AnnotatedWithParamsTest();
        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testHasDelegatingCreator() {
        creatorCollector._creators[CreatorCollector.C_DELEGATE] = null;
        assertFalse(creatorCollector.hasDelegatingCreator());
        creatorCollector._creators[CreatorCollector.C_DELEGATE] = new AnnotatedWithParamsTest();
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testHasPropertyBasedCreator() {
        creatorCollector._creators[CreatorCollector.C_PROPS] = null;
        assertFalse(creatorCollector.hasPropertyBasedCreator());
        creatorCollector._creators[CreatorCollector.C_PROPS] = new AnnotatedWithParamsTest();
        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }
}

class AnnotatedWithParamsTest implements AnnotatedWithParams {

    @Override
    public Class<?> getDeclaringClass() {
        return null;
    }

    @Override
    public Member getMember() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean canDeserialize() {
        return false;
    }

    @Override
    public AnnotatedParameter getParameter(int index) {
        return null;
    }

    @Override
    public int getParameterCount() {
        return 0;
    }

    @Override
    public Class<?> getRawParameterType(int index) {
        return null;
    }

    @Override
    public JavaType getParameterType(int index) {
        return null;
    }

    @Override
    public Object call() throws Exception {
        return null;
    }

    @Override
    public Object call(Object[] args) throws Exception {
        return null;
    }

    @Override
    public Object call1(Object arg0) throws Exception {
        return null;
    }

    @Override
    public Object callOn(Object pojo) throws Exception {
        return null;
    }

    @Override
    public Annotated withAnnotations(AnnotationMap annotations) {
        return null;
    }
}