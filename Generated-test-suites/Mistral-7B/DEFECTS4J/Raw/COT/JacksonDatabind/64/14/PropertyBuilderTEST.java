package com.example; // replace with the actual package name

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.core.type.*;

import org.junit.*;

public class PropertyBuilderTest {

    @Test
    public void testConstructor() {
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
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        ObjectWriter objectWriter = new ObjectMapper().writer();
        SerializerProvider serializerProvider = objectWriter.getSerializerProvider();

        JavaType javaType = TypeFactory.defaultInstance().constructType(Object[].class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new AnnotatedMethod("getProperties", Object.class), javaType, null, null, null);
        BeanPropertyWriter builder = propertyBuilder.buildWriter(serializerProvider, propDef, javaType, null, null, null, null, false);
        assertNotNull(builder);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterWithIllegalType() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        ObjectWriter objectWriter = new ObjectMapper().writer();
        SerializerProvider serializerProvider = objectWriter.getSerializerProvider();

        JavaType javaType = TypeFactory.defaultInstance().constructType(String.class);
        BeanPropertyDefinition propDef = new BeanPropertyDefinition(new AnnotatedMethod("getProperties", Object.class), javaType, null, null, null);
        propertyBuilder.buildWriter(serializerProvider, propDef, javaType, null, null, null, null, false);
        fail("Expected JsonMappingException but no exception was thrown.");
    }

    @Test
    public void testFindSerializationType() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember am = new AnnotatedMethod("getProperties", Object.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(Object[].class);

        JavaType serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertNotNull(serializationType);
        assertEquals(declaredType, serializationType);

        JsonSerialize.Typing typing = new JsonSerialize.Typing() {
            @Override
            public JsonSerialize.Typing defaultTyping() {
                return JsonSerialize.Typing.STATIC;
            }
        };
        am.setValue(typing);
        serializationType = propertyBuilder.findSerializationType(am, false, declaredType);
        assertEquals(declaredType.withStaticTyping(), serializationType);

        serializationType = propertyBuilder.findSerializationType(am, false, null);
        assertNull(serializationType);
    }

    @Test
    public void testFindSerializationTypeWithException() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        AnnotatedMember am = new AnnotatedMethod("getProperties", Object.class);
        JavaType declaredType = TypeFactory.defaultInstance().constructType(Object.class);

        try {
            propertyBuilder.findSerializationType(am, false, declaredType);
            fail("Expected JsonMappingException but no exception was thrown.");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testGetDefaultBean() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);

        Class<?> clazz = new Object() {}.getClass();
        propertyBuilder = new PropertyBuilder(config, beanDesc);
        propertyBuilder._defaultBean = new Object();
        defaultBean = propertyBuilder.getDefaultBean();
        assertNotNull(defaultBean);
        assertEquals(clazz, defaultBean.getClass());
    }

    @Test
    public void testGetPropertyDefaultValue() {
        SerializationConfig config = new SerializationConfig();
        BeanDescription beanDesc = new BeanDescription(new Object() {
            public String getName() {
                return "John Doe";
            }
        }.getClass());
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDesc);

        String name = "Jane Doe";
        AnnotatedMember am = new AnnotatedMethod("getName", Object.class);
        JavaType type = TypeFactory.defaultInstance().constructType(String.class);

        Object defaultValue = propertyBuilder.getPropertyDefaultValue("getName", am, type);
        assertEquals(name, defaultValue);

        propertyBuilder._useRealPropertyDefaults = true;
        defaultValue = propertyBuilder.getPropertyDefaultValue("getName", am, type);
        assertEquals("", defaultValue);

        propertyBuilder._useRealPropertyDefaults = false;
        assertNull(propertyBuilder.getPropertyDefaultValue("getName", am, type));
    }
}