import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    @Test
    public void test_timestamp() throws Exception {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
        Long value1 = null;
        Long value2 = 0L;
        Long value3 = 1516041620000L; // Unix timestamp for 2017-12-06 14:30:20

        assertEquals(0, serializer._timestamp(value1));
        assertEquals(0, serializer._timestamp(value2));
        assertEquals(value3, serializer._timestamp(value3));
    }
}