package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.Before;

public class GnuParserTest {

    private GnuParser parser;

    @Before
    public void setUp() {
        parser = new GnuParser();
    }

    @Test
    public void testFlatten_OptionExistReturnFlattenedArguments() {
        // Arrange
        Options options = new Options();
        options.addOption("f", "foo", false, "Foo option");
        String[] arguments = {"-f"};

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }

    @Test
    public void testFlatten_OptionDoesNotExistReturnFlattenedArguments() {
        // Arrange
        Options options = new Options();
        String[] arguments = {"-x"};

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertEquals(1, result.length);
        assertEquals("-x", result[0]);
    }

    @Test
    public void testFlatten_StopAtNonOptionTrue_ReturnFlattenedArgumentsWithoutNonOption() {
        // Arrange
        Options options = new Options();
        options.addOption("f", "foo", false, "Foo option");
        String[] arguments = {"-f", "file.txt", "-x"};

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("file.txt", result[1]);
    }

    @Test
    public void testFlatten_StopAtNonOptionFalse_ReturnFlattenedArgumentsWithNonOption() {
        // Arrange
        Options options = new Options();
        options.addOption("f", "foo", false, "Foo option");
        String[] arguments = {"-f", "file.txt", "-x"};

        // Act
        String[] result = parser.flatten(options, arguments, false);

        // Assert
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("file.txt", result[1]);
        assertEquals("-x", result[2]);
    }

    @Test
    public void testFlatten_ArgumentListContainsStandardHyphen_ReturnFlattenedArgumentsWithHyphen() {
        // Arrange
        Options options = new Options();
        String[] arguments = {"-"};

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_ArgumentListContainsDoubleHyphen_ReturnFlattenedArgumentsWithDoubleHyphen() {
        // Arrange
        Options options = new Options();
        String[] arguments = {"--"};

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertEquals(1, result.length);
        assertEquals("--", result[0]);
    }

    @Test
    public void testFlatten_ArgumentListContainsOptionValue_ReturnFlattenedArgumentsWithOptionValue() {
        // Arrange
        Options options = new Options();
        options.addOption("D", false, "Set system property");
        String[] arguments = {"-Dproperty=value"};

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertEquals(2, result.length);
        assertEquals("-D", result[0]);
        assertEquals("property=value", result[1]);
    }

    @Test
    public void testFlatten_ArgumentListContainsNonOption_ReturnFlattenedArgumentsWithNonOption() {
        // Arrange
        Options options = new Options();
        options.addOption("f", "foo", false, "Foo option");
        String[] arguments = {"-f", "file.txt", "non_option"};

        // Act
        String[] result = parser.flatten(options, arguments, true);

        // Assert
        assertEquals(3, result.length);
        assertEquals("-f", result[0]);
        assertEquals("file.txt", result[1]);
        assertEquals("non_option", result[2]);
    }
}

