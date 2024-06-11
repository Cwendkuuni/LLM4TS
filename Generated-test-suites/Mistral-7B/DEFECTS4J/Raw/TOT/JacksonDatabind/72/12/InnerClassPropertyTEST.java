package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Constructor;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class InnerClassPropertyTest {

    @InjectMocks
    private InnerClassProperty innerClassProperty;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    // Test case for default constructor
    @Test
    public void testDefaultConstructor() throws IOException {
        ObjectBean objectBean = new ObjectBean();
        JsonParser jsonParser = createMock(JsonParser.class);
        DeserializationContext deserializationContext = createMock(DeserializationContext.class);

        when(jsonParser.getCurrentName()).thenReturn("propertyName");
        when(jsonParser.getValueAsToken()).thenReturn(null);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);

        when(deserializationContext.findBeanProperty(eq("propertyName"))).thenReturn(innerClassProperty);

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, objectBean);

        verify(jsonParser, times(1)).nextToken();
        verify(jsonParser, never()).nextValue();
        assertNull(objectBean.getProperty());
    }

    // Test case for value instantiation and deserialization
    @Test
    public void testValueInstantiationAndDeserialization() throws IOException {
        ObjectBean objectBean = new ObjectBean();
        JsonParser jsonParser = createMock(JsonParser.class);
        DeserializationContext deserializationContext = createMock(DeserializationContext.class);
        Constructor<?> constructor = createMock(Constructor.class);
        when(constructor.newInstance(eq(Object.class))).thenReturn(new InnerClass());

        when(innerClassProperty._creator).thenReturn(constructor);

        when(jsonParser.getCurrentName()).thenReturn("propertyName");
        when(jsonParser.getValueAsToken()).thenReturn(null);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextValue()).thenReturn(new ObjectNode(new ObjectMapper().createObjectNode()));

        when(deserializationContext.findBeanProperty(eq("propertyName"))).thenReturn(innerClassProperty);

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, objectBean);

        verify(jsonParser, times(3)).nextToken();
        verify(jsonParser, times(1)).nextValue();
        assertInstanceOf(InnerClass.class, objectBean.getProperty());
    }

    // Test case for error handling when constructor fails to instantiate
    @Test(expected = IllegalArgumentException.class)
    public void testErrorHandlingWhenConstructorFailsToInstantiate() throws IOException {
        ObjectBean objectBean = new ObjectBean();
        JsonParser jsonParser = createMock(JsonParser.class);
        DeserializationContext deserializationContext = createMock(DeserializationContext.class);
        Constructor<?> constructor = createMock(Constructor.class);

        when(constructor.newInstance(eq(Object.class))).thenThrow(new InstantiationException("Failed to instantiate class InnerClass, problem: test error"));

        when(innerClassProperty._creator).thenReturn(constructor);

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, objectBean);
    }

    // Test case for null value deserializer
    @Test(expected = NullPointerException.class)
    public void testErrorHandlingWhenValueDeserializerIsNull() {
        ObjectBean objectBean = new ObjectBean();
        JsonParser jsonParser = createMock(JsonParser.class);
        DeserializationContext deserializationContext = createMock(DeserializationContext.class);

        innerClassProperty.setValueDeserializer(null);

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, objectBean);
    }

    // Test case for JDK serialization handling
    @Test
    public void testJdkSerializationHandling() throws IOException {
        ObjectBean objectBean = new ObjectBean();
        ObjectSerializer objectSerializer = createMock(ObjectSerializer.class);

        when(objectBean.getProperty()).thenReturn(new InnerClass());
        when(objectSerializer.serializeValue(eq(new InnerClass()), eq(ObjectWriter.Std.DEFAULT_PRETTY_PRINTER), any()))
                .thenReturn(new ObjectNode(new ObjectMapper().createObjectNode()));

        ObjectNode rootNode = new ObjectNode(new ObjectMapper().createObjectNode());
        rootNode.set("propertyName", objectSerializer.serializeValue(eq(new InnerClass()), eq(ObjectWriter.Std.DEFAULT_PRETTY_PRINTER), any()));

        ObjectReader objectReader = new ObjectMapper().readerForUpdating(ObjectBean.class);
        ObjectBean deserializedObjectBean = objectReader.readValue(rootNode);

        assertEquals(objectBean, deserializedObjectBean);
    }

    // Test case for transient constructor
    @Test
    public void testTransientConstructor() throws IOException {
        ObjectBean objectBean = new ObjectBean();
        JsonParser jsonParser = createMock(JsonParser.class);
        DeserializationContext deserializationContext = createMock(DeserializationContext.class);
        AnnotatedConstructor annotatedConstructor = createMock(AnnotatedConstructor.class);

        when(innerClassProperty._annotated).thenReturn(annotatedConstructor);

        when(jsonParser.getCurrentName()).thenReturn("propertyName");
        when(jsonParser.getValueAsToken()).thenReturn(null);
        when(jsonParser.nextToken()).thenReturn(JsonToken.START_OBJECT);

        when(deserializationContext.findBeanProperty(eq("propertyName"))).thenReturn(innerClassProperty);

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, objectBean);

        verify(jsonParser, times(1)).nextToken();
        verify(jsonParser, never()).nextValue();
        assertNull(objectBean.getProperty());
    }

    // Test case for deserializeSetAndReturn
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectBean objectBean = new ObjectBean();
        JsonParser jsonParser = createMock(JsonParser.class);
        DeserializationContext deserializationContext = createMock(DeserializationContext.class);
        Object returnValue = new Object();

        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.nextValue()).thenReturn("testValue");
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
        when(deserializationContext.findBeanProperty(eq("propertyName"))).thenReturn(innerClassProperty);

        Object result = innerClassProperty.deserializeSetAndReturn(jsonParser, deserializationContext, objectBean);

        verify(jsonParser, times(4)).nextToken();
        verify(jsonParser, times(1)).nextValue();
        assertEquals("testValue", objectBean.getProperty().toString());
        assertEquals(returnValue, result);
    }

    // Test case for setAndReturn
    @Test
    public void testSetAndReturn() throws IOException {
        ObjectBean objectBean = new ObjectBean();
        Object value = new Object();

        when(objectBean.getProperty()).thenReturn(null);
        innerClassProperty.set(objectBean, value);

        assertEquals(value, objectBean.getProperty());
    }

    // Test case for assignIndex
    @Test
    public void testAssignIndex() {
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        int index = 0;

        innerClassProperty.assignIndex(index);

        assertEquals(index, innerClassProperty.getPropertyIndex());
    }

    // Test case for getAnnotation
    @Test
    public void testGetAnnotation() {
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);
        Annotation annotation = new Annotation() {};

        assertEquals(annotation, innerClassProperty.getAnnotation(annotation.getClass()));
    }

    // Test case for getMember
    @Test
    public void testGetMember() {
        InnerClassProperty innerClassProperty = new InnerClassProperty(null, null);

        assertNull(innerClassProperty.getMember());
    }

    private JsonParser createMock(Class<?> clazz) {
        return Mockito.mock(JsonParser.class);
    }

    private DeserializationContext createMock(Class<?> clazz) {
        return Mockito.mock(DeserializationContext.class);
    }

    private Constructor<?> createMock(Class<?> clazz) {
        return Mockito.mock(Constructor.class);
    }

    private AnnotatedConstructor createMock(Class<?> clazz) {
        return Mockito.mock(AnnotatedConstructor.class);
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(InnerClass.class, new InnerClassDeserializer());
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }

    private ObjectNode createObjectNode() {
        return new ObjectMapper().createObjectNode();
    }

    private ObjectReader createObjectReader() {
        return new ObjectMapper().readerForUpdating(ObjectBean.class);
    }
}