package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TypeDeserializerBaseTest {

    private static final String DEFAULT_IMPL_CLASS_NAME = "java.util.HashMap";
    private static final String TYPE_PROPERTY_NAME = "type";
    private static final boolean TYPE_ID_VISIBLE = true;
    private static final JavaType BASE_TYPE = new Jdk8MapTypeFactory().constructType(Map.class, String.class, Integer.class);
    private static final TypeIdResolver idResolver = new Jdk8TypeIdResolver();
    private static final Converter<String, JavaType> idResolverInstance = new StandardConverter(idResolver);

    private ObjectMapper objectMapper;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(BASE_TYPE, idResolver, TYPE_PROPERTY_NAME, TYPE_ID_VISIBLE,
                Class.forName(DEFAULT_IMPL_CLASS_NAME));
        typeDeserializerBase = new TypeDeserializerBase(typeDeserializer, null);
    }

    @Test
    public void testBaseTypeName() throws Exception {
        assertEquals(BASE_TYPE.getRawClass().getName(), typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws Exception {
        assertEquals(TYPE_PROPERTY_NAME, typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() throws Exception {
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:class java.util.Map<java.lang.String, java.lang.Integer>; id-resolver:com.fasterxml.jackson.databind.Jdk8TypeIdResolver]";
        assertEquals(expected, typeDeserializerBase.toString());
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty property = new BeanProperty("testProperty", String.class, null);
        TypeDeserializerBase newTypeDeserializerBase = typeDeserializerBase.forProperty(property);
        assertNotSame(typeDeserializerBase, newTypeDeserializerBase);
        assertEquals(BASE_TYPE, newTypeDeserializerBase._baseType);
        assertSame(idResolver, newTypeDeserializerBase._idResolver);
        assertEquals(TYPE_PROPERTY_NAME, newTypeDeserializerBase._typePropertyName);
        assertEquals(TYPE_ID_VISIBLE, newTypeDeserializerBase._typeIdVisible);
        assertNotNull(newTypeDeserializerBase._property);
    }

    @Test
    public void testFindDeserializer() throws Exception {
        ObjectCodec objectCodec = objectMapper.getFactory().getCodec();
        JsonParser jsonParser = objectCodec.getParser();
        jsonParser.nextToken();
        String typeId = "HashMapStringInteger";
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext(), typeId);
        assertEquals(HashMap.class, result.getClass());

        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), typeId);
        assertNotNull(deserializer);
        assertEquals(HashMap.class, deserializer.getParameterizedType());

        result = deserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        assertEquals(HashMap.class, result.getClass());
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(objectMapper.getDeserializationContext());
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof NullifyingDeserializer);
    }

    @Test(expected = IOException. class)
    public void testHandleUnknownTypeId() throws Exception {
        String unknownTypeId = "UnknownTypeId";
        typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), unknownTypeId, idResolver, BASE_TYPE);
    }
}