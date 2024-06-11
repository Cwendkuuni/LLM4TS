package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class CreatorPropertyTest extends TestBase {

    public static class TestBean {

        @JsonCreator
        public TestBean(@JsonProperty("name") String name) {
            this.name = name;
        }

        @JsonProperty("name")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @org.junit.Test
    public void testCreatorPropertyDefault() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertNull(bean.getName());
    }

    @org.junit.Test
    public void testCreatorPropertyWithValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":\"John Doe\"}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertEquals("John Doe", bean.getName());
    }

    @org.junit.Test(expected = IOException.class)
    public void testCreatorPropertyMissingSetter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":1}"; // intentionally wrong getter/setter
        TestBean testBean = mapper.readValue(json, TestBean.class);
    }

    @org.junit.Test
    public void testCreatorPropertyWithInjectableValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"@inject\":\"JavaBean\"}";
        TestBean testBean = mapper.readValue(json, TestBean.class);
        assertNotNull(testBean);
    }

    @org.junit.Test
    public void testCreatorPropertyWithFallbackSetter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\":null}";
        TestBean testBean = mapper.readValue(json, TestBean.class);
        assertNull(testBean.getName());

        TestBean testBeanWithValue = new TestBean("TestName");
        CreatorProperty property = mapper.findProperty("name", TestBean.class);
        property.setFallbackSetter(TestBean.class.getDeclaredField("name"));
        property.inject(mapper.getDeserializationContext(), testBeanWithValue);
        assertEquals("TestName", testBeanWithValue.getName());
    }

    @org.junit.Test(expected = InvalidDefinitionException.class)
    public void testCreatorPropertyWithMissingInjectableValueId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"@TestAnnotation\":\"TestValue\"}";
        TestBean testBean = mapper.readValue(json, TestBean.class);
    }

    @org.junit.Test
    public void testCreatorPropertyWithInjectableValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"@TestAnnotation\":\"TestValue\"}";
        TestBean testBean = mapper.readValue(json, TestBean.class);
        Object injectableValue = mapper.findInjectableValue(mapper.getDeserializationContext(), testBean);
        assertNotNull(injectableValue);
        assertEquals("TestValue", injectableValue.toString());

        TestBean testBeanWithValue = new TestBean("TestName");
        CreatorProperty property = mapper.findProperty("name", TestBean.class);
        property.inject(mapper.getDeserializationContext(), testBeanWithValue);
        assertEquals("TestValue", testBeanWithValue.getName());
    }
}