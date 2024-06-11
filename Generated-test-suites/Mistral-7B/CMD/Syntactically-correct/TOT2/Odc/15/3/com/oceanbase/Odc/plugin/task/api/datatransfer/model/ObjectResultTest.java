import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {

    @Test
    public void testObjectResultEmptyConstructor() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getSchema());
        assertNull(objectResult.getName());
        assertNull(objectResult.getType());
        assertEquals(ObjectStatus.STATUS_DEFAULT, objectResult.getStatus());
        assertEquals(0L, objectResult.getCount().get());
        assertEquals(0L, objectResult.getTotal().get());
    }
}