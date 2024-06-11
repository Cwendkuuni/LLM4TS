package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.test.BASE_TEST_CASE;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CreatorPropertyTest extends BASE_TEST_CASE {

    private ObjectMapper objectMapper;

    @BeforeClass
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreatorPropertyWithNormalDeserialization() throws JsonProcessingException {
        // Given
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                TypeFactory.defaultInstance.constructType(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                PropertyMetadata.STANDARD_EMPTY);

        // When
        String value = "Hello World";
        Object target = new Object();
        property.set(target, value);

        // Then
        assertEquals("Hello World", property.get(target));
    }

    @Test
    public void testCreatorPropertyWithInjectableValue() throws JsonProcessingException {
        // Given
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                TypeFactory.defaultInstance.constructType(String.class),
                null,
                null,
                null,
                mock(AnnotatedParameter.class),
                0,
                "injectedValueId",
                PropertyMetadata.STANDARD_EMPTY);

        DeserializationContext context = mock(DeserializationContext.class);
        Object target = new Object();

        when(context.findInjectableValue("injectedValueId", property, target)).thenReturn("Injected Value");

        // When
        property.inject(context, target);

        // Then
        assertEquals("Injected Value", property.get(target));
        verify(context).findInjectableValue("injectedValueId", property, target);
    }

    @Test(expectedExceptions = InvalidDefinitionException.class)
    public void testCreatorPropertyMissingSetterShouldThrowException() throws JsonProcessingException {
        // Given
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                TypeFactory.defaultInstance.constructType(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                PropertyMetadata.STANDARD_EMPTY);

        Object target = new Object();

        // When
        property.deserializeAndSet(mock(JsonParser.class), this.getContext(), target);

        // Then - This should throw InvalidDefinitionException
    }

    @Test
    public void testCreatorPropertyWithFallbackSetter() throws JsonProcessingException {
        // Given
        CreatorProperty fallbackSetter = new CreatorProperty(
                PropertyName.construct("testFallbackSetter"),
                TypeFactory.defaultInstance.constructType(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                PropertyMetadata.STANDARD_EMPTY);

        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                TypeFactory.defaultInstance.constructType(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                PropertyMetadata.STANDARD_EMPTY);
        property.setFallbackSetter(fallbackSetter);

        Object target = new Object();

        // When
        String value = "Hello World";
        property.set(target, value);

        // Then
        assertEquals("Hello World", fallbackSetter.get(target));
        assertEquals("Hello World", property.get(target));
    }

    @Test
    public void testCreatorPropertyIsIgnorable() throws JsonProcessingException {
        // Given
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                TypeFactory.defaultInstance.constructType(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                PropertyMetadata.STANDARD_EMPTY);
        property.markAsIgnorable();

        Object target = new Object();
        String value = "Hello World";

        // When
        property.set(target, value);

        // Then
        assertFalse(property.isIgnorable());

        // When - Setting the value again
        property.set(target, value);

        // Then
        assertTrue(property.isIgnorable());
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testCreatorPropertyWithNullInjectableValueShouldThrowException()
            throws JsonProcessingException {
        // Given
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                TypeFactory.defaultInstance.constructType(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                PropertyMetadata.STANDARD_EMPTY);

        DeserializationContext context = mock(DeserializationContext.class);
        Object target = new Object();

        when(context.findInjectableValue("injectedValueId", property, target)).thenReturn(null);

        // When
        property.inject(context, target);

        // Then - This should throw JsonMappingException
    }

    @Test
    public void testCreatorPropertyWithJsonNodeInjectableValue() throws JsonProcessingException {
        // Given
        CreatorProperty property = new CreatorProperty(
                PropertyName.construct("testProperty"),
                TypeFactory.defaultInstance.constructType(String.class),
                null,
                null,
                null,
                null,
                -1,
                null,
                PropertyMetadata.STANDARD_EMPTY);

        DeserializationContext context = mock(DeserializationContext.class);
        Object target = new Object();
        JsonNode node = JsonNode.valueOf("{\"injectedValue\":\"Value\"}");

        when(context.findInjectableValue("injectedValueId", property, target)).thenReturn(node.get("injectedValue"));

        // When
        property.inject(context, target);

        // Then
        assertEquals("Value", property.get(target));
        verify(context).findInjectableValue("injectedValueId", property, target);
    }
}