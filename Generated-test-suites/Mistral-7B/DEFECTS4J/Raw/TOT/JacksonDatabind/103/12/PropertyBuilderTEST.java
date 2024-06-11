package com.fasterxml.jackson.databind.ser.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Test for {@link PropertyBuilder} class.
 */
public class PropertyBuilderTest {

    /**
     * Test for constructor and basic functionality.
     */
    @Test
    public void testPropertyBuilder() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonInclude.Value globalInclude = JsonInclude.Value.includeAnnotations();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setDefaultPropertyInclusion(globalInclude);

        BeanDescription beanDesc = mapper.constructType(TestBean.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        assertEquals(globalInclude, builder._defaultInclusion);
        assertEquals(false, builder._useRealPropertyDefaults);
    }

    /**
     * Test for handling of {@link JsonInclude.Include#NON_DEFAULT} inclusion type with default
     * POJO value.
     */
    @Test
    public void testPropertyBuilderNonDefaultInclusionWithDefaultPOJOValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonInclude.Value globalInclude = JsonInclude.Value.includeAnnotations();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setDefaultPropertyInclusion(globalInclude);

        BeanDescription beanDesc = mapper.constructType(TestBean.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        TestBean defaultBean = new TestBean();
        Object defaultValue = "default";
        defaultBean.setStringProperty(defaultValue);

        builder._defaultBean = defaultBean;

        BeanPropertyWriter writer = builder.buildWriter(null, null, TestBean.class, null, null, null, null, false);

        // Test serialization with NON_DEFAULT inclusion type and default POJO value
        ObjectMapper mapper2 = new ObjectMapper();
        mapper2.setSerializationConfig(builder.getSerializationConfig());
        String json = mapper2.writeValueAsString(new TestBean());
        assertNull(json);

        json = mapper2.writeValueAsString(defaultBean);
        assertEquals("{\"stringProperty\":\"" + defaultValue + "\"}", json);

        // Test deserialization with NON_DEFAULT inclusion type and default POJO value
        TestBean deserBean = mapper2.readValue(json, TestBean.class);
        assertEquals(defaultBean.getStringProperty(), deserBean.getStringProperty());
    }

    /**
     * Test for handling of {@link JsonInclude.Include#NON_DEFAULT} inclusion type with non-default
     * POJO value.
     */
    @Test
    public void testPropertyBuilderNonDefaultInclusionWithNonDefaultPOJOValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonInclude.Value globalInclude = JsonInclude.Value.includeAnnotations();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setDefaultPropertyInclusion(globalInclude);

        BeanDescription beanDesc = mapper.constructType(TestBean.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        TestBean nonDefaultBean = new TestBean();
        nonDefaultBean.setStringProperty("nonDefault");

        builder._defaultBean = nonDefaultBean;

        BeanPropertyWriter writer = builder.buildWriter(null, null, TestBean.class, null, null, null, null, false);

        // Test serialization with NON_DEFAULT inclusion type and non-default POJO value
        ObjectMapper mapper2 = new ObjectMapper();
        mapper2.setSerializationConfig(builder.getSerializationConfig());
        String json = mapper2.writeValueAsString(new TestBean());
        assertNull(json);

        json = mapper2.writeValueAsString(nonDefaultBean);
        assertEquals("{\"stringProperty\":\"nonDefault\"}", json);

        // Test deserialization with NON_DEFAULT inclusion type and non-default POJO value
        TestBean deserBean = mapper2.readValue(json, TestBean.class);
        assertEquals(nonDefaultBean.getStringProperty(), deserBean.getStringProperty());
    }

    /**
     * Test for handling of {@link JsonInclude.Include#NON_DEFAULT} inclusion type without a default
     * POJO value.
     */
    @Test
    public void testPropertyBuilderNonDefaultInclusionWithoutDefaultPOJOValue() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonInclude.Value globalInclude = JsonInclude.Value.includeAnnotations();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setDefaultPropertyInclusion(globalInclude);

