import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ObjectResultTest {

    ObjectStatus objectStatus;
    ObjectResult objectResult;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        objectStatus = new ObjectStatus();
        objectResult = ObjectResult.of(objectStatus);
    }

    @Test
    public void testObjectResultCreation() {
        ObjectResult expectedObjectResult = new ObjectResult("schema", "name", "type");
        ObjectResult actualObjectResult = new ObjectResult("schema", "name", "type");

        assertEquals(expectedObjectResult.getSchema(), actualObjectResult.getSchema());
        assertEquals(expectedObjectResult.getName(), actualObjectResult.getName());
        assertEquals(expectedObjectResult.getType(), actualObjectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = mock(ObjectStatus.class);
        when(objectStatus.getType()).thenReturn("type");
        when(objectStatus.getStatus()).thenReturn(ObjectStatus.Status.COMPLETED);

        ObjectResult expectedObjectResult = new ObjectResult();
        ObjectResult actualObjectResult = ObjectResult.of(objectStatus);

        assertEquals(expectedObjectResult.getType(), actualObjectResult.getType());
        assertEquals(expectedObjectResult.getStatus(), actualObjectResult.getStatus());
        assertEquals(expectedObjectResult.getName(), actualObjectResult.getName());
        assertEquals(expectedObjectResult.getSchema(), actualObjectResult.getSchema());
        assertEquals(expectedObjectResult.getCount().get(), actualObjectResult.getCount().get());
        assertEquals(expectedObjectResult.getTotal().get(), actualObjectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String expectedSummary = "schema.name[type]";
        String schema = "schema";
        String name = "name";
        String type = "type";

        ObjectResult objectResult = new ObjectResult(schema, name, type);
        String actualSummary = objectResult.getSummary();

        assertEquals(expectedSummary, actualSummary);
    }
}