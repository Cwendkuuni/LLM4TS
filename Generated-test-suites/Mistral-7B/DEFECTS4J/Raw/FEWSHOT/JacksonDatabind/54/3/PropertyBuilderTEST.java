package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        BeanPropertyDefinition propDef = null;
        JavaType declaredType = TypeFactory.type(String[].class);
        AnnotatedMember am = null;
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
            fail("Expected IllegalArgumentException");
        } catch (JsonMappingException e) {
            // Expected
        }

        propDef = new BeanPropertyDefinition("testProperty", null, null, null, null, null, null, null, null, null, null, null);
        am = new AnnotatedMember(propDef, null, null);
        propertyBuilder = new PropertyBuilder(null, null);

        JavaType serializationType1 = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNull(serializationType1);

        JsonSerialize ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        propertyBuilder = new PropertyBuilder(null, null);
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        config.setAnnotationIntrospector(new StandardAnnotationIntrospector());
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        propDef = new BeanPropertyDefinition("testProperty", null, null, null, null, null, null, null, null, null, null, null);
        am = new AnnotatedMember(propDef, null, null);
        JavaType secondaryType = TypeFactory.type(String.class);
        JavaType serializationType2 = propertyBuilder.findSerializationType(am, true, declaredType);
        assertEquals(secondaryType, serializationType2);

        JsonSerialize annotation = new SimpleSerializers.StringSerializer();
        propertyBuilder = new PropertyBuilder(null, null);
        am = new AnnotatedMember(propDef, null, annotation);
        JavaType serializationType3 = propertyBuilder.findSerializationType(am, true, declaredType);
        assertNull(serializationType3);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanPropertyDefinition propDef = null;
        JavaType declaredType = TypeFactory.type(String[].class);
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = null;
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        try {
            BeanPropertyWriter bpw = propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
            fail("Expected IllegalArgumentException");
        } catch (JsonMappingException e) {
            // Expected
        }

        propDef = new BeanPropertyDefinition("testProperty", null, null, null, null, null, null, null, null, null, null, null);
        am = new AnnotatedMember(propDef, null, null);
        propertyBuilder = new PropertyBuilder(null, null);

        JavaType serializationType = TypeFactory.type(String.class);
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNull(bpw);

        ser = new SimpleSerializers.StringSerializer();
        propertyBuilder = new PropertyBuilder(null, null);
        bpw = propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNull(bpw);

        propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder._defaultBean = new Object();
        propDef = new BeanPropertyDefinition("testProperty", null, null, null, null, null, null, null, null, null, null, null);
        am = new AnnotatedMember(propDef, null, null);
        Object defaultBean = propertyBuilder.getPropertyDefaultValue("testProperty", am, serializationType);
        assertEquals(defaultBean, propertyBuilder.getDefaultBean());

        propDef = new BeanPropertyDefinition("testProperty", null, null, null, null, null, null, null, null, null, null, null);
        am = new AnnotatedMember(propDef, null, null);
        JavaType secondaryType = TypeFactory.type(String.class);
        PropertyBuilder propertyBuilder1 = new PropertyBuilder(null, null);
        propertyBuilder1._annotationIntrospector = new StandardAnnotationIntrospector();
        propertyBuilder1._defaultBean = new Object();
        Object defaultBean1 = propertyBuilder1.getPropertyDefaultValue("testProperty", am, secondaryType);
        assertNull(defaultBean1);

        propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder._annotationIntrospector = new StandardAnnotationIntrospector();
        propDef = new BeanPropertyDefinition("testProperty", null, null, null, null, null, null, null, null, null, null, null);
        am = new AnnotatedMember(propDef, null, null);
        JsonSerialize.Typing typing = JsonSerialize.Typing.STATIC;
        propertyBuilder.findSerializationTyping(am);
        propertyBuilder._config.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        bpw = propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        assertNotNull(bpw);
    }

    @Test
    public void testGetClassAnnotations() {
        BeanDescription beanDesc = null;
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.getClassAnnotations());

        beanDesc = new BeanDescription(Object.class);
        propertyBuilder = new PropertyBuilder(null, beanDesc);
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
        assertEquals(0, annotations.size());
    }
}