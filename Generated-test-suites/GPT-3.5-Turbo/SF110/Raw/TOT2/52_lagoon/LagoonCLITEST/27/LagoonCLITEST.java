import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class LagoonCLITest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(System.out);
        System.setErr(System.err);
    }

    @Test
    public void testMainValidArgs() {
        String[] args = {"property_file.xml", "1000"};
        LagoonCLI.main(args);
        assertEquals("Expected output", "Lagoon initialized successfully\n", outContent.toString());
    }

    @Test
    public void testMainInvalidArgs() {
        String[] args = {};
        LagoonCLI.main(args);
        assertEquals("Expected output", "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n", outContent.toString());
    }

    @Test
    public void testGetProperty() {
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("testProperty", "testValue");
        try {
            String result = LagoonCLI.getProperty("testProperty");
            assertEquals("testValue", result);
        } catch (LagoonException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testShowTime() {
        LagoonCLI.showTime(5000);
        assertEquals("in 5 s\n", outContent.toString());
    }

}