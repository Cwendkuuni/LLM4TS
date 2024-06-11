import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {

    @Test
    public void constructorTest() {
         ObjectResult objectResult = new ObjectResult("schema", "name", "type");
         assertNotNull(objectResult);
    }

    @Test
    public void ofTest() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setName("name");
        objectStatus.setType("type");
        ObjectResult objectResult = ObjectResult.of(objectStatus);
        assertNotNull(objectResult);
    }

    @Test
    public void getSummaryTest() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", objectResult.getSummary());
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNullArgsTest() {
        new ObjectResult(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void ofWithNullArgTest() {
        ObjectResult.of(null);
    }

    @Test(expected = NullPointerException.class)
    public void getSummaryWithNullArgsTest() {
        ObjectResult objectResult = new ObjectResult(null, null, null);
        objectResult.getSummary();
    }
}