import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeySerializersTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetStdKeySerializer() throws JsonProcessingException {
        Class<?> rawKeyType = String.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), rawKeyType, false);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER.getClass(), serializer.getClass());

        rawKeyType = int.class;
        serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), rawKeyType, false);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

        rawKeyType = Date.class;
        serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), rawKeyType, false);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.TYPE_DATE, serializer.getClass().getField("_typeId").getInt(serializer));

        rawKeyType = Calendar.class;
        serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), rawKeyType, false);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, serializer.getClass().getField("_typeId").getInt(serializer));

        rawKeyType = java.util.UUID.class;
        serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), rawKeyType, false);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, serializer.getClass().getField("_typeId").getInt(serializer));

        rawKeyType = Class.class;
        serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getSerializationConfig(), rawKeyType, false);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.TYPE_CLASS, serializer.getClass().getField("_typeId").getInt(serializer));
    }

    @Test
    public void testGetFallbackKeySerializer() throws JsonProcessingException {
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), String.class);
        assertNull(serializer);

        serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), int.class);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER.getClass(), serializer.getClass());

        serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), Date.class);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.TYPE_DATE, serializer.getClass().getField("_typeId").getInt(serializer));

        serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), Calendar.class);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, serializer.getClass().getField("_typeId").getInt(serializer));

        serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), java.util.UUID.class);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, serializer.getClass().getField("_typeId").getInt(serializer));

        serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getSerializationConfig(), Class.class);
        assertNotNull(serializer);
        assertEquals(StdKeySerializers.Default.TYPE_CLASS, serializer.getClass().getField("_typeId").getInt(serializer));
    }

    @Test
    public void testDefaultKeySerializer() throws JsonProcessingException {
        ObjectNode node = objectMapper.createObjectNode();
        JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        UUID uuid = UUID.randomUUID();
        Class<?> enumType = EnumSet.class;
        String string = "test";

        serializer.serialize(date, new JsonGenerator(node), objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        assertEquals("\"date\"", node.get("0").asText());

        serializer.serialize(calendar, new JsonGenerator(node), objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        assertEquals("\"calendar\"", node.get("1").asText());

        serializer.serialize(uuid, new JsonGenerator(node), objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        assertEquals("\"uuid\"", node.get("2").asText());

        serializer.serialize(enumType, new JsonGenerator(node), objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        assertEquals("\"class java.util.EnumSet\"", node.get("3").asText());

        serializer.serialize(string, new JsonGenerator(node), objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        assertEquals("\"test\"", node.get("4").asText());
    }

    @Test
    public void testDynamicKeySerializer() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        ObjectNode mapNode = objectMapper.createObjectNode();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("key", "value");
        mapNode.put("keys", node);
        rootNode.put("map", mapNode);
        ObjectNode keyNode = objectMapper.createObjectNode();
        keyNode.put("test", new Object());

        JsonSerializer<Object> dynamicSerializer = StdKeySerializers.getDefault();
        PropertySerializerMap dynamicMap = new PropertySerializerMap();
        JsonSerializer<Object> serializer = dynamicSerializer._findAndAddDynamic(dynamicMap, Object.class.getTypeName().toString(), objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        serializer.serialize(mapNode, new JsonGenerator(rootNode), objectMapper.getSerializationConfig().getDefaultSerializerProvider());

        assertEquals(instanceOf(StdKeySerializers.Dynamic.class), serializer);
        assertEquals("\"map\"", rootNode.get("map").asText());
        assertEquals("{ \"keys\" : { \"0\" : { \"@class\" : \"com.fasterxml.jackson.core.JsonNode\", \"@type\":\"NODE\", \"key\":\"keys\" } } }", rootNode.toString());

        ObjectNode mapNode2 = objectMapper.createObjectNode();
        mapNode2.put("keys", keyNode);
        ObjectNode node2 = objectMapper.createObjectNode();
        node2.put("test2", new Object());
        keyNode.set("test2", node2);
        JsonSerializer<Object> serializer2 = dynamicSerializer._findAndAddDynamic(dynamicMap, Object.class.getTypeName().toString(), objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        serializer2.serialize(mapNode2, new JsonGenerator(rootNode), objectMapper.getSerializationConfig().getDefaultSerializerProvider());

        assertEquals(instanceOf(StdKeySerializers.Dynamic.class), serializer2);
        assertEquals("\"map\"", rootNode.get("map").asText());
        assertEquals("{ \"keys\" : { \"0\" : { \"@class\" : \"com.fasterxml.jackson.core.JsonNode\", \"@type\":\"NODE\", \"key\":\"keys\" }, \"1\" : { \"@class\" : \"com.fasterxml.jackson.core.JsonNode\", \"@type\":\"NODE\", \"key\":\"test2\" } } }", rootNode.toString());
    }

    @Test
    public void testStringKeySerializer() throws JsonProcessingException {
        StringKeySerializer stringKeySerializer = new StringKeySerializer();
        ObjectNode node = objectMapper.createObjectNode();
        String key = "test";

        stringKeySerializer.serialize(key, new JsonGenerator(node), objectMapper.getSerializationConfig().getDefaultSerializerProvider());
        assertEquals("\"test\"", node.get("0").asText());
    }
}