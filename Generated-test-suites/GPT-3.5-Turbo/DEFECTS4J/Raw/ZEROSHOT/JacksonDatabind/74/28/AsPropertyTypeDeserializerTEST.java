package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonToken;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AsPropertyTypeDeserializerTest {

    @Test
    public void testAsPropertyTypeDeserializerConstructor() {
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "testTypeProperty";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        As inclusion = As.PROPERTY;

        AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);

        assertEquals(bt, deserializer._baseType);
        assertEquals(idRes, deserializer._idResolver);
        assertEquals(typePropertyName, deserializer._typePropertyName);
        assertEquals(typeIdVisible, deserializer._typeIdVisible);
        assertEquals(defaultImpl, deserializer._defaultImpl);
        assertEquals(inclusion, deserializer._inclusion);
    }

    @Test
    public void testForProperty() {
        AsPropertyTypeDeserializer deserializer = mock(AsPropertyTypeDeserializer.class);
        BeanProperty property = mock(BeanProperty.class);

        TypeDeserializer result = deserializer.forProperty(property);

        assertTrue(result instanceof AsPropertyTypeDeserializer);
        assertNotEquals(deserializer, result);
    }

    @Test
    public void testGetTypeInclusion() {
        AsPropertyTypeDeserializer deserializer = mock(AsPropertyTypeDeserializer.class);
        when(deserializer.getTypeInclusion()).thenReturn(As.PROPERTY);

        As result = deserializer.getTypeInclusion();

        assertEquals(As.PROPERTY, result);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        AsPropertyTypeDeserializer deserializer = mockConstructor(AsPropertyTypeDeserializer.class).withArgs(JavaType.class, TypeIdResolver.class, String.class, boolean.class, JavaType.class, As.class).create(mock(JavaType.class), mock(TypeIdResolver.class), "testTypeProperty", true, mock(JavaType.class), As.PROPERTY);

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TokenBuffer tb = mock(TokenBuffer.class);
        JsonToken fieldToken = JsonToken.FIELD_NAME;
        JsonToken valueToken = mock(JsonToken.class);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        when(p.getCurrentToken()).thenReturn(JsonToken.START_OBJECT, fieldToken, valueToken, fieldToken, valueToken, fieldToken, valueToken, JsonToken.END_OBJECT);
        when(p.nextToken()).thenReturn(fieldToken, valueToken, fieldToken, valueToken, fieldToken, valueToken, JsonToken.END_OBJECT);
        when(p.getCurrentName()).thenReturn("name", "value1", "name", "value2", "typePropertyName", "typeId");
        when(p.getTypeId()).thenReturn("testTypeId");
        when(valueToken.isScalarValue()).thenReturn(true);
        when(valueToken.asString()).thenReturn("testValue");
        when(ctxt.findContextualValueDeserializer(any(), any())).thenReturn(valueDeserializer);
        when(valueDeserializer.deserialize(any(), any())).thenReturn("testResult");

        Object result = deserializer.deserializeTypedFromObject(p, ctxt);

        assertEquals("testResult", result);
        verify(p, times(1)).canReadTypeId();
        verify(p, times(1)).getTypeId();
        verify(deserializer, times(1))._deserializeWithNativeTypeId(any(), any(), any());
        verify(p, times(7)).getCurrentToken();
        verify(p, times(5)).nextToken();
        verify(p, times(2)).getCurrentName();
        verify(p, times(2)).skipChildren();
        verify(tb, times(2)).writeFieldName(any());
        verify(tb, times(2)).copyCurrentStructure(p);
        verify(deserializer, times(1))._deserializeTypedForId(p, ctxt, tb);
    }

    @Test
    public void testDeserializeTypedForId() throws IOException {
        AsPropertyTypeDeserializer deserializer = mockConstructor(AsPropertyTypeDeserializer.class).withArgs(JavaType.class, TypeIdResolver.class, String.class, boolean.class, JavaType.class, As.class).create(mock(JavaType.class), mock(TypeIdResolver.class), "testTypeProperty", true, mock(JavaType.class), As.PROPERTY);

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TokenBuffer tb = mock(TokenBuffer.class);
        when(p.getText()).thenReturn("testTypeId");
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(any(), any())).thenReturn(deser);
        when(deserializer._typeIdVisible).thenReturn(true);

        Object result = deserializer._deserializeTypedForId(p, ctxt, tb);

        assertEquals("testValue", result);
        verify(p, times(1)).getCurrentName();
        verify(tb, times(1)).writeFieldName(any());
        verify(tb, times(1)).writeString("testTypeId");
        verify(tb, times(1)).asParser(p);
        verify(p, times(1)).clearCurrentToken();
        verify(deser, times(1)).deserialize(any(), any());
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        AsPropertyTypeDeserializer deserializer = mockConstructor(AsPropertyTypeDeserializer.class).withArgs(JavaType.class, TypeIdResolver.class, String.class, boolean.class, JavaType.class, As.class).create(mock(JavaType.class), mock(TypeIdResolver.class), "testTypeProperty", true, mock(JavaType.class), As.PROPERTY);

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TokenBuffer tb = mock(TokenBuffer.class);
        JsonDeserializer<Object> deser = mockConstructor(StdDeserializer.class).create(mock(JavaType.class));
        JsonDeserializer<Object> resultDeser = mockConstructor(StdDeserializer.class).create(mock(JavaType.class));
        when(deserializer._findDefaultImplDeserializer(any())).thenReturn(deser);
        when(deser.deserialize(any(), any())).thenReturn("testResult");
        when(deserializer._baseType).thenReturn(mock(JavaType.class));
        when(p.getCurrentToken()).thenReturn(JsonToken.UNKNOWN);
        when(p.nextToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.FIELD_NAME);

        Object result = deserializer._deserializeTypedUsingDefaultImpl(p, ctxt, tb);

        assertEquals("testResult", result);
        verify(p, times(1)).getCurrentToken();
        verify(deserializer, times(1))._findDefaultImplDeserializer(any());
        verify(deser, times(1)).deserialize(any(), any());
        verify(p, times(1)).nextToken();
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        AsPropertyTypeDeserializer deserializer = mockConstructor(AsPropertyTypeDeserializer.class).withArgs(JavaType.class, TypeIdResolver.class, String.class, boolean.class, JavaType.class, As.class).create(mock(JavaType.class), mock(TypeIdResolver.class), "testTypeProperty", true, mock(JavaType.class), As.PROPERTY);

        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(p.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        when(p.nextToken()).thenReturn(JsonToken.START_ARRAY, JsonToken.FIELD_NAME);
        when(p.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);

        Object result = deserializer.deserializeTypedFromAny(p, ctxt);

        assertTrue(result instanceof AsPropertyTypeDeserializer);
        verify(deserializer, times(1)).deserializeTypedFromObject(p, ctxt);
        verify(deserializer, never()).deserializeTypedFromArray(p, ctxt);
    }

    private <T> T mockConstructor(final Class<T> c) {
        return new MockUnitBlock() {
            public void define() {
                mock(c);
            }
        }.getMock(c);
    }
}