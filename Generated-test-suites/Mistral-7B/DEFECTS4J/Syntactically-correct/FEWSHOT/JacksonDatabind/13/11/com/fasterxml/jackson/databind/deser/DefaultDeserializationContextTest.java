package com.fasterxml.jackson.databind.deser.test;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator$IdKey;
import com.fasterxml.jackson.databind.util.ObjectIdGenerator$SimpleObjectIdResolver;

/**
 * Test class for DefaultDeserializationContext
 */
public class DefaultDeserializationContextTest {

    @JsonAutoDetect(fieldVisibility = NONE, getterVisibility = NONE, isGetterVisibility = NONE)
    static class TestClass {
        private Integer id;
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @JsonAutoDetect
    static class TestClassWithId {
        @JsonIdentityInfo(generator = ObjectIdGenerator.PropertyGenerator.class, property = "id")
        private Integer id;
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    public void testFindObjectId() {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getFactory().createContext();

        TestClass testClass = new TestClass();
        testClass.setId(1);
        testClass.setName("testClass");

        ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey(TestClass.class, "id");
        ObjectIdGenerator<?> gen = mapper.getFactory().getConfig()
                .getObjectMapperConfig().getObjectIdGenerator();

        ReadableObjectId objectId = context.findObjectId(testClass, gen, ObjectIdGenerator.IdIntrospector.class);
        assertNotNull(objectId);
        assertEquals(idKey, objectId.getKey());
        assertNull(objectId.getResolver());

        TestClass testClass2 = new TestClass();
        testClass2.setId(1);
        testClass2.setName("testClass2");

        ReadableObjectId objectId2 = context.findObjectId(testClass2, gen, ObjectIdGenerator.IdIntrospector.class);
        assertNotNull(objectId2);
        assertNotSame(objectId, objectId2);
        assertEquals(idKey, objectId2.getKey());
        assertNull(objectId2.getResolver());
    }

    @Test
    public void testFindObjectIdWithResolver() {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getFactory().createContext();

        SimpleObjectIdResolver resolver = new SimpleObjectIdResolver();
        context.setObjectIdResolvers(Arrays.asList(resolver));

        TestClass testClass = new TestClass();
        testClass.setId(1);
        testClass.setName("testClass");

        ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey(TestClass.class, "id");
        ObjectIdGenerator<?> gen = mapper.getFactory().getConfig()
                .getObjectMapperConfig().getObjectIdGenerator();

        ReadableObjectId objectId = context.findObjectId(testClass, gen, ObjectIdGenerator.IdIntrospector.class);
        assertNotNull(objectId);
        assertEquals(idKey, objectId.getKey());
        assertNotNull(objectId.getResolver());
        assertEquals(resolver, objectId.getResolver());

        TestClass testClass2 = new TestClass();
        testClass2.setId(1);
        testClass2.setName("testClass2");

        ReadableObjectId objectId2 = context.findObjectId(testClass2, gen, ObjectIdGenerator.IdIntrospector.class);
        assertNotNull(objectId2);
        assertNotSame(objectId, objectId2);
        assertEquals(idKey, objectId2.getKey());
        assertNotNull(objectId2.getResolver());
        assertNotSame(resolver, objectId2.getResolver());
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getFactory().createContext();

        TestClass testClass = new TestClass();
        testClass.setId(1);
        testClass.setName("testClass");

        ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey(TestClass.class, "id");
        ObjectIdGenerator<?> gen = mapper.getFactory().getConfig()
                .getObjectMapperConfig().getObjectIdGenerator();

        context.findObjectId(testClass, gen, ObjectIdGenerator.IdIntrospector.class);

        // Check unresolved object id
        context.checkUnresolvedObjectId();

        TestClass testClass2 = new TestClass();
        testClass2.setId(1);
        testClass2.setName("testClass2");

        context.findObjectId(testClass2, gen, ObjectIdGenerator.IdIntrospector.class);

        TestClass testClass3 = new TestClass();
        testClass3.setName("testClass3");

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference to be thrown.");
        } catch (UnresolvedForwardReference e) {
            // Expected
            List<Map.Entry<ObjectIdGenerator.IdKey, Class<?>>> ids = e.getIds();
            assertEquals(1, ids.size());
            Map.Entry<ObjectIdGenerator.IdKey, Class<?>> id = ids.get(0);
            assertEquals(idKey, id.getKey());
            assertEquals(TestClass.class, id.getValue());
        }
    }

    @Test
    public void testDeserializerInstance() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getFactory().createContext();

        TestClassWithId testClass = new TestClassWithId();
        testClass.setId(1);
        testClass.setName("testClass");

        JsonDeserializer<TestClassWithId> deserializer = new TestClassWithIdDeserializer();

