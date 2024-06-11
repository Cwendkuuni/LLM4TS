package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;

    @Before
    public void setUp() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription();
        propertyBuilder = new PropertyBuilder(config, beanDesc);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedMember();
        boolean defaultUseStaticTyping = true;

        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        assertNotNull(writer);
        assertEquals(propDef, writer.getDefinition());
        assertEquals(am, writer.getMember());
        assertEquals(declaredType, writer.getMemberType());
        assertEquals(ser, writer.getSerializer());
        assertEquals(typeSer, writer.getTypeSerializer());
        assertEquals(contentTypeSer, writer.getContentTypeSerializer());
        assertEquals(defaultUseStaticTyping, writer.isDefaultUseStaticTyping());
    }

    @Test
    public void testFindSerializationTypeWithRefine() throws JsonMappingException {
        Annotated a = new Annotated();
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType();

        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);

        assertNotNull(serializationType);
        assertEquals(declaredType, serializationType);
    }

    @Test
    public void testFindSerializationTypeWithoutRefine() throws JsonMappingException {
        Annotated a = new Annotated();
        boolean useStaticTyping = false;
        JavaType declaredType = new JavaType();

        JavaType serializationType = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);

        assertNull(serializationType);
    }

    @Test
    public void testGetPropertyDefaultValueWithDefaultBean() {
        String name = "test";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();

        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);

        assertNull(defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValueWithoutDefaultBean() {
        String name = "test";
        AnnotatedMember member = new AnnotatedMember();
        JavaType type = new JavaType();

        Object defaultValue = propertyBuilder.getPropertyDefaultValue(name, member, type);

        assertNotNull(defaultValue);
        assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }

    @Test
    public void testGetDefaultValueForPrimitiveType() {
        JavaType type = new JavaType(int.class);

        Object defaultValue = propertyBuilder.getDefaultValue(type);

        assertEquals(0, defaultValue);
    }

    @Test
    public void testGetDefaultValueForWrapperType() {
        JavaType type = new JavaType(Integer.class);

        Object defaultValue = propertyBuilder.getDefaultValue(type);

        assertEquals(0, defaultValue);
    }

    @Test
    public void testGetDefaultValueForStringType() {
        JavaType type = new JavaType(String.class);

        Object defaultValue = propertyBuilder.getDefaultValue(type);

        assertEquals("", defaultValue);
    }

    @Test
    public void testGetDefaultValueForArrayType() {
        JavaType type = new JavaType(int[].class);

        Object defaultValue = propertyBuilder.getDefaultValue(type);

        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValueForCollectionType() {
        JavaType type = new JavaType(List.class);

        Object defaultValue = propertyBuilder.getDefaultValue(type);

        assertEquals(JsonInclude.Include.NON_EMPTY, defaultValue);
    }

}