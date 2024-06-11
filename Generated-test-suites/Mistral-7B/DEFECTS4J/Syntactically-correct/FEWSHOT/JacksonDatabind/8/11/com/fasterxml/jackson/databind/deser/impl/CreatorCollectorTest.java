package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class CreatorCollectorTest {
    @Test
    public void testCreatorCollectorDefaultConstructor() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        assertEquals(beanDesc, collector._beanDesc);
        assertTrue(collector._canFixAccess);
        assertNull(collector._creators);
        assertFalse(collector._hasNonDefaultCreator);
        assertNull(collector._delegateArgs);
        assertNull(collector._propertyBasedArgs);
        assertNull(collector._incompleteParameter);
    }

    @Test
    public void testSetDefaultCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams defaultCreator = mock(AnnotatedWithParams.class);
        collector.setDefaultCreator(defaultCreator);
        assertEquals(defaultCreator, collector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams stringCreator = mock(AnnotatedWithParams.class);
        collector.addStringCreator(stringCreator, false);
        assertEquals(stringCreator, collector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams intCreator = mock(AnnotatedWithParams.class);
        collector.addIntCreator(intCreator, false);
        assertEquals(intCreator, collector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams longCreator = mock(AnnotatedWithParams.class);
        collector.addLongCreator(longCreator, false);
        assertEquals(longCreator, collector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams doubleCreator = mock(AnnotatedWithParams.class);
        collector.addDoubleCreator(doubleCreator, false);
        assertEquals(doubleCreator, collector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams booleanCreator = mock(AnnotatedWithParams.class);
        collector.addBooleanCreator(booleanCreator, false);
        assertEquals(booleanCreator, collector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams delegatingCreator = mock(AnnotatedWithParams.class);
        CreatorProperty[] injectables = new CreatorProperty[0];
        collector.addDelegatingCreator(delegatingCreator, false, injectables);
        assertEquals(delegatingCreator, collector._creators[CreatorCollector.C_DELEGATE]);
        assertArrayEquals(injectables, collector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        BeanDescription beanDesc = mock(BeanDescription.class);
        CreatorCollector collector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams propertyCreator = mock(AnnotatedWithParams.class);
        CreatorProperty[] properties = new CreatorProperty[0];
        collector.addPropertyCreator(propertyCreator, false, properties);
        assertEquals(propertyCreator, collector._creators[CreatorCollector.C_PROPS]);
        assertArrayEquals(properties, collector._propertyBasedArgs);
    }
}