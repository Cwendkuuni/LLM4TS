import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Test;

public class ObjectResultTest {

    private ObjectResult testObjectResult;

    @Before
    public void setUp() {
        testObjectResult = new ObjectResult("testSchema", "testName", "testType");
    }

    @Test
    public void testObjectResultConstructor() {
        assertNotNull(testObjectResult);
        assertEquals("testSchema", testObjectResult.getSchema());
        assertEquals("testName", testObjectResult.getName());
        assertEquals("testType", testObjectResult.getType());
    }

    @Test
    public void testObjectResultOf() {
        ObjectStatus objectStatus = new ObjectStatus("testName", new AtomicLong(5), new AtomicLong(10), "testType");
        ObjectResult result = ObjectResult.of(objectStatus);
        
        assertNotNull(result);
        assertEquals("testName", result.getName());
        assertEquals("testType", result.getType());
        assertEquals(5, result.getCount().get());
        assertEquals(10, result.getTotal().get());
    }

    @Test
    public void testGetSummary() {
        String summary = testObjectResult.getSummary();
        assertEquals("testSchema.testName[testType]", summary);
    }

    @Test
    public void testExportPaths() {
        URL testUrl = null;
        try {
            testUrl = new URL("http://www.example.com");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        testObjectResult.setExportPaths(new ArrayList<>());
        testObjectResult.getExportPaths().add(testUrl);

        assertNotNull(testObjectResult.getExportPaths());
        assertNotEquals(0, testObjectResult.getExportPaths().size());
    }

}