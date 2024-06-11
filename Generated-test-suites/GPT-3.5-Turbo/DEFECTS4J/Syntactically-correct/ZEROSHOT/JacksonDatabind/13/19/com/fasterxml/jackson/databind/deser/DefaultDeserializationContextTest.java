package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class DefaultDeserializationContextTest {

    private DefaultDeserializationContext deserializationContext;

    @Before
    public void setUp() {
        deserializationContext = new DefaultDeserializationContext.Impl(new DeserializerFactory(), new DeserializerCache());
    }

    @Test
    public void testFindObjectId() {
        Object id = "id";
        ObjectIdGenerator<?> gen = new ObjectIdGenerator<>();
        ObjectIdResolver resolverType = new SimpleObjectIdResolver();

        ReadableObjectId objectId = deserializationContext.findObjectId(id, gen, resolverType);

        assertNotNull(objectId);
        assertSame(objectId, deserializationContext.findObjectId(id, gen, resolverType));
    }

    @Test
    public void testFindObjectId_withNullParameters_shouldReturnNull() {
        ReadableObjectId objectId = deserializationContext.findObjectId(null, null, null);

        assertNull(objectId);
    }

    @Test
    public void testCreateInstance() {
        DeserializationConfig config = new DeserializationConfig(null, null, null);
        JsonParser jp = null;
        InjectableValues values = null;

        DefaultDeserializationContext context = deserializationContext.createInstance(config, jp, values);

        assertNotNull(context);
    }

    @Test
    public void testWith() {
        DeserializerFactory factory = new DeserializerFactory();

        DefaultDeserializationContext context = deserializationContext.with(factory);

        assertNotNull(context);
        assertSame(factory, context._factory);
    }

    @Test
    public void testStaticCopy() {
        DefaultDeserializationContext context = deserializationContext.copy();

        assertNotNull(context);
    }

    @Test(expected = IllegalStateException.class)
    public void testCopy_notOverridden_shouldThrowException() {
        DefaultDeserializationContext context = new DefaultDeserializationContext() {
            // must override copy since it is called by the test
        };

        context.copy();
    }

    @Test
    public void testDeserializerInstance_withDeserializerDefClass_shouldReturnDeserializer() throws Exception {
        Annotated ann = new Annotated() {
            // dummy class just to test the method
        };
        Class<JsonParser> deserDef = JsonParser.class;

        JsonDeserializer<Object> deser = deserializationContext.deserializerInstance(ann, deserDef);

        assertNotNull(deser);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstance_withDeserializerDefClassNotAssignable_shouldThrowException() throws Exception {
        Annotated ann = new Annotated() {
            // dummy class just to test the method
        };
        Class<String> deserDef = String.class;

        deserializationContext.deserializerInstance(ann, deserDef);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstance_withInvalidDeserializerDefinition_shouldThrowException() throws Exception {
        Annotated ann = new Annotated() {
            // dummy class just to test the method
        };
        Object deserDef = new Object();

        deserializationContext.deserializerInstance(ann, deserDef);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializerInstance_withDeserializerDefClassInstance_shouldReturnDeserializer() throws Exception {
        Annotated ann = new Annotated() {
            // dummy class just to test the method
        };
        Object deserDef = new JsonDeserializer<Object>() {
            // dummy class just to test the method

            @Override
            public Object deserialize(JsonParser p, DeserializationContext ctxt) {
                return null;
            }
        };

        JsonDeserializer<Object> deser = deserializationContext.deserializerInstance(ann, deserDef);

        assertNotNull(deser);
    }

    @Test
    public void testKeyDeserializerInstance_withKeyDeserDefClass_shouldReturnKeyDeserializer() throws Exception {
        Annotated ann = new Annotated() {
            // dummy class just to test the method
        };
        Class<JsonParser> deserDef = JsonParser.class;

        KeyDeserializer deser = deserializationContext.keyDeserializerInstance(ann, deserDef);

        assertNotNull(deser);
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstance_withKeyDeserDefClassNotAssignable_shouldThrowException() throws Exception {
        Annotated ann = new Annotated() {
            // dummy class just to test the method
        };
        Class<String> deserDef = String.class;

        deserializationContext.keyDeserializerInstance(ann, deserDef);
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstance_withInvalidKeyDeserializerDefinition_shouldThrowException() throws Exception {
        Annotated ann = new Annotated() {
            // dummy class just to test the method
        };
        Object deserDef = new Object();

        deserializationContext.keyDeserializerInstance(ann, deserDef);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testKeyDeserializerInstance_withKeyDeserDefClassInstance_shouldReturnKeyDeserializer() throws Exception {
        Annotated ann = new Annotated() {
            // dummy class just to test the method
        };
        Object deserDef = new KeyDeserializer() {
            // dummy class just to test the method

            @Override
            public Object deserializeKey(String key, DeserializationContext ctxt) {
                return null;
            }
        };

        KeyDeserializer deser = deserializationContext.keyDeserializerInstance(ann, deserDef);

        assertNotNull(deser);
    }

    @Test
    public void testCheckUnresolvedObjectId_objectIdsIsNull_shouldNotThrowException() throws UnresolvedForwardReference {
        deserializationContext.checkUnresolvedObjectId();
    }

    @Test
    public void testCheckUnresolvedObjectId_unresolvedForwardReferencesNotAllowed_shouldThrowException() {
        deserializationContext.enable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);

        ReadableObjectId objectId = new ReadableObjectId(new IdKey(null, null));
        objectId.setResolver(new SimpleObjectIdResolver());
        deserializationContext._objectIds = new LinkedHashMap<IdKey, ReadableObjectId>();
        deserializationContext._objectIds.put(objectId.getKey(), objectId);

        try {
            deserializationContext.checkUnresolvedObjectId();
            fail("UnresolvedForwardReference was expected");
        } catch (UnresolvedForwardReference e) {
            assertNotNull(e);
            assertEquals("Unresolved forward references for: ", e.getMessage());
        }
    }

    @Test
    public void testCheckUnresolvedObjectId_unresolvedForwardReferencesAllowed_shouldNotThrowException() {
        deserializationContext.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);

        ReadableObjectId objectId = new ReadableObjectId(new IdKey(null, null));
        objectId.setResolver(new SimpleObjectIdResolver());
        deserializationContext._objectIds = new LinkedHashMap<IdKey, ReadableObjectId>();
        deserializationContext._objectIds.put(objectId.getKey(), objectId);

        try {
            deserializationContext.checkUnresolvedObjectId();
        } catch (UnresolvedForwardReference e) {
            fail("UnresolvedForwardReference was not expected");
        }
    }

}