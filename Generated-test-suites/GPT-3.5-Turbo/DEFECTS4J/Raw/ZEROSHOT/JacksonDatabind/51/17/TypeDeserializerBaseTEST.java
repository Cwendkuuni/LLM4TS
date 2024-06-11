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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {
    
    private TypeDeserializerBase typeDeserializerBase;
    
    @Before
    public void setUp() {
        TypeIdResolver idResolver = new TypeIdResolver() {
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
            public JavaType typeFromId(DatabindContext context, String id) {
                return null;
            }

            @Override
            public JsonTypeInfo.Id getMechanism() {
                return null;
            }
        };

        JavaType baseType = null;
        String typePropertyName = "";
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;

        typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName,
                typeIdVisible, defaultImpl) {

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
                return null;
            }
        };
    }
    
    @After
    public void tearDown() {
        typeDeserializerBase = null;
    }
    
    @Test
    public void testBaseTypeName() {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertNull(baseTypeName);
    }
    
    @Test
    public void testGetPropertyName() {
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals("", propertyName);
    }
    
    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver idResolver = typeDeserializerBase.getTypeIdResolver();
        assertNotNull(idResolver);
    }
    
    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertNull(defaultImpl);
    }
    
    @Test
    public void testToString() {
        String str = typeDeserializerBase.toString();
        assertNotNull(str);
    }
    
    @Test
    public void test_findDeserializer() {
        DeserializationContext ctxt = new DeserializationContext();
        DeserializationFeature feature = DeserializationFeature.FAIL_ON_INVALID_SUBTYPE;
        DeserializationConfig config = new DeserializationConfig();
        config = config.with(ctxt, feature);
        JsonParser jp = new JsonParser(null);
        try {
            typeDeserializerBase._findDeserializer(ctxt, "");
            typeDeserializerBase._findDeserializer(ctxt, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(jp);
    }
    
    @Test
    public void testFindDefaultImplDeserializer() {
        DeserializationContext ctxt = new DeserializationContext();
        try {
            typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNull(typeDeserializerBase._defaultImpl);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId() {
        JsonParser jp = new JsonParser(null);
        DeserializationContext ctxt = new DeserializationContext();
        try {
            typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNull(jp);
    }
    
    @Test
    public void testHandleUnknownTypeId() {
        DeserializationContext ctxt = new DeserializationContext();
        try {
            typeDeserializerBase._handleUnknownTypeId(ctxt, "", null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(ctxt);
    }
}