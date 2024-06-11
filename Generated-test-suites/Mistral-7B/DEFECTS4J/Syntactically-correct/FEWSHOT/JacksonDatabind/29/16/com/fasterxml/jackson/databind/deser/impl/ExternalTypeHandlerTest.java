package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private JsonParser mockParser;
    private DeserializationContext mockContext;
    private Object mockBean;
    private List<ExtTypedProperty> mockedProperties;
    private Map<String, Integer> mockedNameToPropertyIndex;

    @Before
    public void setup() throws Exception {
        mockParser = mock(JsonParser.class);
        mockContext = mock(DeserializationContext.class);
        mockBean = mock(Object.class);
        mockedProperties = new ArrayList<>();
        mockedNameToPropertyIndex = new HashMap<>();

        handler = new ExternalTypeHandler(mockedProperties.toArray(new ExtTypedProperty[0]),
                mockedNameToPropertyIndex, null, null);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String propName = "propName";
        ExtTypedProperty mockedProperty = mock(ExtTypedProperty.class);
        when(mockedProperty.getPropertyName()).thenReturn(propName);
        when(mockedProperty.hasTypePropertyName(eq(propName))).thenReturn(true);

        mockedProperties.add(mockedProperty);
        mockedNameToPropertyIndex.put(propName, 0);

        String typeId = "typeId";
        when(mockParser.getText()).thenReturn(typeId);

        verify(handler).handlePropertyValue(mockParser, eq(mockContext), eq(propName), eq(mockBean));
    }

    @Test
    public void testHandleTypePropertyValue_noMatchingProperty() throws IOException {
        String propName = "propName";
        ExtTypedProperty mockedProperty = mock(ExtTypedProperty.class);
        when(mockedProperty.getPropertyName()).thenReturn(propName);
        when(mockedProperty.hasTypePropertyName(eq(propName))).thenReturn(false);

        mockedProperties.add(mockedProperty);
        mockedNameToPropertyIndex.put(propName, 0);

        String typeId = "typeId";
        when(mockParser.getText()).thenReturn(typeId);

        verify(handler, never()).handlePropertyValue(mockParser, eq(mockContext), eq(propName), eq(mockBean));
    }

    @Test
    public void testHandlePropertyValue_handleValue() throws IOException {
        String propName = "propName";
        ExtTypedProperty mockedProperty = mock(ExtTypedProperty.class);
        when(mockedProperty.getPropertyName()).thenReturn(propName);
        when(mockedProperty.hasTypePropertyName(eq(propName))).thenReturn(false);

        mockedProperties.add(mockedProperty);
        mockedNameToPropertyIndex.put(propName, 0);

        String typeId = "typeId";

        when(mockParser.getText()).thenReturn(typeId);
        when(mockParser.readValueAs(String.class)).thenReturn("value");

        TypeDeserializer mockedTypeDeserializer = mock(TypeDeserializer.class);
        mockedProperty.getTypeDeserializer() = mockedTypeDeserializer;
        doAnswer(invocation -> {
            Object result = invocation.callRealMethod();
            if (result != null) {
                mockedTypeDeserializer.getProperty().set(mockBean, result);
            }
            return null;
        }).when(mockedTypeDeserializer).deserialize(any(JsonParser.class), eq(mockContext), eq(mockedTypeDeserializer.getProperty()));

        handler.handlePropertyValue(mockParser, eq(mockContext), eq(propName), eq(mockBean));

        verify(mockedTypeDeserializer, times(1)).deserialize(eq(mockParser), eq(mockContext), eq(mockedTypeDeserializer.getProperty()));
    }

    @Test
    public void testHandlePropertyValue_bufferValue() throws IOException {
        String propName = "propName";
        ExtTypedProperty mockedProperty = mock(ExtTypedProperty.class);
        when(mockedProperty.getPropertyName()).thenReturn(propName);
        when(mockedProperty.hasTypePropertyName(eq(propName))).thenReturn(false);

        mockedProperties.add(mockedProperty);
        mockedNameToPropertyIndex.put(propName, 0);

        String typeId = "typeId";

        when(mockParser.getText()).thenReturn(typeId);
        when(mockParser.readValueAs(String.class)).thenReturn("value");

        TypeDeserializer mockedTypeDeserializer = mock(TypeDeserializer.class);
        mockedProperty.getTypeDeserializer() = mockedTypeDeserializer;
        doReturn(mockedTypeDeserializer).when(mockedTypeDeserializer).deserializeIfNatural(any(JsonParser.class), eq(mockContext), eq(eq(String.class)));
        doThrow(new IOException("test")).when(mockedTypeDeserializer).deserialize(any(JsonParser.class), eq(mockContext), any());

        handler.handlePropertyValue(mockParser, eq(mockContext), eq(propName), eq(mockBean));

        verify(mockedTypeDeserializer, never()).deserialize(eq(mockParser), eq(mockContext), any());
    }

    @Test
    public void testComplete() throws IOException {
        int length = 3;
        String[] typeIds = new String[length];
        TokenBuffer[] tokens = new TokenBuffer[length];

        for (int i = 0; i < length; i++) {
            String propName = "prop" + i;
            ExtTypedProperty mockedProperty = mock(ExtTypedProperty.class);
            when(mockedProperty.getPropertyName()).thenReturn(propName);
            mockedProperties.add(mockedProperty);
            mockedNameToPropertyIndex.put(propName, i);

            String typeId = "typeId" + i;
            typeIds[i] = typeId;

            TokenBuffer token = new TokenBuffer(mockParser);
            tokens[i] = token;
        }

        Object result = handler.complete(mockParser, eq(mockContext), mockBean);

        for (int i = 0; i < length; i++) {
            String typeId = handler._typeIds[i];
            if (typeId != null) {
                verify(mockedProperties.get(i).getProperty()).deserialize(eq(mockParser), eq(mockContext), eq(mockBean));
            }
            if (tokens[i] != null) {
                verify(tokens[i]).close();
            }
        }

        assertSame(mockBean, result);
    }

    @Test
    public void testComplete_withPropertyValueBuffer() throws IOException {
        int length = 3;
        String[] typeIds = new String[length];
        TokenBuffer[] tokens = new TokenBuffer[length];

        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);

        for (int i = 0; i < length; i++) {
            String propName = "prop" + i;
            ExtTypedProperty mockedProperty = mock(ExtTypedProperty.class);
            when(mockedProperty.getPropertyName()).thenReturn(propName);
            mockedProperties.add(mockedProperty);
            mockedNameToPropertyIndex.put(propName, i);

            String typeId = "typeId" + i;
            typeIds[i] = typeId;

            TokenBuffer token = new TokenBuffer(mockParser);
            tokens[i] = token;
        }

        Object result = handler.complete(mockParser, eq(mockContext), eq(buffer), eq(creator));

        for (int i = 0; i < length; i++) {
            String typeId = handler._typeIds[i];
            if (typeId != null) {
                verify(mockedProperties.get(i).getProperty()).deserialize(eq(mockParser), eq(mockContext), eq(mockBean));
            }
            if (tokens[i] != null) {
                verify(tokens[i]).close();
            }
            if (creator.findCreatorProperty(propName) != null) {
                verify(buffer).assignParameter(eq(mockedProperties.get(i).getProperty()), any());
            }
        }

        verify(creator, times(1)).build(eq(mockContext), eq(buffer));

        assertNull(handler._typeIds);
        assertNull(handler._tokens);
    }

    @Test(expected = IOException.class)
    public void testComplete_missingProperty() throws IOException {
        int length = 3;
        String[] typeIds = new String[length];
        TokenBuffer[] tokens = new TokenBuffer[length];

        for (int i = 0; i < length; i++) {
            String propName = "prop" + i;
            ExtTypedProperty mockedProperty = mock(ExtTypedProperty.class);
            when(mockedProperty.getPropertyName()).thenReturn(propName);
            mockedProperties.add(mockedProperty);
            mockedNameToPropertyIndex.put(propName, i);

            String typeId = "typeId" + i;
            typeIds[i] = typeId;

            TokenBuffer token = new TokenBuffer(mockParser);
            tokens[i] = token;
        }

        String missingPropName = "missingProp";
        ExtTypedProperty missingProperty = mock(ExtTypedProperty.class);
        when(missingProperty.getPropertyName()).thenReturn(missingPropName);
        mockedProperties.add(missingProperty);
        mockedNameToPropertyIndex.put(missingPropName, length);

        try {
            handler.complete(mockParser, eq(mockContext), mockBean);
        } finally {
            verify(mockContext, times(1)).mappingException(anyString());
        }
    }

    @Test(expected = IOException.class)
    public void testComplete_missingTypeId() throws IOException {
        int length = 3;
        String[] typeIds = new String[length];
        TokenBuffer[] tokens = new TokenBuffer[length];

        for (int i = 0; i < length; i++) {
            String propName = "prop" + i;
            ExtTypedProperty mockedProperty = mock(ExtTypedProperty.class);
            when(mockedProperty.getPropertyName()).thenReturn(propName);
            mockedProperties.add(mockedProperty);
            mockedNameToPropertyIndex.put(propName, i);

            String typeId = "typeId" + i;
            typeIds[i] = typeId;

            TokenBuffer token = new TokenBuffer(mockParser);
            tokens[i] = token;
        }

        int index = length;
        String propName = "prop" + index;
        ExtTypedProperty mockedProperty = mock(ExtTypedProperty.class);
        when(mockedProperty.getPropertyName()).thenReturn(propName);
        mockedProperties.add(mockedProperty);
        mockedNameToPropertyIndex.put(propName, index);

        try {
            handler.complete(mockParser, eq(mockContext), mockBean);
        } finally {
            verify(mockContext, times(1)).mappingException(anyString());
        }
    }

}