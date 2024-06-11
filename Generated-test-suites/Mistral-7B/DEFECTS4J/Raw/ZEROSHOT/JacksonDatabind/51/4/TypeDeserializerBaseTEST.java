package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;

/**
 * Test class for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        TypeIdResolver typeIdResolver = new TypeIdResolverBase();
        JavaType baseType = SimpleType.construct(Integer.class, null);
        typeDeserializerBase = new TypeDeserializerBase(baseType, typeIdResolver, "", false, null);
    }

    @Test
    public void testForProperty() throws IOException {
        BeanProperty beanProperty = new BeanProperty("id", Integer.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(beanProperty);
        assertNotNull(typeDeserializer);
    }

    @Test(expected = NullPointerException.class)
    public void testForPropertyNullProperty() {
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(null);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertEquals(JsonTypeInfo.As.WRAP_INCLUDE, typeInclusion);
    }

    @Test(expected = NullPointerException.class)
    public void testGetTypeIdResolverNull() {
        TypeDeserializerBase typeDeserializerBase2 = new TypeDeserializerBase(null, null, "", false, null);
    }

    @Test
    public void testGetBaseTypeName() {
        assertEquals("class java.lang.Integer", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testToString() {
        String expected = "[class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;"
                + " base-type:class java.lang.Integer; id-resolver:com.fasterxml.jackson.databind.TypeIdResolverBase()]";
        assertEquals(expected, typeDeserializerBase.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testFindDeserializerNullContext() throws IOException {
        typeDeserializerBase._deserializers = null;
        _findDeserializer(null, "typeId");
    }

    @Test(expected = NullPointerException.class)
    public void testFindDeserializerNullTypeId() throws IOException {
        _findDeserializer(new DeserializationContext(new ObjectMapper()), null);
    }

    @Test
    public void testFindDeserializerTypeIdExists() throws IOException {
        JsonDeserializer<Object> jsonDeserializer = new StdDeserializer<>(String.class);
        typeDeserializerBase._deserializers.put("typeId", jsonDeserializer);
        assertEquals(jsonDeserializer, typeDeserializerBase._findDeserializer(new DeserializationContext(new ObjectMapper()), "typeId"));
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerTypeIdMissing() throws IOException {
        _findDeserializer(new DeserializationContext(new ObjectMapper()), "missingTypeId");
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JsonDeserializer<Object> jsonDeserializer = new StdDeserializer<>(String.class);
        typeDeserializerBase._defaultImplDeserializer = jsonDeserializer;
        assertEquals(jsonDeserializer, typeDeserializerBase._findDefaultImplDeserializer(new DeserializationContext(new ObjectMapper())));
    }

    @Test(expected = NullPointerException.class)
    public void testFindDefaultImplDeserialatorNull() throws IOException {
        typeDeserializerBase._defaultImplDeserialator = null;
        _findDefaultImplDeserialator(new DeserializationContext(new ObjectMapper()));
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullTypeId() throws IOException {
        _handleUnknownTypeId(new DeserializationContext(new ObjectMapper()), null, new TypeIdResolverBase(), null);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullIdResolver() throws IOException {
        _handleUnknownTypeId(new DeserializationContext(new ObjectMapper()), "typeId", null, null);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullBaseType() throws IOException {
        _handleUnknownTypeId(new DeserializationContext(new ObjectMapper()), "typeId", new TypeIdResolverBase(), null);
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdNullDescription() throws IOException {
        _handleUnknownTypeId(new DeserializationContext(new ObjectMapper()), "typeId", new TypeIdResolverBase(), new JavaType(Integer.class));
    }

    @Test
    public void testHandleUnknownTypeIdKnownTypeIds() throws IOException {
        TypeIdResolverBase typeIdResolverBase = new TypeIdResolverBase();
        typeIdResolverBase.setKnownTypeIds(new String[]{"typeId1", "typeId2"});
        _handleUnknownTypeId(new DeserializationContext(new ObjectMapper()), "unknownTypeId", typeIdResolverBase, new JavaType(Integer.class));
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdUnknownTypeId() throws IOException {
        _handleUnknownTypeId(new DeserializationContext(new ObjectMapper()), "unknownTypeId", new TypeIdResolverBase(), new JavaType(Integer.class));
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"typeId\":\"Integer\"}");
        Object object = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, new DeserializationContext(objectMapper));
        assertEquals(Integer.class, object.getClass());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdNullContext() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"typeId\":\"Integer\"}");
        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, null);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdNullTypeId() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{}");
        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, new DeserializationContext(objectMapper));
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdUnknownTypeId() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"typeId\":\"UnknownType\"}");
        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, new DeserializationContext(objectMapper));
    }

    private void _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
        typeDeserializerBase._findDeserializer(ctxt, typeId);
    }

    private JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
        return typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
    }
}