import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus.Status;

public class ObjectResultTest {

    @Test
    public void testObjectResultCreation() {
        ObjectStatus objectStatus = mock(ObjectStatus.class);
        when(objectStatus.getType()).thenReturn("type");
        when(objectStatus.getStatus()).thenReturn(Status.SUCCESS);
        when(objectStatus.getName()).thenReturn("name");
        when(objectStatus.getSchema()).thenReturn("schema");
        when(objectStatus.getCount()).thenReturn(new AtomicLong(1));
        when(objectStatus.getTotal()).thenReturn(new AtomicLong(5));

        ObjectResult objectResult = new ObjectResult();
        ObjectResult createdObjectResult = objectResult.of(objectStatus);

        Assertions.assertEquals("schema", createdObjectResult.getSchema());
        Assertions.assertEquals("name", createdObjectResult.getName());
        Assertions.assertEquals("type", createdObjectResult.getType());
        Assertions.assertEquals(Status.SUCCESS, createdObjectResult.getStatus());
        Assertions.assertEquals(1, createdObjectResult.getCount().get());
        Assertions.assertEquals(5, createdObjectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult();
        objectResult.setSchema("schema");
        objectResult.setName("name");
        objectResult.setType("type");

        String expectedSummary = "schema.name[type]";
        String actualSummary = objectResult.getSummary();

        Assertions.assertEquals(expectedSummary, actualSummary);
    }

    @Test
    public void testNullObjectStatus() {
        ObjectStatus objectStatus = null;
        Executable codeBlock = () -> new ObjectResult().of(objectStatus);

        Assertions.assertThrows(NullPointerException.class, codeBlock);
    }
}