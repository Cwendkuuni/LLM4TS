package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * Test cases for the {@link ExternalTypeHandler} class.
 */
public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = createTestHandler();
        String propName = "typeIdProperty";
        String expectedTypeId = "expectedTypeId";
        Object testBean = new Object();

        JsonParser mockParser = createMockParser();
        mockParser.setValue(propName, expectedTypeId);

        assertTrue(handler.handleTypePropertyValue(mockParser, new DeserializationContext(), propName, testBean));
        Assert.assertEquals(expectedTypeId, handler.getTypeId(propName));

        // Test with null property name and bean
        assertFalse(handler.handleTypePropertyValue(mockParser, new DeserializationContext(), null, null));

        // Test with non-existent property name
        propName = "nonExistentProperty";
        assertFalse(handler.handleTypePropertyValue(mockParser, new DeserializationContext(), propName, testBean));

        // Test with property that does not match type property name
        propName = "anotherProperty";
        SettableBeanProperty anotherProperty = new SimpleBeanProperty("anotherProperty", String.class);
        handler.addProperty(anotherProperty);
        assertFalse(handler.handleTypePropertyValue(mockParser, new DeserializationContext(), propName, testBean));

        // Test with null typeDeserializer
        TypeDeserializer nullTypeDeserializer = null;
        SettableBeanProperty typeProperty = new SimpleBeanProperty("typeIdProperty", String.class);
        handler.addProperty(typeProperty, nullTypeDeserializer);
        mockParser.setValue(propName, expectedTypeId);
        assertFalse(handler.handleTypePropertyValue(mockParser, new DeserializationContext(), propName, testBean));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = createTestHandler();
        String propName = "valueProperty";
        String expectedValue = "expectedValue";
        Object testBean = new Object();

        JsonParser mockParser = createMockParser();
        TokenBuffer tokenBuffer = new TokenBuffer(mockParser);
        tokenBuffer.copyCurrentStructure(mockParser);

        handler.handlePropertyValue(mockParser, new DeserializationContext(), propName, testBean);
        Object actualValue = handler.getPropertyValue(propName);

        Assert.assertNull(actualValue);

        // Test with non-existent property name
        propName = "nonExistentProperty";
        assertNull(handler.getPropertyValue(propName));

        // Test with property that does not match property name
        propName = "anotherProperty";
        SettableBeanProperty anotherProperty = new SimpleBeanProperty("anotherProperty", String.class);
        handler.addProperty(anotherProperty);
        tokenBuffer.writeString(anotherProperty.getName());
        handler.handlePropertyValue(mockParser, new DeserializationContext(), propName, testBean);
        actualValue = handler.getPropertyValue(propName);

        Assert.assertNull(actualValue);

        // Test with value being set
        tokenBuffer.writeValue(expectedValue);
        handler.handlePropertyValue(mockParser, new DeserializationContext(), propName, testBean);
        actualValue = handler.getPropertyValue(propName);

        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler handler = createTestHandler();

        Object testBean = new Object();

        JsonParser mockParser = createMockParser();

        handler.complete(mockParser, new DeserializationContext(), testBean);

        // Test with no type ids or tokens
        for (int i = 0; i < handler._properties.length; i++) {
            assertNull(handler._typeIds[i]);
            assertNull(handler._tokens[i]);
        }

        // Test with missing type id but a token
        String missingTypeIdPropName = "missingTypeIdProperty";
        SettableBeanProperty missingTypeIdProperty = new SimpleBeanProperty(missingTypeIdPropName, String.class);
        handler.addProperty(missingTypeIdProperty);
        mockParser.setValue(missingTypeIdPropName, "missingTypeId");
        handler.handlePropertyValue(mockParser, new DeserializationContext(), missingTypeIdPropName, testBean);
        handler.complete(mockParser, new DeserializationContext(), testBean);
        String missingTypeId = handler.getTypeId(missingTypeIdPropName);

        Assert.assertEquals("missingTypeId", missingTypeId);

        // Test with missing property but a type id
        String missingPropertyTypeId = "missingPropertyTypeId";
        TypeDeserializer missingPropertyTypeDeserializer = new SimpleTypeDeserializer("missingPropertyTypeId", String.class);
        handler.addProperty(new SimpleBeanProperty("property", String.class), missingPropertyTypeDeserializer);
        handler._typeIds[handler.indexOfProperty("property")] = missingPropertyTypeId;
        handler.complete(mockParser, new DeserializationContext(), testBean);
        JsonToken missingPropertyToken = handler._tokens[handler.indexOfProperty("property")];

        // Verify that the type id and token have been deserialized
        String propertyValue = handler.getPropertyValue("property");
        Assert.assertEquals("property", propertyValue);
        Assert.assertEquals(JsonToken.START_OBJECT, missingPropertyToken.tokenTypeId());

        // Test with missing both type id and property
        String missingBothTypeIdAndProperty = "missingBothTypeIdAndProperty";
        handler.addProperty(new SimpleBeanProperty(missingBothTypeIdAndProperty, String.class));
        handler.complete(mockParser, new DeserializationContext(), testBean);
        String missingBothTypeIdAndPropertyTypeId = handler.getTypeId(missingBothTypeIdAndProperty);

        Assert.assertNull(missingBothTypeIdAndPropertyTypeId);

        // Test with non-null type id and null property value
        String nonNullTypeIdPropertyName = "nonNullTypeIdProperty";
        SettableBeanProperty nonNullTypeIdProperty = new SimpleBeanProperty(nonNullTypeIdPropertyName, String.class);
        handler.addProperty(nonNullTypeIdProperty);
        handler._typeIds[handler.indexOfProperty(nonNullTypeIdPropertyName)] = "nonNullTypeId";
        handler.complete(mockParser, new DeserializationContext(), testBean);
        String nonNullTypeId = handler.getTypeId(nonNullTypeIdPropertyName);

        Assert.assertEquals("nonNullTypeId", nonNullTypeId);
        Assert.assertNull(handler.getPropertyValue(nonNullTypeIdPropertyName));

        // Test with null type id and non-null property value
        String nullTypeIdPropertyName = "nullTypeIdProperty";
        handler.addProperty(new SimpleBeanProperty(nullTypeIdPropertyName, String.class));
        handler._typeIds[handler.indexOfProperty(nullTypeIdPropertyName)] = null;
        handler.handlePropertyValue(mockParser, new DeserializationContext(), nullTypeIdPropertyName, testBean);
        handler.complete(mockParser, new DeserializationContext(), testBean);
        String nullTypeId = handler.getTypeId(nullTypeIdPropertyName);

        Assert.assertNull(nullTypeId);
        String nullTypeIdPropertyValue = handler.getPropertyValue(nullTypeIdPropertyName);
        Assert.assertEquals("null", nullTypeIdPropertyValue);

        // Test with both null type id and property value
        String bothNullTypeIdAndProperty = "bothNullTypeIdAndProperty";
        handler.addProperty(new SimpleBeanProperty(bothNullTypeIdAndProperty, String.class));
        handler.complete(mockParser, new DeserializationContext(), testBean);
        String bothNullTypeIdAndPropertyTypeId = handler.getTypeId(bothNullTypeIdAndProperty);
        String bothNullTypeIdAndPropertyValue = handler.getPropertyValue(bothNullTypeIdAndProperty);

        Assert.assertNull(bothNullTypeIdAndPropertyTypeId);
        Assert.assertEquals("null", bothNullTypeIdAndPropertyValue);
    }

    @Test
    public void testCompleteWithPropertyBasedCreator() throws IOException {
        ExternalTypeHandler handler = createTestHandler();

        Object testBean = new Object();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new SimplePropertyBasedCreator();

        JsonParser mockParser = createMockParser();

        handler.complete(mockParser, new DeserializationContext(), buffer, creator);

        // Test with no type ids or tokens
        for (int i = 0; i < handler._properties.length; i++) {
            assertNull(handler._typeIds[i]);
            assertNull(handler._tokens[i]);
        }

        // Test with missing both type id and property
        String missingBothTypeIdAndProperty = "missingBothTypeIdAndProperty";
        SettableBeanProperty missingBothTypeIdAndPropertyProperty = new SimpleBeanProperty(missingBothTypeIdAndProperty, String.class);
        handler.addProperty(missingBothTypeIdAndPropertyProperty);
        handler.complete(mockParser, new DeserializationContext(), buffer, creator);

        // Test that property value is assigned to buffer
        Assert.assertEquals(missingBothTypeIdAndProperty, buffer.getParameterName(0));
        String missingBothTypeIdAndPropertyValue = (String) buffer.getParameterValue(0);
        Assert.assertEquals("null", missingBothTypeIdAndPropertyValue);

        // Test with missing property but a type id
        String missingPropertyTypeId = "missingPropertyTypeId";
        TypeDeserializer missingPropertyTypeDeserializer = new SimpleTypeDeserializer("missingPropertyTypeId", String.class);
        handler.addProperty(new SimpleBeanProperty("property", String.class), missingPropertyTypeDeserializer);
        handler._typeIds[handler.indexOfProperty("property")] = "missingPropertyTypeId";
        handler._deserialize(mockParser, new DeserializationContext(), handler.indexOfProperty("property"), "missingPropertyTypeId");
        handler.complete(mockParser, new DeserializationContext(), buffer, creator);

        // Test that property value is assigned to creator
        String propertyValue = (String) creator.build(new DeserializationContext(), buffer).getPropertyValue("property");
        Assert.assertEquals("property", propertyValue);

        // Test with both missing type id and property
        String bothMissingTypeIdAndProperty = "bothMissingTypeIdAndProperty";
        handler.addProperty(new SimpleBeanProperty(bothMissingTypeIdAndProperty, String.class));
        handler.complete(mockParser, new DeserializationContext(), buffer, creator);

        // Test that property value is assigned to bean
        String bothMissingTypeIdAndPropertyValue = (String) testBean.getClass().getDeclaredField(bothMissingTypeIdAndProperty).get(testBean);
        Assert.assertEquals("null", bothMissingTypeIdAndPropertyValue);

        // Test with non-null type id and null property value
        String nonNullTypeIdPropertyName = "nonNullTypeIdProperty";
        SettableBeanProperty nonNullTypeIdProperty = new SimpleBeanProperty(nonNullTypeIdPropertyName, String.class);
        handler.addProperty(nonNullTypeIdProperty);
        handler._typeIds[handler.indexOfProperty(nonNullTypeIdPropertyName)] = "nonNullTypeId";
        handler._deserialize(mockParser, new DeserializationContext(), handler.indexOfProperty(nonNullTypeIdPropertyName), "nonNullTypeId");
        handler.complete(mockParser, new DeserializationContext(), buffer, creator);

        // Test that type id is assigned to creator
        String nonNullTypeId = (String) creator.build(new DeserializationContext(), buffer).getPropertyValue(nonNullTypeIdPropertyName);
        Assert.assertEquals("nonNullTypeId", nonNullTypeId);

        // Test that property value is assigned to bean
        String nonNullTypeIdPropertyValue = (String) testBean.getClass().getDeclaredField(nonNullTypeIdPropertyName).get(testBean);
        Assert.assertNull(nonNullTypeIdPropertyValue);

        // Test with null type id and non-null property value
        String nullTypeIdPropertyName = "nullTypeIdProperty";
        handler.addProperty(new SimpleBeanProperty(nullTypeIdPropertyName, String.class));
        handler._typeIds[handler.indexOfProperty(nullTypeIdPropertyName)] = null;
        handler._deserialize(mockParser, new DeserializationContext(), handler.indexOfProperty(nullTypeIdPropertyName), "value");
        handler.complete(mockParser, new DeserializationContext(), buffer, creator);

        // Test that property value is assigned to bean
        String nullTypeIdPropertyValue = (String) testBean.getClass().getDeclaredField(nullTypeIdPropertyName).get(testBean);
        Assert.assertEquals("value", nullTypeIdPropertyValue);

        // Test with both null type id and property value
        String bothNullTypeIdAndProperty = "bothNullTypeIdAndProperty";
        handler.addProperty(new SimpleBeanProperty(bothNullTypeIdAndProperty, String.class));
        handler.complete(mockParser, new DeserializationContext(), buffer, creator);

        // Test that property value is assigned to bean
        String bothNullTypeIdAndPropertyValue = (String) testBean.getClass().getDeclaredField(bothNullTypeIdAndProperty).get(testBean);
        Assert.assertEquals("null", bothNullTypeIdAndPropertyValue);
    }

    private ExternalTypeHandler createTestHandler() {
        ExternalTypeHandler handler = new ExternalTypeHandler();
        handler.addProperty(new SimpleBeanProperty("typeIdProperty", String.class));
        handler.addProperty(new SimpleBeanProperty("valueProperty", String.class));
        handler.addProperty(new SimpleBeanProperty("property", String.class), new SimpleTypeDeserializer("property", String.class));
        return handler;
    }

    private JsonParser createMockParser() {
        return new JsonParser() {
            @Override
            public boolean nextToken() throws IOException {
                return true;
            }

            @Override
            public void close() throws IOException {

            }

            @Override
            public void close() {

            }

            @Override
            public void setValue(String name, Object value) {

            }

            @Override
            public void writeValueAsString(Object value) {

            }

            @Override
            public void writeValueAsString(String text) {

            }

            @Override
            public void writeValueAsArray(Object value) {

            }

            @Override
            public void writeValueAsArray(Object values) {

            }

            @Override
            public void writeObjectAsValue(Object value) {

            }

            @Override
            public void writeStartObject() {

            }

            @Override
            public void writeStartArray() {

            }

            @Override
            public void writeFieldName(String name) {

            }

            @Override
            public void writeNumber(double value) {

            }

            @Override
            public void writeValue(Object value) {

            }

            @Override
            public void writeNull() {

            }

            @Override
            public void writeRawValue(String text) {

            }

            @Override
            public void writeBoolean(boolean b) {

            }

            @Override
            public void writeStartArray() {
                // Do nothing
            }

            @Override
            public void writeEndArray() {

            }

            @Override
            public void writeEndObject() {

            }
        };
    }
}