package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext context;
    private ObjectMapper mapper;
    private ObjectReader reader;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        reader = mapper.readerForClass(DefaultDeserializationContext.class);
        context = reader.readValue(getClass().getResource("/default-deserialization-context.json"), DefaultDeserializationContext.class);
    }

    @Test
    public void testFindObjectId() throws JsonMappingException {
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey(getClass(), id);
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId result = context.findObjectId(id, gen, resolver);
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    public void testFindObjectIdWithNullId() throws JsonMappingException {
        ObjectIdGenerator<?> gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return null;
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId result = context.findObjectId(null, gen, resolver);
        assertNull(result);
    }

    @Test
    public void testFindObjectIdWithNullGenerator() throws JsonMappingException {
        Object id = new Object();
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        ReadableObjectId result = context.findObjectId(id, null, resolver);
        assertNull(result);
    }

    @Test
    public void testFindObjectIdWithNullResolver() throws JsonMappingException {
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey(getClass(), id);
            }
        };
        ReadableObjectId result = context.findObjectId(id, gen, null);
        assertNull(result);
    }

    @Test
    public void testFindObjectIdWithExistingId() throws JsonMappingException {
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey(getClass(), id);
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        context.findObjectId(id, gen, resolver);
        ReadableObjectId result = context.findObjectId(id, gen, resolver);
        assertSame(result, context.findObjectId(id, gen, resolver));
    }

    @Test
    public void testCheckUnresolvedObjectId() throws JsonMappingException {
        Object id1 = new Object();
        Object id2 = new Object();
        ObjectIdGenerator<?> gen1 = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey(getClass(), id);
            }
        };
        ObjectIdGenerator<?> gen2 = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey(getClass(), id);
            }
        };
        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        context.findObjectId(id1, gen1, resolver);
        context.findObjectId(id2, gen2, resolver);
        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // Expected
        }
    }

    @Test
    public void testDeserializerInstance() throws Exception {
        JsonProperty property = new JsonProperty();
        property.value(DefaultDeserializationContext.class);
        Annotated ann = new AnnotatedClass(DefaultDeserializationContext.class);
        ann.addAnnotation(property);
        ObjectDeserializer deserializer = new ObjectDeserializer() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new Object();
            }
        };
        Object result = context.deserializerInstance(ann, deserializer);
        assertSame(deserializer, result);
    }

    @Test
    public void testDeserializerInstanceWithNullDeserializer() throws Exception {
        Object result = context.deserializerInstance(null, null);
        assertNull(result);
    }

    @Test
    public void testDeserializerInstanceWithNullAnnotated() throws Exception {
        Object result = context.deserializerInstance(null, new ObjectDeserializer() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new Object();
            }
        });
        assertNull(result);
    }

    @Test
    public void testDeserializerInstanceWithClass() throws Exception {
        Object result = context.deserializerInstance(null, Object.class);
        assertNull(result);
    }

    @Test
    public void testDeserializerInstanceWithAnnotation() throws Exception {
        JsonProperty property = new JsonProperty();
        property.value(DefaultDeserializationContext.class);
        Annotated ann = new AnnotatedClass(DefaultDeserializationContext.class);
        ann.addAnnotation(property);
        Object result = context.deserializerInstance(ann, Object.class);
        assertNull(result);
    }

    @Test
    public void testDeserializerInstanceWithCustomDeserializer() throws Exception {
        JsonProperty property = new JsonProperty();
        property.value(DefaultDeserializationContext.class);
        Annotated ann = new AnnotatedClass(DefaultDeserializationContext.class);
        ann.addAnnotation(property);
        ObjectDeserializer deserializer = new ObjectDeserializer() {
            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return new Object();
            }
        };
        Object result = context.deserializerInstance(ann, deserializer);
        assertSame(deserializer, result);
    }

    @Test
    public void testKeyDeserializerInstance() throws Exception {
        Annotated ann = new AnnotatedClass(String.class);
        Object result = context.keyDeserializerInstance(ann, new StringKeyDeserializer());
        assertSame(new StringKeyDeserializer(), result);
    }

    @Test
    public void testKeyDeserializerInstanceWithNullKeyDeserializer() throws Exception {
        Annotated ann = new AnnotatedClass(String.class);
        Object result = context.keyDeserializerInstance(ann, null);
        assertNull(result);
    }

    @Test
    public void testKeyDeserializerInstanceWithNullAnnotated() throws Exception {
        Object result = context.keyDeserializerInstance(null, new StringKeyDeserializer());
        assertNull(result);
    }

    @Test
    public void testKeyDeserializerInstanceWithClass() throws Exception {
        Annotated ann = new AnnotatedClass(String.class);
        Object result = context.keyDeserializerInstance(ann, Object.class);
        assertNull(result);
    }

    @Test
    public void testKeyDeserializerInstanceWithAnnotation() throws Exception {
        Annotated ann = new AnnotatedClass(String.class);
        Object result = context.keyDeserializerInstance(ann, new JsonProperty());
        assertNull(result);
    }

    @Test
    public void testKeyDeserializerInstanceWithCustomKeyDeserializer() throws Exception {
        Annotated ann = new AnnotatedClass(String.class);
        ann.addAnnotation(new JsonProperty());
        Object result = context.keyDeserializerInstance(ann, new StringKeyDeserializer());
        assertSame(new StringKeyDeserializer(), result);
    }

    @Test
    public void testWith() throws Exception {
        ObjectMapper newMapper = mapper.copy();
        ObjectReader newReader = newMapper.readerForClass(DefaultDeserializationContext.class);
        DefaultDeserializationContext newContext = newReader.readValue(getClass().getResource("/default-deserialization-context.json"), DefaultDeserializationContext.class);
        DefaultDeserializationContext copiedContext = context.copy();
        DefaultDeserializationContext result = newContext.with(copiedContext);
        assertEquals(copiedContext, result);
    }

    @Test
    public void testCreateInstance() throws Exception {
        ObjectMapper newMapper = mapper.copy();
        ObjectReader newReader = newMapper.readerForClass(DefaultDeserializationContext.class);
        DefaultDeserializationContext result = newReader.readValue(getClass().getResource("/default-deserialization-context.json"), DefaultDeserializationContext.class).createInstance(mapper.getConfiguration(), null, null);
        DefaultDeserializationContext expected = new DefaultDeserializationContext.Impl(mapper.getConfiguration(), null, null);
        assertEquals(expected, result);
    }

    private static class ObjectDeserializer extends JsonDeserializer<Object> {
    }

    private static class StringKeyDeserializer extends KeyDeserializer {
    }
}