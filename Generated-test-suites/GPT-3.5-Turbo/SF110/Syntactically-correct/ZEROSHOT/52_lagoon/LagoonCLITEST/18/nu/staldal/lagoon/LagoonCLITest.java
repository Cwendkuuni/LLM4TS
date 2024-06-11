import org.junit.*;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.Properties;

public class LagoonCLITest {

    @Test
    public void testMain_noArgs() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.main(new String[]{});

        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n";
        Assert.assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testMain_build() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        LagoonCLI.main(new String[]{"propertyFile", "build"});

        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n";
        Assert.assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testMain_force() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        LagoonCLI.main(new String[]{"propertyFile", "force"});

        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n";
        Assert.assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testMain_invalidInterval() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        LagoonCLI.main(new String[]{"propertyFile", "invalid"});

        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]\n";
        Assert.assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testMain_invalidPropertyFile() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        LagoonCLI.main(new String[]{"invalidPropertyFile"});

        String expectedOutput = "Error while initializing Lagoon:\nFile not found: invalidPropertyFile\n";
        Assert.assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testMain_unknownException() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        LagoonCLI.main(new String[]{"propertyFile"});

        String expectedOutput = "Error while initializing Lagoon:\nI/O error: java.io.IOException\n";
        Assert.assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testGetProperty_existingProperty() throws LagoonException {
        Properties properties = new Properties();
        properties.setProperty("targetURL", "http://example.com");
        LagoonCLI.setProperties(properties);

        String result = LagoonCLI.getProperty("targetURL");

        Assert.assertEquals("http://example.com", result);
    }

    @Test(expected = LagoonException.class)
    public void testGetProperty_missingProperty() throws LagoonException {
        Properties properties = new Properties();
        LagoonCLI.setProperties(properties);

        LagoonCLI.getProperty("targetURL");
    }

    @Test
    public void testShowTime_lessThan10Seconds() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.showTime(5000);

        String expectedOutput = "in 5000 ms\n";
        Assert.assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void testShowTime_moreThan10Seconds() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        LagoonCLI.showTime(20000);

        String expectedOutput = "in 20 s\n";
        Assert.assertEquals(expectedOutput, outContent.toString());
    }
}