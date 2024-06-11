
package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testConstructor() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        assertNotNull(creatorCollector);
    }

    @Test
    public void testConstructValueInstantiator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        DeserializationConfig config = new DeserializationConfig();
        ValueInstantiator valueInstantiator = creatorCollector.constructValueInstantiator(config);
        assertNotNull(valueInstantiator);
    }

    @Test
    public void testSetDefaultCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DEFAULT]);
    }

    @Test
    public void testAddStringCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addStringCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_STRING]);
    }

    @Test
    public void testAddIntCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addIntCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_INT]);
    }

    @Test
    public void testAddLongCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addLongCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_LONG]);
    }

    @Test
    public void testAddDoubleCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addDoubleCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DOUBLE]);
    }

    @Test
    public void testAddBooleanCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.addBooleanCreator(creator, false);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_BOOLEAN]);
    }

    @Test
    public void testAddDelegatingCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] injectables = new CreatorProperty[1];
        creatorCollector.addDelegatingCreator(creator, false, injectables);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_DELEGATE]);
        assertEquals(injectables, creatorCollector._delegateArgs);
    }

    @Test
    public void testAddPropertyCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedWithParams creator = new AnnotatedWithParams();
        CreatorProperty[] properties = new CreatorProperty[1];
        creatorCollector.addPropertyCreator(creator, false, properties);
        assertEquals(creator, creatorCollector._creators[CreatorCollector.C_PROPS]);
        assertEquals(properties, creatorCollector._propertyBasedArgs);
    }

    @Test
    public void testAddIncompeteParameter() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        AnnotatedParameter parameter = new AnnotatedParameter();
        creatorCollector.addIncompeteParameter(parameter);
        assertEquals(parameter, creatorCollector._incompleteParameter);
    }

    @Test
    public void testHasDefaultCreator() {
        BeanDescription beanDesc = new BeanDescription();
        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, true);
        assertFalse(creatorCollector.hasDefaultCreator());

        AnnotatedWithParams creator = new AnnotatedWithParams();
        creatorCollector.setDefaultCreator(creator);
        assertTrue(creatorCollector.hasDefaultCreator());
    }
}
