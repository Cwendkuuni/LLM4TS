package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class CreatorCollectorTest {

    @Test
    public void testConstructor() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        assertNotNull(collector);
        assertSame(beanDesc, collector._beanDesc);
        assertEquals(config.canOverrideAccessModifiers(), collector._canFixAccess);
        assertEquals(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS), collector._forceAccess);
    }

    @Test
    public void testHasDefaultCreator() {
        CreatorCollector collector = new CreatorCollector(new BeanDescription(), new MapperConfig<>());

        assertFalse(collector.hasDefaultCreator());

        collector.setDefaultCreator(new AnnotatedWithParams());

        assertTrue(collector.hasDefaultCreator());
    }

    @Test
    public void testHasDelegatingCreator() {
        CreatorCollector collector = new CreatorCollector(new BeanDescription(), new MapperConfig<>());

        assertFalse(collector.hasDelegatingCreator());

        collector.addDelegatingCreator(new AnnotatedWithParams(), false, new CreatorProperty[] {});

        assertTrue(collector.hasDelegatingCreator());
    }

    @Test
    public void testHasPropertyBasedCreator() {
        CreatorCollector collector = new CreatorCollector(new BeanDescription(), new MapperConfig<>());

        assertFalse(collector.hasPropertyBasedCreator());

        collector.addPropertyCreator(new AnnotatedWithParams(), false, new CreatorProperty[] {});

        assertTrue(collector.hasPropertyBasedCreator());
    }
}