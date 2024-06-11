package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class DefaultDeserializationContextTest {

    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        ObjectIdGenerator.IdKey idKey = new ObjectIdGenerator.IdKey("", "", null);
        ReadableObjectId readableObjectId = context.findObjectId("", null, null);
        assertNotNull(readableObjectId);
        assertEquals(idKey.hashCode(), readableObjectId.getKey().hashCode());
    }

    @Test
    public void testCheckUnresolvedObjectId() throws UnresolvedForwardReference {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        context._objectIds = new LinkedHashMap<>();
        ReadableObjectId readableObjectId = new ReadableObjectId(null);
        context._objectIds.put(null, readableObjectId);

        UnresolvedForwardReference exception = null;
        Entry<DefaultDeserializationContext.Impl.IdKey, ReadableObjectId> entry = context._objectIds.entrySet()
                .iterator().next();
        ReadableObjectId roid = entry.getValue();
        roid.appendReferring(new DefaultDeserializationContext.Impl.Referring(readableObjectId, null));
        try {
            context.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            exception = e;
        }

        assertNotNull(exception);
        assertNotNull(exception.getUnresolvedIds());
        assertEquals(0, exception.getUnresolvedIds().size());
    }

    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        JsonDeserializer<Object> deserializer = context.deserializerInstance(null, null);
        assertNull(deserializer);
    }

    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        KeyDeserializer keyDeserializer = context.keyDeserializerInstance(null, null);
        assertNull(keyDeserializer);
    }

    @Test
    public void testWith() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        DefaultDeserializationContext.Impl newContext = context.with(null);
        assertNotNull(newContext);
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext.Impl context = new DefaultDeserializationContext.Impl(null);
        DeserializationConfig config = new DeserializationConfig(null, null, null, null, null, null, null);
        JsonParser jp = null;
        InjectableValues values = null;

        DefaultDeserializationContext.Impl newInstance = context.createInstance(config, jp, values);
        assertNotNull(newInstance);
    }
}