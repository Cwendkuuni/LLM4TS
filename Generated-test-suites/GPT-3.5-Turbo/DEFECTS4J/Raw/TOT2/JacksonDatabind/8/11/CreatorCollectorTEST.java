package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreatorCollectorTest {

    @Mock
    private BeanDescription beanDescription;

    @Mock
    private DeserializationConfig deserializationConfig;

    private CreatorCollector creatorCollector;

    @Before
    public void setup() {
        creatorCollector = new CreatorCollector(beanDescription, true);
    }

    @Test
    public void testConstructValueInstantiator() {
        // Test null delegate type
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(deserializationConfig);
        assertNotNull(instantiator);
        assertTrue(instantiator.canCreateUsingDefault());

        // Add a delegate creator
        creatorCollector.addDelegatingCreator(mock(AnnotatedWithParams.class), true, null);
        instantiator = creatorCollector.constructValueInstantiator(deserializationConfig);
        assertNotNull(instantiator);
    }

    @Test
    public void testSetDefaultCreator() {
        creatorCollector.setDefaultCreator(mock(AnnotatedWithParams.class));
        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testAddStringCreator() {
        creatorCollector.addStringCreator(mock(AnnotatedWithParams.class), true);
    }

    @Test
    public void testAddIntCreator() {
        creatorCollector.addIntCreator(mock(AnnotatedWithParams.class), true);
    }

    @Test
    public void testAddLongCreator() {
        creatorCollector.addLongCreator(mock(AnnotatedWithParams.class), true);
    }

    @Test
    public void testAddDoubleCreator() {
        creatorCollector.addDoubleCreator(mock(AnnotatedWithParams.class), true);
    }

    @Test
    public void testAddBooleanCreator() {
        creatorCollector.addBooleanCreator(mock(AnnotatedWithParams.class), true);
    }

    @Test
    public void testAddPropertyCreator() {
        creatorCollector.addPropertyCreator(mock(AnnotatedWithParams.class), true, null);
    }

    @Test
    public void testAddIncompeteParameter() {
        creatorCollector.addIncompeteParameter(mock(AnnotatedParameter.class));
    }
}