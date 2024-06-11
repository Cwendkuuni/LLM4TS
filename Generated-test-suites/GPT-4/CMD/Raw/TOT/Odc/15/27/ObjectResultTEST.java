import org.junit.Test;
import static org.junit.Assert.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    @Test
    public void testObjectResultConstructor() {
        String schema = "testSchema";
        String name = "testName";
        String type = "testType";
        
        ObjectResult objectResult = new ObjectResult(schema, name, type);

        assertEquals(schema, objectResult.getSchema());
        assertEquals(name, objectResult.getName());
        assertEquals(type, objectResult.getType());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus();
        objectStatus.setType("testType");
        objectStatus.setStatus("testStatus");
        objectStatus.setName("testName");
        objectStatus.setSchema("testSchema");
        objectStatus.setCount(new AtomicLong(0));
        objectStatus.setTotal(new AtomicLong(0));

        ObjectResult objectResult = ObjectResult.of(objectStatus);

        assertEquals(objectStatus.getType(), objectResult.getType());
        assertEquals(objectStatus.getStatus(), objectResult.getStatus());
        assertEquals(objectStatus.getName(), objectResult.getName());
        assertEquals(objectStatus.getSchema(), objectResult.getSchema());
        assertEquals(objectStatus.getCount().get(), objectResult.getCount().get());
        assertEquals(objectStatus.getTotal().get(), objectResult.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String schema = "testSchema";
        String name = "testName";
        String type = "testType";

        ObjectResult objectResult = new ObjectResult(schema, name, type);
        String expectedSummary = schema + "." + name + "[" + type + "]";

        assertEquals(expectedSummary, objectResult.getSummary());
    }
}