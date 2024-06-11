package com.example;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test class for {@link FieldProperty}
 */
public class FieldPropertyTest {

    @Mock
    private JsonParser mockJsonParser;

    @Mock
    private DeserializationContext mockContext;

    @Mock
    private Object mockInstance;

    @Mock
    private AnnotatedField mockAnnotatedField;

    @Mock
    private Annotation mockAnnotation;

    @Mock
    private BeanPropertyDefinition mockPropertyDefinition;

    @Mock
    private TypeDeserializer mockTypeDeserializer;

    @Mock
    private NullValueProvider mockNullValueProvider;

    @InjectMocks
    private FieldProperty testSubject;

    private static final String TEST_FIELD_NAME = "testField";
    private static final Annotation TEST_ANNOTATION = mock(Annotation.class);
    private static final Field TEST_FIELD = mock(Field.class);
    private static final Object TEST_VALUE = new Object();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mockAnnotatedField.getAnnotated()).thenReturn(TEST_FIELD);
        when(TEST_FIELD.getName()).thenReturn(TEST_FIELD_NAME);
        when(mockAnnotatedField.getAnnotation(mock(Class.class))).thenReturn(TEST_ANNOTATION);
        when(mockAnnotatedField.getType()).thenReturn(Object.class);
        when(mockPropertyDefinition.getName()).thenReturn(TEST_FIELD_NAME);
    }

    @Test
    public void testConstructorWithNullValueProvider() {
        // Given

        // When
        FieldProperty fieldProperty = new FieldProperty(mockPropertyDefinition,  mockTypeDeserializer, null);

        // Then
        assertSame(mockPropertyDefinition, fieldProperty.getPropertyDefinition());
        assertSame(mockTypeDeserializer, fieldProperty.getTypeDeserializer());
        assertNull(fieldProperty.getNullValueProvider());
    }

    @Test
    public void testConstructorWithValueDeserializer() {
        // Given
        JsonDeserializer<Object> mockJsonDeserializer = mock(JsonDeserializer.class);

        // When
        FieldProperty fieldProperty = new FieldProperty(mockPropertyDefinition,  mockTypeDeserializer, mockJsonDeserializer);

        // Then
        assertSame(mockPropertyDefinition, fieldProperty.getPropertyDefinition());
        assertSame(mockTypeDeserializer, fieldProperty.getTypeDeserializer());
        assertSame(mockJsonDeserializer, fieldProperty.getValueDeserializer());
    }

    @Test
    public void testConstructorWithNullProviderAndValueDeserializer() {
        // Given
        JsonDeserializer<Object> mockJsonDeserializer = mock(JsonDeserializer.class);
        NullValueProvider mockNullValueProvider = mock(NullValueProvider.class);

        // When
        FieldProperty fieldProperty = new FieldProperty(mockPropertyDefinition, mockTypeDeserializer, mockJsonDeserializer, mockNullValueProvider);

        // Then
        assertSame(mockPropertyDefinition, fieldProperty.getPropertyDefinition());
        assertSame(mockTypeDeserializer, fieldProperty.getTypeDeserializer());
        assertSame(mockJsonDeserializer, fieldProperty.getValueDeserializer());
        assertSame(mockNullValueProvider, fieldProperty.getNullValueProvider());
    }

    @Test
    public void testConstructorWithPropertyName() {
        // Given
        PropertyName mockPropertyName = mock(PropertyName.class);

        // When
        FieldProperty fieldProperty = new FieldProperty(testSubject, mockPropertyName);

        // Then
        assertSame(testSubject, fieldProperty.getPropertyDefinition());
        assertNull(fieldProperty.getTypeDeserializer());
        assertNull(fieldProperty.getValueDeserializer());
        assertNull(fieldProperty.getNullValueProvider());
        assertEquals(TEST_FIELD_NAME, fieldProperty.getName());
    }

    @Test
    public void testGetAnnotation() {
        // Given

        // When
        Annotation result = testSubject.getAnnotation(mock(Class.class));

        // Then
        verify(mockAnnotatedField).getAnnotation(eq(mock(Class.class)));
        assertSame(TEST_ANNOTATION, result);
    }

    @Test
    public void testGetMember() {
        // Given

        // When
        AnnotatedMember result = testSubject.getMember();

        // Then
        verify(mockAnnotatedField).getMember();
        assertSame(mockAnnotatedField, result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        // Given
        when(mockJsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);

        // When
        testSubject.deserializeAndSet(mockJsonParser, mockContext, mockInstance);

        // Then
        if (_skipNulls) {
            verify(mockContext, never()).findNullValueProvider();
            verifyNoMoreInteractions(mockJsonParser);
        } else {
            verify(mockContext).findNullValueProvider();
            verify(mockNullValueProvider).getNullValue(mockContext);
            verifyNoMoreInteractions(mockJsonParser);
        }
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        // Given
        when(mockJsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonDeserializer.deserialize(mockJsonParser, mockContext)).thenReturn(TEST_VALUE);

        // When
        Object result = testSubject.deserializeSetAndReturn(mockJsonParser, mockContext, mockInstance);

        // Then
        if (_skipNulls) {
            verify(mockContext, never()).findNullValueProvider();
            verifyNoMoreInteractions(mockJsonParser);
            assertSame(mockInstance, result);
        } else {
            verify(mockContext).findNullValueProvider();
            verify(mockNullValueProvider).getNullValue(mockContext);
            verifyNoMoreInteractions(mockJsonParser);
            verify(mockInstance).set(any(), eq(TEST_VALUE));
            assertSame(mockInstance, result);
        }
    }

    @Test
    public void testSet() throws IOException {
        // Given

        // When
        testSubject.set(mockInstance, TEST_VALUE);

        // Then
        verify(TEST_FIELD).set(mockInstance, TEST_VALUE);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        // Given

        // When
        Object result = testSubject.setAndReturn(mockInstance, TEST_VALUE);

        // Then
        verify(TEST_FIELD).set(mockInstance, TEST_VALUE);
        assertSame(mockInstance, result);
    }

    @Test
    public void testFixAccess() {
        // Given

        // When
        testSubject.fixAccess(mockContext);

        // Then
        verify(TEST_FIELD).setAccessible(true);
    }

    @Test
    public void testReadResolve() {
        // Given

        // When
        Object result = testSubject.readResolve();

        // Then
        verify(testSubject).hashCode(); // verify hashCode called before equality check
        assertSame(testSubject, result);
    }
}