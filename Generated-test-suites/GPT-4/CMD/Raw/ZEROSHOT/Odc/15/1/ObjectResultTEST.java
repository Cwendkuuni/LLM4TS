
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    @Test
    public void testConstructorWithParams() {
        String schema = "Test_Schema";
        String name = "Test_Name";
        String type = "Test_Type";
        ObjectResult objectResult = new ObjectResult(schema, name, type);

        Assert.assertEquals(schema, objectResult.getSchema());
        Assert.assertEquals(name, objectResult.getName());
        Assert.assertEquals(type, objectResult.getType());
    }

    @Test
    public void testOf() {
        String schema = "Test_Schema";
        String name = "Test_Name";
        String type = "Test_Type";
        AtomicLong count = new AtomicLong(5);
        AtomicLong total = new AtomicLong(10);
        ObjectStatus objectStatus = new ObjectStatus(name, count, total, null);
        objectStatus.setType(type);
        objectStatus.setSchema(schema);

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        Assert.assertEquals(schema, objectResult.getSchema());
        Assert.assertEquals(name, objectResult.getName());
        Assert.assertEquals(type, objectResult.getType());
        Assert.assertEquals(count.get(), objectResult.getCount().get());
        Assert.assertEquals(total.get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String schema = "Test_Schema";
        String name = "Test_Name";
        String type = "Test_Type";
        ObjectResult objectResult = new ObjectResult(schema, name, type);

        String expectedSummary = schema + '.' + name + '[' + type + ']';
        Assert.assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test
    public void testExportPaths() {
        ObjectResult objectResult = new ObjectResult();
        ArrayList<URL> paths = new ArrayList<>();
        objectResult.setExportPaths(paths);

        Assert.assertEquals(paths, objectResult.getExportPaths());
    }
}
