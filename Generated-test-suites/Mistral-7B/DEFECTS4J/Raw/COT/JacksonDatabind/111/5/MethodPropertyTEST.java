package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

/**
 * Test class for MethodProperty.
 */
@RunWith(MockitoJUnitRunner.class)
public class MethodPropertyTest {

    @InjectMocks
    private MethodProperty methodProperty;

    @Mock
    private AnnotatedMember annotated;

    @Mock
    private Method setter;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext context;

    @Mock
    private Object instance;

    @Mock
    private Object value;

    @Mock
    private JsonDeserializer<?> valueDeserializer;

    @Mock
    private NullValueProvider nullValueProvider;

    @Mock
    private TypeDeserializer typeDeserializer;

    @Mock
    private Annotations contextAnnotations;

    @Mock
    private TypeDeserializer valueTypeDeserializer;

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertEquals(BeanPropertyDefinition.class, methodProperty.getBeanPropertyDefinition().getClass());
        assertEquals(JavaType.class, methodProperty.getType().getClass());
        assertEquals(TypeDeserializer.class, methodProperty.getTypeDeserializer().getClass());
        assertEquals(Annotations.class, methodProperty.getContextAnnotations().getClass());
        assertEquals(AnnotatedMethod.class, methodProperty.getAnnotated().getClass());
        assertEquals(Method.class, methodProperty.getSetter().getClass());
    }

    /**
     * Test getAnnotation method.
     */
    @Test
    public void testGetAnnotation() {
        when(annotated.getAnnotation(Annotation.class)).thenReturn(new Object() {});
        assertEquals(new Object(), methodProperty.getAnnotation(Annotation.class));
    }

    /**
     * Test deserializeAndSet method with null input.
     */
    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullInput() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(jsonParser.getCodec()).thenReturn(new Object() {
            @JsonDeserializer.Use(JsonDeserializer.None.class)
            public JsonDeserializer<?> findValueDeserializer(DeserializationConfig config,
                    BeanPropertyPropertyName propertyName, TypeDeserializer vtd) {
                return null;
            }
        });
        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
        when(methodProperty._annotated.getAnnotated()).thenReturn(annotated);
        when(methodProperty._setter.invoke(instance, null)).thenThrow(new InvocationTargetException(new IOException()));

        methodProperty.deserializeAndSet(jsonParser, context, instance);
    }

    /**
     * Test deserializeAndSet method with non-null input.
     */
    @Test
    public void testDeserializeAndSetWithNonNullInput() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(jsonParser.getValueAsString()).thenReturn("test");
        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getCodec()).thenReturn(new Object() {
            @JsonDeserializer.Use(JsonDeserializer.None.class)
            public JsonDeserializer<?> findValueDeserializer(DeserializationConfig config,
                    BeanPropertyPropertyName propertyName, TypeDeserializer vtd) {
                return valueDeserializer;
            }
        });
        when(valueDeserializer.deserialize(jsonParser, context)).thenReturn(value);
        when(methodProperty._annotated.getAnnotated()).thenReturn(annotated);
        when(methodProperty._setter.invoke(instance, value)).thenReturn(value);

        methodProperty.deserializeAndSet(jsonParser, context, instance);
        assertEquals(value, methodProperty.getValue());
    }

    /**
     * Test deserializeSetAndReturn method with null input.
     */
    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithNullInput() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(jsonParser.getCodec()).thenReturn(new Object() {
            @JsonDeserializer.Use(JsonDeserializer.None.class)
            public JsonDeserializer<?> findValueDeserializer(DeserializationConfig config,
                    BeanPropertyPropertyName propertyName, TypeDeserializer vtd) {
                return null;
            }
        });
        when(methodProperty._annotated.getAnnotated()).thenReturn(annotated);

        assertEquals(instance, methodProperty.deserializeSetAndReturn(jsonParser, context, instance));
    }

    /**
     * Test deserializeSetAndReturn method with non-null input.
     */
    @Test
    public void testDeserializeSetAndReturnWithNonNullInput() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(jsonParser.getValueAsString()).thenReturn("test");
        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getCodec()).thenReturn(new Object() {
            @JsonDeserializer.Use(JsonDeserializer.None.class)
            public JsonDeserializer<?> findValueDeserializer(DeserializationConfig config,
                    BeanPropertyPropertyName propertyName, TypeDeserializer vtd) {
                return valueDeserializer;
            }
        });
        when(valueDeserializer.deserialize(jsonParser, context)).thenReturn(value);
        when(methodProperty._annotated.getAnnotated()).thenReturn(annotated);
        when(methodProperty._setter.invoke(instance, value)).thenReturn(value);

        assertEquals(value, methodProperty.deserializeSetAndReturn(jsonParser, context, instance));
    }

    /**
     * Test set method.
     */
    @Test
    public void testSet() throws IOException {
        when(methodProperty._setter.invoke(instance, value)).thenReturn(value);

        methodProperty.set(instance, value);
        assertEquals(value, methodProperty.getValue());
    }

    /**
     * Test set method with exception.
     */
    @Test(expected = IOException.class)
    public void testSetWithException() throws IOException {
        when(methodProperty._setter.invoke(instance, value)).thenThrow(new InvocationTargetException(new IOException()));

        methodProperty.set(instance, value);
    }

    /**
     * Test fixAccess method.
     */
    @Test
    public void testFixAccess() {
        methodProperty.fixAccess(context);
        verify(annotated).fixAccess(context.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }
}