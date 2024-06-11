package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;

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

public class CreatorCollectorTest {
    private CreatorCollector creatorCollector;
    private BeanDescription beanDescription;
    private MapperConfig<?> config;

    @Before
    public void setUp() {
        beanDescription = new BeanDescription();
        config = new MapperConfig();
        creatorCollector = new CreatorCollector(beanDescription, config);
    }

    @Test
    public void testConstructValueInstantiator() {
        DeserializationConfig deserializationConfig = new DeserializationConfig();
        JavaType javaType = new JavaType();
        creatorCollector.constructValueInstantiator(deserializationConfig);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addStringCreator(creator, true);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addIntCreator(creator, true);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addLongCreator(creator, true);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addDoubleCreator(creator, true);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addBooleanCreator(creator, true);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        creatorCollector.addDelegatingCreator(creator, true, injectables);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = new AnnotatedWithParams();
        SettableBeanProperty[] properties = new SettableBeanProperty[1];
        creatorCollector.addPropertyCreator(creator, true, properties);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);
    }

    @Test
    public void testHasDefaultCreator() {
        creatorCollector.hasDefaultCreator();
    }

    @Test
    public void testHasDelegatingCreator() {
        creatorCollector.hasDelegatingCreator();
    }

    @Test
    public void testHasPropertyBasedCreator() {
        creatorCollector.hasPropertyBasedCreator();
    }

    @Test
    public void testVerifyNonDup() {
        AnnotatedWithParams newOne = new AnnotatedWithParams();
        creatorCollector.verifyNonDup(newOne, 0, true);
    }
}