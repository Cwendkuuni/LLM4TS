package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler.Builder builder;

    @Before
    public void setup() {
        builder = new ExternalTypeHandler.Builder();
    }

    @Test
    public void testHandleTypePropertyValue_WhenPropertyIndexIsNull_ThenReturnFalse() {
        // Arrange
        ExternalTypeHandler handler = builder.build();
        String propName = "type";
        Object bean = new Object();

        // Act
        boolean result = handler.handleTypePropertyValue(null, null, propName, bean);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHandleTypePropertyValue_WhenTypePropertyNameDoesNotMatch_ThenReturnFalse() {
        // Arrange
        ExternalTypeHandler handler = builder.build();
        String propName = "type";
        Object bean = new Object();
        builder.addExternal(new SettableBeanProperty(), new TypeDeserializer()); // Add a property that does not match

        // Act
        boolean result = handler.handleTypePropertyValue(null, null, propName, bean);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue_WhenPropertyIndexIsNull_ThenReturnFalse() {
        // Arrange
        ExternalTypeHandler handler = builder.build();
        String propName = "value";
        Object bean = new Object();

        // Act
        boolean result = handler.handlePropertyValue(null, null, propName, bean);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue_WhenTypePropertyNameMatches_ThenReturnTrue() {
        // Arrange
        ExternalTypeHandler handler = builder.build();
        String propName = "type";
        Object bean = new Object();
        builder.addExternal(new SettableBeanProperty(), new TypeDeserializer()); // Add a property that matches

        // Act
        boolean result = handler.handlePropertyValue(null, null, propName, bean);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHandlePropertyValue_WhenTypePropertyNameDoesNotMatch_ThenReturnTrue() {
        // Arrange
        ExternalTypeHandler handler = builder.build();
        String propName = "value";
        Object bean = new Object();
        builder.addExternal(new SettableBeanProperty(), new TypeDeserializer()); // Add a property that does not match

        // Act
        boolean result = handler.handlePropertyValue(null, null, propName, bean);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testComplete_WhenTypeIsNull_ThenReportMappingException() {
        // Arrange
        ExternalTypeHandler handler = builder.build();
        handler.addExternal(new SettableBeanProperty(), new TypeDeserializer());
        Object bean = new Object();

        // Act & Assert
        try {
            handler.complete(null, null, bean);
            fail("Expected MappingException was not thrown");
        } catch (IOException e) {
            assertEquals("Missing external type id property 'type'", e.getMessage());
        }
    }

    @Test
    public void testComplete_WhenTokensIsNull_ThenReportMappingException() {
        // Arrange
        ExternalTypeHandler handler = builder.build();
        handler.addExternal(new SettableBeanProperty(), new TypeDeserializer());
        Object bean = new Object();

        // Act & Assert
        try {
            handler.complete(null, null, bean);
            fail("Expected MappingException was not thrown");
        } catch (IOException e) {
            assertEquals("Missing property 'value' for external type id 'type'", e.getMessage());
        }
    }

    @Test
    public void testComplete_WhenTypeAndTokensAreNotNull_ThenDeserializeAndSet() {
        // Arrange
        ExternalTypeHandler handler = builder.build();
        handler.addExternal(new SettableBeanProperty(), new TypeDeserializer());
        Object bean = new Object();

        // Act
        Object result = handler.complete(null, null, bean);

        // Assert
        assertNotNull(result);
    }
}