package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

public class CreatorPropertyTest {

    static class TestBean {
        @JsonProperty("id")
        private int id;
        @JsonProperty("name")
        private String name;

        @JsonCreator
        public TestBean(@JsonProperty("id") int id, @JsonProperty("name") String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Test
    public void testCreatorPropertyDefault() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(TestBean.class);
        ObjectReader reader = mapper.readerFor(javaType);

        CreatorProperty property = reader.findProperty("id");
        assertNull(property._injectableValueId);
        assertNull(property._fallbackSetter);

        String json = "{\"id\":1}";
        TestBean bean = reader.readValue(json);
        assertEquals(1, bean.id);
    }

    @Test
    public void testCreatorPropertyInject() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(TestBean.class);
        ObjectReader reader = mapper.readerFor(javaType);

        CreatorProperty property = reader.findProperty("name");
        String injectId = "injectId";
        property.setInjectableValueId(injectId);

        JsonTestData testData = new JsonTestData();
        testData.add(new JsonTestData.TestEntry("name", "TestName"));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ObjectWriter writer = mapper.writerFor(javaType);
        String json = writer.writeValueAsString(new TestBean(1, "TestName"));

        ObjectReader objectReader = mapper.readerFor(javaType);
        TestBean deserialized = objectReader.readValue(json);
        assertEquals("TestName", deserialized.name);

        TestBean injectedBean = objectReader.readValue(json);
        assertNotNull(injectedBean.name);
        assertEquals("TestName", injectedBean.name);

        Object injectValue = mapper.readerForValueType(String.class).readValue(mapper.readTree("{\"id\":1, \"name\":\"TestName\"}"));
        TestBean injectedDeserialized = mapper.readerFor(javaType).readValue(mapper.readTree("{\"id\":1, \"name\":\"" + injectValue + "\"}"));
        assertEquals("TestName", injectedDeserialized.name);
    }

    @Test
    public void testCreatorPropertyInjectError() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(TestBean.class);
        ObjectReader reader = mapper.readerFor(javaType);

        CreatorProperty property = reader.findProperty("name");
        String injectId = "injectId";
        property.setInjectableValueId(injectId);

        JsonTestData testData = new JsonTestData();
        testData.add(new JsonTestData.TestEntry("id", 1));
        testData.add(new JsonTestData.TestEntry("name", null));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ObjectWriter writer = mapper.writerFor(javaType);
        String json = writer.writeValueAsString(new TestBean(1, "TestName"));

        try {
            ObjectReader objectReader = mapper.readerFor(javaType);
            objectReader.readValue(json);
            fail("Exception expected");
        } catch (JsonMappingException e) {
            assertEquals("Invalid definition for property 'name' of type [simple type, class com.fasterxml.jackson.databind.deser.CreatorPropertyTest$TestBean]: injected value id 'injectId' not found", e.getMessage());
        }
    }

    @Test
    public void testCreatorPropertyFallback() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(TestBean.class);
        ObjectReader reader = mapper.readerFor(javaType);

        CreatorProperty property = reader.findProperty("name");
        String fallbackSetterName = "setName";
        property.setFallbackSetter(reader.findProperty(fallbackSetterName));

        TestBean bean = new TestBean(1, "TestName");
        String json = mapper.writeValueAsString(bean);

        ObjectReader objectReader = mapper.readerFor(javaType);
        TestBean deserialized = objectReader.readValue(json);
        assertEquals("TestName", deserialized.name);

        TestBean fallbackDeserialized = new TestBean();
        ObjectReader fallbackReader = mapper.readerFor(TestBean.class);
        fallbackReader.findAndBind(fallbackSetterName, fallbackDeserialized);
        ObjectReader objectReader2 = mapper.readerFor(javaType);
        objectReader2.readValue(json, fallbackDeserialized);
        assertEquals("TestName", fallbackDeserialized.name);
    }

    @Test
    public void testCreatorPropertyFallbackError() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(TestBean.class);
        ObjectReader reader = mapper.readerFor(javaType);

        CreatorProperty property = reader.findProperty("name");
        String fallbackSetterName = "setName";
        property.setFallbackSetter(reader.findProperty(fallbackSetterName));

        TestBean bean = new TestBean(1, "TestName");
        String json = mapper.writeValueAsString(bean);

        TestBean fallbackDeserialized = new TestBean();
        ObjectReader fallbackReader = mapper.readerFor(TestBean.class);
        fallbackReader.findAndBind(fallbackSetterName, fallbackDeserialized);

        ObjectReader objectReader = mapper.readerFor(javaType);
        try {
            objectReader.readValue(json, fallbackDeserialized);
            fail("Exception expected");
        } catch (JsonMappingException e) {
            assertEquals("No fallback setter/field defined for creator property 'name'", e.getMessage());
        }
    }

    @Test
    public void testCreatorPropertyTypeDeserializer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(TestBean.class);
        ObjectReader reader = mapper.readerFor(javaType);

        CreatorProperty property = reader.findProperty("id");
        TypeDeserializer typeDeserializer = new FromStringDeserializer();
        property.setTypeDeserializer(typeDeserializer);

        String json = "{\"id\":\"123\"}";
        TestBean bean = reader.readValue(json);
        assertEquals(123, bean.id);
    }

    @Test
    public void testCreatorPropertyTypeDeserializerError() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(TestBean.class);
        ObjectReader reader = mapper.readerFor(javaType);

        CreatorProperty property = reader.findProperty("id");
        TypeDeserializer typeDeserializer = new FromStringDeserializer();
        property.setTypeDeserializer(typeDeserializer);

        String json = "{\"id\":\"test\"}";
        try {
            reader.readValue(json);
            fail("Exception expected");
        } catch (JsonProcessingException e) {
            String expectedMessage = "Cannot deserialize value of type [simple type, class java.lang.Integer] from String \"test\": not a valid representation";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}

class JsonTestData {
    private final java.util.List<TestEntry> _entries;

    public JsonTestData() {
        _entries = new ArrayList<>();
    }

    public void add(TestEntry entry) {
        _entries.add(entry);
    }

    public static class TestEntry {
        private final String _name;
        private final Object _value;

        public TestEntry(String name, Object value) {
            _name = name;
            _value = value;
        }

        public String getName() {
            return _name;
        }

        public Object getValue() {
            return _value;
        }
    }

    public JsonTestData readValue(ObjectMapper mapper, String jsonData) throws IOException {
        ObjectReader reader = mapper.readerForValueType(TypeFactory.defaultInstance().constructType(JsonTestData.class));
        JsonTestData deserialized = reader.readValue(jsonData);
        assertEquals(_entries.size(), deserialized._entries.size());
        for (int i = 0; i < _entries.size(); i++) {
            assertEquals(_entries.get(i).getName(), deserialized._entries.get(i).getName());
            assertEquals(_entries.get(i).getValue(), deserialized._entries.get(i).getValue());
        }
        return deserialized;
    }

    public String writeValueAsString(ObjectMapper mapper) throws IOException {
        ObjectWriter writer = mapper.writerForValueType(TypeFactory.defaultInstance().constructType(JsonTestData.class));
        return writer.writeValueAsString(this);
    }
}