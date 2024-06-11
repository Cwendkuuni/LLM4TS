import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
    @Test
    public void testFlattenWithEmptyArguments() {
        // Given
        Options options = new Options();
        String[] arguments = new String[0];
        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, false);

        // Then
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenWithSingleArgument() {
        // Given
        Options options = new Options();
        String[] arguments = {"arg1"};
        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, false);

        // Then
        assertEquals(1, result.length);
        assertEquals("arg1", result[0]);
    }

    @Test
    public void testFlattenWithDoubleDash() {
        // Given
        Options options = new Options();
        String[] arguments = {"--"};
        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, false);

        // Then
        assertEquals(1, result.length);
        assertEquals("--", result[0]);
    }

    @Test
    public void testFlattenWithSingleHyphen() {
        // Given
        Options options = new Options();
        String[] arguments = {"-"};
        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, false);

        // Then
        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlattenWithOption() {
        // Given
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] arguments = {"-o"};
        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, false);

        // Then
        assertEquals(1, result.length);
        assertEquals("-o", result[0]);
        assertEquals("option1", result[0].substring(1));
    }

    @Test
    public void testFlattenWithOptionWithValue() {
        // Given
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] arguments = {"-o", "value1"};
        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, false);

        // Then
        assertEquals(2, result.length);
        assertEquals("-o", result[0]);
        assertEquals("option1", result[0].substring(1));
        assertEquals("value1", result[1]);
    }

    @Test
    public void testFlattenWithLongOption() {
        // Given
        Options options = new Options();
        options.addOption("longOption1", true, "l");
        String[] arguments = {"-l", "longOptionValue"};
        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, false);

        // Then
        assertEquals(2, result.length);
        assertEquals("-l", result[0]);
        assertEquals("longOption1", result[0].substring(1));
        assertEquals("longOptionValue", result[1]);
    }

    @Test
    public void testFlattenWithLongOptionWithValueAndMoreArguments() {
        // Given
        Options options = new Options();
        options.addOption("longOption1", true, "l");
        String[] arguments = {"-l", "longOptionValue1", "arg1", "arg2"};
        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, false);

        // Then
        assertEquals(4, result.length);
        assertEquals("-l", result[0]);
        assertEquals("longOption1", result[0].substring(1));
        assertEquals("longOptionValue1", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("arg2", result[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidOption() {
        // Given
        Options options = new Options();
        String[] arguments = {"-x"};
        PosixParser parser = new PosixParser();

        // When & Then
        parser.flatten(options, arguments, false);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        // Given
        Options options = new Options();
        options.addOption("option1", true, "o");
        String[] arguments = {"-o", "value1", "nonOption"};
        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, arguments, true);

        // Then
        assertEquals(3, result.length);
        assertEquals("-o", result[0]);
        assertEquals("option1", result[0].substring(1));
        assertEquals("value1", result[1]);
        assertEquals("nonOption", result[2]);
    }
}