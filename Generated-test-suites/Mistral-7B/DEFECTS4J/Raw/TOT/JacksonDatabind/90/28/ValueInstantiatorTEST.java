package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValueInstantiatorTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Mock
    private DeserializationContext context;
    @Mock
    private AnnotatedClass annotatedClass;
    @Mock
    private AnnotatedField field;
    @Mock
    private TypeDeserializer typeDeserializer;

    @InjectMocks
    private ValueInstantiator valueInstantiator;

    @Before
    public void setUp() {
        when(context.findBeanProperty("field")).thenReturn(field);
        when(annotatedClass.getAnnotatedFields()).thenReturn(new AnnotatedField[0]);
        when(annotatedClass.findAnnotation(ValueInstantiator.ValueClass.class)).thenReturn(null);
        when(annotatedClass.getRawClass()).thenReturn(String.class);
    }

    @Test
    public void testCreateFromObjectWith_ValidArguments_ReturnsInstance() throws Exception {
        // Given
        String json = "{\"field\":\"value\"}";
        when(context.readValueAs(String.class, MAPPER.readerForValueType(String.class))).thenReturn(json);
        when(field.getType()).thenReturn(String.class);
        when(field.getAnnotation(ValueInstantiator.Value.class)).thenReturn(new ValueInstantiator.Value());

        // When
        Object result = valueInstantiator.createFromObjectWith(context, new Object[]{"value"});

        // Then
        assertEquals("value", result);
    }

    @Test
    public void testCreateFromObjectWith_MissingArgument_ThrowsIOException() throws Exception {
        // Given
        String json = "{\"field\":\"value\"}";
        when(context.readValueAs(String.class, MAPPER.readerForValueType(String.class))).thenReturn(json);
        when(field.getType()).thenReturn(String.class);
        when(field.getAnnotation(ValueInstantiator.Value.class)).thenReturn(new ValueInstantiator.Value());

        // When & Then
        assertThrows(IOException.class, () -> valueInstantiator.createFromObjectWith(context, new Object[0]));
    }

    @Test
    public void testCreateUsingDefault_ReturnsNull() throws Exception {
        // Given

        // When
        Object result = valueInstantiator.createUsingDefault(context);

        // Then
        assertNull(result);
    }

    @Test
    public void testCreateFromString_ValidString_ReturnsInstance() throws Exception {
        // Given
        String json = "\"Hello World\"";

        // When
        Object result = valueInstantiator.createFromString(context, json);

        // Then
        assertEquals("Hello World", result);
    }

    @Test
    public void testCreateFromString_EmptyString_ReturnsNull() throws Exception {
        // Given

        // When
        Object result = valueInstantiator.createFromString(context, "");

        // Then
        assertNull(result);
    }

    @Test
    public void testCreateFromString_InvalidString_ThrowsJsonProcessingException() throws Exception {
        // Given
        String json = "Invalid";

        // When & Then
        assertThrows(JsonProcessingException.class, () -> valueInstantiator.createFromString(context, json));
    }
}