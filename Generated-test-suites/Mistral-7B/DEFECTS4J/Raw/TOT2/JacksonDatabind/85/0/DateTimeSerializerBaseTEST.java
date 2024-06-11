import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer.None;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(Long.class, true, null);

        // Test with null customFormat
        DateTimeSerializerBase<Long> withTimestampOnly = serializer.withFormat(true, null);
        ObjectNode result = mapper.writeValueAsTree(new Long(1609459200000L), withTimestampOnly, new SerializerProvider());
        assertEquals("1471526400000", result.toString());

        // Test with customFormat
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateTimeSerializerBase<Long> withCustomFormat = serializer.withFormat(false, customFormat);
        result = mapper.writeValueAsTree(new Long(1609459200000L), withCustomFormat, new SerializerProvider());
        assertEquals("14-06-2017 12:00:00", result.toString());
    }
}