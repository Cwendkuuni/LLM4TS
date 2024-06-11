import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Before;
import org.junit.Test;
import java.net.URL;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.Assert.assertEquals;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setup() {
        objectResult = new ObjectResult("schema1", "name1", "type1");
        objectResult.setExportPaths(Collections.singletonList(new URL("http://test.com")));
    }

    @Test
    public void testObjectResultCreation() {
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("name1", objectResult.getName());
        assertEquals("type1", objectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus status = new ObjectStatus("name2", new AtomicLong(1), new AtomicLong(2), null);
        status.setType("type2");
        status.setSchema("schema2");
        ObjectResult result = ObjectResult.of(status);
        assertEquals("schema2", result.getSchema());
        assertEquals("name2", result.getName());
        assertEquals("type2", result.getType());
        assertEquals(1, result.getCount().get());
        assertEquals(2, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String summary = objectResult.getSummary();
        String expectedSummary = "schema1.name1[type1]";
        assertEquals(expectedSummary, summary);
    }
}