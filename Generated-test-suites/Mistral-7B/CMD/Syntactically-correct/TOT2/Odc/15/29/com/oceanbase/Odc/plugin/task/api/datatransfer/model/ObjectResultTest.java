import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
        assertEquals("schema", objectResult.getSchema());
        assertNotNull(objectResult.getCount());
        assertNotNull(objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus();
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", objectResult.getSummary());
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultConstructorWithNullSchema() {
        new ObjectResult(null, "name", "type");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorWithEmptySchema() {
        new ObjectResult("", "name", "type");
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultConstructorWithNullName() {
        new ObjectResult("schema", null, "type");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorWithEmptyName() {
        new ObjectResult("schema", "", "type");
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultConstructorWithNullType() {
        new ObjectResult("schema", "name", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorWithEmptyType() {
        new ObjectResult("schema", "name", "");
    }

    @Test(expected = NullPointerException.class)
    public void testObjectResultOfWithNullObjectStatus() {
        ObjectResult.of(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultOfWithInvalidObjectStatus() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setType("invalidType");
        ObjectResult.of(objectStatus);
    }
}