package com.example.junit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class FieldPropertyTest {

    private FieldProperty fieldProperty;
    private AnnotatedField annotatedField;
    private Field field;
    private Object instance;
    private JsonParser jsonParser;
    private DeserializationContext context;
    private NullValueProvider nullValueProvider;
    private JsonDeserializer<?> valueDeserializer;
    private TypeDeserializer typeDeserializer;
    private Object value;

    @Before
    public void setUp() throws Exception {
        annotatedField = mock(AnnotatedField.class);
        field = mock(Field.class);
        when(annotatedField.getAnnotated()).thenReturn(field);
        instance = mock(Object.class);
        jsonParser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
        nullValueProvider = mock(NullValueProvider.class);
        valueDeserializer = mock(JsonDeserializer.class);
        typeDeserializer = mock(TypeDeserializer.class);
        when(jsonParser.getCurrentName()).thenReturn("testField");
        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getValueAsString()).thenReturn("testValue");
        when(jsonParser.nextValue()).thenReturn(JsonToken.START_OBJECT);
        when(context.findValueDeserializer(String.class)).thenReturn(valueDeserializer);
        when(context.findValueDeserializer(Object.class)).thenReturn(valueDeserializer);
        when(context.findNullValueProvider()).thenReturn(nullValueProvider);
        when(context.findTypeDeserializer(Object.class)).thenReturn(typeDeserializer);

        fieldProperty = new FieldProperty(new BeanPropertyDefinition(new PropertyName("testField"), null, null),
                ObjectType.INSTANCE, typeDeserializer, Annotations.empty(), annotatedField);
    }

    @Test
    public void testConstructor() {
        assertSame(fieldProperty.getBeanPropertyDefinition().getName(), "testField");
        assertSame(fieldProperty.getType(), ObjectType.INSTANCE);
    }

    @Test
    public void testWithName() {
        FieldProperty newFieldProperty = fieldProperty.withName(new PropertyName("newName"));
        assertSame(newFieldProperty.getBeanPropertyDefinition().getName(), "newName");
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        when(valueDeserializer.deserialize(jsonParser, context)).thenReturn("testDeserializedValue");

        FieldProperty newFieldProperty = fieldProperty.withValueDeserializer(valueDeserializer);

        ArgumentCaptor<JsonDeserializer<?>> deserializerCaptor = ArgumentCaptor.forClass(JsonDeserializer.class);
        ArgumentCaptor<NullValueProvider> nullValueProviderCaptor = ArgumentCaptor.forClass(NullValueProvider.class);

        fieldProperty.deserializeAndSet(jsonParser, context, instance);
        newFieldProperty.deserializeAndSet(jsonParser, context, instance);

        fieldProperty.getValueDeserializer().equals(deserializerCaptor.capture());
        newFieldProperty.getValueDeserializer().equals(deserializerCaptor.getValue());

        fieldProperty.getNullProvider().equals(nullValueProviderCaptor.capture());
        newFieldProperty.getNullProvider().equals(nullValueProviderCaptor.getValue());

        assertEquals("testDeserializedValue", fieldProperty.getValueDeserializer().deserialize(jsonParser, context));
        assertEquals("testDeserializedValue", newFieldProperty.getValueDeserializer().deserialize(jsonParser, context));
    }

    @Test
    public void testWithNullProvider() throws Exception {
        when(nullValueProvider.getNullValue(context)).thenReturn(null);

        FieldProperty newFieldProperty = fieldProperty.withNullProvider(nullValueProvider);

        ArgumentCaptor<NullValueProvider> nullValueProviderCaptor = ArgumentCaptor.forClass(NullValueProvider.class);

        fieldProperty.deserializeAndSet(jsonParser, context, instance);
        newFieldProperty.deserializeAndSet(jsonParser, context, instance);

        fieldProperty.getNullProvider().equals(nullValueProviderCaptor.capture());
        newFieldProperty.getNullProvider().equals(nullValueProviderCaptor.getValue());

        fieldParser.deserializeAndSet(jsonParser, context, instance);
        newFieldProperty.deserializeAndSet(jsonParser, context, instance);

        assertNull(fieldProperty.getValueDeserializer().deserialize(jsonParser, context));
        assertNull(newFieldProperty.getValueDeserializer().deserialize(jsonParser, context));
    }

    @Test
    public void testFixAccess() {
        ClassUtil.checkAndFixAccess(field, true);
    }

    @Test
    public void testGetAnnotation() {
        when(field.getAnnotations()).thenReturn(new Annotation[] { mock(Annotation.class) });
        assertEquals(mock(Annotation.class), fieldProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetMember() {
        assertEquals(annotatedField, fieldProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        when(valueDeserializer.deserialize(jsonParser, context)).thenReturn("testDeserializedValue");

        fieldProperty.deserializeAndSet(jsonParser, context, instance);

        assertEquals("testDeserializedValue", field.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        when(valueDeserializer.deserialize(jsonParser, context)).thenReturn("testDeserializedValue");

        assertEquals(instance, fieldProperty.deserializeSetAndReturn(jsonParser, context, instance));
        assertEquals("testDeserializedValue", field.get(instance));
    }

    @Test
    public void testSet() throws Exception {
        when(valueDeserializer.deserialize(jsonParser, context)).thenReturn("testDeserializedValue");

        fieldProperty.set(instance, "testValue");
        assertEquals("testValue", field.get(instance));

        fieldProperty.set(instance, "testDeserializedValue");
        assertEquals("testDeserializedValue", field.get(instance));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        when(valueDeserializer.deserialize(jsonParser, context)).thenReturn("testDeserializedValue");

        assertEquals(instance, fieldProperty.setAndReturn(instance, "testValue"));
        assertEquals("testValue", field.get(instance));

        assertEquals(instance, fieldProperty.setAndReturn(instance, "testDeserializedValue"));
        assertEquals("testDeserializedValue", field.get(instance));
    }

    @Test
    public void testReadResolve() throws Exception {
        Object resolvedObject = fieldProperty.readResolve();
        assertSame(fieldProperty, resolvedObject);
    }
}