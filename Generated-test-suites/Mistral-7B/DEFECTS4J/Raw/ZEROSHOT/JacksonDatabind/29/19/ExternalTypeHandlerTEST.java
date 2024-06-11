package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private ObjectMock beanMock;
    private JsonParser mockParser;
    private DeserializationContext mockContext;

    @Before
    public void setup() throws Exception {
        mockContext = Mockito.mock(DeserializationContext.class);
        beanMock = new ObjectMock();
        mockParser = Mockito.mock(JsonParser.class);

        SettableBeanProperty prop1 = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty prop2 = Mockito.mock(SettableBeanProperty.class);

        TypeDeserializer typeDeserializer1 = Mockito.mock(TypeDeserializer.class);
        TypeDeserializer typeDeserializer2 = Mockito.mock(TypeDeserializer.class);

        Mockito.when(typeDeserializer1.getDefaultImpl()).thenReturn(String.class);
        Mockito.when(typeDeserializer1.getTypeIdResolver()).thenReturn(Mockito.mock(TypeIdResolver.class));
        Mockito.when(typeDeserializer1.getPropertyName()).thenReturn("typePropertyName1");

        Mockito.when(typeDeserializer2.getDefaultImpl()).thenReturn(Integer.class);
        Mockito.when(typeDeserializer2.getTypeIdResolver()).thenReturn(Mockito.mock(TypeIdResolver.class));
        Mockito.when(typeDeserializer2.getPropertyName()).thenReturn("typePropertyName2");

        Mockito.when(mockContext.findBeanProperty("propertyName1")).thenReturn(prop1);
        Mockito.when(mockContext.findBeanProperty("propertyName2")).thenReturn(prop2);

        handler = new ExternalTypeHandler.Builder()
                .addExternal(prop1, typeDeserializer1)
                .addExternal(prop2, typeDeserializer2)
                .build();
    }

    @Test
    public void testHandleTypePropertyValue() throws JsonProcessingException, IOException {
        String propertyName1 = "propertyName1";
        String typeId1 = "typeId1";

        Mockito.when(mockParser.getText()).thenReturn(typeId1);

        handler.handleTypePropertyValue(mockParser, mockContext, propertyName1, beanMock);

        TokenBuffer expectedTokens1 = new TokenBuffer(mockParser);
        assertEquals(expectedTokens1, handler._tokens[0]);
        assertEquals(typeId1, handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValue_NullTypeId() throws JsonProcessingException, IOException {
        String propertyName1 = "propertyName1";

        handler.handlePropertyValue(mockParser, mockContext, propertyName1, beanMock);

        assertNull(handler._typeIds[0]);
        assertNotNull(handler._tokens[0]);
    }

    @Test
    public void testHandlePropertyValue_NullProperty() throws JsonProcessingException, IOException {
        String propertyName2 = "propertyName2";

        handler.handlePropertyValue(mockParser, mockContext, propertyName2, null);

        assertNull(handler._typeIds[1]);
        assertNull(handler._tokens[1]);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandlePropertyValue_MissingProperty() throws JsonProcessingException, IOException {
        String propertyName3 = "nonExistentProperty";

        handler.handlePropertyValue(mockParser, mockContext, propertyName3, beanMock);
    }

    @Test
    public void testComplete() throws JsonProcessingException, IOException {
        String typeId1 = "typeId1";
        String typeId2 = "typeId2";

        handler.complete(mockParser, mockContext, beanMock);

        Object result = beanMock.getResult();
        assertEquals(result.getClass(), Object[].class);
        assertArrayEquals(new String[] { typeId1, typeId2 }, ((Object[]) result));
    }

    @Test
    public void testComplete_PropertyBasedCreator() throws JsonProcessingException, IOException {
        PropertyBasedCreator creatorMock = Mockito.mock(PropertyBasedCreator.class);
        PropertyValueBuffer bufferMock = Mockito.mock(PropertyValueBuffer.class);

        handler.complete(mockParser, mockContext, bufferMock, creatorMock);

        Object result = creatorMock.build(mockContext, bufferMock);
        assertEquals(result.getClass(), String.class);
    }
}

class ObjectMock {
    private Object result;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}