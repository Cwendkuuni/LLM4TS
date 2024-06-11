package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

/**
 * This is a test class for the SetterlessProperty class.
 */
public class SetterlessPropertyTest {

    @Test
    public void testSetterlessProperty() throws Exception {
        // Create mocks
        JsonParser mockParser = mock(JsonParser.class);
        DeserializationContext mockContext = mock(DeserializationContext.class);
        Object mockInstance = mock(Object.class);
        TypeDeserializer mockTypeDeserializer = mock(TypeDeserializer.class);
        NullValueProvider mockNullValueProvider = mock(NullValueProvider.class);
        TypeDeserializer mockValueTypeDeserializer = mock(TypeDeserializer.class);
        AnnotatedMethod mockAnnotatedMethod = mock(AnnotatedMethod.class);
        Method mockGetter = mock(Method.class);
        Annotations mockContextAnnotations = mock(Annotations.class);
        BeanPropertyDefinition mockPropertyDefinition = mock(BeanPropertyDefinition.class);
        Object mockValue = createTestValue();

        // Create SetterlessProperty instance
        SetterlessProperty setterlessProperty = new SetterlessProperty(mockPropertyDefinition, mockType, mockTypeDeserializer, mockContextAnnotations, mockAnnotatedMethod) {
            @Override
            protected Method getter() {
                return mockGetter;
            }
        };

        // Test basic functionality
        assertSame(mockPropertyDefinition, setterlessProperty.getPropertyDefinition());
        assertSame(mockType, setterlessProperty.getType());
        assertSame(mockTypeDeserializer, setterlessProperty.getTypeDeserializer());
        assertSame(mockContextAnnotations, setterlessProperty.getContextAnnotations());
        assertSame(mockAnnotatedMethod, setterlessProperty.getMember());
        assertSame(mockGetter, setterlessProperty.getGetter());

        // Test withValueDeserializer method
        JsonDeserializer<Object> mockValueDeserializer = mock(JsonDeserializer.class);
        SettableBeanProperty settableBeanProperty = spy(setterlessProperty);
        SettableBeanProperty newSetterlessProperty = settableBeanProperty.withValueDeserializer(mockValueDeserializer);
        assertSame(mockValueDeserializer, newSetterlessProperty.getValueDeserializer());

        // Test withNullProvider method
        NullValueProvider newNullValueProvider = spy(mockNullValueProvider);
        SettableBeanProperty newSetterlessProperty2 = settableBeanProperty.withNullProvider(newNullValueProvider);
        assertSame(newNullValueProvider, newSetterlessProperty2.getNullProvider());

        // Test fixAccess method
        setterlessProperty.fixAccess(mockContext);
        verify(mockAnnotatedMethod).fixAccess(mockContext.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));

        // Test deserializeAndSet method
        when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getValueAsString()).thenReturn("testValue");
        when(mockGetter.invoke(mockInstance, (Object[]) null)).thenReturn(new ArrayList<>());
        setterlessProperty.deserializeAndSet(mockParser, mockContext, mockInstance);
        verify(mockValueDeserializer).deserialize(mockParser, mockContext, new ArrayList<>());

        // Test deserializeSetAndReturn method
        when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        setterlessProperty.deserializeAndSet(mockParser, mockContext, mockInstance);
        verifyZeroInteractions(mockValueDeserializer);

        // Test set method
        setterlessProperty.set(mockInstance, mockValue);
        verify(mockInstance).getClass(); // Verify instance is not null
        verify(mockInstance).getClass().getMethod(getName()).invoke(mockInstance, mockValue);

        // Test setAndReturn method
        when(mockParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockParser.getValueAsString()).thenReturn("testValue");
        Object returnValue = mockInstance;
        when(mockInstance.getClass()).thenReturn(Map.class);
        when(mockInstance.getClass().getMethod(getName())).then((invocationOnMock) -> {
            Object value = invocationOnMock.getArguments()[0];
            Map<String, Object> map = (Map<String, Object>) mockInstance;
            map.put("testKey", value);
            returnValue = map;
            return null;
        });
        when(mockParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
        Object result = setterlessProperty.deserializeSetAndReturn(mockParser, mockContext, mockInstance);
        verify(mockParser).nextToken();
        verify(mockValueDeserializer).deserialize(mockParser, mockContext, result);
        assertSame(returnValue, result);

        // Test getAnnotation method
        assertSame(mockAnnotations.findAnnotation(mockParser.getCurrentName(), JsonProperty.class), setterlessProperty.getAnnotation(JsonProperty.class));

        // Test getMember method
        assertSame(mockAnnotatedMethod, setterlessProperty.getMember());
    }

    private Object createTestValue() {
        return new HashMap<String, Object>() {{
            put("key1", "value1");
            put("key2", "value2");
        }};
    }

    private String getName() {
        return "testProperty";
    }
}