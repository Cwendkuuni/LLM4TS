
import org.junit.Assert;
import org.junit.Test;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;

public class ObjectResultTest {

    @Test
    public void testMainConstructor() {
        String schema = "testSchema";
        String name = "testName";
        String type = "testType";
        ObjectResult result = new ObjectResult(schema, name, type);
        Assert.assertEquals(schema, result.getSchema());
        Assert.assertEquals(name, result.getName());
        Assert.assertEquals(type, result.getType());
        Assert.assertEquals(0L, result.getCount().longValue());
        Assert.assertEquals(0L, result.getTotal().longValue());
    }

    @Test
    public void testOfMethod() {
        String name = "testName";
        String schema = "testSchema";
        String status = "testStatus";
        String type = "testType";
        AtomicLong count = new AtomicLong(20);
        AtomicLong total = new AtomicLong(30);
        ObjectStatus objectStatus = new ObjectStatus(name, count, total, status);
        objectStatus.setType(type);
        objectStatus.setSchema(schema);
        ObjectResult result = ObjectResult.of(objectStatus);
        Assert.assertEquals(schema, result.getSchema());
        Assert.assertEquals(name, result.getName());
        Assert.assertEquals(type, result.getType());
        Assert.assertEquals(count.longValue(), result.getCount().longValue());
        Assert.assertEquals(total.longValue(), result.getTotal().longValue());
        Assert.assertEquals(status, result.getStatus());
    }

    @Test
    public void testGetSummary() {
        String schema = "testSchema";
        String name = "testName";
        String type = "testType";
        ObjectResult result = new ObjectResult(schema, name, type);
        Assert.assertEquals(schema + "." + name + "[" + type + "]", result.getSummary());
    }

    @Test
    public void testGetExportPaths() {
        ObjectResult result = new ObjectResult();
        Assert.assertNull(result.getExportPaths());
        List<URL> paths = new ArrayList<>();
        result.setExportPaths(paths);
        Assert.assertEquals(paths, result.getExportPaths());
    }
}
