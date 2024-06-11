package nu.staldal.lagoon;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LagoonCLITest {

    private String propertyFilePath;
    private String sitemapFilePath;
    private long interval;
    private boolean build;
    private boolean force;

    @Before
    public void setup() {
        propertyFilePath = "path/to/property/file";
        sitemapFilePath = "path/to/sitemap/file";
        interval = 1000;
        build = true;
        force = true;
    }

    @Test
    public void testMainWithNoArgs() {
        // TODO: Implement test case
    }

    @Test
    public void testMainWithArgsBuild() {
        // TODO: Implement test case
    }

    @Test
    public void testMainWithArgsForce() {
        // TODO: Implement test case
    }

    @Test
    public void testMainWithArgsInterval() {
        // TODO: Implement test case
    }

    @Test
    public void testMainWithInvalidInterval() {
        // TODO: Implement test case
    }

    @Test
    public void testMainExceptionHandling() {
        // TODO: Implement test case
    }

    @Test
    public void testGetProperty() throws LagoonException {
        assertEquals("value", LagoonCLI.getProperty("name"));
    }

    @Test(expected = LagoonException.class)
    public void testGetPropertyNotFound() throws LagoonException {
        LagoonCLI.getProperty("invalid");
    }

    @Test
    public void testShowTimeWithMs() {
        // TODO: Implement test case
    }

    @Test
    public void testShowTimeWithS() {
        // TODO: Implement test case
    }
}