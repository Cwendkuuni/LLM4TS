import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;
    private JsonNode rootNode;

    @Before
    public void setUp() throws IOException {
        // Create a JSON tree for testing
        String json = "{\"name\":\"John\",\"age\":30,\"city\":\"New York\"}";
        rootNode = ObjectMapper.readTree(json);
        parser = new TreeTraversingParser(rootNode);
    }

    @Test
    public void testNextToken() throws IOException {
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
        Assert.assertNull(parser.nextToken());
    }

    @Test
    public void testSkipChildren() throws IOException {
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextValue());
        Assert.assertEquals("John", parser.getText());
        parser.skipChildren();
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testGetCurrentName() throws IOException {
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals("John", parser.getText());
    }

    @Test
    public void testGetText() throws IOException {
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        parser.nextValue();
        Assert.assertEquals("John", parser.getText());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        parser.nextValue();
        Assert.assertEquals("30", parser.getText());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        parser.nextValue();
        Assert.assertEquals("New York", parser.getText());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }

    @Test
    public void testGetNumberType() throws IOException {
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertNull(parser.getNumberType());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(NumberType.INT, parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertNull(parser.getBigIntegerValue());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(BigInteger.valueOf(30), parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertNull(parser.getDecimalValue());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(BigDecimal.valueOf(30), parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException {
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals(0.0, parser.getDoubleValue(), 0);
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(30.0, parser.getDoubleValue(), 0);
    }

    @Test
    public void testGetFloatValue() throws IOException {
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals(0.0f, parser.getFloatValue(), 0);
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(30.0f, parser.getFloatValue(), 0);
    }

    @Test
    public void testGetIntValue() throws IOException {
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals(0, parser.getIntValue());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(30, parser.getIntValue());
    }

    @Test
    public void testGetLongValue() throws IOException {
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals(0L, parser.getLongValue());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(30L, parser.getLongValue());
    }

    @Test
    public void testGetNumberValue() throws IOException {
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertNull(parser.getNumberValue());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(30, parser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() throws IOException {
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertNull(parser.getEmbeddedObject());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testGetBinaryValue() throws IOException {
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertNull(parser.getBinaryValue(null));
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertNull(parser.getBinaryValue(null));
    }

    @Test
    public void testReadBinaryValue() throws IOException {
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(0, parser.readBinaryValue(null, null));
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals(0, parser.readBinaryValue(null, null));
    }

    @Test
    public void testIsNaN() throws IOException {
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertFalse(parser.isNaN());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertFalse(parser.isNaN());
    }
}