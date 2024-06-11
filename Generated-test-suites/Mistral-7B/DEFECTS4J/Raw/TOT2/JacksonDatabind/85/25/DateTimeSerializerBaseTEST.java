import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<String> serializer = new MyDateTimeSerializerBase();
        DateTimeSerializerBase<String> serializedSerializer = serializer.withFormat(true, null);

        assertEquals(serializer.getClass(), serializedSerializer.getClass());
    }
}