package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanPropertyWriter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.BeanPropertyWriter.MarkerForEmpty;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.StdConverter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PropertyBuilderTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        SerializationConfig config = new SerializationConfig();
        ObjectMapper factory = new ObjectMapper();
        this.objectMapper = factory.configure(config, new AnnotationIntrospector());
    }

    @Test
    public void testDefaultConstructor() {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder._config);
        assertNull(propertyBuilder._beanDesc);
    }

    @Test
    public void testConstructor() throws Exception {
        BeanDescription beanDescription = new BeanDescription(Object.class);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
        assertEquals(config, propertyBuilder._config);
        assertEquals(beanDescription, propertyBuilder._beanDesc);
    }

    @Test
    public void testGetClassAnnotations() throws Exception {
        BeanDescription beanDescription = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        assertEquals(beanDescription.getClassAnnotations(), propertyBuilder.getClassAnnotations());
    }

    @Test
    public void testBuildWriterWithNullDeclaredType() throws Exception {
        BeanDescription beanDescription = new BeanDescription(Object.class);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
        assertNull(propertyBuilder.buildWriter(null, null, null, null, null, null, null, false));
    }

    @Test
    public void testBuildWriter() throws Exception {
        BeanDescription beanDescription = new BeanDescription(new TestBean("test"));
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writerWithView(new TestJsonView());
        SerializerProvider serializerProvider = writer.getSerializerProvider();
        TypeSerializer typeSerializer = objectMapper.getTypeSerializer();
        TypeSerializer contentTypeSerializer = null;
        AnnotatedMember am = new TestAnnotatedMember();
        JavaType declaredType = new TypeReference<TestBean>("com.fasterxml.jackson.databind.ser.test.TestBean") {}.getType();
        JsonSerializer<?> ser = null;
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, null, declaredType, ser, typeSerializer, contentTypeSerializer, am, false);
        assertNotNull(beanPropertyWriter);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildWriterWithNullSerializerProvider() throws Exception {
        BeanDescription beanDescription = new BeanDescription(Object.class);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
        propertyBuilder.buildWriter(null, null, null, null, null, null, null, false);
    }

    @Test(expected = JsonMappingException.class)
    public void testBuildWriterWithInvalidSerializationType() throws Exception {
        BeanDescription beanDescription = new BeanDescription(Object.class);
        SerializationConfig config = new SerializationConfig();
        PropertyBuilder propertyBuilder = new PropertyBuilder(config, beanDescription);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writerWithView(new TestJsonView());
        SerializerProvider serializerProvider = writer.getSerializerProvider();
        TypeSerializer typeSerializer = objectMapper.getTypeSerializer();
        TypeSerializer contentTypeSerializer = objectMapper.getTypeSerializer();
        AnnotatedMember am = new TestAnnotatedMember();
        JavaType declaredType = new TypeReference<TestBean>("com.fasterxml.jackson.databind.ser.test.TestBean") {}.getType();
        JsonSerializer<?> ser = new TestJsonSerializer();
        BeanPropertyWriter beanPropertyWriter = propertyBuilder.buildWriter(serializerProvider, null, declaredType, ser, typeSerializer, contentTypeSerializer, am, false);
    }

    @Test
    public void testFindSerializationTypeWithNoAnnotations() throws Exception {
        BeanDescription beanDescription = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        JavaType declaredType = new TypeReference<TestBean>("com.fasterxml.jackson.databind.ser.test.TestBean") {}.getType();
        assertEquals(declaredType, propertyBuilder.findSerializationType(null, false, declaredType));
    }

    @Test
    public void testFindSerializationTypeWithCustomAnnotation() throws Exception {
        BeanDescription beanDescription = new BeanDescription(new TestBean("test"));
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        Annotated a = new TestAnnotatedMember();
        JavaType declaredType = new TypeReference<TestBean>("com.fasterxml.jackson.databind.ser.test.TestBean") {}.getType();
        assertEquals(declaredType.getSuperClass(), propertyBuilder.findSerializationType(a, false, declaredType));
    }

    @Test
    public void testGetPropertyDefaultValueWithEmptyList() throws Exception {
        BeanDescription beanDescription = new BeanDescription(new TestListBean());
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        assertEquals(MarkerForEmpty.class, propertyBuilder.getPropertyDefaultValue("list", null, new TypeReference<TestListBean>("com.fasterxml.jackson.databind.ser.test.TestListBean") {}.getType()).getClass());
    }

    @Test
    public void testGetPropertyDefaultValueWithNullDefaultBean() throws Exception {
        BeanDescription beanDescription = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        assertEquals(null, propertyBuilder.getPropertyDefaultValue("test", null, null));
    }

    @Test
    public void testGetPropertyDefaultValueWithDefaultBean() throws Exception {
        BeanDescription beanDescription = new BeanDescription(new TestBean("test"));
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        assertEquals(new TestBean("test").toString(), propertyBuilder.getPropertyDefaultValue("toString", null, beanDescription.getType()));
    }

    @Test
    public void testGetPropertyDefaultValueWithNonDefaultBean() throws Exception {
        BeanDescription beanDescription = new BeanDescription(new TestBean("test"));
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        TestBean testBean = new TestBean("test");
        assertEquals(testBean.getName(), propertyBuilder.getPropertyDefaultValue("name", new TestAnnotatedMember(), beanDescription.getType()));
    }

    @Test
    public void testGetDefaultBeanWithNullBeanDesc() throws Exception {
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, null);
        assertNull(propertyBuilder.getDefaultBean());
    }

    @Test
    public void testGetDefaultBeanWithEmptyBean() throws Exception {
        BeanDescription beanDescription = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        assertNull(propertyBuilder.getDefaultBean());
    }

    @Test
    public void testGetDefaultBeanWithDefaultConstructorBean() throws Exception {
        BeanDescription beanDescription = new BeanDescription(Object.class);
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertNull(defaultBean);
    }

    @Test
    public void testGetDefaultBeanWithCustomConstructorBean() throws Exception {
        BeanDescription beanDescription = new BeanDescription(new TestBean("test"));
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        Object defaultBean = propertyBuilder.getDefaultBean();
        assertEquals("test", defaultBean.toString());
    }

    @Test
    public void testFindSerializationTypeWithStaticTyping() throws Exception {
        BeanDescription beanDescription = new BeanDescription(new TestBean("test"));
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        Annotated a = new TestAnnotatedMember();
        JavaType declaredType = new TypeReference<TestBean>("com.fasterxml.jackson.databind.ser.test.TestBean") {}.getType();
        JavaType javaType = propertyBuilder.findSerializationType(a, true, declaredType);
        assertEquals(declaredType, javaType);
    }

    @Test
    public void testFindSerializationTypeWithDynamicTyping() throws Exception {
        BeanDescription beanDescription = new BeanDescription(new TestBean("test"));
        PropertyBuilder propertyBuilder = new PropertyBuilder(null, beanDescription);
        Annotated a = new TestAnnotatedMember();
        JavaType declaredType = new TypeReference<TestBean>("com.fasterxml.jackson.databind.ser.test.TestBean") {}.getType();
        JavaType javaType = propertyBuilder.findSerializationType(a, false, declaredType);
        assertNull(javaType);
    }

    @Test
    public void testThrowWrappedExceptionWithStringPropName() throws Exception {
        String propName = "test";
        Object defaultBean = new Object();
        Exception e = new RuntimeException("Test Exception");
        Object throwableObject = propertyBuilder._throwWrapped(e, propName, defaultBean);
        assertEquals("Failed to get property '" + propName + "' of default " + defaultBean.getClass().getName() + " instance", throwableObject.getMessage());
    }

    @Test
    public void testThrowWrappedExceptionWithNumberPropName() throws Exception {
        int propName = 1;
        Object defaultBean = new Object();
        Exception e = new RuntimeException("Test Exception");
        Object throwableObject = propertyBuilder._throwWrapped(e, propName, defaultBean);
        assertEquals("Failed to get property '" + propName + "' of default " + defaultBean.getClass().getName() + " instance", throwableObject.getMessage());
    }

    @Test
    public void testThrowWrappedExceptionWithBooleanPropName() throws Exception {
        boolean propName = true;
        Object defaultBean = new Object();
        Exception e = new RuntimeException("Test Exception");
        Object throwableObject = propertyBuilder._throwWrapped(e, propName, defaultBean);
        assertEquals("Failed to get property '" + propName + "' of default " + defaultBean.getClass().getName() + " instance", throwableObject.getMessage());
    }

    @Test
    public void testThrowWrappedExceptionWithNullDefaultBean() throws Exception {
        String propName = "test";
        Object defaultBean = null;
        Exception e = new RuntimeException("Test Exception");
        Object throwableObject = propertyBuilder._throwWrapped(e, propName, defaultBean);
        assertEquals("Failed to get property 'test' of default null instance", throwableObject.getMessage());
    }

    private static class TestBean {
        private final String name;

        TestBean(String name) {
            this.name = name;
        }

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        String getToString() {
            return name;
        }

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class TestListBean {
        private List<String> list;

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public List<String> getList() {
            return list;
        }

        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        public void setList(List<String> list) {
            this.list = list;
        }
    }

    private static class TestAnnotatedMember {
        @JsonSerialize(using = TestJsonSerializer.class)
        private Object value;

        public Object getValue() throws IllegalAccessException, InvocationTargetException {
            Method method = TestAnnotatedMember.class.getDeclaredMethod("getValue");
            method.setAccessible(true);
            return method.invoke(this);
        }

        public void setValue(Object value) throws IllegalAccessException, InvocationTargetException {
            Method method = TestAnnotatedMember.class.getDeclaredMethod("setValue", Object.class);
            method.setAccessible(true);
            method.invoke(this, value);
        }

        public Object getNullableValue() {
            return value;
        }

        public void setNullableValue(Object value) {
            this.value = value;
        }
    }

    private static class TestJsonSerializer extends StdConverter<Object, String> {
        @Override
        public String convertAndWrite(Object t, JsonGenerator jg, SerializerProvider serializers) throws IOException {
            jg.writeString(t.toString());
            return null;
        }
    }

    private static class TestJsonView extends View {
        @Override
        public void configure(MapperConfig config, DeserializationConfig deserializationConfig, SerializationConfig serializationConfig) throws JsonMappingException {
            deserializationConfig.findAndRegisterConverter(new ClassToInstanceConverter(TestBean.class));
            deserializationConfig.findAndRegisterConverter(new ClassToInstanceConverter(TestListBean.class));
            serializationConfig.findAndRegisterConverter(new ClassToInstanceConverter(TestAnnotatedMember.class));
        }

        @Override
        public void serializeValue(SerializerProvider serializers, JsonGenerator jsonGenerator, Object value) throws IOException {
            jsonGenerator.writeObject(value);
        }

        @Override
        public void deserializeValue(DeserializationContext deserializationContext, JsonParser p, Object c) throws IOException {
            Class<?> valueClass = c.getClass();
            if (TestBean.class.isAssignableFrom(valueClass)) {
                DeserializationContext context = deserializationContext;
                JsonParser jsonParser = p;
                Object bean = context.readValueAs(valueClass, jsonParser);
                c = bean;
            } else if (TestListBean.class.isAssignableFrom(valueClass)) {
                DeserializationContext context = deserializationContext;
                JsonParser jsonParser = p;
                Object list = context.readValueAs(new TypeReference<List<String>>(){}.getType(), jsonParser);
                c = list;
            }
        }
    }

    
}