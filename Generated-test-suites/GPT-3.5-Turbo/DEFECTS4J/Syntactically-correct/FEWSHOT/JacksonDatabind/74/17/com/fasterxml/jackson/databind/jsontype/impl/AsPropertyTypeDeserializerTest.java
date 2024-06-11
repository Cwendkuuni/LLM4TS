package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Iterator;

public class AsPropertyTypeDeserializerTest {
    private AsPropertyTypeDeserializer asPropertyTypeDeserializer;

    @Mock
    private JavaType javaType;
    @Mock
    private TypeIdResolver typeIdResolver;
    @Mock
    private JsonParser jsonParser;
    @Mock
    private DeserializationContext deserializationContext;
    @Mock
    private TokenBuffer tokenBuffer;
    @Mock
    private JsonDeserializer<Object> jsonDeserializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(
                javaType,
                typeIdResolver,
                "typePropertyName",
                true,
                javaType,
                As.PROPERTY
        );
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(As.PROPERTY, asPropertyTypeDeserializer.getTypeInclusion());
    }

    @Test
    public void testDeserializeTypedUsingDefaultImplWithDefaultImplDeserializer() throws IOException {
        AsPropertyTypeDeserializer asPropertyTypeDeserializerSpy = Mockito.spy(asPropertyTypeDeserializer);
        Mockito.when(asPropertyTypeDeserializerSpy._findDefaultImplDeserializer(ArgumentMatchers.any(DeserializationContext.class))).thenReturn(jsonDeserializer);

        Object result = asPropertyTypeDeserializerSpy._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);

        assertTrue(result instanceof Object);
        Mockito.verify(asPropertyTypeDeserializerSpy, Mockito.times(1))._findDefaultImplDeserializer(ArgumentMatchers.any(DeserializationContext.class));
        Mockito.verify(jsonDeserializer, Mockito.times(1)).deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImplWithNaturalValue() throws IOException {
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_TRUE);

        Object result = asPropertyTypeDeserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);

        assertNull(result);
        Mockito.verify(jsonParser, Mockito.times(1)).getCurrentToken();
        Mockito.verify(jsonParser, Mockito.times(1)).clearCurrentToken();
    }

    @Test
    public void testDeserializeTypedUsingDefaultImplWithNaturalNullValue() throws IOException {
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        Object result = asPropertyTypeDeserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);

        assertNull(result);
        Mockito.verify(jsonParser, Mockito.times(1)).getCurrentToken();
        Mockito.verify(jsonParser, Mockito.times(1)).clearCurrentToken();
    }

    @Test
    public void testDeserializeTypedFromAnyWithStartArray() throws IOException {
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        asPropertyTypeDeserializer = Mockito.spy(asPropertyTypeDeserializer);

        Object result = asPropertyTypeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext);

        assertTrue(result instanceof Object);
        Mockito.verify(asPropertyTypeDeserializer, Mockito.times(1)).deserializeTypedFromArray(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        Object result = asPropertyTypeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext);

        assertTrue(result instanceof Object);
        Mockito.verify(asPropertyTypeDeserializer, Mockito.times(1)).deserializeTypedFromObject(jsonParser, deserializationContext);
    }

    @SuppressWarnings("resource")
    @Test
    public void testDeserializeTypedForIdWithTypeIdVisible() throws IOException {
        Mockito.doReturn(true).when(asPropertyTypeDeserializer)._typeIdVisible;
        Mockito.when(jsonParser.getCurrentName()).thenReturn("typePropertyName");
        Mockito.when(jsonParser.getText()).thenReturn("typeId");
        Mockito.when(asPropertyTypeDeserializer._findDeserializer(deserializationContext, "typeId")).thenReturn(jsonDeserializer);

        Object result = asPropertyTypeDeserializer._deserializeTypedForId(jsonParser, deserializationContext, tokenBuffer);

        assertTrue(result instanceof Object);
        Mockito.verify(jsonParser, Mockito.times(1)).getCurrentName();
        Mockito.verify(jsonParser, Mockito.times(1)).getText();
        Mockito.verify(asPropertyTypeDeserializer, Mockito.times(1))._findDeserializer(deserializationContext, "typeId");
        Mockito.verify(tokenBuffer, Mockito.times(1)).writeFieldName("typePropertyName");
        Mockito.verify(tokenBuffer, Mockito.times(1)).writeString("typeId");
        Mockito.verify(jsonDeserializer, Mockito.times(1)).deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserializeTypedForIdWithoutTypeIdVisible() throws IOException {
        Mockito.doReturn(false).when(asPropertyTypeDeserializer)._typeIdVisible;

        Object result = asPropertyTypeDeserializer._deserializeTypedForId(jsonParser, deserializationContext, tokenBuffer);

        assertNull(result);
        Mockito.verify(jsonParser, Mockito.times(1)).getCurrentName();
        Mockito.verify(jsonParser, Mockito.times(1)).getText();
    }

    @Test
    public void testDeserializeTypedUsingDefaultImplWithDefaultDeserializer() throws IOException {
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
        Object result = asPropertyTypeDeserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);

        assertTrue(result instanceof Object);
        Mockito.verify(jsonParser, Mockito.times(1)).getCurrentToken();
        Mockito.verify(jsonParser, Mockito.times(1)).nextToken();
        Mockito.verify(asPropertyTypeDeserializer, Mockito.times(1))._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);
    }

    @SuppressWarnings("resource")
    @Test
    public void testDeserializeTypedFromObjectWithProperty() throws IOException {
        Iterator<String> fieldNameIterator = Mockito.mock(Iterator.class);
        Mockito.when(fieldNameIterator.hasNext()).thenReturn(true, false);
        Mockito.when(fieldNameIterator.next()).thenReturn("typePropertyName");
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
        Mockito.when(jsonParser.getCurrentName()).thenReturn("typePropertyName");
        Mockito.when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(jsonParser.getText()).thenReturn("typeId");
        Mockito.when(jsonParser.copyCurrentStructure(Mockito.any(JsonParser.class))).thenReturn(fieldNameIterator);
        Mockito.when(asPropertyTypeDeserializer._deserializeTypedForId(jsonParser, deserializationContext, tokenBuffer)).thenReturn(new Object());

        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);

        assertTrue(result instanceof Object);
        Mockito.verify(jsonParser, Mockito.times(1)).getCurrentToken();
        Mockito.verify(jsonParser, Mockito.times(1)).nextToken();
        Mockito.verify(jsonParser, Mockito.times(2)).getCurrentName();
        Mockito.verify(jsonParser, Mockito.times(2)).getText();
        Mockito.verify(jsonParser, Mockito.times(1)).copyCurrentStructure(jsonParser);
        Mockito.verify(asPropertyTypeDeserializer, Mockito.times(1))._deserializeTypedForId(jsonParser, deserializationContext, tokenBuffer);
    }

    @SuppressWarnings("resource")
    @Test
    public void testDeserializeTypedFromObjectWithoutProperty() throws IOException {
        Iterator<String> fieldNameIterator = Mockito.mock(Iterator.class);
        Mockito.when(fieldNameIterator.hasNext()).thenReturn(false);
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
        Mockito.when(jsonParser.copyCurrentStructure(Mockito.any(JsonParser.class))).thenReturn(fieldNameIterator);
        Mockito.when(asPropertyTypeDeserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer)).thenReturn(new Object());

        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);

        assertTrue(result instanceof Object);
        Mockito.verify(jsonParser, Mockito.times(1)).getCurrentToken();
        Mockito.verify(jsonParser, Mockito.times(1)).nextToken();
        Mockito.verify(jsonParser, Mockito.times(2)).getCurrentName();
        Mockito.verify(jsonParser, Mockito.times(1)).copyCurrentStructure(jsonParser);
        Mockito.verify(asPropertyTypeDeserializer, Mockito.times(1))._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);
    }

    @Test
    public void testDeserializeTypedFromObjectWithTypeId() throws IOException {
        Mockito.when(jsonParser.canReadTypeId()).thenReturn(true);
        Mockito.when(jsonParser.getTypeId()).thenReturn("typeId");
        Mockito.when(asPropertyTypeDeserializer._deserializeWithNativeTypeId(jsonParser, deserializationContext, "typeId")).thenReturn(new Object());

        Object result = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);

        assertTrue(result instanceof Object);
        Mockito.verify(jsonParser, Mockito.times(1)).canReadTypeId();
        Mockito.verify(jsonParser, Mockito.times(1)).getTypeId();
        Mockito.verify(asPropertyTypeDeserializer, Mockito.times(1))._deserializeWithNativeTypeId(jsonParser, deserializationContext, "typeId");
    }
}