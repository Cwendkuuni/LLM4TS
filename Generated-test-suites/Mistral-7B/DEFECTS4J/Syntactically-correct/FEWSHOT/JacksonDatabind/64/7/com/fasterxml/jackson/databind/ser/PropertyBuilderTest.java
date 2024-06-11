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
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated annotated = mock(Annotated.class);
        JavaType declaredType = TypeFactory.uncheckedType(String[].class);

        // Test with null
        Assert.assertNull(propertyBuilder.findSerializationType(annotated, false, declaredType));

        // Test with empty annotations
        when(annotated.getAnnotation(JsonSerialize.class)).thenReturn(null);
        Assert.assertNull(propertyBuilder.findSerializationType(annotated, true, declaredType));

        // Test with JsonSerialize annotation with default typing
        JsonSerialize jsonSerialize = mock(JsonSerialize.class);
        when(jsonSerialize.typing()).thenReturn(JsonSerialize.Typing.DEFAULT_TYPING);
        when(annotated.getAnnotation(JsonSerialize.class)).thenReturn(jsonSerialize);
        Assert.assertNull(propertyBuilder.findSerializationType(annotated, true, declaredType));

        // Test with JsonSerialize annotation with static typing
        when(jsonSerialize.typing()).thenReturn(JsonSerialize.Typing.STATIC);
        Assert.assertNull(propertyBuilder.findSerializationType(annotated, true, declaredType));

        // Test with JsonSerialize annotation with custom type
        when(jsonSerialize.type()).thenReturn(TypeFactory.uncheckedType(Integer.class));
        Assert.assertEquals(TypeFactory.uncheckedType(Integer.class), propertyBuilder.findSerializationType(annotated, true, declaredType));

        // Test with JsonSerialize annotation and JsonType annotation with custom type
        JsonType jsonType = mock(JsonType.class);
        when(jsonType.contentType()).thenReturn(TypeFactory.uncheckedType(String.class));
        when(jsonSerialize.type()).thenReturn(jsonType);
        Assert.assertEquals(TypeFactory.uncheckedType(String.class), propertyBuilder.findSerializationType(annotated, true, declaredType));
    }

    @Test
    public void testGetClassAnnotations() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotation[] annotations = new Annotation[]{mock(JsonSerialize.class), mock(JsonInclude.class)};
        when(beanDesc.getClassAnnotations()).thenReturn(annotations);

        Assert.assertArrayEquals(annotations, propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.uncheckedType(String.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        // Test with null serializer provider
        Assert.assertNull(propertyBuilder.buildWriter(null, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping));

        // Test with null property definition
        Assert.assertNull(propertyBuilder.buildWriter(prov, null, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping));

        // Test with null declared type
        Assert.assertNull(propertyBuilder.buildWriter(prov, propDef, null, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping));

        // Test with null json serializer
        Assert.assertNull(propertyBuilder.buildWriter(prov, propDef, declaredType, null, typeSer, contentTypeSer, am, defaultUseStaticTyping));

        // Test with null type serializer
        Assert.assertNull(propertyBuilder.buildWriter(prov, propDef, declaredType, ser, null, contentTypeSer, am, defaultUseStaticTyping));

        // Test with null content type serializer
        Assert.assertNull(propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, defaultUseStaticTyping));

        // Test with null annotated member
        Assert.assertNull(propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, null, defaultUseStaticTyping));

        // Test with default useStaticTyping
        when(propertyBuilder._config.getDefaultPropertyInclusion(declaredType.getRawClass(), _defaultInclusion)).thenReturn(JsonInclude.Value.builder().build());
        when(propertyBuilder._config.getDefaultPropertyInclusion()).thenReturn(JsonInclude.Value.builder().build());
        when(propertyBuilder._useRealPropertyDefaults).thenReturn(false);
        when(propertyBuilder._annotationIntrospector.findNullSerializer(am)).thenReturn(ser);
        when(propertyBuilder._annotationIntrospector.findUnwrappingNameTransformer(am)).thenReturn(null);

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        Assert.assertNotNull(bpw);

        // Test with custom null serializer
        when(propertyBuilder._annotationIntrospector.findNullSerializer(am)).thenReturn(mock(JsonSerializer.class));
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        Assert.assertNotNull(bpw);

        // Test with unwrapping writer
        when(propertyBuilder._annotationIntrospector.findUnwrappingNameTransformer(am)).thenReturn(mock(NameTransformer.class));
        bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        Assert.assertNotNull(bpw);
    }

    @Test
    public void testFindSerializationTypeWithContentTypeSerializer() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = mock(SerializerProvider.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType declaredType = TypeFactory.uncheckedType(Map.class);
        JavaType contentType = TypeFactory.uncheckedType(String.class);
        JsonSerializer<?> ser = mock(JsonSerializer.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);
        boolean defaultUseStaticTyping = true;

        when(propertyBuilder._config.getDefaultPropertyInclusion(declaredType.getRawClass(), _defaultInclusion)).thenReturn(JsonInclude.Value.builder().build());
        when(propertyBuilder._config.getDefaultPropertyInclusion()).thenReturn(JsonInclude.Value.builder().build());
        when(propertyBuilder._useRealPropertyDefaults).thenReturn(false);

        when(propertyBuilder.findSerializationType(am, defaultUseStaticTyping, declaredType)).thenThrow(new JsonMappingException("Test exception"));

        when(propertyBuilder._annotationIntrospector.findNullSerializer(am)).thenReturn(ser);
        when(propertyBuilder._annotationIntrospector.findUnwrappingNameTransformer(am)).thenReturn(null);

        // Test with null contentTypeSerializer
        Assert.assertThrows(JsonMappingException.class, () -> propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, null, am, defaultUseStaticTyping));

        // Test with contentTypeSerializer
        when(contentTypeSer.deserializeValue(prov, null, null)).thenReturn("content");
        when(contentTypeSer.serializeValue(null, null, null, null)).thenReturn("content");

        BeanPropertyWriter bpw = propertyBuilder.buildWriter(prov, propDef, declaredType, ser, typeSer, contentTypeSer, am, defaultUseStaticTyping);
        Assert.assertNotNull(bpw);
    }
}