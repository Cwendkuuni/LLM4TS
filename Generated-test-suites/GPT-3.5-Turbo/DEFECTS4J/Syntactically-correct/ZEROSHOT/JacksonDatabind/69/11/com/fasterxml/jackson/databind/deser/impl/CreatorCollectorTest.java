package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @BeforeEach
    public void setup() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        MapperConfig<?> config = mock(MapperConfig.class);
        creatorCollector = new CreatorCollector(beanDesc, config);
    }

    @Test
    public void testSetDefaultCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.setDefaultCreator(creator);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addStringCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addIntCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addLongCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addDoubleCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        creatorCollector.addBooleanCreator(creator, false);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] injectables = {mock(SettableBeanProperty.class)};
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertSame(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        AnnotatedWithParams creator = mock(AnnotatedWithParams.class);
        SettableBeanProperty[] properties = {mock(SettableBeanProperty.class)};
        creatorCollector.addPropertyCreator(creator, false, properties);
        assertSame(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertSame(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        AnnotatedParameter parameter = mock(AnnotatedParameter.class);
        creatorCollector.addIncompeteParameter(parameter);
        assertSame(parameter, creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        creatorCollector._creators[CreatorCollector.C_DEFAULT] = mock(AnnotatedWithParams.class);
        assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testHasDelegatingCreator() {
        creatorCollector._creators[CreatorCollector.C_DELEGATE] = mock(AnnotatedWithParams.class);
        assertTrue(creatorCollector.hasDelegatingCreator());
    }

    @Test
    public void testHasPropertyBasedCreator() {
        creatorCollector._creators[CreatorCollector.C_PROPS] = mock(AnnotatedWithParams.class);
        assertTrue(creatorCollector.hasPropertyBasedCreator());
    }

}