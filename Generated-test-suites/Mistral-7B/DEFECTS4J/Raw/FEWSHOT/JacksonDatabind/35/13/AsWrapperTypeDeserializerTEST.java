package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;
import com.fasterxml.jackson.databind.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeIdResolver TYPE_ID_RESOLVER = new TypeIdResolverBase();

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        final JavaType baseType = MAPPER.getTypeFactory().constructType(String.class);
        final JavaType wrapperType = MAPPER.getTypeFactory().constructType(Map.class.getName() + "<String,?" + baseType.getClassName() + ">");

        final String typeId = "myTypeId";
        final Object expected = "expectedValue";

        final TypeIdResolver typeIdResolverMock = mock(TypeIdResolver.class);
        when(typeIdResolverMock.resolveTypeId(any(DeserializationContext.class), anyString(), eq(String.class)))
                .thenReturn(typeId);

        final JsonDeserializer<Object> deserializerMock = mock(JsonDeserializer.class);
        when(deserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(expected);

        final AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolverMock, "typePropertyName", true, String.class);

        final JsonParser parser = createParserWithWrapperObject(typeId, expected);

        final Object actual = deserializer.deserializeTypedFromObject(parser, MAPPER.getDeserializationContext());

        assertEquals(expected, actual);
        verify(deserializerMock).deserialize(parser, MAPPER.getDeserializationContext());
        verifyNoMoreInteractions(typeIdResolverMock);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        final JavaType baseType = MAPPER.getTypeFactory().constructType(String.class);
        final JavaType wrapperType = MAPPER.getTypeFactory().constructType(Map.class.getName() + "<String,?" + baseType.getClassName() + ">");

        final String typeId = "myTypeId";
        final Object expected = "expectedValue";

        final TypeIdResolver typeIdResolverMock = mock(TypeIdResolver.class);
        when(typeIdResolverMock.resolveTypeId(any(DeserializationContext.class), anyString(), eq(String[].class)))
                .thenReturn(new String[]{typeId});

        final JsonDeserializer<Object> deserializerMock = mock(JsonDeserializer.class);
        when(deserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(expected);

        final AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolverMock, "typePropertyName", true, String.class);

        final JsonParser parser = createParserWithWrapperArray(new String[]{typeId}, expected);

        final Object actual = deserializer.deserializeTypedFromArray(parser, MAPPER.getDeserializationContext());

        assertEquals(expected, actual);
        verify(deserializerMock).deserialize(parser, MAPPER.getDeserializationContext());
        verifyNoMoreInteractions(typeIdResolverMock);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        final JavaType baseType = MAPPER.getTypeFactory().constructType(String.class);
        final JavaType wrapperType = MAPPER.getTypeFactory().constructType(Map.class.getName() + "<String,?" + baseType.getClassName() + ">");

        final String typeId = "myTypeId";
        final Object expected = "expectedValue";

        final TypeIdResolver typeIdResolverMock = mock(TypeIdResolver.class);
        when(typeIdResolverMock.resolveTypeId(any(DeserializationContext.class), eq(JsonLocation.class), eq("")))
                .thenReturn(typeId);

        final JsonDeserializer<Object> deserializerMock = mock(JsonDeserializer.class);
        when(deserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(expected);

        final AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolverMock, "typePropertyName", true, String.class);

        final JsonParser parser = createParserWithWrapperScalar(typeId, expected);

        final Object actual = deserializer.deserializeTypedFromScalar(parser, MAPPER.getDeserializationContext());

        assertEquals(expected, actual);
        verify(deserializerMock).deserialize(parser, MAPPER.getDeserializationContext());
        verifyNoMoreInteractions(typeIdResolverMock);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        final JavaType baseType = MAPPER.getTypeFactory().constructType(String.class);
        final JavaType wrapperType = MAPPER.getTypeFactory().constructType(Map.class.getName() + "<String,?" + baseType.getClassName() + ">");

        final String typeId = "myTypeId";
        final Object expected = "expectedValue";

        final TypeIdResolver typeIdResolverMock = mock(TypeIdResolver.class);
        when(typeIdResolverMock.resolveTypeId(any(DeserializationContext.class), anyString(), any(TypeReference.class)))
                .thenReturn(typeId);

        final JsonDeserializer<Object> deserializerMock = mock(JsonDeserializer.class);
        when(deserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(expected);

        final AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolverMock, "typePropertyName", true, String.class);

        final JsonParser parser = createParserWithWrapperAny(new TypeReference<Object>() {});
        parser.setValue(ObjectMapperUtil.convertValue(expected, new TypeReference<Object>() {}));

        final Object actual = deserializer.deserializeTypedFromAny(parser, MAPPER.getDeserializationContext());

        assertEquals(expected, actual);
        verify(deserializerMock).deserialize(parser, MAPPER.getDeserializationContext());
        verifyNoMoreInteractions(typeIdResolverMock);
    }

    private JsonParser createParserWithWrapperObject(String typeId, Object expected) throws IOException {
        final JsonParser parser = MAPPER.getFactory().createParser("{\"type\":\"" + typeId + "\", \"value\":\"expectedValue\"}");
        parser.nextToken();
        return parser;
    }

    private JsonParser createParserWithWrapperArray(String[] typeIds, Object expected) throws IOException {
        final JsonParser parser = MAPPER.getFactory().createParser("[" + ObjectMapperUtil.valueToQuotedString(typeIds) + "]");
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.setValue(ObjectMapperUtil.convertValue(expected, MAPPER.getTypeFactory().constructType(expected.getClass().getGenericSuperclass())));
        return parser;
    }

    private JsonParser createParserWithWrapperScalar(String typeId, Object expected) throws IOException {
        final JsonParser parser = MAPPER.getFactory().createParser("\"" + typeId + "\"");
        parser.nextToken();
        parser.setValue(ObjectMapperUtil.convertValue(expected, MAPPER.getTypeFactory().constructType(expected.getClass().getGenericSuperclass())));
        return parser;
    }

    private JsonParser createParserWithWrapperAny(TypeReference<?> typeReference) throws IOException {
        final JsonParser parser = MAPPER.getFactory().createParser("{}");
        parser.nextToken();
        parser.nextToken();
        parser.setValue(ObjectMapperUtil.convertValue(typeReference.getValue(), typeReference));
        return parser;
    }
}