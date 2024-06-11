package com.fasterxml.jackson.databind.deser.test;

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
import com.fasterxml.jackson.databind.util.TestBase;

import java.util.*;

import static org.junit.Assert.*;

public class DefaultDeserializationContextTest extends TestBase {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @org.junit.Test
    public void testFindObjectId() throws Exception {
        ObjectIdGenerator idGen = MAPPER.getFactory().getConfig().getObjectMapper()
                .getPolymorphicSerializer(String.class).getConfig()
                .getValueInstance(ObjectIdGenerator.class);
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(MAPPER.getFactory());
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        Object id = "testId";
        ObjectIdGenerator.IdKey key = idGen.key(id);
        ReadableObjectId roid = context.findObjectId(id, idGen, resolver);
        assertNotNull(roid);
        assertEquals(id, roid.getId());
        assertEquals(resolver, roid.getResolver());
    }

    @org.junit.Test
    public void testFindObjectId_NullId() throws Exception {
        ObjectIdGenerator idGen = MAPPER.getFactory().getConfig().getObjectMapper()
                .getPolymorphicSerializer(String.class).getConfig()
                .getValueInstance(ObjectIdGenerator.class);
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(MAPPER.getFactory());

        ObjectIdGenerator.IdKey key = idGen.key(null);
        assertNull(context.findObjectId(null, idGen, new SimpleObjectIdResolver()));
    }

    @org.junit.Test
    public void testFindObjectId_EmptyList() throws Exception {
        ObjectIdGenerator idGen = MAPPER.getFactory().getConfig().getObjectMapper()
                .getPolymorphicSerializer(String.class).getConfig()
                .getValueInstance(ObjectIdGenerator.class);
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(MAPPER.getFactory());
        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        assertNull(context.findObjectId(null, idGen, resolver));
    }

    @org.junit.Test
    public void testCheckUnresolvedObjectId() throws Exception {
        ObjectIdGenerator idGen = MAPPER.getFactory().getConfig().getObjectMapper()
                .getPolymorphicSerializer(String.class).getConfig()
                .getValueInstance(ObjectIdGenerator.class);
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(MAPPER.getFactory());
        context._objectIds = new LinkedHashMap<>();

        ObjectIdResolver resolver = new SimpleObjectIdResolver();
        for (int i = 0; i < 10; i++) {
            Object id = "testId" + i;
            ObjectIdGenerator.IdKey key = idGen.key(id);
            ReadableObjectId roid = new ReadableObjectId(key);
            roid.setResolver(resolver);
            context._objectIds.put(key, roid);
        }

        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // Expected exception
            List<String> unresolvedIds = e.getUnresolvedIds();
            assertEquals(10, unresolvedIds.size());
        }
    }

    @org.junit.Test
    public void testDeserializerInstance() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(MAPPER.getFactory());

        JsonDeserializer<String> deserializer = new StdDeserializer<>();
        Annotated ann = mock(Annotated.class);
        when(ann.getClass()).thenReturn(String.class);
        when(ann.hasMarkerAnnotation(JsonDeserializer.class)).thenReturn(true);
        when(ann.getRawValue()).thenReturn(deserializer);

        JsonDeserializer<String> deser = context.deserializerInstance(ann, deserializer);
        assertEquals(deserializer, deser);
    }

    @org.junit.Test
    public void testDeserializerInstance_NullDeserializerDef() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(MAPPER.getFactory());

        Annotated ann = mock(Annotated.class);
        when(ann.getClass()).thenReturn(String.class);

        JsonDeserializer<String> deser = context.deserializerInstance(ann, null);
        assertNull(deser);
    }

    @org.junit.Test
    public void testDeserializerInstance_ClassDeserializerDef() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(MAPPER.getFactory());

        Annotated ann = mock(Annotated.class);
        when(ann.getClass()).thenReturn(String.class);
        when(ann.hasMarkerAnnotation(JsonDeserializer.class)).thenReturn(false);
        when(ann.getRawValue()).thenReturn(JsonDeserializer.class);

        Class<? extends JsonDeserializer<?>> deserClass = JsonDeserializer.class;
        JsonDeserializer<String> deser = context.deserializerInstance(ann, deserClass);
        assertNotNull(deser);
        assertTrue(deser instanceof JsonDeserializer);
    }

    @org.junit.Test
    public void testDeserializerInstance_NullDeserializer() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(MAPPER.getFactory());

        Annotated ann = mock(Annotated.class);
        when(ann.getClass()).thenReturn(String.class);
        when(ann.hasMarkerAnnotation(JsonDeserializer.class)).thenReturn(false);
        when(ann.getRawValue()).thenReturn(null);

        JsonDeserializer<String> deser = context.deserializerInstance(ann, JsonDeserializer.class);
        assertNull(deser);
    }

    @org.junit.Test
    public void testDeserializerInstance_ClassNotFound() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(MAPPER.getFactory());

        Annotated ann = mock(Annotated.class);
        when(ann.getClass()).thenReturn(String.class);
        when(ann.hasMarkerAnnotation(JsonDeserializer.class)).thenReturn(false);
        when(ann.getRawValue()).thenReturn("com.test.InvalidDeserializer");

        try {
            context.deserializerInstance(ann, JsonDeserializer.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
            String msg = e.getMessage();
            assertTrue(msg.contains("com.test.InvalidDeserializer"));
            assertTrue(msg.contains("Class not found"));
        }
    }

    @org.junit.Test
    public void testKeyDeserializerInstance() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(MAPPER.getFactory());

        Annotated ann = mock(Annotated.class);
        when(ann.getClass()).thenReturn(String.class);
        when(ann.hasMarkerAnnotation(JsonDeserializer.class)).thenReturn(false);
        when(ann.getRawValue()).thenReturn(KeyDeserializer.class);

        KeyDeserializer deser = context.keyDeserializerInstance(ann, KeyDeserializer.class);
        assertNotNull(deser);
        assertTrue(deser instanceof KeyDeserializer);
    }

    @org.junit.Test
    public void testKeyDeserializerInstance_NullKeyDeserializerDef() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(MAPPER.getFactory());

        Annotated ann = mock(Annotated.class);
        when(ann.getClass()).thenReturn(String.class);

        KeyDeserializer deser = context.keyDeserializerInstance(ann, null);
        assertNull(deser);
    }

    @org.junit.Test
    public void testKeyDeserializerInstance_NullKeyDeserializer() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(MAPPER.getFactory());

        Annotated ann = mock(Annotated.class);
        when(ann.getClass()).thenReturn(String.class);
        when(ann.hasMarkerAnnotation(JsonDeserializer.class)).thenReturn(false);
        when(ann.getRawValue()).thenReturn(null);

        KeyDeserializer deser = context.keyDeserializerInstance(ann, KeyDeserializer.class);
        assertNull(deser);
    }

    @org.junit.Test
    public void testKeyDeserializerInstance_ClassNotFound() throws Exception {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(MAPPER.getFactory());

        Annotated ann = mock(Annotated.class);
        when(ann.getClass()).thenReturn(String.class);
        when(ann.hasMarkerAnnotation(JsonDeserializer.class)).thenReturn(false);
        when(ann.getRawValue()).thenReturn("com.test.InvalidKeyDeserializer");

        try {
            context.keyDeserializerInstance(ann, KeyDeserializer.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected exception
            String msg = e.getMessage();
            assertTrue(msg.contains("com.test.InvalidKeyDeserializer"));
            assertTrue(msg.contains("Class not found"));
        }
    }

    private static class StdDeserializer<T> implements JsonDeserializer<T> {
    }
}