package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Assert;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        Object serializedType = propertyBuilder.findSerializationType(null, true, null);
        Assert.assertNull(serializedType);

        // Test with annotated type
        JsonInclude jsonIncludeAnnotation = new JsonInclude();
        JsonSerialize jsonSerializeAnnotation = new JsonSerialize();
        jsonSerializeAnnotation.using(new StaticSerializerProvider(null).findValueSerializer(String.class));
        Class<String> stringClass = String.class;
        AnnotatedMember annotatedMember = new AnnotatedClassMember("test", stringClass);
        Annotation[] annotations = new Annotation[]{jsonIncludeAnnotation, jsonSerializeAnnotation};
        JavaType declaredType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String[].class);
        try {
            JavaType serializationType = propertyBuilder.findSerializationType(annotatedMember, false, declaredType);
            Assert.assertNotNull(serializationType);
            Assert.assertEquals(String[].class, serializationType.getRawClass());
        } catch (JsonMappingException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Test with annotated type with super class
        jsonIncludeAnnotation = new JsonInclude();
        jsonIncludeAnnotation.value(JsonInclude.Include.NON_DEFAULT);
        jsonSerializeAnnotation = new JsonSerialize();
        jsonSerializeAnnotation.using(new StaticSerializerProvider(null).findValueSerializer(Integer[].class));
        Class<Integer> integerClass = Integer.class;
        annotatedMember = new AnnotatedClassMember("test", integerClass);
        annotations = new Annotation[]{jsonIncludeAnnotation, jsonSerializeAnnotation};
        try {
            JavaType serializationType = propertyBuilder.findSerializationType(annotatedMember, false, declaredType);
            Assert.assertNotNull(serializationType);
            Assert.assertEquals(Integer[].class, serializationType.getRawClass());
        } catch (JsonMappingException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Test with no annotations and static typing enabled
        try {
            JavaType serializationType = propertyBuilder.findSerializationType(null, true, declaredType);
            Assert.assertNull(serializationType);
        } catch (JsonMappingException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // Test with no annotations and static typing disabled
        try {
            JavaType serializationType = propertyBuilder.findSerializationType(null, false, declaredType);
            Assert.assertNull(serializationType);
        } catch (JsonMappingException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        ObjectWriter objectWriter = ObjectMapper.getDefaultInstance().writerWithDefaultPrettyPrinter();
        SerializerProvider serializerProvider = new StaticSerializerProvider(null);
        BeanPropertyDefinition propDef = null;
        AnnotatedMember am = null;
        JavaType declaredType = null;
        String propName = null;
        
        // Test with no serializer and no type serializer
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, null, null, null, am, false);
        Assert.assertNull(beanPropertyWriter);

        // Test with no property definition
        propDef = new BeanPropertyDefinition(null, null, null, null, null, null, null, null, null, null);
        beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, null, null, null, am, false);
        Assert.assertNull(beanPropertyWriter);

        // Test with no declared type
        declaredType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(String.class);
        beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, null, null, null, null, am, false);
        Assert.assertNull(beanPropertyWriter);

        // Test with no serializer
        JsonSerializer<String> jsonSerializer = new JsonSerializer<String>() {
            @Override
            public void serialize(String t, JsonGenerator jg, SerializerProvider sp) throws IOException {
                jg.writeString(t);
            }
        };
        beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, null, null, am, false);
        Assert.assertNotNull(beanPropertyWriter);

        // Test with no type serializer
        TypeSerializer typeSerializer = ObjectMapper.getDefaultInstance().getTypeSerializer();
        beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, null, am, false);
        Assert.assertNotNull(beanPropertyWriter);

        // Test with string property
        propName = "testString";
        propDef = new BeanPropertyDefinition("testString", String.class, null, null, null, null, null, null, null, null);
        am = new AnnotatedField("testString", propDef, String.class, null, null, null, null);
        beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, null, am, false);
        Assert.assertNotNull(beanPropertyWriter);

        // Test with integer property
        propName = "testInteger";
        propDef = new BeanPropertyDefinition("testInteger", Integer.class, null, null, null, null, null, null, null, null);
        am = new AnnotatedField("testInteger", propDef, Integer.class, null, null, null, null);
        beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, null, am, false);
        Assert.assertNotNull(beanPropertyWriter);

        // Test with custom serializer
        JsonSerializer<Integer> customSerializer = new JsonSerializer<Integer>() {
            @Override
            public void serialize(Integer t, JsonGenerator jg, SerializerProvider sp) throws IOException {
                jg.writeNumber(t.intValue() * 2);
            }
        };
        beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, customSerializer, am, false);
        Assert.assertNotNull(beanPropertyWriter);

        // Test with container type property
        JavaType containerType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(List.class);
        beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, null, am, false);
        Assert.assertNull(beanPropertyWriter);

        // Test with container type property and type serializer
        TypeSerializer contentTypeSerializer = ObjectMapper.getDefaultInstance().getTypeSerializer();
        beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, contentTypeSerializer, am, false);
        Assert.assertNotNull(beanPropertyWriter);

        // Test with custom null serializer
        JsonSerializer<String> customNullSerializer = new JsonSerializer<String>() {
            @Override
            public void serialize(String t, JsonGenerator jg, SerializerProvider sp) throws IOException {
                jg.writeString("null");
            }
        };
        beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, null, am, true);
        Assert.assertNotNull(beanPropertyWriter);
        beanPropertyWriter.assignNullSerializer(serializerProvider.serializerInstance(am, customNullSerializer));
        Assert.assertEquals(customNullSerializer, beanPropertyWriter.getNullSerializer());

        // Test with unwrapping name transformer
        NameTransformer nameTransformer = new NameTransformer() {
            @Override
            public String transform(String name, JsonSerializer<?> s) {
                return name + "_transformed";
            }
        };
        beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, propDef, declaredType, jsonSerializer, typeSerializer, null, am, false);
        Assert.assertNotNull(beanPropertyWriter);
        beanPropertyWriter = beanPropertyWriter.unwrappingWriter(nameTransformer);
        Assert.assertEquals(nameTransformer, beanPropertyWriter.getUnwrappingNameTransformer());
    }
}