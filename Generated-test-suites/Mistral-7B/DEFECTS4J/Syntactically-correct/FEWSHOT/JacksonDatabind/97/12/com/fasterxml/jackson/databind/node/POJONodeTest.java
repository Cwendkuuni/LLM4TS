package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.TestName;

import org.junit.Before;
import org.junit.Test;

public class POJONodeTest extends TestBase {

    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
    }

    // Test for getNodeType() method

    @Test
    @TestName("TestPOJONode_getNodeType")
    public void testGetNodeType() {
        JsonNode node = mapper.readTree("{ 'name': 'John Doe' }");
        JsonNode pojoNode = node.path("name");

        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
    }

    // Test for asText() method

    @Test
    @TestName("TestPOJONode_asText")
    public void testAsText() {
        String jsonString = "{\"name\":\"John Doe\",\"age\":30,\"city\":\"New York\"}";
        JsonNode rootNode = mapper.readTree(jsonString);

        JsonNode pojoNode = rootNode.path("name");
        assertEquals("John Doe", pojoNode.asText());

        pojoNode = rootNode.path("age");
        assertNull(pojoNode.asText());

        pojoNode = rootNode.path("city");
        assertEquals("New York", pojoNode.asText());
    }

    // Test for asBoolean() method

    @Test
    @TestName("TestPOJONode_asBoolean")
    public void testAsBoolean() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{ 'booleanValue': true }");
        JsonNode pojoNode = node.path("booleanValue");

        assertTrue(pojoNode.asBoolean(false));

        node = mapper.readTree("{ 'booleanValue': false }");
        pojoNode = node.path("booleanValue");

        assertFalse(pojoNode.asBoolean(true));
    }

    // Test for asInt() method

    @Test
    @TestName("TestPOJONode_asInt")
    public void testAsInt() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{ 'intValue': 10 }");
        JsonNode pojoNode = node.path("intValue");

        assertEquals(10, pojoNode.asInt(0));

        node = mapper.readTree("{ 'intValue': -5 }");
        pojoNode = node.path("intValue");

        assertEquals(-5, pojoNode.asInt(0));
    }

    // Test for asLong() method

    @Test
    @TestName("TestPOJONode_asLong")
    public void testAsLong() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{ 'longValue': 20L }");
        JsonNode pojoNode = node.path("longValue");

        assertEquals(20L, pojoNode.asLong(0L));

        node = mapper.readTree("{ 'longValue': -15L }");
        pojoNode = node.path("longValue");

        assertEquals(-15L, pojoNode.asLong(0L));
    }

    // Test for asDouble() method

    @Test
    @TestName("TestPOJONode_asDouble")
    public void testAsDouble() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{ 'doubleValue': 3.14 }");
        JsonNode pojoNode = node.path("doubleValue");

        assertEquals(3.14, pojoNode.asDouble(0.0), 0.001);

        node = mapper.readTree("{ 'doubleValue': -1.5 }");
        pojoNode = node.path("doubleValue");

        assertEquals(-1.5, pojoNode.asDouble(0.0), 0.001);
    }

    // Test for serialize() method

    @Test
    @TestName("TestPOJONode_serialize")
    public void testSerialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializable serializableObject = new MySerializableObject();
        JsonNode pojoNode = Json.valueNode(serializableObject);

        JsonFactory factory = mapper.getFactory();
        JsonGenerator gen = factory.createGenerator(new ByteArrayOutputStream(), mapper.getSerializerProvider());

        pojoNode.serialize(gen, mapper.getSerializerProvider());

        String expectedJson = "{\"myField\":\"hello world\"}";
        assertEquals(expectedJson, gen.getOutputStandaloneString());
    }

    // Test for getPojo() method

    @Test
    @TestName("TestPOJONode_getPojo")
    public void testGetPojo() {
        ObjectMapper mapper = new ObjectMapper();
        MySerializableObject obj = new MySerializableObject();
        JsonNode pojoNode = Json.valueNode(obj);

        Object pojo = pojoNode.getPojo();
        assertSame(obj, pojo);
    }

    // Test for hashCode() method

    @Test
    @TestName("TestPOJONode_hashCode")
    public void testHashCode() {
        ObjectMapper mapper = new ObjectMapper();
        MySerializableObject obj1 = new MySerializableObject();
        MySerializableObject obj2 = new MySerializableObject();

        JsonNode pojoNode1 = Json.valueNode(obj1);
        JsonNode pojoNode2 = Json.valueNode(obj2);

        assertEquals(obj1.hashCode(), pojoNode1.hashCode());
        assertEquals(obj2.hashCode(), pojoNode2.hashCode());
        assertNotEquals(obj1.hashCode(), obj2.hashCode());
        assertNotEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
    }

    // Test for equals() method

    @Test
    @TestName("TestPOJONode_equals")
    public void testEquals() {
        ObjectMapper mapper = new ObjectMapper();
        MySerializableObject obj1 = new MySerializableObject();
        MySerializableObject obj2 = new MySerializableObject();

        JsonNode pojoNode1 = Json.valueNode(obj1);
        JsonNode pojoNode2 = Json.valueNode(obj2);

        assertFalse(pojoNode1.equals(pojoNode2));
        assertFalse(pojoNode1.equals("Test String"));
        assertFalse(pojoNode1.equals(null));

        obj2.setValue("Test String");
        assertFalse(pojoNode1.equals(pojoNode2));

        obj1.setValue("Test String");
        assertTrue(pojoNode1.equals(pojoNode2));
    }

    // Test for binaryValue() method

    @Test
    @TestName("TestPOJONode_binaryValue")
    public void testBinaryValue() throws IOException {
        byte[] binaryData = new byte[] { 1, 2, 3 };
        JsonNode pojoNode = Json.valueNode(binaryData);

        byte[] readBinaryData = pojoNode.binaryValue();
        assertArrayEquals(binaryData, readBinaryData);
    }

    static class MySerializableObject implements JsonSerializable {
        private String myField;

        public MySerializableObject() {}

        public MySerializableObject(String myField) {
            this.myField = myField;
        }

        @Override
        public void serialize(JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStringField("myField", myField);
        }

        public String getMyField() {
            return myField;
        }

        public void setMyField(String myField) {
            this.myField = myField;
        }
    }
}