package com.example.propertybuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class PropertyBuilderTest {

    private SerializationConfig serializationConfig;
    private BeanDescription beanDescription;
    private PropertyBuilder propertyBuilder;

    @Before
    public void setup() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        serializationConfig = objectMapper.getSerializationConfig();
        beanDescription = BeanDescription.forDeserialization(MyBean.class, rootNode);
        propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);
    }

    @Test
    public void testGetClassAnnotations() {
        Annotations annotations = propertyBuilder.getClassAnnotations();
        assertNotNull(annotations);
    }

    @Test
    public void testBuildWriter_NoAnnotation_DefaultValues() throws JsonMappingException {
        MyBean myBean = new MyBean();
        MyBeanProperty myBeanProperty = new MyBeanProperty();
        JavaType declaredType = ObjectTypeFactory.defaultInstance().constructType(MyBean.class);
        JsonSerializer<?> serializer = mock(JsonSerializer.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);

        BeanPropertyWriter result = propertyBuilder.buildWriter(mock(SerializerProvider.class),
                myBeanProperty, declaredType, serializer, typeSerializer, contentTypeSerializer, am, false);

        assertEquals(MyBean.class, result.getType());
        assertEquals(MyBeanProperty.class, myBeanProperty);
        assertTrue(result.isWritable());
        assertTrue(result.isSerializationBypassed());
        assertNull(result.getNullSerializer());
        assertNull(result.getUnwrappingNameTransformer());
    }

    @Test
    public void testBuildWriter_CustomSerializationType_SerializesCorrectly() throws JsonMappingException, JsonProcessingException {
        MyBean myBean = new MyBean();
        MyBeanProperty myBeanProperty = new MyBeanProperty();
        JavaType declaredType = ObjectTypeFactory.constructType(MyBean.class, MyCustomBean.class);
        JsonSerializer<?> serializer = mock(JsonSerializer.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);

        when(am.getValue(anyObject())).thenReturn(MyCustomBean.CUSTOM_VALUE);

        BeanPropertyWriter result = propertyBuilder.buildWriter(mock(SerializerProvider.class),
                myBeanProperty, declaredType, serializer, typeSerializer, contentTypeSerializer, am, false);

        ObjectWriter writer = serializationConfig.getObjectWriter();
        String json = writer.writeValueAsString(myBean);

        assertJsonEquals("{\"customProperty\":\"MyCustomBean.CUSTOM_VALUE\"}", json);
    }

    @Test
    public void testBuildWriter_CustomSerializationType_SerializerUsed() throws JsonMappingException {
        MyBean myBean = new MyBean();
        MyBeanProperty myBeanProperty = new MyBeanProperty();
        JavaType declaredType = ObjectTypeFactory.defaultInstance().constructType(MyBean.class);
        JsonSerializer<?> serializer = mock(JsonSerializer.class);
        TypeSerializer typeSerializer = mock(TypeSerializer.class);
        TypeSerializer contentTypeSerializer = mock(TypeSerializer.class);
        AnnotatedMember am = mock(AnnotatedMember.class);

        when(am.getValue(anyObject())).thenReturn(MyBean.DEFAULT_VALUE);

        when(serializer.serializeValues(any(), any(), any(), any(), any()))
                .thenReturn(new StringBuffer());

        propertyBuilder.buildWriter(mock(SerializerProvider.class), myBeanProperty, declaredType, serializer, typeSerializer, contentTypeSerializer, am, true);

        verify(serializer).serializeValues(myBean, null, null, null, null);
    }

    @Test
    public void testFindSerializationType_NoAnnotation_DefaultValues() {
        MyBeanProperty myBeanProperty = new MyBeanProperty();
        JavaType declaredType = ObjectTypeFactory.defaultInstance().constructType(MyBean.class);

        JavaType result = propertyBuilder.findSerializationType(null, false, declaredType);

        assertEquals(declaredType, result);
    }

    @Test
    public void testFindSerializationType_CustomAnnotation_CustomValues() {
        MyBeanProperty myBeanProperty = new MyBeanProperty();
        JavaType declaredType = ObjectTypeFactory.constructType(MyBean.class, MyCustomBean.class);

        JavaType result = propertyBuilder.findSerializationType(null, true, declaredType);

        assertEquals(MyCustomBean.class, result.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindSerializationType_InvalidAnnotation() {
        MyBeanProperty myBeanProperty = new MyBeanProperty();
        JavaType declaredType = ObjectTypeFactory.defaultInstance().constructType(MyBean.class);

        propertyBuilder.findSerializationType(null, true, declaredType);
    }

    @Test
    public void testGetPropertyDefaultValue_StringProperty_DefaultValue() {
        String defaultValue = propertyBuilder.getPropertyDefaultValue("name", null, String.class);

        assertEquals("", defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_IntegerProperty_DefaultValue() {
        int defaultValue = propertyBuilder.getPropertyDefaultValue("age", null, Integer.TYPE);

        assertEquals(0, defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_ListProperty_DefaultValue() {
        JavaType javaType = ObjectTypeFactory.defaultInstance().constructTypeCollectionType(List.class, Integer.TYPE);
        Object defaultValue = propertyBuilder.getPropertyDefaultValue("numbers", null, javaType);

        assertTrue(defaultValue instanceof List);
        assertTrue(((List<?>) defaultValue).isEmpty());
    }

    @Test
    public void testGetPropertyDefaultValue_BeanProperty_DefaultValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        SerializationConfig serializationConfig = objectMapper.getSerializationConfig();
        BeanDescription beanDescription = BeanDescription.forDeserialization(MyBean.class, rootNode);
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

        Object defaultValue = propertyBuilder.getPropertyDefaultValue("myBeanProperty", null, MyBeanProperty.class);

        assertNull(defaultValue);
    }

    @Test
    public void testGetPropertyDefaultValue_BeanProperty_DefaultBean() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        SerializationConfig serializationConfig = objectMapper.getSerializationConfig();
        BeanDescription beanDescription = BeanDescription.forDeserialization(MyBean.class, rootNode);
        PropertyBuilder propertyBuilder = new PropertyBuilder(serializationConfig, beanDescription);

        MyBean myBean = new MyBean();
        MyBeanProperty myBeanProperty = new MyBeanProperty();

        Object defaultValue = propertyBuilder.getPropertyDefaultValue("myBeanProperty", new AnnotatedClassMember(MyBean.class, "myBeanProperty"), MyBeanProperty.class);

        assertEquals(myBean, defaultValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_InvalidPropertyName() {
        propertyBuilder.getPropertyDefaultValue("", null, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyDefaultValue_InvalidPropertyType() {
        propertyBuilder.getPropertyDefaultValue("name", null, new Class[0]);
    }

    private static class MyBean {
        private String name;
        private int age;
        private List<Integer> numbers;

        public static final String DEFAULT_VALUE = "default value";

        public static class MyCustomBean {
            public static final String CUSTOM_VALUE = "custom value";
        }

        public MyBean() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public List<Integer> getNumbers() {
            return numbers;
        }

        public void setNumbers(List<Integer> numbers) {
            this.numbers = numbers;
        }
    }

    private static class MyBeanProperty {
    }

    private static class AnnotatedClassMember {
        private Class<?> clazz;
        private String name;

        public AnnotatedClassMember(Class<?> clazz, String name) {
            this.clazz = clazz;
            this.name = name;
        }

        public AnnotatedElement getElement() {
            return null;
        }

        public Method getMember() {
            return null;
        }

        public AnnotatedMember getValue(Object obj) {
            try {
                return clazz.getDeclaredField(name).get(obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
    }
}