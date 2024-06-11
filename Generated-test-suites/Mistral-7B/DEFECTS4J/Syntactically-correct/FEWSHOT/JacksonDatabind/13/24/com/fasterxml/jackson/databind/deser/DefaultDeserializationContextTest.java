package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.fasterxml.jackson.databind.util.JavaType;

public class DefaultDeserializationContextTest {

    private static DefaultDeserializationContext createContext() {
        ObjectMapper mapper = new ObjectMapper();
        DefaultDeserializationContext context = mapper.getDeserializationContext();
        return context;
    }

    /**
     * Test for findObjectId method
     */
    @Test
    public void testFindObjectId() {
        DefaultDeserializationContext context = createContext();

        ObjectIdGenerator<?> gen = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey();
            }
        };

        ObjectIdResolver resolver = new SimpleObjectIdResolver();

        ReadableObjectId id = context.findObjectId(null, gen, null);
        assertNull(id);

        id = context.findObjectId(new Object(), gen, null);
        assertNull(id);

        id = context.findObjectId(new Object(), gen, resolver);
        assertNotNull(id);

        id = context.findObjectId(new Object(), gen, null);
        assertNull(id);

        id = context.findObjectId(new Object(), gen, resolver);
        assertNotNull(id);

        id = context.findObjectId(new ObjectIdGenerator.IdKey(), gen, null);
        assertNull(id);

        id = context.findObjectId(new ObjectIdGenerator.IdKey(), gen, resolver);
        assertNotNull(id);
    }

    /**
     * Test for checkUnresolvedObjectId method
     */
    @Test
    public void testCheckUnresolvedObjectId() {
        DefaultDeserializationContext context = createContext();

        // Test with empty _objectIds
        context._objectIds = null;
        context.checkUnresolvedObjectId();

        // Test with no unresolved object ids
        context._objectIds = new HashMap<>();
        context.checkUnresolvedObjectId();

        // Test with unresolved object ids
        context._objectIds = new LinkedHashMap<>();
        ObjectIdGenerator.IdKey idKey1 = new IdKey();
        ObjectIdGenerator.IdKey idKey2 = new IdKey();
        JavaType javaType1 = JavaType.construct();
        JavaType javaType2 = JavaType.construct();
        ReadableObjectId id1 = new ReadableObjectId(idKey1);
        ReadableObjectId id2 = new ReadableObjectId(idKey2);
        id1.setResolver(new SimpleObjectIdResolver());
        id2.setResolver(new SimpleObjectIdResolver());
        id1.setReferringProperties(Collections.singletonList(new Referring(javaType1, 1)));
        id2.setReferringProperties(Collections.singletonList(new Referring(javaType2, 1)));
        context._objectIds.put(idKey1, id1);
        context._objectIds.put(idKey2, id2);
        try {
            context.checkUnresolvedObjectId();
            fail("Expected UnresolvedForwardReference exception");
        } catch (UnresolvedForwardReference e) {
            // Expected
        }
    }

    /**
     * Test for deserializerInstance method
     */
    @Test
    public void testDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = createContext();

        ObjectIdGenerator<?> idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey();
            }
        };

        Class<? extends JsonDeserializer<?>> deserializerClass1 = JsonDeserializer.class;
        Class<? extends JsonDeserializer<?>> deserializerClass2 = JsonDeserializer.None.class;

        JsonDeserializer<?> deserializer1 = context.deserializerInstance(new AnnotatedClass(), deserializerClass1);
        assertNotNull(deserializer1);

        JsonDeserializer<?> deserializer2 = context.deserializerInstance(new AnnotatedClass(), deserializerClass2);
        assertNull(deserializer2);
    }

    /**
     * Test for keyDeserializerInstance method
     */
    @Test
    public void testKeyDeserializerInstance() throws JsonMappingException {
        DefaultDeserializationContext context = createContext();

        ObjectIdGenerator<?> idGenerator = new ObjectIdGenerator() {
            @Override
            public ObjectIdGenerator.IdKey key(Object id) {
                return new IdKey();
            }
        };

        Class<? extends KeyDeserializer<?>> deserializerClass1 = KeyDeserializer.class;
        Class<? extends KeyDeserializer<?>> deserializerClass2 = KeyDeserializer.None.class;

        KeyDeserializer deserializer1 = context.keyDeserializerInstance(new AnnotatedClass(), deserializerClass1);
        assertNotNull(deserializer1);

        KeyDeserializer deserializer2 = context.keyDeserializerInstance(new AnnotatedClass(), deserializerClass2);
        assertNull(deserializer2);
    }

    /**
     * Test for with method
     */
    @Test
    public void testWith() {
        DefaultDeserializationContext context1 = createContext();
        DefaultDeserializationContext context2 = context1.with(new DeserializerFactory() {
            @Override
            public JsonDeserializer<? extends Object> createValueDeserializer(DeserializationConfig config, JsonParser p, Class<?> vc)
                    throws JsonMappingException, JsonProcessingException {
                return null;
            }
        });
        assertNotNull(context2);
        assertEquals(context1.getClass(), context1.getClass());
        assertNotEquals(context1.getClass(), context2.getClass());
    }

    /**
     * Test for createInstance method
     */
    @Test
    public void testCreateInstance() {
        DefaultDeserializationContext context1 = createContext();
        DefaultDeserializationContext context2 = new DefaultDeserializationContext.Impl(context1);
        assertNotNull(context2);
        assertEquals(context1.getClass(), context1.getClass());
        assertNotEquals(context1.getClass(), context2.getClass());
    }

    /**
     * Test for copy method
     */
    @Test
    public void testCopy() {
        DefaultDeserializationContext context1 = createContext();
        DefaultDeserializationContext context2 = context1.copy();
        assertNotNull(context2);
        assertEquals(context1.getClass(), context1.getClass());
        assertEquals(context1, context2);
    }

    /**
     * Test for constructor with null argument
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNull() {
        new DefaultDeserializationContext(null, null);
    }

    /**
     * Test for constructor with null _objectIds
     */
    @Test
    public void testConstructorNullObjectIds() {
        new DefaultDeserializationContext(new DefaultDeserializationContext(), null, null, null);
    }

    /**
     * Test for constructor with null _objectIdResolvers
     */
    @Test
    public void testConstructorNullObjectIdResolvers() {
        new DefaultDeserializationContext(new DefaultDeserializationContext(), null, null, new ArrayList<ObjectIdResolver>());
    }

}

