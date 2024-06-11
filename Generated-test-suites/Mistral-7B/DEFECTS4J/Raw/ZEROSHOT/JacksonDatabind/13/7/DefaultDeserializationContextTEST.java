package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.*;

/**
 * Unit tests for {@link DefaultDeserializationContext} class
 */
public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DeserializerFactory factory = new DeserializerFactory();
        DefaultDeserializationContext context = new DefaultDeserializationContext(factory, null);
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    private final String idName;

                    public IdKey(String idName) {
                        this.idName = idName;
                    }

                    @Override
                    public String toString() {
                        return idName;
                    }
                };
            }

            @Override
            public ObjectIdGenerator.Id getId(Object id) {
                return null;
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver() {
            @Override
            public ObjectId idFromObject(Object id) {
                return new ObjectId() {
                    private final String idName;

                    public ObjectId(String idName) {
                        this.idName = idName;
                    }

                    @Override
                    public String toString() {
                        return idName;
                    }
                };
            }
        };

        ReadableObjectId id = context.findObjectId(new Object(), idGen, resolver);
        assertNotNull(id);
        assertEquals("id", id.toString());

        context._objectIds = null;
        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // expected
        }
    }

    @Test
    public void testFindObjectIdWithNullIdGenerator() {
        DeserializerFactory factory = new DeserializerFactory();
        DefaultDeserializationContext context = new DefaultDeserializationContext(factory, null);
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        try {
            context.findObjectId(null, null, resolver);
            fail("Expected IllegalArgumentException exception");
        } catch (IllegalArgumentException e) {
            // expected
        }

        context._objectIds = null;
        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // expected
        }
    }

    @Test
    public void testFindObjectIdWithNullIdResolver() {
        DeserializerFactory factory = new DeserializerFactory();
        DefaultDeserializationContext context = new DefaultDeserializationContext(factory, null);
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    private final String idName;

                    public IdKey(String idName) {
                        this.idName = idName;
                    }

                    @Override
                    public String toString() {
                        return idName;
                    }
                };
            }

            @Override
            public ObjectId getId(Object id) {
                return null;
            }
        };

        try {
            context.findObjectId(new Object(), idGen, null);
            fail("Expected IllegalArgumentException exception");
        } catch (IllegalArgumentException e) {
            // expected
        }

        context._objectIds = null;
        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // expected
        }
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DeserializerFactory factory = new DeserializerFactory();
        DefaultDeserializationContext context = new DefaultDeserializationContext(factory, null);
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    private final String idName;

                    public IdKey(String idName) {
                        this.idName = idName;
                    }

                    @Override
                    public String toString() {
                        return idName;
                    }
                };
            }

            @Override
            public ObjectId getId(Object id) {
                return null;
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        JsonDeserializer<Object> deserializer = context.deserializerInstance(null, idGen);
        assertNull(deserializer);

        // Test with class descriptor
        JsonDeserializer<Object> classDeserializer = context.deserializerInstance(null, Object.class);
        assertNotNull(classDeserializer);
        assertTrue(classDeserializer instanceof JsonDeserializer.None);

        JsonDeserializer<String> stringDeserializer = context.deserializerInstance(null, String.class);
        assertNotNull(stringDeserializer);
        assertFalse(stringDeserializer instanceof JsonDeserializer.None);

        JsonDeserializer<Integer> intDeserializer = context.deserializerInstance(null, Integer.class);
        assertNotNull(intDeserializer);
        assertFalse(intDeserializer instanceof JsonDeserializer.None);

        JsonDeserializer<MyClass> myClassDeserializer = context.deserializerInstance(null, MyClass.class);
        assertNotNull(myClassDeserializer);
        assertFalse(myClassDeserializer instanceof JsonDeserializer.None);

        // Test with instance
        JsonDeserializer<Object> instanceDeserializer = context.deserializerInstance(null, new MyClass());
        assertNotNull(instanceDeserializer);
        assertTrue(instanceDeserializer instanceof JsonDeserializer.None);

        // Test with HandlerInstantiator
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {
            @Override
            public JsonDeserializer<?> deserializerInstance(DeserializationConfig config, Annotated member, Class<?> deserializerClass)
                throws JsonMappingException {
                return new JsonDeserializer<Object>() {};
            }
        };

        JsonDeserializer<Object> handlerInstantiatorDeserializer = context.deserializerInstance(null, null, handlerInstantiator);
        assertNotNull(handlerInstantiatorDeserializer);
        assertTrue(handlerInstantiatorDeserializer instanceof JsonDeserializer.None);
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DeserializerFactory factory = new DeserializerFactory();
        DefaultDeserializationContext context = new DefaultDeserializationContext(factory, null);
        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    private final String idName;

                    public IdKey(String idName) {
                        this.idName = idName;
                    }

                    @Override
                    public String toString() {
                        return idName;
                    }
                };
            }

            @Override
            public ObjectId getId(Object id) {
                return null;
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        KeyDeserializer keyDeserializer = context.keyDeserializerInstance(null, idGen);
        assertNull(keyDeserializer);

        // Test with class descriptor
        KeyDeserializer classKeyDeserializer = context.keyDeserializerInstance(null, Object.class);
        assertNotNull(classKeyDeserializer);
        assertTrue(classKeyDeserializer instanceof KeyDeserializer.None);

        KeyDeserializer stringKeyDeserializer = context.keyDeserializerInstance(null, String.class);
        assertNotNull(stringKeyDeserializer);
        assertFalse(stringKeyDeserializer instanceof KeyDeserializer.None);

        KeyDeserializer integerKeyDeserializer = context.keyDeserializerInstance(null, Integer.class);
        assertNotNull(integerKeyDeserializer);
        assertFalse(integerKeyDeserializer instanceof KeyDeserializer.None);

        KeyDeserializer myClassKeyDeserializer = context.keyDeserializerInstance(null, MyClass.class);
        assertNotNull(myClassKeyDeserializer);
        assertFalse(myClassKeyDeserializer instanceof KeyDeserializer.None);

        // Test with instance
        KeyDeserializer instanceKeyDeserializer = context.keyDeserializerInstance(null, new MyClass());
        assertNotNull(instanceKeyDeserializer);
        assertTrue(instanceKeyDeserializer instanceof KeyDeserializer.None);

        // Test with HandlerInstantiator
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {
            @Override
            public KeyDeserializer keyDeserializerInstance(DeserializationConfig config, Annotated member, Class<?> keyDeserializerClass)
                throws JsonMappingException {
                return new KeyDeserializer.None();
            }
        };

        KeyDeserializer handlerInstantiatorKeyDeserializer = context.keyDeserializerInstance(null, null, handlerInstantiator);
        assertNotNull(handlerInstantiatorKeyDeserializer);
        assertTrue(handlerInstantiatorKeyDeserializer instanceof KeyDeserializer.None);
    }

    static class MyClass {
        private String id;

        @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
        @JsonIdentityProperty("id")
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}