package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey() {
                    @Override
                    public String id() {
                        return "testKey";
                    }
                };
            }
        };

        ReadableObjectId id = context.findObjectId(new Object(), idGenerator, new SimpleObjectIdResolver());
        assertNotNull(id);
        assertEquals("testKey", id.getKey().id());

        context._objectIds = new LinkedHashMap<>();
        id = context.findObjectId(new Object(), idGenerator, new SimpleObjectIdResolver());
        assertNotNull(id);
        assertEquals("testKey", id.getKey().id());

        context._objectIds = null;
        id = context.findObjectId(new Object(), idGenerator, new SimpleObjectIdResolver());
        assertNull(id);
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new IdKey() {
            @Override
            public String id() {
                return "testKey";
            }
        }, new ReadableObjectId(new IdKey() {
            @Override
            public String id() {
                return "testKey";
            }
        }));

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference to be thrown");
        } catch (UnresolvedForwardReference e) {
            // expected
        }

        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(new IdKey() {
            @Override
            public String id() {
                return "testKey";
            }
        }, new ReadableObjectId(new IdKey() {
            @Override
            public String id() {
                return "testKey";
            }
        }));

        context.checkUnresolvedObjectId();

        context._objectIds = null;
        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            fail("Expected null to be passed to checkUnresolvedObjectId when _objectIds is null");
        }
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        JsonDeserializer<String> deserializer = new JsonDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext c) throws IOException {
                return "test";
            }
        };

        assertEquals(deserializer, context.deserializerInstance(null, deserializer));

        Annotated ann = new AnnotatedClass<?>() {};
        assertEquals(deserializer, context.deserializerInstance(ann, deserializer));

        Class<JsonDeserializer<String>> deserializerClass = JsonDeserializer.class;
        assertEquals(deserializer, context.deserializerInstance(ann, deserializerClass));

        HandlerInstantiator hi = new HandlerInstantiator() {};
        assertEquals(deserializer, hi.deserializerInstance(context._config, ann, deserializerClass));

        assertEquals(null, context.deserializerInstance(ann, String.class));
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        KeyDeserializer deserializer = new KeyDeserializer() {
            @Override
            public Object deserializeKey(JsonParser p, DeserializationContext c) throws IOException {
                return "test";
            }
        };

        assertEquals(deserializer, context.keyDeserializerInstance(null, deserializer));

        Annotated ann = new AnnotatedClass<?>() {};
        assertEquals(deserializer, context.keyDeserializerInstance(ann, deserializer));

        Class<KeyDeserializer> deserializerClass = KeyDeserializer.class;
        assertEquals(deserializer, context.keyDeserializerInstance(ann, deserializerClass));

        HandlerInstantiator hi = new HandlerInstantiator() {};
        assertEquals(deserializer, hi.keyDeserializerInstance(context._config, ann, deserializerClass));

        assertEquals(null, context.keyDeserializerInstance(ann, String.class));
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        DefaultDeserializationContext newContext = context.with(new ObjectMapper.DefaultDeserializationFeature() {});
        assertNotSame(context, newContext);
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        DefaultDeserializationContext newContext = context.createInstance(null, null, null);
        assertNotSame(context, newContext);
    }

    private static class AnnotatedClass<T> implements Annotated {
        @Override
        public Class<?> getClassInfo() {
            return null;
        }

        @Override
        public TypeDescriptor forType() {
            return null;
        }

        @Override
        public bean PropertyName() {
            return null;
        }

        @Override
        public AnnotationBean annotations() {
            return null;
        }

        @Override
        public Iterator<AnnotationBean> annotationsIterator() {
            return null;
        }

        @Override
        public AnnotationBean findAnnotation(Class<? extends Annotation> aClass) {
            return null;
        }

        @Override
        public <T extends Annotation> T findAnnotation(Class<T> annotationType) {
            return null;
        }

        @Override
        public Object getValue(String name) {
            return null;
        }

        @Override
        public Object getValue() {
            return null;
        }
    }
}