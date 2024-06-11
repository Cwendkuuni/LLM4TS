package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {
    
    private AsWrapperTypeDeserializer deserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;

    @Before
    public void setup() {
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        
        deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
    }
    
    @Test
    public void testForProperty_sameProperty_returnThis() {
        // Arrange
        BeanProperty prop = mock(BeanProperty.class);
        
        // Act
        TypeDeserializer result = deserializer.forProperty(prop);
        
        // Assert
        assertEquals(deserializer, result);
    }
    
    @Test
    public void testForProperty_differentProperty_returnNewInstance() {
        // Arrange
        BeanProperty prop = mock(BeanProperty.class);
        
        // Act
        TypeDeserializer result = deserializer.forProperty(prop);
        
        // Assert
        assertNotSame(deserializer, result);
        assertEquals(AsWrapperTypeDeserializer.class, result.getClass());
    }
    
    @Test
    public void testGetTypeInclusion_returnWrapperObject() {
        // Act
        As result = deserializer.getTypeInclusion();
        
        // Assert
        assertEquals(As.WRAPPER_OBJECT, result);
    }
    
    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        // Arrange
        Object expectedResult = new Object();
        doReturn(expectedResult).when(deserializer)._deserialize(jsonParser, deserializationContext);
        
        // Act
        Object result = deserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        
        // Assert
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        // Arrange
        Object expectedResult = new Object();
        doReturn(expectedResult).when(deserializer)._deserialize(jsonParser, deserializationContext);
        
        // Act
        Object result = deserializer.deserializeTypedFromArray(jsonParser, deserializationContext);
        
        // Assert
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        // Arrange
        Object expectedResult = new Object();
        doReturn(expectedResult).when(deserializer)._deserialize(jsonParser, deserializationContext);
        
        // Act
        Object result = deserializer.deserializeTypedFromScalar(jsonParser, deserializationContext);
        
        // Assert
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        // Arrange
        Object expectedResult = new Object();
        doReturn(expectedResult).when(deserializer)._deserialize(jsonParser, deserializationContext);
        
        // Act
        Object result = deserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
        
        // Assert
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testDeserialize_withNativeTypeId() throws IOException {
        // Arrange
        Object expectedResult = new Object();
        String typeId = "typeId";
        doReturn(typeId).when(jsonParser).getTypeId();
        doReturn(expectedResult).when(deserializer)._deserializeWithNativeTypeId(jsonParser, deserializationContext, typeId);
        
        // Act
        Object result = deserializer._deserialize(jsonParser, deserializationContext);
        
        // Assert
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testDeserialize_needJsonObject_throwException() throws IOException {
        // Arrange
        doReturn(JsonToken.VALUE_NULL).when(jsonParser).getCurrentToken();
        
        try {
            // Act
            deserializer._deserialize(jsonParser, deserializationContext);
            fail("Exception should be thrown");
        } catch (JsonParseException ex) {
            // Assert
            assertEquals("need JSON Object to contain As.WRAPPER_OBJECT type information for class java.lang.Object", ex.getMessage());
        }
    }
    
    @Test
    public void testDeserialize_needJsonFieldName_throwException() throws IOException {
        // Arrange
        doReturn(JsonToken.START_OBJECT).doReturn(JsonToken.END_OBJECT).when(jsonParser).getCurrentToken();
        doReturn(JsonToken.VALUE_STRING).when(jsonParser).nextToken();
        
        try {
            // Act
            deserializer._deserialize(jsonParser, deserializationContext);
            fail("Exception should be thrown");
        } catch (JsonParseException ex) {
            // Assert
            assertEquals("need JSON String that contains type id (for subtype of java.lang.Object)", ex.getMessage());
        }
    }
    
    @Test
    public void testDeserialize_deserializeReturnValue() throws IOException {
        // Arrange
        String typeId = "typeId";
        Object value = new Object();
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        doReturn(typeId).when(jsonParser).getText();
        doReturn(deser).when(deserializer)._findDeserializer(deserializationContext, typeId);
        doReturn(JsonToken.END_OBJECT).when(jsonParser).nextToken();
        doReturn(value).when(deser).deserialize(jsonParser, deserializationContext);
        
        // Act
        Object result = deserializer._deserialize(jsonParser, deserializationContext);
        
        // Assert
        assertEquals(value, result);
    }
    
    @Test
    public void testDeserialize_typeIdVisibleWithStartObject_wrapJsonParser() throws IOException {
        // Arrange
        String typeId = "typeId";
        JsonToken startObject = JsonToken.START_OBJECT;
        JsonToken fieldName = JsonToken.FIELD_NAME;
        JsonToken endObject = JsonToken.END_OBJECT;
        JsonToken valueString = JsonToken.VALUE_STRING;
        JsonToken endObject2 = JsonToken.END_OBJECT;
        
        JsonParser firstJsonParser = mock(JsonParser.class);
        JsonParser secondJsonParser = mock(JsonParser.class);
        
        TokenBuffer tb = mock(TokenBuffer.class);
        
        when(firstJsonParser.canReadTypeId()).thenReturn(true);
        when(firstJsonParser.getTypeId()).thenReturn(typeId);
        when(firstJsonParser.getCurrentToken()).thenReturn(startObject);
        
        when(firstJsonParser.nextToken()).thenReturn(fieldName, endObject);
        when(firstJsonParser.getText()).thenReturn(typeId);
        
        when(secondJsonParser.nextToken()).thenReturn(endObject2);
        
        when(tb.asParser(firstJsonParser)).thenReturn(secondJsonParser);
        
        JsonParserSequence jsonParserSequence = JsonParserSequence.createFlattened(tb.asParser(firstJsonParser), firstJsonParser);
        jsonParserSequence.nextToken();
        
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        Object value = new Object();
        
        doReturn(typeId).when(jsonParser).getText();
        doReturn(deser).when(deserializer)._findDeserializer(deserializationContext, typeId);
        doReturn(jsonParserSequence).when(deserializer).createClassWithAdditionalPropertiesTokenizer(tb, jsonParser);
        doReturn(JsonToken.END_OBJECT).when(jsonParser).nextToken();
        doReturn(value).when(deser).deserialize(jsonParserSequence, deserializationContext);
        
        // Act
        deserializer._deserialize(jsonParser, deserializationContext);
        
        // Assert
        verify(jsonParser).nextToken();
        verify(jsonParserSequence).nextToken();
        verify(tb).writeStartObject();
        verify(tb).writeFieldName("type");
        verify(tb).writeString(typeId);
        verify(tb).asParser(jsonParser);
        verify(jsonParserSequence).flatCopy(tb.asParser(jsonParser), jsonParser);
        verify(jsonParserSequence).nextToken();
        verify(deser).deserialize(jsonParserSequence, deserializationContext);
        verify(jsonParser).nextToken();
    }
    
    @Test
    public void testDeserialize_typeIdVisibleNotStartObject_throwException() throws IOException {
        // Arrange
        doReturn("").when(jsonParser).getTypeId();
        doReturn(false).when(jsonParser).canReadTypeId();
        doReturn(JsonToken.VALUE_STRING).when(jsonParser).getCurrentToken();
        
        try {
            // Act
            deserializer._deserialize(jsonParser, deserializationContext);
            fail("Exception should be thrown");
        } catch (JsonParseException ex) {
            // Assert
            assertEquals("need JSON Object to contain As.WRAPPER_OBJECT type information for class java.lang.Object", ex.getMessage());
        }
    }
    
    @Test
    public void testDeserialize_missingEndObject_throwException() throws IOException {
        // Arrange
        doReturn(JsonToken.START_OBJECT).when(jsonParser).getCurrentToken();
        doReturn(JsonToken.FIELD_NAME).when(jsonParser).nextToken();
        doReturn("").when(jsonParser).getText();
        
        try {
            // Act
            deserializer._deserialize(jsonParser, deserializationContext);
            fail("Exception should be thrown");
        } catch (JsonParseException ex) {
            // Assert
            assertEquals("expected closing END_OBJECT after type information and deserialized value", ex.getMessage());
        }
    }
}