import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {
    
    private JsonGeneratorImpl jsonGenerator;
    
    @Before
    public void setUp() {
        jsonGenerator = new JsonGeneratorImpl(null, 0, null) {
            // Mock implementation of abstract methods
            @Override
            protected void _releaseBuffers() {}
            
            @Override
            protected void _verifyValueWrite(String typeMsg) {}
            
            @Override
            public void close() {}
        };
    }
    
    @Test
    public void testWriteStringField_success() throws IOException {
        // Arrange
        String fieldName = "name";
        String value = "John";
        
        // Act
        jsonGenerator.writeStringField(fieldName, value);
        String output = jsonGenerator.getOutput();
        
        // Assert
        assertEquals("\"name\":\"John\"", output);
    }
    
    @Test
    public void testWriteStringField_emptyFieldName() throws IOException {
        // Arrange
        String fieldName = "";
        String value = "John";
        
        // Act
        jsonGenerator.writeStringField(fieldName, value);
        String output = jsonGenerator.getOutput();
        
        // Assert
        assertEquals("\":\"John\"", output);
    }
    
    @Test
    public void testWriteStringField_nullFieldName() {
        // Arrange
        String fieldName = null;
        String value = "John";
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            jsonGenerator.writeStringField(fieldName, value);
        });
    }
    
    @Test
    public void testWriteStringField_nullValue() throws IOException {
        // Arrange
        String fieldName = "name";
        String value = null;
        
        // Act
        jsonGenerator.writeStringField(fieldName, value);
        String output = jsonGenerator.getOutput();
        
        // Assert
        assertEquals("\"name\":null", output);
    }
}