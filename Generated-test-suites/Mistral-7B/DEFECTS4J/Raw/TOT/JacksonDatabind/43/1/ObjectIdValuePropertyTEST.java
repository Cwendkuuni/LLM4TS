package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static com.fasterxml.jackson.databind.DeserializationTest.createContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class ObjectIdValuePropertyTest {

    private ObjectIdReader objectIdReader;
    private ObjectIdValueProperty objectIdValueProperty;
    private AnnotatedMember mockedAnnotatedMember;

    @Before
    public void setUp() {
        objectIdReader = mock(ObjectIdReader.class);
        mockedAnnotatedMember = mock(AnnotatedMember.class);
        BeanPropertyDefinition mockedBeanPropertyDefinition = mock(BeanPropertyDefinition.class);
        given(mockedBeanPropertyDefinition.getMember()).willReturn(mockedAnnotatedMember);

        objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
    }

    @Test
    public void testWithName() {
        PropertyName newName = Mockito.mock(PropertyName.class);
        ObjectIdValueProperty objectIdValuePropertyWithNewName = objectIdValueProperty.withName(newName);
        assertEquals(newName, objectIdValuePropertyWithNewName.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<String> jsonDeserializerMock = mock(JsonDeserializer.class);
        ObjectIdValueProperty objectIdValuePropertyWithJsonDeserializer = objectIdValueProperty.withValueDeserializer(jsonDeserializerMock);
        assertEquals(jsonDeserializerMock, objectIdValuePropertyWithJsonDeserializer.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object id = "123";
        DeserializationContext context = createContext();
        given(objectIdReader.generator).willReturn(Mockito.mock(ObjectIdGenerator.class));
        given(objectIdReader.resolver).willReturn(Mockito.mock(ObjectIdResolver.class));

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object result = objectIdValueProperty.deserializeAndSet(Mockito.mock(JsonParser.class), context, Mockito.mock(Object.class));

        ReadableObjectId roid = Mockito.mock(ReadableObjectId.class);
        given(context.findObjectId(id, objectIdReader.generator, objectIdReader.resolver)).willReturn(roid);
        given(roid.bindItem(Mockito.any(Object.class))).willReturn(null);

        ObjectIdValueProperty objectIdValuePropertyWithIdProperty = new ObjectIdValueProperty(objectIdValueProperty, null);
        given(objectIdValuePropertyWithIdProperty.getIdProperty()).willReturn(mockedBeanPropertyDefinition);

        Object resultWithIdProperty = objectIdValuePropertyWithIdProperty.deserializeAndSet(Mockito.mock(JsonParser.class), context, Mockito.mock(Object.class));

        assertEquals(id, resultWithIdProperty);
    }

    @Test
    public void testDeserializeSetAndReturnNullId() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object result = objectIdValueProperty.deserializeSetAndReturn(Mockito.mock(JsonParser.class), createContext(), Mockito.mock(Object.class));

        assertNull(result);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object();
        Object value = new Object();

        Object result = objectIdValueProperty.setAndReturn(instance, value);

        ObjectIdValueProperty objectIdValuePropertyWithIdProperty = new ObjectIdValueProperty(objectIdValueProperty, null);
        given(objectIdValuePropertyWithIdProperty.getIdProperty()).willReturn(mockedBeanPropertyDefinition);

        Object resultWithIdProperty = objectIdValuePropertyWithIdProperty.setAndReturn(instance, value);

        assertEquals(resultWithIdProperty, result);
    }
}