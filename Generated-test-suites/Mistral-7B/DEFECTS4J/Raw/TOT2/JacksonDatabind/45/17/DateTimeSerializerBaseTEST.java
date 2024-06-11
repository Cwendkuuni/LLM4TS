import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<Object>(Object.class, false, null);
        assertTrue(serializer.isEmpty(null));
        assertTrue(serializer.isEmpty(serializer, null));
        assertFalse(serializer.isEmpty(serializer, new Object()));
        assertFalse(serializer.isEmpty(serializer, new Long(0)));
    }
}