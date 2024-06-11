
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TreeTraversingParserTest {
    
    private TreeTraversingParser parser;
    
    @Before
    public void setup() {
        // Create a JSON tree for testing
        String jsonString = "{\"name\":\"John\", \"age\":30, \"city\":\"New York\"}";
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create the parser
        parser = new TreeTraversingParser(jsonNode);
    }
    
    @Test
    public void testNextTokenForStartObject() throws IOException {
        Assert.assertEquals(JsonToken.START_OBJECT, parser.nextToken());
    }
    
    @Test
    public void testNextTokenForFieldName() throws IOException {
        parser.nextToken();
        Assert.assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
    }
    
    @Test
    public void testNextTokenForValueString() throws IOException {
        parser.nextToken(); // Start Object
        parser.nextToken(); // Field Name
        Assert.assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
    }
    
    @Test
    public void testNextTokenForValueNumberInt() throws IOException {
        parser.nextToken(); // Start Object
        parser.nextToken(); // Field Name
        parser.nextToken(); // Value String
        Assert.assertEquals(JsonToken.VALUE_NUMBER_INT, parser.nextToken());
    }
    
    @Test
    public void testNextTokenForValueNumberFloat() throws IOException {
        parser.nextToken(); // Start Object
        parser.nextToken(); // Field Name
        parser.nextToken(); // Value String
        parser.nextToken(); // Value Number Int
        Assert.assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
    }
    
    @Test
    public void testNextTokenForEndObject() throws IOException {
        parser.nextToken(); // Start Object
        parser.nextToken(); // Field Name
        parser.nextToken(); // Value String
        parser.nextToken(); // Value Number Int
        parser.nextToken(); // Value Number Float
        Assert.assertEquals(JsonToken.END_OBJECT, parser.nextToken());
    }
    
    @Test
    public void testCurrentName() throws IOException {
        parser.nextToken(); // Start Object
        parser.nextToken(); // Field Name
        Assert.assertEquals("name", parser.getCurrentName());
    }
    
    @Test
    public void testGetTextForFieldname() throws IOException {
        parser.nextToken(); // Start Object
        parser.nextToken(); // Field Name
        Assert.assertEquals("name", parser.getText());
    }
    
    @Test
    public void testGetTextForValueString() throws IOException {
        parser.nextToken(); // Start Object
        parser.nextToken(); // Field Name
        parser.nextToken(); // Value String
        Assert.assertEquals("John", parser.getText());
    }
    
    @Test
    public void testGetTextForValueNumberInt() throws IOException {
        parser.nextToken(); // Start Object
        parser.nextToken(); // Field Name
        parser.nextToken(); // Value String
        parser.nextToken(); // Value Number Int
        Assert.assertEquals("30", parser.getText());
    }
    
    @Test
    public void testGetTextForValueNumberFloat() throws IOException {
        parser.nextToken(); // Start Object
        parser.nextToken(); // Field Name
        parser.nextToken(); // Value String
        parser.nextToken(); // Value Number Int
        parser.nextToken(); // Value Number Float
        Assert.assertEquals("30.0", parser.getText());
    }
    
    @Test
    public void testGetTextForEndObject() throws IOException {
        parser.nextToken(); // Start Object
        parser.nextToken(); // Field Name
        parser.nextToken(); // Value String
        parser.nextToken(); // Value Number Int
        parser.nextToken(); // Value Number Float
        parser.nextToken(); // End Object
        Assert.assertEquals(null, parser.getText());
    }
    
    @Test
    public void testGetIntValue() throws IOException {
        parser.nextToken(); // Start Object
        parser.nextToken(); // Field Name
        parser.nextToken(); // Value String
        parser.nextToken(); // Value Number Int
        Assert.assertEquals(30, parser.getIntValue());
    }
    
    @Test
    public void testGetLongValue() throws IOException {
        parser.nextToken(); // Start Object
        parser.nextToken(); // Field Name
        parser.nextToken(); // Value String
        parser.nextToken(); // Value Number Int
        Assert.assertEquals(30L, parser.getLongValue());
    }
    
    @Test
    public void testGetFloatValue() throws IOException {
        parser.nextToken(); // Start Object
        parser.nextToken(); // Field Name
        parser.nextToken(); // Value String
        parser.nextToken(); // Value Number Int
        parser.nextToken(); // Value Number Float
        Assert.assertEquals(30.0f, parser.getFloatValue(), 0.001f);
    }
    
    @Test
    public void testGetDoubleValue() throws IOException {
        parser.nextToken(); // Start Object
        parser.nextToken(); // Field Name
        parser.nextToken(); // Value String
        parser.nextToken(); // Value Number Int
        parser.nextToken(); // Value Number Float
        Assert.assertEquals(30.0, parser.getDoubleValue(), 0.001);
    }
    
    @Test
    public void testGetCurrentLocation() {
        JsonLocation expectedLocation = JsonLocation.NA;
        Assert.assertEquals(expectedLocation, parser.getCurrentLocation());
    }
    
    @Test
    public void testIsClosed() throws IOException {
        Assert.assertFalse(parser.isClosed());
        parser.close();
        Assert.assertTrue(parser.isClosed());
    }
    
    @Test
    public void testSkipChildren() throws IOException {
        parser.nextToken(); // Start Object
        parser.nextToken(); // Field Name
        parser.skipChildren();
        Assert.assertEquals(JsonToken.END_OBJECT, parser.getCurrentToken());
    }
    
    @Test
    public void testGetBinaryValue() throws IOException {
        parser.nextToken(); // Start Object
        parser.nextToken(); // Field Name
        byte[] expectedValue = null;
        Assert.assertArrayEquals(expectedValue, parser.getBinaryValue(Base64Variants.MIME));
    }
    
}
