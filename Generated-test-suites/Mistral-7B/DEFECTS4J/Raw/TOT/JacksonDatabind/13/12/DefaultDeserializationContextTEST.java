package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator$AlwaysStableIdGenerator;

public class DefaultDeserializationContextTest {

    private static class TestBean {
        private int id;
        private String name;

        public TestBean(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return "TestBean{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    private static void testFindObjectId(DefaultDeserializationContext context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        ContextualDeserializerProvider prov = new ContextualDeserializerProvider();
        prov.init(parser, ObjectMapper.getDefaultConfig(), new ObjectMapper());
        JsonDeserializer<TestBean> deserializer = prov.findBeanDeserializer(TestBean.class);

        TestBean bean1 = new TestBean(1, "TestBean1");
        TestBean bean2 = new TestBean(2, "TestBean2");
        ObjectIdGenerator<Object> idGen = new ObjectIdGenerator$AlwaysStableIdGenerator();

        // First test: findObjectId with existing id
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId readableId1 = context.findObjectId(bean1, idGen, resolver);
        assertEquals(bean1, readableId1.getId());

        // Test: findObjectId with null id
        ObjectIdResolver nullResolver = null;
        ReadableObjectId readableId2 = context.findObjectId(null, idGen, nullResolver);
        assertNull(readableId2);

        // Test: findObjectId with same id but different type
        TestBean bean3 = new TestBean(1, "TestBean3");
        ReadableObjectId readableId3 = context.findObjectId(bean3, idGen, resolver);
        assertNotEquals(bean1, readableId3.getId());

        // Test: findObjectId with a circular reference
        TestBean testBeanA = new TestBean(1, "TestBeanA");
        TestBean testBeanB = new TestBean(2, "TestBeanB");
        testBeanA.setIdProperty(testBeanB);
        testBeanB.setIdProperty(testBeanA);

        ReadableObjectId readableIdA = context.findObjectId(testBeanA, idGen, resolver);
        assertEquals(testBeanA, readableIdA.getId());

        ReadableObjectId readableIdB = context.findObjectId(testBeanB, idGen, resolver);
        assertEquals(testBeanB, readableIdB.getId());

        // Test: findObjectId with unregistered ObjectIdResolver
        ObjectIdResolver unregisteredResolver = new TestObjectIdResolver();
        ReadableObjectId readableId4 = context.findObjectId(bean1, idGen, unregisteredResolver);
        assertNull(readableId4);

        context.registerObjectIdResolver(unregisteredResolver);
        ReadableObjectId readableId5 = context.findObjectId(bean1, idGen, unregisteredResolver);
        assertEquals(bean1, readableId5.getId());

        // Test: checkUnresolvedObjectId
        context.registerObjectIdResolver(new TestUnregisteredObjectIdResolver());
        context.checkUnresolvedObjectId();
        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference");
        } catch (UnresolvedForwardReference e) {
            assertEquals(1, e.size());
            assertEquals(TestBean.class, e.getUnresolvedId(0).getDeserializer().getClass());
        }

        context.unregisterObjectIdResolver(new TestUnregisteredObjectIdResolver());
    }

    private static class TestObjectIdResolver extends SimpleObjectIdResolver {
        @Override
        public JsonDeserializer<?> newDeserializerInstance(DeserializationConfig config, Annotated param, JavaType type) {
            return new TestObjectDeserializer();
        }
    }

    private static class TestUnregisteredObjectIdResolver extends SimpleObjectIdResolver {
        // Empty implementation
    }

    private static class TestObjectDeserializer implements JsonDeserializer<TestBean> {
        @Override
        public TestBean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new TestBean(ctxt.getIdValue(), null);
        }

        @Override
        public void resolve(DeserializationContext ctxt) throws JsonProcessingException {
            if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_EMPTY_BEANS)) {
                throw new JsonProcessingException("Empty bean encountered");
            }
        }

        @Override
        public JavaType getValueType(DeserializationContext ctxt) throws JsonMappingException {
            return TestBean.class;
        }
    }

    @org.junit.Test
    public void testFindObjectId() throws JsonProcessingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new ObjectMapper().getFactory().getDeserializerFactory());
        testFindObjectId(context);
    }

    @org.junit.Test
    public void testFindObjectIdWithConfig() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        ContextualDeserializerProvider prov = new ContextualDeserializerProvider();
        prov.init(parser, ObjectMapper.getDefaultConfig(), new ObjectMapper());
        JsonDeserializer<TestBean> deserializer = prov.findBeanDeserializer(TestBean.class);

        DeserializationConfig config = mapper.getConfig();
        config.setFailureHandler(new FailureHandler() {
            @Override
            public void handleMissingValueException(MissingValueException p) throws IOException {
                throw new RuntimeException("Unexpected MissingValueException", p);
            }

            @Override
            public void handleProcessingException(ProcessingException p) throws IOException {
                throw new RuntimeException("Unexpected ProcessingException", p);
            }
        });

        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new ObjectMapper().getFactory().getDeserializerFactory(), config);
        testFindObjectId(context);
    }

    @org.junit.Test
    public void testFindObjectIdWithHandlerInstantiator() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        ContextualDeserializerProvider prov = new ContextualDeserializerProvider();
        prov.init(parser, ObjectMapper.getDefaultConfig(), new ObjectMapper());

        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {
            @Override
            public <T> T findDeserializer(DeserializationConfig config, Annotated param, JavaType type) {
                if (TestBean.class.equals(type)) {
                    return (T) new TestObjectDeserializer();
                }
                return null;
            }
        };

        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new ObjectMapper().getFactory().getDeserializerFactory(), ObjectMapper.getDefaultConfig(), handlerInstantiator);
        testFindObjectId(context);
    }
}