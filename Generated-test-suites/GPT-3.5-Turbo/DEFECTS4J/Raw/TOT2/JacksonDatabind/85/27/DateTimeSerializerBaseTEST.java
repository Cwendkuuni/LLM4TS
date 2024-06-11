import static org.junit.Assert.*;

import org.junit.Test;
import com.fasterxml.jackson.databind.ser.std.*;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<String> serializer = new CustomDateTimeSerializer();
        DateTimeSerializerBase<String> updatedSerializer = serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd"));
        assertNotNull(updatedSerializer);
        assertEquals(false, updatedSerializer._useTimestamp);
        assertNotNull(updatedSerializer._customFormat);
    }

    @Test
    public void testCreateContextual() {
        DateTimeSerializerBase<String> serializer = new CustomDateTimeSerializer();
        try {
            serializer.createContextual(null, null);
        } catch (JsonMappingException e) {
            assertNotNull(e);
        }
    }

    private class CustomDateTimeSerializer extends DateTimeSerializerBase<String> {
        public CustomDateTimeSerializer() {
            super(String.class, true, new SimpleDateFormat("yyyy-MM-dd"));
        }

        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value);
        }

        @Override
        protected long _timestamp(String value) {
            return 0L;
        }
    }
}