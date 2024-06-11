package com.example.junit; // replace with your package name

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.NullValueProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for {@link FieldProperty}
 */
public class FieldPropertyTest {

    private static final String FIELD_NAME = "testField";
    private static final Class<Integer> FIELD_TYPE = Integer.class;
    private static final Integer DEFAULT_VALUE = 42;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext context;

    @Mock
    private JsonDeserializer<?> valueDeserializer;

    @Mock
    private TypeDeserializer valueTypeDeserializer;

    @Mock
    private NullValueProvider nullValueProvider;

    private final BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
    private final AnnotatedField field = mock(AnnotatedField.class);
    private final Annotations contextAnnotations = mock(Annotations.class);

    private final FieldProperty fieldProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(field.getName()).thenReturn(new PropertyName(FIELD_NAME));
        when(field.getType()).thenReturn(FIELD_TYPE);

        fieldProperty = new FieldProperty(propDef, Object.class.getDeclaredField(FIELD_NAME), null, contextAnnotations, field);
    }

    /**
     * Test constructor with basic arguments
     */
    @Test
    public void testConstructorWithBasicArguments() {
        assertEquals(propDef, fieldProperty.getPropertyDefinition());
        assertEquals(FIELD_TYPE, fieldProperty.getType());
        assertNull(fieldProperty.getValueDeserializer());
        assertNull(fieldProperty.getNullProvider());
        assertFalse(fieldProperty.isTransient());
        assertTrue(fieldProperty.isField());
    }

    /**
     * Test constructor with JsonDeserializer
     */
    @Test
    public void testConstructorWithJsonDeserializer() {
        Object deserializedValue = new Object();

        when(valueDeserializer.deserialize(jsonParser, context)).thenReturn(deserializedValue);

        FieldProperty fieldPropertyWithDeserializer = new FieldProperty(fieldProperty, valueDeserializer, nullValueProvider);

        assertEquals(fieldProperty.getPropertyDefinition(), fieldPropertyWithDeserializer.getPropertyDefinition());
        assertEquals(fieldProperty.getType(), fieldPropertyWithDeserializer.getType());
        assertEquals(valueDeserializer, fieldPropertyWithDeserializer.getValueDeserializer());
        assertNull(fieldPropertyWithDeserializer.getNullProvider());
        assertFalse(fieldPropertyWithDeserializer.isTransient());
        assertTrue(fieldPropertyWithDeserializer.isField());

        assertEquals(deserializedValue, fieldPropertyWithDeserializer.getValue());
    }

    /**
     * Test constructor with PropertyName
     */
    @Test
    public void testConstructorWithPropertyName() {
        PropertyName newName = new PropertyName("newName");

        FieldProperty fieldPropertyWithNewName = new FieldProperty(fieldProperty, newName);

        assertEquals(fieldProperty.getPropertyDefinition(), fieldPropertyWithNewName.getPropertyDefinition());
        assertEquals(fieldProperty.getType(), fieldPropertyWithNewName.getType());
        assertEquals(fieldProperty.getValueDeserializer(), fieldPropertyWithNewName.getValueDeserializer());
        assertEquals(fieldProperty.getNullProvider(), fieldPropertyWithNewName.getNullProvider());
        assertEquals(newName, fieldPropertyWithNewName.getName());
        assertFalse(fieldPropertyWithNewName.isTransient());
        assertTrue(fieldPropertyWithNewName.isField());
    }

    /**
     * Test constructor used for JDK Serialization
     */
    @Test
    public void testConstructorForJdkSerialization() {
        FieldProperty fieldPropertyToCopy = new FieldProperty(fieldProperty);

        assertEquals(fieldProperty.getPropertyDefinition(), fieldPropertyToCopy.getPropertyDefinition());
        assertEquals(fieldProperty.getType(), fieldPropertyToCopy.getType());
        assertEquals(fieldProperty.getValueDeserializer(), fieldPropertyToCopy.getValueDeserializer());
        assertEquals(fieldProperty.getNullProvider(), fieldPropertyToCopy.getNullProvider());
        assertEquals(fieldProperty.getName(), fieldPropertyToCopy.getName());
        assertFalse(fieldPropertyToCopy.isTransient());
        assertTrue(fieldPropertyToCopy.isField());
    }

    /**
     * Test method getAnnotation
     */
    @Test
    public void testGetAnnotation() {
        Annotation annotation = mock(Annotation.class);

        when(field.getAnnotated()).thenReturn(annotation);

        assertEquals(annotation, fieldProperty.getAnnotation(annotation.getClass()));
    }

    /**
     * Test method getMember
     */
    @Test
    public void testGetMember() {
        assertEquals(field, fieldProperty.getMember());
    }

    /**
     * Test method deserializeAndSet
     */
    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();

        when(jsonParser.getCurrentName()).thenReturn(FIELD_NAME);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        when(jsonParser.getValueAsInt()).thenReturn(DEFAULT_VALUE);

        fieldProperty.deserializeAndSet(jsonParser, context, instance);

        assertEquals(instance, fieldProperty.getLeafProperty());
        assertEquals(DEFAULT_VALUE, fieldProperty.getValue());
        assertEquals(DEFAULT_VALUE, field.get(instance));
    }

    /**
     * Test method deserializeSetAndReturn
     */
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();

        when(jsonParser.getCurrentName()).thenReturn(FIELD_NAME);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        when(jsonParser.getValueAsInt()).thenReturn(DEFAULT_VALUE);

        Object result = fieldProperty.deserializeSetAndReturn(jsonParser, context, instance);

        assertEquals(instance, result);
        assertEquals(DEFAULT_VALUE, fieldProperty.getValue());
        assertEquals(DEFAULT_VALUE, field.get(instance));
    }

    /**
     * Test method set
     */
    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Integer value = 43;

        fieldProperty.set(instance, value);

        assertEquals(instance, fieldProperty.getLeafProperty());
        assertEquals(value, fieldProperty.getValue());
        assertEquals(value, field.get(instance));
    }

    /**
     * Test method setAndReturn
     */
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Integer value = 43;

        Object result = fieldProperty.setAndReturn(instance, value);

        assertEquals(instance, result);
        assertEquals(value, fieldProperty.getValue());
        assertEquals(value, field.get(instance));
    }

    /**
     * Test method fixAccess
     */
    @Test
    public void testFixAccess() {
        fieldProperty.fixAccess(context);

        try {
            field.setAccessible(false);
            fieldProperty.setValue(new Object(), "value");
            fail();
        } catch (IllegalAccessException e) {
            // expected
        }
    }

    /**
     * Test method readResolve
     */
    @Test
    public void testReadResolve() throws IOException {
        Object instance = new Object();
        FieldProperty fieldPropertyToCopy = new FieldProperty(fieldProperty);

        FieldProperty readResolvedFieldProperty = (FieldProperty) fieldPropertyToCopy.readResolve();

        assertEquals(fieldProperty.getPropertyDefinition(), readResolvedFieldProperty.getPropertyDefinition());
        assertEquals(fieldProperty.getType(), readResolvedFieldProperty.getType());
        assertEquals(fieldProperty.getValueDeserializer(), readResolvedFieldProperty.getValueDeserializer());
        assertEquals(fieldProperty.getNullProvider(), readResolvedFieldProperty.getNullProvider());
        assertEquals(fieldProperty.getName(), readResolvedFieldProperty.getName());
        assertFalse(readResolvedFieldProperty.isTransient());
        assertTrue(readResolvedFieldProperty.isField());
    }

    /**
     * Test exception handling: JsonToken.VALUE_NULL when _skipNulls is true
     */
    @Test
    public void testDeserializeAndSetWithNullValueAndSkipNulls() throws IOException {
        when(jsonParser.getCurrentName()).thenReturn(FIELD_NAME);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        fieldProperty.deserializeAndSet(jsonParser, context, new Object());

        verify(context, times(1)).handleMissingValue(any(DeserializationContext.class), anyString(), any(JavaType.class), eq(JsonToken.VALUE_NULL), any(PropertyName.class), any(BeanPropertyDefinition.class));
    }

    /**
     * Test exception handling: JsonToken.VALUE_NULL when _skipNulls is false
     */
    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullValueAndDontSkipNulls() throws IOException {
        when(jsonParser.getCurrentName()).thenReturn(FIELD_NAME);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        fieldProperty.deserializeAndSet(jsonParser, context, new Object());
    }

    /**
     * Test exception handling: JsonDeserializer return null value
     */
    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullValueFromJsonDeserializer() throws IOException {
        when(jsonParser.getCurrentName()).thenReturn(FIELD_NAME);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(valueDeserializer.deserialize(jsonParser, context)).thenReturn(null);

        fieldProperty.deserializeAndSet(jsonParser, context, new Object());
    }

    /**
     * Test method _throwAsIOE
     */
    @Test
    public void testThrowAsIOE() {
        IOException ioException = new IOException("Test IOException");

        fieldProperty._throwAsIOE(ioException, "value");

        assertEquals("Test IOException", ioException.getMessage());
    }
}