package com.example.junit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import com.fasterxml.jackson.databind.util.StdSerializer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.mockito.MockitoAnnotations;

public class FieldPropertyTest {

    private JsonParser parser;
    private DeserializationContext ctxt;
    private ObjectCint instance;
    private AnnotatedField annotatedField;
    private FieldProperty fieldProperty;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parser = mock(JsonParser.class);
        ctxt = mock(DeserializationContext.class);
        instance = mock(ObjectCint.class);
        annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getAnnotated()).thenReturn(instance);

        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        when(typeDeserializer.deserializeWithType(parser, ctxt, typeDeserializer)).thenReturn("Test Value");

        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);

        fieldProperty = new FieldProperty(new BeanPropertyDefinition(testName.getMethodName(), null, null, null, null, null, null),
                new TypeDeserializer() {
                    @Override
                    public JsonDeserializer<Object> getNullValueDeserializer(DeserializationConfig config, BeanProperty property, JsonValueStatus valueStatus) throws JsonMappingException {
                        return null;
                    }

                    @Override
                    public JsonDeserializer<Object> createContextDeserializer(DeserializationConfig config, BeanProperty property, TypeDeserializer vtd) throws JsonMappingException {
                        return null;
                    }

                    @Override
                    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer vtd) throws IOException {
                        return "Test Value";
                    }
                }, contextAnnotations, annotatedField);

        fieldProperty = new FieldProperty(fieldProperty, null);
        fieldProperty.fixAccess(ctxt);

        when(parser.getCurrentName()).thenReturn(testName.getMethodName());
        when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
    }

    @Test
    public void testConstructor() {
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        assertNull(fieldProperty._annotated);
        assertNull(fieldProperty._field);
        assertFalse(fieldProperty._skipNulls);
    }

    @Test
    public void testSettableBeanPropertyWithName() {
        SettableBeanProperty settableBeanProperty = fieldProperty.withName(new PropertyName("testName"));
        assertSame(fieldProperty, settableBeanProperty);
        assertSame(testName.getMethodName(), settableBeanProperty.getName());
    }

    @Test
    public void testSettableBeanPropertyWithValueDeserializer() {
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        SettableBeanProperty settableBeanProperty = fieldProperty.withValueDeserializer(valueDeserializer);
        assertSame(fieldProperty, settableBeanProperty);
        assertSame(valueDeserializer, settableBeanProperty.getValueDeserializer());
    }

    @Test
    public void testSettableBeanPropertyWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);
        SettableBeanProperty settableBeanProperty = fieldProperty.withNullProvider(nva);
        assertSame(fieldProperty, settableBeanProperty);
        assertSame(nva, settableBeanProperty._nullProvider);
    }

    @Test
    public void testFixAccess() {
        fieldProperty.fixAccess(ctxt);
        verify(instance).setAccessible(true);
    }

    @Test
    public void testGetAnnotation() {
        when(annotatedField.getAnnotation(Annotation.class)).thenReturn(new Annotation() {});
        assertSame(new Annotation(), fieldProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        assertSame(annotatedField, fieldProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(parser.nextValue()).thenReturn("Test Value");
        fieldProperty.deserializeAndSet(parser, ctxt, instance);
        verify(instance).set(instance, "Test Value");
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(parser.nextValue()).thenReturn("Test Value");
        Object result = fieldProperty.deserializeSetAndReturn(parser, ctxt, instance);
        assertEquals(instance, result);
        verify(instance).set(instance, "Test Value");
    }

    @Test(expected = IOException.class)
    public void testSetWithNullValue() throws IOException {
        when(parser.nextToken()).thenReturn(JsonToken.VALUE_NULL);
        fieldProperty.set(instance, null);
    }

    @Test
    public void testSetWithNonEmptyStringValue() throws IOException {
        when(parser.nextValue()).thenReturn("Test Value");
        fieldProperty.set(instance, "Test Value");
        verify(instance).set(instance, "Test Value");
    }

    @Test
    public void testSetWithEmptyStringValue() throws IOException {
        when(parser.nextValue()).thenReturn("");
        fieldProperty.set(instance, "");
        verify(instance).set(instance, "");
    }

    @Test
    public void testSetWithNullValueDeserializer() throws IOException {
        when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser.nextValue()).thenReturn(null);
        fieldProperty.set(instance, null);
        verify(instance).set(instance, null);
    }

    @Test
    public void testSetWithValueDeserializer() throws IOException {
        when(parser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(parser.nextValue()).thenReturn("Test Value");
        when(valueTypeDeserializer.deserialize(parser, ctxt)).thenReturn("Test Deserialized Value");

        fieldProperty = new FieldProperty(fieldProperty, valueTypeDeserializer);
        fieldProperty.set(instance, null);
        verify(instance).set(instance, "Test Deserialized Value");
    }

    @Test
    public void testReadResolve() {
        FieldProperty fieldProperty = new FieldProperty(fieldProperty);
        assertSame(fieldProperty, fieldProperty.readResolve());
    }
}

class ObjectCint {
    private Object field;

    public Object getField() {
        return field;
    }

    public void setField(Object field) {
        this.field = field;
    }
}