package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        AnnotatedMember am =Mockito.mock(AnnotatedMember.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);
        JavaType declaredType = Mockito.mock(JavaType.class);

        // Test with null declaredType
        Assert.assertNull(propertyBuilder.findSerializationType(am, false, null));

        // Test with annotation present and useStaticTyping = false
        JsonSerialize annotation = Mockito.mock(JsonSerialize.class);
        when(annotation.type()).thenReturn(JavaType.construct(String[].class));
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(annotation);
        when(am.getRawType()).thenReturn(String[].class);
        JavaType secondary = Mockito.mock(JavaType.class);
        when(propertyBuilder.annotationIntrospector.refineSerializationType(null, am, null)).thenReturn(secondary);
        Assert.assertEquals(JavaType.construct(String[].class), propertyBuilder.findSerializationType(am, false, null));

        // Test with annotation present and useStaticTyping = true
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(annotation);
        when(am.getRawType()).thenReturn(String[].class);
        when(propertyBuilder.annotationIntrospector.findSerializationTyping(am)).thenReturn(JsonSerialize.Typing.STATIC);
        Assert.assertEquals(JavaType.construct(String[].class).withStaticTyping(), propertyBuilder.findSerializationType(am, true, declaredType));

        // Test with no annotations
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(null);
        Assert.assertNull(propertyBuilder.findSerializationType(am, true, declaredType));

        // Test with same type
        when(am.getAnnotation(JsonSerialize.class)).thenReturn(annotation);
        when(am.getRawType()).thenReturn(String[].class);
        Assert.assertEquals(declaredType, propertyBuilder.findSerializationType(am, false, declaredType));

        // Test with super type
        when(am.getRawType()).thenReturn(List.class);
        Assert.assertEquals(JavaType.construct(ArrayList.class), propertyBuilder.findSerializationType(am, true, declaredType));
    }

    @Test
    public void testBuildWriter() {
        BeanDescription beanDesc = new BeanDescription(Object.class);
        SerializerProvider prov = Mockito.mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(BeanPropertyDefinition.Kind.FIELD, null, null, null, null, null, null, null);
        JavaType declaredType = Mockito.mock(JavaType.class);
        JsonSerializer<?> ser = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember am =Mockito.mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = false;

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);

        // Test with null propDef
        Assert.assertNull(propertyBuilder.buildWriter(prov, null, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping));

        // Test with null declaredType
        Assert.assertNull(propertyBuilder.buildWriter(prov, propDef, null, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping));

        // Test with null ser
        Assert.assertNull(propertyBuilder.buildWriter(prov, propDef, declaredType, null, typeSer, contentTypeSer, am, defaultUseStaticTyping));

        // Test with null typeSer
        Assert.assertNull(propertyBuilder.buildWriter(prov, propDef, declaredType, ser, null, contentTypeSer, am, defaultUseStaticTyping));

        // Test with null contentTypeSer
        Assert.assertNull(propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, defaultUseStaticTyping));

        // Test with null am
        Assert.assertNull(propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, null, defaultUseStaticTyping));

        // Test with defaultUseStaticTyping = false
        when(am.getGenericType()).thenReturn(JavaType.construct(String.class));
        Assert.assertEquals(BeanPropertyWriter.class, propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping).getClass());

        // Test with defaultUseStaticTyping = true
        when(am.getGenericType()).thenReturn(JavaType.construct(String[].class));
        Assert.assertEquals(BeanPropertyWriter[].class, propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, true).getClass());

        // Test with no serializationType
        when(am.getGenericType()).thenReturn(JavaType.construct(String.class));
        when(propertyBuilder.findSerializationType(am, defaultUseStaticTyping, declaredType)).thenReturn(null);
        Assert.assertEquals(BeanPropertyWriter.class, propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping).getClass());

        // Test with serializationType = null
        when(am.getGenericType()).thenReturn(JavaType.construct(String.class));
        JavaType serializationType = Mockito.mock(JavaType.class);
        when(propertyBuilder.findSerializationType(am, defaultUseStaticTyping, declaredType)).thenReturn(serializationType);
        Assert.assertEquals(BeanPropertyWriter.class, propertyBuilder.buildWriter(prov, propDef, declaredType, ser, null, contentTypeSer, am, defaultUseStaticTyping).getClass());

        // Test with serializationType = declaredType
        when(am.getGenericType()).thenReturn(JavaType.construct(String.class));
        when(propertyBuilder.findSerializationType(am, defaultUseStaticTyping, declaredType)).thenReturn(declaredType);
        Assert.assertEquals(BeanPropertyWriter.class, propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping).getClass());

        // Test with serializationType = different type
        when(am.getGenericType()).thenReturn(JavaType.construct(String.class));
        JavaType expectedType = JavaType.construct(Integer.class);
        when(propertyBuilder.findSerializationType(am, defaultUseStaticTyping, declaredType)).thenReturn(expectedType);
        Assert.assertEquals(BeanPropertyWriter.class, propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping).getClass());

        // Test with contentTypeSer = null
        when(am.getGenericType()).thenReturn(JavaType.construct(String[].class));
        JavaType ct = Mockito.mock(JavaType.class);
        when(ct.getContentType()).thenReturn(JavaType.construct(Integer.class));
        when(serializationType.getContentType()).thenReturn(ct);
        Assert.assertEquals(BeanPropertyWriter.class, propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, defaultUseStaticTyping).getClass());

        // Test with contentTypeSer = different type
        when(am.getGenericType()).thenReturn(JavaType.construct(String[].class));
        JavaType ct2 = Mockito.mock(JavaType.class);
        when(ct2.getContentType()).thenReturn(JavaType.construct(String.class));
        when(serializationType.getContentType()).thenReturn(ct2);
        Assert.assertEquals(BeanPropertyWriter.class, propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping).getClass());
    }
}