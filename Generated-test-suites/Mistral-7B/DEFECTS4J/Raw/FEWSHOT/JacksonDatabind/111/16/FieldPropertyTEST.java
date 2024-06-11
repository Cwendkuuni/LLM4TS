package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.JsonToken.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectMapperTestBase;

public class FieldPropertyTest extends ObjectMapperTestBase {

    static class TestBean {
        @JsonProperty("field1")
        @JsonDeserialize(using = FieldProperty.class)
        private int field1;

        @JsonProperty("field2")
        @JsonDeserialize(using = FieldProperty.class)
        private String field2;

        public int getField1() {
            return field1;
        }

        public void setField1(int field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }

        @JsonProperty
        private transient Field field1Field;
        @JsonProperty
        private transient Field field2Field;

        static {
            try {
                field1Field = TestBean.class.getDeclaredField("field1");
                field1Field.setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Unexpected error", e);
            }

            try {
                field2Field = TestBean.class.getDeclaredField("field2");
                field2Field.setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Unexpected error", e);
            }
        }
    }

    @Test
    public void testFieldProperty() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(mapper.writeValueAsString(new TestBean()), TestBean.class);

        // Test integer field
        assertEquals(1, bean.getField1());
        mapper.writeValue(new File("test.json"), bean);
        bean = mapper.readValue(new File("test.json"), TestBean.class);
        assertEquals(1, bean.getField1());

        // Test string field
        assertEquals("foo", bean.getField2());
        mapper.writeValue(new File("test.json"), bean);
        bean = mapper.readValue(new File("test.json"), TestBean.class);
        assertEquals("foo", bean.getField2());

        // Test null value
        TestBean nullBean = new TestBean();
        mapper.writeValue(new File("test.json"), nullBean);
        TestBean nullReadBean = mapper.readValue(new File("test.json"), TestBean.class);
        assertNull(nullReadBean.getField1());
        assertNull(nullReadBean.getField2());

        // Test skipping nulls
        TestBean skipNullsBean = new TestBean();
        mapper.setConfig(MapperFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setConfig(MapperFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.setNullValueProvider(new NullValueProvider() {
            @Override
            public Object getNullValue(DeserializationContext ctxt) {
                return null;
            }
        });
        mapper.writeValue(new File("test.json"), skipNullsBean);
        TestBean skipNullsReadBean = mapper.readValue(new File("test.json"), TestBean.class);
        assertNull(skipNullsReadBean.getField1());
        assertNull(skipNullsReadBean.getField2());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean();
        assertEquals(0, bean.getField1());
        Object newValue = Integer.valueOf(1);
        Object result = mapper.readerForUpdating(bean)
                .readValue(mapper.writeValueAsString(newValue));
        assertEquals(1, bean.getField1());
        assertEquals(newValue, result);
    }

    @Test
    public void testSetAndReturnWithNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean();
        assertEquals(0, bean.getField1());
        Object newValue = null;
        Object result = mapper.readerForUpdating(bean)
                .readValue(mapper.writeValueAsString(newValue));
        assertEquals(0, bean.getField1());
        assertNull(result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean();
        JsonParser p = mapper.getFactory().createParser(mapper.writeValueAsString(Integer.valueOf(1)));
        mapper.reader(bean.getClass()).readValue(p);
        assertEquals(1, bean.getField1());
    }

    @Test
    public void testDeserializeAndSetWithNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean();
        JsonParser p = mapper.getFactory().createParser(mapper.writeValueAsString(null));
        mapper.reader(bean.getClass()).readValue(p);
        assertNull(bean.getField1());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean();
        JsonParser p = mapper.getFactory().createParser(mapper.writeValueAsString(Integer.valueOf(1)));
        Object result = mapper.readerForUpdating(bean).readValue(p);
        assertEquals(1, bean.getField1());
        assertEquals(bean, result);
    }

