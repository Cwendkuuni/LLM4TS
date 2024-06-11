package com.fasterxml.jackson.databind.ser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class PropertyBuilderTest {

    @Test
    public void testFindSerializationType() {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(int[].class);
        try {
            JavaType actualType = propertyBuilder.findSerializationType(am, true, declaredType);
            assertNotNull(actualType);
            assertEquals(int[].class, actualType.getRawClass());
        } catch (JsonMappingException e) {
            // Should not reach here
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test with annotation
        AnnotationIntrospector introspector = Mockito.mock(AnnotationIntrospector.class);
        when(introspector.refineSerializationType(eq(config), eq(am), eq(declaredType)))
                .thenReturn(TypeFactory.defaultInstance().constructType(String.class));
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder._annotationIntrospector = introspector;
        try {
            JavaType actualType = propertyBuilder.findSerializationType(am, true, declaredType);
            assertNotNull(actualType);
            assertEquals(String.class, actualType.getRawClass());
        } catch (JsonMappingException e) {
            // Should not reach here
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test with no annotation and no declared type
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        try {
            propertyBuilder.findSerializationType(am, true, null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }

        // Test with non-assignable type
        JavaType nonAssignableType = TypeFactory.defaultInstance().constructType(String.class);
        try {
            propertyBuilder.findSerializationType(am, true, declaredType);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertThat(e.getMessage(), startsWith("Illegal concrete-type annotation for method '"));
        }
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        SerializationConfig config = new SerializationConfig();
        config.setDefaultPropertyInclusion(JsonInclude.Value.include(JsonInclude.Include.ALWAYS));
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        ObjectWriter objectWriter = config.builder().build();
        ObjectMapper objectMapper = new ObjectMapper(objectWriter);

        JavaType typeToSerialize = TypeFactory.defaultInstance().constructType(MyClass.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(MyClass.class, "property1");
        SerializerProvider prov = new StandardSerializerProvider();
        JavaType declaredType = TypeFactory.defaultInstance().constructType(Integer.class);
        JsonSerializer<Integer> intSerializer = new SimpleSerializerProvider.SerializerProvider.BaseSerializerProvider.SerializerProviderBase<Integer, Json>(null) {
            @Override
            public JsonSerializer<Integer> findValueSerializer(SerializerProvider p, TypeSerializer tsc, BeanPropertyDefinition propDef, JavaType declaredType) {
                return new SimpleJsonSerializer<Integer>() {
                    @Override
                    public void serialize(Integer value, JsonGenerator jg, SerializerProvider sp) throws IOException {
                        jg.writeNumber(value);
                    }
                };
            }
        };
        TypeSerializer intTypeSerializer = new StandardSerializers.BaseSerializerSerializerMap().findValueSerializer(config, Integer[].class);

        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, intSerializer, intTypeSerializer, null, am, false);

        MyClass instance = new MyClass(1, "test");
        ObjectNode node = objectMapper.writeValueAsTree(instance);
        assertEquals("1", node.get("property1").asText());
    }

    @Test
    public void testBuildWriterWithNullValueToSuppress() {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        SerializationConfig config = new SerializationConfig();
        config.setDefaultPropertyInclusion(JsonInclude.Value.include(JsonInclude.Include.NON_NULL));
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        ObjectWriter objectWriter = config.builder().build();
        ObjectMapper objectMapper = new ObjectMapper(objectWriter);

        JavaType typeToSerialize = TypeFactory.defaultInstance().constructType(MyClass.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(MyClass.class, "property1");
        SerializerProvider prov = new StandardSerializerProvider();
        JsonSerializer<Integer> intSerializer = new SimpleSerializerProvider.SerializerProvider.BaseSerializerProvider.SerializerProviderBase<Integer, Json>(null) {
            @Override
            public JsonSerializer<Integer> findValueSerializer(SerializerProvider p, TypeSerializer tsc, BeanPropertyDefinition propDef, JavaType declaredType) {
                return new SimpleJsonSerializer<Integer>() {
                    @Override
                    public void serialize(Integer value, JsonGenerator jg, SerializerProvider sp) throws IOException {
                        jg.writeNumber(value);
                    }
                };
            }
        };
        TypeSerializer intTypeSerializer = new StandardSerializers.BaseSerializerSerializerMap().findValueSerializer(config, Integer[].class);

        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, intSerializer, intTypeSerializer, null, am, true);

        MyClass instance = new MyClass(null, "test");
        ObjectNode node = objectMapper.writeValueAsTree(instance);
        assertTrue(node.isMissingNode("property1"));
    }

    @Test
    public void testBuildWriterWithValueToSuppress() {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        SerializationConfig config = new SerializationConfig();
        config.setDefaultPropertyInclusion(JsonInclude.Value.include(JsonInclude.Include.NON_NULL));
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        ObjectWriter objectWriter = config.builder().build();
        ObjectMapper objectMapper = new ObjectMapper(objectWriter);

        JavaType typeToSerialize = TypeFactory.defaultInstance().constructType(MyClass.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(MyClass.class, "property1");
        SerializerProvider prov = new StandardSerializerProvider();
        JsonSerializer<Integer> intSerializer = new SimpleSerializerProvider.SerializerProvider.BaseSerializerProvider.SerializerProviderBase<Integer, Json>(null) {
            @Override
            public JsonSerializer<Integer> findValueSerializer(SerializerProvider p, TypeSerializer tsc, BeanPropertyDefinition propDef, JavaType declaredType) {
                return new SimpleJsonSerializer<Integer>() {
                    @Override
                    public void serialize(Integer value, JsonGenerator jg, SerializerProvider sp) throws IOException {
                        jg.writeNumber(value);
                    }
                };
            }
        };
        TypeSerializer intTypeSerializer = new StandardSerializers.BaseSerializerSerializerMap().findValueSerializer(config, Integer[].class);

        Object defaultBean = new MyClass(1, "test");
        propertyBuilder._defaultBean = defaultBean;
        BeanPropertyWriter writer = propertyBuilder.buildWriter(prov, propDef, declaredType, intSerializer, intTypeSerializer, null, am, false);

        MyClass instance = new MyClass(1, "test");
        ObjectNode node = objectMapper.writeValueAsTree(instance);
        assertEquals("1", node.get("property1").asText());

        ObjectNode nodeWithDefault = objectMapper.writeValueAsTree(defaultBean);
        Object defaultValue = nodeWithDefault.get("property1");
        assertEquals("1", defaultValue.asText());

        MyClass instanceWithDefault = new MyClass(1, defaultValue.asText());
        ObjectNode nodeWithInstanceDefault = objectMapper.writeValueAsTree(instanceWithDefault);
        assertEquals("1", nodeWithInstanceDefault.get("property1").asText());

        assertEquals(instanceWithDefault, instanceWithInstanceDefault);
    }

    @Test
    public void testFindDefaultBean() {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        MyClass instance = new MyClass(1, "test");
        propertyBuilder._beanDesc = beanDesc;
        propertyBuilder._config = config;
        propertyBuilder._defaultBean = instance;

        Object defaultBean = propertyBuilder.getDefaultBean();
        assertEquals(instance, defaultBean);

        propertyBuilder._defaultBean = null;
        defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        propertyBuilder._beanDesc = null;
        propertyBuilder._config = null;
        defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }

    @Test
    public void testGetPropertyDefaultValue() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        ObjectWriter objectWriter = config.builder().build();
        ObjectMapper objectMapper = new ObjectMapper(objectWriter);

        JavaType typeToSerialize = TypeFactory.defaultInstance().constructType(MyClass.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(MyClass.class, "property1");
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getName()).thenReturn("property1");

        propertyBuilder._beanDesc = beanDesc;
        propertyBuilder._config = config;
        Object defaultBean = new MyClass(1, "test");
        propertyBuilder._defaultBean = defaultBean;

        Object defaultValue = propertyBuilder.getPropertyDefaultValue("property1", am, TypeFactory.defaultInstance().constructType(Integer.class));
        assertEquals(1, defaultValue);

        propertyBuilder._defaultBean = null;
        defaultValue = propertyBuilder.getPropertyDefaultValue("property1", am, TypeFactory.defaultInstance().constructType(Integer.class));
        assertNull(defaultValue);

        propertyBuilder._beanDesc = null;
        propertyBuilder._config = null;
        defaultValue = propertyBuilder.getPropertyDefaultValue("property1", am, TypeFactory.defaultInstance().constructType(Integer.class));
        assertNull(defaultValue);
    }

    @Test
    public void testGetDefaultValue() throws JsonMappingException {
        BeanDescription beanDesc = new BeanDescription(MyClass.class);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        ObjectWriter objectWriter = config.builder().build();
        ObjectMapper objectMapper = new ObjectMapper(objectWriter);

        JavaType typeToSerialize = TypeFactory.defaultInstance().constructType(MyClass.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(MyClass.class, "property1");
        AnnotatedMember am = Mockito.mock(AnnotatedMember.class);
        when(am.getName()).thenReturn("property1");

        propertyBuilder._beanDesc = beanDesc;
        propertyBuilder._config = config;

        Object defaultValue = propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(Integer.class));
        assertNull(defaultValue);

        propertyBuilder._beanDesc = null;
        propertyBuilder._config = null;
        defaultValue = propertyBuilder.getDefaultValue(TypeFactory.defaultInstance().constructType(Integer.class));
        assertNull(defaultValue);
    }
}

class MyClass {
    private int property1;
    private String property2;

    public MyClass(int property1, String property2) {
        this.property1 = property1;
        this.property2 = property2;
    }
}