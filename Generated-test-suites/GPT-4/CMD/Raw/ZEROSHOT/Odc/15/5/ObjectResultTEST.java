
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Test;
import org.junit.Assert;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ObjectResultTest {
    @Test
    public void testConstructorWithParameters() {
        ObjectResult obj = new ObjectResult("schema1", "name1", "type1");
        Assert.assertEquals("schema1", obj.getSchema());
        Assert.assertEquals("name1", obj.getName());
        Assert.assertEquals("type1", obj.getType());
        Assert.assertEquals(0, obj.getTotal().get());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus that = new ObjectStatus();
        that.setStatus("status");
        that.setName("name");
        that.setType("type");
        that.setSchema("schema");
        that.setCount(new AtomicLong(1));
        that.setTotal(new AtomicLong(2));

        ObjectResult result = ObjectResult.of(that);

        Assert.assertEquals("name", result.getName());
        Assert.assertEquals("type", result.getType());
        Assert.assertEquals("schema", result.getSchema());
        Assert.assertEquals("status", result.getStatus());
        Assert.assertEquals(1, result.getCount().get());
        Assert.assertEquals(2, result.getTotal().get());
    }

    @Test
    public void testGetSummaryMethod() {
        ObjectResult obj = new ObjectResult("schema1", "name1", "type1");
        String summary = obj.getSummary();

        Assert.assertEquals("schema1.name1[type1]", summary);
    }

    @Test
    public void testExportPaths() {
        List<URL> paths = new ArrayList<>();
        ObjectResult obj = new ObjectResult();

        obj.setExportPaths(paths);
        Assert.assertEquals(paths, obj.getExportPaths());
    }
}
