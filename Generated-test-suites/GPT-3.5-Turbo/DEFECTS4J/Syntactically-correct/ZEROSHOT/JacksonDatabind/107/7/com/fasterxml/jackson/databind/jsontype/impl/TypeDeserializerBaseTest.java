package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        JavaType baseType = JavaType.builder().build();
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
            public String idFromBaseType() {
                return null;
            }

            @Override
            public JavaType typeFromId(DatabindContext context, String id) {
                return null;
            }

            @Override
            public String getDescForKnownTypeIds() {
                return null;
            }
        };
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = JavaType.builder().build();
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
        };
    }

    @Test
    public void testBaseTypeName() {
        typeDeserializerBase.baseTypeName();
    }

    @Test
    public void testGetPropertyName() {
        typeDeserializerBase.getPropertyName();
    }

    @Test
    public void testGetTypeIdResolver() {
        typeDeserializerBase.getTypeIdResolver();
    }

    @Test
    public void testGetDefaultImpl() {
        typeDeserializerBase.getDefaultImpl();
    }

    @Test
    public void testBaseType() {
        typeDeserializerBase.baseType();
    }

    @Test
    public void testToString() {
        typeDeserializerBase.toString();
    }

    @Test
    public void testFindDeserializer() {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = new DeserializationContext(mapper.getFactory());
        JsonDeserializer<Object> deser = typeDeserializerBase._findDeserializer(ctxt, "typeId");
        assertNull(deser);
    }

    @Test
    public void testFindDefaultImplDeserializer() {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = new DeserializationContext(mapper.getFactory());
        try {
            JsonDeserializer<Object> deser = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
            assertNotNull(deser);
            assertTrue(deser instanceof NullifyingDeserializer);
        } catch (IOException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testHandleUnknownTypeId() {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = new DeserializationContext(mapper.getFactory());
        String typeId = "unknown";
        try {
            typeDeserializerBase._handleUnknownTypeId(ctxt, typeId);
        } catch (IOException e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testHandleMissingTypeId() {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = new DeserializationContext(mapper.getFactory());
        String extraDesc = "missing";
        try {
            typeDeserializerBase._handleMissingTypeId(ctxt, extraDesc);
        } catch (IOException e) {
            fail("Should not throw an exception");
        }
    }
}