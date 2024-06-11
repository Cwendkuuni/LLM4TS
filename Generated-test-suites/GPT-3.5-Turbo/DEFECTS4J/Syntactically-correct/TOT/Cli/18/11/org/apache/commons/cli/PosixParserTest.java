import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {

    @Test
    public void testFlatten_validOptions() {
        // Arrange
        Options options = new Options();
        options.addOption(Option.builder("a").required().build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"-a", "-b", "value"};

        PosixParser parser = new PosixParser();

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        assertEquals("Expected 3 tokens", 3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        // Arrange
        Options options = new Options();
        options.addOption(Option.builder("a").required().build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"-a", "-b", "value", "nonOption"};

        PosixParser parser = new PosixParser();

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertEquals("Expected 4 tokens", 4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("value", result[2]);
        assertEquals("nonOption", result[3]);
    }

    @Test
    public void testFlatten_burstToken() {
        // Arrange
        Options options = new Options();
        options.addOption(Option.builder("a").required().build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"-abc"};

        PosixParser parser = new PosixParser();

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        assertEquals("Expected 3 tokens", 3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("-c", result[2]);
    }

    @Test
    public void testFlatten_invalidOption() {
        // Arrange
        Options options = new Options();
        options.addOption(Option.builder("a").required().build());
        options.addOption(Option.builder("b").hasArg().build());
        options.addOption(Option.builder("c").build());

        String[] arguments = {"-d"};

        PosixParser parser = new PosixParser();

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        assertEquals("Expected 1 token", 1, result.length);
        assertEquals("-d", result[0]);
    }
}