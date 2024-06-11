import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    @Test
    public void testIsEmpty() {
        // Given: An instance of DateTimeSerializerBase
        DateTimeSerializerBase<Long> dateTimeSerializer = new DateTimeSerializerBase<Long>(Long.class, false, null);

        // When: Checking if the value is empty
        assertTrue(dateTimeSerializer.isEmpty(null));
        assertTrue(dateTimeSerializer.isEmpty(new Long(0)));

        // Then: The method returns true for null and zero values
    }
}