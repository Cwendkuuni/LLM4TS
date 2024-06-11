package com.example.junit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

@RunWith(MockitoJUnitRunner.class)
public class TypeDeserializerBaseTest {

    @Mock
    private DeserializationContext context;

    @Mock
    private JsonParser parser;

    @Mock
    private TypeIdResolver idResolver;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private TypeDeserializerBase typeDeserializerBase;

    @Test
    public void testForProperty() throws IOException {
        BeanProperty beanProperty = new BeanProperty("name", String.class);
        TypeDeserializer<Object> result = typeDeserializerBase.forProperty(beanProperty);
        assertNotNull(result);
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As result = typeDeserializerBase.getTypeInclusion();
        assertNotNull(result);
    }

    @Test
    public void testBaseTypeName() {
        String result = typeDeserializerBase.baseTypeName();
        assertNotNull(result);
    }

    @Test
    public void testGetPropertyName() {
        String result = typeDeserializerBase.getPropertyName();
        assertNull(result);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver result = typeDeserializerBase.getTypeIdResolver();
        assertNotNull(result);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> result = typeDeserializerBase.getDefaultImpl();
        assertNull(result);
    }

    @Test
    public void testToString() {
        String result = typeDeserializerBase.toString();
        assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindDeserializerNullContext() throws IOException {
        String typeId = null;
        typeDeserializerBase._findDeserializer(null, typeId);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFindDeserializerNullTypeId() throws IOException {
        typeDeserializerBase._findDeserializer(context, null);
    }

    @Test
    public void testFindDeserializerEmptyMap() throws IOException {
        typeDeserializerBase._deserializers = new ConcurrentHashMap<>();
        String typeId = "testTypeId";
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(context, typeId);
        assertNull(result);
    }

    @Test
    public void testFindDeserializerExistingMap() throws IOException {
        typeDeserializerBase._deserializers = new ConcurrentHashMap<>();
        String typeId = "testTypeId";
        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        typeDeserializerBase._deserializers.put(typeId, deserializer);
        JsonDeserializer<Object> result = typeDeserializerBase._findDeserializer(context, typeId);
        assertSame(deserializer, result);
    }

    @Test
    public void testFindDefaultImplDeserializerNullContext() throws IOException {
        typeDeserializerBase._defaultImplDeserializer = null;
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(context));
    }

    @Test
    public void testFindDefaultImplDeserializerNullDefaultImpl() throws IOException {
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(context));
    }

    @Test
    public void testFindDefaultImplDeserializerExistingDefaultImplDeserializer() throws IOException {
        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        typeDeserializerBase._defaultImplDeserializer = deserializer;
        assertSame(deserializer, typeDeserializerBase._findDefaultImplDeserializer(context));
    }

    @Test
    public void testHandleUnknownTypeIdNullContext() throws IOException {
        JavaType result = typeDeserializerBase._handleUnknownTypeId(null, "testTypeId", idResolver, null);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdNullIdResolver() throws IOException {
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, null, null, null);
        assertNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHandleUnknownTypeIdNullBaseType() throws IOException {
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, "testTypeId", idResolver, null);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdEmptyStringTypeId() throws IOException {
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, "", idResolver, null);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdNotStaticTypeIds() throws IOException {
        typeDeserializerBase._idResolver = idResolver;
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, "testTypeId", idResolver, null);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdKnownTypeIds() throws IOException {
        Convert converter = new Converter.Std() {};
        idResolver = new TypeIdResolverBase(converter) {
            @Override
            public String getDescForKnownTypeIds() {
                return "known type ids are statically known";
            }
        };
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, "testTypeId", idResolver, null);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdUnknownType() throws IOException {
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, "testTypeId", idResolver, null);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdUnknownTypeWithDefaultImpl() throws IOException {
        typeDeserializerBase._idResolver = idResolver;
        typeDeserializerBase._defaultImpl = new JavaType(Object.class);
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, "testTypeId", idResolver, null);
        assertNull(result);
    }

    @Test
    public void testHandleUnknownTypeIdUnknownTypeWithDeserializer() throws IOException {
        typeDeserializerBase._idResolver = idResolver;
        JsonDeserializer<Object> deserializer = new JsonDeserializer<Object>() {};
        typeDeserializerBase._deserializers = new ConcurrentHashMap<>();
        typeDeserializerBase._deserializers.put("testTypeId", deserializer);
        JavaType result = typeDeserializerBase._handleUnknownTypeId(context, "testTypeId", idResolver, null);
        assertSame(deserializer, result);
    }
}