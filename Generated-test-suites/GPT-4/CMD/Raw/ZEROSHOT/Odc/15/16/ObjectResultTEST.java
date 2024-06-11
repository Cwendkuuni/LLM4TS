
import org.junit.Assert;
import org.junit.Test;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.Assert.assertEquals;

public class ObjectResultTest {

    @Test
    public void testConstructor() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        Assert.assertNotNull(objectResult);
        assertEquals("schema", objectResult.getSchema());
        assertEquals("name", objectResult.getName());
        assertEquals("type", objectResult.getType());
    }
    
    @Test
    public void testOf() {
        ObjectStatus that = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), null);
        that.setType("type");
        that.setSchema("schema");
        ObjectResult objectResult = ObjectResult.of(that);
        assertEquals(that.getSchema(), objectResult.getSchema());
        assertEquals(that.getName(), objectResult.getName());
        assertEquals(that.getType(), objectResult.getType());
        assertEquals(that.getCount().get(), objectResult.getCount().get());
        assertEquals(that.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        ObjectResult objectResult = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", objectResult.getSummary());
    }

    @Test
    public void testGetSetExportPaths() {
        List<URL> urls = new ArrayList<>();
        urls.add(new URL("http://example.com"));
        ObjectResult objectResult = new ObjectResult();
        objectResult.setExportPaths(urls);
        assertEquals(urls, objectResult.getExportPaths());
    }
}
