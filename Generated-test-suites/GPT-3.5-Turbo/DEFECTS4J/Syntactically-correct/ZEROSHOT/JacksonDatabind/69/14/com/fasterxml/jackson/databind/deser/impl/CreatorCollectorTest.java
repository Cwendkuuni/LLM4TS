package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Assert;
import org.junit.Test;

public class CreatorCollectorTest {

    @Test
    public void testSetDefaultCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);
        creatorCollector.setDefaultCreator(new AnnotatedWithParams());

        Assert.assertTrue(creatorCollector.hasDefaultCreator());
    }

    @Test
    public void testAddStringCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);
        creatorCollector.addStringCreator(new AnnotatedWithParams(), false);

        Assert.assertTrue(creatorCollector.hasNonDefaultCreator());
    }

    @Test
    public void testAddIntCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);
        creatorCollector.addIntCreator(new AnnotatedWithParams(), false);

        Assert.assertTrue(creatorCollector.hasNonDefaultCreator());
    }

    @Test
    public void testAddLongCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);
        creatorCollector.addLongCreator(new AnnotatedWithParams(), false);

        Assert.assertTrue(creatorCollector.hasNonDefaultCreator());
    }

    @Test
    public void testAddDoubleCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);
        creatorCollector.addDoubleCreator(new AnnotatedWithParams(), false);

        Assert.assertTrue(creatorCollector.hasNonDefaultCreator());
    }

    @Test
    public void testAddBooleanCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);
        creatorCollector.addBooleanCreator(new AnnotatedWithParams(), false);

        Assert.assertTrue(creatorCollector.hasNonDefaultCreator());
    }

    @Test
    public void testAddDelegatingCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);
        creatorCollector.addDelegatingCreator(new AnnotatedWithParams(), false, new SettableBeanProperty[0]);

        Assert.assertTrue(creatorCollector.hasNonDefaultCreator());
    }

    @Test
    public void testAddPropertyCreator() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);
        creatorCollector.addPropertyCreator(new AnnotatedWithParams(), false, new SettableBeanProperty[0]);

        Assert.assertTrue(creatorCollector.hasNonDefaultCreator());
    }

    @Test
    public void testAddIncompeteParameter() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig config = new MapperConfig();

        CreatorCollector creatorCollector = new CreatorCollector(beanDesc, config);
        creatorCollector.addIncompeteParameter(new AnnotatedParameter());

        Assert.assertNotNull(creatorCollector.getIncompleteParameter());
    }
}