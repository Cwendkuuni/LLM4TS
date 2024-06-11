import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class LagoonCLITest {

    @Test
    public void testMainArgsLengthLessThan1() {
        try {
            LagoonCLI.main(new String[] {});
            fail("Expected System.exit(0)");
        } catch (SecurityException e) {
            assertEquals("Expected exit status", e.getMessage());
        }
    }
    
    @Test
    public void testMainArgsLengthGreaterThan1Build() {
        try {
            LagoonCLI.main(new String[] {"file.xml", "build"});
            fail("Expected System.exit(0)");
        } catch (SecurityException e) {
            assertEquals("Expected exit status", e.getMessage());
        }
    }
    
    @Test
    public void testMainArgsLengthGreaterThan1Force() {
        try {
            LagoonCLI.main(new String[] {"file.xml", "force"});
            fail("Expected System.exit(0)");
        } catch (SecurityException e) {
            assertEquals("Expected exit status", e.getMessage());
        }
    }
    
    @Test
    public void testMainArgsLengthGreaterThan1InvalidInterval() {
        try {
            LagoonCLI.main(new String[] {"file.xml", "abc"});
            fail("Expected System.exit(0)");
        } catch (SecurityException e) {
            assertEquals("Expected exit status", e.getMessage());
        }
    }
    
    @Test
    public void testMainArgsLengthGreaterThan1ValidInterval() {
        try {
            LagoonCLI.main(new String[] {"file.xml", "10"});
            fail("Expected infinite loop");
        } catch (InterruptedException e) {
            assertTrue(Thread.interrupted());
        }
    }
    
    @Test
    public void testMainArgsLengthGreaterThan1InvalidArgs() {
        try {
            LagoonCLI.main(new String[] {"file.xml", "abc", "def"});
            fail("Expected System.exit(0)");
        } catch (SecurityException e) {
            assertEquals("Expected exit status", e.getMessage());
        }
    }
    
    @Test
    public void testMainArgsLengthGreaterThan1ValidPropertyFile() {
        try {
            LagoonCLI.main(new String[] {"file.properties"});
            fail("Expected System.exit(0)");
        } catch (SecurityException e) {
            assertEquals("Expected exit status", e.getMessage());
        }
    }
    
    @Test
    public void testMainArgsLengthGreaterThan1InvalidPropertyFile() {
        try {
            LagoonCLI.main(new String[] {"nonexistent.properties"});
            fail("Expected System.exit(0)");
        } catch (SecurityException e) {
            assertEquals("Expected exit status", e.getMessage());
        }
    }
    
    @Test
    public void testGetPropertyExistingProperty() throws LagoonException {
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "http://example.com");
        String result = LagoonCLI.getProperty("targetURL");
        assertEquals("http://example.com", result);
    }
    
    @Test(expected = LagoonException.class)
    public void testGetPropertyNonexistentProperty() throws LagoonException {
        LagoonCLI.properties = new Properties();
        LagoonCLI.getProperty("nonexistent");
    }
    
    @Test(expected = LagoonException.class)
    public void testGetPropertyNullProperty() throws LagoonException {
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("property", null);
        LagoonCLI.getProperty("property");
    }
    
    @Test
    public void testShowTimeLessThan10Seconds() {
        LagoonCLI.showTime(5000L);
        assertTrue(System.out.toString().contains("in 5000 ms"));
    }
    
    @Test
    public void testShowTimeMoreThan10Seconds() {
        LagoonCLI.showTime(20000L);
        assertTrue(System.out.toString().contains("in 20 s"));
    }
    
    @Test
    public void testShowTimeLessThan1Second() {
        LagoonCLI.showTime(500L);
        assertTrue(System.out.toString().contains("in 500 ms"));
    }

}