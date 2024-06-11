package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.serializer.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class InnerClassPropertyTest extends TestBase {

    @SuppressWarnings("unchecked")
    @Test
    public void testInnerClassProperty() throws Exception {
        SettableBeanProperty delegate = mock(SettableBeanProperty.class);
        Constructor<InnerClass> constructor = mock(Constructor.class);
        AnnotatedConstructor annotated = mock(AnnotatedConstructor.class);
        when(constructor.newInstance(anyObject())).thenReturn(new InnerClass());
        InnerClassProperty property = new InnerClassProperty(delegate, constructor);

        Object value = property.deserializeAndSet(createParser(), createContext(), new Object());
        assertNotNull(value);

        property = new InnerClassProperty(property, annotated);
        value = property.deserializeAndSet(createParser(), createContext(), new Object());
        assertNotNull(value);

        property = new InnerClassProperty(property, createValueDeserializer());
        value = property.deserializeAndSet(createParser(), createContext(), new Object());
        assertNotNull(value);

        property = new InnerClassProperty(property, PropertyName.construct("name"));
        value = property.deserializeAndSet(createParser(), createContext(), new Object());
        assertNotNull(value);

        property = new InnerClassProperty(property, createJsonDeserializer());
        value = property.deserializeAndSet(createParser(), createContext(), new Object());
        assertNotNull(value);

        // test null values
        JsonParser parser = createParser();
        parser.nextValue(); // consume START_ARRAY
        parser.nextValue(); // consume VALUE_NULL
        property.deserializeAndSet(parser, createContext(), new Object());
        assertNull(property.getValue());

        parser.nextToken(); // consume VALUE_NULL
        parser.nextValue(); // consume END_ARRAY
        property.deserializeAndSet(parser, createContext(), new Object());
        assertNull(property.getValue());

        // test value deserialization
        parser = createParser();
        Object valueToDeserialize = new Object();
        when(createJsonDeserializer().deserialize(parser, createContext(), valueToDeserialize)).thenReturn(new Object());

        property = new InnerClassProperty(delegate, constructor);
        property.setValueDeserializer(createJsonDeserializer());
        property.deserializeAndSet(parser, createContext(), new Object());
        assertNotNull(property.getValue());

        // test set and return
        Object instance = new Object();
        when(delegate.setAndReturn(instance, anyObject())).thenReturn(instance);

        property = new InnerClassProperty(delegate, constructor);
        property.setValueDeserializer(createJsonDeserializer());
        Object returnedValue = property.deserializeSetAndReturn(parser, createContext(), instance);
        assertNotNull(returnedValue);
        assertEquals(instance, returnedValue);

        // test JDK serialization
        property = new InnerClassProperty(property, annotated);
        property = property.readResolve();
        assertNotNull(property);

        property = property.writeReplace();
        assertNotNull(property);
    }
}