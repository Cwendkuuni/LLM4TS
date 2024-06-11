package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testPropertyBuilder() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        assertNotNull(propertyBuilder);
        assertEquals(config, propertyBuilder._config);
        assertEquals(beanDesc, propertyBuilder._beanDesc);
        assertNotNull(propertyBuilder._annotationIntrospector);
        assertNull(propertyBuilder._defaultBean);
        assertNull(propertyBuilder._defaultInclusion);
        assertFalse(propertyBuilder._useRealPropertyDefaults);
    }

    @Test
    public void testBuildWriter_noAnnotation() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        ObjectNode rootNode = new ObjectMapper().createObjectNode();
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, JsonType.STANDARD_INSTANCE, null, null, null, null, false);
        assertNotNull(beanPropertyWriter);
        ObjectNode node = beanPropertyWriter.serializeAsStartNode(RootType.VALUE_IDENTITY, rootNode);
        assertNotNull(node);
        assertEquals("null", node.toString());
    }

    @Test
    public void testBuildWriter_withAnnotation() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(null, null);
        JsonInclude inclusionAnnotation = new JsonInclude();
        JsonInclude.Value inclusionValueAnnotation = new JsonInclude.Value();
        inclusionValueAnnotation.setValueInclusion(JsonInclude.Include.NON_DEFAULT);
        inclusionAnnotation.setValue(inclusionValueAnnotation);
        AnnotationIntrospector annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
        when(annotationIntrospector.findSerializationType(Mockito.any(Annotated.class), Mockito.anyBoolean(), Mockito.any(JavaType.class)))
                .thenReturn(new JavaType(String.class));
        when(annotationIntrospector.findNullSerializer(Mockito.any(Annotated.class)))
                .thenReturn(null);
        when(annotationIntrospector.findUnwrappingNameTransformer(Mockito.any(Annotated.class)))
                .thenReturn(null);
        when(annotationIntrospector.findPropertyFilter(Mockito.any(JavaType.class), Mockito.any(JsonDeserializer.class), Mockito.any(BeanPropertyDefinition.class)))
                .thenReturn(null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder._annotationIntrospector = annotationIntrospector;
        ObjectNode rootNode = new ObjectMapper().createObjectNode();
        JsonSerializer<String> serializer = Mockito.mock(JsonSerializer.class);
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(null, null, JsonType.STANDARD_INSTANCE, serializer, null, null, null, false);
        assertNotNull(beanPropertyWriter);
        ObjectNode node = beanPropertyWriter.serializeAsStartNode(RootType.VALUE_IDENTITY, rootNode);
        assertNotNull(node);
        assertEquals("null", node.toString());
        verify(annotationIntrospector, times(1)).findSerializationType(Mockito.any(Annotated.class), Mockito.anyBoolean(), Mockito.any(JavaType.class));
        verifyNoMoreInteractions(annotationIntrospector);
    }

    @Test
    public void testFindSerializationType() throws JsonMappingException {
        JsonInclude inclusionAnnotation = new JsonInclude();
        JsonInclude.Value inclusionValueAnnotation = new JsonInclude.Value();
        inclusionValueAnnotation.setValueInclusion(JsonInclude.Include.NON_DEFAULT);
        AnnotationIntrospector annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        Class<?> clazz = Mockito.mock(Class.class);
        when(annotationIntrospector.refineSerializationType(Mockito.any(SerializationConfig.class), Mockito.any(Annotated.class), Mockito.any(JavaType.class)))
                .thenReturn(javaType);
        when(javaType.getRawClass())
                .thenReturn(clazz);
        when(clazz.isAssignableFrom(JsonType.STANDARD_INSTANCE.getRawClass()))
                .thenReturn(true);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        propertyBuilder._annotationIntrospector = annotationIntrospector;
        JavaType result = propertyBuilder.findSerializationType(null, true, JsonType.STANDARD_INSTANCE);
        assertSame(javaType, result);
        verify(annotationIntrospector, times(1)).refineSerializationType(Mockito.any(SerializationConfig.class), Mockito.any(Annotated.class), Mockito.any(JavaType.class));
        verifyNoMoreInteractions(annotationIntrospector);
    }

    @Test
    public void testGetDefaultBean() {
        BeanDescription beanDesc = new BeanDescription(null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
        beanDesc.instantiateBean(true);
        defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() throws Exception {
        BeanDescription beanDesc = new BeanDescription(null, null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDesc);
        Class<?> clazz = Mockito.mock(Class.class);
        BeanPropertyDefinition beanPropertyDefinition = Mockito.mock(BeanPropertyDefinition.class);
        JsonType jsonType = Mockito.mock(JsonType.class);
        when(beanDesc.getClassAnnotations())
                .thenReturn(new Annotations());
        when(beanDesc.instantiateBean(true))
                .thenReturn(new Object());
        when(beanDesc.findPropertyDefinition(Mockito.anyString()))
                .thenReturn(beanPropertyDefinition);
        when(beanPropertyDefinition.findAccessor())
                .thenReturn(null);
        when(beanPropertyDefinition.getName())
                .thenReturn("testProperty");
        when(beanPropertyDefinition.getType())
                .thenReturn(jsonType);
        when(jsonType.getRawClass())
                .thenReturn(clazz);
        propertyBuilder._beanDesc = beanDesc;
        Object result = propertyBuilder.getPropertyDefaultValue("testProperty", null, jsonType);
        assertNull(result);
        verify(beanDesc, times(1)).instantiateBean(true);
        verifyNoMoreInteractions(beanDesc);
    }

    @Test
    public void testGetDefaultValue() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Object result = propertyBuilder.getDefaultValue(JsonType.STANDARD_INSTANCE);
        assertNull(result);
    }
}