package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testBuildWriter() throws JsonMappingException {
        // Arrange
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType declaredType = new JavaType();
        JsonSerializer<?> ser = new JsonSerializer<>();
        TypeSerializer typeSer = new TypeSerializer();
        TypeSerializer contentTypeSer = new TypeSerializer();
        AnnotatedMember am = new AnnotatedField(BeanPropertyWriter.class.getDeclaredField("name"));
        boolean defaultUseStaticTyping = true;

        // Act
        BeanPropertyWriter actual = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);

        // Assert
        // Make assertions on actual
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        // Arrange
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
        Annotated a = new AnnotatedField(BeanPropertyWriter.class.getDeclaredField("name"));
        boolean useStaticTyping = true;
        JavaType declaredType = new JavaType();

        // Act
        JavaType actual = propertyBuilder.findSerializationType(a, useStaticTyping, declaredType);

        // Assert
        // Make assertions on actual
    }

    @Test
    public void testGetPropertyDefaultValue() {
        // Arrange
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
        String name = "";
        AnnotatedMember member = new AnnotatedField(BeanPropertyWriter.class.getDeclaredField("name"));
        JavaType type = new JavaType();

        // Act
        Object actual = propertyBuilder.getPropertyDefaultValue(name, member, type);

        // Assert
        // Make assertions on actual
    }

    @Test
    public void testGetDefaultValue() {
        // Arrange
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(), new BeanDescription());
        JavaType type = new JavaType();

        // Act
        Object actual = propertyBuilder.getDefaultValue(type);

        // Assert
        // Make assertions on actual
    }

}