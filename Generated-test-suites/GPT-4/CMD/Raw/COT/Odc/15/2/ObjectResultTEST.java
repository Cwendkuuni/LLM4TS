
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.atomic.AtomicLong;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ObjectResultTest {

    private ObjectResult objectResult;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("schemaTest", "nameTest", "typeTest");
    }

    @Test
    public void testObjectResultConstructor() {
        assertNotNull(objectResult);
        assertEquals("schemaTest", objectResult.getSchema());
        assertEquals("nameTest", objectResult.getName());
        assertEquals("typeTest", objectResult.getType());
    }    

    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setSchema("schemaStatus");
        objectStatus.setName("nameStatus");
        objectStatus.setType("typeStatus");
        objectStatus.setCount(new AtomicLong(10));
        objectStatus.setTotal(new AtomicLong(20));

        ObjectResult result = ObjectResult.of(objectStatus);

        assertNotNull(result);
        assertEquals("schemaStatus", result.getSchema());
        assertEquals("nameStatus", result.getName());
        assertEquals("typeStatus", result.getType());
        assertEquals(10, result.getCount().get());
        assertEquals(20, result.getTotal().get());
    }

    @Test
    public void testGetSummaryMethod() {
        String summary = objectResult.getSummary();
        assertNotNull(summary);
        assertEquals("schemaTest.nameTest[typeTest]", summary);
    }

    @Test
    public void testOfMethod_NullObjectStatus(){
        ObjectResult result = ObjectResult.of(null);

        assertNull(result);
    }

    @Test
    public void testExportPaths() {
        List<URL> urlList  = new ArrayList<URL>();
        urlList.add(new URL("http://dummyurl.com"));
        objectResult.setExportPaths(urlList);

        assertNotNull(objectResult.getExportPaths());
        assertTrue(objectResult.getExportPaths().size() == 1);
        assertEquals("http://dummyurl.com", objectResult.getExportPaths().get(0).toString());
    }


}
