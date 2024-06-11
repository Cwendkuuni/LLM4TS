package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

/**
 * Unit tests for {@link InnerClassProperty}.
 */
public class InnerClassPropertyTest {

    private static class TestInnerClass {
        private TestInnerClassInner inner;
    }

    private static class TestInnerClassInner {
        private String str;
    }

    @Test
    public void testInnerClassPropertyDeserialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "{\"inner\":{\"str\":\"test\"}}";
        TestInnerClass testInnerClass = mapper.readValue(json, TestInnerClass.class);
        assertEquals("test", testInnerClass.inner.str);
    }

    @Test(expected = IOException.class)
    public void testInnerClassPropertyDeserializationMissingConstructor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "{\"inner\":null}";
        TestInnerClass testInnerClass = mapper.readValue(json, TestInnerClass.class);
        fail("Expected IOException: Missing constructor (broken JDK (de)serialization?)");
    }

    @Test
    public void testInnerClassPropertyDeserializationWithAnnotations() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonDeserializer<TestInnerClassInner> innerDeserializer = new TestInnerClassInnerDeserializer();
        mapper.registerVal(TestInnerClassInner.class, innerDeserializer);

        String json = "{\"inner\":{\"@TestAnnotation\":{}}}}";
        TestInnerClass testInnerClass = mapper.readValue(json, TestInnerClass.class);
        assertNotNull(testInnerClass.inner);
        assertTrue(testInnerClass.inner instanceof TestInnerClassInner);
        assertNotNull(testInnerClass.inner.getClass().getAnnotation(TestAnnotation.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInnerClassPropertyDeserializationWithNullAnnotatedConstructor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "{\"inner\":null}";
        TestInnerClass testInnerClass = mapper.readValue(json, TestInnerClass.class);
        throw new IllegalArgumentException("Missing constructor (broken JDK (de)serialization?)");
    }

    @Test
    public void testInnerClassPropertySerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        TestInnerClass testInnerClass = new TestInnerClass();
        testInnerClass.inner = new TestInnerClassInner();
        testInnerClass.inner.str = "test";

        String json = mapper.writeValueAsString(testInnerClass);
        assertEquals("{\"inner\":{\"str\":\"test\"}}", json);
    }

    @Test
    public void testInnerClassPropertySerializationWithAnnotations() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        TestInnerClass testInnerClass = new TestInnerClass();
        testInnerClass.inner = new TestInnerClassInner();
        testInnerClass.inner.str = "test";

        TestAnnotation testAnnotation = new TestAnnotation();
        testInnerClass.inner.getClass().getDeclaredField("str").setAccessible(true);
        testInnerClass.inner.getClass().getDeclaredField("str").set(testInnerClass.inner, testAnnotation);

        String json = mapper.writeValueAsString(testInnerClass);
        ObjectNode node = mapper.readTree(json);
        ObjectNode innerNode = node.path("inner");
        Assert.assertNotNull(innerNode);
        Assert.assertEquals("TestAnnotation", innerNode.getClass().getName());
        Assert.assertNotNull(innerNode.get("@TestAnnotation"));
    }

    static class TestInnerClassInnerDeserializer extends StdDeserializer<TestInnerClassInner> {
        @Override
        public TestInnerClassInner deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException {
            TestInnerClassInner inner = new TestInnerClassInner();
            jp.nextToken();
            jp.readField("str");
            inner.str = jp.getValueAsString();
            return inner;
        }
    }

    @TestAnnotation
    static class TestAnnotation {
        // Empty annotation
    }
}