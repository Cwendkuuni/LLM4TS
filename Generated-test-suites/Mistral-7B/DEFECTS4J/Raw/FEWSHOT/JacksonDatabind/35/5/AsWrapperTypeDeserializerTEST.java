package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TestBase;

public class AsWrapperTypeDeserializerTest extends TestBase {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static class TestBean {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class TestBeanDeserializer extends StdSerializer<TestBean> {
        public TestBeanDeserializer() {
            this(null);
        }

        public TestBeanDeserializer(Class<TestBean> t) {
            this(null);
        }

        public TestBeanDeserializer(Class<TestBean> t, BeanProperty property) {
            this(null);
        }

        public TestBeanDeserializer(Class<TestBean> t, BeanProperty property, AsWrapperTypeDeserializer wrapper) {
            this(null);
            this._property = property;
            this._wrapper = wrapper;
            bind(t);
        }

        public TestBean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return new TestBean();
        }

        @Override
        public As getTypeInclusion() {
            return As.WRAPPER_OBJECT;
        }
    }

    @Test
    public void testDeserializeFromObject() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(TestBean.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "type", true, TestBean.class);

        String json = "{\"type\":\"TestBean\",\"name\":\"John Doe\"}";
        Object result = mapper.readValue(json, deserializer.getType());
        assertInstanceOf(TestBean.class, result);
        TestBean testBean = (TestBean) result;
        assertEquals("John Doe", testBean.getName());
    }

    @Test
    public void testDeserializeFromArray() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(TestBean[].class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "type", true, TestBean.class);

        String json = "[{\"type\":\"TestBean\",\"name\":\"John Doe\"},{\"type\":\"TestBean\",\"name\":\"Jane Doe\"}]";
        Object result = mapper.readValue(json, deserializer.getType());
        assertInstanceOf(TestBean[].class, result.getClass());
        TestBean[] testBeans = (TestBean[]) result;
        assertEquals(2, testBeans.length);
        assertEquals("John Doe", testBeans[0].getName());
        assertEquals("Jane Doe", testBeans[1].getName());
    }

    @Test
    public void testDeserializeFromScalar() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(String.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "type", false, TestBean.class);

        String json = "{\"type\":\"TestBean\",\"name\":\"John Doe\"}";
        Object result = mapper.readValue(json, deserializer.getType());
        assertEquals("John Doe", result);
    }

    @Test
    public void testDeserializeFromAny() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(Number.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "type", false, TestBean.class);

        String json = "{\"type\":\"TestBean\",\"name\":\"John Doe\"}";
        Object result = mapper.readValue(json, deserializer.getType());
        assertInstanceOf(Number.class, result.getClass());
        assertNull(result);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(TestBean.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        idResolver.setValueIdResolver(new JavaTypeValueIdResolver(TestBean.class));
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "type", true, TestBean.class);

        String json = "{\"type\":\"TestBean\",\"name\":\"John Doe\"}";
        Object result = mapper.readValue(json, deserializer.getType());
        assertInstanceOf(TestBean.class, result);
        TestBean testBean = (TestBean) result;
        assertEquals("John Doe", testBean.getName());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithWrongJsonStructure() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(TestBean.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "type", true, TestBean.class);

        String json = "{\"name\":\"John Doe\"}";
        mapper.readValue(json, deserializer.getType());
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithMissingTypeId() throws IOException {
        JavaType javaType = mapper.getTypeFactory().constructType(TestBean.class);
        TypeIdResolver idResolver = mapper.getTypeIdResolver();
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(javaType, idResolver, "type", true, TestBean.class);

        String json = "{\"name\":\"John Doe\"}";
        mapper.readValue(json, deserializer.getType());
    }
}