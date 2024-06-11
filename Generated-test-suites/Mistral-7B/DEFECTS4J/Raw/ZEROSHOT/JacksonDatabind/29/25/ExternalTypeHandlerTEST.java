package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private ObjectMapper mapper;
    private ExternalTypeHandler handler;
    private List<SettableBeanProperty> properties;
    private Map<String, SettableBeanProperty> propertyMap;
    private JsonParser parser;
    private DeserializationContext context;
    private TypeDeserializer typeDeserializer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        properties = new ArrayList<>();
        propertyMap = new HashMap<>();
        parser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
        typeDeserializer = mock(TypeDeserializer.class);

        when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);

        SettableBeanProperty p1 = mock(SettableBeanProperty.class);
        when(p1.getName()).thenReturn(PropertyName.construct("p1"));
        SettableBeanProperty p2 = mock(SettableBeanProperty.class);
        when(p2.getName()).thenReturn(PropertyName.construct("p2"));

        propertyMap.put("p1", p1);
        propertyMap.put("p2", p2);

        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().addExternal(p1, typeDeserializer).build());
    }

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        String typeId = "type1";

        when(parser.getText()).thenReturn(typeId);

        handler.handleTypePropertyValue(parser, context, "p1", null);

        verify(context).handleUnexpectedValue(eq(parser), eq(typeId), anyInt(), any(JsonDeserializer.class));
        verify(parser, times(2)).nextToken();
        verifyNoMoreInteractions(parser);

        assertEquals("type1", handler.getTypeIds()[0]);
    }

    @Test
    public void testHandlePropertyValue() throws Exception {
        String value = "value1";

        when(parser.getText()).thenReturn(value);

        handler.handlePropertyValue(parser, context, "p1", null);

        verify(parser).skipChildren();
        verifyNoMoreInteractions(parser);

        verify(parser, times(2)).nextToken();
        verifyNoMoreInteractions(parser);
    }

    @Test
    public void testComplete() throws Exception {
        String typeId1 = "type1";
        String typeId2 = "type2";

        when(parser.getText()).thenReturn(typeId1).thenReturn(JsonToken.START_ARRAY).thenReturn(typeId2)
                .thenReturn(JsonToken.END_ARRAY).thenReturn(JsonToken.END_OBJECT);

        handler.handlePropertyValue(parser, context, "p1", null);
        handler.handlePropertyValue(parser, context, "p2", null);

        handler.complete(parser, context, null);

        verify(parser, times(2)).nextToken();
        verifyNoMoreInteractions(parser);

        assertEquals("type1", handler.getTypeIds()[0]);
        assertEquals("type2", handler.getTypeIds()[1]);
    }

    @Test
    public void testCompleteWithCreator() throws Exception {
        String typeId1 = "type1";
        String typeId2 = "type2";

        when(parser.getText()).thenReturn(typeId1).thenReturn(JsonToken.START_ARRAY).thenReturn(typeId2)
                .thenReturn(JsonToken.END_ARRAY);

        Object bean = new Object();

        handler.handlePropertyValue(parser, context, "p1", bean);
        handler.handlePropertyValue(parser, context, "p2", bean);

        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);

        when(creator.build(any(DeserializationContext.class), any(PropertyValueBuffer.class))).thenReturn(new Object());

        handler.complete(parser, context, buffer, creator);

        verify(parser, times(2)).nextToken();
        verifyNoMoreInteractions(parser);

        assertEquals("type1", handler.getTypeIds()[0]);
        assertEquals("type2", handler.getTypeIds()[1]);

        verify(buffer).assignParameter(eq(propertyMap.get("p1")), any());
        verify(buffer).assignParameter(eq(propertyMap.get("p2")), any());

        verify(creator).build(eq(context), eq(buffer));
    }

    @Test
    public void testDeserialize() throws Exception {
        String json = "{ \"typeId\": \"type1\", \"value\": 1 }";

        JsonParser parser = mapper.getFactory().createParser(json);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        SettableBeanProperty prop = mock(SettableBeanProperty.class);

        when(typeDeserializer.getPropertyName()).thenReturn(PropertyName.construct("typeId"));

        Object result = handler._deserialize(parser, context, 0, "type1");

        assertEquals(prop.getType().getClass(), result.getClass());
        assertEquals(1, result);

        verify(parser, times(3)).nextToken();
        verifyNoMoreInteractions(parser);
        verify(context, times(1)).handleUnexpectedValue(any(JsonParser.class), eq("type1"), anyInt(), any(JsonDeserializer.class));
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        String json = "{ \"typeId\": \"type1\", \"value\": 1 }";

        when(parser.getText()).thenReturn("type1");

        Object bean = new Object();
        SettableBeanProperty prop = mock(SettableBeanProperty.class);

        handler.handleTypePropertyValue(parser, context, "p1", bean);

        handler._deserializeAndSet(parser, context, bean, 0, "type1");

        verify(parser, times(3)).nextToken();
        verifyNoMoreInteractions(parser);

        assertEquals(1, prop.get(bean));
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingExternalProperty() throws Exception {
        handler.complete(parser, context, null);

        verify(parser, times(2)).nextToken();
        verify(parser).nextToken();

        throw new JsonMappingException("Missing property 'p3' for external type id 'type3'");
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingTypeIdProperty() throws Exception {
        handler.handlePropertyValue(parser, context, "p1", null);

        handler.complete(parser, context, null);

        verify(parser, times(2)).nextToken();
        verify(parser).nextToken();

        throw new JsonMappingException("Missing external type id property 'typePropertyName'");
    }
}