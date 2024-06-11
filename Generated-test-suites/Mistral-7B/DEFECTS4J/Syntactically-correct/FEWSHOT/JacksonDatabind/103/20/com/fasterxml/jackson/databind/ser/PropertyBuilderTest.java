package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyWriter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.junit.Test;

import static org.junit.Assert.*;

public class PropertyBuilderTest {

    @Test
    public void testBuildWriter_Default() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        JavaType declaredType = TypeFactory.defaultInstance().unresolvedSerializerType(String.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("propertyName", declaredType);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);

        BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(
                Mockito.mock(SerializerProvider.class),
                propDef,
                declaredType,
                ser,
                typeSer,
                contentTypeSer,
                am,
                false);

        assertNotNull(propertyWriter);
    }

    @Test
    public void testBuildWriter_WithSerializationType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        JavaType declaredType = TypeFactory.defaultInstance().unresolvedSerializerType(String.class);
        JavaType serializationType = TypeFactory.defaultInstance().constructType(String[].class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("propertyName", declaredType);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);

        when(am.getGenericType()).thenReturn(serializationType);

        BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(
                Mockito.mock(SerializerProvider.class),
                propDef,
                declaredType,
                ser,
                typeSer,
                contentTypeSer,
                am,
                false);

        assertNotNull(propertyWriter);
        assertEquals(serializationType, propertyWriter.getSerializerType());
    }

    @Test
    public void testBuildWriter_WithContentTypeSerializer() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        JavaType declaredType = TypeFactory.defaultInstance().unresolvedSerializerType(String.class);
        JavaType serializationType = TypeFactory.defaultInstance().constructType(String.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("propertyName", declaredType);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);

        when(am.getGenericType()).thenReturn(declaredType);
        when(typeSer.createSerializerInstance(declaredType)).thenReturn(ser);

        BeanPropertyWriter propertyWriter = propertyBuilder.buildWriter(
                Mockito.mock(SerializerProvider.class),
                propDef,
                declaredType,
                ser,
                typeSer,
                contentTypeSer,
                am,
                false);

        assertNotNull(propertyWriter);
        assertNull(propertyWriter.getContentSerializer());

        TypeSerializer contentTypeSerMock = Mockito.mock(TypeSerializer.class);
        BeanPropertyWriter propertyWriterWithContentSerializer = propertyBuilder.buildWriter(
                Mockito.mock(SerializerProvider.class),
                propDef,
                declaredType,
                ser,
                typeSer,
                contentTypeSerMock,
                am,
                false);

        assertNotNull(propertyWriterWithContentSerializer);
        assertNotNull(propertyWriterWithContentSerializer.getContentSerializer());
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = TypeFactory.defaultInstance().unresolvedSerializerType(String.class);

        when(am.getRawType()).thenReturn(Integer.class);

        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);

        assertNull(serializationType);

        when(am.getRawType()).thenReturn(String[].class);

        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);

        assertNull(serializationType);

        JavaType jsonType = TypeFactory.defaultInstance().constructType(String.class, Integer.class);

        when(am.getRawType()).thenReturn(jsonType);

        JsonSerialize annotation = Mockito.mock(JsonSerialize.class);
        when(annotation.type()).thenReturn(JsonSerialize.Typing.STATIC);

        when(am.getAnnotation(JsonSerialize.class)).thenReturn(annotation);

        serializationType = propertyBuilder.findSerializationType(am, true, declaredType);

        assertEquals(jsonType, serializationType);
    }

    @Test
    public void testFindSerializationType_WithJsonType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = TypeFactory.defaultInstance().unresolvedSerializerType(String.class);

        when(am.getRawType()).thenReturn(Map.class);
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(null);

        JavaType jsonType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);

        JsonSerialize jsonTypeAnnotation = Mockito.mock(JsonSerialize.class);
        when(jsonTypeAnnotation.type()).thenReturn(JsonSerialize.Typing.STATIC);
        when(jsonTypeAnnotation.contentAsType()).thenReturn(Integer.class);

        when(am.getAnnotation(JsonTypeNameSerializer.class)).thenReturn(jsonTypeAnnotation);

        JavaType serializationType = propertyBuilder.findSerializationType(am, true, declaredType);

        assertEquals(jsonType, serializationType);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindSerializationType_WithInvalidJsonType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = TypeFactory.defaultInstance().unresolvedSerializerType(String.class);

        when(am.getRawType()).thenReturn(Map.class);
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(null);
        when(am.getAnnotation(JsonTypeNameSerializer.class)).thenReturn(null);

        JavaType jsonType = TypeFactory.defaultInstance().constructType(Map.class, String.class, Integer.class);

        propertyBuilder.findSerializationType(am, true, declaredType);

        fail("Expected JsonMappingException");
    }

    @Test
    public void testGetClassAnnotations() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Annotations annotations = propertyBuilder.getClassAnnotations();

        assertTrue(annotations.isEmpty());

        Annotation annotation = Mockito.mock(JsonInclude.class);
        when(annotation.value()).thenReturn(JsonInclude.Include.NON_DEFAULT);

        Annotations annotationsWithAnnotation = propertyBuilder.getClassAnnotations();

        assertFalse(annotationsWithAnnotation.isEmpty());
        assertEquals(annotation, annotationsWithAnnotation.getAnnotation(JsonInclude.class));
    }
}