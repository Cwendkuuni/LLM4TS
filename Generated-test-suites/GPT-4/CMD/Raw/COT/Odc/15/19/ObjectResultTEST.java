
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {

    private ObjectResult objectResult;
    private ObjectStatus objectStatus;

    @Before
    public void setup() {
        objectStatus = new ObjectStatus("demoName", new AtomicLong(10), new AtomicLong(100), "demoStatus");
        objectResult = new ObjectResult("demoSchema", "demoName", "demoType");
    }

    @Test
    public void testOf() {
        ObjectResult expectedResult = ObjectResult.of(objectStatus);
        Assert.assertEquals("demoName", expectedResult.getName());
        Assert.assertEquals(10, expectedResult.getCount().get());
        Assert.assertEquals(100, expectedResult.getTotal().get());
        Assert.assertEquals("demoStatus", expectedResult.getStatus());
    }

    @Test
    public void testGetSummary() {
        String result = objectResult.getSummary();
        Assert.assertEquals("demoSchema.demoName[demoType]", result);
    }

    @Test
    public void testFileURLExportPath() throws MalformedURLException {
        objectResult.setExportPaths(Arrays.asList(new URL("file:///path/to/file")));
        Assert.assertTrue(objectResult.getExportPaths().get(0).toString().contains("file:///path/to/file"));
    }

    @Test(expected = MalformedURLException.class)
    public void testInvalidURLExportPath() throws MalformedURLException {
        objectResult.setExportPaths(Arrays.asList(new URL("bad://url")));
    }

    @Test
    public void testDefaultConstructor() {
        ObjectResult defaultObjectResult = new ObjectResult();
        Assert.assertNull(defaultObjectResult.getName());
        Assert.assertEquals(new AtomicLong(0).get(), defaultObjectResult.getCount().get());
        Assert.assertEquals(new AtomicLong(0).get(), defaultObjectResult.getTotal().get());
    }
}
