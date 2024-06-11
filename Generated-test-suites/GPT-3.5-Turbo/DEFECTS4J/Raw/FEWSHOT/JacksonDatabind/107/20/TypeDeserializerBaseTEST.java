package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializationTestBase;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest extends DeserializationTestBase {
    private static class MockTypeIdResolver implements TypeIdResolver {
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
        public String idFromBaseType() {
            return null;
        }

        @Override
        public JavaType typeFromId(DatabindContext context, String id, Class<?> cls) {
            return null;
        }

        @Override
        public JsonTypeInfo.Id getMechanism() {
            return null;
        }
    }
    
    private static class MockDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }

    private static final JavaType baseType = JavaType.constructType(Object.class);
    private static final TypeIdResolver idResolver = new MockTypeIdResolver();
    private static final BeanProperty property = null;
    private static final JavaType defaultImpl = JavaType.constructType(Object.class);
    private static final String typePropertyName = "type";
    private static final boolean typeIdVisible = true;
    private final TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver,
            typePropertyName, typeIdVisible, defaultImpl) {
        private static final long serialVersionUID = 1L;

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public TypeCategory getTypeCategory() {
            return null;
        }

        @Override
        public DeserializerType getType() {
            return null;
        }

        @Override
        public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    };
    
    public void testBaseTypeName() {
        assertEquals("java.lang.Object", typeDeserializer.baseTypeName());
    }

    public void testGetPropertyName() {
        assertEquals("type", typeDeserializer.getPropertyName());
    }
    
    public void testGetTypeIdResolver() {
        assertEquals(idResolver, typeDeserializer.getTypeIdResolver());
    }

    public void testGetDefaultImpl() {
        assertEquals(Object.class, typeDeserializer.getDefaultImpl());
    }
    
    public void testBaseType() {
        assertEquals(baseType, typeDeserializer.baseType());
    }
    
    public void testFindDeserializer() throws IOException {
        DeserializationContext context = createContext();
        assertNull(typeDeserializer._findDeserializer(context, null));
        assertNull(typeDeserializer._findDeserializer(context, "test"));
        
        typeDeserializer._deserializers.put("test", new MockDeserializer());
        JsonDeserializer<Object> expectedDeserializer = typeDeserializer._deserializers.get("test");
        assertEquals(expectedDeserializer, typeDeserializer._findDeserializer(context, "test"));
    }
    
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext context = createJsonMapper().createContext(createParser("test"), false);
        assertNull(typeDeserializer._findDefaultImplDeserializer(context));
        typeDeserializer._defaultImplDeserializer = new MockDeserializer();
        JsonDeserializer<Object> expectedDeserializer = typeDeserializer._defaultImplDeserializer;
        assertEquals(expectedDeserializer, typeDeserializer._findDefaultImplDeserializer(context));
    }
    
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext context = createJsonMapper().createContext(createParser("test"), false);
        String typeId = "test";
        assertNull(typeDeserializer._handleUnknownTypeId(context, typeId));
    }
    
    public void testHandleMissingTypeId() throws IOException {
        DeserializationContext context = createJsonMapper().createContext(createParser("test"), false);
        String extraDesc = "extraDesc";
        assertNull(typeDeserializer._handleMissingTypeId(context, extraDesc));
    }
}