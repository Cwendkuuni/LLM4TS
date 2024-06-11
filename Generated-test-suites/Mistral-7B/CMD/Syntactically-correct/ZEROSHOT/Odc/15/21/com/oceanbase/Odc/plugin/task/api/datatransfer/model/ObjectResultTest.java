import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.mockito.Mockito.mock;

class ObjectResultTest {

    @Test
    void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");

        Assertions.assertEquals("testSchema", objectResult.getSchema());
        Assertions.assertEquals("testName", objectResult.getName());
        Assertions.assertEquals("testType", objectResult.getType());
        Assertions.assertNull(objectResult.getExportPaths());
        Assertions.assertEquals(ObjectStatus.Status.INIT, objectResult.getStatus());
        Assertions.assertEquals(new AtomicLong(0), objectResult.getCount());
        Assertions.assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    void testObjectResultOf() {
        ObjectStatus objectStatus = mock(ObjectStatus.class);
        ObjectResult that = new ObjectResult();
        ObjectResult result = ObjectResult.of(objectStatus);

        Mockito.when(objectStatus.getType()).thenReturn("testType");
        Mockito.when(objectStatus.getStatus()).thenReturn(ObjectStatus.Status.COMPLETED);
        Mockito.when(objectStatus.getName()).thenReturn("testName");
        Mockito.when(objectStatus.getSchema()).thenReturn("testSchema");
        Mockito.when(objectStatus.getCount()).thenReturn(new AtomicLong(10));
        Mockito.when(objectStatus.getTotal()).thenReturn(new AtomicLong(20));

        Assertions.assertEquals("testSchema", result.getSchema());
        Assertions.assertEquals("testName", result.getName());
        Assertions.assertEquals("testType", result.getType());
        Assertions.assertNull(result.getExportPaths());
        Assertions.assertEquals(ObjectStatus.Status.COMPLETED, result.getStatus());
        Assertions.assertEquals(new AtomicLong(10), result.getCount());
        Assertions.assertEquals(new AtomicLong(20), result.getTotal());
    }

    @Test
    void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("testSchema");
        objectResult.setName("testName");
        objectResult.setType("testType");

        Assertions.assertEquals("testSchema.testName[testType]", objectResult.getSummary());
    }
}