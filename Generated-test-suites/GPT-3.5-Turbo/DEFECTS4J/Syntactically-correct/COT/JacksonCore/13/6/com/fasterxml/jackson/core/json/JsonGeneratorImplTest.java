
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {
  
  @Test
  public void testWriteStringField() {
    // Create a new instance of JsonGeneratorImpl
    JsonGeneratorImpl generator = new JsonGeneratorImpl();
    
    // Create a field name and value
    String fieldName = "name";
    String value = "John";
    
    // Call the writeStringField method
    generator.writeStringField(fieldName, value);
    
    // Verify that the field name and value are written correctly
    assertEquals("\"name\":\"John\"", generator.toString());
  }
}
