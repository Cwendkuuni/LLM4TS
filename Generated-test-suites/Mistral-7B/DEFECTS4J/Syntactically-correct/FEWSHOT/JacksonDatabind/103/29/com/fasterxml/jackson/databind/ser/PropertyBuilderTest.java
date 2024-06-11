package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = Mockito.mock(JavaType.class);
        JavaType type = Mockito.mock(JavaType.class);

        // Test with null annotations and static typing
        when(propertyBuilder._annotationIntrospector.refineSerializationType(null, member, declaredType)).thenReturn(type);
        when(declaredType.getRawClass()).thenReturn(String.class);
        when(type.getRawClass()).thenReturn(Integer.class);
        PropertyBuilder underTest = new PropertyBuilder(null, beanDesc);
        JavaType result = underTest.findSerializationType(member, false, declaredType);
        assertEquals(Integer.class, result.getRawClass());

        // Test with annotations and static typing
        JsonSerialize annotation = Mockito.mock(JsonSerialize.class);
        when(annotation.type()).thenReturn(JsonSerialize.Typing.STATIC);
        when(member.getAnnotation(JsonSerialize.class)).thenReturn(annotation);
        when(propertyBuilder._annotationIntrospector.refineSerializationType(null, member, declaredType)).thenReturn(type);
        when(declaredType.getRawClass()).thenReturn(String.class);
        when(type.getRawClass()).thenReturn(Integer.class);
        underTest = new PropertyBuilder(null, beanDesc);
        result = underTest.findSerializationType(member, true, declaredType);
        assertEquals(Integer.class, result.getRawClass());

        // Test with annotations and dynamic typing
        when(annotation.type()).thenReturn(JsonSerialize.Typing.DYNAMIC);
        result = underTest.findSerializationType(member, true, declaredType);
        assertNull(result);

        // Test with no annotations and static typing
        when(declaredType.getRawClass()).thenReturn(String.class);
        when(type.getRawClass()).thenReturn(Integer.class);
        result = underTest.findSerializationType(member, false, declaredType);
        assertNull(result);

        // Test with no annotations and dynamic typing
        result = underTest.findSerializationType(member, false, declaredType);
        assertNull(result);
    }

    @Test
    public void testBuildWriter() {
        ObjectWriter objectWriter = Mockito.mock(ObjectWriter.class);
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        when(provider.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)).thenReturn(false);

        // Test with no property definition
        BeanPropertyDefinition propDef = null;
        JavaType declaredType = Mockito.mock(JavaType.class);
        when(provider.findValueSerializer(Mockito.eq(declaredType), Mockito.eq(null), Mockito.eq(null)))
                .thenReturn(Mockito.mock(JsonSerializer.class));
        when(provider.getSerializerProvider()).thenReturn(provider);
        when(provider.getNullValueSerializer()).thenReturn(Mockito.mock(JsonSerializer.class));
        when(beanDesc.findPropertyDefinition(Mockito.eq("name"))).thenReturn(propDef);
        when(beanDesc.findBeanClass()).thenReturn(String.class);

        try {
            propertyBuilder.buildWriter(provider, propDef, declaredType, null, null, null, null, false);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("could not determine property type", e.getMessage());
        }

        // Test with a property definition and a valid serializer
        propDef = new BeanPropertyDefinition("name", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        when(beanDesc.findPropertyDefinition("name")).thenReturn(propDef);
        JsonSerializer<?> serializer = Mockito.mock(JsonSerializer.class);
        when(provider.findValueSerializer(Mockito.eq(declaredType), Mockito.eq(null), Mockito.eq(null)))
                .thenReturn(serializer);
        BeanPropertyWriter result = propertyBuilder.buildWriter(provider, propDef, declaredType, serializer, null, null, null, false);
        assertNotNull(result);
        verify(provider, times(1)).findValueSerializer(Mockito.eq(declaredType), Mockito.eq(null), Mockito.eq(null));

        // Test with a property definition and a TypeSerializer
        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);
        when(provider.findValueSerializer(Mockito.eq(declaredType), Mockito.eq(null), Mockito.eq(typeSerializer)))
                .thenReturn(serializer);
        result = propertyBuilder.buildWriter(provider, propDef, declaredType, serializer, null, typeSerializer, null, false);
        assertNotNull(result);
        verify(provider, times(1)).findValueSerializer(Mockito.eq(declaredType), Mockito.eq(null), Mockito.eq(typeSerializer));

        // Test with a property definition and a ContentTypeSerializer
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        when(declaredType.getContentType()).thenReturn(Mockito.mock(JavaType.class));
        when(declaredType.getContentType().getContentTypeHandler()).thenReturn(contentTypeSer);
        result = propertyBuilder.buildWriter(provider, propDef, declaredType, serializer, null, null, null, false);
        assertNotNull(result);
        verify(provider, times(1)).findValueSerializer(Mockito.eq(declaredType), Mockito.eq(contentTypeSer), Mockito.eq(null));
    }

    @Test
    public void testGetClassAnnotations() {
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);
        Annotation[] annotations = new Annotation[0];
        when(beanDesc.getClassAnnotations()).thenReturn(annotations);
        MapperFeature feature = Mockito.mock(MapperFeature.class);
        when(propertyBuilder._config.isEnabled(feature)).thenReturn(true);
        when(propertyBuilder._config.canOverrideAccessModifiers()).thenReturn(false);
        Class<?> clazz = Mockito.mock(Class.class);
        when(beanDesc.getClassInfo()).thenReturn(new BeanProperty.BeanPropertyInfo(clazz, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
        BeanProperty property = new BeanProperty("name", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        when(beanDesc.findPropertyDefinition("name")).thenReturn(property);
        AnnotatedMember member = Mockito.mock(AnnotatedMember.class);
        when(member.getName()).thenReturn("name");
        when(member.getMember()).thenReturn(property);

        Annotations result = propertyBuilder.getClassAnnotations();
        assertArrayEquals(annotations, result.getClassAnnotations());
        verify(property, times(1)).findClassAnnotations();
    }
}