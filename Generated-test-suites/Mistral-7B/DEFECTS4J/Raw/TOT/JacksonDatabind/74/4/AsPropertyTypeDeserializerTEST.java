import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AsPropertyTypeDeserializerTest {

    @Test
    public void testDeserializeTypedObjectWithProperty() throws IOException {
        String json = "{\"typePropertyName\":\"String\",\"value\":\"test\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(Map.class, String.class, String.class);
        Object readValue = objectMapper.readValue(json, new TypeReference<>() {});
        Assert.assertTrue(readValue instanceof Map);
        Map map = (Map) readValue;
        Assert.assertEquals("String", map.get("typePropertyName"));
        Assert.assertEquals("test", map.get("value"));

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object deserializedObject = new AsPropertyTypeDeserializer(null, null, "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(jsonParser, new TestContext());
        Assert.assertTrue(deserializedObject instanceof Map);
        Map deserializedMap = (Map) deserializedObject;
        Assert.assertEquals("String", deserializedMap.get("typePropertyName"));
        Assert.assertEquals("test", deserializedMap.get("value"));
    }

    @Test
    public void testDeserializeTypedObjectWithMissingProperty() throws IOException {
        String json = "{\"value\":\"test\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        Object deserializedObject = objectMapper.readValue(json, Map.class);
        Assert.assertNull(deserializedObject);

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = new AsPropertyTypeDeserializer(null, null, "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(jsonParser, new TestContext());
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeTypedObjectWithWrongPropertyName() throws IOException {
        String json = "{\"incorrectTypePropertyName\":\"String\",\"value\":\"test\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        Object deserializedObject = objectMapper.readValue(json, Map.class);
        Assert.assertNull(deserializedObject);

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = new AsPropertyTypeDeserializer(null, null, "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(jsonParser, new TestContext());
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeTypedObjectWithNullValue() throws IOException {
        String json = "{\"typePropertyName\":\"String\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        Object deserializedObject = objectMapper.readValue(json, Map.class);
        Assert.assertTrue(deserializedObject instanceof Map);
        Map map = (Map) deserializedObject;
        Assert.assertEquals("String", map.get("typePropertyName"));
        Assert.assertNull(map.get("value"));

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = new AsPropertyTypeDeserializer(null, null, "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(jsonParser, new TestContext());
        Assert.assertTrue(result instanceof Map);
        Map deserializedMap = (Map) result;
        Assert.assertEquals("String", deserializedMap.get("typePropertyName"));
        Assert.assertNull(deserializedMap.get("value"));
    }

    @Test
    public void testDeserializeTypedObjectWithTypeIdVisible() throws IOException {
        String json = "{\"typePropertyName\":\"String\",\"@type\":\"java.lang.String\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        Object deserializedObject = objectMapper.readValue(json, Map.class);
        Assert.assertTrue(deserializedObject instanceof Map);
        Map map = (Map) deserializedObject;
        Assert.assertEquals("String", map.get("typePropertyName"));
        Assert.assertEquals("java.lang.String", map.get("@type"));
        Assert.assertNull(map.get("value"));

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = new AsPropertyTypeDeserializer(null, null, "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(jsonParser, new TestContext());
        Assert.assertTrue(result instanceof Map);
        Map deserializedMap = (Map) result;
        Assert.assertEquals("String", deserializedMap.get("typePropertyName"));
        Assert.assertEquals("java.lang.String", deserializedMap.get("@type"));
        Assert.assertNull(deserializedMap.get("value"));
    }

    @Test
    public void testDeserializeTypedObjectWithNativeTypeId() throws IOException {
        String json = "{\"typePropertyName\":\"java.util.List\",\"@type\":\"[java.lang.String]\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, String.class);
        Object deserializedObject = objectMapper.readValue(json, javaType);
        Assert.assertTrue(deserializedObject instanceof List);
        List list = (List) deserializedObject;
        Assert.assertEquals(0, list.size());

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = new AsPropertyTypeDeserializer(null, null, "typePropertyName", true, null, As.PROPERTY).deserializeTypedFromObject(jsonParser, new TestContext());
        Assert.assertTrue(result instanceof List);
        List deserializedList = (List) result;
        Assert.assertEquals(0, deserializedList.size());
    }

    @Test
    public void testDeserializeTypedObjectWithDefaultImpl() throws IOException {
        String json = "[1,2,3]";
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
        Object deserializedObject = objectMapper.readValue(json, javaType);
        Assert.assertTrue(deserializedObject instanceof List);
        List list = (List) deserializedObject;
        Assert.assertEquals(3, list.size());
        Assert.assertEquals(1, list.get(0));
        Assert.assertEquals(2, list.get(1));
        Assert.assertEquals(3, list.get(2));

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = new AsPropertyTypeDeserializer(null, null, null, false, null, As.PROPERTY).deserializeTypedFromObject(jsonParser, new TestContext());
        Assert.assertTrue(result instanceof List);
        List deserializedList = (List) result;
        Assert.assertEquals(3, deserializedList.size());
        Assert.assertEquals(1, deserializedList.get(0));
        Assert.assertEquals(2, deserializedList.get(1));
        Assert.assertEquals(3, deserializedList.get(2));
    }

    @Test
    public void testDeserializeTypedObjectWithInvalidTypeId() throws IOException {
        String json = "{\"typePropertyName\":\"java.util.List\",\"@type\":\"[java.lang.String]\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule().addDeserializer(javaType, new TestDeserializer()));
        Object deserializedObject = mapper.readValue(json, javaType);
        Assert.assertNotNull(deserializedObject);

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = new AsPropertyTypeDeserializer(null, null, null, false, null, As.PROPERTY).deserializeTypedFromObject(jsonParser, new TestContext());
        Assert.assertNotNull(result);
    }

    @Test
    public void testDeserializeTypedObjectWithMissingTypeId() throws IOException {
        String json = "{\"typePropertyName\":\"java.util.List\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = new TypeReference<List<Integer>>(){}.getType();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule().addDeserializer(javaType, new TestDeserializer()));
        Object deserializedObject = mapper.readValue(json, javaType);
        Assert.assertNull(deserializedObject);

        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        Object result = new AsPropertyTypeDeserializer(null, null, null, false, null, As.PROPERTY).deserializeTypedFromObject(jsonParser, new TestContext());
        Assert.assertNull(result);
    }

    private static class TestDeserializer extends StdDeserializer<List<Integer>> {
        @Override
        public List<Integer> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new ArrayList<>();
        }
    }

    private static class TestContext extends DeserializationContext {
        // Empty implementation
    }
}