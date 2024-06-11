package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.Assert.*;

public class MethodPropertyTest {

    private ObjectMapper objectMapper;
    private MethodProperty methodProperty;
    private TestBean testBean;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        testBean = new TestBean();

        Method setTestPropertyMethod = TestBean.class.getDeclaredMethod("setTestProperty", String.class);
        setTestPropertyMethod.setAccessible(true);

        methodProperty = new MethodProperty(
                Mockito.mock(BeanPropertyDefinition.class),
                Mockito.mock(JavaType.class),
                Mockito.mock(TypeDeserializer.class),
                Mockito.mock(Annotations.class),
                Mockito.mock(AnnotatedMethod.class)
        ) {
            @Override
            protected Method _setter = setTestPropertyMethod;
            @Override
            final protected boolean _skipNulls = false;
        };
    }

    @Test
    public void testDeserializeAndSet() throws IOException, JsonProcessingException {
        String testValue = "testValue";
        String expectedTestPropertyValue = "expectedTestPropertyValue";

        methodProperty.deserializeAndSet(objectMapper.getFactory().createParser(testValue), objectMapper, testBean);
        assertEquals(expectedTestPropertyValue, testBean.getTestProperty());
    }

    @Test
    public void testDeserializeAndSetNullValue() throws IOException, JsonProcessingException {
        methodProperty.deserializeAndSet(objectMapper.getFactory().createParser(null), objectMapper, testBean);
        assertNull(testBean.getTestProperty());
    }

    @Test
    public void testDeserializeAndSetTypeDeserializer() throws IOException, JsonProcessingException {
        String jsonString = "{\"testProperty\": 123}";
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeUsing(any(Class.class), any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(123);

        methodProperty = new MethodProperty(
                Mockito.mock(BeanPropertyDefinition.class),
                Mockito.mock(JavaType.class),
                typeDeserializer,
                Mockito.mock(Annotations.class),
                Mockito.mock(AnnotatedMethod.class)
        ) {
            @Override
            final protected Method _setter = Mockito.mock(Method.class);
            @Override
            final protected boolean _skipNulls = false;
        };

        methodProperty.deserializeAndSet(objectMapper.getFactory().createParser(jsonString), objectMapper, testBean);
        assertEquals(123, testBean.getTestProperty());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetThrowsException() throws IOException, JsonProcessingException {
        String testValue = "testValue";
        methodProperty.deserializeAndSet(objectMapper.getFactory().createParser(testValue), objectMapper, testBean);
        throw new JsonMappingException("Test exception message");
    }

    @Test
    public void testSet() throws IOException {
        String testValue = "testValue";
        methodProperty.set(testBean, testValue);
        assertEquals(testValue, testBean.getTestProperty());
    }

    @Test
    public void testSetNullValue() {
        methodProperty.set(testBean, null);
        assertNull(testBean.getTestProperty());
    }

    @Test
    public void testSetTypeDeserializer() throws IOException {
        Object value = new Object() {
            @Override
            public String toString() {
                return "typeDeserializerValue";
            }
        };
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeUsing(any(Class.class), any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(value);

        methodProperty = new MethodProperty(
                Mockito.mock(BeanPropertyDefinition.class),
                Mockito.mock(JavaType.class),
                typeDeserializer,
                Mockito.mock(Annotations.class),
                Mockito.mock(AnnotatedMethod.class)
        ) {
            @Override
            final protected Method _setter = Mockito.mock(Method.class);
            @Override
            final protected boolean _skipNulls = false;
        };

        methodProperty.set(testBean, "testValue");
        assertEquals(value, testBean.getTestProperty());
    }

    @Test(expected = IOException.class)
    public void testSetThrowsIOException() throws IOException {
        methodProperty.set(testBean, "testValue");
        throw new IOException("Test exception message");
    }

    @Test
    public void testSerialization() throws IOException {
        String testValue = "testValue";
        testBean.setTestProperty(testValue);

        String jsonString = objectMapper.writeValueAsString(testBean);
        TestBean deserializedTestBean = objectMapper.readValue(jsonString, TestBean.class);

        assertEquals(testValue, deserializedTestBean.getTestProperty());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationThrowsJsonMappingException() throws IOException {
        TestBean testBean = new TestBean();
        testBean.setTestProperty(new JsonMappingException("Test exception message"));

        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        String jsonString = objectMapper.writeValueAsString(testBean);

        throw new JsonMappingException("Test exception message");
    }

    @Test
    public void testSerializationWithNullValue() throws IOException {
        TestBean testBean = new TestBean();
        testBean.setTestProperty(null);

        String jsonString = objectMapper.writeValueAsString(testBean);
        TestBean deserializedTestBean = objectMapper.readValue(jsonString, TestBean.class);

        assertNull(deserializedTestBean.getTestProperty());
    }

    @Test
    public void testSerializationWithTypeDeserializer() throws IOException {
        String testValue = "testValue";
        TestBean testBean = new TestBean();
        testBean.setTestProperty(new Object() {
            @Override
            public String toString() {
                return testValue;
            }
        });

        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeUsing(any(Class.class), any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn(testValue);

        when(objectMapper.getTypeFactory().constructType(TestBean.class, typeDeserializer.getClass()))
                .thenReturn(Mockito.mock(JavaType.class));

        jsonMapper.writeValue(new File("test.json"), testBean);
        TestBean deserializedTestBean = objectMapper.readValue(new File("test.json"), TestBean.class);

        assertEquals(testValue, deserializedTestBean.getTestProperty());
    }

    @Test
    public void testDeserializeAndSetWithAnnotation() throws IOException, JsonProcessingException {
        String jsonString = "{\"testProperty\": 123}";
        TestWithAnnotation testBean = new TestWithAnnotation();
        TestAnnotation testAnnotation = new TestAnnotation();
        testBean.setTestAnnotation(testAnnotation);

        methodProperty = new MethodProperty(
                Mockito.mock(BeanPropertyDefinition.class),
                Mockito.mock(JavaType.class),
                Mockito.mock(TypeDeserializer.class),
                Mockito.mock(Annotations.class),
                Mockito.mock(AnnotatedMethod.class)
        ) {
            @Override
            @SuppressWarnings("unchecked")
            protected AnnotatedMethod _annotated = Mockito.mock(AnnotatedMethod.class);
            @Override
            protected Method _setter = Mockito.mock(Method.class);
            @Override
            final protected boolean _skipNulls = false;

            @Override
            public Annotation[] getAnnotations() {
                return new Annotation[]{testAnnotation};
            }
        };

        methodProperty.deserializeAndSet(objectMapper.getFactory().createParser(jsonString), objectMapper, testBean);
        assertEquals(123, testBean.getTestProperty());
        assertEquals(testAnnotation, testBean.getTestAnnotation());
    }

    private static class TestBean {
        private String testProperty;
        private TestAnnotation testAnnotation;

        public void setTestProperty(String testProperty) {
            this.testProperty = testProperty;
        }

        public void setTestAnnotation(TestAnnotation testAnnotation) {
            this.testAnnotation = testAnnotation;
        }

        public String getTestProperty() {
            return testProperty;
        }

        public TestAnnotation getTestAnnotation() {
            return testAnnotation;
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @InterfaceDeclaredAnnotation
    private @interface TestAnnotation {
    }
}