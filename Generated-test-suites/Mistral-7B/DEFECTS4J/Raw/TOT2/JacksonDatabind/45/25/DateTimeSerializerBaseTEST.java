import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class DateTimeSerializerBaseTest {
    @Test
    public void testWithFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        DateTimeSerializerBase<Long> serializer = new DateSerializerBase<Long>(Long.class, false, null);

        JsonSerializer<Long> serializerWithFormatTimestamp = serializer.withFormat(Boolean.TRUE, null);
        JsonSerializer<Long> serializerWithFormatCustomFormat = serializer.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        String timestampJson = mapper.writeValueAsString(1L, serializerWithFormatTimestamp, new SerializerProvider().builder().build());
        String customFormatJson = mapper.writeValueAsString(1L, serializerWithFormatCustomFormat, new SerializerProvider().builder().build());

        assertEquals("1", timestampJson);
        assertEquals("2022-12-31 12:59:59", customFormatJson);
    }
}