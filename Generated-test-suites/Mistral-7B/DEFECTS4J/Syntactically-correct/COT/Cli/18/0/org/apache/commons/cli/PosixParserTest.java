import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

    @Test
    public void testFlattenSimpleOptions() {
        String[] args = {"--option1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--option1", "--option2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionsWithArguments() {
        String[] args = {"--option1=arg1", "--option2=arg2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--option1=arg1", "--option2=arg2"};
        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenNonOption() {
        String[] args = {"arg1", "--option1", "arg2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenSingleHyphen() {
        String[] args = {"-"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOption() {
        String[] args = {"--longOption"};
        Options options = new Options();
        options.addLongOption("longOption", true, "lo");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--longOption"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionWithArgumentAndNonOption() {
        String[] args = {"--option1=arg1", "nonOption"};
        Options options = new Options();
        options.addOption("option1", true, "o1");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--option1=arg1", "nonOption"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenOptionWithLongArgument() {
        String[] args = {"--option1=longArg"};
        Options options = new Options();
        options.addOption("option1", true, "o1");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--option1=longArg"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithArguments() {
        String[] args = {"--option1=arg1", "--option2=arg2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--option1=arg1", "--option2=arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithArgument() {
        String[] args = {"--longOption=longArg"};
        Options options = new Options();
        options.addLongOption("longOption", true, "lo");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--longOption=longArg"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleLongOptionsWithArguments() {
        String[] args = {"--longOption1=arg1", "--longOption2=arg2"};
        Options options = new Options();
        options.addLongOption("longOption1", true, "lo1");
        options.addLongOption("longOption2", true, "lo2");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--longOption1=arg1", "--longOption2=arg2"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"arg1", "--option1", "arg2"};
        Options options = new Options();
        options.addOption("option1", true, "o1");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        String[] expected = {"arg1", "--option1"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenSingleCharacterOption() {
        String[] args = {"--o"};
        Options options = new Options();
        options.addOption("o", true, "o");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--o"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenTwoCharacterOption() {
        String[] args = {"--oo"};
        Options options = new Options();
        options.addOption("oo", true, "oo");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--oo"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenOptionWithArgument() {
        String[] args = {"--oo=arg"};
        Options options = new Options();
        options.addOption("oo", true, "oo");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--oo=arg"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenLongOption() {
        String[] args = {"--longOption"};
        Options options = new Options();
        options.addLongOption("longOption", true, "lo");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--longOption"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenLongOptionWithArgument() {
        String[] args = {"--longOption=arg"};
        Options options = new Options();
        options.addLongOption("longOption", true, "lo");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--longOption=arg"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenNonOption() {
        String[] args = {"nonOption", "--option"};
        Options options = new Options();
        options.addOption("option", true, "o");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"nonOption", "--option"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstTokenNonOptionWithArguments() {
        String[] args = {"nonOption", "--option=arg1=arg2"};
        Options options = new Options();
        options.addOption("option", true, "o");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        String[] expected = {"nonOption", "--option=arg1=arg2"};
        assertArrayEquals(expected, result);
    }
}