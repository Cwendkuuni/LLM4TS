package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyBuilderTest {

    private PropertyBuilder propertyBuilder;
    private ObjectMapper objectMapper;
    private BeanDescription beanDescription;
    private SerializationConfig serializationConfig;

    @Before
    public void setUp() {
        serializationConfig = mock(SerializationConfig.class);
        beanDescription = mock(BeanDescription.class);
        objectMapper = mock(ObjectMapper.class);
        when(objectMapper.getSerializationConfig()).thenReturn(serializationConfig);
        when(serializationConfig.getDefaultPropertyInclusion()).thenReturn(JsonInclude.Value.empty());
        propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
    }

    @Test
    public void testConstructor() {
        // Empty constructor test
        assertNotNull(new PropertyBuilder(null, null));

        // Constructor with arguments test
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        when(beanDescription.getClassAnnotations()).thenReturn(mock(Annotations.class));
        Annotations annotations = propertyBuilder.getClassAnnotations();
        verify(beanDescription).getClassAnnotations();
        assertEquals(annotations, propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testBuildWriter_NullSerializer() {
        JsonSerializer<?> jsonSerializer = null;
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        BeanPropertyWriter builder = propertyBuilder.buildWriter(mock(SerializerProvider.class), propDef, declaredType, jsonSerializer, typeSerializer, null, mock(AnnotatedMember.class), false);
        assertNull(builder);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriter_BadPropertyDefinition() {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        JsonSerializer<?> jsonSerializer = mock(JsonSerializer.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        BeanPropertyDefinition propDef = null;
        JavaType declaredType = mock(JavaType.class);
        propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, null, mock(AnnotatedMember.class), false);
    }

    @Test
    public void testBuildWriter_DefaultUseStaticTypingFalse() {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        JsonSerializer<?> jsonSerializer = mock(JsonSerializer.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, contentTypeSerializer, am, false);
    }

    @Test
    public void testBuildWriter_DefaultUseStaticTypingTrue() {
        SerializerProvider serializerProvider = mock(SerializerProvider.class);
        JsonSerializer<?> jsonSerializer = mock(JsonSerializer.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = mock(JavaType.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, contentTypeSerializer, am, true);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindSerializationType_AnnotatedIsNull() {
        Annotated annotated = null;
        JavaType declaredType = mock(JavaType.class);
        propertyBuilder.findSerializationType(annotated, false, declaredType);
    }

    @Test
    public void testFindSerializationType_TypeIsNotAssignableFromDeclaredType() {
        Annotated annotated = mock(Annotated.class);
        JavaType declaredType = mock(JavaType.class);
        JavaType serializationType = new ClassType(Object.class);
        when(annotated.getType()).thenReturn(serializationType);
        when(serializationType.getRawClass()).thenReturn(String.class);
        when(declaredType.getRawClass()).thenReturn(Integer.class);
        propertyBuilder.findSerializationType(annotated, false, declaredType);
    }

    @Test
    public void testFindSerializationType_TypeIsAssignableFromDeclaredType() {
        Annotated annotated = mock(Annotated.class);
        JavaType declaredType = mock(JavaType.class);
        JavaType serializationType = new ClassType(Integer.class);
        when(annotated.getType()).thenReturn(serializationType);
        when(declaredType.getRawClass()).thenReturn(Integer.class);
        propertyBuilder.findSerializationType(annotated, false, declaredType);
    }

    @Test
    public void testFindSerializationType_TypeIsSubtypeOfDeclaredType() {
        Annotated annotated = mock(Annotated.class);
        JavaType declaredType = new ClassType(Number.class);
        JavaType serializationType = new ClassType(Integer.class);
        when(annotated.getType()).thenReturn(serializationType);
        when(declaredType.getRawClass()).thenReturn(Number.class);
        propertyBuilder.findSerializationType(annotated, false, declaredType);
    }

    @Test
    public void testGetDefaultBean() {
        Object defaultBean = new Object();
        propertyBuilder._defaultBean = defaultBean;
        Object result = propertyBuilder.getDefaultBean();
        assertEquals(defaultBean, result);
    }

    @Test
    public void testGetDefaultBean_Null() {
        Object result = propertyBuilder.getDefaultBean();
        assertNull(result);
    }

    @Test
    public void testGetPropertyDefaultValue_NullPropName() {
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        Object result = propertyBuilder.getPropertyDefaultValue(null, member, type);
        assertNull(result);
    }

    @Test
    public void testGetPropertyDefaultValue_StringPropName() {
        String propName = "testProp";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = mock(JavaType.class);
        Object defaultBean = new Object();
        Object result = propertyBuilder.getPropertyDefaultValue(propName, member, type);
        verify(propertyBuilder).getDefaultBean();
        try {
            Object memberValue = member.getValue(defaultBean);
            result = memberValue;
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
        assertEquals(result, propertyBuilder.getPropertyDefaultValue(propName, member, type));
    }

    @Test
    public void testGetPropertyDefaultValue_NonDefaultType() {
        String propName = "testProp";
        AnnotatedMember member = mock(AnnotatedMember.class);
        JavaType type = new ValueType(int[].class);
        Object result = propertyBuilder.getPropertyDefaultValue(propName, member, type);
        assertNull(result);
    }

    @Test
    public void testGetDefaultValue_NullType() {
        JavaType type = null;
        Object result = propertyBuilder.getDefaultValue(type);
        assertNull(result);
    }

    @Test
    public void testGetDefaultValue_SimpleType() {
        JavaType type = new ValueType(int.class);
        Object result = propertyBuilder.getDefaultValue(type);
        assertEquals(0, result.hashCode());
    }

    @Test
    public void testGetDefaultValue_ArrayType() {
        JavaType type = new ArrayType(int.class, 1);
        Object result = propertyBuilder.getDefaultValue(type);
        assertArrayEquals(new int[]{0}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDefaultValue_ComplexType() {
        JavaType type = new ClassType(ComplexType.class);
        propertyBuilder.getDefaultValue(type);
    }

    @Test
    public void testThrowWrapped_IllegalArgumentException() {
        Exception e = new IllegalArgumentException();
        String propName = "testProp";
        Object defaultBean = new Object();
        Object result = propertyBuilder._throwWrapped(e, propName, defaultBean);
        fail("IllegalArgumentException should not be wrapped");
    }

    @Test(expected = RuntimeException.class)
    public void testThrowWrapped_RuntimeException() {
        RuntimeException e = new RuntimeException();
        String propName = "testProp";
        Object defaultBean = new Object();
        propertyBuilder._throwWrapped(e, propName, defaultBean);
    }
}

class ComplexType {
    // Add your custom class implementation here
}