import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DateTimeSerializerBaseTest {
    @Test
    public void testWithFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Long> serializer = mapper.getDefaultInstance().findValueSerializer(Long.class);
        DateTimeSerializerBase<Long> withFormatSerializer = serializer.withFormat(Boolean.TRUE, null);
        assertNotNull(withFormatSerializer);
    }
}