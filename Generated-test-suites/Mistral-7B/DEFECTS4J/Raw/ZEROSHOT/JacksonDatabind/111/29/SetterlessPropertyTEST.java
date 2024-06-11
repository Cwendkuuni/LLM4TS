package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SetterlessPropertyTest {

    private SetterlessProperty setterlessProperty;
    private AnnotatedMethod annotatedMethod;
    private Object instance;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;

    @Before
    public void setup() {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        annotatedMethod = mock(AnnotatedMethod.class);
        instance = mock(Object.class);
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        setterlessProperty = new SetterlessProperty(
                Mockito.mock(BeanPropertyDefinition.class),
                Mockito.mock(JavaType.class),
                typeDeserializer,
                Annotations.EMPTY_ANNOTATIONS,
                annotatedMethod
        );
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(annotatedMethod.getMethod()).thenReturn(Mockito.mock(Method.class));

        JsonToken jsonToken = JsonToken.VALUE_STRING;
        String expectedValue = "testValue";
        when(jsonParser.getCurrentToken()).thenReturn(jsonToken);
        when(jsonParser.getValueAsString()).thenReturn(expectedValue);

        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(instance).getClass();
        verify(annotatedMethod).getMethod().invoke(instance, (Object[]) null);
        verify(jsonParser).nextToken();
        assertEquals(expectedValue, annotatedMethod.getMember().getValue(instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        setterlessProperty.set(instance, new Object());
    }
}