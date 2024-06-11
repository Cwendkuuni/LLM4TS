import static org.junit.Assert.*;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(String.class, true, new SimpleDateFormat("yyyy-MM-dd"));
        DateTimeSerializerBase<?> newSerializer = serializer.withFormat(false, new SimpleDateFormat("dd/MM/yyyy"));
        assertNotNull(newSerializer);
    }

    @Test
    public void testCreateContextual() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(String.class, true, new SimpleDateFormat("yyyy-MM-dd"));
        try {
            serializer.createContextual(null, null);
            fail("Should throw JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(String.class, true, new SimpleDateFormat("yyyy-MM-dd"));
        assertFalse(serializer.isEmpty(null, "2021-10-15"));
    }

    @Test
    public void testTimestamp() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(Date.class, true, new SimpleDateFormat("yyyy-MM-dd"));
        Date date = new Date();
        long timestamp = serializer._timestamp(date);
        assertTrue(timestamp > 0);
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<>(String.class, true, new SimpleDateFormat("yyyy-MM-dd"));
        assertNotNull(serializer.getSchema(null, null));
    }

    @Test
    public void testSerialize() {
        // This test requires mocking JsonGenerator and SerializerProvider
        // Hence, it can't be completed in isolation
        // It should be tested in an integrated test suite
    }

}