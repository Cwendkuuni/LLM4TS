package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getAnnotations()).thenReturn(new JsonSerializeAnnotation(JsonSerialize.Typing.STATIC));
        JavaType declaredType = TypeFactory.type(String.class);

        JavaType result = propertyBuilder.findSerializationType(am, true, declaredType);
        assertEquals(declaredType.withStaticTyping(), result);

        when(am.getAnnotations()).thenReturn(new JsonSerializeAnnotation(JsonSerialize.Typing.DYNAMIC));
        result = propertyBuilder.findSerializationType(am, true, declaredType);
        assertNull(result);

        when(am.getAnnotations()).thenReturn(Mockito.mock(JsonInclude.class));
        result = propertyBuilder.findSerializationType(am, true, declaredType);
        assertNull(result);

        when(am.getAnnotations()).thenReturn(new JsonSerializeAnnotation(JsonSerialize.Typing.DEFAULT_TYPING));
        result = propertyBuilder.findSerializationType(am, true, declaredType);
        assertNull(result);

        when(am.getAnnotations()).thenReturn(new JsonSerializeAnnotation(JsonSerialize.Typing.STATIC, String.class));
        result = propertyBuilder.findSerializationType(am, true, declaredType);
        assertEquals(TypeFactory.type(String.class, String.class), result);
    }

    @Test
    public void testBuildWriter() {
        SerializationConfig config = new SerializationConfig(null);
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getName()).thenReturn("PropertyName");
        when(am.getRawType()).thenReturn(Integer.class);

        JavaType declaredType = TypeFactory.type(String.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);

        BeanPropertyWriter result = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class),
                Mockito.mock(BeanPropertyDefinition.class), declaredType, ser, typeSer, contentTypeSer, am, false);

        verify(ser, times(1)).serializeValues(anyObject(), eq(config), anyObject(), anyObject(), anyObject(), anyObject());
        verify(typeSer, times(1)).writeTypePrefix(anyObject(), anyObject());
        verify(contentTypeSer, times(1)).writeTypePrefix(anyObject(), anyObject());
        verify(result, times(1)).setNullSerializer(eq(ser));
    }

    @Test
    public void testFindSerializationTypeWithUnwrapping() {
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getName()).thenReturn("PropertyName");
        when(am.getRawType()).thenReturn(Wrapper.class);
        when(am.getAnnotations()).thenReturn(new JsonSerializeAnnotation(JsonSerialize.Typing.STATIC));

        JavaType declaredType = TypeFactory.type(String.class);
        JavaType wrapperType = TypeFactory.type(Wrapper.class);
        JavaType rawType = TypeFactory.type(String.class);

        ObjectNameTransformer unwrapper = Mockito.mock(ObjectNameTransformer.class);
        when(unwrapper.transform(eq("PropertyName"), anyObject())).thenReturn("WrappedPropertyName");

        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        when(typeSer.writeTypePrefix(anyObject(), anyObject())).thenReturn(TypeSerializer.emptyValueSerializer);
        when(contentTypeSer.writeTypePrefix(anyObject(), anyObject())).thenReturn(TypeSerializer.emptyValueSerializer);

        SerializerProvider prov = Mockito.mock(SerializerProvider.class);
        when(prov.findValueSerializer(eq(String.class), anyObject())).thenReturn(Mockito.mock(JsonSerializer.class));

        BeanPropertyWriter result = propertyBuilder.buildWriter(prov,
                Mockito.mock(BeanPropertyDefinition.class), declaredType, null, typeSer, contentTypeSer, am, false);

        verify(prov, times(1)).findValueSerializer(eq(String.class), eq("WrappedPropertyName"));
    }

    @Test
    public void testBuildWriterWithNullSerializerDef() {
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);

        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getName()).thenReturn("PropertyName");
        when(am.getRawType()).thenReturn(Integer.class);

        JavaType declaredType = TypeFactory.type(String.class);
        JsonSerializer<?> ser = null;
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);

        BeanPropertyWriter result = propertyBuilder.buildWriter(Mockito.mock(SerializerProvider.class),
                Mockito.mock(BeanPropertyDefinition.class), declaredType, ser, typeSer, contentTypeSer, am, false);

        assertNull(result.getNullSerializer());
    }

    @Test
    public void testGetClassAnnotations() {
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);

        Annotations result = propertyBuilder.getClassAnnotations();
        assertEquals(beanDesc.getClassAnnotations(), result);
    }

    @Test
    public void testGetPropertiesDefaultInclusion() {
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);

        JsonInclude.Value result = propertyBuilder.getDefaultInclusion();
        JsonInclude.Value expected = JsonInclude.Value.empty();
        assertEquals(expected, result);

        beanDesc.setDefaultPropertyInclusion(JsonInclude.Include.ALWAYS);
        propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);
        result = propertyBuilder.getDefaultInclusion();
        expected = JsonInclude.Value.construct(JsonInclude.Include.ALWAYS, null);
        assertEquals(expected, result);

        beanDesc.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);
        result = propertyBuilder.getDefaultInclusion();
        expected = JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, null);
        assertEquals(expected, result);

        beanDesc.setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY);
        propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);
        result = propertyBuilder.getDefaultInclusion();
        expected = JsonInclude.Value.construct(JsonInclude.Include.NON_EMPTY, null);
        assertEquals(expected, result);

        beanDesc.setDefaultPropertyInclusion(JsonInclude.Include.NON_ABSENT);
        propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);
        result = propertyBuilder.getDefaultInclusion();
        expected = JsonInclude.Value.construct(JsonInclude.Include.NON_ABSENT, null);
        assertEquals(expected, result);

        beanDesc.setDefaultPropertyInclusion(JsonInclude.Include.CUSTOM);
        propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);
        result = propertyBuilder.getDefaultInclusion();
        expected = JsonInclude.Value.construct(JsonInclude.Include.CUSTOM, null);
        assertEquals(expected, result);
    }

    @Test
    public void testDetermineDefaultBean() {
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);

        Object result = propertyBuilder.getDefaultBean();
        assertNull(result);

        beanDesc.setDefaultInstance(new Object());
        propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);
        result = propertyBuilder.getDefaultBean();
        assertEquals(beanDesc.getDefaultInstance(), result);

        beanDesc.setDefaultInstance(null);
        propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);
        result = propertyBuilder.getDefaultBean();
        assertNull(result);
    }

    @Test
    public void testGetPropertyDefaultValue() {
        BeanDescription beanDesc = new BeanDescription();
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null), beanDesc);

        String name = "PropertyName";
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getName()).thenReturn(name);
        when(am.getValue(anyObject())).thenReturn("Value");

        JavaType type = TypeFactory.type(String.class);

        Object result = propertyBuilder.getPropertyDefaultValue(name, am, type);
        assertEquals("Value", result);

        when(am.getValue(anyObject())).thenThrow(new IllegalAccessException());
        result = propertyBuilder.getPropertyDefaultValue(name, am, type);
        assertNull(result);
    }

    @Test
    public void testGetDefaultValue() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(new SerializationConfig(null), null);

        Object result = propertyBuilder.getDefaultValue(TypeFactory.type(String[].class));
        assertArrayEquals(new Object[0], result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(String.class));
        assertEquals(null, result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(int[].class));
        assertArrayEquals(new Integer[0], result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(int.class));
        assertEquals(0, result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(boolean.class));
        assertEquals(false, result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(Boolean.class));
        assertEquals(false, result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(long.class));
        assertEquals(0L, result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(Long.class));
        assertEquals(0L, result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(double.class));
        assertEquals(0.0, result, 0.001);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(Double.class));
        assertEquals(0.0, result, 0.001);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(float.class));
        assertEquals(0.0f, result, 0.001f);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(Float.class));
        assertEquals(0.0f, result, 0.001f);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(char.class));
        assertEquals('\u0000', result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(Character.class));
        assertEquals('\u0000', result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(byte[].class));
        assertArrayEquals(new Byte[0], result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(byte.class));
        assertEquals((byte) 0, result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(Byte.class));
        assertEquals((byte) 0, result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(short[].class));
        assertArrayEquals(new Short[0], result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(short.class));
        assertEquals((short) 0, result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(Short.class));
        assertEquals((short) 0, result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(boolean[].class));
        assertArrayEquals(new Boolean[0], result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(boolean.class));
        assertEquals(false, result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(Boolean.class));
        assertEquals(false, result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(String.class, String[].class));
        assertEquals(new String[0], result);

        result = propertyBuilder.getDefaultValue(TypeFactory.type(String.class, String[].class, String[].class));
        assertEquals(new String[0][], result);
    }
}