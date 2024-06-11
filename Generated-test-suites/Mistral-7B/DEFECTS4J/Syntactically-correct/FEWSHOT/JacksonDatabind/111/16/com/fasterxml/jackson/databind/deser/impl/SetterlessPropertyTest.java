package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SetterlessPropertyTest {

    @Mock
    private JsonParser mockJsonParser;

    @Mock
    private DeserializationContext mockContext;

    @Mock
    private Object mockInstance;

    @Mock
    private TypeDeserializer mockTypeDeserializer;

    @Mock
    private NullValueProvider mockNullValueProvider;

    @InjectMocks
    private SetterlessProperty setterlessProperty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mockJsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(mockJsonParser.nextValue()).thenReturn("[]");

        when(mockJsonParser.getValueAsToken()).thenReturn(JsonToken.START_ARRAY);
        when(mockJsonParser.nextValue()).thenReturn(JsonToken.VALUE_NULL);
        when(mockJsonParser.nextValue()).thenReturn(JsonToken.END_ARRAY);

        when(mockTypeDeserializer.findDeserializerForEmptyValue(any())).thenReturn(new StdDeserializer<>());
        when(mockTypeDeserializer.findDeserializerForValueType(any(), eq(String.class))).thenReturn(new StdDeserializer<>());

        JavaType javaType = mock(JavaType.class);
        when(javaType.getRawClass()).thenReturn(ArrayList.class);
        when(mockContext.findValueDeserializer(eq(ArrayList.class))).thenReturn(new StdDeserializer<>());

        when(mockContext.findBeanPropertyDefinition(eq(mockAnnotatedMethod))).thenReturn(new BeanPropertyDefinition(mockAnnotatedMethod, javaType, null, null, null, null, null));

        AnnotatedMethod mockAnnotatedMethod = new AnnotatedMethod(mockMethod, Annotations.findAnnotation(mockMethod, JsonProperty.class));
        setterlessProperty = new SetterlessProperty(new BeanPropertyDefinition(mockAnnotatedMethod, javaType, mockTypeDeserializer, Annotations.empty(), mockAnnotatedMethod), javaType, mockTypeDeserializer, Annotations.empty(), mockAnnotatedMethod);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        setterlessProperty.deserializeAndSet(mockJsonParser, mockContext, mockInstance);
        verify(mockInstance, times(1)).invoke(mockMethod, (Object[]) null);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        setterlessProperty.deserializeSetAndReturn(mockJsonParser, mockContext, mockInstance);
        verify(mockInstance, times(1)).invoke(mockMethod, (Object[]) null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        setterlessProperty.set(mockInstance, "value");
    }

    @Test
    public void testSetAndReturn() throws IOException {
        setterlessProperty.setAndReturn(mockInstance, "value");
    }

    @Test
    public void testGetAnnotation() {
        JsonProperty jsonProperty = new JsonProperty();
        AnnotatedMethod mockAnnotatedMethod = new AnnotatedMethod(mockMethod, Annotations.findAnnotation(mockMethod, JsonProperty.class));
        assertEquals(jsonProperty, setterlessProperty.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetMember() {
        assertEquals(mockAnnotatedMethod, setterlessProperty.getMember());
    }
}