package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

    @Test
    public void testDefaultCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        AnnotatedParameter annotatedParameter = new AnnotatedParameter(null, null, null, null, null);
        creatorCollector.addIncompeteParameter(annotatedParameter);
        creatorCollector.setDefaultCreator(annotatedParameter);
        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testStringCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        AnnotatedParameter annotatedParameter = new AnnotatedParameter(null, null, null, null, null);
        creatorCollector.addStringCreator(annotatedParameter, true);
        assertNotNull(creatorCollector.verifyNonDup(annotatedParameter, 1, true));
    }

    @Test
    public void testIntCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        AnnotatedParameter annotatedParameter = new AnnotatedParameter(null, null, null, null, null);
        creatorCollector.addIntCreator(annotatedParameter, true);
        assertNotNull(creatorCollector.verifyNonDup(annotatedParameter, 2, true));
    }

    @Test
    public void testLongCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        AnnotatedParameter annotatedParameter = new AnnotatedParameter(null, null, null, null, null);
        creatorCollector.addLongCreator(annotatedParameter, true);
        assertNotNull(creatorCollector.verifyNonDup(annotatedParameter, 3, true));
    }

    @Test
    public void testDoubleCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        AnnotatedParameter annotatedParameter = new AnnotatedParameter(null, null, null, null, null);
        creatorCollector.addDoubleCreator(annotatedParameter, true);
        assertNotNull(creatorCollector.verifyNonDup(annotatedParameter, 4, true));
    }

    @Test
    public void testBooleanCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        AnnotatedParameter annotatedParameter = new AnnotatedParameter(null, null, null, null, null);
        creatorCollector.addBooleanCreator(annotatedParameter, true);
        assertNotNull(creatorCollector.verifyNonDup(annotatedParameter, 5, true));
    }

    @Test
    public void testDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        AnnotatedParameter annotatedParameter = new AnnotatedParameter(null, null, null, null, null);
        CreatorProperty[] injectables = new CreatorProperty[1];
        creatorCollector.addDelegatingCreator(annotatedParameter, true, injectables);
        assertNotNull(creatorCollector.verifyNonDup(annotatedParameter, 6, true));
    }

    @Test
    public void testPropertyCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        AnnotatedParameter annotatedParameter = new AnnotatedParameter(null, null, null, null, null);
        CreatorProperty[] properties = new CreatorProperty[1];
        creatorCollector.addPropertyCreator(annotatedParameter, true, properties);
        assertNotNull(creatorCollector.verifyNonDup(annotatedParameter, 7, true));
    }

    @Test
    public void testConstructValueInstantiator() throws IOException {
        CreatorCollector creatorCollector = new CreatorCollector(null, true);
        AnnotatedParameter annotatedParameter = new AnnotatedParameter(null, null, null, null, null);
        creatorCollector.setDefaultCreator(annotatedParameter);
        DeserializationConfig config = new DeserializationConfig(null, null, null, true);
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);
        assertTrue(valueInstantiator.canInstantiate());
        assertTrue(valueInstantiator.canCreateUsingDefault());
        assertNotNull(valueInstantiator.createUsingDefault(null));
    }

}