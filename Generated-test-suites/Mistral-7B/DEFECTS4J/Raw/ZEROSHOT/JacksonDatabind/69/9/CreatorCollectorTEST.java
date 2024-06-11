package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Member;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Unit tests for {@link CreatorCollector} class.
 */
public class CreatorCollectorTest {

    private CreatorCollector collector;
    private MapperConfig<ObjectMapper> config;
    private BeanDescription beanDesc;

    @Before
    public void setUp() throws Exception {
        config = Mockito.mock(MapperConfig.class);
        beanDesc = Mockito.mock(BeanDescription.class);
        collector = new CreatorCollector(beanDesc, config);
    }

    @Test
    public void testDefaultCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        collector.setDefaultCreator(creator);
        assertTrue(collector.hasDefaultCreator());
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        collector.addStringCreator(creator, false);
        assertTrue(collector.hasStringCreator());
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        collector.addIntCreator(creator, false);
        assertTrue(collector.hasIntCreator());
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        collector.addLongCreator(creator, false);
        assertTrue(collector.hasLongCreator());
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        collector.addDoubleCreator(creator, false);
        assertTrue(collector.hasDoubleCreator());
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        collector.addBooleanCreator(creator, false);
        assertTrue(collector.hasBooleanCreator());
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        SettableBeanProperty[] injectables = new SettableBeanProperty[1];
        injectables[0] = Mockito.mock(SettableBeanProperty.class);
        collector.addDelegatingCreator(creator, false, injectables);
        assertNotNull(collector.getDelegateType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDuplicateCreator() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        collector.addStringCreator(creator, false);
        collector.addStringCreator(creator, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddExplicitCreatorTwice() {
        AnnotatedWithParams creator = Mockito.mock(AnnotatedWithParams.class);
        collector.addStringCreator(creator, true);
        collector.addStringCreator(creator, true);
    }

    @Test
    public void testGetValueInstantiator() throws Exception {
        JavaType delegateType = Mockito.mock(JavaType.class);
        JavaType arrayDelegateType = Mockito.mock(JavaType.class);
        ValueInstantiator inst = collector.constructValueInstantiator(Mockito.mock(DeserializationConfig.class));

        assertEquals(delegateType, inst.getDelegateType());
        assertEquals(arrayDelegateType, inst.getArrayDelegateType());
    }
}