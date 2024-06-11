package com.example.junit.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer.EmptyTypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOClass;

import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

@SuppressWarnings("unchecked")
public class FieldPropertyTest {

    // Helper class for testing
    @JsonPOJOClass(value = TestBean.class)
    static class TestBean {
        @JsonDeserialize(using = TestFieldProperty.class)
        private int _field;

        public int getField() {
            return _field;
        }

        public void setField(int field) {
            this._field = field;
        }
    }

    // Tested class
    static class TestFieldProperty extends FieldProperty {
        public TestFieldProperty(BeanPropertyDefinition propDef, JavaType type,
                TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedField field) {
            super(propDef, type, typeDeser, contextAnnotations, field);
        }

        @Override
        public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
                throws IOException {
            super.deserializeAndSet(p, ctxt, instance);
        }

        @Override
        public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance)
                throws IOException {
            return super.deserializeSetAndReturn(p, ctxt, instance);
        }

        @Override
        public void set(Object instance, Object value) throws IOException {
            super.set(instance, value);
        }

        @Override
        public Object setAndReturn(Object instance, Object value) throws IOException {
            return super.setAndReturn(instance, value);
        }
    }

    @Test
    public void testFieldProperty_BasicFunctionality() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"field\": 1}";
        TestBean testBean = mapper.readValue(json, TestBean.class);

        assertEquals(1, testBean.getField());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFieldProperty_MissingField() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{}";
        TestBean testBean = mapper.readValue(json, TestBean.class);
    }

    @Test
    public void testFieldProperty_NullValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"field\": null}";
        TestBean testBean = mapper.readValue(json, TestBean.class);

        assertNull(testBean.getField());
    }

    @Test(expected = JsonProcessingException.class)
    public void testFieldProperty_InvalidJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"field\": \"hello\"}";
        TestBean testBean = mapper.readValue(json, TestBean.class);
    }

    @Test
    public void testFieldProperty_SkipNulls() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setNullValueProvider(new NullValueProvider.DefaultProvider());

        String json = "{\"field\": null}";
        TestBean testBean = mapper.readValue(json, TestBean.class);

        assertNull(testBean.getField());
    }

    @Test
    public void testFieldProperty_CustomNullValueProvider() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setNullValueProvider(new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) {
                return "nullValue";
            }
        });

        String json = "{\"field\": null}";
        TestBean testBean = mapper.readValue(json, TestBean.class);

        assertEquals("nullValue", testBean.getField());
    }

    @Test
    public void testFieldProperty_EmptyTypeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setTypeDeserializer(JsonToken.VALUE_NULL, EmptyTypeDeserializer.instance);

        String json = "{\"field\": null}";
        TestBean testBean = mapper.readValue(json, TestBean.class);

        assertNull(testBean.getField());
    }

    @Test
    public void testFieldProperty_CustomTypeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setTypeDeserializer(TestBean.class, new JsonDeserializer<TestBean>() {
            @Override
            public TestBean deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                TestBean testBean = p.readValueAs(TestBean.class);
                testBean.setField(5);
                return testBean;
            }
        });

        String json = "{\"field\": 1}";
        TestBean testBean = mapper.readValue(json, TestBean.class);

        assertEquals(5, testBean.getField());
    }

    @Test
    public void testFieldProperty_AccessModifiers() throws Exception {
        TestBean testBean = new TestBean();

        FieldProperty fieldProperty = new FieldProperty(new BeanPropertyDefinition(
                new PropertyName("field"), TestBean.class, null, null, null, null),
                null, null, null, new AnnotatedField(TestBean.class.getField("_field"), null, null));

        ClassUtil.checkAndFixAccess(TestBean.class.getField("_field"), true);

        fieldProperty.set(testBean, "hello");

        assertEquals("hello", testBean.getField());
    }
}