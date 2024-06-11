
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {
    
    @Test
    public void ObjectResult_constructor_test() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");
        
        Assert.assertEquals("schema1", result.getSchema());
        Assert.assertEquals("name1", result.getName());
        Assert.assertEquals("type1", result.getType());
        Assert.assertEquals(0, result.getCount().get());
        Assert.assertEquals(0, result.getTotal().get());
    }

    @Test
    public void ObjectResult_of_test() {
        ObjectStatus status = new ObjectStatus("name1", new AtomicLong(1), new AtomicLong(10), "completed");
        status.setType("type1");
        status.setSchema("schema1");

        ObjectResult result = ObjectResult.of(status);

        Assert.assertEquals("schema1", result.getSchema());
        Assert.assertEquals("name1", result.getName());
        Assert.assertEquals("type1", result.getType());
        Assert.assertEquals(1, result.getCount().get());
        Assert.assertEquals(10, result.getTotal().get());
        Assert.assertEquals("completed", result.getStatus());
    }
    
    @Test
    public void ObjectResult_getSummary_test() {
        ObjectResult result = new ObjectResult("schema1", "name1", "type1");

        String summary = result.getSummary();

        Assert.assertEquals("schema1.name1[type1]", summary);
    }
}
