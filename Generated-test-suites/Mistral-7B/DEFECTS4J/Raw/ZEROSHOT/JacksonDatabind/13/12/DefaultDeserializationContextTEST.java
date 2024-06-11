package com.example.jackson.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
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

/**
 * Unit tests for {@link DefaultDeserializationContext} class.
 */
public class DefaultDeserializationContextTest extends TestBase {

    private DefaultDeserializationContext ctx;
    private DeserializerFactory factory;

    @Before
    public void setup() {
        factory = mock(DeserializerFactory.class);
        ctx = new DefaultDeserializationContext.Impl(factory);
    }

    @Test
    public void testFindObjectId() {
        String id = "test_id";
        ObjectIdGenerator<String> gen = mock(ObjectIdGenerator.class);
        ObjectIdResolver resolver = mock(ObjectIdResolver.class);
        ReadableObjectId expected = new ReadableObjectId(new IdKey("test_key"), resolver);

        when(gen.key(eq(id))).thenReturn(new IdKey("test_key"));
        when(resolver.newForDeserialization(ctx)).thenReturn(resolver);

        ReadableObjectId actual = ctx.findObjectId(id, gen);
        assertNotNull(actual);
        assertEquals(expected.getKey().getKey(), actual.getKey().getKey());
        assertEquals(expected.getResolver(), actual.getResolver());
    }

    @Test
    public void testFindObjectIdWithSimpleObjectIdResolver() {
        String id = "test_id";
        ObjectIdGenerator<String> gen = spy(new SimpleObjectIdResolver());
        ObjectIdResolver resolver = mock(ObjectIdResolver.class);
        ReadableObjectId expected = new ReadableObjectId(new IdKey("test_key"), resolver);

        when(gen.key(eq(id))).thenReturn(new IdKey("test_key"));
        when(resolver.newForDeserialization(ctx)).thenReturn(resolver);

        ReadableObjectId actual = ctx.findObjectId(id, gen);
        assertNotNull(actual);
        assertEquals(expected.getKey().getKey(), actual.getKey().getKey());
        assertEquals(expected.getResolver(), actual.getResolver());

        // Check that SimpleObjectIdResolver was not modified
        assertEquals(SimpleObjectIdResolver.class, gen.getClass());
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectId() {
        IdKey key = new IdKey("test_key");
        ReadableObjectId id1 = new ReadableObjectId(key);
        ReadableObjectId id2 = new ReadableObjectId(key);
        id2.setResolver(mock(ObjectIdResolver.class));
        id2.addReferringProperty(new Referring("test_property", String.class, new Object(){}.getClass().getDeclaredField("test_field")));

        ctx._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>();
        ctx._objectIds.put(key, id1);
        ctx._objectIds.put(key, id2);
        ctx.checkUnresolvedObjectId();
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
        Object id = new Object(){};
        Annotated ann = mock(Annotated.class);
        Object deserDef = mock(JsonDeserializer.class);

        JsonDeserializer<Object> expected = deserDef;
        when(deserDef.getClass()).thenReturn(JsonDeserializer.class);

        JsonDeserializer<Object> actual = ctx.deserializerInstance(ann, deserDef, gen);
        assertEquals(expected, actual);
    }

    @Test
    public void testDeserializerInstanceWithClass() throws JsonMappingException {
        ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
        Object id = new Object(){};
        Annotated ann = mock(Annotated.class);
        Class<JsonDeserializer<Object>> deserClass = JsonDeserializer.class;

        JsonDeserializer<Object> expected = spy(ClassUtil.createInstance(deserClass));
        when(deserClass.isAssignableFrom(JsonDeserializer.class)).thenReturn(true);

        JsonDeserializer<Object> actual = ctx.deserializerInstance(ann, id, gen, deserClass);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceInvalidClass() throws JsonMappingException {
        ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
        Object id = new Object(){};
        Annotated ann = mock(Annotated.class);
        Class<?> deserClass = Integer.class;

        JsonDeserializer<Object> actual = ctx.deserializerInstance(ann, id, gen, deserClass);
        fail("Should have thrown exception");
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
        Object id = new Object(){};
        Annotated ann = mock(Annotated.class);
        Object deserDef = mock(JsonDeserializer.class);

        KeyDeserializer expected = deserDef;
        when(deserDef.getClass()).thenReturn(JsonDeserializer.class);

        KeyDeserializer actual = ctx.keyDeserializerInstance(ann, deserDef);
        assertEquals(expected, actual);
    }

    @Test
    public void testKeyDeserializerInstanceWithClass() throws JsonMappingException {
        ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
        Object id = new Object(){};
        Annotated ann = mock(Annotated.class);
        Class<KeyDeserializer<Object>> deserClass = KeyDeserializer.class;

        KeyDeserializer expected = spy(ClassUtil.createInstance(deserClass));
        when(deserClass.isAssignableFrom(KeyDeserializer.class)).thenReturn(true);

        KeyDeserializer actual = ctx.keyDeserializerInstance(ann, id, gen, deserClass);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceInvalidClass() throws JsonMappingException {
        ObjectIdGenerator<?> gen = mock(ObjectIdGenerator.class);
        Object id = new Object(){};
        Annotated ann = mock(Annotated.class);
        Class<?> deserClass = Integer.class;

        KeyDeserializer actual = ctx.keyDeserializerInstance(ann, id, gen, deserClass);
        fail("Should have thrown exception");
    }
}