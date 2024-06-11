package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializationContext.DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createDeserializer(DeserializationConfig config, Annotated member, Object deserializerDef)
                    throws JsonMappingException {
                return null;
            }
        }, null);

        ObjectIdGenerator idGen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return ObjectIdGenerator.IdKey.STRING;
            }
        };

        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        ReadableObjectId expected = new ReadableObjectId(ObjectIdGenerator.IdKey.STRING);
        expected.setResolver(resolver);

        ReadableObjectId actual = context.findObjectId(new Object() {}, idGen, resolver);

        assertEquals(expected, actual);
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializationContext.DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createDeserializer(DeserializationConfig config, Annotated member, Object deserializerDef)
                    throws JsonMappingException {
                return null;
            }
        }, null);

        context._objectIds = new LinkedHashMap<>();
        context._objectIds.put(ObjectIdGenerator.IdKey.STRING, new ReadableObjectId(ObjectIdGenerator.IdKey.STRING));

        context.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializationContext.DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createDeserializer(DeserializationConfig config, Annotated member, Object deserializerDef)
                    throws JsonMappingException {
                return new StubJsonDeserializer();
            }
        }, null);

        Annotated ann = new AnnotatedClass(String.class);
        Object deserDef = new Object() {};

        JsonDeserializer<?> actual = context.deserializerInstance(ann, deserDef);

        assertTrue(actual instanceof StubJsonDeserializer);
    }

    @Test
    public void testKeyDeserializerInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializationContext.DeserializerFactory() {
            @Override
            public KeyDeserializer keyDeserializerInstance(DeserializationConfig config, Annotated member, Object deserializerDef)
                    throws JsonMappingException {
                return new StubKeyDeserializer();
            }
        }, null);

        Annotated ann = new AnnotatedClass(String.class);
        Object deserDef = new Object() {};

        KeyDeserializer actual = context.keyDeserializerInstance(ann, deserDef);

        assertTrue(actual instanceof StubKeyDeserializer);
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(new DefaultDeserializationContext.DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createDeserializer(DeserializationConfig config, Annotated member, Object deserializerDef)
                    throws JsonMappingException {
                return null;
            }
        }, null);

        DefaultDeserializationContext newContext = context.with(new DefaultDeserializationContext.DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createDeserializer(DeserializationConfig config, Annotated member, Object deserializerDef)
                    throws JsonMappingException {
                return new StubJsonDeserializer();
            }
        });

        assertNotSame(context, newContext);
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext.DeserializerFactory factory = new DefaultDeserializationContext.DeserializerFactory() {
            @Override
            public JsonDeserializer<?> createDeserializer(DeserializationConfig config, Annotated member, Object deserializerDef)
                    throws JsonMappingException {
                return null;
            }
        };

        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(factory, null);

        DefaultDeserializationContext newContext = new DefaultDeserializationContext.Impl(context, new DefaultDeserializationConfig(), new JsonParser() {}, new InjectableValues());

        assertNotSame(context, newContext);
    }

    static class StubJsonDeserializer extends JsonDeserializer<Object> {}

    static class StubKeyDeserializer extends KeyDeserializer {}

    static class AnnotatedClass implements Annotated {
        private Class<?> clazz;

        public AnnotatedClass(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public Annotation[] getAnnotations() {
            return new Annotation[0];
        }

        @Override
        public Annotation[] getDeclaredAnnotations() {
            return new Annotation[0];
        }

        @Override
        public Annotation[] getMemberAnnotations(Class<?> type) {
            return new Annotation[0];
        }

        @Override
        public Annotation[] getClassAnnotations() {
            return new Annotation[0];
        }

        @Override
        public Object getAnnotation(Class<? extends Annotation> annotationType) {
            return null;
        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public int getMemberName() {
            return 0;
        }

        @Override
        public String getName() {
            return "";
        }

        @Override
        public Class<?> getType() {
            return clazz;
        }
    }
}