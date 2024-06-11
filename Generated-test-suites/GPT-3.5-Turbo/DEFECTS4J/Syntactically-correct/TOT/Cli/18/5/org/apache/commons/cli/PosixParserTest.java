import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        // Arrange
        String[] arguments = {"-a", "value1", "nonOption"};
        options.addOption(Option.builder("a").argName("arg").build());

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
    }

    @Test
    public void testFlatten_NotStopAtNonOption() {
        // Arrange
        String[] arguments = {"-a", "value1", "nonOption"};
        options.addOption(Option.builder("a").argName("arg").build());

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("nonOption", result[2]);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        // Arrange
        String[] arguments = {"--option=value"};
        options.addOption(Option.builder("option").hasArg().argName("arg").build());

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("--option", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlatten_LongOptionWithoutValue() {
        // Arrange
        String[] arguments = {"--option"};
        options.addOption(Option.builder("option").build());

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("--option", result[0]);
    }

    @Test
    public void testFlatten_HyphenToken() {
        // Arrange
        String[] arguments = {"-"};
        options.addOption(Option.builder("a").argName("arg").build());

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_ShortOptionWithValue() {
        // Arrange
        String[] arguments = {"-a", "value1"};
        options.addOption(Option.builder("a").hasArg().argName("arg").build());

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
    }

    @Test
    public void testFlatten_ShortOptionWithoutValue() {
        // Arrange
        String[] arguments = {"-b"};
        options.addOption(Option.builder("b").build());

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("-b", result[0]);
    }

    @Test
    public void testFlatten_InvalidOption_StopAtNonOption() {
        // Arrange
        String[] arguments = {"-a", "nonOption"};
        options.addOption(Option.builder("b").build());

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("nonOption", result[1]);
    }

    @Test
    public void testFlatten_InvalidOption_NotStopAtNonOption() {
        // Arrange
        String[] arguments = {"-a", "nonOption"};
        options.addOption(Option.builder("b").build());

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("nonOption", result[1]);
        assertEquals("--", result[2]);
    }
}