class AnnotatedClass implements Annotated {
    @Override
    public AnnotationMetaData getAnnotationMetadata() {
        return new AnnotationMetaData();
    }

    @Override
    public Object getRawValue() {
        return null;
    }

    @Override
    public JsonTypeValue getType() {
        return null;
    }

    @Override
    public ObjectIdGenerator.IdKey getId() {
        return null;
    }

    @Override
    public JavaType getValueType() {
        return null;
    }

    @Override
    public JavaType getParameterType() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Class<?> getValueClass() {
        return null;
    }

    @Override
    public JsonSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public JsonDeserializer<?> getDeserializer() {
        return null;
    }

    @Override
    public JsonSerializer<?> getValueInstantiator() {
        return null;
    }

    @Override
    public ObjectIdGenerator<?> getValueIdGenerator() {
        return null;
    }

    @Override
    public ObjectIdGenerator<?> getIdGenerator() {
        return null;
    }

    @Override
    public JsonSerializer<?> getContainerSerializer() {
        return null;
    }

    @Override
    public JsonDeserializer<?> getContainedDeserializer() {
        return null;
    }

    @Override
    public JsonSerializer<?> getNullValueSerializer() {
        return null;
    }

    @Override
    public Collection<JsonSerializerModifier> getSerializationModifiers() {
        return Collections.emptyList();
    }

    @Override
    public JsonSerializerModifier getSerializerModifier(Class<?> modifierClass) {
        return null;
    }

    @Override
    public Collection<JsonDeserializerModifier> getDeserializationModifiers() {
        return Collections.emptyList();
    }

    @Override
    public JsonDeserializerModifier getDeserializationModifier(Class<?> modifierClass) {
        return null;
    }

    @Override
    public boolean hasSingleValue() {
        return false;
    }

    @Override
    public boolean hasSingleValueOrWrapper() {
        return false;
    }

    @Override
    public JavaType getTargetType() {
        return null;
    }

    @Override
    public boolean hasSingleValueOrWrapperInType() {
        return false;
    }

    @Override
    public boolean isContainer() {
        return false;
    }

    @Override
    public boolean isContainerElement() {
        return false;
    }

    @Override
    public boolean isValueStable() {
        return true;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public boolean isOptional() {
        return false;
    }

    @Override
    public boolean isUnwrappingDisabled() {
        return false;
    }

    @Override
    public boolean isDefaultValueInjectable() {
        return false;
    }

    @Override
    public boolean isGetterInjectionEnabled() {
        return true;
    }

    @Override
    public boolean isCreatorInjectionEnabled() {
        return true;
    }

    @Override
    public boolean hasValueHandler() {
        return false;
    }

    @Override
    public JsonValueHandler<?> getValueHandler() {
        return null;
    }

    @Override
    public boolean hasValueHandlers() {
        return false;
    }

    @Override
    public Collection<JsonValueHandler<?>> getValueHandlers() {
        return Collections.emptyList();
    }

    @Override
    public boolean hasValueInjection() {
        return true;
    }

    @Override
    public boolean hasValueInjectionFunction() {
        return false;
    }

    @Override
    public JsonValueFunction getValueInjectionFunction() {
        return null;
    }

    @Override
    public boolean hasValueProcessor() {
        return false;
    }

    @Override
    public JsonValueProcessor getValueProcessor() {
        return null;
    }

    @Override
    public boolean hasValueProcessors() {
        return false;
    }

    @Override
    public Collection<JsonValueProcessor> getValueProcessors() {
        return Collections.emptyList();
    }

    @Override
    public boolean isValueInstantiatorOverridden() {
        return false;
    }

    @Override
    public JsonSerializer<?> getValueInstantiatorValueSerializer() {
        return null;
    }

    @Override
    public AnnotationIntrospector getAnnotationIntrospector() {
        return null;
    }

    @Override
    public BeanDescription getBeanDescription() {
        return null;
    }

    @Override
    public PropertyName getNameValuePair() {
        return null;
    }
}

class AnnotationMetaData {}