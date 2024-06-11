package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.TestUtils;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.node.*;
import org.testng.Assert;
import org.testng.annotations.*;

public class ExternalTypeHandlerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ExternalTypeHandler.Builder BUILDER = new ExternalTypeHandler.Builder();

    @BeforeClass
    public static void setupClass() {
        // Define a simple POJO for testing
        static class TestPojo {
            private String name;
            private int id;
            private String typeId;

            public String getTypeId() {
                return typeId;
            }

            public void setTypeId(String typeId) {
                this.typeId = typeId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }
        }

        // Define a custom type deserializer for the TestPojo
        static class TestPojoTypeDeserializer extends StdDeserializer<TestPojo> {
            @Override
            public TestPojo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                TestPojo pojo = new TestPojo();
                pojo.setName(ctxt.readValueAs(String.class, p));
                String typeId = p.getText();
                pojo.setTypeId(typeId);
                return pojo;
            }

            @Override
            public BeanDescription getBeanDescription() {
                return new BeanDescription(TestPojo.class, null);
            }

            @Override
            public String getPropertyName() {
                return "typeId";
            }
        }

        // Add the TestPojo to the builder and register the custom deserializer
        BUILDER.addExternal(TestPojo.class.getDeclaredField("name"), null);
        BUILDER.addExternal(TestPojo.class.getDeclaredField("typeId"), new TestPojoTypeDeserializer());
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String json = "{\"name\":\"test\",\"typeId\":\"TestType\"}";
        TestPojo pojo = MAPPER.readValue(json, TestPojo.class, new ExternalTypeHandler());
        Assert.assertEquals(pojo.getName(), "test");
        Assert.assertEquals(pojo.getTypeId(), "TestType");
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        String json = "{\"name\":\"test\",\"testType\":\"TestType\"}";
        TestPojo pojo = MAPPER.readValue(json, TestPojo.class, new ExternalTypeHandler());
        Assert.assertEquals(pojo.getName(), "test");
        Assert.assertNull(pojo.getTypeId());

        // Test with missing property
        json = "{\"name\":\"test\"}";
        TestPojo pojoMissingProperty = MAPPER.readValue(json, TestPojo.class, new ExternalTypeHandler());
        Assert.assertEquals(pojoMissingProperty.getName(), "test");
        Assert.assertNull(pojoMissingProperty.getTypeId());
    }

    @Test
    public void testComplete() throws IOException {
        String json = "{\"name\":\"test\",\"typeId\":\"TestType\"}";
        TestPojo pojo = MAPPER.readValue(json, TestPojo.class, new ExternalTypeHandler());
        Assert.assertEquals(pojo.getName(), "test");
        Assert.assertEquals(pojo.getTypeId(), "TestType");

        // Test with missing typeId
        String jsonMissingTypeId = "{\"name\":\"test\"}";
        TestPojo pojoMissingTypeId = MAPPER.readValue(jsonMissingTypeId, TestPojo.class, new ExternalTypeHandler());
        Assert.assertEquals(pojoMissingTypeId.getName(), "test");
        Assert.assertNull(pojoMissingTypeId.getTypeId());

        // Test with nested objects
        String jsonNested = "{" +
                "  \"name\":\"test\"," +
                "  \"testType\":{" +
                "    \"id\":1," +
                "    \"typeId\":\"TestNestedType\"" +
                "  }" +
                "}";
        TestPojo pojoNested = MAPPER.readValue(jsonNested, TestPojo.class, new ExternalTypeHandler());
        Assert.assertEquals(pojoNested.getName(), "test");
        Assert.assertEquals(pojoNested.getTypeId(), "TestNestedType");

        // Test with null values
        String jsonNull = "{" +
                "  \"name\":null," +
                "  \"testType\":{" +
                "    \"id\":null," +
                "    \"typeId\":null" +
                "  }" +
                "}";
        TestPojo pojoNull = MAPPER.readValue(jsonNull, TestPojo.class, new ExternalTypeHandler());
        Assert.assertNull(pojoNull.getName());
        Assert.assertNull(pojoNull.getTypeId());

        // Test with missing property
        String jsonMissingPropertyNull = "{" +
                "  \"name\":null" +
                "}";
        TestPojo pojoMissingPropertyNull = MAPPER.readValue(jsonMissingPropertyNull, TestPojo.class, new ExternalTypeHandler());
        Assert.assertNull(pojoMissingPropertyNull.getName());
        Assert.assertNull(pojoMissingPropertyNull.getTypeId());

        // Test with missing typeId and property
        String jsonMissingBoth = "{" +
                "  \"name\":null" +
                "}";
        TestPojo pojoMissingBoth = MAPPER.readValue(jsonMissingBoth, TestPojo.class, new ExternalTypeHandler());
        Assert.assertNull(pojoMissingBoth.getName());
        Assert.assertNull(pojoMissingBoth.getTypeId());
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        String jsonWithCreator = "{" +
                "  \"name\":\"test\"," +
                "  \"testType\":{" +
                "    \"id\":1," +
                "    \"typeId\":\"TestCreatorType\"" +
                "  }," +
                "  \"creator\":{" +
                "    \"id\":\"CreatorId\"," +
                "    \"typeId\":\"TestCreatorType\"" +
                "  }" +
                "}";

        TestPojo pojo = MAPPER.readValue(jsonWithCreator, TestPojo.class, new ExternalTypeHandler());
        Assert.assertEquals(pojo.getName(), "test");
        Assert.assertEquals(pojo.getTypeId(), "TestCreatorType");
        Assert.assertNotNull(pojo.getId());
        Assert.assertEquals(pojo.getId(), 1);

        String jsonWithCreatorNull = "{" +
                "  \"name\":\"test\"," +
                "  \"testType\":{" +
                "    \"id\":1," +
                "    \"typeId\":null" +
                "  }," +
                "  \"creator\":{" +
                "    \"id\":\"CreatorId\"" +
                "  }" +
                "}";

        TestPojo pojoNull = MAPPER.readValue(jsonWithCreatorNull, TestPojo.class, new ExternalTypeHandler());
        Assert.assertEquals(pojoNull.getName(), "test");
        Assert.assertNull(pojoNull.getTypeId());
        Assert.assertNull(pojoNull.getId());

        String jsonWithCreatorMissing = "{" +
                "  \"name\":\"test\"" +
                "}";

        TestPojo pojoMissing = MAPPER.readValue(jsonWithCreatorMissing, TestPojo.class, new ExternalTypeHandler());
        Assert.assertEquals(pojoMissing.getName(), "test");
        Assert.assertNull(pojoMissing.getTypeId());
        Assert.assertNull(pojoMissing.getId());
    }

    @Test
    public void testCompleteWithNullJson() throws IOException {
        String jsonNull = "null";
        TestPojo pojoNull = MAPPER.readValue(jsonNull, TestPojo.class, new ExternalTypeHandler());
        Assert.assertNull(pojoNull.getName());
        Assert.assertNull(pojoNull.getTypeId());
        Assert.assertNull(pojoNull.getId());
    }

    @Test
    public void testCompleteWithEmptyJson() throws IOException {
        String jsonEmpty = "{}";
        TestPojo pojoEmpty = MAPPER.readValue(jsonEmpty, TestPojo.class, new ExternalTypeHandler());
        Assert.assertNull(pojoEmpty.getName());
        Assert.assertNull(pojoEmpty.getTypeId());
        Assert.assertNull(pojoEmpty.getId());
    }

    @Test
    public void testCompleteWithInvalidJson() throws IOException {
        String jsonInvalid = "{invalid:json}";
        Assert.expectThrows(JsonProcessingException.class, () -> MAPPER.readValue(jsonInvalid, TestPojo.class, new ExternalTypeHandler()));
    }

    @AfterTest
    public void tearDown() {
        MAPPER.close();
    }
}