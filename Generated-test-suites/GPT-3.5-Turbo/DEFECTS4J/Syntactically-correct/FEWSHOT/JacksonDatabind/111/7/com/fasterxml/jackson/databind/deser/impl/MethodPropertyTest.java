package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MethodPropertyTest {

    @Mock
    private BeanPropertyDefinition beanPropertyDefinition;

    private JavaType javaType;

    @Mock
    private TypeDeserializer typeDeserializer;

    @Mock
    private Annotations contextAnnotations;

    @Mock
    private AnnotatedMethod annotatedMethod;

    @Mock
    private Method method;

    @Mock
    private JsonDeserializer<?> jsonDeserializer;

    @Mock
    private NullValueProvider nullValueProvider;

    @Mock
    private DeserializationConfig deserializationConfig;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    private MethodProperty methodProperty;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        javaType = mock(JavaType.class);

        when(annotatedMethod.getAnnotated()).thenReturn(method);
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        methodProperty = new MethodProperty(beanPropertyDefinition, javaType, typeDeserializer, contextAnnotations, annotatedMethod);
    }

    @Test
    public void testConstructor() {
        assertEquals(beanPropertyDefinition, methodProperty.getMember());
        assertEquals(annotatedMethod, methodProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        methodProperty.deserializeAndSet(jsonParser, deserializationContext, objectMapper);
        // TODO: test the logic of methodProperty.deserializeAndSet
    }

    @Test
    public void testWithNullProvider() {
        MethodProperty methodPropertyWithNullProvider = (MethodProperty) methodProperty.withNullProvider(
                nullValueProvider);
        assertEquals(nullValueProvider, methodPropertyWithNullProvider.orNullValueProvider(null));
    }

    @Test
    public void testFixAccess() {
        methodProperty.fixAccess(deserializationConfig);
        // TODO: test the logic of methodProperty.fixAccess
    }
}