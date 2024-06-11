package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

public class PropertyBuilderTest {

    @Test
    public void testDefaultInclusion() {
        ObjectNode node = ObjectMapper.findStandardMapper().createObjectNode();
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        JsonInclude.Value defaultInclusion = propertyBuilder.getDefaultInclusion();
        assertEquals(JsonInclude.Include.NON_DEFAULT, defaultInclusion.getValueInclusion());
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        JsonInclude annotation = new JsonInclude();
        JsonSerialize serializationAnnotation = new JsonSerialize();
        Class<String> stringClass = String.class;

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);

        // Test with no annotations
        JavaType declaredType = BeanPropertyType.StandardBuilder.forFieldType(String.class).build();
        assertEquals(declaredType, propertyBuilder.findSerializationType(null, false, declaredType));

        // Test with JsonInclude annotation
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);
        when(annotatedMember.getRawType()).thenReturn(String.class);
        when(annotatedMember.findAnnotation(JsonInclude.class)).thenReturn(annotation);
        assertEquals(stringClass, propertyBuilder.findSerializationType(annotatedMember, false, declaredType));

        // Test with JsonSerialize annotation
        JsonSerialize jsonSerializeAnnotation = new JsonSerialize();
        when(annotatedMember.findAnnotation(JsonSerialize.class)).thenReturn(jsonSerializeAnnotation);
        assertEquals(String.class, propertyBuilder.findSerializationType(annotatedMember, false, declaredType));

        // Test with both annotations
        when(annotatedMember.findAnnotation(JsonInclude.class)).thenReturn(annotation);
        when(annotatedMember.findAnnotation(JsonSerialize.class)).thenReturn(jsonSerializeAnnotation);
        assertEquals(stringClass, propertyBuilder.findSerializationType(annotatedMember, false, declaredType));
    }

    @Test
    public void testGetClassAnnotations() {
        BeanDescription beanDescription = Mockito.mock(BeanDescription.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        ObjectMapper objectMapper = ObjectMapper.findStandardMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        BeanDescription beanDescription = Mockito.mock(BeanDescription.class);
        JsonSerializer<Object> jsonSerializer = Mockito.mock(JsonSerializer.class);
        TypeSerializer typeSerializer = Mockito.mock(TypeSerializer.class);
        TypeSerializer contentTypeSerializer = Mockito.mock(TypeSerializer.class);
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);

        // Test with no annotations
        JavaType declaredType = BeanPropertyType.StandardBuilder.forFieldType(String.class).build();
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        assertEquals(jsonSerializer, propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, contentTypeSerializer, annotatedMember, false));

        // Test with JsonInclude annotation
        JsonInclude jsonIncludeAnnotation = new JsonInclude();
        when(annotatedMember.findAnnotation(JsonInclude.class)).thenReturn(jsonIncludeAnnotation);
        when(beanDescription.getClassAnnotations()).thenReturn(new Annotations());
        Annotations classAnnotations = new Annotations();
        classAnnotations.forAnnotation(JsonInclude.class).add(jsonIncludeAnnotation);
        when(beanDescription.getClassAnnotations()).thenReturn(classAnnotations);
        assertEquals(jsonSerializer, propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, contentTypeSerializer, annotatedMember, false));

        // Test with JsonSerialize annotation
        JsonSerialize jsonSerializeAnnotation = new JsonSerialize();
        when(annotatedMember.findAnnotation(JsonSerialize.class)).thenReturn(jsonSerializeAnnotation);
        when(jsonSerializeAnnotation.using()).thenReturn(jsonSerializer);
        assertEquals(jsonSerializer, propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, contentTypeSerializer, annotatedMember, false));

        // Test with both annotations
        JsonInclude jsonIncludeAnnotation = new JsonInclude();
        JsonSerialize jsonSerializeAnnotation = new JsonSerialize();
        when(annotatedMember.findAnnotation(JsonInclude.class)).thenReturn(jsonIncludeAnnotation);
        when(annotatedMember.findAnnotation(JsonSerialize.class)).thenReturn(jsonSerializeAnnotation);
        when(jsonSerializeAnnotation.using()).thenReturn(jsonSerializer);
        assertEquals(jsonSerializer, propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, contentTypeSerializer, annotatedMember, false));

        // Test with JsonTypeName annotation
        JsonTypeName jsonTypeNameAnnotation = new JsonTypeName();
        when(annotatedMember.findAnnotation(JsonTypeName.class)).thenReturn(jsonTypeNameAnnotation);
        when(jsonTypeNameAnnotation.value()).thenReturn("foo");
        assertEquals(jsonSerializer, propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, contentTypeSerializer, annotatedMember, false));

        // Test with JsonDeserialize annotation
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        when(annotatedMember.findAnnotation(JsonDeserialize.class)).thenReturn(jsonDeserializeAnnotation);
        when(jsonDeserializeAnnotation.contentAsStream()).thenReturn(true);
        assertEquals(jsonSerializer, propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, contentTypeSerializer, annotatedMember, false));

        // Test with JsonUnwrapped annotation
        JsonUnwrapped jsonUnwrappedAnnotation = new JsonUnwrapped();
        when(annotatedMember.findAnnotation(JsonUnwrapped.class)).thenReturn(jsonUnwrappedAnnotation);
        assertEquals(jsonSerializer, propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, contentTypeSerializer, annotatedMember, false).unwrappingWriter(jsonUnwrappedAnnotation));
    }

    @Test
    public void testUseRealPropertyDefaults() {
        ObjectMapper objectMapper = ObjectMapper.findStandardMapper();
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();
        BeanDescription beanDescription = Mockito.mock(BeanDescription.class);
        JsonInclude jsonIncludeAnnotation = new JsonInclude();
        Class<String> stringClass = String.class;

        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);

        // Test with no default value
        Object defaultBean = null;
        Object valueToSuppress = propertyBuilder.buildWriter(serializerProvider, null, stringClass, null, null, null, null, false).getValueToSuppress();
        assertNull(valueToSuppress);

        // Test with default value
        Object defaultBean1 = new Object();
        when(beanDescription.instantiateBean(true)).thenReturn(defaultBean1);
        Object valueToSuppress1 = propertyBuilder.buildWriter(serializerProvider, null, stringClass, null, null, null, null, false).getValueToSuppress();
        assertEquals(defaultBean1, valueToSuppress1);

        // Test with JsonInclude.Include.NON_DEFAULT
        JsonInclude.Include includeAnnotation = JsonInclude.Include.NON_DEFAULT;
        when(beanDescription.findPropertyInclusion(includeAnnotation)).thenReturn(JsonInclude.Value.builder().include(includeAnnotation).build());
        Object valueToSuppress2 = propertyBuilder.buildWriter(serializerProvider, null, stringClass, null, null, null, null, false).getValueToSuppress();
        assertNull(valueToSuppress2);
    }
}