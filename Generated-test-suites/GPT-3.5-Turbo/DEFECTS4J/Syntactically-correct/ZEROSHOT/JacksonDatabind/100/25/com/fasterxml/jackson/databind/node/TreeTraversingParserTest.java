import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class TreeTraversingParserTest {
    private ObjectMapper mapper;
    private JsonNode jsonNode;
    private TreeTraversingParser parser;

    @Before
    public void setup() throws IOException {
        mapper = new ObjectMapper();
        String json = "{ \"name\": \"John\", \"age\": 30, \"city\": \"New York\" }";
        jsonNode = mapper.readTree(json);
        parser = new TreeTraversingParser(jsonNode);
    }

    @After
    public void tearDown() {
        parser.close();
    }

    @Test
    public void testNextToken() throws IOException {
        parser.nextToken();
        Assert.assertEquals(JsonToken.START_OBJECT, parser.getCurrentToken());
    }

    @Test
    public void testSkipChildren() throws IOException {
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        parser.skipChildren();
        Assert.assertEquals(JsonToken.END_ARRAY, parser.getCurrentToken());
    }

    @Test
    public void testIsClosed() throws IOException {
        Assert.assertFalse(parser.isClosed());
        parser.close();
        Assert.assertTrue(parser.isClosed());
    }

    @Test
    public void testGetCurrentName() throws IOException {
        parser.nextToken();
        parser.nextToken();
        Assert.assertEquals("name", parser.getCurrentName());
    }

    @Test
    public void testGetText() throws IOException {
        parser.nextToken();
        parser.nextToken();

        Assert.assertEquals("John", parser.getText());
    }

    @Test
    public void testGetNumberType() throws IOException {
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();

        Assert.assertEquals(NumberType.INT, parser.getNumberType());
    }

    @Test
    public void testGetBigIntegerValue() throws IOException {
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();

        Assert.assertEquals(new BigInteger("30"), parser.getBigIntegerValue());
    }

    @Test
    public void testGetDecimalValue() throws IOException {
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();

        Assert.assertEquals(new BigDecimal("30"), parser.getDecimalValue());
    }

    @Test
    public void testGetDoubleValue() throws IOException {
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();

        Assert.assertEquals(30.0, parser.getDoubleValue(), 0.0);
    }

    @Test
    public void testGetFloatValue() throws IOException {
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();

        Assert.assertEquals(30.0f, parser.getFloatValue(), 0.0);
    }

    @Test
    public void testGetLongValue() throws IOException {
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();

        Assert.assertEquals(30, parser.getLongValue());
    }

    @Test
    public void testGetIntValue() throws IOException {
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();

        Assert.assertEquals(30, parser.getIntValue());
    }

    @Test
    public void testGetNumberValue() throws IOException {
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();

        Assert.assertEquals(30, parser.getNumberValue());
    }

    @Test
    public void testGetEmbeddedObject() throws IOException {
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();

        Assert.assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() throws IOException {
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();

        Assert.assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() throws IOException {
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();

        Assert.assertNull(parser.getBinaryValue(Base64Variant.getDefaultVariant()));
    }

    @Test
    public void testReadBinaryValue() throws IOException {
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();

        Assert.assertEquals(0, parser.readBinaryValue(Base64Variant.getDefaultVariant(), null));
    }
}