        BeanDescription beanDesc = mapper.constructType(TestBean.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        TestBean nonDefaultBean = new TestBean();
        nonDefaultBean.setStringProperty("nonDefault");

        builder._defaultBean = null;

        BeanPropertyWriter writer = builder.buildWriter(null, null, TestBean.class, null, null, null, null, false);

        // Test serialization with NON_DEFAULT inclusion type and no default POJO value
        ObjectMapper mapper2 = new ObjectMapper();
        mapper2.setSerializationConfig(builder.getSerializationConfig());
        String json = mapper2.writeValueAsString(new TestBean());
        assertNull(json);

        // Test deserialization with NON_DEFAULT inclusion type and no default POJO value
        try {
            TestBean deserBean = mapper2.readValue("{}", TestBean.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Pass
        }
    }

    /**
     * Test for handling of {@link JsonInclude.Include#NON_EMPTY} inclusion type.
     */
    @Test
    public void testPropertyBuilderNonEmptyInclusion() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonInclude.Value globalInclude = JsonInclude.Value.includeAnnotations();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setDefaultPropertyInclusion(globalInclude);

        BeanDescription beanDesc = mapper.constructType(TestBean.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        TestBean bean = new TestBean();
        bean.setStringProperty(Collections.emptyList());

        BeanPropertyWriter writer = builder.buildWriter(null, null, TestBean.class, null, null, null, null, false);

        // Test serialization with NON_EMPTY inclusion type and empty list
        ObjectMapper mapper2 = new ObjectMapper();
        mapper2.setSerializationConfig(builder.getSerializationConfig());
        String json = mapper2.writeValueAsString(bean);
        assertNull(json);

        // Test deserialization with NON_EMPTY inclusion type and empty list
        TestBean deserBean = mapper2.readValue("{}", TestBean.class);
        assertNull(deserBean.getStringProperty());

        // Test serialization with NON_EMPTY inclusion type and non-empty list
        List<String> nonEmptyList = Arrays.asList("nonEmpty1", "nonEmpty2");
        bean.setStringProperty(nonEmptyList);

        json = mapper2.writeValueAsString(bean);
        assertEquals("[\"nonEmpty1\",\"nonEmpty2\"]", json);

        // Test deserialization with NON_EMPTY inclusion type and non-empty list
        TestBean deserBean2 = mapper2.readValue(json, TestBean.class);
        assertEquals(nonEmptyList, deserBean2.getStringProperty());
    }

    /**
     * Test for handling of {@link JsonInclude.Include#NON_NULL} inclusion type.
     */
    @Test
    public void testPropertyBuilderNonNullInclusion() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonInclude.Value globalInclude = JsonInclude.Value.includeAnnotations();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setDefaultPropertyInclusion(globalInclude);

        BeanDescription beanDesc = mapper.constructType(TestBean.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        TestBean bean = new TestBean();
        bean.setStringProperty(null);

        BeanPropertyWriter writer = builder.buildWriter(null, null, TestBean.class, null, null, null, null, false);

        // Test serialization with NON_NULL inclusion type and null value
        ObjectMapper mapper2 = new ObjectMapper();
        mapper2.setSerializationConfig(builder.getSerializationConfig());
        String json = mapper2.writeValueAsString(bean);
        assertNull(json);

        // Test deserialization with NON_NULL inclusion type and null value
        try {
            TestBean deserBean = mapper2.readValue("{}", TestBean.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Pass
        }

        // Test serialization with NON_NULL inclusion type and non-null value
        String nonNullValue = "nonNull";
        bean.setStringProperty(nonNullValue);

        json = mapper2.writeValueAsString(bean);
        assertEquals("{\"stringProperty\":\"" + nonNullValue + "\"}", json);

        // Test deserialization with NON_NULL inclusion type and non-null value
        TestBean deserBean3 = mapper2.readValue(json, TestBean.class);
        assertEquals(nonNullValue, deserBean3.getStringProperty());
    }

    /**
     * Test for handling of custom inclusion filter.
     */
    @Test
    public void testPropertyBuilderCustomInclusionFilter() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonInclude.Value globalInclude = JsonInclude.Value.includeAnnotations();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setDefaultPropertyInclusion(globalInclude);

        BeanDescription beanDesc = mapper.constructType(TestBean.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        TestBean bean = new TestBean();

        BeanPropertyWriter writer = builder.buildWriter(null, null, TestBean.class, null, null, null, null, false);

        // Test serialization with custom inclusion filter and null value
        ObjectMapper mapper2 = new ObjectMapper();
        mapper2.setSerializationConfig(builder.getSerializationConfig());
        Map<String, JsonFilter> filters = new HashMap<>();
        JsonInclude.IncludeFilter includeFilter = new JsonInclude.IncludeFilter() {
            @Override
            public boolean includeField(Serializers.SerializerProvider serializers, BeanPropertyWriter writer)
                    throws JsonMappingException {
                return filters.containsKey(writer.getName());
            }
        };
        JsonInclude.Value customInclude = JsonInclude.Value.custom(includeFilter);
        config.setDefaultPropertyInclusion(customInclude);

        filters.put("stringProperty", new JsonFilter() {
            @Override
            public void serializeAsValue(SerializerProvider prov, Serializer serializers, Object value,
                    TypeSerializer vs) throws Exception {
                if (value == null) {
                    prov.getSerializerProvider().defaultSerializeNull(prov.getOutput(), null);
                } else {
                    serializers.serializeValue(prov, value, vs);
                }
            }
        });

        String json = mapper2.writeValueAsString(bean);
        assertNull(json);

        // Test deserialization with custom inclusion filter and null value
        try {
            TestBean deserBean = mapper2.readValue("{}", TestBean.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Pass
        }

        // Test serialization with custom inclusion filter and non-null value
        String nonNullValue = "nonNull";
        bean.setStringProperty(nonNullValue);

        json = mapper2.writeValueAsString(bean);
        assertEquals("{\"stringProperty\":\"" + nonNullValue + "\"}", json);

        // Test deserialization with custom inclusion filter and non-null value
        TestBean deserBean4 = mapper2.readValue(json, TestBean.class);
        assertEquals(nonNullValue, deserBean4.getStringProperty());
    }

    /**
     * Test for handling of {@link JsonInclude.Include#ALWAYS} inclusion type.
     */
    @Test
    public void testPropertyBuilderAlwaysInclusion() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonInclude.Value globalInclude = JsonInclude.Value.includeAnnotations();
        SerializationConfig config = mapper.getSerializationConfig();
        config.setDefaultPropertyInclusion(globalInclude);

        BeanDescription beanDesc = mapper.constructType(TestBean.class);
        PropertyBuilder builder = new PropertyBuilder(config, beanDesc);

        TestBean bean = new TestBean();

        BeanPropertyWriter writer = builder.buildWriter(null, null, TestBean.class, null, null, null, null, false);

        // Test serialization with ALWAYS inclusion type and null value
        ObjectMapper mapper2 = new ObjectMapper();
        mapper2.setSerializationConfig(builder.getSerializationConfig());
        JsonInclude.Value alwaysInclude = JsonInclude.Value.alwaysInclude("*");
        config.setDefaultPropertyInclusion(alwaysInclude);

        String json = mapper2.writeValueAsString(bean);
        assertEquals("{}", json);

        // Test deserialization with ALWAYS inclusion type and null value
        TestBean deserBean = mapper2.readValue(json, TestBean.class);
        assertNull(deserBean.getStringProperty());

        // Test serialization with ALWAYS inclusion type and non-null value
        String nonNullValue = "nonNull";
        bean.setStringProperty(nonNullValue);

        json = mapper2.writeValueAsString(bean);
        assertEquals("{\"stringProperty\":\"" + nonNullValue + "\"}", json);

        // Test deserialization with ALWAYS inclusion type and non-null value
        TestBean deserBean5 = mapper2.readValue(json, TestBean.class);
        assertEquals(nonNullValue, deserBean5.getStringProperty());
    }

    static class TestBean {
        private String stringProperty;

        public String getStringProperty() {
            return stringProperty;
        }

        public void setStringProperty(String value) {
            this.stringProperty = value;
        }
    }
}