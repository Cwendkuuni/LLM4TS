package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdResolver;
import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import java.util.*;

public class DefaultDeserializationContextTest extends TestBase {

    @SuppressWarnings({"unchecked", "rawtypes"})
    private DefaultDeserializationContext context;

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        context = mapper.getDeserializationContext();
    }

    // Testing the constructor --------------------------------------------------

    @Test
    public void testDefaultDeserializationContextConstructor() {
        assertNotNull(context);
    }

    @Test(expected = IllegalStateException.class)
    public void testDefaultDeserializationContextCopyEmpty() {
        context.copy();
    }

    // Testing the findObjectId method ------------------------------------------

    @Test
    public void testFindObjectIdNullId() {
        ObjectIdGenerator idGen = mock(ObjectIdGenerator.class);
        ObjectIdResolver resolver = mock(ObjectIdResolver.class);
        context.findObjectId(null, idGen, resolver);
    }

    @Test
    public void testFindObjectIdNullKeyAndGen() {
        context.findObjectId(null, null);
    }

    @Test
    public void testFindObjectIdNullKeyAndResolver() {
        ObjectIdGenerator idGen = mock(ObjectIdGenerator.class);
        context.findObjectId(null, idGen);
    }

    @Test
    public void testFindObjectIdValidIdAndGen() {
        ObjectIdGenerator idGen = mock(ObjectIdGenerator.class);
        ObjectIdResolver resolver = mock(ObjectIdResolver.class);
        ObjectIdKey key = new ObjectIdKey("key");
        ReadableObjectId id = new ReadableObjectId(key);
        when(idGen.key(anyObject())).thenReturn(key);

        context.findObjectId("id", idGen, resolver);
        assertEquals(id, context._objectIds.get(key));
    }

    @Test
    public void testFindObjectIdValidIdAndResolver() {
        ObjectIdResolver resolver = mock(ObjectIdResolver.class);
        ObjectIdKey key = new ObjectIdKey("key");
        ReadableObjectId id = new ReadableObjectId(key);
        when(resolver.canUseFor(any(ObjectIdResolver.class))).thenReturn(true);
        when(resolver.newForDeserialization(context)).thenReturn(resolver);

        context.findObjectId("id", new SimpleObjectIdResolver());
        assertEquals(id, context._objectIds.get(key));
    }

    // Testing the checkUnresolvedObjectId method ---------------------------------

    @Test
    public void testCheckUnresolvedObjectIdEmptyMap() {
        context.checkUnresolvedObjectId();
    }

    @Test(expected = UnresolvedForwardReference.class)
    public void testCheckUnresolvedObjectIdWithUnresolvedIds() {
        ObjectIdGenerator idGen = mock(ObjectIdGenerator.class);
        ObjectIdResolver resolver = mock(ObjectIdResolver.class);
        ObjectIdKey key1 = new ObjectIdKey("key1");
        ObjectIdKey key2 = new ObjectIdKey("key2");
        ReadableObjectId id1 = new ReadableObjectId(key1);
        ReadableObjectId id2 = new ReadableObjectId(key2);
        id1.setResolver(resolver);
        id2.setResolver(resolver);
        context._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>() {{
            put(key1, id1);
            put(key2, id2);
        }};

        context.checkUnresolvedObjectId();
    }

    // Testing the deserializerInstance method ------------------------------------

    @Test
    public void testDeserializerInstanceNullAnnotated() {
        context.deserializerInstance(null, null);
    }

    @Test
    public void testDeserializerInstanceNullDeserDef() {
        Annotated ann = mock(Annotated.class);
        context.deserializerInstance(ann, null);
    }

    @Test
    public void testDeserializerInstanceJsonDeserializer() {
        Annotated ann = mock(Annotated.class);
        JsonDeserializer deser = mock(JsonDeserializer.class);
        context.deserializerInstance(ann, deser);
        assertEquals(deser, context._deserializers.get(ann));
    }

    @Test
    public void testDeserializerInstanceClassOfJsonDeserializer() {
        Annotated ann = mock(Annotated.class);
        JsonDeserializer deser = mock(JsonDeserializer.class);
        when(ann.getClass()).thenReturn(JsonDeserializer.class);
        context.deserializerInstance(ann, deser);
        assertEquals(deser, context._deserializers.get(ann));
    }

    @Test
    public void testDeserializerInstanceClassNotJsonDeserializer() {
        Annotated ann = mock(Annotated.class);
        String className = "com.example.MyCustomDeserializer";
        Class deserClass = loadClass(className);
        JsonDeserializer deser = mock(JsonDeserializer.class);
        when(deserClass.newInstance()).thenReturn(deser);
        context.deserializerInstance(ann, deserClass);
        assertEquals(deser, context._deserializers.get(ann));
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializerInstanceInvalidClass() {
        Annotated ann = mock(Annotated.class);
        String className = "java.lang.String";
        Class deserClass = loadClass(className);
        context.deserializerInstance(ann, deserClass);
    }

    // Testing the keyDeserializerInstance method ----------------------------------

    @Test
    public void testKeyDeserializerInstanceNullAnnotated() {
        context.keyDeserializerInstance(null, null);
    }

    @Test
    public void testKeyDeserializerInstanceNullDeserDef() {
        Annotated ann = mock(Annotated.class);
        context.keyDeserializerInstance(ann, null);
    }

    @Test
    public void testKeyDeserializerInstanceJsonKeyDeserializer() {
        Annotated ann = mock(Annotated.class);
        KeyDeserializer deser = mock(KeyDeserializer.class);
        context.keyDeserializerInstance(ann, deser);
        assertEquals(deser, context._keyDeserializers.get(ann));
    }

    @Test
    public void testKeyDeserializerInstanceClassOfJsonKeyDeserializer() {
        Annotated ann = mock(Annotated.class);
        KeyDeserializer deser = mock(KeyDeserializer.class);
        when(ann.getClass()).thenReturn(KeyDeserializer.class);
        context.keyDeserializerInstance(ann, deser);
        assertEquals(deser, context._keyDeserializers.get(ann));
    }

    @Test
    public void testKeyDeserializerInstanceClassNotJsonKeyDeserializer() {
        Annotated ann = mock(Annotated.class);
        String className = "com.example.MyCustomKeyDeserializer";
        Class deserClass = loadClass(className);
        KeyDeserializer deser = mock(KeyDeserializer.class);
        when(deserClass.newInstance()).thenReturn(deser);
        context.keyDeserializerInstance(ann, deserClass);
        assertEquals(deser, context._keyDeserializers.get(ann));
    }

    @Test(expected = IllegalStateException.class)
    public void testKeyDeserializerInstanceInvalidClass() {
        Annotated ann = mock(Annotated.class);
        String className = "java.lang.String";
        Class deserClass = loadClass(className);
        context.keyDeserializerInstance(ann, deserClass);
    }

    // Testing the with method ----------------------------------------------------

    @Test
    public void testWithNullFactory() {
        assertNull(context.with(null));
    }

    @Test
    public void testWithNewDefaultDeserializationContext() {
        DefaultDeserializationContext newContext = context.with(null);
        assertEquals(context.getClass(), newContext.getClass());
    }

    @Test
    public void testWithNewDeserializationContextSubclass() {
        DefaultDeserializationContext newContext = context.with(DefaultDeserializationContextSubclass.class);
        assertNotSame(context.getClass(), newContext.getClass());
        assertEquals(DefaultDeserializationContextSubclass.class, newContext.getClass());
    }

    // Testing the createInstance method ------------------------------------------

    @Test
    public void testCreateInstanceNullConfig() {
        assertNull(context.createInstance(null, null, null));
    }

    @Test
    public void testCreateInstanceNullJp() {
        DeserializationConfig config = new DeserializationConfig();
        assertNull(context.createInstance(config, null, null));
    }

    @Test
    public void testCreateInstanceNullValues() {
        DeserializationConfig config = new DeserializationConfig();
        JsonParser jp = createMockParser();
        InjectableValues values = null;
        DefaultDeserializationContext newContext = context.createInstance(config, jp, values);
        assertEquals(context.getClass(), newContext.getClass());
    }

    @Test
    public void testCreateInstanceValidArguments() {
        DeserializationConfig config = new DeserializationConfig();
        JsonParser jp = createMockParser();
        InjectableValues values = new InjectableValues();
        DefaultDeserializationContext newContext = context.createInstance(config, jp, values);
        assertNotSame(context, newContext);
        assertEquals(DefaultDeserializationContext.class, newContext.getClass());
    }

    // Testing the with method and createInstance ----------------------------------

    @Test
    public void testWithAndCreateInstanceNewDefaultDeserializationContext() {
        DefaultDeserializationContext newContext = context.with(null).createInstance(null, null, null);
        assertEquals(context.getClass(), newContext.getClass());
    }

    @Test
    public void testWithAndCreateInstanceNewDeserializationContextSubclass() {
        DefaultDeserializationContext newContext = context.with(DefaultDeserializationContextSubclass.class).createInstance(null, null, null);
        assertNotSame(context.getClass(), newContext.getClass());
        assertEquals(DefaultDeserializationContextSubclass.class, newContext.getClass());
    }

    // Testing the copy method ----------------------------------------------------

    @Test
    public void testCopyEmptyContext() {
        DefaultDeserializationContext copy = context.copy();
        assertEquals(context, copy);
    }

    @Test
    public void testCopyPopulatedContext() {
        ObjectIdGenerator idGen = mock(ObjectIdGenerator.class);
        ObjectIdResolver resolver = mock(ObjectIdResolver.class);
        ObjectIdKey key = new ObjectIdKey("key");
        ReadableObjectId id = new ReadableObjectId(key);
        id.setResolver(resolver);
        context._objectIds = new LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId>() {{
            put(key, id);
        }};

        DefaultDeserializationContext copy = context.copy();
        Map<ObjectIdGenerator.IdKey, ReadableObjectId> newObjectIds = copy._objectIds;
        assertEquals(context._objectIds.size(), newObjectIds.size());
        for (Entry<ObjectIdGenerator.IdKey, ReadableObjectId> entry : context._objectIds.entrySet()) {
            ObjectIdGenerator.IdKey keyEntry = entry.getKey();
            ReadableObjectId idEntry = entry.getValue();
            ReadableObjectId newIdEntry = newObjectIds.get(keyEntry);
            assertEquals(idEntry, newIdEntry);
        }
    }

    // Testing the abstract methods -----------------------------------------------

    @Test(expected = IllegalStateException.class)
    public void testEmptyCopy() {
        context.copy();
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateInstanceEmpty() {
        context.createInstance(null, null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateInstanceNullFactory() {
        context.createInstance(null, null, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithNullFactory() {
        context.with(null);
    }

    // Testing the Impl subclass --------------------------------------------------

    static class DefaultDeserializationContextSubclass extends DefaultDeserializationContext {
        private static final long serialVersionUID = 1L;

        public DefaultDeserializationContextSubclass() {
            super(null);
        }

        @Override
        public DefaultDeserializationContext copy() {
            return new DefaultDeserializationContextSubclass();
        }

        @Override
        public DefaultDeserializationContext createInstance(DeserializationConfig config, JsonParser jp, InjectableValues values) {
            return new DefaultDeserializationContextSubclass();
        }

        @Override
        public DefaultDeserializationContext with(DeserializerFactory factory) {
            return new DefaultDeserializationContextSubclass();
        }
    }
}