package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten_NormalArguments_ReturnsFlattenedArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {"-a", "value1", "-b", "value2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value2", result[3]);
    }

    @Test
    public void testFlatten_StopAtNonOption_ReturnsFlattenedArgumentsUntilNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {"-a", "value1", "-b", "value2", "nonOption"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value2", result[3]);
    }

    @Test
    public void testFlatten_StopAtNonOption_NoNonOption_ReturnsAllFlattenedArguments() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {"-a", "value1", "-b", "value2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("value2", result[3]);
    }

    @Test
    public void testFlatten_DoubleHyphen_ReturnsArgumentsAfterHyphen() {
        Options options = new Options();

        String[] arguments = {"-a", "value1", "--", "-b", "value2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--", result[2]);
        assertEquals("-b", result[3]);
    }

    @Test
    public void testFlatten_SingleHyphen_ReturnsSingleHyphenAsArgument() {
        Options options = new Options();

        String[] arguments = {"-", "value1"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-", result[0]);
        assertEquals("value1", result[1]);
    }

    @Test
    public void testFlatten_OptionWithSubOptions_ReturnsOptionAndSubOptionsSeparately() {
        Options options = new Options();
        options.addOption("D", true, "Define property");

        String[] arguments = {"-Dproperty=value"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-D", result[0]);
        assertEquals("property=value", result[1]);
    }

    @Test
    public void testFlatten_UnknownOption_ReturnsUnknownOptionAsArgument() {
        Options options = new Options();
        options.addOption("a", false, "Option a");

        String[] arguments = {"-b", "value1"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("-b", result[0]);
        assertEquals("value1", result[1]);
    }

}