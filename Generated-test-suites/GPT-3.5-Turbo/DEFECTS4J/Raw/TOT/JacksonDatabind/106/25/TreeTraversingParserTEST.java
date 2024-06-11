import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.node.*;

public class TreeTraversingParserTest {

    @Test
    public void testJsonTokenStartArrayNextToken() {
        JsonNode jsonNode = new ArrayNode(null);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        try {
            JsonToken token = parser.nextToken();
            assertEquals(JsonToken.START_ARRAY, token);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testJsonTokenStartObjectNextToken() {
        JsonNode jsonNode = new ObjectNode(null, null);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        try {
            JsonToken token = parser.nextToken();
            assertEquals(JsonToken.START_OBJECT, token);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testJsonTokenValueStringNextToken() {
        JsonNode jsonNode = new TextNode("String Value");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        try {
            JsonToken token = parser.nextToken();
            assertEquals(JsonToken.VALUE_STRING, token);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testJsonTokenValueNumberIntNextToken() {
        JsonNode jsonNode = new IntNode(10);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        try {
            JsonToken token = parser.nextToken();
            assertEquals(JsonToken.VALUE_NUMBER_INT, token);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testJsonTokenValueNumberFloatNextToken() {
        JsonNode jsonNode = new FloatNode(10.5f);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        try {
            JsonToken token = parser.nextToken();
            assertEquals(JsonToken.VALUE_NUMBER_FLOAT, token);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testJsonTokenValueEmbeddedObjectNextToken() {
        JsonNode jsonNode = new BinaryNode("Embedded Object".getBytes());
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        try {
            JsonToken token = parser.nextToken();
            assertEquals(JsonToken.VALUE_EMBEDDED_OBJECT, token);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testJsonTokenFieldNameGetCurrentName() {
        JsonNode jsonNode = new ObjectNode(null, null);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        try {
            parser.nextToken();
            String name = parser.getCurrentName();
            assertNull(name);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testJsonTokenValueStringText() {
        JsonNode jsonNode = new TextNode("String Value");
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        try {
            parser.nextToken();
            String text = parser.getText();
            assertEquals("String Value", text);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testJsonTokenValueNumberIntGetIntValue() {
        JsonNode jsonNode = new IntNode(10);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        try {
            parser.nextToken();
            int intValue = parser.getIntValue();
            assertEquals(10, intValue);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testJsonTokenValueNumberFloatGetFloatValue() {
        JsonNode jsonNode = new FloatNode(10.5f);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        try {
            parser.nextToken();
            float floatValue = parser.getFloatValue();
            assertEquals(10.5f, floatValue, 0.0001);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testJsonTokenValueNumberIntGetNodeNumberType() {
        JsonNode jsonNode = new IntNode(10);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        try {
            parser.nextToken();
            NumberType numberType = parser.getNumberType();
            assertEquals(NumberType.INT, numberType);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testJsonTokenValueNumberFloatGetBigIntegerValue() {
        JsonNode jsonNode = new FloatNode(10.5f);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        try {
            parser.nextToken();
            BigInteger bigIntegerValue = parser.getBigIntegerValue();
            assertNull(bigIntegerValue);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testJsonTokenValueNumberIntGetDecimalValue() {
        JsonNode jsonNode = new IntNode(10);
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        try {
            parser.nextToken();
            BigDecimal decimalValue = parser.getDecimalValue();
            assertEquals(new BigDecimal(10), decimalValue);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testJsonTokenValueEmbeddedObjectGetEmbeddedObject() {
        JsonNode jsonNode = new BinaryNode("Embedded Object".getBytes());
        TreeTraversingParser parser = new TreeTraversingParser(jsonNode);
        try {
            parser.nextToken();
            Object embeddedObject = parser.getEmbeddedObject();
            assertNotNull(embeddedObject);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}