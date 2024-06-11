package com.fasterxml.jackson.databind.tests.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TestBase;

import org.testng.annotations.*;

import java.io.IOException;
import java.util.Collections;

import static org.testng.Assert.*;

public class AsWrapperTypeDeserializerTest extends TestBase {

    static class TestBean {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class TestBeanDeserializer extends StdSerializer<TestBean> {
        public TestBeanDeserializer() { this(null); }
        public TestBeanDeserializer(Class<TestBean> t) { super(t); }

        @Override
        public TestBean deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return new TestBean();
        }
    }

    @BeforeClass
    public static void setup() throws Exception {
        setUp();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        tearDown();
    }

    @Test
    public void testDeserializeValidWrapperObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType testBeanType = mapper.getTypeFactory().constructType(TestBean.class);

        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                testBeanType, new JacksonAnnotationIntrospector(),
                "_type", true, TestBean.class);

        ObjectNode node = mapper.createObjectNode();
        node.put("_class", As.WRAPPER_OBJECT.getId());
        node.put("name", "TestBean");

        TestBean bean = mapper.readerForUpdating(TestBean.class).readValue(node);
        assertNotNull(bean);
        assertEquals("TestBean", bean.getName());
    }

    @Test
    public void testDeserializeInvalidWrapperObject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType testBeanType = mapper.getTypeFactory().constructType(TestBean.class);

        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                testBeanType, new JacksonAnnotationIntrospector(),
                "_type", true, TestBean.class);

        ObjectNode node = mapper.createObjectNode();
        node.put("_class", As.WRAPPER_OBJECT.getId());
        node.put("typeId", "NonExistentType");
        node.put("name", "TestBean");

        try {
            TestBean bean = mapper.readerForUpdating(TestBean.class).readValue(node);
            fail("Expected JacksonException");
        } catch (JsonProcessingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeNullInput() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType testBeanType = mapper.getTypeFactory().constructType(TestBean.class);

        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                testBeanType, new JacksonAnnotationIntrospector(),
                "_type", true, TestBean.class);

        ObjectNode node = mapper.createObjectNode();

        TestBean bean = mapper.readerForUpdating(TestBean.class).readValue(node);
        assertNull(bean);
    }

    @Test
    public void testDeserializeNoTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType testBeanType = mapper.getTypeFactory().constructType(TestBean.class);

        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                testBeanType, new JacksonAnnotationIntrospector(),
                "_type", true, TestBean.class);

        ObjectNode node = mapper.createObjectNode();
        node.put("name", "TestBean");

        try {
            TestBean bean = mapper.readerForUpdating(TestBean.class).readValue(node);
            fail("Expected JacksonException");
        } catch (JsonMappingException e) {
            assertEquals("No _type property found in the input", e.getMessage());
        }
    }

    @Test
    public void testDeserializeNoName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType testBeanType = mapper.getTypeFactory().constructType(TestBean.class);

        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                testBeanType, new JacksonAnnotationIntrospector(),
                "_type", true, TestBean.class);

        ObjectNode node = mapper.createObjectNode();
        node.put("_class", As.WRAPPER_OBJECT.getId());

        try {
            TestBean bean = mapper.readerForUpdating(TestBean.class).readValue(node);
            fail("Expected JacksonException");
        } catch (JsonMappingException e) {
            assertEquals("No property 'name' found in the input", e.getMessage());
        }
    }

    @Test
    public void testDeserializeInvalidClass() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType testBeanType = mapper.getTypeFactory().constructType(TestBean.class);
        JavaType intType = mapper.getTypeFactory().constructType(int.class);

        TypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                testBeanType, new JacksonAnnotationIntrospector(),
                "_type", true, TestBean.class);

        ObjectNode node = mapper.createObjectNode();
        node.put("_class", As.WRAPPER_OBJECT.getId());
        node.put("typeId", intType.getId().toString());
        node.put("name", "TestBean");

        try {
            TestBean bean = mapper.readerForUpdating(TestBean.class).readValue(node);
            fail("Expected JacksonException");
        } catch (JsonMappingException e) {
            assertEquals("Class id '"+intType.getId().toString()+"' is not a subtype of '"+testBeanType.getId().toString()+"'", e.getMessage());
        }
    }
}