import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

class ObjectResultTest {

    @Test
    void testObjectResultConstruction() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        Assertions.assertEquals("schema1", objectResult.getSchema());
        Assertions.assertEquals("name1", objectResult.getName());
        Assertions.assertEquals("type1", objectResult.getType());
        Assertions.assertNull(objectResult.getExportPaths());
        Assertions.assertEquals(ObjectStatus.Status.SUCCESS, objectResult.getStatus());
        Assertions.assertEquals(new AtomicLong(0), objectResult.getCount());
        Assertions.assertEquals(new AtomicLong(0), objectResult.getTotal());
    }

    @Test
    void testObjectResultOf() {
        ObjectStatus objectStatus = mock(ObjectStatus.class);
        when(objectStatus.getType()).thenReturn("type1");
        when(objectStatus.getStatus()).thenReturn(ObjectStatus.Status.SUCCESS);
        when(objectStatus.getName()).thenReturn("name1");
        when(objectStatus.getSchema()).thenReturn("schema1");
        when(objectStatus.getCount()).thenReturn(new AtomicLong(5));
        when(objectStatus.getTotal()).thenReturn(new AtomicLong(10));

        ObjectResult objectResult = ObjectResult.of(objectStatus);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"schema\":\"schema1\",\"name\":\"name1\",\"type\":\"type1\",\"status\":\"SUCCESS\",\"count\":5,\"total\":10}";
        ObjectResult expectedObjectResult = objectMapper.readValue(json, ObjectResult.class);
        Assertions.assertEquals(expectedObjectResult.getSchema(), objectResult.getSchema());
        Assertions.assertEquals(expectedObjectResult.getName(), objectResult.getName());
        Assertions.assertEquals(expectedObjectResult.getType(), objectResult.getType());
        Assertions.assertEquals(expectedObjectResult.getStatus(), objectResult.getStatus());
        Assertions.assertEquals(expectedObjectResult.getCount(), objectResult.getCount());
        Assertions.assertEquals(expectedObjectResult.getTotal(), objectResult.getTotal());
        Assertions.assertNull(objectResult.getExportPaths());
    }

    @Test
    void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema1", "name1", "type1");
        String expectedSummary = "schema1.name1[type1]";
        Assertions.assertEquals(expectedSummary, objectResult.getSummary());
    }
}