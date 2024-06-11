import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicLong;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectStatus;
import com.oceanbase.odc.plugin.task.api.datatransfer.model.ObjectResult;

public class ObjectResultTest {

  @Test
  public void testConstructor() {
    String schema = "schema";
    String name = "name";
    String type = "type";

    ObjectResult result = new ObjectResult(schema, name, type);

    assertEquals(schema, result.getSchema());
    assertEquals(name, result.getName());
    assertEquals(type, result.getType());
  }

  @Test
  public void testOf() {
    ObjectStatus status = new ObjectStatus();
    status.setSchema("schema");
    status.setName("name");
    status.setType("type");
    status.setCount(new AtomicLong(5));
    status.setTotal(new AtomicLong(10));

    ObjectResult objectResult = ObjectResult.of(status);

    assertEquals(status.getSchema(), objectResult.getSchema());
    assertEquals(status.getName(), objectResult.getName());
    assertEquals(status.getType(), objectResult.getType());
    assertEquals(status.getCount().get(), objectResult.getCount().get());
    assertEquals(status.getTotal().get(), objectResult.getTotal().get());
  }

  @Test
  public void testGetSummary() {
    String schema = "schema";
    String name = "name";
    String type = "type";

    ObjectResult result = new ObjectResult(schema, name, type);

    String expected = schema + "." + name + "[" + type + "]";
    assertEquals(expected, result.getSummary());
  }

  @Test(expected = NullPointerException.class)
  public void testOf_WithNullObjectStatus() {
    ObjectResult.of(null);
  }
}