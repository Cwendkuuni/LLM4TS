package com.example.test;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyBuilder;
import org.junit.Assert;
import org.junit.Test;

import static com.fasterxml.jackson.databind.node.TextNode.valueOf;
import static org.mockito.Mockito.*;

public class PropertyBuilderTest {

    

    @Test
    public void testBuildWriter_WithNullParameter() {
        SerializationConfig config = mock(SerializationConfig.class);
        BeanDescription beanDesc = mock(BeanDescription.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Assert.assertNull(propertyBuilder.buildWriter(null, null, null, null, null, null, null, false));
    }

    @Test
    public void testFindSerializationType_WithValidAnnotations() {
        JavaType declaredType = TypeFactory.defaultInstance().constructType(Object[].class);
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(annotatedMember.getGenericType()).thenReturn(declaredType);
        when(annotatedMember.getType()).thenReturn(declaredType);

        SerializationConfig config = mock(SerializationConfig.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);

        JavaType serializationType = propertyBuilder.findSerializationType(annotatedMember, false, declaredType);
        Assert.assertEquals(declaredType, serializationType);
    }

    @Test
    public void testFindSerializationType_WithInvalidAnnotations() {
        JavaType declaredType = TypeFactory.defaultInstance().constructType(Object[].class);
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(annotatedMember.getGenericType()).thenReturn(declaredType);
        when(annotatedMember.getType()).thenReturn(declaredType);

        SerializationConfig config = mock(SerializationConfig.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);

        JavaType serializationType = propertyBuilder.findSerializationType(annotatedMember, false, declaredType);
        Assert.assertNull(serializationType);
    }

    @Test
    public void testFindSerializationType_WithDefaultTypes() {
        JavaType declaredType = TypeFactory.defaultInstance().constructType(int[].class);
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(annotatedMember.getGenericType()).thenReturn(declaredType);
        when(annotatedMember.getType()).thenReturn(declaredType);

        SerializationConfig config = mock(SerializationConfig.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, null);

        JavaType serializationType = propertyBuilder.findSerializationType(annotatedMember, true, declaredType);
        Assert.assertEquals(int[].class, serializationType.getRawClass());
    }

    @Test
    public void testGetClassAnnotations() {
        BeanDescription beanDescription = mock(BeanDescription.class);
        when(beanDescription.getClassAnnotations()).thenReturn(new Annotations());

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        Annotations classAnnotations = propertyBuilder.getClassAnnotations();
        Assert.assertEquals(beanDescription.getClassAnnotations(), classAnnotations);
    }

    @Test
    public void testBuildWriter_WithValidParameters() {
        SerializationConfig config = mock(SerializationConfig.class);
        BeanDescription beanDesc = mock(BeanDescription.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<?> jsonSerializer = mock(JsonSerializer.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        when(am.getValue()).thenReturn(valueOf("Test"));

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(config, propDef, declaredType, jsonSerializer, typeSerializer, contentTypeSerializer, am, false);

        Assert.assertNotNull(beanPropertyWriter);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildWriter_WithEmptyContentType() {
        SerializationConfig config = mock(SerializationConfig.class);
        BeanDescription beanDesc = mock(BeanDescription.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String[].class);
        JsonSerializer<?> jsonSerializer = mock(JsonSerializer.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(config, propDef, declaredType, jsonSerializer, typeSerializer, null, am, false);
    }

    @Test
    public void testGetDefaultBean() {
        BeanDescription beanDescription = mock(BeanDescription.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        Object defaultBean = propertyBuilder.getDefaultBean();
        Assert.assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue_WithStringProperty() {
        BeanDescription beanDescription = mock(BeanDescription.class);
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(annotatedMember.getName()).thenReturn("testProperty");
        when(beanDescription.instantiateBean(true)).thenReturn("Default Value");

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        String propertyDefaultValue = (String) propertyBuilder.getPropertyDefaultValue("testProperty", annotatedMember, TypeFactory.defaultInstance().constructType(String.class));
        Assert.assertEquals("Default Value", propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_WithArrayProperty() {
        BeanDescription beanDescription = mock(BeanDescription.class);
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(annotatedMember.getName()).thenReturn("testProperty");
        when(beanDescription.instantiateBean(true)).thenReturn(new Object[]{});

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("testProperty", annotatedMember, TypeFactory.defaultInstance().constructType(Object[].class));
        Assert.assertEquals(JsonInclude.Include.NON_EMPTY, propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_WithNullProperty() {
        BeanDescription beanDescription = mock(BeanDescription.class);
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("testProperty", annotatedMember, TypeFactory.defaultInstance().constructType(String.class));
        Assert.assertNull(propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_WithNullDefaultBean() {
        BeanDescription beanDescription = mock(BeanDescription.class);
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(annotatedMember.getName()).thenReturn("testProperty");

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("testProperty", annotatedMember, TypeFactory.defaultInstance().constructType(String.class));
        Assert.assertNull(propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_WithNullValue() {
        BeanDescription beanDescription = mock(BeanDescription.class);
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(annotatedMember.getName()).thenReturn("testProperty");
        when(beanDescription.instantiateBean(true)).thenReturn(null);

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("testProperty", annotatedMember, TypeFactory.defaultInstance().constructType(String.class));
        Assert.assertNull(propertyDefaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_WithObjectProperty() {
        BeanDescription beanDescription = mock(BeanDescription.class);
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(annotatedMember.getName()).thenReturn("testProperty");
        when(beanDescription.instantiateBean(true)).thenReturn(new Object());

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue("testProperty", annotatedMember, TypeFactory.defaultInstance().constructType(Object.class));
        Assert.assertNotNull(propertyDefaultValue);
    }

    
}