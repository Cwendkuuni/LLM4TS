import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.*;

public class NumberSerializerTest {

    @Test
    public void testSerialize() throws IOException {
        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        
        assertEquals("123", writer.writeValueAsString(new NumberSerializer().serialize((short) 123)));
        assertEquals("123", writer.writeValueAsString(new NumberSerializer().serialize((int) 123)));
        assertEquals("123", writer.writeValueAsString(new NumberSerializer().serialize((long) 123)));
        assertEquals("123.45", writer.writeValueAsString(new NumberSerializer().serialize((float) 123.45)));
        assertEquals("123.45", writer.writeValueAsString(new NumberSerializer().serialize((double) 123.45)));
        assertEquals("123", writer.writeValueAsString(new NumberSerializer().serialize((byte) 123)));
        assertEquals("123", writer.writeValueAsString(new NumberSerializer().serialize((char) 123)));
        assertEquals("1234567890123456789", writer.writeValueAsString(new NumberSerializer().serialize(new BigInteger("1234567890123456789"))));
        assertEquals("1234567.890123456789", writer.writeValueAsString(new NumberSerializer().serialize(new BigDecimal("1234567.890123456789"))));
    }

    @Test
    public void testGetSchema() {
        JsonNode schema = new NumberSerializer().getSchema(null, null);
        assertEquals("number", schema.get("type").asText());
        assertEquals(true, schema.get("required").asBoolean());
    }
}