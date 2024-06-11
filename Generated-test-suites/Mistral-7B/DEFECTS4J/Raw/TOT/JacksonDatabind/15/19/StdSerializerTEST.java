import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

public class StdSerializerTest {

    @Test
    public void testDefaultSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // Test serializing a simple JavaBean using the default serializer
        JavaBean javaBean = new JavaBean("Test", 1);
        String json = mapper.writeValueAsString(javaBean);
        ObjectNode jsonNode = (ObjectNode) mapper.readTree(json);
        Assert.assertEquals("{\"name\":\"Test\",\"age\":1}", jsonNode.toString());

        // Test deserializing the JSON back to the JavaBean
        JavaBean deserializedJavaBean = mapper.readValue(jsonNode, JavaBean.class);
        Assert.assertEquals("Test", deserializedJavaBean.getName());
        Assert.assertEquals(1, deserializedJavaBean.getAge());
    }

    @Test
    public void testCustomSerializer() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StdSerializer<CustomClass> customSerializer = new StdSerializer<CustomClass>(CustomClass.class);
        SimpleModule module = new SimpleModule();
        module.addSerializer(CustomClass.class, customSerializer);
        mapper.registerModule(module);

        // Test serializing a custom JavaBean using the custom serializer
        CustomClass customJavaBean = new CustomClass("TestCustom", 2);
        String json = mapper.writeValueAsString(customJavaBean);
        ObjectNode jsonNode = (ObjectNode) mapper.readTree(json);
        Assert.assertEquals("{\"name\":\"TestCustom\",\"customField\":\"CustomValue\"}", jsonNode.toString());

        // Test deserializing the JSON back to the CustomClass
        CustomClass deserializedCustomJavaBean = mapper.readValue(jsonNode, CustomClass.class);
        Assert.assertEquals("TestCustom", deserializedCustomJavaBean.getName());
        Assert.assertEquals("CustomValue", deserializedCustomJavaBean.getCustomField());
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingSchema() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // Test missing schema throws JsonMappingException
        Object jsonNode = mapper.readTree("{}");
        StdSerializer<MissingSchema> missingSchemaSerializer = new StdSerializer<MissingSchema>(MissingSchema.class);
        JsonNode result = mapper.treeToValue(jsonNode, missingSchemaSerializer);
        Assert.fail(); // Test should have thrown JsonMappingException
    }

    @Test
    public void testSchema() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        // Test schema generation
        StdSerializer<SchemaTest> schemaTestSerializer = new StdSerializer<SchemaTest>(SchemaTest.class);
        JsonSchema schema = schemaTestSerializer.getSchema(mapper.getSerializerProvider(), SchemaTest.class);
        Assert.assertEquals("{\"type\":\"object\",\"properties\":{\"name\":{\"type\":\"string\"}},\"required\":[\"name\"]}", schema.toString());
    }

    @Test
    public void testFilter() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        FilterProvider filters = mapper.getFilterProvider();
        PropertyFilter propertyFilter = filters.findPropertyFilter("testFilter", new Object());

        // Test filtering an empty list
        ObjectNode emptyListNode = mapper.createObjectNode();
        StdSerializer<FilterTest> filterTestSerializer = new StdSerializer<FilterTest>(FilterTest.class);
        JsonNode result = mapper.treeToValue(emptyListNode, filterTestSerializer);
        Assert.assertEquals(filterTestSerializer.createSchemaNode("null"), result);

        // Test filtering a list with a filtered value
        ObjectNode listNodeWithFilteredValue = mapper.createObjectNode();
        listNodeWithFilteredValue.put("filteredValue", "filteredValue");
        JsonNode resultWithFilteredValue = mapper.treeToValue(listNodeWithFilteredValue, filterTestSerializer);
        Assert.assertEquals(filterTestSerializer.createSchemaNode("null"), resultWithFilteredValue);

        // Test filtering a list without a filtered value
        ObjectNode listNodeWithoutFilteredValue = mapper.createObjectNode();
        JsonNode resultWithoutFilteredValue = mapper.treeToValue(listNodeWithoutFilteredValue, filterTestSerializer);
        Assert.assertEquals(filterTestSerializer.createSchemaNode("array"), resultWithoutFilteredValue);
    }
}