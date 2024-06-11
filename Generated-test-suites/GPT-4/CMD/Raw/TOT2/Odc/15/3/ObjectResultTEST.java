
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("TestSchema", "TestName", "TestType");
        assertEquals("TestSchema", objectResult.getSchema());
        assertEquals("TestName", objectResult.getName());
        assertEquals("TestType", objectResult.getType());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("TestName", new AtomicLong(0), new AtomicLong(0), null);
        objectStatus.setType("TestType");
        objectStatus.setSchema("TestSchema");

        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertEquals(objectResult.getType(), objectStatus.getType());
        assertEquals(objectResult.getSchema(), objectStatus.getSchema());
        assertEquals(objectResult.getName(), objectStatus.getName());
        assertEquals(objectResult.getCount().get(), objectStatus.getCount().get());
        assertEquals(objectResult.getTotal().get(), objectStatus.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("TestSchema", "TestName", "TestType");
        assertEquals("TestSchema.TestName[TestType]", objectResult.getSummary());
    }
}
