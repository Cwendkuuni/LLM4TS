import org.junit.Test;
import org.junit.Assert.*;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import java.util.ArrayList;
import java.util.List;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
        assertNull(objectResult.getExportPaths());
        assertEquals(ObjectStatus.StatusType.UNINITIALIZED, objectResult.getStatus());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setSchema("schema2");
        objectStatus.setName("name2");
        objectStatus.setType("type2");
        objectStatus.setStatus(ObjectStatus.StatusType.COMPLETED);
        objectStatus.setCount(new java.lang.Long(10));
        objectStatus.setTotal(new java.lang.Long(20));

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals("schema2", objectResult.getSchema());
        assertEquals("name2", objectResult.getName());
        assertEquals("type2", objectResult.getType());
        assertEquals(ObjectStatus.StatusType.COMPLETED, objectResult.getStatus());
        assertEquals(new Long(10), objectResult.getCount().get());
        assertEquals(new Long(20), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummaryMethod() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema3");
        objectResult.setName("name3");
        objectResult.setType("type3");

        String expectedSummary = "schema3.name3[type3]";
        String actualSummary = objectResult.getSummary();

        assertEquals(expectedSummary, actualSummary);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorWithNullSchema() {
        new ObjectResult(null, "name4", "type4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorWithNullName() {
        new ObjectResult("schema5", null, "type5");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorWithNullType() {
        new ObjectResult("schema6", "name6", null);
    }

    @Test
    public void testSetStatusMethodWithInvalidStatus() {
        ObjectResult objectResult = new ObjectResult();
        assertNotEquals(ObjectStatus.StatusType.INVALID, objectResult.getStatus());
        objectResult.setStatus(ObjectStatus.StatusType.INVALID);
        assertEquals(ObjectStatus.StatusType.INVALID, objectResult.getStatus());
    }

    @Test
    public void testCountAndTotalAtomicLongGetAndSetMethod() {
        ObjectResult objectResult = new ObjectResult();
        Long initialCount = new Long(5);
        Long initialTotal = new Long(10);

        objectResult.setCount(initialCount);
        objectResult.setTotal(initialTotal);

        Long currentCount = objectResult.getCount().get();
        Long currentTotal = objectResult.getTotal().get();

        assertEquals(initialCount, currentCount);
        assertEquals(initialTotal, currentTotal);

        Long newCount = new Long(7);
        Long newTotal = new Long(15);

        objectResult.setCount(newCount);
        objectResult.setTotal(newTotal);

        Long newCurrentCount = objectResult.getCount().get();
        Long newCurrentTotal = objectResult.getTotal().get();

        assertEquals(newCount, newCurrentCount);
        assertEquals(newTotal, newCurrentTotal);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCountAndTotalAtomicLongSetMethod() {
        ObjectResult objectResult = new ObjectResult();
        Long valueToSet = new Long(1234);

        objectResult.getCount().set(valueToSet);
    }

    @Test
    public void testExportPathsGetMethodIsJsonIgnore() {
        ObjectResult objectResult = new ObjectResult();
        assertNull(objectResult.getExportPaths());
    }

    @Test
    public void testExportPathsSetMethod() {
        ObjectResult objectResult = new ObjectResult();
        List<URL> exportPaths = new ArrayList<>();
        exportPaths.add(new URL("http://example.com/export1"));
        exportPaths.add(new URL("http://example.com/export2"));

        objectResult.setExportPaths(exportPaths);

        List<URL> actualExportPaths = objectResult.getExportPaths();
        assertEquals(2, actualExportPaths.size());
        assertEquals("http://example.com/export1", actualExportPaths.get(0).toString());
        assertEquals("http://example.com/export2", actualExportPaths.get(1).toString());
    }

}