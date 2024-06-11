package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.node.POJONode;

public class POJONodeTest {

    @Test
    public void testPOJONodeWithStringValue() {
        ObjectMapper objectMapper = new ObjectMapper();
        String value = "test string";
        POJONode pojoNode = new POJONode(value);

        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
        assertEquals("test string", pojoNode.asText());
        assertEquals("test string", pojoNode.asText(null));
        assertEquals(true, pojoNode.asBoolean(false));
        assertEquals(4, pojoNode.asInt(0));
        assertEquals(4L, pojoNode.asLong(0L));
        assertEquals(4.0, pojoNode.asDouble(0.0), 0.0);

        JsonGenerator gen = null;
        try {
            gen = objectMapper.getFactory().createGenerator(System.out, null);
            pojoNode.serialize(gen, new SerializerProvider());
            gen.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (gen != null) {
                    gen.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonOutput = System.out.toString();
        assertEquals("\"test string\"", jsonOutput.trim());
    }

    @Test
    public void testPOJONodeWithBooleanValue() {
        ObjectMapper objectMapper = new ObjectMapper();
        Boolean value = true;
        POJONode pojoNode = new POJONode(value);

        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
        assertEquals("true", pojoNode.asText());
        assertEquals("true", pojoNode.asText(null));
        assertEquals(true, pojoNode.asBoolean(false));
        assertEquals(0, pojoNode.asInt(0));
        assertEquals(0L, pojoNode.asLong(0L));
        assertEquals(1.0, pojoNode.asDouble(0.0), 0.0);

        JsonGenerator gen = null;
        try {
            gen = objectMapper.getFactory().createGenerator(System.out, null);
            pojoNode.serialize(gen, new SerializerProvider());
            gen.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (gen != null) {
                    gen.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonOutput = System.out.toString();
        assertEquals("true", jsonOutput.trim());
    }

    @Test
    public void testPOJONodeWithIntegerValue() {
        ObjectMapper objectMapper = new ObjectMapper();
        Integer value = 123;
        POJONode pojoNode = new POJONode(value);

        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
        assertEquals("123", pojoNode.asText());
        assertEquals("123", pojoNode.asText(null));
        assertEquals(true, pojoNode.asBoolean(false));
        assertEquals(123, pojoNode.asInt(0));
        assertEquals(123L, pojoNode.asLong(0L));
        assertEquals(123.0, pojoNode.asDouble(0.0), 0.0);

        JsonGenerator gen = null;
        try {
            gen = objectMapper.getFactory().createGenerator(System.out, null);
            pojoNode.serialize(gen, new SerializerProvider());
            gen.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (gen != null) {
                    gen.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonOutput = System.out.toString();
        assertEquals("123", jsonOutput.trim());
    }

    @Test
    public void testPOJONodeWithLongValue() {
        ObjectMapper objectMapper = new ObjectMapper();
        Long value = 456L;
        POJONode pojoNode = new POJONode(value);

        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
        assertEquals("456", pojoNode.asText());
        assertEquals("456", pojoNode.asText(null));
        assertEquals(true, pojoNode.asBoolean(false));
        assertEquals(456, pojoNode.asInt(0));
        assertEquals(456L, pojoNode.asLong(0L));
        assertEquals(456.0, pojoNode.asDouble(0.0), 0.0);

        JsonGenerator gen = null;
        try {
            gen = objectMapper.getFactory().createGenerator(System.out, null);
            pojoNode.serialize(gen, new SerializerProvider());
            gen.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (gen != null) {
                    gen.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonOutput = System.out.toString();
        assertEquals("456", jsonOutput.trim());
    }

    @Test
    public void testPOJONodeWithDoubleValue() {
        ObjectMapper objectMapper = new ObjectMapper();
        Double value = 789.123;
        POJONode pojoNode = new POJONode(value);

        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
        assertEquals("789.123", pojoNode.asText());
        assertEquals("789.123", pojoNode.asText(null));
        assertEquals(true, pojoNode.asBoolean(false));
        assertEquals(0, pojoNode.asInt(0));
        assertEquals(0L, pojoNode.asLong(0L));
        assertEquals(789.123, pojoNode.asDouble(0.0), 0.001);

        JsonGenerator gen = null;
        try {
            gen = objectMapper.getFactory().createGenerator(System.out, null);
            pojoNode.serialize(gen, new SerializerProvider());
            gen.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (gen != null) {
                    gen.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonOutput = System.out.toString();
        assertEquals("789.123", jsonOutput.trim());
    }

    @Test
    public void testPOJONodeWithJsonSerializableValue() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonSerializable jsonSerializableValue = new MyJsonSerializable();
        POJONode pojoNode = new POJONode(jsonSerializableValue);

        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());

        JsonGenerator gen = null;
        try {
            gen = objectMapper.getFactory().createGenerator(System.out, null);
            pojoNode.serialize(gen, new SerializerProvider());
            gen.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (gen != null) {
                    gen.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonOutput = System.out.toString();
        MyJsonSerializable deserializedValue = null;
        try {
            deserializedValue = objectMapper.readValue(new StringReader(jsonOutput), MyJsonSerializable.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(jsonSerializableValue, deserializedValue);
    }

    @Test
    public void testPOJONodeWithNullValue() {
        ObjectMapper objectMapper = new ObjectMapper();
        POJONode pojoNode = new POJONode(null);

        assertEquals(JsonNodeType.POJO, pojoNode.getNodeType());
        assertEquals("null", pojoNode.asText());
        assertEquals("null", pojoNode.asText(null));
        assertEquals(false, pojoNode.asBoolean(true));
        assertEquals(0, pojoNode.asInt(0));
        assertEquals(0L, pojoNode.asLong(0L));
        assertEquals(0.0, pojoNode.asDouble(0.0), 0.0);

        JsonGenerator gen = null;
        try {
            gen = objectMapper.getFactory().createGenerator(System.out, null);
            pojoNode.serialize(gen, new SerializerProvider());
            gen.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (gen != null) {
                    gen.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonOutput = System.out.toString();
        assertEquals("null", jsonOutput.trim());
    }

    @Test
    public void testPOJONodeEquals() {
        ObjectMapper objectMapper = new ObjectMapper();
        String value1 = "test string 1";
        String value2 = "test string 2";
        POJONode pojoNode1 = new POJONode(value1);
        POJONode pojoNode2 = new POJONode(value2);

        assertFalse(pojoNode1.equals(pojoNode2));
        assertFalse(pojoNode2.equals(pojoNode1));
    }

    @Test
    public void testPOJONodeHashCode() {
        ObjectMapper objectMapper = new ObjectMapper();
        String value = "test string";
        POJONode pojoNode1 = new POJONode(value);
        POJONode pojoNode2 = new POJONode(value);

        assertEquals(pojoNode1.hashCode(), pojoNode2.hashCode());
    }
}

class MyJsonSerializable implements JsonSerializable {
    @Override
    public void serialize(JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString("MyJsonSerializable");
    }
}