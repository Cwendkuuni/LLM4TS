package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class DefaultDeserializationContextTest {

    private DeserializationConfig config;
    private JsonParser jp;
    private InjectableValues values;

    private static ObjectMapper mapper = JsonMapper.builder().build();

    @Before
    public void setUp() {
        config = new DeserializationConfig(null, null, null);
        ObjectReader reader = mapper.reader(config);
        jp = reader.getFactory().createParser("");
        values = null;
    }

    @Test
    public void testFindObjectId() {
        JsonDeserializer<Object> deserDef = null;
        ObjectIdResolver resolverType = null;
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);

        try {
            ReadableObjectId readableObjectId = new ReadableObjectId(new ObjectIdGenerator.IdKey(""), new SimpleObjectIdResolver());
            LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> objectIds = new LinkedHashMap<>();
            objectIds.put(new ObjectIdGenerator.IdKey(""), readableObjectId);

            context._objectIds = objectIds;

            ReadableObjectId objectId = context.findObjectId(1, null, resolverType);
            Assert.assertEquals(readableObjectId, objectId);

            context._objectIds = null;
            objectId = context.findObjectId(1, null, resolverType);
            Assert.assertNotNull(objectId);
            Assert.assertEquals(readableObjectId.getKey().key, objectId.getKey().key);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        try {
            boolean enabled = context.isEnabled(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
            Assert.assertFalse(enabled);

            context._objectIds = null;
            context.checkUnresolvedObjectId();

            context._objectIds = new LinkedHashMap<>();
            UnresolvedForwardReference exception = new UnresolvedForwardReference("Unresolved forward references for: ");
            context._objectIds.put(new ObjectIdGenerator.IdKey(""), new ReadableObjectId(new ObjectIdGenerator.IdKey("")));
            try {
                context.checkUnresolvedObjectId();
                Assert.fail("Exception not thrown");
            } catch (UnresolvedForwardReference e) {
                Assert.assertEquals(exception.getMessage(), e.getMessage());
            }
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testDeserializerInstance() {
        Annotated ann = null;
        Object deserDef = null;
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        try {
            JsonDeserializer<Object> deser = context.deserializerInstance(ann, deserDef);
            Assert.assertNull(deser);

            deserDef = Object.class;
            AssertionError error = null;

            try {
                context.deserializerInstance(ann, deserDef);
            } catch (AssertionError e) {
                error = e;
            }

            Assert.assertNotNull(error);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testKeyDeserializerInstance() {
        Annotated ann = null;
        Object deserDef = null;
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        try {
            KeyDeserializer deser = context.keyDeserializerInstance(ann, deserDef);
            Assert.assertNull(deser);

            deserDef = Object.class;
            AssertionError error = null;

            try {
                context.keyDeserializerInstance(ann, deserDef);
            } catch (AssertionError e) {
                error = e;
            }

            Assert.assertNotNull(error);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        try {
            DefaultDeserializationContext instance = context.createInstance(config, jp, values);
            Assert.assertNotNull(instance);
            Assert.assertNotEquals(context, instance);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testWith() {
        DeserializerFactory factory = null;
        DefaultDeserializationContext context = new DefaultDeserializationContext.Impl(null);
        try {
            DefaultDeserializationContext instance = context.with(factory);
            Assert.assertNotNull(instance);
            Assert.assertNotEquals(context, instance);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}