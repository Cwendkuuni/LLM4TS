import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class TreeTraversingParserTest {

    private TreeTraversingParser parser;
    private JsonNode rootNode;
    
    @Before
    public void setup() {
        try {
            String json = "{\"key\": \"value\"}";
            ObjectMapper mapper = new ObjectMapper();
            rootNode = mapper.readTree(json);
            parser = new TreeTraversingParser(rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetCodec() {
        ObjectCodec codec = new ObjectMapper();
        parser.setCodec(codec);
        assertEquals(codec, parser.getCodec());
    }

    @Test
    public void testVersion() {
        Version version = com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION;
        assertEquals(version, parser.version());
    }

    @Test
    public void testClose() {
        try {
            parser.close();
            assertTrue(parser.isClosed());
        } catch (IOException e) {
            fail("Exception thrown while closing parser");
        }
    }

    @Test
    public void testNextToken() {
        try {
            assertEquals(JsonToken.START_OBJECT, parser.nextToken());
            assertEquals(JsonToken.FIELD_NAME, parser.nextToken());
            assertEquals(JsonToken.VALUE_STRING, parser.nextToken());
            assertEquals(JsonToken.END_OBJECT, parser.nextToken());
            assertNull(parser.nextToken());
        } catch (IOException e) {
            fail("Exception thrown while getting next token");
        }
    }

    @Test
    public void testSkipChildren() {
        try {
            parser.nextToken();
            parser.nextToken();
            parser.nextToken();
            assertEquals(parser, parser.skipChildren());
        } catch (IOException e) {
            fail("Exception thrown while skipping children");
        }
    }

    @Test
    public void testGetCurrentName() {
        try {
            parser.nextToken();
            assertEquals("key", parser.getCurrentName());
        } catch (IOException e) {
            fail("Exception thrown while getting current name");
        }
    }

    @Test
    public void testGetText() {
        try {
            parser.nextToken();
            assertEquals("key", parser.getText());
            parser.nextToken();
            assertEquals("value", parser.getText());
        } catch (IOException e) {
            fail("Exception thrown while getting text");
        }
    }

    @Test
    public void testGetNumberType() {
        try {
            assertNull(parser.getNumberType());
        } catch (IOException e) {
            fail("Exception thrown while getting number type");
        }
    }

    @Test
    public void testGetBigIntegerValue() {
        try {
            assertNull(parser.getBigIntegerValue());
        } catch (IOException e) {
            fail("Exception thrown while getting big integer value");
        }
    }

    @Test
    public void testGetDecimalValue() {
        try {
            assertNull(parser.getDecimalValue());
        } catch (IOException e) {
            fail("Exception thrown while getting decimal value");
        }
    }

    @Test
    public void testGetDoubleValue() {
        try {
            assertEquals(0.0, parser.getDoubleValue(), 0.0001);
        } catch (IOException e) {
            fail("Exception thrown while getting double value");
        }
    }

    @Test
    public void testGetFloatValue() {
        try {
            assertEquals(0.0f, parser.getFloatValue(), 0.0001);
        } catch (IOException e) {
            fail("Exception thrown while getting float value");
        }
    }

    @Test
    public void testGetLongValue() {
        try {
            assertEquals(0, parser.getLongValue());
        } catch (IOException e) {
            fail("Exception thrown while getting long value");
        }
    }

    @Test
    public void testGetIntValue() {
        try {
            assertEquals(0, parser.getIntValue());
        } catch (IOException e) {
            fail("Exception thrown while getting int value");
        }
    }

    @Test
    public void testGetNumberValue() {
        try {
            assertNull(parser.getNumberValue());
        } catch (IOException e) {
            fail("Exception thrown while getting number value");
        }
    }

    @Test
    public void testGetEmbeddedObject() {
        assertNull(parser.getEmbeddedObject());
    }

    @Test
    public void testIsNaN() {
        assertFalse(parser.isNaN());
    }

    @Test
    public void testGetBinaryValue() {
        try {
            assertNull(parser.getBinaryValue(Base64Variant.getDefaultVariant()));
        } catch (IOException e) {
            fail("Exception thrown while getting binary value");
        }
    }

    @Test
    public void testReadBinaryValue() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            assertEquals(0, parser.readBinaryValue(Base64Variant.getDefaultVariant(), out));
        } catch (IOException e) {
            fail("Exception thrown while reading binary value");
        }
    }

}