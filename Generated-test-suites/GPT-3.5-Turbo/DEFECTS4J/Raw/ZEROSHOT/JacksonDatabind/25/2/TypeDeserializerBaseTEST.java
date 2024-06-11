package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        JavaType baseType = TypeFactory.defaultInstance().constructType(Object.class);
        TypeIdResolver idResolver = new SomeTypeIdResolver();
        String typePropertyName = "type";
        boolean typeIdVisible = true;
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
        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }
  
    @Test
    public void testGetPropertyName() {
        assertEquals("type", typeDeserializerBase.getPropertyName());
    }
  
    @Test
    public void testGetTypeIdResolver() {
        assertNotNull(typeDeserializerBase.getTypeIdResolver());
    }
  
    @Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }
  
    private static class SomeTypeIdResolver implements TypeIdResolver {
  
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
        public ResolvedType typeFromId(DatabindContext context, String id) throws IOException {
            return null;
        }
  
        @Override
        public String getDescForKnownTypeIds() {
            return null;
        }
  
        @Override
        public Id getMechanism() {
            return null;
        }
    }
}