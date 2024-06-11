package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        final BeanDescription beanDesc = new BeanDescription(new ClassName("com.test.TestClass"), null, null, null);
        final PropertyName propertyName = new PropertyName("testProperty");
        final AnnotatedMember member = new AnnotatedMember(new AnnotatedClass("com.test.TestClass"), propertyName, null, null, null, null, null);
        final JavaType declaredType = new TypeReference<Object>() {}.getType();

        final PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(new ObjectMapper()), beanDesc);
        final JavaType result = propertyBuilder.findSerializationType(member, false, declaredType);

        assertNull(result);
    }

    @Test
    public void testFindSerializationTypeWithJsonSerializeAnnotation() throws JsonMappingException {
        final BeanDescription beanDesc = new BeanDescription(new ClassName("com.test.TestClass"), null, null, null);
        final PropertyName propertyName = new PropertyName("testProperty");
        final AnnotatedMember member = new AnnotatedMember(new AnnotatedClass("com.test.TestClass"), propertyName, null, null, new JsonSerialize(String.class), null, null);
        final JavaType declaredType = new TypeReference<Object>() {}.getType();

        final PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(new ObjectMapper()), beanDesc);
        final JavaType result = propertyBuilder.findSerializationType(member, false, declaredType);

        assertEquals(String.class, result.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationTypeWithIncompatibleClass() throws JsonMappingException {
        final BeanDescription beanDesc = new BeanDescription(new ClassName("com.test.TestClass"), null, null, null);
        final PropertyName propertyName = new PropertyName("testProperty");
        final AnnotatedMember member = new AnnotatedMember(new AnnotatedClass("com.test.TestClass"), propertyName, null, null, null, null, new ClassName("java.lang.String"));
        final JavaType declaredType = new TypeReference<Object>() {}.getType();

        final PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(new ObjectMapper()), beanDesc);
        propertyBuilder.findSerializationType(member, false, declaredType);

        fail("Should throw exception");
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        final SerializationConfig serializationConfig = new SerializationConfig(new ObjectMapper());
        final BeanDescription beanDesc = new BeanDescription(new ClassName("com.test.TestClass"), null, null, null);
        final PropertyName propertyName = new PropertyName("testProperty");
        final BeanPropertyDefinition propDef = new BeanPropertyDefinition(propertyName, null, new ClassName("java.lang.String"), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        final JavaType declaredType = new TypeReference<Object>() {}.getType();
        final JsonSerializer<?> ser = new JsonSerializer<Object>() {};
        final TypeSerializer typeSer = new StandardSerializer<>(String.class);
        final TypeSerializer contentTypeSer = new StandardSerializer<>(Integer.class);
        final AnnotatedMember am = new AnnotatedMember(new AnnotatedClass("com.test.TestClass"), propertyName, null, null, null, null, null);

        final PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDesc);
        final BeanPropertyWriter result = propertyBuilder.buildWriter(new Provider(), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(result);
    }

    @Test
    public void testBuildWriterWithNullContentTypeSerializer() throws JsonMappingException {
        final SerializationConfig serializationConfig = new SerializationConfig(new ObjectMapper());
        final BeanDescription beanDesc = new BeanDescription(new ClassName("com.test.TestClass"), null, null, null);
        final PropertyName propertyName = new PropertyName("testProperty");
        final BeanPropertyDefinition propDef = new BeanPropertyDefinition(propertyName, null, new ClassName("java.lang.String"), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        final JavaType declaredType = new TypeReference<Object>() {}.getType();
        final JsonSerializer<?> ser = new JsonSerializer<Object>() {};
        final TypeSerializer typeSer = new StandardSerializer<>(String.class);
        final AnnotatedMember am = new AnnotatedMember(new AnnotatedClass("com.test.TestClass"), propertyName, null, null, null, null, null);

        final PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDesc);
        final BeanPropertyWriter result = propertyBuilder.buildWriter(new Provider(), propDef, declaredType, ser, typeSer, null, am, false);

        assertNotNull(result);
    }

    @Test
    public void testBuildWriterWithEmptyContentTypeSerializer() throws JsonMappingException {
        final SerializationConfig serializationConfig = new SerializationConfig(new ObjectMapper());
        final BeanDescription beanDesc = new BeanDescription(new ClassName("com.test.TestClass"), null, null, null);
        final PropertyName propertyName = new PropertyName("testProperty");
        final BeanPropertyDefinition propDef = new BeanPropertyDefinition(propertyName, null, new ClassName("java.lang.String"), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        final JavaType declaredType = new TypeReference<Object>() {}.getType();
        final JsonSerializer<?> ser = new JsonSerializer<Object>() {};
        final TypeSerializer typeSer = new StandardSerializer<>(String.class);
        final TypeSerializer contentTypeSer = new StandardSerializer<>(new TypeReference<Collection<Integer>>() {}.getType());
        final AnnotatedMember am = new AnnotatedMember(new AnnotatedClass("com.test.TestClass"), propertyName, null, null, null, null, null);

        final PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDesc);
        final BeanPropertyWriter result = propertyBuilder.buildWriter(new Provider(), propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNotNull(result);
    }

    @Test
    public void testGetClassAnnotations() {
        final BeanDescription beanDesc = new BeanDescription(new ClassName("com.test.TestClass"), null, null, null);

        final PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(new ObjectMapper()), beanDesc);
        final Annotations result = propertyBuilder.getClassAnnotations();

        assertNotNull(result);
    }
}