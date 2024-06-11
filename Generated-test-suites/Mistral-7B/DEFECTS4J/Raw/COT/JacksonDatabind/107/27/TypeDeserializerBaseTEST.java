package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.core.DefaultParserConfig;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

@RunWith(MockitoJUnitRunner.class)
public class TypeDeserializerBaseTest {

    @Mock
    private TypeIdResolver idResolver;

    @Mock
    private BeanProperty property;

    @Mock
    private DeserializationContext context;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private JsonFactory jsonFactory;

    @Mock
    private JsonParser jsonParser;

    @InjectMocks
    private TypeDeserializerBase typeDeserializerBase;

    private ObjectMapper mapper;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        mapper.setFeature(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        mapper.setFactory(jsonFactory);
        mapper.setParserFactory(new DefaultParserFactory());
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setTypeFactory(new DefaultTypeFactory());
        mapper.setDefaultTyping(JsonTypeInfo.As.WRAPPER_ARRAY);
        mapper.setDefaultPropertyInclusion(JsonTypeInfo.As.ALWAYS);
    }

    @Test
    public void testTypeDeserializerBaseConstructor() {
        JavaType baseType = new TypeReference<String>("java.lang.String") {}.getType();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver,
                "typePropertyName", true, null);
        assertNotNull(typeDeserializer);
    }

    @Test
    public void testTypeDeserializerBaseForProperty() {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(typeDeserializerBase, property);
        assertNotNull(typeDeserializer);
        assertEquals("typePropertyName", typeDeserializer.getPropertyName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeDeserializerBaseForPropertyNullProperty() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(typeDeserializerBase, null);
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        JavaType baseType = new TypeReference<String>("java.lang.String") {}.getType();
        assertEquals("java.lang.String", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testToString() {
        JavaType baseType = new TypeReference<String>("java.lang.String") {}.getType();
        String typePropertyName = "typePropertyName";
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver,
                typePropertyName, true, null);
        String expected = "[class com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:java.lang.String; id-resolver:com.fasterxml.jackson.databind.TypeIdResolver@...";
        assertEquals(expected, typeDeserializer.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testForPropertyNullBaseType() {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(null, idResolver, "typePropertyName",
                true, null);
        typeDeserializer.forProperty(property);
    }

    @Test(expected = IllegalStateException.class)
    public void testForPropertyNullIdResolver() {
        JavaType baseType = new TypeReference<String>("java.lang.String") {}.getType();
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, null, "typePropertyName",
                true, null);
        typeDeserializer.forProperty(property);
    }

    @Test(expected = IllegalStateException.class)
    public void testForPropertyNullProperty() {
        JavaType baseType = new TypeReference<String>("java.lang.String") {}.getType();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, null, true, null);
        typeDeserializer.forProperty(null);
    }

    @Test
    public void testFindDeserializerNullContext() {
        String typeId = "testTypeId";
        JsonDeserializer<Object> expectedDeserializer = mock(JsonDeserializer.class);
        when(idResolver.typeFromId(context, typeId)).thenReturn(null);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, typeId);
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializerNullTypeId() {
        JsonDeserializer<Object> expectedDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, null);
        assertNull(deserializer);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        String typeId = "testTypeId";
        JavaType type = new TypeReference<String>("java.lang.String") {}.getType();
        JsonDeserializer<Object> expectedDeserializer = mock(JsonDeserializer.class);
        when(idResolver.typeFromId(context, typeId)).thenReturn(type);
        when(context.findContextualValueDeserializer(type, property)).thenReturn(expectedDeserializer);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, typeId);
        assertEquals(expectedDeserializer, deserializer);
    }

    @Test(expected = NullPointerException.class)
    public void testFindDeserializerNullType() {
        String typeId = "testTypeId";
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(context, typeId);
    }

    @Test
    public void testFindDefaultImplDeserializerNullContext() throws IOException {
        JsonDeserializer<Object> expectedDeserializer = mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(context.getDefaultValueInstance(), property))
                .thenReturn(expectedDeserializer);
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertEquals(expectedDeserializer, deserializer);
    }

    @Test(expected = NullPointerException.class)
    public void testFindDefaultImplDeserializerNullContextDefaultImpl() {
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
    }

    @Test
    public void testHandleUnknownTypeIdNullContext() throws IOException {
        String extraDesc = "Unknown type id";
        JavaType baseType = new TypeReference<String>("java.lang.String") {}.getType();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, "typePropertyName",
                true, null);
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        when(idResolver.getTypeIdResolver()).thenReturn(typeIdResolver);
        when(typeIdResolver.getTypeId(context, baseType)).thenReturn(null);
        Object result = typeDeserializer._handleUnknownTypeId(context, extraDesc);
        assertNull(result);
    }

    @Test
    public void testHandleMissingTypeIdNullContext() throws IOException {
        String extraDesc = "Missing type id";
        JavaType baseType = new TypeReference<String>("java.lang.String") {}.getType();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, "typePropertyName",
                true, null);
        when(idResolver.getTypeIdResolver()).thenReturn(null);
        Object result = typeDeserializer._handleMissingTypeId(context, extraDesc);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleMissingTypeIdWithContext() throws IOException {
        String extraDesc = "Missing type id";
        JavaType baseType = new TypeReference<String>("java.lang.String") {}.getType();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, "typePropertyName",
                true, null);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(context.getConfig()).thenReturn(objectMapper.getConfig());
        when(objectMapper.getFactory()).thenReturn(jsonFactory);
        when(jsonFactory.createParser(new ByteArrayInputStream("{}".getBytes()))).thenReturn(jsonParser);
        when(jsonParser.nextValue()).thenReturn(null);
        Object result = typeDeserializer._handleMissingTypeId(context, extraDesc);
        fail("Unexpected result: " + result);
    }

    @Test
    public void testHandleMissingTypeIdWithTypeId() throws IOException {
        String extraDesc = "Missing type id";
        JavaType baseType = new TypeReference<String>("java.lang.String") {}.getType();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBase(baseType, idResolver, "typePropertyName",
                true, null);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(context.getConfig()).thenReturn(objectMapper.getConfig());
        when(objectMapper.getFactory()).thenReturn(jsonFactory);
        when(jsonFactory.createParser(new ByteArrayInputStream("{}".getBytes()))).thenReturn(jsonParser);
        when(jsonParser.nextValue()).thenReturn(null);
        when(jsonParser.getText()).thenReturn("testTypeId");
        Object result = typeDeserializer._handleMissingTypeId(context, extraDesc);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdNullContext() throws IOException {
        String json = "{}";
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(context.getConfig()).thenReturn(objectMapper.getConfig());
        when(objectMapper.getFactory()).thenReturn(jsonFactory);
        when(jsonFactory.createParser(new ByteArrayInputStream(json.getBytes()))).thenReturn(jsonParser);
        when(jsonParser.nextValue()).thenReturn(null);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeIdNullContextTypeId() throws IOException {
        String json = "{}";
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(context.getConfig()).thenReturn(objectMapper.getConfig());
        when(objectMapper.getFactory()).thenReturn(jsonFactory);
        when(jsonFactory.createParser(new ByteArrayInputStream(json.getBytes()))).thenReturn(jsonParser);
        when(jsonParser.nextValue()).thenReturn(null);
        when(jsonParser.getText()).thenReturn("testTypeId");
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, null);
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        String json = "{\"@type\":\"testTypeId\"}";
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(context.getConfig()).thenReturn(objectMapper.getConfig());
        when(objectMapper.getFactory()).thenReturn(jsonFactory);
        when(jsonFactory.createParser(new ByteArrayInputStream(json.getBytes()))).thenReturn(jsonParser);
        when(jsonParser.nextValue()).thenReturn(null);
        when(jsonParser.getText()).thenReturn("testTypeId");
        String expectedTypeId = "testTypeId";
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, expectedTypeId);
        assertEquals(expectedTypeId, result.getClass().getName());
    }
}