import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

@RunWith(JUnitPlatform.class)
public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        String schema = "testSchema";
        String name = "testName";
        String type = "testType";
        ObjectResult objectResult = new ObjectResult(schema, name, type);

        // Test with valid inputs
        Assertions.assertNotNull(objectResult);
        Assertions.assertEquals(schema, objectResult.getSchema());
        Assertions.assertEquals(name, objectResult.getName());
        Assertions.assertEquals(type, objectResult.getType());
        // Test getters for AtomicLong fields
        Assertions.assertNotNull(objectResult.getCount());
        Assertions.assertNotNull(objectResult.getTotal());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus status = mock(ObjectStatus.class);
        String schema = "testSchema";
        String name = "testName";
        String type = "testType";
        long count = 10;
        long total = 20;
        status.setType(type);
        status.setStatus(ObjectStatus.Status.SUCCESS);
        status.setName(name);
        status.setSchema(schema);
        status.setCount(new AtomicLong(count));
        status.setTotal(new AtomicLong(total));

        ObjectResult objectResult = ObjectResult.of(status);

        // Test with valid ObjectStatus
        Assertions.assertNotNull(objectResult);
        Assertions.assertEquals(schema, objectResult.getSchema());
        Assertions.assertEquals(name, objectResult.getName());
        Assertions.assertEquals(type, objectResult.getType());
        Assertions.assertEquals(count, objectResult.getCount().get());
        Assertions.assertEquals(total, objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String schema = "testSchema";
        String name = "testName";
        String type = "testType";

        ObjectResult objectResult = new ObjectResult(schema, name, type);

        String expectedSummary = String.format("%s.%s[%s]", schema, name, type);
        String actualSummary = objectResult.getSummary();

        Assertions.assertEquals(expectedSummary, actualSummary);
    }

    @Test
    public void testEmptyConstructor() {
        ObjectResult objectResult = new ObjectResult();

        // Test with empty constructor
        Assertions.assertNull(objectResult.getName());
        Assertions.assertNull(objectResult.getSchema());
        Assertions.assertNull(objectResult.getType());
        Assertions.assertNull(objectResult.getCount());
        Assertions.assertNull(objectResult.getTotal());
    }

    @Test
    public void testJsonIgnoreGetter() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = new ArrayList<>();
        objectResult.setExportPaths(exportPaths);

        // Test with valid List<URL>
        Assertions.assertNotNull(objectResult.getExportPaths());
        Assertions.assertTrue(objectResult.getExportPaths().isEmpty());

        // Test JSON ignore annotation
        Assertions.assertNull(objectResult.getExportPaths());
    }
}