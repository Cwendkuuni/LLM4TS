package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test cases for the DefaultDeserializationContext class.
 */
public class DefaultDeserializationContextTest {

    /**
     * Test case for the findObjectId method.
     */
    @org.junit.Test
    public void testFindObjectId() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new StandardDeserializer.Std(String.class));

        // Test with null id and null generator
        assertNull(ctx.findObjectId(null, null));

        // Test with null id and valid generator
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("test");
            }

            @Override
            public ObjectIdResolver forResolver() {
                return new SimpleObjectIdResolver();
            }
        };
        assertNull(ctx.findObjectId(null, generator, SimpleObjectIdResolver.class));

        // Test with valid id and null generator
        String id = new Object();
        ObjectIdGenerator generator2 = null;
        ReadableObjectId roid = ctx.findObjectId(id, generator2, SimpleObjectIdResolver.class);
        assertNull(roid);

        // Test with valid id and valid generator
        ObjectIdGenerator generator3 = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("test");
            }

            @Override
            public ObjectIdResolver forResolver() {
                return new SimpleObjectIdResolver();
            }
        };
        roid = ctx.findObjectId(id, generator3, SimpleObjectIdResolver.class);
        assertNotNull(roid);
        assertEquals("test", roid.getKey().key);

        // Test with valid id and valid resolver
        ObjectIdResolver resolver = new ObjectIdResolver() {
            @Override
            public ObjectIdResolver newForDeserialization(DefaultDeserializationContext ctxt) {
                return this;
            }

            @Override
            public ObjectId resolveId(Object id, DeserializationContext ctxt) throws JsonMappingException {
                return null;
            }
        };
        roid = ctx.findObjectId(id, generator3, resolver.getClass());
        assertNotNull(roid);
        assertEquals("test", roid.getKey().key);

        // Test with valid id, valid generator, and valid resolver
        ObjectIdGenerator generator4 = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("test");
            }

            @Override
            public ObjectIdResolver forResolver() {
                return resolver;
            }
        };
        roid = ctx.findObjectId(id, generator4, resolver.getClass());
        assertNotNull(roid);
        assertEquals("test", roid.getKey().key);

        // Test with duplicate id
        ObjectIdGenerator generator5 = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("test");
            }

            @Override
            public ObjectIdResolver forResolver() {
                return resolver;
            }
        };
        roid = ctx.findObjectId(id, generator5, resolver.getClass());
        assertNotNull(roid);
        assertEquals("test", roid.getKey().key);

        // Test with a resolver that cannot handle the given type
        ObjectIdResolver resolver2 = new ObjectIdResolver() {
            @Override
            public ObjectId resolveId(Object id, DeserializationContext ctxt) throws JsonMappingException {
                return null;
            }

            @Override
            public boolean canUseFor(Class<?> type) {
                return type != SimpleObjectIdResolver.class;
            }
        };
        assertNull(ctx.findObjectId(id, generator3, resolver2.getClass()));

        // Test with circular reference
        Object id2 = new Object();
        ReadableObjectId roid2 = ctx.findObjectId(id2, generator3, resolver.getClass());
        assertNotNull(roid2);
        assertEquals("test", roid2.getKey().key);

        // Test with an unresolved referencing property
        Object id3 = new Object() {
            public Object referenced;
        };

        ObjectIdGenerator generator6 = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("test");
            }

            @Override
            public ObjectIdResolver forResolver() {
                return resolver;
            }
        };
        ReadableObjectId roid3 = ctx.findObjectId(id3, generator6, resolver.getClass());
        assertNotNull(roid3);
        assertEquals("test", roid3.getKey().key);

        // Test with an unresolved referencing property that has a value
        ObjectNode node = ObjectMapper.getDefaultInstance().createObjectNode();
        node.put("referenced", "value");
        JsonParser parser = ObjectMapper.getDefaultInstance().getFactory().createParser(node);
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext ctx2 = mapper.getDeserializationContext();
        JsonDeserializer<Object> deserializer = ctx2.findBeanDeserializer(AnnotatedClass.construct(MyClass.class, mapper));
        Object result = deserializer.deserialize(parser, MyClass.class);
        assertNotNull(result);
        assertEquals("value", ((MyClass) result).referenced);

        // Test with an unresolved referencing property in a subclass
        @JsonTypeName("subClass")
        @JsonSubTypes({
                @JsonSubTypes.Type(value = MySubClass.class, name = "subClass1")
        })
        static class MySubClass extends MyClass {
            @JsonProperty("subProperty")
            private Object referenced;
        }
        deserializer = ctx2.findBeanDeserializer(AnnotatedClass.construct(MySubClass.class, mapper));
        result = deserializer.deserialize(parser, MySubClass.class);
        assertNotNull(result);
        assertEquals("value", ((MySubClass) result).referenced);

        // Test with an unresolved referencing property in a subclass with a different type
        @JsonTypeName("subClass2")
        @JsonSubTypes({
                @JsonSubTypes.Type(value = MySubClass2.class, name = "subClass1")
        })
        static class MySubClass2 extends MyClass {
            @JsonProperty("subProperty")
            private Integer referenced;
        }
        deserializer = ctx2.findBeanDeserializer(AnnotatedClass.construct(MySubClass2.class, mapper));
        result = deserializer.deserialize(parser, MySubClass2.class);
        assertNotNull(result);
        assertEquals(42, ((MySubClass2) result).referenced);

        // Test with an unresolved referencing property in a List
        List<MyClass> list = new ArrayList<>();
        MyClass element = new MyClass();
        ObjectIdGenerator generator7 = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("test");
            }

            @Override
            public ObjectIdResolver forResolver() {
                return resolver;
            }
        };
        list.add(element);
        element.referenced = new Object();
        ObjectNode node2 = ObjectMapper.getDefaultInstance().createObjectNode();
        node2.put("list", ObjectMapper.getDefaultInstance().valueToTree(list));
        parser = ObjectMapper.getDefaultInstance().getFactory().createParser(node2);
        deserializer = ctx2.findBeanDeserializer(AnnotatedClass.construct(List.class, mapper, Integer.TYPE, null));
        result = deserializer.deserialize(parser, List.class);
        assertNotNull(result);
        assertEquals(1, result.size());
        MyClass element2 = (MyClass) result.get(0);
        assertNotNull(element2.referenced);

        // Test with an unresolved referencing property in a Map
        Map<String, MyClass> map = new HashMap<>();
        element = new MyClass();
        ObjectIdGenerator generator8 = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("test");
            }

            @Override
            public ObjectIdResolver forResolver() {
                return resolver;
            }
        };
        map.put("key", element);
        element.referenced = new Object();
        node2 = ObjectMapper.getDefaultInstance().createObjectNode();
        node2.put("map", ObjectMapper.getDefaultInstance().valueToTree(map));
        parser = ObjectMapper.getDefaultInstance().getFactory().createParser(node2);
        deserializer = ctx2.findBeanDeserializer(AnnotatedClass.construct(Map.class, mapper, String.class, MyClass.class, null));
        result = deserializer.deserialize(parser, Map.class);
        assertNotNull(result);
        assertEquals(1, result.size());
        String key = result.keySet().iterator().next();
        assertNotNull(result.get(key));
        assertNotNull(((MyClass) result.get(key)).referenced);
    }

    /**
     * Test case for the checkUnresolvedObjectId method.
     */
    @org.junit.Test
    public void testCheckUnresolvedObjectId() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new StandardDeserializer.Std(String.class));

        // Test with no unresolved id
        ctx.checkUnresolvedObjectId();

        // Test with unresolved id
        ObjectIdGenerator generator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("test");
            }

            @Override
            public ObjectIdResolver forResolver() {
                return new ObjectIdResolver() {
                    @Override
                    public ObjectId resolveId(Object id, DeserializationContext ctxt) throws JsonMappingException {
                        return null;
                    }
                };
            }
        };
        ctx.findObjectId(new Object(), generator, SimpleObjectIdResolver.class);
        try {
            ctx.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // Expected
        }
    }

    /**
     * Test case for the deserializerInstance method.
     */
    @org.junit.Test
    public void testDeserializerInstance() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new StandardDeserializer.Std(String.class));

        // Test with null deserDef
        JsonDeserializer deserializer = ctx.deserializerInstance(null, null);
        assertNull(deserializer);

        // Test with null ann
        deserializer = ctx.deserializerInstance(null, new StdSerializer<String>(String.class));
        assertNotNull(deserializer);
        assertEquals(StdSerializer.class, deserializer.getClass());

        // Test with null deserDef and null ann
        deserializer = ctx.deserializerInstance(null, null);
        assertNull(deserializer);

        // Test with valid deserDef and null ann
        deserializer = ctx.deserializerInstance(new StdSerializer<String>(String.class), null);
        assertNotNull(deserializer);
        assertEquals(StdSerializer.class, deserializer.getClass());

        // Test with null deserDef and valid ann
        StdSerializer<String> ann = new StdSerializer<String>(String.class) {
            @Override
            public Class<JsonDeserializer<Object>> getBindingClass() {
                return JsonDeserializer.class;
            }
        };
        deserializer = ctx.deserializerInstance(null, ann);
        assertNotNull(deserializer);
        assertEquals(StdSerializer.class, deserializer.getClass());

        // Test with valid deserDef and valid ann
        deserializer = ctx.deserializerInstance(new StdSerializer<String>(String.class), ann);
        assertNotNull(deserializer);
        assertEquals(StdSerializer.class, deserializer.getClass());
    }

    /**
     * Test case for the keyDeserializerInstance method.
     */
    @org.junit.Test
    public void testKeyDeserializerInstance() throws Exception {
        DefaultDeserializationContext ctx = new DefaultDeserializationContext.Impl(new StandardDeserializer.Std(String.class));

        // Test with null deserDef
        KeyDeserializer deserializer = ctx.keyDeserializerInstance(null, null);
        assertNull(deserializer);

        // Test with null ann
        deserializer = ctx.keyDeserializerInstance(null, new StdSerializer<SerializableKey>(SerializableKey.class));
        assertNotNull(deserializer);
        assertEquals(StdSerializer.class, deserializer.getClass());

        // Test with null deserDef and valid ann
        StdSerializer<SerializableKey> ann = new StdSerializer<SerializableKey>(SerializableKey.class) {
            @Override
            public Class<KeyDeserializer> getBindingClass() {
                return KeyDeserializer.class;
            }
        };
        deserializer = ctx.keyDeserializerInstance(null, ann);
        assertNotNull(deserializer);
        assertEquals(StdSerializer.class, deserializer.getClass());

        // Test with valid deserDef and valid ann
        deserializer = ctx.keyDeserializerInstance(new StdSerializer<SerializableKey>(SerializableKey.class), ann);
        assertNotNull(deserializer);
        assertEquals(StdSerializer.class, deserializer.getClass());
    }
}