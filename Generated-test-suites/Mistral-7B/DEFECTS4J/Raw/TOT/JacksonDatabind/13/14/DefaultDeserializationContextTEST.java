package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.jupiters.java8.TestWorthy;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@TestWorthy
public class DefaultDeserializationContextTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private DefaultDeserializationContext context;

    @BeforeMethod
    public void setUp() throws Exception {
        context = mapper.getDeserializationContext();
    }

    @Test
    public void testFindObjectId() throws IOException {
        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("test");
            }
        };

        // Test with valid ID
        Object id = new Object();
        ReadableObjectId expected = context.findObjectId(id, idGenerator, SimpleObjectIdResolver.instance);
        Assert.assertNotNull(expected);
        Assert.assertEquals(expected.getKey().key, "test");

        // Test with null ID
        Object nullId = null;
        ReadableObjectId result = context.findObjectId(nullId, idGenerator, SimpleObjectIdResolver.instance);
        Assert.assertNull(result);

        // Test with existing ID
        ReadableObjectId existingId = context.findObjectId(id, idGenerator, SimpleObjectIdResolver.instance);
        ReadableObjectId expectedExistingId = context.findObjectId(id, idGenerator, SimpleObjectIdResolver.instance);
        Assert.assertEquals(existingId, expectedExistingId);

        // Test with new ID
        Object newId = new Object();
        ReadableObjectId newEntry = context.findObjectId(newId, idGenerator, SimpleObjectIdResolver.instance);
        Assert.assertNotNull(newEntry);
        Assert.assertFalse(context._objectIds.containsKey(newId.getClass()));

        // Test with multiple IDs
        List<Object> ids = List.of(new Object(), new Object());
        for (Object id : ids) {
            ReadableObjectId entry = context.findObjectId(id, idGenerator, SimpleObjectIdResolver.instance);
            Assert.assertNotNull(entry);
        }

        // Test with duplicate IDs
        Object duplicateId = ids.get(0);
        ReadableObjectId duplicateEntry = context.findObjectId(duplicateId, idGenerator, SimpleObjectIdResolver.instance);
        ReadableObjectId previousEntry = context.findObjectId(duplicateId, idGenerator, SimpleObjectIdResolver.instance);
        Assert.assertEquals(duplicateEntry, previousEntry);
    }

    @Test
    public void testCheckUnresolvedObjectId() throws JsonProcessingException {
        Object id1 = new Object();
        Object id2 = new Object();
        Object id3 = new Object();

        context.findObjectId(id1, new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("test1");
            }
        }, SimpleObjectIdResolver.instance);

        context.findObjectId(id2, new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("test1");
            }
        }, SimpleObjectIdResolver.instance);

        context.findObjectId(id3, new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("test2");
            }
        }, SimpleObjectIdResolver.instance);

        context.checkUnresolvedObjectId();

        // Test with unresolved object IDs
        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new ObjectIdGenerator.IdKey("test1"), null);
        context._objectIds.put(new ObjectIdGenerator.IdKey("test2"), null);

        try {
            context.checkUnresolvedObjectId();
            Assert.fail();
        } catch (UnresolvedForwardReference e) {
            Assert.assertTrue(e.getMessage().contains("Unresolved forward references for: test1"));
            Assert.assertTrue(e.getMessage().contains("test1.Class"));
        }
    }

    @Test
    public void testDeserializerInstance() throws IOException {
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        JsonDeserializer<Integer> integerDeserializer = new IntegerDeserializer();

        AnnotatedClass annotatedClass = mapper.getTypeFactory().constructType(TestBean.class);
        TestBean bean = mapper.readValue(getClass().getResource("/test-bean.json"), TestBean.class);

        ObjectIdGenerator idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("test");
            }
        };

        JsonDeserializer<TestBean> result = context.deserializerInstance(annotatedClass, stringDeserializer.getClass());
        Assert.assertNotNull(result);
        Assert.assertEquals(result, stringDeserializer);

        result = context.deserializerInstance(annotatedClass, integerDeserializer.getClass());
        Assert.assertNotNull(result);
        Assert.assertEquals(result, integerDeserializer);

        result = context.deserializerInstance(annotatedClass, TestBeanDeserializer.class);
        Assert.assertNotNull(result);
        Assert.assertInstanceOf(TestBeanDeserializer.class, result);

        result = context.deserializerInstance(annotatedClass, TestBeanDeserializerSub.class);
        Assert.assertNull(result);
    }

    static class StringDeserializer extends StdDeserializer<String> {}
    static class IntegerDeserializer extends StdDeserializer<Integer> {}
    static class TestBeanDeserializer extends JsonDeserializer<TestBean> {}
    static class TestBeanDeserialSub extends TestBeanDeserializer {}

    @JsonAutoDetect
    @JsonSubTypes({
            @JsonSubTypes.Type(value = TestBeanSub.class, name = "Sub")
    })
    static class TestBean {
        private String name;
        private TestBeanSub sub;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public TestBeanSub getSub() {
            return sub;
        }

        public void setSub(TestBeanSub sub) {
            this.sub = sub;
        }
    }

    static class TestBeanSub extends TestBean {
        // Empty
    }
}