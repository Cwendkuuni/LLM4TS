import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Assert;
import org.junit.Test;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        String type = "type";
        String schema = "schema";
        String name = "name";
        
        ObjectResult objectResult = new ObjectResult(schema, name, type);
        
        Assert.assertEquals(schema, objectResult.getSchema());
        Assert.assertEquals(name, objectResult.getName());
        Assert.assertEquals(type, objectResult.getType());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullParams() {
        new ObjectResult(null, null, null);
    }

    @Test
    public void testOf() {
        ObjectStatus status = new ObjectStatus();
        status.setName("name");
        status.setType("type");
        status.setSchema("schema");
        // ... set other fields ...
        ObjectResult result = ObjectResult.of(status);
        
        Assert.assertEquals(status.getName(), result.getName());
        Assert.assertEquals(status.getType(), result.getType());
        Assert.assertEquals(status.getSchema(), result.getSchema());
    }

    @Test(expected = NullPointerException.class)
    public void testOfWithNullParam() {
        ObjectResult.of(null);
    }

    @Test
    public void testGetSummary() {
        String schema = "schema";
        String name = "name";
        String type = "type";
        
        ObjectResult objectResult = new ObjectResult(schema, name, type);
        String expectedSummary = schema + "." + name + "[" + type + "]";
        
        Assert.assertEquals(expectedSummary, objectResult.getSummary());
    }

    @Test(expected = NullPointerException.class)
    public void testGetSummaryWithNullData() {
        new ObjectResult(null, null, null).getSummary();
    }

}