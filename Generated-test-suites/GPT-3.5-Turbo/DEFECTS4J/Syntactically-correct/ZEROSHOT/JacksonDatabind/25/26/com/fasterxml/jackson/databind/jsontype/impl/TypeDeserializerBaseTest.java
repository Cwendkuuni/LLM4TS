
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

/**
 * Base class for all standard Jackson {@link TypeDeserializer}s.
 */
public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        TypeIdResolver idResolver = new TypeIdResolver() {
            @Override
            public void init(JavaType javaType) {
            }

            @Override
            public String idFromValue(Object o) {
                return null;
            }

            @Override
            public String idFromValueAndType(Object o, Class<?> aClass) {
                return null;
            }

            @Override
            public String idFromBaseType() {
                return null;
            }

            @Override
            public String idFromValueAndType(Object o, JavaType javaType) {
                return null;
            }

            @Override
            public JavaType typeFromId(DatabindContext databindContext, String s) {
                return null;
            }

            @Override
            public JavaType typeFromId(DatabindContext databindContext, String s, Object o) {
                return null;
            }

            @Override
            public JsonTypeInfo.Id getMechanism() {
                return null;
            }
        };
        JavaType baseType = null;
        String typePropertyName = "";
        boolean typeIdVisible = false;
        Class<?> defaultImpl = null;

        typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        Assert.assertNull(baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = typeDeserializerBase.getPropertyName();
        Assert.assertEquals("", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver resolver = typeDeserializerBase.getTypeIdResolver();
        Assert.assertNotNull(resolver);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        Assert.assertNull(defaultImpl);
    }

    @Test
    public void testToString() {
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTest$1]";
        String result = typeDeserializerBase.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = "";

        JsonDeserializer<Object> deser = typeDeserializerBase._findDeserializer(ctxt, typeId);
        Assert.assertNull(deser);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = null;

        JsonDeserializer<Object> deser = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        Assert.assertNull(deser);
    }

    @Test(expected = com.fasterxml.jackson.databind.JsonMappingException.class)
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = null;
        String typeId = "";
        TypeIdResolver idResolver = null;
        JavaType baseType = null;

        JsonDeserializer<Object> deser = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
        Assert.assertNull(deser);
    }
}
