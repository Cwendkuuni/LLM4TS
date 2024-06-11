package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;

public class CreatorCollectorTest {

    private CreatorCollector creatorCollector;

    @Before
    public void setUp() {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        creatorCollector = new CreatorCollector(beanDesc, config);
    }

    @Test
    public void testConstructValueInstantiator() throws IOException {
        DeserializationConfig config = new DeserializationConfig();
        ValueInstantiator instantiator = creatorCollector.constructValueInstantiator(config);

        assertNotNull(instantiator);
    }

    @Test
    public void testHasDefaultCreator() {
        boolean hasDefaultCreator = creatorCollector.hasDefaultCreator();

        assertFalse(hasDefaultCreator);
    }

    @Test
    public void testHasDelegatingCreator() {
        boolean hasDelegatingCreator = creatorCollector.hasDelegatingCreator();

        assertFalse(hasDelegatingCreator);
    }

    @Test
    public void testHasPropertyBasedCreator() {
        boolean hasPropertyBasedCreator = creatorCollector.hasPropertyBasedCreator();

        assertFalse(hasPropertyBasedCreator);
    }
}