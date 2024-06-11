package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyBuilderTest {

    

    @Test
    public void testPropertyBuilderConstructor() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNotNull(propertyBuilder);
    }

    @Test
    public void testGetClassAnnotations() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotations classAnnotations = propertyBuilder.getClassAnnotations();
        assertNotNull(classAnnotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(new ClassName("com.test.MyClass"));
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object obj = new Object();
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("name", String.class, obj.getClass());
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;
        JavaType serializationType = TypeFactory.type(String.class, obj.getClass());
        BeanPropertyWriter expectedResult = mock(BeanPropertyWriter.class);
        when(am.getGenericType()).thenReturn(TypeFactory.defaultInstance().constructType(String[].class));
        when(typeSer.isContentTypeSerializable(eq(declaredType))).thenReturn(true);
        when(contentTypeSer.getContentType()).thenReturn(TypeFactory.defaultInstance().constructType(Integer.class));
        when(propertyBuilder.findSerializationType(am, defaultUseStaticTyping, declaredType)).thenReturn(serializationType);
        when(propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping))
                .thenReturn(expectedResult);

        BeanPropertyWriter actualResult = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildWriterWithIllegalConcreteTypeAnnotation() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(new ClassName("com.test.MyClass"));
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Object obj = new Object();
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("name", String.class, obj.getClass());
        JavaType declaredType = TypeFactory.defaultInstance().constructType(Integer.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;
        when(am.getRawType()).thenReturn(Long.class);

        propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationTypeWithIllegalSubtype() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        when(a.getName()).thenReturn("test");
        when(a.getRawType()).thenReturn(Object.class);
        when(propertyBuilder.findSerializationType(a, false, declaredType)).thenThrow(new IllegalArgumentException());
    }

    @Test
    public void testGetSerializationTypeWithNonNullDeclaredType() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType expectedResult = declaredType;
        JavaType actualResult = propertyBuilder.findSerializationType(null, false, declaredType);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetSerializationTypeWithAnnotatedTypeSupport() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(new ClassName("com.test.MyClassWithAnnotatedTypeSupport"));
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(Integer[].class);
        when(a.getName()).thenReturn("test");
        when(a.getRawType()).thenReturn(Integer[].class);
        when(propertyBuilder.findSerializationType(a, false, declaredType)).thenReturn(TypeFactory.type(String[].class, Integer[].class));

        JavaType actualResult = propertyBuilder.findSerializationType(a, false, declaredType);
        JavaType expectedResult = TypeFactory.type(String[].class, Integer[].class);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetSerializationTypeWithStaticTyping() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        when(a.getName()).thenReturn("test");
        when(a.getRawType()).thenReturn(Object.class);
        when(propertyBuilder.findSerializationType(a, true, declaredType)).thenReturn(declaredType.withStaticTyping());

        JavaType actualResult = propertyBuilder.findSerializationType(a, true, declaredType);
        JavaType expectedResult = declaredType.withStaticTyping();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetSerializationTypeWithNullDeclaredType() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType expectedResult = null;
        JavaType actualResult = propertyBuilder.findSerializationType(null, false, null);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetSerializationTypeWithNullAnnotated() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        JavaType expectedResult = null;
        JavaType actualResult = propertyBuilder.findSerializationType(null, false, TypeFactory.defaultInstance().constructType(String.class));
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetSerializationTypeWithMissingAnnotatedType() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(String.class);
        when(a.getName()).thenReturn("test");
        when(a.getRawType()).thenReturn(Object.class);
        when(propertyBuilder.findSerializationType(a, false, declaredType)).thenReturn(null);

        JavaType actualResult = propertyBuilder.findSerializationType(a, false, declaredType);
        JavaType expectedResult = null;
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetSerializationTypeWithJsonSerializeAnnotation() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(new ClassName("com.test.MyClassWithJsonSerializeAnnotation"));
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(Object.class);
        when(a.getName()).thenReturn("test");
        when(a.getRawType()).thenReturn(Object.class);
        when(propertyBuilder.findSerializationType(a, false, declaredType)).thenReturn(TypeFactory.type(String.class));

        JavaType actualResult = propertyBuilder.findSerializationType(a, false, declaredType);
        JavaType expectedResult = TypeFactory.type(String.class);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetSerializationTypeWithJsonSerializeAnnotationWithType() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(new ClassName("com.test.MyClassWithJsonSerializeAnnotationWithType"));
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(Integer.class);
        when(a.getName()).thenReturn("test");
        when(a.getRawType()).thenReturn(Integer.class);
        when(propertyBuilder.findSerializationType(a, false, declaredType)).thenReturn(TypeFactory.type(String.class, Integer.class));

        JavaType actualResult = propertyBuilder.findSerializationType(a, false, declaredType);
        JavaType expectedResult = TypeFactory.type(String.class, Integer.class);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetSerializationTypeWithJsonSerializeAnnotationWithStaticTyping() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(new ClassName("com.test.MyClassWithJsonSerializeAnnotationWithStaticTyping"));
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated a = mock(Annotated.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(Integer.class);
        when(a.getName()).thenReturn("test");
        when(a.getRawType()).thenReturn(Integer.class);
        when(propertyBuilder.findSerializationType(a, true, declaredType)).thenReturn(TypeFactory.type(String.class, Integer.class).withStaticTyping());

        JavaType actualResult = propertyBuilder.findSerializationType(a, true, declaredType);
        JavaType expectedResult = TypeFactory.type(String.class, Integer.class).withStaticTyping();
        assertEquals(expectedResult, actualResult);
    }

    
}