package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;

@RunWith(MockitoJUnitRunner.class)
public class TypeDeserializerBaseTest {

    private TypeDeserializerBase deserializerBase;

    @Mock
    private DeserializationContext context;

    @Mock
    private JavaType baseType;

    @Mock
    private TypeIdResolver idResolver;

    @Mock
    private BeanProperty property;

    @Before
    public void setup() {
        deserializerBase = new TypeDeserializerBaseImpl(baseType, idResolver, "type", true, Object.class);
    }

    @Test
    public void testForProperty() {
        TypeDeserializer result = deserializerBase.forProperty(property);
        assertNotNull(result);
        assertEquals(TypeDeserializerBaseImpl.class, result.getClass());
        assertEquals(property, ((TypeDeserializerBaseImpl) result).getProperty());
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As result = deserializerBase.getTypeInclusion();
        assertNotNull(result);
        assertEquals(JsonTypeInfo.As.PROPERTY, result);
    }

    @Test
    public void testBaseTypeName() {
        String result = deserializerBase.baseTypeName();
        assertNotNull(result);
        assertEquals(Object.class.getName(), result);
    }

    @Test
    public void testGetPropertyName() {
        String result = deserializerBase.getPropertyName();
        assertEquals("type", result);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver result = deserializerBase.getTypeIdResolver();
        assertNotNull(result);
        assertEquals(idResolver, result);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> result = deserializerBase.getDefaultImpl();
        assertNotNull(result);
        assertEquals(Object.class, result);
    }

    @Test
    public void testToString() {
        String result = deserializerBase.toString();
        assertNotNull(result);
        assertTrue(result.contains(TypeDeserializerBaseImpl.class.getName()));
        assertTrue(result.contains(baseType.toString()));
        assertTrue(result.contains(idResolver.toString()));
    }

    @Test
    public void test_findDeserializerForDeserializationWithKnownTypeId() throws IOException {
        String typeId = "type_id";
        JsonParser jsonParserMocked = mock(JsonParser.class);
        JsonDeserializer<Object> jsonDeserializerMocked = mock(JsonDeserializer.class);
        
        Map<String, JsonDeserializer<Object>> deserializersMap = new HashMap<>();
        deserializersMap.put(typeId, jsonDeserializerMocked);
        
        TypeDeserializerBaseImpl typeDeserializer = new TypeDeserializerBaseImpl(baseType, idResolver, "type", true, Object.class);
        typeDeserializer.setDeserializers(deserializersMap);

        when(idResolver.typeFromId(any(), any())).thenReturn(baseType);
        when(jsonParserMocked.getTypeId()).thenReturn(typeId);
        when(jsonDeserializerMocked.deserialize(jsonParserMocked, context)).thenReturn("result");

        Object result = typeDeserializer._findDeserializer(jsonParserMocked, context, typeId);

        assertNotNull(result);
        assertEquals("result", result);
    }

    @Test(expected = IOException.class)
    public void test_findDeserializerForDeserializationWithUnknownTypeId() throws IOException {
        String typeId = "type_id";
        JsonParser jsonParserMocked = mock(JsonParser.class);
        
        TypeDeserializerBaseImpl typeDeserializer = new TypeDeserializerBaseImpl(baseType, idResolver, "type", true, Object.class);

        when(idResolver.typeFromId(any(), any())).thenReturn(null);

        typeDeserializer._findDeserializer(jsonParserMocked, context, typeId);
    }

    private static class TypeDeserializerBaseImpl extends TypeDeserializerBase {

        private static final long serialVersionUID = 1;

        private Map<String, JsonDeserializer<Object>> _deserializers;
        
        private BeanProperty _property;

        public TypeDeserializerBaseImpl(JavaType baseType, TypeIdResolver idRes, String typePropertyName,
                                        boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        public void setDeserializers(Map<String, JsonDeserializer<Object>> deserializers) {
            _deserializers = deserializers;
        }

        public void setProperty(BeanProperty property) {
            _property = property;
        }

        public BeanProperty getProperty() {
            return _property;
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            TypeDeserializerBaseImpl result = new TypeDeserializerBaseImpl(_baseType, _idResolver, _typePropertyName,
                    _typeIdVisible, getDefaultImpl());
            result.setDeserializers(_deserializers);
            result.setProperty(prop);
            return result;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.PROPERTY;
        }

        @Override
        protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
                throws IOException {
            return _deserializers.get(typeId);
        }

        @Override
        protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
            return null;
        }

        @Override
        protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                                                                 TypeIdResolver idResolver, JavaType baseType) throws IOException {
            throw new IOException("Unknown type");
        }
    }
}