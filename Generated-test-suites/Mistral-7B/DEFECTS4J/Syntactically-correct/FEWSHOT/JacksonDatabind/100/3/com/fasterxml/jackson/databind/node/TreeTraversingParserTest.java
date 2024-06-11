package com.fasterxml.jackson.databind.node;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Test;

public class TreeTraversingParserTest {

    @Test
    public void testNullParser() {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        assertNull(parser.nextToken());
        parser.close();
    }

    @Test
    public void testEmptyNode() {
        JsonNode rootNode = null;
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        parser.close();
    }

    @Test
    public void testArrayNode() throws IOException {
        String json = "[1, 2.5, 'foo', null, true]";
        JsonNode rootNode = new ObjectMapper().readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        assertEquals(JsonToken.INT_VALUE, parser.nextToken());
        assertEquals(1, parser.getIntValue());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("foo", parser.getText());
        assertEquals(JsonToken.NULL, parser.nextToken());
        assertEquals(JsonToken.BOOL_TRUE, parser.nextToken());
        assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        parser.close();
    }

    @Test
    public void testObjectNode() throws IOException {
        String json = "{\"name\": \"John\", \"age\": 30, \"city\": null}";
        JsonNode rootNode = new ObjectMapper().readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("name", parser.getCurrentName());
        assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        assertEquals("John", parser.getText());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("age", parser.getCurrentName());
        assertEquals(JsonToken.INT_VALUE, parser.nextToken());
        assertEquals(30, parser.getIntValue());
        assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        assertEquals("city", parser.getCurrentName());
        assertNull(parser.nextToken());
        assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        parser.close();
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidJson() throws IOException {
        String json = "{\"name\": \"John\"age: 30, \"city\": null}"; // missing comma
        JsonNode rootNode = new ObjectMapper().readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        parser.nextToken(); // parse invalid JSON
    }

    @Test
    public void testSkipChildren() throws IOException {
        String json = "[1, 2, 3]";
        JsonNode rootNode = new ObjectMapper().readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        parser.nextToken();
        parser.nextToken();
        parser.skipChildren();
        assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        assertEquals(3, parser.getIntValue());
        parser.close();
    }

    @Test
    public void testClose() throws IOException {
        String json = "[1, 2, 3]";
        JsonNode rootNode = new ObjectMapper().readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.close();
        assertNull(parser.nextToken());
    }

    @Test
    public void testAccessTextValue() throws IOException {
        String json = "{\"name\": \"John\", \"age\": 30, \"city\": null}";
        JsonNode rootNode = new ObjectMapper().readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals("John", parser.getText());
        parser.nextToken();
        assertEquals("30", parser.getText());
        parser.nextToken();
        assertNull(parser.getText());

        parser.close();
    }

    @Test
    public void testAccessNumberValue() throws IOException {
        String json = "[1, 2.5, 30, 4]";
        JsonNode rootNode = new ObjectMapper().readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        assertEquals(1, parser.getIntValue());
        parser.nextToken();
        assertEquals(2.5, parser.getDoubleValue(), 0.001);
        parser.nextToken();
        assertEquals(30, parser.getIntValue());
        parser.nextToken();
        assertEquals(4, parser.getIntValue());

        parser.close();
    }

    @Test
    public void testAccessEmbeddedObject() throws IOException {
        String json = "{\"name\": {\"first\": \"John\", \"last\": \"Doe\"}}";
        JsonNode rootNode = new ObjectMapper().readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        parser.nextToken();
        assertEquals("name", parser.getCurrentName());
        JsonNode embeddedNode = parser.nextToken();
        assertEquals(JsonToken.START_OBJECT, embeddedNode.asToken().asStartStructToken().getTokenType());

        assertEquals("first", parser.getCurrentName());
        assertEquals("John", parser.getText());
        parser.nextToken();
        assertEquals("last", parser.getCurrentName());
        assertEquals("Doe", parser.getText());
        parser.nextToken();
        assertEquals(JsonToken.END_OBJECT, parser.nextToken().asToken().asEndStructToken().getTokenType());

        parser.close();
    }

    @Test
    public void testAccessBinaryValue() throws IOException {
        String json = "{\"image\": \"R0lGODlhCAAQAPeOAQABAAAAAAAD//ywAAAAAAQABAAACAUwAAAAAAQABAAAD//8AAEQgAAAwAAAAQAQAA AgAAh/0PoAAAkJcMNykwOwPDxoQsOpq6DuJFxjJFyJHxjzJ2jzJxjwJxjyJyopJyowJ10pJ14pJ18pJ1+wJ19wJ23wJ27wJ31wJ35wJ3+wJ37wJ41wJ45wJ5+wJ53wJ57wJ6+wJ63wJ67wJ71wJ75wJ7+wJ77wJ81wJ85wJ89wJ91wJ95wJ99wJa1wJa5wJa9wJb1wJb5wJb9wJc1wJc5wJc9wJd1wJd5wJd9wJe1wJe5wJe9wJf1wJf5wJf9wJg1wJg5wJg9wJh1wJh5wJh9wJi1wJi5wJi9wJj1wJj5wJj9wJk1wJk5wJk9wJl1wJl5wJl9wJm1wJm5wJm9wJn1wJn5wJn9wJo1wJo5wJo9wJp1wJp5wJp9wJq1wJq5wJq9wJr1wJr5wJr9wJs1wJs5wJs9wJt1wJt5wJt9wJu1wJu5wJu9wJv1wJv5wJv9wJw1wJw5wJw9wJx1wJx5wJx9wJy1wJy5wJy9wJz1wJz5wJz9wKA==\"}";
        JsonNode rootNode = new ObjectMapper().readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        parser.nextToken();
        assertEquals("image", parser.getCurrentName());
        byte[] binaryData = parser.getBinaryValue(Base64Variant.STANDARD);
        assertNotNull(binaryData);
        parser.nextToken();

        parser.close();
    }

    @Test
    public void testReadBinaryValue() throws IOException {
        String json = "{\"image\": \"R0lGODlhCAAQAPeOAQABAAAAAAAD//ywAAAAAAQABAAACAUwAAAAAAQABAAAD//8AAEQgAAAwAAAAQAQAA AgAAh/0PoAAAkJcMNykwOwPDxoQsOpq6DuJFxjJFyJHxjzJ2jzJxjwJxjyJyopJyowJ10pJ14pJ18pJ1+wJ19wJ23wJ27wJ31wJ35wJ3+wJ37wJ41wJ45wJ5+wJ53wJ57wJ6+wJ63wJ67wJ71wJ75wJ7+wJ77wJ81wJ85wJ89wJ91wJ95wJ99wJa1wJa5wJa9wJb1wJb5wJb9wJc1wJc5wJc9wJd1wJd5wJd9wJe1wJe5wJe9wJf1wJf5wJf9wJg1wJg5wJg9wJh1wJh5wJh9wJi1wJi5wJi9wJj1wJj5wJj9wJk1wJk5wJk9wJl1wJl5wJl9wJm1wJm5wJm9wJn1wJn5wJn9wJo1wJo5wJo9wJp1wJp5wJp9wJq1wJq5wJq9wJr1wJr5wJr9wJs1wJs5wJs9wJt1wJt5wJt9wJu1wJu5wJu9wJv1wJv5wJv9wJw1wJw5wJw9wJx1wJx5wJx9wJy1wJy5wJy9wJz1wJz5wJz9wKA==\"}";
        JsonNode rootNode = new ObjectMapper().readTree(json);
        TreeTraversingParser parser = new TreeTraversingParser(rootNode);

        parser.nextToken();
        assertEquals("image", parser.getCurrentName());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int readBytes = parser.readBinaryValue(Base64Variant.STANDARD, outputStream);
        byte[] binaryData = outputStream.toByteArray();
        assertNotNull(binaryData);
        assertEquals(readBytes, binaryData.length);
        parser.nextToken();

        parser.close();
    }
}