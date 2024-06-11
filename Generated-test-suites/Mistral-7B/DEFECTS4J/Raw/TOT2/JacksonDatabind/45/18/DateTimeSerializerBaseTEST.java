import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Test serialization with default settings
        ObjectNode defaultOutput = mapper.valueToTree(123L);
        assertNotNull(defaultOutput);

        // Test serialization with custom format
        DateTimeSerializerBase<Long> customFormatterSerializer = serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        ObjectNode customOutput = mapper.valueToTree(123L, customFormatterSerializer);
        TextNode customText = (TextNode) customOutput.get(0);
        String customFormattedValue = customText.asText();
        String expectedCustomFormattedValue = "2023-03-31T12:34:56+01:00"; // Set the expected value based on the custom format
        assertNotNull(customOutput);
        assertEquals(expectedCustomFormattedValue, customFormattedValue);
    }
}