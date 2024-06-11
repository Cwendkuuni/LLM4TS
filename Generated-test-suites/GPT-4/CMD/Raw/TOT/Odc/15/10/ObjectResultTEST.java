
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    private ObjectResult objectType;
    private ObjectStatus objectStatus;

    @Before
    public void setUp() {
        objectStatus = new ObjectStatus("test", new AtomicLong(2), new AtomicLong(5), "Type1");
        objectType = new ObjectResult("Schema1", "Name1", "Type1");
    }

    @Test
    public void testObjectResultConstructor() {
        assertEquals("Schema1", objectType.getSchema());
        assertEquals("Name1", objectType.getName());
        assertEquals("Type1", objectType.getType());
    }

    @Test
    public void testOfMethod() {
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String expectedSummary = "Schema1.Name1[Type1]";
        assertEquals(expectedSummary, objectType.getSummary());
    }
}
