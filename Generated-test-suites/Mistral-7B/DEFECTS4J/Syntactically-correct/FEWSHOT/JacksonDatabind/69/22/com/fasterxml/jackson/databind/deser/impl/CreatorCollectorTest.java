package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testCreatorCollectorDefaultConstructor() {
        BeanDescription beanDesc = null;
        MapperConfig<Object> config = null;
        CreatorCollector collector = new CreatorCollector(beanDesc, config);
        assertNull(collector._creators[CreatorCollector.C_DEFAULT]);
        assertFalse(collector.hasDefaultCreator());
    }

    @Test
    public void testConstructValueInstantiator() {
        BeanDescription beanDesc = null;
        MapperConfig<Object> config = null;
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        ValueInstantiator inst = collector.constructValueInstantiator(null);
        assertNull(inst);
    }

    @Test
    public void testSetDefaultCreator() {
        BeanDescription beanDesc = null;
        MapperConfig<Object> config = null;
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JavaType type = null;
        AnnotatedWithParams creator = null;
        collector.setDefaultCreator(creator);
        assertNull(collector._creators[CreatorCollector.C_DEFAULT]);
        assertFalse(collector.hasDefaultCreator());

        collector.setDefaultCreator(creator);
        collector.setDefaultCreator(creator);
        collector.setDefaultCreator(creator);
        assertEquals(creator, collector._creators[CreatorCollector.C_DEFAULT]);
        assertTrue(collector.hasDefaultCreator());

        type = null;
        creator = new AnnotatedWithParams(null, type, null, null, null);
        collector.setDefaultCreator(creator);
        assertEquals(creator, collector._creators[CreatorCollector.C_DEFAULT]);
        assertTrue(collector.hasDefaultCreator());
    }

    @Test
    public void testAddStringCreator() {
        BeanDescription beanDesc = null;
        MapperConfig<Object> config = null;
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JavaType type = null;
        AnnotatedWithParams creator = null;
        collector.addStringCreator(creator, false);
        assertNull(collector._creators[CreatorCollector.C_STRING]);
        assertFalse(collector.hasStringCreator());

        collector.addStringCreator(creator, false);
        collector.addStringCreator(creator, false);
        collector.addStringCreator(creator, false);
        assertEquals(creator, collector._creators[CreatorCollector.C_STRING]);
        assertTrue(collector.hasStringCreator());

        creator = new AnnotatedWithParams(null, type, null, null, null);
        collector.addStringCreator(creator, true);
        assertTrue(collector.hasExplicitStringCreator());
    }

    @Test
    public void testAddIntCreator() {
        BeanDescription beanDesc = null;
        MapperConfig<Object> config = null;
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JavaType type = null;
        AnnotatedWithParams creator = null;
        collector.addIntCreator(creator, false);
        assertNull(collector._creators[CreatorCollector.C_INT]);
        assertFalse(collector.hasIntCreator());

        collector.addIntCreator(creator, false);
        collector.addIntCreator(creator, false);
        collector.addIntCreator(creator, false);
        assertEquals(creator, collector._creators[CreatorCollector.C_INT]);
        assertTrue(collector.hasIntCreator());

        creator = new AnnotatedWithParams(null, type, null, null, null);
        collector.addIntCreator(creator, true);
        assertTrue(collector.hasExplicitIntCreator());
    }

    @Test
    public void testAddLongCreator() {
        BeanDescription beanDesc = null;
        MapperConfig<Object> config = null;
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JavaType type = null;
        AnnotatedWithParams creator = null;
        collector.addLongCreator(creator, false);
        assertNull(collector._creators[CreatorCollector.C_LONG]);
        assertFalse(collector.hasLongCreator());

        collector.addLongCreator(creator, false);
        collector.addLongCreator(creator, false);
        collector.addLongCreator(creator, false);
        assertEquals(creator, collector._creators[CreatorCollector.C_LONG]);
        assertTrue(collector.hasLongCreator());

        creator = new AnnotatedWithParams(null, type, null, null, null);
        collector.addLongCreator(creator, true);
        assertTrue(collector.hasExplicitLongCreator());
    }

    @Test
    public void testAddDoubleCreator() {
        BeanDescription beanDesc = null;
        MapperConfig<Object> config = null;
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JavaType type = null;
        AnnotatedWithParams creator = null;
        collector.addDoubleCreator(creator, false);
        assertNull(collector._creators[CreatorCollector.C_DOUBLE]);
        assertFalse(collector.hasDoubleCreator());

        collector.addDoubleCreator(creator, false);
        collector.addDoubleCreator(creator, false);
        collector.addDoubleCreator(creator, false);
        assertEquals(creator, collector._creators[CreatorCollector.C_DOUBLE]);
        assertTrue(collector.hasDoubleCreator());

        creator = new AnnotatedWithParams(null, type, null, null, null);
        collector.addDoubleCreator(creator, true);
        assertTrue(collector.hasExplicitDoubleCreator());
    }

    @Test
    public void testAddBooleanCreator() {
        BeanDescription beanDesc = null;
        MapperConfig<Object> config = null;
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JavaType type = null;
        AnnotatedWithParams creator = null;
        collector.addBooleanCreator(creator, false);
        assertNull(collector._creators[CreatorCollector.C_BOOLEAN]);
        assertFalse(collector.hasBooleanCreator());

        collector.addBooleanCreator(creator, false);
        collector.addBooleanCreator(creator, false);
        collector.addBooleanCreator(creator, false);
        assertEquals(creator, collector._creators[CreatorCollector.C_BOOLEAN]);
        assertTrue(collector.hasBooleanCreator());

        creator = new AnnotatedWithParams(null, type, null, null, null);
        collector.addBooleanCreator(creator, true);
        assertTrue(collector.hasExplicitBooleanCreator());
    }

    @Test
    public void testAddDelegatingCreator() {
        BeanDescription beanDesc = null;
        MapperConfig<Object> config = null;
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JavaType type = null;
        AnnotatedWithParams creator = null;
        SettableBeanProperty[] injectables = null;
        collector.addDelegatingCreator(creator, false, injectables);
        assertNull(collector._creators[CreatorCollector.C_DELEGATE]);
        assertFalse(collector.hasDelegatingCreator());

        collector.addDelegatingCreator(creator, false, injectables);
        collector.addDelegatingCreator(creator, false, injectables);
        collector.addDelegatingCreator(creator, false, injectables);
        assertEquals(creator, collector._creators[CreatorCollector.C_DELEGATE]);
        assertTrue(collector.hasDelegatingCreator());

        collector.addDelegatingCreator(creator, true, injectables);
        assertTrue(collector.hasExplicitDelegatingCreator());

        SettableBeanProperty[] arrayInjectables = new SettableBeanProperty[1];
        collector.addArrayDelegateCreator(creator, false, arrayInjectables);
        assertNull(collector._creators[CreatorCollector.C_ARRAY_DELEGATE]);
        assertFalse(collector.hasArrayDelegateCreator());

        collector.addArrayDelegateCreator(creator, false, arrayInjectables);
        collector.addArrayDelegateCreator(creator, false, arrayInjectables);
        collector.addArrayDelegateCreator(creator, false, arrayInjectables);
        assertEquals(creator, collector._creators[CreatorCollector.C_ARRAY_DELEGATE]);
        assertTrue(collector.hasArrayDelegateCreator());

        collector.addArrayDelegateCreator(creator, true, arrayInjectables);
        assertTrue(collector.hasExplicitArrayDelegateCreator());
    }

    @Test
    public void testAddPropertyCreator() {
        BeanDescription beanDesc = null;
        MapperConfig<Object> config = null;
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JavaType type = null;
        AnnotatedWithParams creator = null;
        SettableBeanProperty[] properties = null;
        collector.addPropertyCreator(creator, false, properties);
        assertNull(collector._creators[CreatorCollector.C_PROPS]);
        assertFalse(collector.hasPropertyBasedCreator());

        collector.addPropertyCreator(creator, false, properties);
        collector.addPropertyCreator(creator, false, properties);
        collector.addPropertyCreator(creator, false, properties);
        assertEquals(creator, collector._creators[CreatorCollector.C_PROPS]);
        assertTrue(collector.hasPropertyBasedCreator());

        collector.addPropertyCreator(creator, true, properties);
        assertTrue(collector.hasExplicitPropertyBasedCreator());
    }
}