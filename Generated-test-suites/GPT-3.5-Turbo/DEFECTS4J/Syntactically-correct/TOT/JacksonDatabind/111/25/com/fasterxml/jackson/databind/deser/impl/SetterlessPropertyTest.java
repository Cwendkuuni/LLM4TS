package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;

public class SetterlessPropertyTest {

    @Test
    public void testDeserializeAndSet_WhenTokenIsNull_ThenDoNothing() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        
        when(jsonParser.getCurrentToken()).thenReturn(null);
        
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                mock(BeanPropertyDefinition.class),
                mock(Method.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedMethod.class)
        );
        
        // Act
        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        
        // Assert
        // No exception should be thrown and nothing should be set since the token is null
        verifyZeroInteractions(deserializationContext);
        verifyZeroInteractions(instance);
    }
	
    @Test
    public void testDeserializeAndSet_WhenTokenTypeIsValueNull_ThenDoNothing() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                mock(BeanPropertyDefinition.class),
                mock(Method.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedMethod.class)
        );
        
        // Act
        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        
        // Assert
        // No exception should be thrown and nothing should be set since the token is value null
        verifyZeroInteractions(deserializationContext);
        verifyZeroInteractions(instance);
    }
	
    @Test (expected = JsonMappingException.class)
    public void testDeserializeAndSet_WhenGetterInvocationThrowsException_ThenThrowJsonMappingException() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        
        when(jsonParser.getCurrentToken()).thenReturn(mock(JsonToken.class));
        
        Method getterMethod = mock(Method.class);
        when(getterMethod.invoke(eq(instance), (Object[]) any())).thenThrow(Exception.class);
        
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                mock(BeanPropertyDefinition.class),
                mock(Method.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedMethod.class)
        );
        
        // Act
        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        
        // Assert
        // JsonMappingException should be thrown and nothing should be set since the getter invocation threw an exception
        verifyZeroInteractions(instance);
    }
	
    @Test (expected = JsonParseException.class)
    public void testDeserializeAndSet_WhenToModifyIsNull_ThenThrowJsonParseException() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        
        when(jsonParser.getCurrentToken()).thenReturn(mock(JsonToken.class));
        
        Method getterMethod = mock(Method.class);
        when(getterMethod.invoke(eq(instance), (Object[]) any())).thenReturn(null);
        
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                mock(BeanPropertyDefinition.class),
                mock(Method.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedMethod.class)
        );
        
        // Act
        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        
        // Assert
        // JsonParseException should be thrown and nothing should be set since the toModify object is null
        verifyZeroInteractions(instance);
    }
	
    @Test
    public void testDeserializeAndSet_WhenToModifyIsNotNull_ThenDeserializeWithDeserializer() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        
        JsonNode toModify = mock(JsonNode.class);
        when(toModify.isNull()).thenReturn(false);
        
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        when(objectMapper.readTree(jsonParser)).thenReturn(toModify);
        
        Object instance = mock(Object.class);
        when(jsonParser.getCodec()).thenReturn(objectMapper);
        
        when(jsonParser.getCurrentToken()).thenReturn(mock(JsonToken.class));
        
        Method getterMethod = mock(Method.class);
        when(getterMethod.invoke(eq(instance), (Object[]) any())).thenReturn(toModify);
        
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                mock(BeanPropertyDefinition.class),
                mock(Method.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedMethod.class)
        );
        
        // Act
        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);
        
        // Assert
        // Deserialization should be done with the provided deserializer and the resulting object should be set
        verify(deserializationContext, times(1)).reportBadDefinition(eq(setterlessProperty.getType()), anyString());
    }
	
    @Test
    public void testDeserializeSetAndReturn_WhenToModifyIsNull_ThenNothingChanges() throws IOException {
        // Arrange
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        
        when(jsonParser.getCurrentToken()).thenReturn(mock(JsonToken.class));
        
        Method getterMethod = mock(Method.class);
        when(getterMethod.invoke(eq(instance), (Object[]) any())).thenReturn(null);
        
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                mock(BeanPropertyDefinition.class),
                mock(Method.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedMethod.class)
        );
        
        // Act
        Object result = setterlessProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        
        // Assert
        // Nothing should change and the same instance should be returned
        Assert.assertEquals(instance, result);
    }
	
    @Test
    public void testSet_ThenThrowUnsupportedOperationException() throws IOException {
        // Arrange
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                mock(BeanPropertyDefinition.class),
                mock(Method.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedMethod.class)
        );
        
        // Act and Assert
        // UnsupportedOperationException should be thrown since the set method should never be called on a setterless property
        Assert.assertThrows(UnsupportedOperationException.class, () -> setterlessProperty.set(mock(Object.class), mock(Object.class)));
    }
	
    @Test
    public void testSetAndReturn_ThenThrowUnsupportedOperationException() throws IOException {
        // Arrange
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                mock(BeanPropertyDefinition.class),
                mock(Method.class),
                mock(TypeDeserializer.class),
                mock(Annotations.class),
                mock(AnnotatedMethod.class)
        );
        
        // Act and Assert
        // UnsupportedOperationException should be thrown since the set method should never be called on a setterless property
        Assert.assertThrows(UnsupportedOperationException.class, () -> setterlessProperty.setAndReturn(mock(Object.class), mock(Object.class)));
    }
}