import static org.junit.Assert.*;

import com.oceanbase.tools.loaddump.common.model.ObjectStatus;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    private ObjectResult objectResult;
    private ObjectStatus objectStatus;

    @Before
    public void setUp() {
        objectResult = new ObjectResult("schema1", "name1", "type1");
        objectStatus = new ObjectStatus();
        objectStatus.setName("name2");
        objectStatus.setSchema("schema2");
        objectStatus.setType("type2");
        objectStatus.setCount(new AtomicLong(10));
        objectStatus.setTotal(new AtomicLong(100));
    }

    //Test constructor
    @Test
    public void testObjectResultConstruct() {
        assertNotNull("Constructor should create a new instance", objectResult);
        assertEquals("name1", objectResult.getName());
        assertEquals("schema1", objectResult.getSchema());
        assertEquals("type1", objectResult.getType());
        assertEquals(0, objectResult.getCount().get());
        assertEquals(0, objectResult.getTotal().get());
    }

    //Test static factory method
    @Test
    public void testObjectResultOf() {
        ObjectResult result = ObjectResult.of(objectStatus);
        assertNotNull("Factory should create a new instance", result);
        assertEquals("name2", result.getName());
        assertEquals("schema2", result.getSchema());
        assertEquals("type2", result.getType());
        assertEquals(10, result.getCount().get());
        assertEquals(100, result.getTotal().get());
    }

    //Test summary
    @Test
    public void testGetSummary() {
        String expectedSummary = "schema1.name1[type1]";
        assertEquals(expectedSummary, objectResult.getSummary());
    }

}