        Annotated ann = AnnotatedClass.construct(TestClassWithId.class, null, mapper.getAnnotationIntrospector());
        Object deserDef = deserializer;

        JsonDeserializer<TestClassWithId> deserializerInstance = context.deserializerInstance(ann, deserDef);
        assertEquals(deserializer, deserializerInstance);

        TestClassWithId testClass2 = new TestClassWithId();
        testClass2.setId(2);
        testClass2.setName("testClass2");

        deserializerInstance = context.deserializerInstance(ann, deserDef);
        assertNotSame(deserializerInstance, deserializerInstance);

        Object deserDef2 = new Object() {
            @Override
            public JsonDeserializer<?> deserialize(Annotated a, TypeDeserializer td) throws JsonMappingException {
                return null;
            }
        };

        assertNull(context.deserializerInstance(ann, deserDef2));

        TestClassWithId testClass3 = new TestClassWithId();
        testClass3.setName("testClass3");

        try {
            context.deserializerInstance(ann, deserDef);
            fail("Expected IllegalStateException to be thrown.");
        } catch (IllegalStateException e) {
            // Expected
            assertEquals("AnnotationIntrospector returned deserializer definition of type "
                    + deserDef2.getClass().getName()
                    + "; expected type JsonDeserializer or Class<JsonDeserializer> instead", e.getMessage());
        }

        TestClassWithId testClass4 = new TestClassWithId();
        testClass4.setId(null);
        testClass4.setName("testClass4");

        try {
            context.deserializerInstance(ann, deserDef);
            fail("Expected IllegalStateException to be thrown.");
        } catch (IllegalStateException e) {
            // Expected
            assertEquals("AnnotationIntrospector returned deserializer definition of type "
                    + deserDef.getClass().getName()
                    + "; expected type JsonDeserializer or Class<JsonDeserializer> instead", e.getMessage());
        }
    }

    static class TestClassWithIdDeserializer extends JsonDeserializer<TestClassWithId> {
        @Override
        public TestClassWithId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            TestClassWithId testClass = new TestClassWithId();
            testClass.setId(ctxt.readValueAs(Integer.class, p));
            testClass.setName(ctxt.readValueAs(String.class, p));
            return testClass;
        }
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getFactory().createContext();

        TestClassWithId testClass = new TestClassWithId();
        testClass.setId(1);
        testClass.setName("testClass");

        KeyDeserializer keyDeserializer = new TestClassWithIdKeyDeserializer();

        Annotated ann = AnnotatedClass.construct(TestClassWithId.class, null, mapper.getAnnotationIntrospector());
        Object deserDef = keyDeserializer;

        KeyDeserializer keyDeserializerInstance = context.keyDeserializerInstance(ann, deserDef);
        assertEquals(keyDeserializer, keyDeserializerInstance);

        TestClassWithId testClass2 = new TestClassWithId();
        testClass2.setId(2);
        testClass2.setName("testClass2");

        keyDeserializerInstance = context.keyDeserializerInstance(ann, deserDef);
        assertNotSame(keyDeserializerInstance, keyDeserializerInstance);

        Object deserDef2 = new Object() {
            @Override
            public KeyDeserializer keyDeserialize(Annotated a, TypeDeserializer td) throws JsonMappingException {
                return null;
            }
        };

        assertNull(context.keyDeserializerInstance(ann, deserDef2));

        TestClassWithId testClass3 = new TestClassWithId();
        testClass3.setName("testClass3");

        try {
            context.keyDeserializerInstance(ann, deserDef);
            fail("Expected IllegalStateException to be thrown.");
        } catch (IllegalStateException e) {
            // Expected
            assertEquals("AnnotationIntrospector returned key deserializer definition of type "
                    + deserDef2.getClass().getName()
                    + "; expected type KeyDeserializer or Class<KeyDeserializer> instead", e.getMessage());
        }

        TestClassWithId testClass4 = new TestClassWithId();
        testClass4.setId(null);
        testClass4.setName("testClass4");

        try {
            context.keyDeserializerInstance(ann, deserDef);
            fail("Expected IllegalStateException to be thrown.");
        } catch (IllegalStateException e) {
            // Expected
            assertEquals("AnnotationIntrospector returned key deserializer definition of type "
                    + deserDef.getClass().getName()
                    + "; expected type KeyDeserializer or Class<KeyDeserializer> instead", e.getMessage());
        }
    }

    static class TestClassWithIdKeyDeserializer extends KeyDeserializer {
        @Override
        public TestClassWithId keyDeserialize(JsonParser p, DeserializationContext ctxt, Object bean, TypeDeserializer vtd)
                throws IOException {
            TestClassWithId testClass = new TestClassWithId();
            testClass.setId(ctxt.readValueAs(Integer.class, p));
            return testClass;
        }
    }
}