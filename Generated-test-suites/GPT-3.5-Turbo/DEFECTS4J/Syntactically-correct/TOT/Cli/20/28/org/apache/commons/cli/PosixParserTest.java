
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.cli.CommandLine;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("file").hasArg().create("f"));

        String[] arguments = {"--file=test.txt"};
        String[] expected = {"--file", "test.txt"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithoutValue() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").create("h"));

        String[] arguments = {"--help"};
        String[] expected = {"--help"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("verbose").create("v"));

        String[] arguments = {"-"};
        String[] expected = {"-"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleCharacterOption() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("verbose").create("v"));

        String[] arguments = {"-v"};
        String[] expected = {"-v"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleCharacterOption_Invalid() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("verbose").create("v"));

        String[] arguments = {"-x"};
        String[] expected = {"-x"};
        String[] result = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithValue_StopAtNonOption() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("file").hasArg().create("f"));

        String[] arguments = {"--file=test.txt", "arg1", "arg2"};
        String[] expected = {"--file", "test.txt", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_LongOptionWithoutValue_StopAtNonOption() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("help").create("h"));

        String[] arguments = {"--help", "arg1", "arg2"};
        String[] expected = {"--help", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleHyphen_StopAtNonOption() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("verbose").create("v"));

        String[] arguments = {"-", "arg1", "arg2"};
        String[] expected = {"-", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleCharacterOption_StopAtNonOption() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("verbose").create("v"));

        String[] arguments = {"-v", "arg1", "arg2"};
        String[] expected = {"-v", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_SingleCharacterOption_Invalid_StopAtNonOption() {
        Options options = new Options();
        options.addOption(OptionBuilder.withLongOpt("verbose").create("v"));

        String[] arguments = {"-x", "arg1", "arg2"};
        String[] expected = {"-x", "arg1", "arg2"};
        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

}

