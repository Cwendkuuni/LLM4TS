package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.test.BaseMapTest;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CreatorPropertyTest extends BaseMapTest {

    static class TestBean {
        @JsonCreator
        public TestBean(@JsonProperty("name") String name) {
            this.name = name;
        }

        @JsonProperty
        private String name;

        CreatorProperty creatorProperty;

        public CreatorProperty getCreatorProperty() {
            return creatorProperty;
        }

        public void setCreatorProperty(CreatorProperty creatorProperty) {
            this.creatorProperty = creatorProperty;
        }
    }

    @Test
    public void testNormalOperation() throws Exception {
        String json = "{\"name\":\"John Doe\"}";
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertNotNull(bean);
        assertNotNull(bean.creatorProperty);
        assertEquals(bean.name, "John Doe");
    }

    @Test
    public void testInjectableValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        TestBean bean = mapper.readValue(getComplexJson(), TestBean.class);
        assertNotNull(bean);
        assertNotNull(bean.creatorProperty);
        assertEquals(bean.name, "Test Name");
        assertEquals(bean.getCreatorProperty().getInjectableValueId(), "TestId");

        // Test injection via findInjectableValue
        DeserializationContext ctx = mapper.getDeserializationContext();
        Object injectedValue = bean.creatorProperty.findInjectableValue(ctx, bean);
        assertNotNull(injectedValue);
        assertEquals(injectedValue.getClass(), String.class);
        assertEquals(injectedValue, "Injected Value");

        // Test injection via inject method
        bean.creatorProperty.inject(ctx, bean);
        assertNotNull(bean.creatorProperty.getInjectableValueId());
        assertEquals(bean.name, "Test Name");
        assertEquals(bean.getCreatorProperty().getInjectableValueId(), "Injected Value");
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void testMissingSetter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "{\"name\":\"John Doe\", \"creatorProperty\":{}}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertNotNull(bean);
        assertNull(bean.creatorProperty);

        // Should throw exception since there is no setter
        bean.creatorProperty = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
    }

    @Test
    public void testMissingInjectableValueId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String json = "{\"name\":\"John Doe\", \"creatorProperty\":{}}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertNotNull(bean);
        assertNull(bean.creatorProperty);

        DeserializationContext ctx = mapper.getDeserializationContext();
        Object injectedValue = bean.creatorProperty.findInjectableValue(ctx, bean);
        assertNull(injectedValue);
    }

    @Test
    public void testTypeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.registerTypeDeserializer(TypeX.class, new TypeXDeserializer());

        String json = "{\"name\":\"John Doe\", \"creatorProperty\":{}}";
        TestBean bean = mapper.readValue(json, TestBean.class);
        assertNotNull(bean);
        assertNull(bean.creatorProperty);

        TypeDeserializer typeDeserializer = bean.creatorProperty.getTypeDeser();
        assertNotNull(typeDeserializer);
        assertEquals(typeDeserializer.getClass(), TypeXDeserializer.class);
    }

    static class TypeXDeserializer extends StdTypeDeserializer<TypeX> {
        public TypeXDeserializer() { super(TypeX.class); }
        @Override
        public TypeX deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new TypeX();
        }
    }

    static class TypeX {
        @Override
        public String toString() { return "TypeX"; }
    }

    static String getComplexJson() {
        return "{\"name\":\"Test Name\", \"creatorProperty\":{" +
                "\"@com.fasterxml.jackson.annotation.JsonIgnore\": true," +
                "\"@com.fasterxml.jackson.annotation.JsonProperty\": {\"value\": \"TestId\"}," +
                "\"@com.fasterxml.jackson.annotation.JsonCreator\": {" +
                "\"value\": [{\"name\": \"TestId\", \"value\": \"TestId\"}]}}";
    }
}