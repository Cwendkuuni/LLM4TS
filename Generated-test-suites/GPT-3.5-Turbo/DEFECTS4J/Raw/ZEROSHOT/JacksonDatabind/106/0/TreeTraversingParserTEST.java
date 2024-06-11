import org.junit.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;

public class TreeTraversingParserTest {
    
    private ObjectMapper mapper = new ObjectMapper();
    
    @Test
    public void testNextToken() {
        // Arrange
        JsonNode node = mapper.createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(node);
        
        // Act
        JsonToken token = parser.nextToken();
        
        // Assert
        assertEquals(JsonToken.START_OBJECT, token);
    }
    
    @Test
    public void testSkipChildren() {
        // Arrange
        JsonNode node = mapper.createObjectNode();
        TreeTraversingParser parser = new TreeTraversingParser(node);
        
        // Act
        parser.nextToken();
        parser.skipChildren();
        JsonToken token = parser.getCurrentToken();
        
        // Assert
        assertEquals(JsonToken.END_OBJECT, token);
    }
    
    @Test
    public void testGetCurrentName() {
        // Arrange
        JsonNode node = mapper.createObjectNode().put("name", "John");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        
        // Act
        parser.nextToken();
        String name = parser.getCurrentName();
        
        // Assert
        assertEquals("name", name);
    }
    
    @Test
    public void testGetText() {
        // Arrange
        JsonNode node = mapper.createObjectNode().put("name", "John");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        
        // Act
        parser.nextToken();
        String text = parser.getText();
        
        // Assert
        assertEquals("name", text);
    }
    
    @Test
    public void testGetNumberType() {
        // Arrange
        JsonNode node = mapper.createObjectNode().put("age", 30);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        
        // Act
        parser.nextToken();
        NumberType type = parser.getNumberType();
        
        // Assert
        assertEquals(NumberType.INT, type);
    }
    
    @Test
    public void testGetBigIntegerValue() {
        // Arrange
        JsonNode node = mapper.createObjectNode().put("value", BigInteger.valueOf(10));
        TreeTraversingParser parser = new TreeTraversingParser(node);
        
        // Act
        parser.nextToken();
        BigInteger value = parser.getBigIntegerValue();
        
        // Assert
        assertEquals(BigInteger.valueOf(10), value);
    }
    
    @Test
    public void testGetDecimalValue() {
        // Arrange
        JsonNode node = mapper.createObjectNode().put("value", BigDecimal.valueOf(1.23));
        TreeTraversingParser parser = new TreeTraversingParser(node);
        
        // Act
        parser.nextToken();
        BigDecimal value = parser.getDecimalValue();
        
        // Assert
        assertEquals(BigDecimal.valueOf(1.23), value);
    }
    
    @Test
    public void testGetDoubleValue() {
        // Arrange
        JsonNode node = mapper.createObjectNode().put("value", 1.23);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        
        // Act
        parser.nextToken();
        double value = parser.getDoubleValue();
        
        // Assert
        assertEquals(1.23, value, 0.0);
    }
    
    @Test
    public void testGetFloatValue() {
        // Arrange
        JsonNode node = mapper.createObjectNode().put("value", 1.23);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        
        // Act
        parser.nextToken();
        float value = parser.getFloatValue();
        
        // Assert
        assertEquals(1.23f, value, 0.0);
    }
    
    @Test
    public void testGetIntValue() {
        // Arrange
        JsonNode node = mapper.createObjectNode().put("value", 10);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        
        // Act
        parser.nextToken();
        int value = parser.getIntValue();
        
        // Assert
        assertEquals(10, value);
    }
    
    @Test
    public void testGetLongValue() {
        // Arrange
        JsonNode node = mapper.createObjectNode().put("value", 100L);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        
        // Act
        parser.nextToken();
        long value = parser.getLongValue();
        
        // Assert
        assertEquals(100L, value);
    }
    
    @Test
    public void testGetNumberValue() {
        // Arrange
        JsonNode node = mapper.createObjectNode().put("value", 1.23);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        
        // Act
        parser.nextToken();
        Number value = parser.getNumberValue();
        
        // Assert
        assertEquals(1.23, value);
    }
    
    @Test
    public void testGetEmbeddedObject() {
        // Arrange
        JsonNode node = mapper.createObjectNode().putPOJO("value", "embedded");
        TreeTraversingParser parser = new TreeTraversingParser(node);
        
        // Act
        parser.nextToken();
        Object value = parser.getEmbeddedObject();
        
        // Assert
        assertEquals("embedded", value);
    }
    
    @Test
    public void testIsNaN() {
        // Arrange
        JsonNode node = mapper.createObjectNode().put("value", Double.NaN);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        
        // Act
        parser.nextToken();
        boolean isNaN = parser.isNaN();
        
        // Assert
        assertTrue(isNaN);
    }
    
    @Test
    public void testGetBinaryValue() {
        // Arrange
        byte[] binaryValue = {0x01, 0x02, 0x03};
        JsonNode node = mapper.createObjectNode().put("value", binaryValue);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        
        // Act
        parser.nextToken();
        byte[] value = parser.getBinaryValue(null);
        
        // Assert
        assertArrayEquals(binaryValue, value);
    }
    
    @Test
    public void testReadBinaryValue() {
        // Arrange
        byte[] binaryValue = {0x01, 0x02, 0x03};
        JsonNode node = mapper.createObjectNode().put("value", binaryValue);
        TreeTraversingParser parser = new TreeTraversingParser(node);
        
        // Act
        parser.nextToken();
        byte[] buffer = new byte[3];
        int length = parser.readBinaryValue(null, buffer, 0, 3);
        
        // Assert
        assertEquals(3, length);
    }
}