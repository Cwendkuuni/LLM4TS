
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Arrays;

public class ObjectResultTest {
    
    @Test
    public void ObjectResultConstructorTest() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
    }

    @Test
    public void ObjectResultOfTest() {
        ObjectStatus status = new ObjectStatus("name", new AtomicLong(0), new AtomicLong(0), null);
        status.setType("type");
        status.setSchema("schema");
        ObjectResult result = ObjectResult.of(status);
        assertEquals("schema", result.getSchema());
        assertEquals("name", result.getName());
        assertEquals("type", result.getType());
    }

    @Test
    public void getSummaryTest() {
        ObjectResult result = new ObjectResult("schema", "name", "type");
        assertEquals("schema.name[type]", result.getSummary());
    }
    
    // you might want to add more edge case tests like NullObjectTest, EmptySchemaTest... whatever you think could make your class fail.
}
