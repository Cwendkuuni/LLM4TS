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

@RunWith(MockitoJunitRunner.class)
public class TypeDeserializerBaseTest {

    @Mock
    private DeserializationContext deserializationContextMock;

    @Mock
    private JsonParser jsonParserMock;

    @InjectMocks
    private TypeDeserializerBase typeDeserializerBase;

    @Test
    public void test_getTypeIdResolver() {
        TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();
        assertNotNull(result);
    }

    @Test
    public void test_getDefaultImpl() {
        Class<?> result = typeDeserializerBase.getDefaultImpl();
        assertNull(result);
    }

    @Test
    public void test_forProperty() {
        BeanProperty prop = null;
        TypeDeserializer result = typeDeserializerBase.forProperty(prop);
        assertNotNull(result);
    }

    @Test
    public void test_getTypeInclusion() {
        TypeBase result = typeDeserializerBase.getTypeInclusion();
        assertNotNull(result);
    }

    @Test
    public void testBaseTypeName() {
        String result = typeDeserializerBase.baseTypeName();
        assertNotNull(result);
    }

    @Test
    public void test_getPropertyName() {
        String result = typeDeserializerBase.getPropertyName();
        assertNotNull(result);
    }

    @Test
    public void test_toString() {
        String result = typeDeserializerBase.toString();
        assertNotNull(result);
    }

    @Test
    public void test_deserializeWithNativeTypeId1() throws IOException {
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParserMock, deserializationContextMock);
        assertNotNull(result);
    }

    @Test
    public void test_deserializeWithNativeTypeId2() throws IOException {
        Object typeId = "test_typeId";
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParserMock, deserializationContextMock,typeId);
        assertNotNull(result);
    }

    @Test
    public void test_handleUnknownTypeId() throws IOException {
        String typeId = "test_typeId";
        JsonMappingException result = assertThrows(JsonMappingException.class, () -> {
            typeDeserializerBase._handleUnknownTypeId(deserializationContextMock, typeId, any(TypeIdResolver.class),
            any(JavaType.class));
        });
        assertNotNull(result);
    }
}