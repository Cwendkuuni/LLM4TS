
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class LagoonCLITest {

    @Test
    public void testMain_emptyArgs() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[]{});

        assertEquals("Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());
    }

    @Test
    public void testGetProperty_validProperty() {
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("testKey", "testValue");

        String result = LagoonCLI.getProperty("testKey");

        assertEquals("testValue", result);
    }

    @Test
    public void testShowTime_lessThan10Seconds() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.showTime(5000);

        assertEquals("in 5000 ms\n", outContent.toString());
    }
}
