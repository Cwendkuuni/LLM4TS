
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;

public class AsPropertyTypeDeserializerTest {
    
    private AsPropertyTypeDeserializer deserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private TokenBuffer tokenBuffer;

    @Before
    public void setUp() throws Exception {
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = false;
        JavaType defaultImpl = mock(JavaType.class);
        As inclusion = As.PROPERTY;
        
        deserializer = new AsPropertyTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl, inclusion);
        
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        tokenBuffer = mock(TokenBuffer.class);
    }
    
    @Test
    public void testDeserializeTypedFromObject_NativeTypeIdNotAvailable() throws Exception {
        //Arrange
        when(jsonParser.canReadTypeId()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME).thenReturn(JsonToken.END_OBJECT);
        
        //Act
        Object result = deserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        
        //Assert
        assertNull(result);
        verify(deserializer, never())._deserializeWithNativeTypeId(jsonParser, deserializationContext, null);
        verify(deserializer, never())._deserializeTypedForId(jsonParser, deserializationContext, tokenBuffer);
        verify(deserializer, never())._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);
    }
    
    @Test
    public void testDeserializeTypedFromObject_NativeTypeIdAvailable() throws Exception {
        //Arrange
        when(jsonParser.canReadTypeId()).thenReturn(true);
        Object typeId = "1";
        when(jsonParser.getTypeId()).thenReturn(typeId);
        
        //Act
        Object result = deserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        
        //Assert
        assertNull(result);
        verify(deserializer)._deserializeWithNativeTypeId(jsonParser, deserializationContext, typeId);
        verify(deserializer, never())._deserializeTypedForId(jsonParser, deserializationContext, tokenBuffer);
        verify(deserializer, never())._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);
    }
    
    @Test
    public void testDeserializeTypedFromObject_ObjectStartToken() throws Exception {
        //Arrange
        when(jsonParser.canReadTypeId()).thenReturn(true);
        Object typeId = "1";
        when(jsonParser.getTypeId()).thenReturn(typeId);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jsonParser.getCurrentName()).thenReturn("type");
        
        //Act
        Object result = deserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        
        //Assert
        assertNull(result);
        verify(deserializer, never())._deserializeWithNativeTypeId(jsonParser, deserializationContext, typeId);
        verify(deserializer)._deserializeTypedForId(jsonParser, deserializationContext, tokenBuffer);
        verify(deserializer, never())._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);
    }
    
    @Test
    public void testDeserializeTypedFromObject_NonObjectStartToken() throws Exception {
        //Arrange
        when(jsonParser.canReadTypeId()).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        
        //Act
        Object result = deserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        
        //Assert
        assertNull(result);
        verify(deserializer, never())._deserializeWithNativeTypeId(jsonParser, deserializationContext, null);
        verify(deserializer, never())._deserializeTypedForId(jsonParser, deserializationContext, tokenBuffer);
        verify(deserializer)._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);
    }
    
    @Test
    public void testDeserializeTypedForId() throws Exception {
        //Arrange
        String typeId = "1";
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn(typeId);
        
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(any(DeserializationContext.class), anyString())).thenReturn(deser);
        
        String fieldName = "field";
        when(jsonParser.getCurrentName()).thenReturn(fieldName);
        
        when(tokenBuffer.asParser(jsonParser)).thenReturn(jsonParser);
        
        //Act
        Object result = deserializer._deserializeTypedForId(jsonParser, deserializationContext, tokenBuffer);
        
        //Assert
        assertEquals(result, deser.deserialize(jsonParser, deserializationContext));
        verify(jsonParser).clearCurrentToken();
        verify(jsonParser).writeFieldName(fieldName);
        verify(jsonParser).writeString(typeId);
    }
    
    @Test
    public void _deserializeTypedUsingDefaultImpl_Test() throws Exception {
        //Arrange
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDefaultImplDeserializer(deserializationContext)).thenReturn(deser);
        
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        
        //Act
        Object result = deserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);
        
        //Assert
        assertEquals(result, deser.deserialize(jsonParser, deserializationContext));
        verify(jsonParser, never()).writeFieldName(anyString());
    }
    
    @Test
    public void _deserializeTypedUsingDefaultImpl_DefaultImplDeserializerIsNull_Test() throws Exception {
        //Arrange
        when(deserializer._findDefaultImplDeserializer(deserializationContext)).thenReturn(null);
        
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        
        //Act
        Object result = deserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, tokenBuffer);
        
        //Assert
        assertNull(result);
        verify(deserializer, never()).deserializeTypedFromAny(jsonParser, deserializationContext);
        verify(deserializationContext).reportWrongTokenException(jsonParser, JsonToken.FIELD_NAME, "missing property '"+deserializer._typePropertyName+"' that is to contain type id  (for class javaType)");
    }
    
    @Test
    public void testDeserializeTypedFromAny_ArrayStartToken() throws Exception {
        //Arrange
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        
        //Act
        Object result = deserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
        
        //Assert
        assertNull(result);
        verify(deserializer)._deserializeTypedFromArray(jsonParser, deserializationContext);
        verify(deserializer, never()).deserializeTypedFromObject(jsonParser, deserializationContext);
    }
    
    @Test
    public void testDeserializeTypedFromAny_ObjectStartToken() throws Exception {
        //Arrange
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        
        //Act
        Object result = deserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
        
        //Assert
        assertNull(result);
        verify(deserializer, never())._deserializeTypedFromArray(jsonParser, deserializationContext);
        verify(deserializer).deserializeTypedFromObject(jsonParser, deserializationContext);
    }
}

