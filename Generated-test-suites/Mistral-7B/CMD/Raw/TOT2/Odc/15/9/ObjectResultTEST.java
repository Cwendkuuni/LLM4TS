import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        // ...
    }

    @Test
    public void testObjectResultNullConstructor() {
        assertThrows(NullPointerException.class, () -> new ObjectResult(null));
    }

    @Test
    public void testObjectResultEmptyConstructor() {
        ObjectResult objectResult = new ObjectResult();
        assertEquals("", objectResult.getSchema());
        assertEquals("", objectResult.getName());
        assertEquals("", objectResult.getType());
        assertEquals(ObjectStatus.UNDEFINED, objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultEmptyStringConstructor() {
        ObjectResult objectResult = new ObjectResult("", "", "");
        assertEquals("", objectResult.getSchema());
        assertEquals("", objectResult.getName());
        assertEquals("", objectResult.getType());
        assertEquals(ObjectStatus.UNDEFINED, objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultLongStringConstructor() {
        final String longString = "A".repeat(100);
        ObjectResult objectResult = new ObjectResult(longString, longString, longString);
        assertEquals(longString, objectResult.getSchema());
        assertEquals(longString, objectResult.getName());
        assertEquals(longString, objectResult.getType());
        assertEquals(ObjectStatus.UNDEFINED, objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testObjectResultSettersNullValues() {
        ObjectResult objectResult = new ObjectResult();

        assertThrows(NullPointerException.class, () -> objectResult.setCount(null));
        assertThrows(NullPointerException.class, () -> objectResult.setTotal(null));
        assertThrows(NullPointerException.class, () -> objectResult.setName(null));
        assertThrows(NullPointerException.class, () -> objectResult.setSchema(null));
        assertThrows(NullPointerException.class, () -> objectResult.setType(null));
        assertThrows(NullPointerException.class, () -> objectResult.setStatus(null));
    }

    @Test
    public void testObjectResultSettersEmptyStrings() {
        ObjectResult objectResult = new ObjectResult();

        ObjectResult result = ObjectResult.of(ObjectStatus.UNDEFINED);
        result.setType("");
        result.setName("");
        result.setSchema("");
        assertEquals("", result.getType());
        assertEquals("", result.getName());
        assertEquals("", result.getSchema());
        assertEquals(ObjectStatus.UNDEFINED, result.getStatus());
        assertEquals(0, result.getCount().get());
        assertEquals(0, result.getTotal().get());
    }

    @Test
    public void testObjectResultSettersLongStrings() {
        final String longString = "A".repeat(100);
        ObjectResult objectResult = new ObjectResult();

        objectResult.setType(longString);
        assertEquals(longString, objectResult.getType());

        objectResult.setName(longString);
        assertEquals(longString, objectResult.getName());

        objectResult.setSchema(longString);
        assertEquals(longString, objectResult.getSchema());
    }

    @Test
    public void testObjectResultSetCountAndTotal() {
        ObjectResult objectResult = new ObjectResult();
        AtomicLong count = new AtomicLong(10);
        AtomicLong total = new AtomicLong(20);

        objectResult.setCount(count);
        objectResult.setTotal(total);

        assertEquals(count, objectResult.getCount());
        assertEquals(total, objectResult.getTotal());

        count.set(5);
        total.set(15);
        objectResult.setCount(count);
        objectResult.setTotal(total);

        assertEquals(count, objectResult.getCount());
        assertEquals(total, objectResult.getTotal());
    }
}