package com.fasterxml.jackson.databind.ser; // same as the class package

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class PropertyBuilderTest {

    private SerializationConfig _config;
    private BeanDescription _beanDesc;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() {
        _config = mock(SerializationConfig.class);
        _beanDesc = mock(BeanDescription.class);
        propertyBuilder = new PropertyBuilder(_config, _beanDesc);
    }

    // Test for getClassAnnotations() method
    @Test
    public void testGetClassAnnotations() {
        Object classAnnotations = propertyBuilder.getClassAnnotations();
        Assert.assertNotNull(classAnnotations);
    }

    // Test for buildWriter() method with default arguments
    @Test
    public void testBuildWriterWithDefaultArguments() throws JsonMappingException {
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
        Assert.assertNotNull(writer);
    }

    // Test for buildWriter() method with custom arguments
    @Test
    public void testBuildWriterWithCustomArguments() throws JsonMappingException {
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);

        JavaType serializationType = mock(JavaType.class);
        when(serializationType.getContentType()).thenReturn(mock(JavaType.class));

        Object valueToSuppress = new Object() {};
        when(am.getGenericType()).thenReturn(TypeFactory.unknownType());

        propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        // Check if the correct type is used for serialization
        verify(typeSer).writeValue(eq(prov), eq(propDef), eq(ser), any(Object.class), any(Object.class));
        verify(contentTypeSer).writeValue(eq(prov), eq(declaredType), any(Object.class), any(Object.class), eq(propDef.getName()));

        // Check if the correct value is suppressed
        verify(ser, times(1)).setSerializerProvider(eq(prov));
        verify(ser, times(1)).setValue(eq(propDef), eq(valueToSuppress));
    }

    // Test for findSerializationType() method
    @Test
    public void testFindSerializationType() throws JsonMappingException {
        Annotated a = mock(Annotated.class);
        JavaType declaredType = mock(JavaType.class);

        JavaType secondary = mock(JavaType.class);
        when(secondary.getRawClass()).thenReturn(Integer.TYPE);
        when(_annotationIntrospector.refineSerializationType(_config, a, declaredType)).thenReturn(secondary);

        propertyBuilder.findSerializationType(a, false, declaredType);

        // Check if the correct type is returned
        verify(secondary).getRawClass();
    }

    // Test for getDefaultBean() method
    @Test
    public void testGetDefaultBean() {
        propertyBuilder.getDefaultBean();

        // Check if the default bean is instantiated
        verify(_beanDesc).instantiateBean(_config.canOverrideAccessModifiers());
    }

    // Test for getPropertyDefaultValue() method
    @Test
    public void testGetPropertyDefaultValue() {
        String name = "name";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);

        Object defaultBean = new Object() {};
        when(defaultBean.getClass()).thenReturn(String.class);
        when(member.getValue(defaultBean)).thenReturn("default");

        Object result = propertyBuilder.getPropertyDefaultValue(name, member, type);

        // Check if the correct value is returned
        assertEquals("default", result);
    }

    // Test for getDefaultValue() method
    @Test
    public void testGetDefaultValue() {
        JavaType type = TypeFactory.constructType(Map.class, String.class, Integer.class);

        Object result = propertyBuilder.getDefaultValue(type);

        // Check if the correct value is returned
        assertEquals(JsonInclude.Include.NON_EMPTY, result);
    }

    // Test for _throwWrapped() method
    @Test(expected = IllegalArgumentException.class)
    public void testThrowWrappedException() {
        String propName = "propName";
        Object defaultBean = new Object() {};

        Exception e = new Exception("Exception message");
        propertyBuilder._throwWrapped(e, propName, defaultBean);
    }

    // Edge cases and exception handling tests

    // Test for buildWriter() method with null arguments
    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriterWithNullArguments() {
        propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
    }

    // Test for buildWriter() method with null BeanPropertyDefinition
    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriterWithNullBeanPropertyDefinition() {
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = null;
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);

        propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
    }

    // Test for buildWriter() method with null SerializerProvider
    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriterWithNullSerializerProvider() {
        SerializerProvider prov = null;
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);

        propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
    }

    // Test for buildWriter() method with null AnnotatedMember
    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriterWithNullAnnotatedMember() {
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = null;

        propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
    }

    // Test for buildWriter() method with non-Boolean valueToSuppress
    @Test
    public void testBuildWriterWithNonBooleanValueToSuppress() throws JsonMappingException {
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);

        propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);

        // Check if default behavior is used for valueToSuppress
        verify(ser, times(1)).setValue(eq(propDef), eq(null));
    }

    // Test for buildWriter() method with null typeSer
    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriterWithNullTypeSerializer() {
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = null;
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);

        propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
    }

    // Test for buildWriter() method with null contentTypeSer
    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriterWithNullContentTypeSerializer() {
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = null;
        AnnotatedMember am = mock(AnnotatedMember.class);

        propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, false);
    }

    // Test for findSerializationType() method with null a
    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationTypeWithNullAnnotated() {
        SerializationConfig config = mock(SerializationConfig.class);
        Annotated a = null;
        JavaType declaredType = mock(JavaType.class);

        propertyBuilder.findSerializationType(a, false, declaredType);
    }

    // Test for findSerializationType() method with null declaredType
    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationTypeWithNullDeclaredType() {
        SerializationConfig config = mock(SerializationConfig.class);
        Annotated a = mock(Annotated.class);
        JavaType declaredType = null;

        propertyBuilder.findSerializationType(a, false, declaredType);
    }

    // Test for getPropertyDefaultValue() method with null name
    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueWithNullName() {
        String name = null;
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);

        propertyBuilder.getPropertyDefaultValue(name, member, type);
    }

    // Test for getPropertyDefaultValue() method with null AnnotatedMember
    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueWithNullAnnotatedMember() {
        String name = "name";
        AnnotatedMember member = null;
        JavaType type = mock(JavaType.class);

        propertyBuilder.getPropertyDefaultValue(name, member, type);
    }

    // Test for getPropertyDefaultValue() method with null type
    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValueWithNullType() {
        String name = "name";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = null;

        propertyBuilder.getPropertyDefaultValue(name, member, type);
    }

    // Test for getDefaultValue() method with null JavaType
    @Test
    public void testGetDefaultValueWithNullJavaType() {
        JavaType type = null;

        propertyBuilder.getDefaultValue(type);

        // Check if default value is returned
        assertEquals(JsonInclude.Include.NON_EMPTY, propertyBuilder.getDefaultValue(TypeFactory.constructType(Map.class, String.class, Integer.class)));
    }
}