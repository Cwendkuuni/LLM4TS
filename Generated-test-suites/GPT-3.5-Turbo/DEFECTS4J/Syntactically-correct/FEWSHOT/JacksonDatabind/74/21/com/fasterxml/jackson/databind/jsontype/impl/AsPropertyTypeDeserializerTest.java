package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsPropertyTypeDeserializerTest {

    private AsPropertyTypeDeserializer deserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private TokenBuffer tokenBuffer;

    private final JavaType baseType = mock(JavaType.class);
    private final TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
    private final BeanProperty property = mock(BeanProperty.class);

    @Before
    public void setUp() throws Exception {
        deserializer = new AsPropertyTypeDeserializer(baseType, typeIdResolver, "type", true, null, As.PROPERTY);
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        tokenBuffer = mock(TokenBuffer.class);
    }

    @Test
    public void testAsPropertyTypeDeserializer_6args() {
        assertEquals(baseType, deserializer.baseType);
        assertEquals(typeIdResolver, deserializer.typeIdResolver);
        assertEquals("type", deserializer._typePropertyName);
        assertEquals(true, deserializer._typeIdVisible);
        assertNull(deserializer._defaultImpl);
        assertEquals(As.PROPERTY, deserializer._inclusion);
    }

    @Test
    public void testAsPropertyTypeDeserializer_7args() {
        JavaType defaultImpl = mock(JavaType.class);
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(baseType, typeIdResolver, "type", true, defaultImpl, As.PROPERTY);
        assertEquals(baseType, asPropertyTypeDeserializer.baseType);
        assertEquals(typeIdResolver, asPropertyTypeDeserializer.typeIdResolver);
        assertEquals("type", asPropertyTypeDeserializer._typePropertyName);
        assertEquals(true, asPropertyTypeDeserializer._typeIdVisible);
        assertEquals(defaultImpl, asPropertyTypeDeserializer._defaultImpl);
        assertEquals(As.PROPERTY, asPropertyTypeDeserializer._inclusion);
    }

    @Test
    public void testAsPropertyTypeDeserializer_2args() {
        AsPropertyTypeDeserializer asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(deserializer, property);
        assertEquals(baseType, asPropertyTypeDeserializer.baseType);
        assertEquals(typeIdResolver, asPropertyTypeDeserializer.typeIdResolver);
        assertEquals("type", asPropertyTypeDeserializer._typePropertyName);
        assertEquals(true, asPropertyTypeDeserializer._typeIdVisible);
        assertNull(asPropertyTypeDeserializer._defaultImpl);
        assertEquals(As.PROPERTY, asPropertyTypeDeserializer._inclusion);
    }

    @Test
    public void testForProperty() {
        TypeDeserializer result = deserializer.forProperty(property);
        assertNotNull(result);
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
    }

    @Test
    public void testDeserializeTypedFromObject_withNativeTypeId() throws Exception {
        when(jsonParser.canReadTypeId()).thenReturn(true);
        when(jsonParser.getTypeId()).thenReturn("typeId");
        when(deserializer._deserializeWithNativeTypeId(any(JsonParser.class), any(DeserializationContext.class), any(Object.class))).thenReturn("result");

        Object result = deserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        
        assertEquals("result", result);
    }

    @Test
    public void testDeserializeTypedFromObject_startObjectArrayFieldName() throws Exception {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT).thenReturn(JsonToken.FIELD_NAME).thenReturn(JsonToken.END_OBJECT);
        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME).thenReturn(null);

        Object result = deserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        
        assertNull(result);
    }

    @Test
    public void testDeserializeTypedFromObject_fieldName() throws Exception {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME).thenReturn(JsonToken.END_OBJECT);
        when(jsonParser.getCurrentName()).thenReturn("fieldName");
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT, JsonToken.END_OBJECT);

        Object result = deserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeTypedForId() throws Exception {
        when(jsonParser.getText()).thenReturn("typeId");
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(any(DeserializationContext.class), any(String.class))).thenReturn(deser);
        when(deser.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("result");

        Object result = deserializer._deserializeTypedForId(jsonParser, deserializationContext, tokenBuffer);
        
        assertEquals("result", result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_withDeserializer() throws Exception {
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDefaultImplDeserializer(any(DeserializationContext.class))).thenReturn(deser);
        when(deser.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("result");

        Object result = deserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);
        
        assertEquals("result", result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_withNaturalValue() throws Exception {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        ObjectMapper mapper = new ObjectMapper();
        when(deserializationContext.getParser()).thenReturn(mapper.getFactory().createParser("test"));
        
        Object result = deserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);
        
        assertEquals("test", result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_withArray() throws Exception {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        when(deserializer.deserializeTypedFromAny(jsonParser, deserializationContext)).thenReturn("result");

        Object result = deserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);
        
        assertEquals("result", result);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_withException() throws Exception {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jsonParser.getCurrentName()).thenReturn("fieldName");
        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME, JsonToken.FIELD_NAME);
        when(tokenBuffer.asParser(jsonParser)).thenReturn(jsonParser);

        deserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);
        
        // Verify that reportWrongTokenException method is invoked with correct arguments
        // Using Mockito for this verification
    }

    @Test
    public void testDeserializeTypedFromAny_withArray() throws Exception {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        when(deserializer.deserializeTypedFromArray(jsonParser, deserializationContext)).thenReturn("result");

        Object result = deserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
        
        assertEquals("result", result);
    }

    @Test
    public void testDeserializeTypedFromAny_withObject() throws Exception {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(deserializer.deserializeTypedFromObject(jsonParser, deserializationContext)).thenReturn("result");

        Object result = deserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
        
        assertEquals("result", result);
    }

    @Test
    public void testDeserializeTypedFromAny_withException() throws Exception {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        deserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
        
        // Verify that deserializeTypedFromObject method is invoked with correct arguments
        // Using Mockito for this verification
    }

}