import static org.junit.Assert.*;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        // Test the withFormat method functionality
        DateTimeSerializerBase dateTimeSerializer = new DateTimeSerializerBase<>(String.class, true, new SimpleDateFormat("yyyy-MM-dd"));
        DateTimeSerializerBase result = dateTimeSerializer.withFormat(true, new SimpleDateFormat("MM-dd-yyyy"));
        assertNotNull(result);
        assertTrue(result._useTimestamp);
        assertEquals("MM-dd-yyyy", ((SimpleDateFormat) result._customFormat).toPattern());
    }

    @Test
    public void testCreateContextual() {
        // Test the createContextual method with different scenarios
        DateTimeSerializerBase dateTimeSerializer = new DateTimeSerializerBase<>(String.class, false, null);
        BeanProperty beanProperty = new BeanProperty() {
            // Implement required methods for testing
        };
        SerializerProvider serializers = new SerializerProvider() {
            // Implement required methods for testing
        };
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        dateTimeSerializer.createContextual(serializers, beanProperty);
        assertEquals(Boolean.FALSE, dateTimeSerializer._useTimestamp);
        assertNotNull(dateTimeSerializer._customFormat);
        assertEquals(JsonFormat.Shape.STRING, format.getShape());
    }

    // Add more test cases for other public methods

}