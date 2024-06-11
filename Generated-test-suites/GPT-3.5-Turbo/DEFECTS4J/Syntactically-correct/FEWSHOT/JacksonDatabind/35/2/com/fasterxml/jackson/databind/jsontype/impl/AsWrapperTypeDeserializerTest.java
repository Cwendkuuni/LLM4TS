package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class AsWrapperTypeDeserializerTest {

    @Mock
    private DeserializationContext ctxt;

    @Mock
    private JavaType baseType;

    @Mock
    private TypeIdResolver idResolver;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private JsonDeserializer<Object> deserializer;

    private AsWrapperTypeDeserializer typeDeserializer;
    
    @Before
    public void setUp() {
        typeDeserializer = new AsWrapperTypeDeserializer(baseType, idResolver, "typePropertyName", true, Object.class);
    }
   
    @Test
    public void testConstructor() {
        assertEquals(baseType, typeDeserializer.baseType());
        assertEquals(idResolver, typeDeserializer.idResolver());
        assertEquals("typePropertyName", typeDeserializer.getTypePropertyName());
        assertTrue(typeDeserializer.isTypeIdVisible());
        assertEquals(Object.class, typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testForProperty() {
        BeanProperty prop = mock(BeanProperty.class);
        TypeDeserializer newDeserializer = typeDeserializer.forProperty(prop);
        assertNotNull(newDeserializer);
        assertTrue(newDeserializer instanceof AsWrapperTypeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(As.WRAPPER_OBJECT, typeDeserializer.getTypeInclusion());
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        typeDeserializer.deserializeTypedFromObject(jsonParser, ctxt);
        verify(typeDeserializer).deserializeTypedFromObject(jsonParser, ctxt);
        verifyNoMoreInteractions(typeDeserializer);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        typeDeserializer.deserializeTypedFromArray(jsonParser, ctxt);
        verify(typeDeserializer).deserializeTypedFromArray(jsonParser, ctxt);
        verifyNoMoreInteractions(typeDeserializer);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        typeDeserializer.deserializeTypedFromScalar(jsonParser, ctxt);
        verify(typeDeserializer).deserializeTypedFromScalar(jsonParser, ctxt);
        verifyNoMoreInteractions(typeDeserializer);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        typeDeserializer.deserializeTypedFromAny(jsonParser, ctxt);
        verify(typeDeserializer).deserializeTypedFromAny(jsonParser, ctxt);
        verifyNoMoreInteractions(typeDeserializer);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonToken valueType = JsonToken.START_OBJECT;
        when(jsonParser.getCurrentToken()).thenReturn(valueType);

        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jsonParser.getText()).thenReturn("typeId");

        when(typeDeserializer._findDeserializer(ctxt, "typeId")).thenReturn(deserializer);
        
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
        
        typeDeserializer.deserialize(jsonParser, ctxt);
        
        verify(jsonParser, times(2)).nextToken();
        verify(jsonParser).getText();
        verify(deserializer).deserialize(jsonParser, ctxt);
        verifyNoMoreInteractions(typeDeserializer);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDeserializeWrongTokenException1() throws IOException {
        JsonToken valueType = JsonToken.START_ARRAY;
        when(jsonParser.getCurrentToken()).thenReturn(valueType);

        typeDeserializer.deserialize(jsonParser, ctxt);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializeWrongTokenException2() throws IOException {
        JsonToken valueType = JsonToken.START_OBJECT;
        when(jsonParser.getCurrentToken()).thenReturn(valueType);
        
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_ARRAY);

        typeDeserializer.deserialize(jsonParser, ctxt);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializeWrongTokenException3() throws IOException {
    	JsonToken valueType = JsonToken.START_OBJECT;
        when(jsonParser.getCurrentToken()).thenReturn(valueType);
        
        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jsonParser.getText()).thenReturn("typeId");

        when(typeDeserializer._findDeserializer(ctxt, "typeId")).thenReturn(deserializer);
        
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);

        typeDeserializer.deserialize(jsonParser, ctxt);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeserializeWrongTokenException4() throws IOException {
    	JsonToken valueType = JsonToken.START_OBJECT;
        when(jsonParser.getCurrentToken()).thenReturn(valueType);
        
        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jsonParser.getText()).thenReturn("typeId");

        when(typeDeserializer._findDeserializer(ctxt, "typeId")).thenReturn(deserializer);
        
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);

        typeDeserializer.deserialize(jsonParser, ctxt);
    }
}