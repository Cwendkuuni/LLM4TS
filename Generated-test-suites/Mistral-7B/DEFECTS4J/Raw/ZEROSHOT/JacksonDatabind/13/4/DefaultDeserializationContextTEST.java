package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.FindContext;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class DefaultDeserializationContextTest {

    private DeserializationConfig config;
    private JsonParser parser;
    private InjectableValues injectables;
    private DefaultDeserializationContext context;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        config = mapper.getDeserializationConfig();
        parser = mapper.getFactory().createParser("{}");
        injectables = mapper.getInjectableValues();
        context = new DefaultDeserializationContext.Impl(config, parser, injectables);
    }

    @Test
    public void testFindObjectId() {
        Object key = new Object();
        ObjectIdGenerator<?> generator = new StubObjectIdGenerator();
        ObjectIdResolver resolver = new StubObjectIdResolver();

        ReadableObjectId id = context.findObjectId(key, generator, resolver);
        assertNotNull(id);

        id = context.findObjectId(key, generator);
        assertNotNull(id);

        id = context.findObjectId(key, null);
        assertNull(id);
    }

    @Test
    public void testCheckUnresolvedObjectId() throws UnresolvedForwardReference {
        Object key1 = new Object();
        Object key2 = new Object();
        ObjectIdGenerator<?> generator = new StubObjectIdGenerator();
        ObjectIdResolver resolver = new StubObjectIdResolver();

        context.findObjectId(key1, generator, resolver);
        context.findObjectId(key2, generator, resolver);

        context.checkUnresolvedObjectId();

        // Unresolved reference
        context._objectIds.put(new IdKey(generator, key1), new ReadableObjectId(new IdKey(generator, key1)));

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            // Expected
        }
    }

    @Test
    public void testDeserializerInstance() {
        Object key = new Object();
        Object deserializerDef = new Object();

        JsonDeserializer<Object> deserializer = context.deserializerInstance(new AnnotatedClassTest(key), deserializerDef);
        assertNotNull(deserializer);
    }

    @Test
    public void testKeyDeserializerInstance() {
        Object key = new Object();
        Object deserializerDef = new Object();

        KeyDeserializer deserializer = context.keyDeserializerInstance(new AnnotatedClassTest(key), deserializerDef);
        assertNotNull(deserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateExceptionWhenCopyIsNotOverridden() {
        context.copy();
    }

    static class AnnotatedClassTest implements Annotated {
        private Object id;

        AnnotatedClassTest(Object id) {
            this.id = id;
        }

        @Override
        public ObjectIdGenerator<?> idGenerator() {
            return new StubObjectIdGenerator();
        }

        @Override
        public ObjectIdResolver idResolver() {
            return new StubObjectIdResolver();
        }

        @Override
        public Annotated forType(Class<?> type) {
            return this;
        }

        @Override
        public Annotated forProperty(String propertyName) {
            return this;
        }

        @Override
        public Annotated forMethod(Method method) {
            return this;
        }

        @Override
        public Annotated forConstructor(Constructor<?> constructor) {
            return this;
        }

        @Override
        public Annotated forValue(Object value) {
            return this;
        }

        @Override
        public Object getValue() {
            return id;
        }

        @Override
        public TypeDescriptor getValueType() {
            return null;
        }

        @Override
        public JsonTypeDescriptor getTypingFromAnnotation(Object ann) {
            return null;
        }

        @Override
        public JsonTypeDescriptor getGetterType(Method m) {
            return null;
        }

        @Override
        public JsonTypeDescriptor getSetterType(Method m) {
            return null;
        }

        @Override
        public JsonTypeDescriptor getParameterType(Method m, int p) {
            return null;
        }

        @Override
        public String getFullName() {
            return "test";
        }

        @Override
        public String getName() {
            return "test";
        }
    }

    static class StubObjectIdGenerator implements ObjectIdGenerator<Object> {
        @Override
        public ObjectIdGenerator.IdKey key(Object id) {
            return new IdKey(StubObjectIdGenerator.class, id);
        }
    }

    static class StubObjectIdResolver implements ObjectIdResolver {
        @Override
        public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
            return this;
        }

        @Override
        public ReadableObjectId idFromObject(Object id) {
            return new ReadableObjectId(new IdKey(StubObjectIdResolver.class, id));
        }
    }
}