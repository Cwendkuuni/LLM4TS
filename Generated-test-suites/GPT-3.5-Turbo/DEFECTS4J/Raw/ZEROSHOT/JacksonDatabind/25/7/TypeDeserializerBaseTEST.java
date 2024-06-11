package com.fasterxml.jackson.databind.jsontype.impl;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TypeDeserializerBaseTest {
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        typeDeserializerBase = new TypeDeserializerBase(new JavaType(null), new TypeIdResolver() {
            @Override
            public void init(JavaType baseType) {

            }

            @Override
            public String idFromValue(Object value) {
                return null;
            }

            @Override
            public String idFromValueAndType(Object value, Class<?> suggestedType) {
                return null;
            }

            @Override
            public String idFromBaseType() {
                return null;
            }

            @Override
            public JavaType typeFromId(DatabindContext context, String id) throws IOException {
                return null;
            }

            @Override
            public String getDescForKnownTypeIds() {
                return null;
            }

            @Override
            public JsonTypeInfo.Id getMechanism() {
                return null;
            }
        }, "test", true, Object.class);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        Assert.assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        Assert.assertEquals("null", baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = typeDeserializerBase.getPropertyName();
        Assert.assertEquals("test", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
        Assert.assertNotNull(typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        Assert.assertEquals(Object.class, defaultImpl);
    }

    @Test
    public void testToString() {
        String toString = typeDeserializerBase.toString();
        Assert.assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]", toString);
    }

    @Test
    public void testForProperty() {
        BeanProperty property = null;
        TypeDeserializer forProperty = typeDeserializerBase.forProperty(property);
        Assert.assertNotNull(forProperty);
    }
    
    @Test
    public void testDeserializationWithDefaultProperties() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(parser, context);
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializationWithNativeTypeId() throws IOException {
        JsonParser parser = null;
        DeserializationContext context = null;
        Object typeId = null;
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(parser, context, typeId);
        Assert.assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializationWithUnknownTypeId() throws IOException {
        DeserializationContext context = null;
        String typeId = null;
        TypeIdResolver idResolver = null;
        JavaType baseType = null;
        typeDeserializerBase._handleUnknownTypeId(context, typeId, idResolver, baseType);
    }
}