
package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.HashMap;

import static org.mockito.Mockito.*;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private ExtTypedProperty[] properties;
    private HashMap<String, Integer> nameToPropertyIndex;
    private String[] typeIds;
    private TokenBuffer[] tokens;

    @Mock
    private JsonParser parser;

    @Mock
    private DeserializationContext context;

    @Mock
    private SettableBeanProperty property;

    @Mock
    private TypeDeserializer typeDeser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        properties = new ExtTypedProperty[1];
        typeIds = new String[1];
        tokens = new TokenBuffer[1];
        nameToPropertyIndex = new HashMap<String, Integer>();

        properties[0] = new ExtTypedProperty(property, typeDeser);
        nameToPropertyIndex.put(property.getName(), 0);

        handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
    }

    @Test
    public void testHandleTypePropertyValue_WithMatchingPropertyAndValidType() throws IOException {
        String propName = "propertyName";
        String typeId = "validTypeId";

        when(parser.getText()).thenReturn(typeId);
        when(property.hasTypePropertyName(propName)).thenReturn(true);
        when(property.getName()).thenReturn(propName);
        when(tokens[0]).thenReturn(mock(TokenBuffer.class));

        handler.handleTypePropertyValue(parser, context, propName, new Object());

        verify(parser, times(1)).getText();
        verify(context, times(1)).mappingException("Missing external type id property '%s'", properties[0].getTypePropertyName());
    }

    @Test
    public void testHandlePropertyValue_WithMatchingPropertyAndNoType() throws IOException {
        String propName = "propertyName";

        when(property.hasTypePropertyName(propName)).thenReturn(true);
        when(property.getName()).thenReturn(propName);
        when(tokens[0]).thenReturn(mock(TokenBuffer.class));

        handler.handlePropertyValue(parser, context, propName, new Object());

        verify(parser, times(1)).getText();
        verify(context, times(1)).mappingException("Missing external type id property '%s'", properties[0].getTypePropertyName());
    }
  
    @Test
    public void testComplete_WithMissingTypeAndProperty() throws IOException {
        tokens[0] = mock(TokenBuffer.class);

        handler.complete(parser, context, new Object());

        verify(tokens[0], times(1)).firstToken();
    }
  
    @Test
    public void testComplete_WithMissingProperty() throws IOException {
        typeIds[0] = "validTypeId";

        handler.complete(parser, context, new Object());

        verify(context, times(1)).mappingException("Missing property '%s' for external type id '%s'", property.getName(), properties[0].getTypePropertyName());
    }
  
    @Test
    public void testDeserializeAndSet_WithNonNullTypeIds() throws IOException {
        tokens[0] = mock(TokenBuffer.class);
        when(tokens[0].asParser(parser)).thenReturn(parser);
        when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);

        handler._deserializeAndSet(parser, context, new Object(), 0, "validTypeId");

        verify(tokens[0], times(1)).asParser(parser);
        verify(parser, times(1)).nextToken();
    }
  
    @Test
    public void testDeserialize_WithNonNullTypeIds() throws IOException {
        tokens[0] = mock(TokenBuffer.class);
        when(tokens[0].asParser(parser)).thenReturn(parser);
        when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);

        handler._deserialize(parser, context, 0, "validTypeId");

        verify(tokens[0], times(1)).asParser(parser);
        verify(parser, times(1)).nextToken();
    }

}
