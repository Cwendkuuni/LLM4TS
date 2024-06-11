
import org.junit.*;
import static org.junit.Assert.*;

public class LagoonCLITest {

    @Test
    public void testMain_NoArgs_PrintsSyntaxMessage() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[]{});

        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testMain_BuildArg_BuildsWebsite() {
        LagoonProcessor mockProcessor = Mockito.mock(LagoonProcessor.class);
        LagoonCLI mockCLI = Mockito.spy(new LagoonCLI());
        Mockito.when(mockCLI.createProcessor(Mockito.anyString())).thenReturn(mockProcessor);

        LagoonCLI.main(new String[]{"file.xml", "build"});

        Mockito.verify(mockProcessor).build(false);
    }

    @Test
    public void testMain_ForceArg_ForceBuildsWebsite() {
        LagoonProcessor mockProcessor = Mockito.mock(LagoonProcessor.class);
        LagoonCLI mockCLI = Mockito.spy(new LagoonCLI());
        Mockito.when(mockCLI.createProcessor(Mockito.anyString())).thenReturn(mockProcessor);

        LagoonCLI.main(new String[]{"file.xml", "force"});

        Mockito.verify(mockProcessor).build(true);
    }

    @Test
    public void testMain_InvalidIntervalArg_PrintsSyntaxMessage() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[]{"file.xml", "invalidInterval"});

        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]";
        assertEquals(expectedOutput, outContent.toString());
    }

    // Additional test cases for the main method can be added for each conditional branch

    @Test
    public void testGetProperty_ValidProperty_ReturnsPropertyValue() throws LagoonException {
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "http://example.com");

        String result = LagoonCLI.getProperty("targetURL");

        assertEquals("http://example.com", result);
    }

    @Test(expected = LagoonException.class)
    public void testGetProperty_InvalidProperty_ThrowsLagoonException() throws LagoonException {
        LagoonCLI.getProperty("invalidProperty");
    }

    @Test
    public void testShowTime_TimeLessThan10Seconds_PrintsMilliseconds() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.showTime(5000);

        String expectedOutput = "in 5000 ms";
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testShowTime_TimeGreaterThan10Seconds_PrintsSeconds() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.showTime(15000);

        String expectedOutput = "in 15 s";
        assertEquals(expectedOutput, outContent.toString());
    }

    // Additional test cases for each method in the LagoonCLI class can be added

}

