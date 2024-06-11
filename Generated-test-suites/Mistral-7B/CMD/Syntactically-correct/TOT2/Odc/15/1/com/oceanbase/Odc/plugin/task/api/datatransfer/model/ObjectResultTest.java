import static org.junit.Assert.*;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testEmptyConstructor() {
        ObjectResult objectResult = new ObjectResult();
        assertEquals("", objectResult.getSchema());
        assertEquals("", objectResult.getName());
        assertEquals("", objectResult.getType());
        assertNull(objectResult.getExportPaths());
    }

    @Test(expected = NullPointerException.class)
    public void testNullObjectStatus() {
        ObjectResult objectResult = ObjectResult.of(null);
    }

    @Test
    public void testEmptyExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(Collections.emptyList());
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testInvalidSchema() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("invalidSchema");
        String expectedSummary = "invalidSchema.null[null]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testInvalidName() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setName("invalidName");
        String expectedSummary = "null.invalidName[null]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testInvalidType() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setType("invalidType");
        String expectedSummary = "null.null[invalidType]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testNegativeCount() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setCount(new AtomicLong(-1));
        long expectedCount = -1;
        assertEquals(expectedCount, objectResult.getCount().get());
    }

    @Test
    public void testNegativeTotal() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setTotal(new AtomicLong(-1));
        long expectedTotal = -1;
        assertEquals(expectedTotal, objectResult.getTotal().get());
    }

    @Test(expected = NullPointerException.class)
    public void testGetMethodsCalledOnNullObjectResult() {
        ObjectResult nullObjectResult = null;
        nullObjectResult.getType();
    }

    @Test
    public void testGetType() {
        ObjectResult objectResult = new ObjectResult();
        String expectedType = "expectedType";
        objectResult.setType(expectedType);
        String actualType = objectResult.getType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetStatus() {
        ObjectResult objectResult = new ObjectResult();
        String expectedStatus = "expectedStatus";
        objectResult.setStatus(expectedStatus);
        String actualStatus = objectResult.getStatus();
        assertEquals(expectedStatus, actualStatus);
    }

    @Test
    public void testGetName() {
        ObjectResult objectResult = new ObjectResult();
        String expectedName = "expectedName";
        objectResult.setName(expectedName);
        String actualName = objectResult.getName();
        assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetSchema() {
        ObjectResult objectResult = new ObjectResult();
        String expectedSchema = "expectedSchema";
        objectResult.setSchema(expectedSchema);
        String actualSchema = objectResult.getSchema();
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetCount() {
        ObjectResult objectResult = new ObjectResult();
        long expectedCount = 10;
        objectResult.setCount(new AtomicLong(expectedCount));
        long actualCount = objectResult.getCount().get();
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testGetTotal() {
        ObjectResult objectResult = new ObjectResult();
        long expectedTotal = 20;
        objectResult.setTotal(new AtomicLong(expectedTotal));
        long actualTotal = objectResult.getTotal().get();
        assertEquals(expectedTotal, actualTotal);
    }
}