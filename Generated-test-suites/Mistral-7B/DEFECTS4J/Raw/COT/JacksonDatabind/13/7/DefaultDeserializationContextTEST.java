package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.util.*;
import java.util.Map.Entry;

import org.junit.Test;

public class DefaultDeserializationContextTest {

    @Test
    public void testConstructorWithDeserializerFactoryAndCache() {
        DefaultDeserializationContext context = new DefaultDeserializationContext(new StubDeserializerFactory(), new StubDeserializerCache());
        assertNotNull(context);
    }

    @Test
    public void testConstructorWithSourceAndConfig() {
        DefaultDeserializationContext srcContext = new DefaultDeserializationContext(new DefaultDeserializationContext(), new DeserializationConfig(), null, null);
        DefaultDeserializationContext context = new DefaultDeserializationContext(srcContext, null, null, null);
        assertNotNull(context);
    }

    @Test
    public void testConstructorWithSource() {
        DefaultDeserializationContext srcContext = new DefaultDeserializationContext();
        DefaultDeserializationContext context = new DefaultDeserializationContext(srcContext);
        assertNotNull(context);
    }

    @Test
    public void testConstructorWithSourceAndFactory() {
        DefaultDeserializationContext srcContext = new DefaultDeserializationContext();
        DefaultDeserializationContext context = new DefaultDeserializationContext(srcContext, new StubDeserializerFactory());
        assertNotNull(context);
    }

    @Test
    public void testCopy() {
        DefaultDeserializationContext context = new DefaultDeserializationContext();
        DefaultDeserializationContext copiedContext = context.copy();
        assertNotSame(context, copiedContext);
        assertEquals(context.getClass(), copiedContext.getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testCopyThrowsExceptionWhenSubclassDoesNotOverrideCopy() {
        new DefaultDeserializationContext() {
            @Override
            public DefaultDeserializationContext copy() {
                // Do not override copy method
            }
        }.copy();
    }

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext();
        ObjectIdGenerator generator = new StubObjectIdGenerator();
        ObjectIdResolver resolver = new StubObjectIdResolver();

        ObjectIdKey key = generator.key(new Object());
        ReadableObjectId readableObjectId = context.findObjectId(new Object(), generator, resolver);
        assertNotNull(readableObjectId);
    }

    @Test
    public void testFindObjectIdWithSimpleObjectIdResolver() {
        DefaultDeserializationContext context = new DefaultDeserializationContext();
        ObjectIdGenerator generator = new StubObjectIdGenerator();

        ObjectIdKey key = generator.key(new Object());
        ReadableObjectId readableObjectId = context.findObjectId(new Object(), generator);
        assertNotNull(readableObjectId);
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectIdThrowsExceptionWhenIdsAreUnresolved() {
        DefaultDeserializationContext context = new DefaultDeserializationContext();
        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new ObjectIdGenerator.IdKey(ObjectIdGenerator.IdKey.Class, Object.class), new ReadableObjectId(new ObjectIdGenerator.IdKey(ObjectIdGenerator.IdKey.Class, Object.class)));

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // Expected
        }
    }

    @Test
    public void testDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext();
        StubAnnotated ann = new StubAnnotated();
        StubDeserializer deserializer = new StubDeserializer();
        Object deserDef = StubDeserializer.class;

        JsonDeserializer<Object> deserializerInstance = context.deserializerInstance(ann, deserDef);
        assertNotNull(deserializerInstance);
        assertEquals(deserializer, deserializerInstance);
    }

    @Test
    public void testKeyDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext();
        StubAnnotated ann = new StubAnnotated();
        StubKeyDeserializer deserializer = new StubKeyDeserializer();
        Object deserDef = StubKeyDeserializer.class;

        KeyDeserializer keyDeserializerInstance = context.keyDeserializerInstance(ann, deserDef);
        assertNotNull(keyDeserializerInstance);
        assertEquals(deserializer, keyDeserializerInstance);
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext context = new DefaultDeserializationContext();
        DefaultDeserializationContext withContext = context.with(new StubDeserializerFactory());
        assertNotSame(context, withContext);
        assertEquals(StubDeserializerFactory.class, withContext.getDeserializerFactory().getClass());
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext();
        DeserializationConfig config = new DeserializationConfig();
        JsonParser jp = null;
        InjectableValues values = null;

        DefaultDeserializationContext createdInstance = context.createInstance(config, jp, values);
        assertNotNull(createdInstance);
    }

    private static class StubDeserializerFactory implements DeserializerFactory {
        @Override
        public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, Annotated member, Class<?> paramType)
                throws JsonMappingException {
            return null;
        }

        @Override
        public JsonDeserializer<?> createValueDeserializer(DeserializationConfig config, JavaType type, AnnotationMetaInfo annotations)
                throws JsonMappingException {
            return null;
        }

        @Override
        public KeyDeserializer createKeyDeserializer(DeserializationConfig config, Annotated keyAnnotations, JavaType keyType)
                throws JsonMappingException {
            return null;
        }
    }

    private static class StubDeserializerCache implements DeserializerCache {
        @Override
        public void clear() {
            // Empty
        }

        @Override
        public JsonDeserializer<?> findDeserializer(DeserializationConfig config, BeanDescription beanDesc, TypeDeserializer valueTypeDeserializer) {
            return null;
        }

        @Override
        public JsonDeserializer<?> findValueDeserializer(DeserializationConfig config, Annotated member, Class<?> paramType) {
            return null;
        }

        @Override
        public KeyDeserializer findKeyDeserializer(DeserializationConfig config, Annotated keyAnnotations, JavaType keyType) {
            return null;
        }
    }

    private static class StubObjectIdGenerator implements ObjectIdGenerator {
        @Override
        public ObjectIdGenerator.IdKey key(Object id) {
            return new ObjectIdGenerator.IdKey(ObjectIdGenerator.IdKey.Class, Object.class);
        }
    }

    private static class StubObjectIdResolver implements ObjectIdResolver {
        @Override
        public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
            return this;
        }
    }

    private static class StubDeserializer implements JsonDeserializer<Object> {
        static class None {
            // Marker class
        }
    }

    private static class StubKeyDeserializer implements KeyDeserializer {
        static class None {
            // Marker class
        }
    }

    private static class StubAnnotated implements Annotated {
        @Override
        public JavaType getValueType() {
            return null;
        }

        @Override
        public AnnotationMetaInfo annotations() {
            return null;
        }
    }
}