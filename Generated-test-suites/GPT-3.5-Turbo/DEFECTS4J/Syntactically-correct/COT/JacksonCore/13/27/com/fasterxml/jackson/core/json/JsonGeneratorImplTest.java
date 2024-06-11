import org.junit.Test;
import static org.junit.Assert.*;

public class JsonGeneratorImplTest {

    @Test
    public void testWriteStringField() {
        // Arrange
        JsonGeneratorImpl jsonGenerator = new JsonGeneratorImpl();
        String fieldName = "name";
        String value = "John Smith";
        StringWriter writer = new StringWriter();
        
        // Act
        jsonGenerator.writeStringField(fieldName, value);
        String output = writer.toString();
        
        // Assert
        assertEquals("\"name\":\"John Smith\"", output);
    }

}