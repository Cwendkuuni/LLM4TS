package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.findSerializationType(annotatedMember, false, javaType));

        JsonSerialize annotation = Mockito.mock(JsonSerialize.class);
        AnnotationIntrospector annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
        when(annotationIntrospector.findSerializationType(_eq(null), annotatedMember, javaType)).thenReturn(TypeFactory.defaultInstance().constructType(Integer.class));
        when(annotationIntrospector.findSerializationTyping(annotatedMember)).thenReturn(JsonSerialize.Typing.STATIC);
        when(annotation.getClass()).thenReturn(JsonSerialize.class);
        when(annotatedMember.getAnnotations()).thenReturn(new Annotation[0]);

        JavaType serializationType = propertyBuilder.findSerializationType(annotatedMember, true, javaType);
        assertEquals(Integer.TYPE, serializationType.getRawType());
    }

    @Test
    public void testFindSerializationType_noAnnotation() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.findSerializationType(annotatedMember, false, javaType));
        assertNull(propertyBuilder.findSerializationType(annotatedMember, true, javaType));
    }

    @Test
    public void testFindSerializationType_sameType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        JsonSerialize annotation = Mockito.mock(JsonSerialize.class);
        when(annotation.getClass()).thenReturn(JsonSerialize.class);
        when(annotation.type()).thenReturn(JsonSerialize.Typing.DEFAULT_TYPING);
        when(annotatedMember.getAnnotations()).thenReturn(new Annotation[]{annotation});

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType serializationType = propertyBuilder.findSerializationType(annotatedMember, true, javaType);
        assertEquals(javaType, serializationType);
    }

    @Test
    public void testFindSerializationType_superType() {
        JavaType javaTypeParent = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType javaTypeChild = TypeFactory.defaultInstance().constructType(Integer[].class);
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        JsonSerialize annotation = Mockito.mock(JsonSerialize.class);
        when(annotation.getClass()).thenReturn(JsonSerialize.class);
        when(annotation.type()).thenReturn(JsonSerialize.Typing.STATIC);
        when(annotatedMember.getAnnotations()).thenReturn(new Annotation[]{annotation});

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType serializationType = propertyBuilder.findSerializationType(annotatedMember, true, javaTypeChild);
        assertEquals(javaTypeParent, serializationType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationType_illegalType() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        JsonSerialize annotation = Mockito.mock(JsonSerialize.class);
        when(annotation.getClass()).thenReturn(JsonSerialize.class);
        when(annotatedMember.getAnnotations()).thenReturn(new Annotation[]{annotation});

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JavaType serializationType = propertyBuilder.findSerializationType(annotatedMember, true, TypeFactory.defaultInstance().constructType(Float.class));
    }

    @Test
    public void testBuildWriter() {
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<String> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNotNull(bpw);
    }

    @Test
    public void testBuildWriter_nullValueToSuppress() {
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<String> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertNull(bpw.getValueToSuppress());
    }

    @Test
    public void testBuildWriter_defaultValue() {
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<String> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        BeanPropertyDefinition propDefAnnotation = Mockito.mock(BeanPropertyDefinition.class);
        when(propDef.getName()).thenReturn("testProperty");
        when(propDef.getAnnotation()).thenReturn(propDefAnnotation);
        when(propDefAnnotation.getDefaultValue()).thenReturn("defaultValue");
        JavaType serializationType = TypeFactory.defaultInstance().constructType(String.class);

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertEquals("defaultValue", bpw.getValueToSuppress());
    }

    @Test
    public void testBuildWriter_suppressNulls() {
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<String> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertFalse(bpw.suppressNulls());

        BeanPropertyDefinition propDefAnnotation = Mockito.mock(BeanPropertyDefinition.class);
        when(propDef.getName()).thenReturn("testProperty");
        when(propDef.getAnnotation()).thenReturn(propDefAnnotation);
        when(propDefAnnotation.getAnnotation(JsonInclude.class)).thenReturn(JsonInclude.Include.NON_NULL);
        JavaType serializationType = TypeFactory.defaultInstance().constructType(String.class);

        bpw = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertTrue(bpw.suppressNulls());
    }

    @Test
    public void testBuildWriter_suppressEmptyCollections() {
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JsonSerializer<ArrayList> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertFalse(bpw.suppressEmptyCollections());

        BeanPropertyDefinition propDefAnnotation = Mockito.mock(BeanPropertyDefinition.class);
        when(propDef.getName()).thenReturn("testProperty");
        when(propDef.getAnnotation()).thenReturn(propDefAnnotation);
        when(propDefAnnotation.getAnnotation(JsonInclude.class)).thenReturn(JsonInclude.Include.NON_EMPTY);
        JavaType serializationType = TypeFactory.defaultInstance().constructType(ArrayList.class);

        bpw = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertTrue(bpw.suppressEmptyCollections());
    }

    @Test
    public void testBuildWriter_suppressNullsAndEmptyCollections() {
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(ArrayList.class);
        JsonSerializer<ArrayList> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        BeanPropertyWriter bpw = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertFalse(bpw.suppressNulls());
        assertFalse(bpw.suppressEmptyCollections());

        BeanPropertyDefinition propDefAnnotation = Mockito.mock(BeanPropertyDefinition.class);
        when(propDef.getName()).thenReturn("testProperty");
        when(propDef.getAnnotation()).thenReturn(propDefAnnotation);
        when(propDefAnnotation.getAnnotation(JsonInclude.class)).thenReturn(JsonInclude.Include.NON_NULL);
        JavaType serializationType = TypeFactory.defaultInstance().constructType(ArrayList.class);

        bpw = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertTrue(bpw.suppressNulls());
        assertFalse(bpw.suppressEmptyCollections());

        when(propDefAnnotation.getAnnotation(JsonInclude.class)).thenReturn(JsonInclude.Include.NON_EMPTY);
        bpw = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertTrue(bpw.suppressNulls());
        assertTrue(bpw.suppressEmptyCollections());
    }

    @Test
    public void testGetClassAnnotations() {
        BeanDescription beanDesc = Mockito.mock(BeanDescription.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }
}