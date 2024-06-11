import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("TestSchema", "TestName", "TestType");
    }

    @Test
    public void testConstructor() {
        assertNotNull(objectResult);
        assertEquals("TestSchema", objectResult.getSchema());
        assertEquals("TestName", objectResult.getName());
        assertEquals("TestType", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testOf() {
        ObjectStatus objectStatus = new ObjectStatus("TestName", new AtomicLong(10), new AtomicLong(20), "TestType");
        ObjectResult result = ObjectResult.of(objectStatus);
        
        assertNotNull(result);
        assertEquals("TestName", result.getName());
        assertEquals(10, result.getCount().get());
        assertEquals(20, result.getTotal().get());
        assertEquals("TestType", result.getType());
        assertEquals("", result.getSchema());
        assertEquals(ObjectStatus.Status.PENDING, result.getStatus());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        assertEquals("TestSchema.TestName[TestType]", summary);
    }
}