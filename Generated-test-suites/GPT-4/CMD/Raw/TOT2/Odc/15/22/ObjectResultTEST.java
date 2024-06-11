import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ObjectResultTest {
    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("TestSchema", "TestName", "TestType");
        objectResult.setCount(new AtomicLong(5));
        objectResult.setTotal(new AtomicLong(10));
    }

    @Test
    public void testConstructorWithArgs() {
        assertNotNull(objectResult);
        assertEquals("TestSchema", objectResult.getSchema());
        assertEquals("TestName", objectResult.getName());
        assertEquals("TestType", objectResult.getType());
    }

    @Test
    public void testOf() {
        ObjectStatus status = new ObjectStatus();
        status.setSchema("statusSchema");
        status.setName("statusName");
        status.setType("statusType");
        status.setCount(new AtomicLong(8));
        status.setTotal(new AtomicLong(16));

        ObjectResult result = ObjectResult.of(status);

        assertNotNull(result);
        assertEquals("statusSchema", result.getSchema());
        assertEquals("statusName", result.getName());
        assertEquals("statusType", result.getType());
        assertEquals(8L, result.getCount().get());
        assertEquals(16L, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        assertNotNull(objectResult.getSummary());
        assertEquals("TestSchema.TestName[TestType]", objectResult.getSummary());
    }

    @Test
    public void testEdgeCaseOfEmptySchemaNameAndType() {
        objectResult = new ObjectResult("", "", "");

        assertNotNull(objectResult);
        assertEquals("", objectResult.getSchema());
        assertEquals("", objectResult.getName());
        assertEquals("", objectResult.getType());
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionWithNullObjectStatus() {
        ObjectResult.of(null);
    }
}