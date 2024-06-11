package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.NameTransformer;

/**
 * Test class for ObjectIdValueProperty
 */
@RunWith(MockitoJUnitRunner.class)
public class ObjectIdValuePropertyTest {

    @Mock
    private ObjectIdReader _objectIdReader;
    @Mock
    private JsonDeserializer<?> _valueDeserializer;
    @Mock
    private DeserializationContext _ctxt;
    @Mock
    private AnnotatedMember _member;
    @Mock
    private BeanPropertyDefinition _propertyDefinition;
    @Mock
    private BeanDescription _beanDesc;
    @Mock
    private TypeIdResolver _typeIdResolver;
    @Mock
    private NameTransformer _nameTransformer;

    @InjectMocks
    private ObjectIdValueProperty objIdValueProperty;

    @Before
    public void setUp() throws Exception {
        when(_objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(_objectIdReader.getIdType()).thenReturn(Integer.class);
        when(_objectIdReader.getDeserializer()).thenReturn(_valueDeserializer);
        when(_objectIdReader.idProperty).thenReturn(new TestSettableBeanProperty());
        when(_beanDesc.findBeanPropertyDefinition(_objectIdValueProperty.getName())).thenReturn(_propertyDefinition);
        when(_beanDesc.getBeanPropertyDefinition("otherProperty")).thenReturn(mock(BeanPropertyDefinition.class));
    }

    @Test
    public void testObjectIdValuePropertyConstruction() {
        ObjectIdValueProperty objIdValueProperty = new ObjectIdValueProperty(_objectIdReader, null);
        assertSame(ObjectIdValueProperty.class, objIdValueProperty.getClass());
        assertSame(_objectIdReader, objIdValueProperty._objectIdReader);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object id = new Integer(1);
        when(_valueDeserializer.deserialize(any(JsonParser.class), _ctxt)).thenReturn(id);

        Object instance = mock(Integer.class);
        objIdValueProperty.deserializeAndSet(_mockJsonParser(), _ctxt, instance);

        verify(_objectIdReader.generator).bindItem(instance);
        verify(_objectIdValueProperty.getBeanPropertyDefinition().getSetter()).invoke(instance, id);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object id = new Integer(1);
        when(_valueDeserializer.deserialize(any(JsonParser.class), _ctxt)).thenReturn(id);

        Object instance = mock(Integer.class);
        Object result = objIdValueProperty.deserializeSetAndReturn(_mockJsonParser(), _ctxt, instance);

        assertSame(instance, result);

        verify(_objectIdReader.generator).bindItem(instance);
        verify(_objectIdValueProperty.getBeanPropertyDefinition().getSetter()).invoke(instance, id);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException, InvocationTargetException, IllegalAccessException {
        Object instance = mock(Integer.class);
        objIdValueProperty.set(instance, null);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object id = new Integer(1);
        Object instance = mock(Integer.class);
        Object result = new Integer(2);
        when(_objectIdValueProperty.getBeanPropertyDefinition().getSetter()).then(i -> result);

        Object returnedValue = objIdValueProperty.setAndReturn(instance, id);

        assertSame(result, returnedValue);
        verify(_objectIdReader.generator).bindItem(instance);
        verify(_objectIdValueProperty.getBeanPropertyDefinition().getSetter()).invoke(instance, id);
    }

    @Test
    public void testGetAnnotation() {
        assertNull(objIdValueProperty.getAnnotation(Mockito.any(Annotation.class).getClass()));
    }

    @Test
    public void testGetMember() {
        assertNull(objIdValueProperty.getMember());
    }

    @Test
    public void testOverrideWithName() {
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newObjectIdValueProperty = objIdValueProperty.withName(newName);

        assertSame(newName, newObjectIdValueProperty.getName());
    }

    @Test
    public void testOverrideWithValueDeserializer() {
        JsonDeserializer<Object> newJsonDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty newObjectIdValueProperty = objIdValueProperty.withValueDeserializer(newJsonDeserializer);

        assertSame(newJsonDeserializer, newObjectIdValueProperty._valueDeserializer);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOverrideWithDeprecatedConstructor() {
        ObjectIdValueProperty newObjectIdValueProperty = objIdValueProperty.withName("newName");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOverrideWithDeprecatedConstructor2() {
        ObjectIdValueProperty newObjectIdValueProperty = objIdValueProperty.withName("newName");
    }

    @Test
    public void testDeserializeWhenIdPropertyIsNull() throws IOException {
        Object id = new Integer(1);
        when(_valueDeserializer.deserialize(any(JsonParser.class), _ctxt)).thenReturn(id);

        Object instance = mock(Integer.class);
        objIdValueProperty.deserializeAndSet(_mockJsonParser(), _ctxt, instance);

        verifyNoMoreInteractions(_objectIdReader.generator);
        verifyNoMoreInteractions(_objectIdValueProperty.getBeanPropertyDefinition().getSetter());
    }

    @Test
    public void testDeserializeSetIdProperty() throws IOException {
        Object id = new Integer(1);
        Object idProperty = new TestSettableBeanProperty();
        when(_valueDeserializer.deserialize(any(JsonParser.class), _ctxt)).thenReturn(id);

        Object instance = mock(Integer.class);
        objIdValueProperty = new ObjectIdValueProperty(objIdValueProperty, idProperty);

        Object result = objIdValueProperty.deserializeAndSet(_mockJsonParser(), _ctxt, instance);

        verify(_objectIdReader.generator).bindItem(instance);
        verify(_objectIdProperty.getBeanPropertyDefinition().getSetter()).invoke(instance, id);
        verify(idProperty).setAndReturn(instance, id);
        assertSame(id, result);
    }

    @Test
    public void testDeserializeSetIdPropertyWhenIdIsNull() throws IOException {
        JsonParser mockJsonParser = mock(JsonParser.class);
        when(_valueDeserializer.deserialize(mockJsonParser, _ctxt)).thenReturn(null);

        Object instance = mock(Integer.class);
        objIdValueProperty = new ObjectIdValueProperty(objIdValueProperty, new TestSettableBeanProperty());

        objIdValueProperty.deserializeAndSet(mockJsonParser, _ctxt, instance);

        verifyNoMoreInteractions(_objectIdReader.generator);
        verifyNoMoreInteractions(_objectIdProperty.getBeanPropertyDefinition().getSetter());
        verifyNoMoreInteractions(idProperty.getSetter());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeserializeWhenIdPropertyIsNullAndDeserializerFails() throws IOException {
        Object id = new Integer(1);
        when(_valueDeserializer.deserialize(any(JsonParser.class), _ctxt)).thenThrow(new IOException());

        Object instance = mock(Integer.class);
        objIdValueProperty.deserializeAndSet(_mockJsonParser(), _ctxt, instance);

        verifyNoMoreInteractions(_objectIdReader.generator);
        verifyNoMoreInteractions(_objectIdValueProperty.getBeanPropertyDefinition().getSetter());
    }

    private JsonParser _mockJsonParser() {
        JsonParser jsonParser = mock(JsonParser.class);
        when(jsonParser.getCurrentName()).thenReturn("id");
        return jsonParser;
    }

    private static class TestSettableBeanProperty extends SettableBeanProperty {
        @Override
        public Object setAndReturn(Object instance, Object value) throws IOException {
            return value;
        }

        @Override
        public ObjectIdPropertyDefinition getBeanPropertyDefinition() {
            return mock(ObjectIdPropertyDefinition.class);
        }
    }

}