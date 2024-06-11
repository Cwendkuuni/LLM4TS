package com.fasterxml.jackson.databind.node;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectCodec;
import com.fasterxml.jackson.databind.cfg.PackageVersion;

public class TreeTraversingParserTest {

    @Test
    public void testTreeTraversingParser() throws Exception {
        String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\",\"hobbies\":[\"Reading\",\"Swimming\",\"Coding\"]}";

        ObjectCodec objectCodec = new ObjectMapper().getFactory().findValueCodec(JsonNode.class, null);
        TreeTraversingParser parser = new TreeTraversingParser(JsonFactory.instance.readValue(json, JsonNode.class), objectCodec);

        // Test nextToken
        JsonToken token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("name", parser.getCurrentName());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("John", parser.getText());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("age", parser.getCurrentName());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        Assert.assertEquals(30, parser.getIntValue());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("city", parser.getCurrentName());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("New York", parser.getText());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token);
        Assert.assertEquals("hobbies", parser.getCurrentName());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_ARRAY, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("Reading", parser.getText());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("Swimming", parser.getText());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.VALUE_STRING, token);
        Assert.assertEquals("Coding", parser.getText());

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_ARRAY, token);

        token = parser.nextToken();
        Assert.assertEquals(JsonToken.END_OBJECT, token);

        // Test skipChildren
        parser.skipChildren();
        token = parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, token);

        // Test isClosed
        parser.close();
        token = parser.nextToken();
        Assert.assertNull(token);

        // Test getText
        parser.reset();
        JsonToken token1 = parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, token1);
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals("John", parser.getText());

        // Test getTextCharacters
        char[] chars = parser.getTextCharacters();
        Assert.assertArrayEquals("John".toCharArray(), chars);

        // Test getTextLength
        Assert.assertEquals(5, parser.getTextLength());

        // Test getTextOffset
        Assert.assertEquals(0, parser.getTextOffset());

        // Test hasTextCharacters
        Assert.assertFalse(parser.hasTextCharacters());

        // Test getNumberType
        parser.nextToken(); // "name"
        parser.nextToken(); // "John"
        parser.nextToken(); // "age"
        parser.nextToken(); // 30
        Assert.assertEquals(JsonType.NUMBER_INT, parser.getNumberType());

        // Test getBigIntegerValue
        parser.nextToken(); // "city"
        JsonNode node = parser.currentNode();
        BigInteger value = parser.getBigIntegerValue();
        Assert.assertNull(value);
        Assert.assertEquals("New York", node.asText());

        // Test getDecimalValue
        parser.nextToken(); // "hobbies"
        node = parser.currentNode();
        Assert.assertNotNull(node.get("0"));
        value = parser.getDecimalValue();
        Assert.assertNotNull(value);
        Assert.assertEquals(new BigDecimal("Reading"), value);

        // Test getDoubleValue
        parser.nextToken(); // "[Reading, Swimming, Coding]"
        node = parser.currentNode();
        value = parser.getDoubleValue();
        Assert.assertNull(value);
        Assert.assertEquals(3, node.size());

        // Test getFloatValue
        parser.nextToken(); // "Reading"
        value = parser.getFloatValue();
        Assert.assertEquals(0.0f, value, 0.001f);
        Assert.assertEquals("Reading", parser.getText());

        // Test getLongValue
        parser.nextToken(); // "Swimming"
        value = parser.getLongValue();
        Assert.assertEquals(0L, value);
        Assert.assertEquals("Swimming", parser.getText());

        // Test getIntValue
        parser.nextToken(); // "Coding"
        value = parser.getIntValue();
        Assert.assertEquals(3, value);
        Assert.assertEquals("Coding", parser.getText());

        // Test getNumberValue
        parser.nextToken(); // null
        value = parser.getNumberValue();
        Assert.assertNull(value);

        // Test getEmbeddedObject
        parser.nextToken(); // null
        Assert.assertNull(parser.getEmbeddedObject());

        // Test isNaN
        parser.nextToken(); // "name"
        Assert.assertFalse(parser.isNaN());

        // Test getBinaryValue
        parser.nextToken(); // null
        Assert.assertNull(parser.getBinaryValue(Base64Variant.BASE64_STANDARD));

        // Test readBinaryValue
        parser.nextToken(); // "hobbies"
        OutputStream out = new ByteArrayOutputStream();
        int read = parser.readBinaryValue(Base64Variant.BASE64_STANDARD, out);
        byte[] binary = out.toByteArray();
        Assert.assertEquals(0, read);
        Assert.assertArrayEquals(new byte[0], binary);
    }

    @Test(expected = JsonParseException.class)
    public void testTreeTraversingParserFailure() throws Exception {
        String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\",\"hobbies\":[\"Reading\",\"Swimming\",\"Coding\"]}";

        ObjectCodec objectCodec = new ObjectMapper().getFactory().findValueCodec(JsonNode.class, null);
        TreeTraversingParser parser = new TreeTraversingParser(JsonFactory.instance.readValue(json, JsonNode.class), objectCodec);

        // Test nextToken with closed parser
        parser.close();
        parser.nextToken();
    }

    @Test
    public void testTreeTraversingParserVersion() throws Exception {
        String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\",\"hobbies\":[\"Reading\",\"Swimming\",\"Coding\"]}";

        ObjectCodec objectCodec = new ObjectMapper().getFactory().findValueCodec(JsonNode.class, null);
        TreeTraversingParser parser = new TreeTraversingParser(JsonFactory.instance.readValue(json, JsonNode.class), objectCodec);

        Version version = parser.version();
        Assert.assertEquals(PackageVersion.VERSION, version);
    }
}