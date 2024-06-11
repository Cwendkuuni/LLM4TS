package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testConstructor() {
        BeanDescription beanDesc = new BeanDescription();
        Config config = new Config();
        
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);

        assertNotNull(creatorCollector);
    }

    @Test
    public void testDefaultValueInstantiatorConstruction() {
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), new Config());

        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());

        assertNotNull(valueInstantiator);
        assertTrue(valueInstantiator.canInstantiate());
        assertTrue(valueInstantiator.canCreateUsingDefault());
    }

    @Test
    public void testDefaultValueInstantiatorWithCollection() {
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), new Config());
        creatorCollector.setDefaultCreator(new AnnotatedWithParams());

        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());

        assertTrue(valueInstantiator.createUsingDefault(new DeserializationContext()) instanceof ArrayList); // Because TYPE_COLLECTION is defined for Collection
    }

    @Test
    public void testDefaultValueInstantiatorWithMapTypes() {
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), new Config());
        creatorCollector.setDefaultCreator(new AnnotatedWithParams());

        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(new DeserializationConfig());

        assertTrue(valueInstantiator.createUsingDefault(new DeserializationContext()) instanceof LinkedHashMap); // Because TYPE_MAP is defined for Map
    }

    @Test
    public void testCreatorSettersAndGetters() {
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), new Config());
        AnnotatedWithParams creator = new AnnotatedWithParams();

        creatorCollector.setDefaultCreator(creator);

        assertEquals(creator, creatorCollector._creators[0]);
        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testDelegatingCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), new Config());
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        AnnotatedWithParams creator = new AnnotatedWithParams();

        creatorCollector.addDelegatingCreator(creator, true, injectables);

        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testPropertyBasedCreator() {
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), new Config());
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        AnnotatedWithParams creator = new AnnotatedWithParams();

        creatorCollector.addPropertyCreator(creator, true, properties);

        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }

    @Test
    public void testIncompleteParameter() {
        CreatorCollector creatorCollector = new CreatorCollector(new BeanDescription(), new Config());
        AnnotatedParameter parameter = new AnnotatedParameter();

        creatorCollector.addIncompeteParameter(parameter);

        assertEquals(parameter, creatorCollector._incompleteParameter);
    }

}