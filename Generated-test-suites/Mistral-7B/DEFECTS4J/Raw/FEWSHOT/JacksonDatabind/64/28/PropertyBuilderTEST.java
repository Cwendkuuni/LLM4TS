package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;

import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        // Test case for class with no annotations
        Object result = new PropertyBuilder(null, null).findSerializationType(null, false, null);
        assertNull(result);

        // Test case for class with JsonInclude annotation
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(new ClassInfo(AnnotatedClass.class), null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        Annotated a = new AnnotatedClass(AnnotatedClass.class, null);
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector() {};
        JsonInclude includeAnnotation = new JsonInclude();
        annotationIntrospector.setValue(includeAnnotation, a);
        JavaType declaredType = TypeFactory.defaultInstance();
        JavaType resultType = propertyBuilder.findSerializationType(a, false, declaredType);
        assertEquals(String.class, resultType.getRawClass());

        // Test case for class with JsonSerialize annotation
        JsonSerialize jsonSerializeAnnotation = new JsonSerialize();
        JsonType serializableTypeAnnotation = new JsonType();
        serializableTypeAnnotation.value("java.util.ArrayList");
        annotationIntrospector.setValue(jsonSerializeAnnotation, a);
        annotationIntrospector.setValue(serializableTypeAnnotation, a);
        JavaType expectedType = TypeFactory.type(ArrayList.class);
        resultType = propertyBuilder.findSerializationType(a, false, declaredType);
        assertEquals(expectedType, resultType);

        // Test case for class with JsonInclude and JsonSerialize annotations
        includeAnnotation.include(JsonInclude.Include.NON_DEFAULT);
        annotationIntrospector.setValue(jsonSerializeAnnotation, a);
        annotationIntrospector.setValue(includeAnnotation, a);
        JavaType expectedTypeWithInclude = TypeFactory.type(ArrayList.class, String[].class);
        resultType = propertyBuilder.findSerializationType(a, false, declaredType);
        assertEquals(expectedTypeWithInclude, resultType);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        // Test case for null input
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(new ClassInfo(AnnotatedClass.class), null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        SerializerProvider prov = new SerializerProvider();
        BeanPropertyDefinition propDef = new BeanPropertyDefinition("testProperty", null, null, null, null);
        JavaType declaredType = TypeFactory.defaultInstance();
        JsonSerializer<?> serializer = new JsonSerializer<Object>() {};
        TypeSerializer typeSerializer = new TypeSerializer() {};
        BeanPropertyWriter result = propertyBuilder.buildWriter(prov, propDef, declaredType, serializer, typeSerializer, null, null, false);
        assertNull(result);

        // Test case for JsonInclude.Include.ALWAYS
        String propertyName = "testProperty";
        JavaType serializationType = TypeFactory.type(ArrayList.class);
        AnnotatedMember am = new AnnotatedField("testProperty", new Field() {}, AnnotatedClass.class);
        result = propertyBuilder.buildWriter(prov, propDef, declaredType, serializer, typeSerializer, null, am, false);
        assertNotNull(result);

        // Test case for JsonInclude.Include.NON_DEFAULT
        includeAnnotation.include(JsonInclude.Include.NON_DEFAULT);
        annotationIntrospector.setValue(includeAnnotation, am);
        Object defaultBean = new PropertyBuilder(config, beanDesc).getDefaultBean();
        Object propertyDefaultValue = propertyBuilder.getPropertyDefaultValue(propertyName, am, TypeFactory.defaultInstance());
        BeanPropertyWriter expectedWriter = new BeanPropertyWriter(propDef, am, beanDesc.getClassAnnotations(), declaredType, serializer, typeSerializer, serializationType, false, propertyDefaultValue);
        expectedWriter.assignNullSerializer(serializer);
        result = propertyBuilder.buildWriter(prov, propDef, declaredType, serializer, typeSerializer, null, am, false);
        assertEquals(expectedWriter, result);

        // Test case for JsonInclude.Include.NON_ABSENT
        includeAnnotation.include(JsonInclude.Include.NON_ABSENT);
        annotationIntrospector.setValue(includeAnnotation, am);
        result = propertyBuilder.buildWriter(prov, propDef, declaredType, serializer, typeSerializer, null, am, false);
        assertNull(result);

        // Test case for JsonInclude.Include.NON_EMPTY
        includeAnnotation.include(JsonInclude.Include.NON_EMPTY);
        annotationIntrospector.setValue(includeAnnotation, am);
        result = propertyBuilder.buildWriter(prov, propDef, declaredType, serializer, typeSerializer, null, am, false);
        assertNotNull(result);

        // Test case for JsonInclude.Include.NON_NULL
        includeAnnotation.include(JsonInclude.Include.NON_NULL);
        annotationIntrospector.setValue(includeAnnotation, am);
        TypeSerializer contentTypeSerializer = new TypeSerializer() {};
        result = propertyBuilder.buildWriter(prov, propDef, declaredType, serializer, typeSerializer, contentTypeSerializer, am, false);
        assertNotNull(result);
    }

    @Test
    public void testGetClassAnnotations() {
        // Test case for empty class
        BeanDescription beanDesc = new BeanDescription(new ClassInfo(AnnotatedClass.class), null);
        Annotations result = new PropertyBuilder(null, beanDesc).getClassAnnotations();
        assertNotNull(result);
        assertEquals(0, result.size());

        // Test case for class with annotations
        JsonInclude includeAnnotation = new JsonInclude();
        includeAnnotation.include(JsonInclude.Include.NON_DEFAULT);
        beanDesc.getClassAnnotations().add(includeAnnotation);
        result = new PropertyBuilder(null, beanDesc).getClassAnnotations();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(includeAnnotation.getClass(), result.get(0).getClass());
    }

    @Test
    public void testGetJsonSerializeAnnotation() {
        // Test case for class with no JsonSerialize annotations
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(new ClassInfo(AnnotatedClass.class), null);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        AnnotatedMember am = new AnnotatedField("testProperty", new Field() {}, AnnotatedClass.class);
        JsonSerialize jsonSerializeAnnotation = propertyBuilder._annotationIntrospector.findSerializerAnnotation(am);
        assertNull(jsonSerializeAnnotation);

        // Test case for class with JsonSerialize annotations
        JsonSerialize jsonSerializeAnnotation2 = new JsonSerialize();
        annotationIntrospector.setValue(jsonSerializeAnnotation2, am);
        jsonSerializeAnnotation = propertyBuilder._annotationIntrospector.findSerializerAnnotation(am);
        assertEquals(jsonSerializeAnnotation2, jsonSerializeAnnotation);
    }
}