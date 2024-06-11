package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Constructor;

public class InnerClassPropertyTest {

    @Test
    public void testConstructor() throws Exception {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = delegate.getClass().getConstructor();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);

        // Test if constructor initializes the delegate and creator correctly
        assertSame(delegate, property._delegate);
        assertSame(ctor, property._creator);
    }

    @Test
    public void testWithAnnotation() throws Exception {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = delegate.getClass().getConstructor();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);

        Annotation annotation = property.getAnnotation(MyAnnotation.class);
        
        // Test if the delegate returns the correct annotation
        assertNotNull(annotation);
        assertEquals(MyAnnotation.class, annotation.annotationType());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        SettableBeanProperty delegate = new SettableBeanProperty();
        Constructor<?> ctor = delegate.getClass().getConstructor();
        InnerClassProperty property = new InnerClassProperty(delegate, ctor);

        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"name\":\"John\"}");

        DeserializationContext ctxt = mapper.getDeserializationContext();
        Object bean = new Object();

        // Test if the method correctly deserializes the JSON and sets the value
        property.deserializeAndSet(jp, ctxt, bean);

        // Assert that the value was correctly set
        assertEquals("John", bean.getName());
    }
}