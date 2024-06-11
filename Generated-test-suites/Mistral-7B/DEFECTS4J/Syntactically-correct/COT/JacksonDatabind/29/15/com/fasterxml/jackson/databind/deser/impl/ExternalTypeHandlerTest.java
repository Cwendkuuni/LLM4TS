package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.PropertyValueBuffer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializationContext;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private List<ExtTypedProperty> properties;
    private Map<String, Integer> nameToPropertyIndex;

    @Before
    public void setUp() {
        properties = new ArrayList<>();
        nameToPropertyIndex = new HashMap<>();

        SettableBeanProperty prop1 = mock(SettableBeanProperty.class);
        when(prop1.getName()).thenReturn(new PropertyName("prop1"));
        SettableBeanProperty prop2 = mock(SettableBeanProperty.class);
        when(prop2.getName()).thenReturn(new PropertyName("prop2"));

        properties.add(new ExtTypedProperty(prop1, null));
        properties.add(new ExtTypedProperty(prop2, mock(TypeDeserializer.class)));

        handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]),
                nameToPropertyIndex, null, null);
    }

    @Test
    public void testStart() {
        ExternalTypeHandler newHandler = handler.start();
        assertNotNull(newHandler);
    }

    @Test
    public void testHandleTypePropertyValue_WithNullTypeId() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.handleTypePropertyValue(parser, context, "prop1", bean);
        assertFalse(handler.hasNext());
    }

    @Test
    public void testHandleTypePropertyValue_WithValidTypeId() throws IOException {
        String validTypeId = "validTypeId";
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        when(parser.getText()).thenReturn(validTypeId);

        handler.handleTypePropertyValue(parser, context, "prop1", bean);
        assertTrue(handler.hasNext());
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleTypePropertyValue_WithInvalidPropertyName() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.handleTypePropertyValue(parser, context, "invalidPropertyName", bean);
    }

    @Test
    public void testHandlePropertyValue_WithNullTypePropertyName() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.handlePropertyValue(parser, context, "prop1", bean);
        assertFalse(handler.hasNext());
    }

    @Test
    public void testHandlePropertyValue_WithValidTypePropertyName() throws IOException {
        String validTypePropertyName = "validTypePropertyName";
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        when(parser.getText()).thenReturn(validTypePropertyName);

        handler.handlePropertyValue(parser, context, "prop1", bean);
        assertTrue(handler.hasNext());
    }

    @Test(expected = JsonMappingException.class)
    public void testHandlePropertyValue_WithInvalidPropertyName() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.handlePropertyValue(parser, context, "invalidPropertyName", bean);
    }

    @Test
    public void testComplete_WithNullBean() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        handler.complete(parser, context, null);
        assertNull(parser.getCurrentName());
    }

    @Test
    public void testComplete_WithValidBean() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Object bean = mapper.readValue(
                "{ \"prop1\": \"typeId1\", \"prop2\": { \"name\": \"John Doe\" } }", Object.class);

        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = new StdDeserializationContext();

        when(parser.getCurrentName()).thenReturn(null);

        handler.complete(parser, context, bean);
        assertNotNull(parser.getCurrentName());
        assertTrue(parser.getCurrentName().isTextual());
        assertEquals("prop1", parser.getCurrentName().asText());
    }

    @Test(expected = JsonMappingException.class)
    public void testComplete_WithMissingExternalTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object bean = mapper.readValue("{ \"prop1\": {} }", Object.class);

        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = new StdDeserializationContext();

        handler.complete(parser, context, bean);
    }

    @Test(expected = JsonMappingException.class)
    public void testComplete_WithMissingProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object bean = mapper.readValue("{ \"prop2\": {} }", Object.class);

        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = new StdDeserializationContext();

        handler.complete(parser, context, bean);

        AnnotatedClass ac = mock(AnnotatedClass.class);
        AnnotatedMember am = mock(AnnotatedMember.class);

        when(ac.getAnnotations()).thenReturn(new AnnotatedClass.ClassAnnotations(As.EXTERNAL_PROPERTY));
        when(am.getAnnotations()).thenReturn(new AnnotatedMember.MemberAnnotations(ac));
        when(ac.findDeclaredMember(PropertyName.construct("prop1"))).thenReturn(am);

        handler.complete(parser, context, null, ac, am);
    }

    @Test(expected = JsonMappingException.class)
    public void testComplete_WithInvalidTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object bean = mapper.readValue("{ \"prop1\": \"invalidTypeId\" }", Object.class);

        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = new StdDeserializationContext();

        handler.complete(parser, context, bean);
    }

    @Test
    public void testComplete_WithNullTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object bean = mapper.readValue("{ \"prop1\": { \"@class\": \"InvalidClass\" } }", Object.class);

        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = new StdDeserializationContext();

        handler.complete(parser, context, bean);

        AnnotatedClass ac = mock(AnnotatedClass.class);
        AnnotatedMember am = mock(AnnotatedMember.class);

        when(ac.getAnnotations()).thenReturn(new AnnotatedClass.ClassAnnotations(As.EXTERNAL_PROPERTY));
        when(am.getAnnotations()).thenReturn(new AnnotatedMember.MemberAnnotations(ac));
        when(ac.findDeclaredMember(PropertyName.construct("prop1"))).thenReturn(am);

        handler.complete(parser, context, null, ac, am, null);
    }

    @Test
    public void testComplete_WithValidTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object bean = mapper.readValue("{ \"prop1\": { \"@class\": \"ValidClass\" } }", Object.class);

        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = new StdDeserializationContext();
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);

        handler.complete(parser, context, bean, buffer, creator);

        assertNull(parser.getCurrentName());
    }
}