    @Test
    public void testDeserializeSetAndReturnWithNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean();
        JsonParser p = mapper.getFactory().createParser(mapper.writeValueAsString(null));
        Object result = mapper.readerForUpdating(bean).readValue(p);
        assertNull(bean.getField1());
        assertNull(result);
    }

    @Test
    public void testFixAccess() {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = new TestBean();
        FieldProperty property = mapper.findProperty("TestBean.field1", bean.getClass());
        property.fixAccess(mapper.getConfig());
        assertTrue(property._field.isAccessible());
    }

    @Test
    public void testGetAnnotation() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(mapper.writeValueAsString(new TestBean()), TestBean.class);
        JsonProperty jsonProperty = bean.getClass().getAnnotation(JsonProperty.class);
        assertEquals("field1", jsonProperty.value());
        assertEquals(JsonProperty.Access.FIELD, jsonProperty.access());
        JsonDeserialize jsonDeserialize = bean.getClass().getDeclaredField("field1")
                .getAnnotation(JsonDeserialize.class);
        assertEquals(FieldProperty.class, jsonDeserialize.using());
    }

    @Test
    public void testGetMember() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(mapper.writeValueAsString(new TestBean()), TestBean.class);
        assertEquals(TestBean.class.getDeclaredField("field1"), bean.getMember().getAnnotated());
    }

    @Test
    public void testValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(mapper.writeValueAsString(new TestBean()), TestBean.class);
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        FieldProperty property = new FieldProperty(new BeanPropertyDefinition(null, null), null, null, null, null) {
            @Override
            public JsonDeserializer<?> getValueDeserializer() {
                return valueDeserializer;
            }
        };
        assertEquals("valueDeserializer", property.getValueDeserializer().getClass().getName());
    }

    @Test
    public void testTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(mapper.writeValueAsString(new TestBean()), TestBean.class);
        TypeDeserializer typeDeserializer = new StringTypeDeserializer();
        FieldProperty property = new FieldProperty(new BeanPropertyDefinition(null, null), null, null, null, null) {
            @Override
            public TypeDeserializer getValueTypeDeserializer() {
                return typeDeserializer;
            }
        };
        assertEquals("typeDeserializer", property.getValueTypeDeserializer().getClass().getName());
    }

    @Test
    public void testNullProvider() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(mapper.writeValueAsString(new TestBean()), TestBean.class);
        NullValueProvider nullProvider = new StandardNullValueProvider();
        FieldProperty property = new FieldProperty(new BeanPropertyDefinition(null, null), null, null, null, null) {
            @Override
            public NullValueProvider getNullProvider() {
                return nullProvider;
            }
        };
        assertEquals("nullProvider", property.getNullProvider().getClass().getName());
    }

    @Test
    public void testWithName() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(mapper.writeValueAsString(new TestBean()), TestBean.class);
        assertEquals("TestBean.field1", bean.getName());
        FieldProperty property = new FieldProperty(bean, new PropertyName("newName"));
        assertEquals("newName", property.getName().asString());
    }

    @Test
    public void testWithValueDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(mapper.writeValueAsString(new TestBean()), TestBean.class);
        JsonDeserializer<String> valueDeserializer = new StringDeserializer();
        FieldProperty property = new FieldProperty(bean, null, null) {
            @Override
            public JsonDeserializer<?> getValueDeserializer() {
                return valueDeserializer;
            }
        };
        assertEquals("valueDeserializer", property.getValueDeserializer().getClass().getName());
    }

    @Test
    public void testWithNullProvider() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(mapper.writeValueAsString(new TestBean()), TestBean.class);
        NullValueProvider nullProvider = new StandardNullValueProvider();
        FieldProperty property = new FieldProperty(bean, null) {
            @Override
            public NullValueProvider getNullProvider() {
                return nullProvider;
            }
        };
        assertEquals("nullProvider", property.getNullProvider().getClass().getName());
    }

    @Test
    public void testWithTypeDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(mapper.writeValueAsString(new TestBean()), TestBean.class);
        TypeDeserializer typeDeserializer = new StringTypeDeserializer();
        FieldProperty property = new FieldProperty(bean, null, null) {
            @Override
            public TypeDeserializer getValueTypeDeserializer() {
                return typeDeserializer;
            }
        };
        assertEquals("typeDeserializer", property.getValueTypeDeserializer().getClass().getName());
    }

    @Test
    public void testReadResolve() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestBean bean = mapper.readValue(mapper.writeValueAsString(new TestBean()), TestBean.class);
        FieldProperty property = new FieldProperty(bean) {
            @Override
            public Field getField() {
                throw new UnsupportedOperationException();
            }
        };
        assertEquals(bean, property.readResolve());
    }
}

class StringDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsString();
    }
}

class StringTypeDeserializer extends TypeDeserializer {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsString();
    }

    @Override
    public void typeDeserializationParams(DeserializationConfig config, BeanProperty property, TypeDeserializer valueDeserializer) throws JsonMappingException {
        // empty implementation
    }
}

class StandardNullValueProvider implements NullValueProvider {
    @Override
    public Object getNullValue(DeserializationContext ctxt) {
        return null;
    }
}