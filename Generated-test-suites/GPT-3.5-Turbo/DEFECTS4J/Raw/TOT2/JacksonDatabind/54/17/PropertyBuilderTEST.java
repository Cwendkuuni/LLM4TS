package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {
    private PropertyBuilder propertyBuilder;

    @Test
    public void testPropertyBuilderConstructor() {
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        AnnotatedMember am = null;
        JavaType declaredType = null;
        JavaType serializationType = propertyBuilder.findSerializationType(am, true, declaredType);
        assertNull(serializationType);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        String propName = "testProp";
        AnnotatedMember member = null;
        JavaType type = null;
        Object defaultValue = propertyBuilder.getPropertyDefaultValue(propName, member, type);
        assertNotNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() {
        JavaType type = null;
        Object defaultValue = propertyBuilder.getDefaultValue(type);
        assertNotNull(defaultValue);
    }

    @Test
    public void testThrowWrapped() {
        Exception e = new Exception("Test exception");
        String propName = "testProp";
        Object defaultBean = new ObjectNode();
        Throwable throwable = null;
        try {
            propertyBuilder._throwWrapped(e, propName, defaultBean);
        } catch (Exception ex) {
            throwable = ex;
        }
        assertNotNull(throwable);
    }
}