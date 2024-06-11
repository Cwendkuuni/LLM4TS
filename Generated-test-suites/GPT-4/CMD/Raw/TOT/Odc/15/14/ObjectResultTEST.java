import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.oceanbase.tools.loaddump.common.model.ObjectStatus;


public class ObjectResultTest {
    
    private ObjectResult testResult;
    private String testSchema = "testSchema";
    private String testName = "testName";
    private String testType = "testType";

    @Before
    public void setup() {
        testResult = new ObjectResult(testSchema, testName, testType);
    }

    @Test
    public void test_constructor() {
        assertEquals(testName, testResult.getName());
        assertEquals(testSchema, testResult.getSchema());
        assertEquals(testType, testResult.getType());
        assertEquals(0, testResult.getCount().get());
        assertEquals(0, testResult.getTotal().get());
    }

    @Test
    public void test_of() {
        ObjectStatus status = new ObjectStatus(testName, new AtomicLong(10), new AtomicLong(20), null);
        status.setType(testType);
        status.setSchema(testSchema);

        ObjectResult result = ObjectResult.of(status);
        assertEquals(testName, result.getName());
        assertEquals(testSchema, result.getSchema());
        assertEquals(testType, result.getType());
        assertEquals(10, result.getCount().get());
        assertEquals(20, result.getTotal().get());
    }

    @Test
    public void test_getSummary() {
        String expected = testSchema + "." + testName + "[" + testType + "]";
        assertEquals(expected, testResult.getSummary());
    }

 }