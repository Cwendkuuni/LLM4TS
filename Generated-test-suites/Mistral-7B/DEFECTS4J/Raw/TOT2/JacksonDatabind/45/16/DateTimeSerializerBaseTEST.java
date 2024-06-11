import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class DateTimeSerializerBaseTest {

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, false, null);
        assertTrue(serializer.isEmpty(null));
        assertTrue(serializer.isEmpty(serializer, null));
        assertTrue(serializer.isEmpty(serializer, new Object()));
        assertTrue(serializer.isEmpty(serializer, new Object(){long timestamp = 0;}) );
    }
}