package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testDefaultCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        collector.setDefaultCreator(creator);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mock(DeserializationConfig.class));

        assertNotNull(instantiator);
        assertTrue(instantiator instanceof StdValueInstantiator);
    }

    @Test
    public void testStringCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        collector.addStringCreator(creator, false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mock(DeserializationConfig.class));

        assertNotNull(instantiator);
        assertTrue(instantiator instanceof StdValueInstantiator);
        instantiator.configureFromStringCreator(creator);
    }

    @Test
    public void testIntCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        collector.addIntCreator(creator, false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mock(DeserializationConfig.class));

        assertNotNull(instantiator);
        assertTrue(instantiator instanceof StdValueInstantiator);
        instantiator.configureFromIntCreator(creator);
    }

    @Test
    public void testLongCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        collector.addLongCreator(creator, false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mock(DeserializationConfig.class));

        assertNotNull(instantiator);
        assertTrue(instantiator instanceof StdValueInstantiator);
        instantiator.configureFromLongCreator(creator);
    }

    @Test
    public void testDoubleCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        collector.addDoubleCreator(creator, false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mock(DeserializationConfig.class));

        assertNotNull(instantiator);
        assertTrue(instantiator instanceof StdValueInstantiator);
        instantiator.configureFromDoubleCreator(creator);
    }

    @Test
    public void testBooleanCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        collector.addBooleanCreator(creator, false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mock(DeserializationConfig.class));

        assertNotNull(instantiator);
        assertTrue(instantiator instanceof StdValueInstantiator);
        instantiator.configureFromBooleanCreator(creator);
    }

    @Test
    public void testDelegatingCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[1];
        injectables[0] = mock(CreatorProperty.class);
        collector.addDelegatingCreator(creator, false, injectables);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mock(DeserializationConfig.class));

        assertNotNull(instantiator);
        assertTrue(instantiator instanceof StdValueInstantiator);
        instantiator.configureFromObjectSettings(creator, null, null, null, null, null);
        instantiator.configureFromDelegateCreator(creator);
        instantiator.configureDelegateArguments(injectables);
    }

    @Test
    public void testPropertyBasedCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);

        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[2];
        properties[0] = mock(CreatorProperty.class);
        properties[1] = mock(CreatorProperty.class);
        collector.addPropertyCreator(creator, false, properties);

        ValueInstantiator instantiator = collector.constructValueInstantiator(mock(DeserializationConfig.class));

        assertNotNull(instantiator);
        assertTrue(instantiator instanceof StdValueInstantiator);
        instantiator.configureFromObjectSettings(creator, null, null, null, null, null);
        instantiator.configureFromPropertyBasedCreator(creator);
        instantiator.configurePropertyBasedArguments(properties);
    }
}