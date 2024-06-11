package com.fasterxml.jackson.databind.deser.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class MethodPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = new SomeBeanPropertyDefinition();
        JavaType type = new SomeJavaType();
        TypeDeserializer typeDeser = new SomeTypeDeserializer();
        Annotations contextAnnotations = new SomeAnnotations();
        AnnotatedMethod method = new SomeAnnotatedMethod();

        MethodProperty methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertNotNull(methodProperty);
        assertEquals(method, methodProperty._annotated);
    }

    @Test
    public void testWithNullProvider() {
        JsonDeserializer<?> deser = new SomeJsonDeserializer();
        NullValueProvider nva = new SomeNullValueProvider();

        MethodProperty original = new MethodProperty(new SomeMethodProperty(), new SomeJsonDeserializer(), new SomeNullValueProvider());
        MethodProperty modified = original.withNullProvider(nva);

        assertNotNull(modified);
        assertEquals(nva, modified._nullProvider);
    }

    // Other test cases for remaining methods can be added here

}