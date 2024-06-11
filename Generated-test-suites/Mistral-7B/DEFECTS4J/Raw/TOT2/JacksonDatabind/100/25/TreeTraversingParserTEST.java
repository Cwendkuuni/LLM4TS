import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import static com.fasterxml.jackson.databind.node.TextNode.valueOf;

public class TreeTraversingParserTest {

    @Test
    public void testBasicFunctionality_Array() throws Exception {
        JsonNode jsonArray = valueOf("[1,2,3]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonArray);

        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(1, parser.getIntValue());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(2, parser.getIntValue());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(3, parser.getIntValue());
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        parser.close();
    }

    @Test
    public void testBasicFunctionality_Object() throws Exception {
        JsonNode jsonObject = valueOf("{\"name\":\"John\",\"age\":30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonObject);

        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("name", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
        Assert.assertEquals("John", parser.getText());
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
        Assert.assertEquals("age", parser.getCurrentName());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(30, parser.getIntValue());
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testExceptionHandling_NullInput() throws Exception {
        TreeTraversingParser parser = new TreeTraversingParser(null);
        parser.nextToken();
    }

    @Test(expected = JsonParseException.class)
    public void testExceptionHandling_InvalidJson() throws Exception {
        JsonNode jsonObject = valueOf("{name:John}"); // missing colon in property name
        TreeTraversingParser parser = new TreeTraversingParser(jsonObject);
        parser.nextToken();

        parser.close();
    }

    @Test
    public void testSkipChildren() throws Exception {
        JsonNode jsonArray = valueOf("[1,2,3,4,5]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonArray);

        Assert.assertEquals(JsonToken.START_ARRAY, parser.nextToken());
        parser.skipChildren();
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(4, parser.getIntValue());
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
        Assert.assertEquals(5, parser.getIntValue());
        Assert.assertEquals(JsonToken.END_ARRAY, parser.nextToken());

        parser.close();
    }

    @Test
    public void testIsClosed() throws Exception {
        JsonNode jsonArray = valueOf("[1,2,3]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonArray);

        Assert.assertFalse(parser.isClosed());
        parser.close();
        Assert.assertTrue(parser.isClosed());

        parser.nextToken();
    }

    @Test
    public void testTextAccessors() throws Exception {
        JsonNode jsonObject = valueOf("{\"name\":\"John\",\"age\":30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonObject);

        Assert.assertEquals("John", parser.getText());
        Assert.assertEquals("age", parser.getCurrentName());
        Assert.assertEquals("30", parser.getText());

        parser.close();
    }

    @Test
    public void testNumericAccessors() throws Exception {
        JsonNode jsonObject = valueOf("{\"number\":123.45}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonObject);

        Assert.assertEquals(123, parser.getIntValue());
        Assert.assertEquals(123.0, parser.getDoubleValue(), 0.0);
        Assert.assertEquals(123.45, parser.getDoubleValue(), 0.0);

        parser.close();
    }

    @Test
    public void testBinaryAccessors() throws Exception {
        JsonNode jsonObject = valueOf("{\"binary\":AQIDBAQDBAQO/w==}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonObject);

        byte[] binaryData = parser.getBinaryValue(Base64Variant.BASE64_STANDARD);
        Assert.assertArrayEquals(new byte[]{65, 69, 78, 69, 65}, binaryData);

        parser.close();
    }

    @Test
    public void testClose() throws Exception {
        JsonNode jsonArray = valueOf("[1,2,3]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonArray);

        parser.close();
        parser.nextToken();

        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testGetParsingContext() throws Exception {
        JsonNode jsonArray = valueOf("[1,2,3]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonArray);
        JsonStreamContext context = parser.getParsingContext();

        Assert.assertNotNull(context);
        Assert.assertEquals("[1,2,3]", context.getText());

        parser.close();
    }

    @Test
    public void testGetTokenLocation() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        JsonNode jsonArray = mapper.readTree("[1,2,3]");
        TreeTraversingParser parser = new TreeTraversingParser(jsonArray);

        JsonLocation location = parser.getTokenLocation();

        Assert.assertEquals(JsonLocation.Type.PARSE_START, location.getType());
        Assert.assertEquals(0, location.getLineNumber());
        Assert.assertEquals(0, location.getColumnNumber());

        parser.close();
    }

    @Test
    public void testGetCurrentLocation() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        JsonNode jsonObject = mapper.readTree("{\"name\":\"John\",\"age\":30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonObject);

        JsonLocation location = parser.getCurrentLocation();

        Assert.assertEquals(JsonLocation.Type.CURRENT_LOCATION, location.getType());
        Assert.assertEquals("name", location.getName());
        Assert.assertEquals(0, location.getLineNumber());
        Assert.assertEquals(0, location.getColumnNumber());

        parser.nextToken();

        location = parser.getCurrentLocation();

        Assert.assertEquals("age", location.getName());

        parser.close();
    }

    @Test
    public void testGetterMethods() throws Exception {
        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        JsonNode jsonObject = mapper.readTree("{\"name\":\"John\",\"age\":30}");
        TreeTraversingParser parser = new TreeTraversingParser(jsonObject);

        Assert.assertEquals(factory, parser.getCodec().getFactory());
        Assert.assertEquals("Jackson Core JSON Processing", parser.getCodec().getClass().getName());
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());

        parser.close();
    }
}