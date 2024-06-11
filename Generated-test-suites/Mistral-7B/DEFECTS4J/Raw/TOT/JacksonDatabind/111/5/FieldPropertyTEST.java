package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer.NullValueProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

public class FieldPropertyTest {

    static class TestBean {
        @JsonProperty(value = "field1")
        private Integer field1;
        private transient String field2;

        public Integer getField1() {
            return field1;
        }

        public void setField1(Integer field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }
    }

    @org.junit.Test
    public void testSimpleDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"field1\": 42}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertEquals(42, bean.getField1());
    }

    @org.junit.Test
    public void testNullDeserialization() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper nullMapper = mapper.copy();
        nullMapper.getValueFactory().setNullValueProvider(Mapppers.NULL_VALUE_PROVIDER);
        String json = "{\"field1\": null}";
        TestBean bean = nullMapper.readValue(json, TestBean.class);
        assertNull(bean.getField1());
    }

    @org.junit.Test
    public void testDeserializationWithEmptyString() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"field1\": \"\"}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertEquals(null, bean.getField1());
    }

    @org.junit.Test
    public void testDeserializationWithWrongType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"field1\": \"not a number\"}";
        try {
            TestBean bean = mapper.readValue(json, TestBean.class);
            fail("Expected IOE but got: " + bean);
        } catch (IOException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testDeserializationWithTransientField() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"field2\": \"test\"}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertEquals("test", bean.getField2());
    }

    @org.junit.Test
    public void testDeserializationWithAnnotations() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(new AnnotationIntrospector() {
            @Override
            public AnnotatedField findPropertiesAnnotation(BeanPropertyDefinition propDef, JavaType type) {
                return new AnnotatedField(propDef.getMember(), Annotations.findAnnotation(TestBean.class, JsonProperty.class));
            }
        });
        String json = "{\"field1\": 42}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertEquals("field1", bean.getField1Property().getName());
    }

    @org.junit.Test
    public void testDeserializationWithCustomName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"name\": 42}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertEquals(42, bean.getField1());
    }

    @org.junit.Test
    public void testDeserializationWithTypeHandler() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) {
                addDeserializer(Integer.TYPE, new CustomIntegerDeserializer());
            }
        });
        String json = "{\"field1\": 42}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertEquals(new CustomInteger(42), bean.getField1());
    }

    @org.junit.Test
    public void testDeserializationWithNullValueProvider() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper nullMapper = mapper.copy();
        nullMapper.getValueFactory().setNullValueProvider(Mapppers.NULL_VALUE_PROVIDER);
        String json = "{\"field1\": null}";
        TestBean bean = nullMapper.readValue(json, TestBean.class);
        assertNull(bean.getField1());
    }

    @org.junit.Test
    public void testDeserializationWithTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"field1\": {\"type\": \"com.test.MyType\", \"value\": 1}}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertEquals(new MyType(1), bean.getField1());
    }

    @org.junit.Test
    public void testDeserializationWithCustomDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerDeserializer(Integer.class, new CustomIntegerDeserializer());
        String json = "{\"field1\": 42}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertEquals(new CustomInteger(42), bean.getField1());
    }

    @org.junit.Test
    public void testDeserializationWithAccessModifiers() throws IOException {
        TestBean bean = new TestBean();
        FieldProperty property = new FieldProperty(new BeanPropertyDefinition(new PropertyName("field1"), Integer.TYPE), null, null, null, new AnnotatedField(new PropertyName("field1"), new JsonProperty()));
        property.fixAccess(new DeserializationConfig());
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyAccessFactory(new PropertyAccessFactory() {
            @Override
            public PropertyAccessor getPropertyAccessor(BeanProperty property) {
                return new PropertyAccessorBase() {
                    @Override
                    public void setValue(Object obj, Object value) {
                        try {
                            property.set(obj, value);
                        } catch (Exception e) {
                            throw new RuntimeException("Should not throw", e);
                        }
                    }

                    @Override
                    public Object getValue(Object obj) {
                        try {
                            return property.get(obj);
                        } catch (Exception e) {
                            throw new RuntimeException("Should not throw", e);
                        }
                    }
                };
            }
        });
        String json = "{\"field1\": 42}";
        mapper.readValue(json, bean.getClass());
        assertEquals(42, bean.getField1());
    }

    @org.junit.Test
    public void testDeserializationWithSetter() throws IOException {
        TestBean bean = new TestBean();
        Converter converter = new Converter() {
            @Override
            public Object convertNull(DeserializationContext ctxt, BeanProperty property, Object value) {
                return null;
            }

            @Override
            public Object convertValue(DeserializationContext ctxt, BeanProperty property, Object value) {
                bean.setField1((Integer) value);
                return bean;
            }
        };
        ObjectMapper mapper = new ObjectMapper();
        mapper.getValueFactory().setConverter(new SimpleType(Integer.class), converter);
        String json = "{\"field1\": 42}";
        mapper.readValue(json, bean.getClass());
        assertEquals(42, bean.getField1());
    }

    @org.junit.Test
    public void testSerializeAndDeserialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean();
        bean.setField1(1);
        String json = mapper.writeValueAsString(bean);
        TestBean deserializedBean = mapper.readValue(json, TestBean.class);
        assertEquals(bean.getField1(), deserializedBean.getField1());
    }

    static class CustomIntegerDeserializer extends StdDeserializer<CustomInteger> {
        @Override
        public CustomInteger deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new CustomInteger(p.getIntValue());
        }
    }

    static class CustomInteger {
        int value;

        public CustomInteger(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "CustomInteger{" + "value=" + value + '}';
        }
    }

    static class MyType {
        int value;

        public MyType(int value) {
            this.value = value;
        }

        @JsonDeserialize(using = CustomIntegerDeserializer.class)
        public int getValue() {
            return value;
        }
    }
}