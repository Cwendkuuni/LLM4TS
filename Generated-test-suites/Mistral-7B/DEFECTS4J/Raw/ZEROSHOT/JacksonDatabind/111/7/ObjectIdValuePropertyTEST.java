package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * JUnit 4 test file for {@link ObjectIdValueProperty} class.
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private BeanDescription beanDescription;
    private DeserializationContext context;
    private JsonParser jsonParser;
    private Object instance;

    @Before
    public void setup() throws Exception {
        objectIdReader = Mockito.mock(ObjectIdReader.class);
        PropertyName propertyName = Mockito.mock(PropertyName.class);
        JsonDeserializer<?> idDeserializer = Mockito.mock(JsonDeserializer.class);
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, propertyName, new PropertyMetadata(), idDeserializer, null);
        JsonDeserializer<?> deserializer = Mockito.spy(objectIdValueProperty);

        TypeFactory typeFactory = Mockito.mock(TypeFactory.class);
        beanDescription = Mockito.mock(BeanDescription.class);
        context = Mockito.mock(DeserializationContext.class);
        instance = Mockito.mock(Object.class);

        TypeReference<Object> objectTypeRef = new TypeReference<Object>() {};
        jsonParser = Mockito.mock(JsonParser.class);

        when(objectIdReader.propertyName).thenReturn(propertyName);
        when(objectIdReader.getIdType()).thenReturn(Object.class);
        when(objectIdReader.getDeserializer()).thenReturn(idDeserializer);

        when(propertyName.asPrefixPropertyName(anyString())).thenAnswer(invocation -> new PropertyName(invocation.getArgument(0) + "_"));

        when(beanDescription.findBeanProperty(anyString())).thenReturn(deserializer);
        when(beanDescription.getSerializationConfig()).thenReturn(context.getSerializationConfig());
        when(beanDescription.findValueDeserializer(any(PropertyName.class))).thenReturn(valueDeserializer);
        when(beanDescription.getTypeFactory()).thenReturn(typeFactory);
        when(typeFactory.constructType(eq(Object.class))).thenReturn(objectTypeRef.getType());

        when(context.findObjectId(any(Object.class), any(ObjectIdGenerator.class), any(ObjectIdResolver.class))).thenReturn(Mockito.mock(ReadableObjectId.class));
        when(context.handleUnexpectedValue(any(Object.class), any(PropertyName.class), any(JsonParser.class), any(DeserializationContext.class)))
                .thenThrow(JsonProcessingException.class);
        when(context.getActiveContext()).thenReturn(context);
        when(context.getCurrentName()).thenReturn("test");
        when(context.findValueDeserializer(any(PropertyName.class))).thenReturn(valueDeserializer);
        when(context.findValueDeserializer(any(PropertyName.class), any(TypeDeserializer.class.getClass())))
                .thenReturn(valueDeserializer);
        when(context.findBeanProperty(any(PropertyName.class))).thenReturn(deserializer);
        when(context.handleMissingInstantiator(any(Class.class), any(Object.class), any(PropertyName.class)))
                .thenThrow(JsonMappingException.class);
        when(context.handleUnknownProperty(any(PropertyName.class), any(JsonParser.class), any(DeserializationContext.class)))
                .thenThrow(JsonMappingException.class);
        when(context.handleNonWritableProperty(any(PropertyName.class), any(Object.class), any(JsonParser.class)))
                .thenThrow(JsonMappingException.class);
        when(context.handleNonReadableProperty(any(PropertyName.class), any(Object.class), any(DeserializationContext.class)))
                .thenThrow(JsonMappingException.class);
        when(context.handleMissingValue(any(PropertyName.class), any(Object.class), any(JsonParser.class), any(DeserializationContext.class)))
                .thenThrow(JsonMappingException.class);

        doNothing().when(deserializer).deserializeAndSet(jsonParser, context, instance);
        doThrow(JsonMappingException.class).when(deserializer).deserializeSetAndReturn(jsonParser, context, instance);
        doThrow(JsonMappingException.class).when(deserializer).set(instance, any(Object.class));

        doReturn(instance).when(deserializer).setAndReturn(instance, any(Object.class));

        doReturn(deserializer).when(deserializer).withName(any(PropertyName.class));
        doReturn(deserializer).when(deserializer).withValueDeserializer(any(JsonDeserializer.class));
        doReturn(deserializer).when(deserializer).withNullProvider(any(NullValueProvider.class));
    }

    @Test
    public void testGetAnnotation() throws Exception {
        // This test case is not applicable since ObjectIdValueProperty does not have any annotations.
        // Annotation getAnnotation(Class<A> acls) method is inherited from SettableBeanProperty.
    }

    @Test
    public void testGetMember() throws Exception {
        // This test case is not applicable since ObjectIdValueProperty does not have any member.
        // getMember() method is inherited from SettableBeanProperty.
        AnnotatedMember annotatedMember = deserializer.getMember();
        assertNull(annotatedMember);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);

        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        deserializer.deserializeAndSet(jsonParser, context, instance);
        Mockito.verify(instance, Mockito.never()).setAndReturn(any(Object.class), any(Object.class));
    }

    @Test
    public void testDeserializeSetAndReturnNull() throws Exception {
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_NULL);

        Object result = deserializer.deserializeSetAndReturn(jsonParser, context, instance);
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeSetAndReturnNotNull() throws Exception {
        Object id = new Object();
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        doReturn(id).when(jsonParser).getValueAsString();

        Object result = deserializer.deserializeSetAndReturn(jsonParser, context, instance);
        assertEquals(id, result);

        Object roid = Mockito.mock(ReadableObjectId.class);
        when(context.findObjectId(eq(id), any(ObjectIdGenerator.class), any(ObjectIdResolver.class))).thenReturn(roid);
        when(roid.getId()).thenReturn(id);

        SettableBeanProperty idProp = Mockito.mock(SettableBeanProperty.class);
        when(objectIdReader.idProperty).thenReturn(idProp);
        when(idProp.setAndReturn(instance, id)).thenReturn(instance);

        result = deserializer.deserializeSetAndReturn(jsonParser, context, instance);
        assertEquals(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws Exception {
        Object value = new Object();
        deserializer.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object value = new Object();
        Object result = deserializer.setAndReturn(instance, value);
        assertEquals(instance, result);

        SettableBeanProperty idProp = Mockito.mock(SettableBeanProperty.class);
        when(objectIdReader.idProperty).thenReturn(idProp);
        when(idProp.setAndReturn(instance, value)).thenReturn(instance);

        result = deserializer.setAndReturn(instance, value);
        assertEquals(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithName() throws Exception {
        deserializer.withName(new PropertyName("test"));
    }

    @Test
    public void testWithValueDeserializer() throws Exception {
        JsonDeserializer<?> newValueDeserializer = Mockito.mock(JsonDeserializer.class);
        ObjectIdValueProperty newDeserializer = (ObjectIdValueProperty) deserializer.withValueDeserializer(newValueDeserializer);
        assertEquals(newValueDeserializer, newDeserializer._valueDeserializer);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testWithNullProvider() throws Exception {
        deserializer.withNullProvider(null);
    }
}