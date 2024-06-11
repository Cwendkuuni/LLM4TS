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
import java.util.Map.Entry;

import static org.junit.Assert.*;

public class DefaultDeserializationContextTest extends TestBase {

    private static final ObjectMapper mapper = new ObjectMapper();

    @org.junit.Test
    public void testDefaultDeserializationContext_FindObjectId_NullId() {
        DefaultDeserializationContext ctx = mapper.getSerializationConfig().getDefaultDeserializationContext();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return null;
            }
        };
        assertNull(ctx.findObjectId(null, gen, ObjectIdResolver.class));
    }

    @org.junit.Test
    public void testDefaultDeserializationContext_FindObjectId_NewId() {
        DefaultDeserializationContext ctx = mapper.getSerializationConfig().getDefaultDeserializationContext();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("testKey");
            }
        };
        ReadableObjectId roid = ctx.findObjectId(new Object(), gen, ObjectIdResolver.class);
        assertNotNull(roid);
        assertEquals("testKey", roid.getKey().getKeyAsString());
    }

    @org.junit.Test
    public void testDefaultDeserializationContext_FindObjectId_UsedId() {
        DefaultDeserializationContext ctx = mapper.getSerializationConfig().getDefaultDeserializationContext();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("testKey");
            }
        };
        ReadableObjectId roid1 = ctx.findObjectId(new Object(), gen, ObjectIdResolver.class);
        ReadableObjectId roid2 = ctx.findObjectId(new Object(), gen, ObjectIdResolver.class);
        assertSame(roid1, roid2);
    }

    @org.junit.Test(expected = UnresolvedForwardReference.class)
    public void testDefaultDeserializationContext_FindObjectId_UnresolvedId() {
        DefaultDeserializationContext ctx = mapper.getSerializationConfig().getDefaultDeserializationContext();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("testKey");
            }
        };
        ctx.findObjectId(new Object(), gen, ObjectIdResolver.class);
        ctx.findObjectId(null, gen, ObjectIdResolver.class);
    }

    @org.junit.Test
    public void testDefaultDeserializationContext_DeserializerInstance_NullDef() {
        DefaultDeserializationContext ctx = mapper.getSerializationConfig().getDefaultDeserializationContext();
        Annotated ann = null;
        Object deserDef = null;
        JsonDeserializer<Object> deserializer = ctx.deserializerInstance(ann, deserDef);
        assertNull(deserializer);
    }

    @org.junit.Test
    public void testDefaultDeserializationContext_DeserializerInstance_EmptyClass() {
        DefaultDeserializationContext ctx = mapper.getSerializationConfig().getDefaultDeserializationContext();
        Annotated ann = null;
        Object deserDef = Object.class;
        JsonDeserializer<Object> deserializer = ctx.deserializerInstance(ann, deserDef);
        assertNull(deserializer);
    }

    @org.junit.Test
    public void testDefaultDeserializationContext_DeserializerInstance_JsonDeserializer() {
        DefaultDeserializationContext ctx = mapper.getSerializationConfig().getDefaultDeserializationContext();
        Annotated ann = null;
        Object deserDef = new JsonDeserializer<String>() {};
        JsonDeserializer<Object> deserializer = ctx.deserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof JsonDeserializer);
    }

    @org.junit.Test
    public void testDefaultDeserializationContext_DeserializerInstance_Class() {
        DefaultDeserializationContext ctx = mapper.getSerializationConfig().getDefaultDeserializationContext();
        Annotated ann = null;
        Object deserDef = JsonDeserializer.class;
        JsonDeserializer<Object> deserializer = ctx.deserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof JsonDeserializer);
    }

    @org.junit.Test
    public void testDefaultDeserializationContext_KeyDeserializerInstance_NullDef() {
        DefaultDeserializationContext ctx = mapper.getSerializationConfig().getDefaultDeserializationContext();
        Annotated ann = null;
        Object deserDef = null;
        KeyDeserializer deserializer = ctx.keyDeserializerInstance(ann, deserDef);
        assertNull(deserializer);
    }

    @org.junit.Test
    public void testDefaultDeserializationContext_KeyDeserializerInstance_EmptyClass() {
        DefaultDeserializationContext ctx = mapper.getSerializationConfig().getDefaultDeserializationContext();
        Annotated ann = null;
        Object deserDef = Object.class;
        KeyDeserializer deserializer = ctx.keyDeserializerInstance(ann, deserDef);
        assertNull(deserializer);
    }

    @org.junit.Test
    public void testDefaultDeserializationContext_KeyDeserializerInstance_JsonDeserializer() {
        DefaultDeserializationContext ctx = mapper.getSerializationConfig().getDefaultDeserializationContext();
        Annotated ann = null;
        Object deserDef = new JsonDeserializer<String>() {};
        KeyDeserializer deserializer = ctx.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof JsonDeserializer);
    }

    @org.junit.Test
    public void testDefaultDeserializationContext_KeyDeserializerInstance_Class() {
        DefaultDeserializationContext ctx = mapper.getSerializationConfig().getDefaultDeserializationContext();
        Annotated ann = null;
        Object deserDef = JsonDeserializer.class;
        KeyDeserializer deserializer = ctx.keyDeserializerInstance(ann, deserDef);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof JsonDeserializer);
    }

    @org.junit.Test
    public void testDefaultDeserializationContext_CheckUnresolvedObjectId_EmptyMap() {
        DefaultDeserializationContext ctx = mapper.getSerializationConfig().getDefaultDeserializationContext();
        ctx.checkUnresolvedObjectId();
    }

    @org.junit.Test
    public void testDefaultDeserializationContext_CheckUnresolvedObjectId_UnresolvedId() {
        DefaultDeserializationContext ctx = mapper.getSerializationConfig().getDefaultDeserializationContext();
        ctx._objectIds = new LinkedHashMap<>();
        ctx._objectIds.put(new ObjectIdGenerator.IdKey("testKey"), new ReadableObjectId(new ObjectIdGenerator.IdKey("testKey")));
        try {
            ctx.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // Expected
        }
    }

    @org.junit.Test
    public void testDefaultDeserializationContext_DeserializerInstance_ResolvableDeserializer() {
        DefaultDeserializationContext ctx = mapper.getSerializationConfig().getDefaultDeserializationContext();
        Object id = new Object();
        ObjectIdGenerator<?> gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new ObjectIdGenerator.IdKey("testKey");
            }
        };
        ReadableObjectId roid = ctx.findObjectId(id, gen, ObjectIdResolver.class);
        ResolvableDeserializer deserializer = new ResolvableDeserializer(JsonDeserializer.None.class);
        deserializer.resolve(ctx);
        JsonDeserializer<Object> deser = ctx.deserializerInstance(null, deserializer);
        assertNotNull(deser);
    }
}