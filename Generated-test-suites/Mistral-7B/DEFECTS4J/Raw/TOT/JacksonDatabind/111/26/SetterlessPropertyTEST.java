package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.StdConverter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SetterlessPropertyTest {

    private ObjectNode objectNode;
    private JsonParser jsonParser;
    private Object testInstance;
    private SettableBeanProperty setterlessProperty;
    private TypeDeserializer typeDeserializer;
    private JsonDeserializer<List<String>> valueDeserializer;
    private NullValueProvider nullValueProvider;

    @Before
    public void setup() throws Exception {
        this.objectNode = Mockito.mock(ObjectNode.class);
        this.jsonParser = Mockito.mock(JsonParser.class);
        this.testInstance = new HashMap<>();
        this.setterlessProperty = new SetterlessProperty(
                Mockito.mock(BeanPropertyDefinition.class),
                Mockito.mock(JavaType.class),
                typeDeserializer,
                Mockito.mock(Annotations.class),
                Mockito.mock(AnnotatedMethod.class)
        );
        this.typeDeserializer = Mockito.mock(TypeDeserializer.class);
        this.valueDeserializer = mock(JsonDeserializer.class);
        this.nullValueProvider = mock(NullValueProvider.class);

        doReturn(this.valueDeserializer).when(setterlessProperty).withValueDeserializer(any());
        doReturn(this.nullValueProvider).when(setterlessProperty).withNullProvider(any());
    }

    @Test
    public void testDeserializeAndSetSuccess() throws IOException, JsonProcessingException {
        // Given
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn("testValue");
        when(typeDeserializer.deserializeTypedFromObject(eq(jsonParser), any(), any()))
                .thenReturn(Arrays.asList("testValue"));

        // When
        setterlessProperty.deserializeAndSet(jsonParser, Mockito.mock(DeserializationContext.class), testInstance);

        // Then
        verify(testInstance).getClass(); // testInstance is a Map
        AccessibleObject accessibleObject = this.testInstance.getClass().getDeclaredMethod("put", Object.class, Object.class);
        accessibleObject.setAccessible(true);
        Object initialValue = accessibleObject.get(testInstance, "testKey");
        assertEquals(Arrays.asList("testValue"), initialValue);
    }

    @Test
    public void testDeserializeAndSetNull() throws IOException, JsonProcessingException {
        // Given
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        // When
        setterlessProperty.deserializeAndSet(jsonParser, Mockito.mock(DeserializationContext.class), testInstance);

        // Then
        verify(testInstance, never()).getClass(); // testInstance is not a Map
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        // Given

        // When
        setterlessProperty.set(testInstance, "testValue");

        // Then
        // UnsupportedOperationException is expected
    }
}