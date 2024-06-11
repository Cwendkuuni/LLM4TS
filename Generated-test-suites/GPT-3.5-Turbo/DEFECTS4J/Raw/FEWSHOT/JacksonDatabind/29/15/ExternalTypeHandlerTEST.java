
package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler.Builder builder;

    @Mock
    private SettableBeanProperty property1;

    @Mock
    private SettableBeanProperty property2;

    @Mock
    private TypeDeserializer typeDeserializer1;

    @Mock
    private TypeDeserializer typeDeserializer2;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        builder = new ExternalTypeHandler.Builder();
        builder.addExternal(property1, typeDeserializer1);
        builder.addExternal(property2, typeDeserializer2);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = builder.build();

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object bean = new Object();

        when(property1.getName()).thenReturn("property1");
        when(property2.getName()).thenReturn("property2");
        when(typeDeserializer1.getPropertyName()).thenReturn("typeDeserializer1");
        when(typeDeserializer2.getPropertyName()).thenReturn("typeDeserializer2");
        when(jsonParser.getText()).thenReturn("typeId1").thenReturn("typeId2");

        boolean result1 = externalTypeHandler.handleTypePropertyValue(jsonParser, deserializationContext, "typeDeserializer1", bean);
        boolean result2 = externalTypeHandler.handleTypePropertyValue(jsonParser, deserializationContext, "typeDeserializer2", bean);

        assertTrue(result1);
        assertTrue(result2);

        verify(jsonParser, times(2)).getText();
        verifyZeroInteractions(jsonParser);
        verify(typeDeserializer1).hasTypePropertyName("typeDeserializer1");
        verify(typeDeserializer2).hasTypePropertyName("typeDeserializer2");
        verify(property1).deserializeAndSet(any(JsonParser.class), any(DeserializationContext.class), any(Object.class));
        verify(property2).deserializeAndSet(any(JsonParser.class), any(DeserializationContext.class), any(Object.class));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = builder.build();

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object bean = new Object();

        when(property1.getName()).thenReturn("property1");
        when(property2.getName()).thenReturn("property2");
        when(typeDeserializer1.getPropertyName()).thenReturn("typeDeserializer1");
        when(typeDeserializer2.getPropertyName()).thenReturn("typeDeserializer2");

        boolean result1 = externalTypeHandler.handlePropertyValue(jsonParser, deserializationContext, "property1", bean);
        boolean result2 = externalTypeHandler.handlePropertyValue(jsonParser, deserializationContext, "property2", bean);

        assertTrue(result1);
        assertTrue(result2);

        verifyZeroInteractions(jsonParser);
        verifyZeroInteractions(typeDeserializer1);
        verifyZeroInteractions(typeDeserializer2);
        verify(property1).deserializeAndSet(any(JsonParser.class), any(DeserializationContext.class), any(Object.class));
        verify(property2).deserializeAndSet(any(JsonParser.class), any(DeserializationContext.class), any(Object.class));
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler externalTypeHandler = builder.build();

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object bean = new Object();

        externalTypeHandler.complete(jsonParser, deserializationContext, bean);

        verifyZeroInteractions(jsonParser);
        verifyZeroInteractions(deserializationContext);
    }

    @Test
    public void testCompleteWithBuffer() throws IOException {
        ExternalTypeHandler externalTypeHandler = builder.build();

        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);
        Object bean = new Object();

        externalTypeHandler.complete(jsonParser, deserializationContext, buffer, creator);

        verifyZeroInteractions(jsonParser);
        verifyZeroInteractions(deserializationContext);
        verifyZeroInteractions(buffer);
        verifyZeroInteractions(creator);
    }

}
