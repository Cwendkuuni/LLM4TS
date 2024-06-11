package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Map;

public class TypeDeserializerBaseTest {
    private TypeDeserializerBase typeDeserializerBase;
    private JavaType baseType;
    private TypeIdResolver typeIdResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private JavaType defaultImpl;
    private BeanProperty property;

    @Before
    public void setUp() {
        baseType = Mockito.mock(JavaType.class);
        typeIdResolver = Mockito.mock(TypeIdResolver.class);
        typePropertyName = "typePropertyName";
        typeIdVisible = true;
        defaultImpl = Mockito.mock(JavaType.class);
        property = Mockito.mock(BeanProperty.class);
        typeDeserializerBase = new TypeDeserializerBase(baseType, typeIdResolver,
                typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                                                    TypeIdResolver idResolver, JavaType baseType)
                    throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testBaseType() {
        assertEquals(baseType, typeDeserializerBase._baseType);
    }

    @Test
    public void testTypeIdResolver() {
        assertEquals(typeIdResolver, typeDeserializerBase._idResolver);
    }

    @Test
    public void testTypePropertyName() {
        assertEquals(typePropertyName, typeDeserializerBase._typePropertyName);
    }

    @Test
    public void testTypeIdVisible() {
        assertEquals(typeIdVisible, typeDeserializerBase._typeIdVisible);
    }

    @Test
    public void testDefaultImpl() {
        assertEquals(defaultImpl, typeDeserializerBase._defaultImpl);
    }

    @Test
    public void testProperty() {
        assertNull(typeDeserializerBase._property);
    }

    @Test
    public void testGetPropertyname() {
        assertEquals(typePropertyName, typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(typeIdResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertNotNull(typeDeserializerBase.getDefaultImpl());
    }
}