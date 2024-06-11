package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

import org.junit.Test;

/**
 * Test class for CreatorCollector.
 */
public class CreatorCollectorTest {

    @Test
    public void testConstructValueInstantiator() throws Exception {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JsonDeserializer<List<String>> listDeserializer = new StdDeserializer<List<String>>(new TypeReference<List<String>>(){}.getType()) {
            @Override
            public List<String> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return new ArrayList<>();
            }
        };

        JsonDeserializer<Map<String, String>> mapDeserializer = new StdDeserializer<Map<String, String>>(new TypeReference<Map<String, String>>(){}.getType()) {
            @Override
            public Map<String, String> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return new LinkedHashMap<>();
            }
        };

        collector.setDefaultCreator(Annotations.createDefaultCreator(null, null, Introspect.noOpContext()));
        collector.addIntCreator(Annotations.createIntCreator(1, Introspect.noOpContext()), false);
        collector.addIntCreator(Annotations.createIntCreator(2, Introspect.noOpContext()), false);
        collector.addStringCreator(Annotations.createStringCreator("test"), false);
        collector.addBooleanCreator(Annotations.createBooleanCreator(false, Introspect.noOpContext()), false);
        collector.addBooleanCreator(Annotations.createBooleanCreator(true, Introspect.noOpContext()), false);
        collector.addDelegatingCreator(Annotations.createDelegatingCreator(listDeserializer, Introspect.noOpContext(), null), false, new SettableBeanProperty[0]);
        collector.addDelegatingCreator(Annotations.createDelegatingCreator(mapDeserializer, Introspect.noOpContext(), null), false, new SettableBeanProperty[0]);

        ValueInstantiator instantiator = collector.constructValueInstantiator(new DeserializationConfig());

        Class<?> type = Class.forName("java.util.ArrayList");
        assertEquals(type, instantiator.getType());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructValueInstantiatorWithDupIntCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JsonDeserializer<List<String>> listDeserializer = new StdDeserializer<List<String>>(new TypeReference<List<String>>(){}.getType()) {
            @Override
            public List<String> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return new ArrayList<>();
            }
        };

        collector.setDefaultCreator(Annotations.createDefaultCreator(null, null, Introspect.noOpContext()));
        collector.addIntCreator(Annotations.createIntCreator(1, Introspect.noOpContext()), false);
        collector.addIntCreator(Annotations.createIntCreator(1, Introspect.noOpContext()), false);

        collector.constructValueInstantiator(new DeserializationConfig());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructValueInstantiatorWithDupStringCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        collector.setDefaultCreator(Annotations.createDefaultCreator(null, null, Introspect.noOpContext()));
        collector.addStringCreator(Annotations.createStringCreator("test"), false);
        collector.addStringCreator(Annotations.createStringCreator("test"), false);

        collector.constructValueInstantiator(new DeserializationConfig());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructValueInstantiatorWithDupBooleanCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        collector.setDefaultCreator(Annotations.createDefaultCreator(null, null, Introspect.noOpContext()));
        collector.addBooleanCreator(Annotations.createBooleanCreator(false, Introspect.noOpContext()), false);
        collector.addBooleanCreator(Annotations.createBooleanCreator(true, Introspect.noOpContext()), false);
        collector.addBooleanCreator(Annotations.createBooleanCreator(false, Introspect.noOpContext()), false);

        collector.constructValueInstantiator(new DeserializationConfig());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructValueInstantiatorWithDupDelegatingCreator() throws Exception {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JsonDeserializer<List<String>> listDeserializer = new StdDeserializer<List<String>>(new TypeReference<List<String>>(){}.getType()) {
            @Override
            public List<String> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return new ArrayList<>();
            }
        };

        collector.setDefaultCreator(Annotations.createDefaultCreator(null, null, Introspect.noOpContext()));
        collector.addDelegatingCreator(Annotations.createDelegatingCreator(listDeserializer, Introspect.noOpContext(), new SettableBeanProperty[0]), false, new SettableBeanProperty[0]);
        collector.addDelegatingCreator(Annotations.createDelegatingCreator(listDeserializer, Introspect.noOpContext(), new SettableBeanProperty[0]), false, new SettableBeanProperty[0]);

        collector.constructValueInstantiator(new DeserializationConfig());
    }

    @Test
    public void testGetters() throws Exception {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        assertEquals(false, collector.hasDefaultCreator());
        assertEquals(false, collector.hasDelegatingCreator());
        assertEquals(false, collector.hasPropertyBasedCreator());
    }

    @Test
    public void testGettersWithCustomCreators() throws Exception {
        BeanDescription beanDesc = new BeanDescription();
        MapperConfig<?> config = new MapperConfig<>();
        CreatorCollector collector = new CreatorCollector(beanDesc, config);

        JsonDeserializer<List<String>> listDeserializer = new StdDeserializer<List<String>>(new TypeReference<List<String>>(){}.getType()) {
            @Override
            public List<String> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return new ArrayList<>();
            }
        };

        JsonDeserializer<Map<String, String>> mapDeserializer = new StdDeserializer<Map<String, String>>(new TypeReference<Map<String, String>>(){}.getType()) {
            @Override
            public Map<String, String> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return new LinkedHashMap<>();
            }
        };

        collector.setDefaultCreator(Annotations.createDefaultCreator(null, null, Introspect.noOpContext()));
        collector.addIntCreator(Annotations.createIntCreator(1, Introspect.noOpContext()), false);
        collector.addIntCreator(Annotations.createIntCreator(2, Introspect.noOpContext()), false);
        collector.addStringCreator(Annotations.createStringCreator("test"), false);
        collector.addBooleanCreator(Annotations.createBooleanCreator(false, Introspect.noOpContext()), false);
        collector.addBooleanCreator(Annotations.createBooleanCreator(true, Introspect.noOpContext()), false);
        collector.addDelegatingCreator(Annotations.createDelegatingCreator(listDeserializer, Introspect.noOpContext(), new SettableBeanProperty[0]), false, new SettableBeanProperty[0]);
        collector.addDelegatingCreator(Annotations.createDelegatingCreator(mapDeserializer, Introspect.noOpContext(), new SettableBeanProperty[0]), false, new SettableBeanProperty[0]);

        assertEquals(true, collector.hasDefaultCreator());
        assertEquals(true, collector.hasDelegatingCreator());
        assertEquals(true, collector.hasPropertyBasedCreator());
    }
}