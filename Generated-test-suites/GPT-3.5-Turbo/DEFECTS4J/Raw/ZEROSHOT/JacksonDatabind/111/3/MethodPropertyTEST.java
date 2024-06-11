package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodPropertyTest {

    private MethodProperty methodProperty;

    @Before
    public void setup() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();

        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
    }
    
    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = new NullValueProvider();
        SettableBeanProperty newProperty = methodProperty.withNullProvider(nva);

        assertNotNull(newProperty);
        assertNotEquals(methodProperty, newProperty);
        assertEquals(nva, newProperty.getNullProvider());
    }
    
    @Test
    public void testWithNewName() {
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty newProperty = methodProperty.withName(newName);

        assertNotNull(newProperty);
        assertNotEquals(methodProperty, newProperty);
        assertEquals(newName, newProperty.getFullName());
    }

    @Test
    public void testSetValueDeserializer_SameInstance() {
        JsonDeserializer<Object> deser = new JsonDeserializer<>();
        SettableBeanProperty newProperty = methodProperty.withValueDeserializer(deser);

        assertNotNull(newProperty);
        assertEquals(methodProperty, newProperty);
        assertEquals(deser, newProperty.getValueDeserializer());
    }
    
    @Test
    public void testSetValueDeserializer_DifferentInstance() {
        JsonDeserializer<Object> deser = new JsonDeserializer<>();
        SettableBeanProperty newProperty = methodProperty.withValueDeserializer(deser);

        assertNotNull(newProperty);
        assertNotEquals(methodProperty, newProperty);
        assertEquals(deser, newProperty.getValueDeserializer());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = new DeserializationConfig();
        methodProperty.fixAccess(config);

        // Perform assertion here
    }

    @Test
    public void testGetAnnotation_NullAnnotatedMethod() {
        methodProperty._annotated = null;

        assertNull(methodProperty.getAnnotation(Annotation.class));
    }

    @Test
    public void testGetAnnotation_NotNullAnnotatedMethod() {
        Annotation annotation = new Annotation();
        methodProperty._annotated = new AnnotatedMethod();
        methodProperty._annotated.addAnnotation(annotation);

        assertEquals(annotation, methodProperty.getAnnotation(Annotation.class));
    }

    // Other test methods here

}