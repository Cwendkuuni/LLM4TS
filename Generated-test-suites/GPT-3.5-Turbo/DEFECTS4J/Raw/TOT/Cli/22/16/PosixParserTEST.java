import static org.junit.Assert.*;

import org.junit.Test;
import org.apache.commons.cli.*;

public class PosixParserTest {

    @Test
    public void testFlatten_OptionsAndArgumentsExist_ReturnsFlattenedArguments() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-a", "-b", "value"};

        PosixParser parser = new PosixParser();

        // Act
        String[] flattened = parser.flatten(options, arguments, false);

        // Assert
        assertEquals(3, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("value", flattened[2]);
    }

    @Test
    public void testFlatten_StopAtNonOptionEnabled_StopFlatteningAtNonOptionValue() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-a", "-b", "value"};

        PosixParser parser = new PosixParser();

        // Act
        String[] flattened = parser.flatten(options, arguments, true);

        // Assert
        assertEquals(3, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-b", flattened[1]);
        assertEquals("value", flattened[2]);
    }

    @Test
    public void testFlatten_LongOptionWithoutEqualsSign_AddsOptionToFlattenedArguments() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"--a", "--b=value"};

        PosixParser parser = new PosixParser();

        // Act
        String[] flattened = parser.flatten(options, arguments, false);

        // Assert
        assertEquals(4, flattened.length);
        assertEquals("--a", flattened[0]);
        assertEquals("--b", flattened[2]);
        assertEquals("value", flattened[3]);
    }

    @Test
    public void testFlatten_LongOptionWithEqualsSign_AddsOptionValueToFlattenedArguments() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"--a=value"};

        PosixParser parser = new PosixParser();

        // Act
        String[] flattened = parser.flatten(options, arguments, false);

        // Assert
        assertEquals(2, flattened.length);
        assertEquals("--a", flattened[0]);
        assertEquals("value", flattened[1]);
    }

    @Test
    public void testFlatten_SingleHyphenToken_AddsTokenToFlattenedArguments() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-"};

        PosixParser parser = new PosixParser();

        // Act
        String[] flattened = parser.flatten(options, arguments, false);

        // Assert
        assertEquals(1, flattened.length);
        assertEquals("-", flattened[0]);
    }

    @Test
    public void testFlatten_ShortOption_NotExistingOptionAndStopAtNonOptionEnabled_AddsTokenToFlattenedArguments() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-c"};

        PosixParser parser = new PosixParser();

        // Act
        String[] flattened = parser.flatten(options, arguments, true);

        // Assert
        assertEquals(1, flattened.length);
        assertEquals("-c", flattened[0]);
    }

    @Test
    public void testFlatten_ShortOption_NotExistingOptionAndStopAtNonOptionDisabled_IgnoresToken() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-c"};

        PosixParser parser = new PosixParser();

        // Act
        String[] flattened = parser.flatten(options, arguments, false);

        // Assert
        assertEquals(0, flattened.length);
    }

    @Test
    public void testFlatten_ShortOption_ValidOptionNoArgument_AddsTokenToFlattenedArguments() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {"-a"};

        PosixParser parser = new PosixParser();

        // Act
        String[] flattened = parser.flatten(options, arguments, false);

        // Assert
        assertEquals(1, flattened.length);
        assertEquals("-a", flattened[0]);
    }

    @Test
    public void testFlatten_ShortOption_ValidOptionWithArgument_AddsOptionAndArgumentToFlattenedArguments() {
        // Arrange
        Options options = new Options();
        options.addOption("a", true, "Option a");

        String[] arguments = {"-a", "value"};

        PosixParser parser = new PosixParser();

        // Act
        String[] flattened = parser.flatten(options, arguments, false);

        // Assert
        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value", flattened[1]);
    }

    @Test
    public void testFlatten_UnknownOptionsExist_AddsOptionsToFlattenedArguments() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option a");
        options.addOption("b", true, "Option b");

        String[] arguments = {"-a", "-c"};

        PosixParser parser = new PosixParser();

        // Act
        String[] flattened = parser.flatten(options, arguments, false);

        // Assert
        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("-c", flattened[1]);
    }

    @Test
    public void testFlatten_InvalidLongOption_WithStopAtNonOptionEnabled_AddsTokenToFlattenedArguments() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {"--b"};

        PosixParser parser = new PosixParser();

        // Act
        String[] flattened = parser.flatten(options, arguments, true);

        // Assert
        assertEquals(1, flattened.length);
        assertEquals("--b", flattened[0]);
    }

    @Test
    public void testFlatten_InvalidShortOption_WithStopAtNonOptionEnabled_AddsTokenToFlattenedArguments() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {"-b"};

        PosixParser parser = new PosixParser();

        // Act
        String[] flattened = parser.flatten(options, arguments, true);

        // Assert
        assertEquals(1, flattened.length);
        assertEquals("-b", flattened[0]);
    }

    @Test
    public void testFlatten_StopAtNonOptionEnabled_ContinuesFlatteningAfterNonOptionToken() {
        // Arrange
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {"-a", "value", "nonOption", "-b"};

        PosixParser parser = new PosixParser();

        // Act
        String[] flattened = parser.flatten(options, arguments, true);

        // Assert
        assertEquals(4, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value", flattened[1]);
        assertEquals("nonOption", flattened[2]);
        assertEquals("-b", flattened[3]);
    }
}