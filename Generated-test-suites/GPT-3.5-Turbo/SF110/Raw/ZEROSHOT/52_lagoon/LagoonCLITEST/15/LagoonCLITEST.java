import org.junit.*;
import java.io.*;

public class LagoonCLITest {

    @Test
    public void testMainWithEmptyArgs() {
        PrintStream console = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        String[] args = new String[0];
        LagoonCLI.main(args);
        
        System.setOut(console);

        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]" + System.lineSeparator();
        Assert.assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testMainWithNoArgs() {
        PrintStream console = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        String[] args = new String[1];
        args[0] = "property_file.xml";
        LagoonCLI.main(args);

        System.setOut(console);

        String expectedOutput = "Error while initializing Lagoon:" + System.lineSeparator() + "Incorrect password" + System.lineSeparator();
        Assert.assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testMainWithInvalidInterval() {
        PrintStream console = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        String[] args = new String[2];
        args[0] = "property_file.xml";
        args[1] = "invalid_interval";
        LagoonCLI.main(args);

        System.setOut(console);

        String expectedOutput = "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]" + System.lineSeparator();
        Assert.assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testMainWithInvalidFile() {
        PrintStream console = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        String[] args = new String[1];
        args[0] = "invalid_file.xml";
        LagoonCLI.main(args);

        System.setOut(console);

        String expectedOutput = "Error while initializing Lagoon:" + System.lineSeparator() + "File not found: invalid_file.xml" + System.lineSeparator();
        Assert.assertEquals(expectedOutput, output.toString());
    }

    @Test
    public void testMainWithValidArgs() {
        PrintStream console = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        String[] args = new String[1];
        args[0] = "property_file.xml";
        LagoonCLI.main(args);

        System.setOut(console);

        String expectedOutput = "Lagoon initialized successfully" + System.lineSeparator() + "Ready" + System.lineSeparator();
        Assert.assertEquals(expectedOutput, output.toString());
    }
}