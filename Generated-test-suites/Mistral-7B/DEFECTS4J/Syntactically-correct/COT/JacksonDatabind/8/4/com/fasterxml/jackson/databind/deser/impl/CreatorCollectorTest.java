package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.Util;
import com.fasterxml.jackson.databind.type.TypeBindings;

@RunWith(MockitoJUnitRunner.class)
public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;
    private DeserializationConfig config;
    private BeanDescription beanDesc;

    @Before
    public void setup() throws Exception {
        config = Mockito.mock(DeserializationConfig.class);
        beanDesc = Mockito.mock(BeanDescription.class);
        creatorCollector = new CreatorCollector(beanDesc, true);
    }

    @Test
    public void testDefaultCreator() throws Exception {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.setDefaultCreator(creator);
        ValueInstantiator inst = creatorCollector.constructValueInstantiator(config);
        assertTrue(inst instanceof StdValueInstantiator);
        assertNull(inst.getDelegate());
    }

    @Test
    public void testStringCreator() throws Exception {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(creator, false);
        ValueInstantiator inst = creatorCollector.constructValueInstantiator(config);
        assertEquals(creator, inst.findStringValueCreator());
    }

    @Test
    public void testIntCreator() throws Exception {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addIntCreator(creator, false);
        ValueInstantiator inst = creatorCollector.constructValueInstantiator(config);
        assertEquals(creator, inst.findIntValueCreator());
    }

    @Test
    public void testLongCreator() throws Exception {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addLongCreator(creator, false);
        ValueInstantiator inst = creatorCollector.constructValueInstantiator(config);
        assertEquals(creator, inst.findLongValueCreator());
    }

    @Test
    public void testDoubleCreator() throws Exception {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addDoubleCreator(creator, false);
        ValueInstantiator inst = creatorCollector.constructValueInstantiator(config);
        assertEquals(creator, inst.findDoubleValueCreator());
    }

    @Test
    public void testBooleanCreator() throws Exception {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addBooleanCreator(creator, false);
        ValueInstantiator inst = creatorCollector.constructValueInstantiator(config);
        assertEquals(creator, inst.findBooleanValueCreator());
    }

    @Test
    public void testDelegateCreator() throws Exception {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        injectables[0] = new CreatorProperty(propDef, null, null, null);
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        ValueInstantiator inst = creatorCollector.constructValueInstantiator(config);
        assertEquals(creator, inst.getDelegate());
    }

    @Test
    public void testPropertyBasedCreator() throws Exception {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[2];
        BeanPropertyDefinition propDef1 = Mockito.mock(BeanPropertyDefinition.class);
        BeanPropertyDefinition propDef2 = Mockito.mock(BeanPropertyDefinition.class);
        properties[0] = new CreatorProperty(propDef1, null, null, null);
        properties[1] = new CreatorProperty(propDef2, null, null, null);
        creatorCollector.addPropertyCreator(creator, false, properties);
        ValueInstantiator inst = creatorCollector.constructValueInstantiator(config);
        assertEquals(creator, inst.getPropertyBasedCreator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateCreator() throws Exception {
        AnnotatedWithParams creator1 = Mockito.mock(AnnotatedWithParams.class);
        AnnotatedWithParams creator2 = Mockito.mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(creator1, false);
        creatorCollector.addStringCreator(creator2, false);
        creatorCollector.constructValueInstantiator(config);
        fail("Expected IllegalArgumentException, but didn't get it");
    }

    @Test
    public void testVanillaInstantiation() throws Exception {
        creatorCollector = new CreatorCollector(beanDesc, false);
        ValueInstantiator inst = creatorCollector.constructValueInstantiator(config);
        assertFalse(inst instanceof StdValueInstantiator);
    }
}