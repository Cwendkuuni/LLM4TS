package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * Test cases for CreatorCollector class
 */
public class CreatorCollectorTest {

    @Test
    public void testCreatorCollectorDefault() throws IOException {
        // Create a simple JavaBean class to test with
        static class TestBean {
            private int id;
            private String name;

            TestBean() {
            }

            TestBean(int id, String name) {
                this.id = id;
                this.name = name;
            }
        }

        // Test the case where no explicit creators are provided, and the default constructor is used
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getConfig();
        config.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        CreatorCollector collector = new CreatorCollector(TestBean.class, true);
        ValueInstantiator instantiator = collector.constructValueInstantiator(config);

        // Verify that the default constructor was detected and used
        TestBean bean1 = instantiator.createValueInstance(config);
        assertEquals(0, bean1.id);
        assertNull(bean1.name);

        // Test creating an instance using the default constructor with JSON input
        String json = "{}";
        TestBean bean2 = mapper.readValue(json, TestBean.class);
        assertEquals(0, bean2.id);
        assertNull(bean2.name);
    }

    @Test
    public void testCreatorCollectorStringCreator() throws IOException {
        // Create a simple JavaBean class to test with
        static class TestBean {
            private int id;
            private String name;

            TestBean(String name) {
                this.name = name;
            }
        }

        // Test the case where a string creator is provided
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getConfig();
        config.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        TestBean testBean = new TestBean("TestBean1");
        CreatorCollector collector = new CreatorCollector(TestBean.class, true);

        // Set the string creator
        Method method = TestBean.class.getConstructor(String.class);
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(method);
        collector.addStringCreator(new AnnotatedWithParams(annotatedMethod), false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(config);

        // Verify that the string creator was used
        TestBean bean = instantiator.createValueInstance(config, "TestBean2");
        assertEquals("TestBean2", bean.name);
        assertEquals(0, bean.id);

        // Test creating an instance using the string creator with JSON input
        String json = "{\"name\":\"TestBean3\"}";
        TestBean beanFromJson = mapper.readValue(json, TestBean.class);
        assertEquals("TestBean3", beanFromJson.name);
        assertEquals(0, beanFromJson.id);
    }

    @Test
    public void testCreatorCollectorIntCreator() throws IOException {
        // Create a simple JavaBean class to test with
        static class TestBean {
            private int id;

            TestBean(int id) {
                this.id = id;
            }
        }

        // Test the case where an int creator is provided
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getConfig();
        config.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        TestBean testBean = new TestBean(1);
        CreatorCollector collector = new CreatorCollector(TestBean.class, true);

        // Set the int creator
        Method method = TestBean.class.getConstructor(int.class);
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(method);
        collector.addIntCreator(new AnnotatedWithParams(annotatedMethod), false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(config);

        // Verify that the int creator was used
        TestBean bean = instantiator.createValueInstance(config, 2);
        assertEquals(2, bean.id);

        // Test creating an instance using the int creator with JSON input
        String json = "{\"id\":3}";
        TestBean beanFromJson = mapper.readValue(json, TestBean.class);
        assertEquals(3, beanFromJson.id);
    }

    @Test
    public void testCreatorCollectorCustomCreator() throws IOException {
        // Create a simple JavaBean class to test with
        static class TestBean {
            private int id;
            private String name;

            TestBean(int id, String name) {
                this.id = id;
                this.name = name;
            }

            @JsonCreator
            static TestBean create(@JsonProperty("id") int id, @JsonProperty("name") String name) {
                return new TestBean(id, name);
            }
        }

        // Test the case where a custom creator is provided
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getConfig();
        config.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        TestBean testBean = new TestBean(1, "TestBean1");
        CreatorCollector collector = new CreatorCollector(TestBean.class, true);

        // Set the custom creator
        Method method = TestBean.class.getDeclaredMethod("create", int.class, String.class);
        method.setAccessible(true);
        AnnotatedMethod annotatedMethod = new AnnotatedMethod(method);
        collector.addIntCreator(new AnnotatedWithParams(annotatedMethod), true);

        ValueInstantiator instantiator = collector.constructValueInstantiator(config);

        // Verify that the custom creator was used
        TestBean bean = instantiator.createValueInstance(config, 2, "TestBean2");
        assertEquals(2, bean.id);
        assertEquals("TestBean2", bean.name);

        // Test creating an instance using the custom creator with JSON input
        String json = "{\"id\":3, \"name\":\"TestBean3\"}";
        TestBean beanFromJson = mapper.readValue(json, TestBean.class);
        assertEquals(3, beanFromJson.id);
        assertEquals("TestBean3", beanFromJson.name);
    }

    @Test
    public void testCreatorCollectorDelegatingCreator() throws IOException {
        // Create a simple JavaBean class to test with
        static class TestBean {
            private int id;
            private String name;
            private String prefix;

            TestBean(int id, String name, String prefix) {
                this.id = id;
                this.name = name;
                this.prefix = prefix;
            }

            static TestBean create(int id, String name, String prefix) {
                return new TestBean(id, name, prefix);
            }

            static TestBean createWithPrefix(String prefix) {
                return new TestBean(0, null, prefix);
            }
        }

        // Test the case where a delegating creator is provided
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getConfig();
        config.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        TestBean testBean = new TestBean(1, "TestBean1", "TestPrefix1");
        CreatorCollector collector = new CreatorCollector(TestBean.class, true);

        // Set the delegating creator
        Method createMethod = TestBean.class.getDeclaredMethod("create", int.class, String.class);
        createMethod.setAccessible(true);
        AnnotatedMethod createAnnotatedMethod = new AnnotatedMethod(createMethod);

        Method createWithPrefixMethod = TestBean.class.getDeclaredMethod("createWithPrefix", String.class);
        createWithPrefixMethod.setAccessible(true);
        AnnotatedMethod createWithPrefixAnnotatedMethod = new AnnotatedMethod(createWithPrefixMethod);

        CreatorProperty prefixProperty = new CreatorProperty();
        prefixProperty.setName("prefix");
        prefixProperty.setInjectableValueId("prefix");

        collector.addIntCreator(new AnnotatedWithParams(createAnnotatedMethod), false);
        collector.addCreatorProperty(new AnnotatedWithParams(createWithPrefixAnnotatedMethod), false, new CreatorProperty[] { prefixProperty });

        ValueInstantiator instantiator = collector.constructValueInstantiator(config);

        // Verify that the delegating creator was used
        TestBean bean = instantiator.createValueInstance(config, 2, "TestBean2", "TestPrefix2");
        assertEquals(2, bean.id);
        assertEquals("TestBean2", bean.name);
        assertEquals("TestPrefix2", bean.prefix);

        // Test creating an instance using the delegating creator with JSON input
        String json = "{\"id\":3, \"name\":\"TestBean3\", \"prefix\":\"TestPrefix3\"}";
        TestBean beanFromJson = mapper.readValue(json, TestBean.class);
        assertEquals(3, beanFromJson.id);
        assertEquals("TestBean3", beanFromJson.name);
        assertEquals("TestPrefix3", beanFromJson.prefix);
    }

    @Test
    public void testCreatorCollectorCustomConverter() throws IOException {
        // Create a simple JavaBean class to test with
        static class TestBean {
            private int id;
            private boolean flag;

            TestBean(int id, boolean flag) {
                this.id = id;
                this.flag = flag;
            }
        }

        // Test the case where a custom converter is provided
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig config = mapper.getConfig();
        config.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        TestBean testBean = new TestBean(1, false);
        CreatorCollector collector = new CreatorCollector(TestBean.class, true);

        // Set the converter
        Converter<String, Boolean> converter = new StdConverter<String, Boolean>() {
            @Override
            public Boolean convert(String text, DeserializationContext ctxt) throws IOException {
                return Boolean.parseBoolean(text);
            }
        };
        TypeSerializer boolSerializer = config.findValueSerializer(Boolean.TYPE, null);
        if (boolSerializer != null) {
            collector.setValueInstantiator(boolSerializer);
        }

        collector.addBooleanCreator(new AnnotatedWithParams(TestBean.class.getDeclaredConstructor(boolean.class)), false);

        ValueInstantiator instantiator = collector.constructValueInstantiator(config);

        // Verify that the custom converter was used
        TestBean bean = instantiator.createValueInstance(config, "true");
        assertEquals(1, bean.id);
        assertTrue(bean.flag);

        // Test creating an instance using the custom converter with JSON input
        String json = "{\"id\":1, \"flag\":\"true\"}";
        TestBean beanFromJson = mapper.readValue(json, TestBean.class);
        assertEquals(1, beanFromJson.id);
        assertTrue(beanFromJson.flag);
    }
}