
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.junit.Assert;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();

    @Test
    public void testDeserializeShouldReturnStringArray() throws IOException {
        // Arrange
        String json = "[\"apple\", \"banana\", \"orange\"]";
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        // Act
        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
        // Assert
        Assert.assertEquals(3, result.length);
        Assert.assertEquals("apple", result[0]);
        Assert.assertEquals("banana", result[1]);
        Assert.assertEquals("orange", result[2]);
    }
    
    @Test
    public void testDeserializeShouldReturnStringArrayWithNullValue() throws IOException {
        // Arrange
        String json = "[\"apple\", null, \"orange\"]";
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        // Act
        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
        // Assert
        Assert.assertEquals(3, result.length);
        Assert.assertEquals("apple", result[0]);
        Assert.assertNull(result[1]);
        Assert.assertEquals("orange", result[2]);
    }
    
    @Test
    public void testDeserializeShouldReturnEmptyStringArray() throws IOException {
        // Arrange
        String json = "[]";
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        // Act
        String[] result = stringArrayDeserializer.deserialize(jp, ctxt);
        // Assert
        Assert.assertEquals(0, result.length);
    }
}
