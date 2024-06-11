import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testSimpleOption() {
        Options options = new Options();
        options.addOption("a", "anOption", false, "A simple option");

        String[] args = {"-a"};
        try {
            new PosixParser().parse(options, args, true);
            Assert.assertEquals(Arrays.asList("-a"), Arrays.asList(options.getOptionValues("anOption")));
        } catch (ParseException e) {
            Assert.fail("Should parse simple option correctly.");
        }
    }

    @Test
    public void testOptionWithValue() {
        Options options = new Options();
        options.addOption("b", "aBoolValue", true, "A boolean option with a value");

        String[] args = {"-b", "true"};
        try {
            new PosixParser().parse(options, args, true);
            Assert.assertEquals(Arrays.asList("true"), Arrays.asList(options.getOptionValues("aBoolValue")));
        } catch (ParseException e) {
            Assert.fail("Should parse boolean option with value correctly.");
        }
    }

    @Test
    public void testLongOption() {
        Options options = new Options();
        options.addOption("c", "aLongOption", false, "A long option");

        String[] args = {"--aLongOption"};
        try {
            new PosixParser().parse(options, args, true);
            Assert.assertEquals(Arrays.asList("-c"), Arrays.asList(options.getOptionValues("aLongOption")));
        } catch (ParseException e) {
            Assert.fail("Should parse long option correctly.");
        }
    }

    @Test
    public void testLongOptionWithValue() {
        Options options = new Options();
        options.addOption("d", "aStringValue", true, "A string option with a value");

        String[] args = {"--aStringValue", "testValue"};
        try {
            new PosixParser().parse(options, args, true);
            Assert.assertEquals(Arrays.asList("testValue"), Arrays.asList(options.getOptionValues("aStringValue")));
        } catch (ParseException e) {
            Assert.fail("Should parse string option with value correctly.");
        }
    }

    @Test
    public void testMultipleOptions() {
        Options options = new Options();
        options.addOption("e", "anOption1", false, "An option 1");
        options.addOption("e", "anOption2", false, "An option 2");

        String[] args = {"-e", "--anOption2"};
        try {
            new PosixParser().parse(options, args, true);
            Assert.assertEquals(Arrays.asList("-e", "--anOption2"), Arrays.asList(options.getOptionValues("anOption1", "anOption2")));
        } catch (ParseException e) {
            Assert.fail("Should parse multiple options correctly.");
        }
    }

    @Test
    public void testInvalidOption() {
        Options options = new Options();
        options.addOption("f", "anOption", false, "An option");

        String[] args = {"-g"};
        try {
            new PosixParser().parse(options, args, true);
            Assert.fail("Should not parse invalid option correctly.");
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: -g", e.getMessage());
        }
    }

    @Test
    public void testMixedOptions() {
        Options options = new Options();
        options.addOption("h", "aBooleanOption", true, "A boolean option");
        options.addOption("i", "anOption", false, "An option");
        options.addOption("j", "aStringOption", true, "A string option");

        String[] args = {"-i", "--aBooleanOption", "-j", "--anOption", "-h", "true", "--anOption"};
        try {
            new PosixParser().parse(options, args, true);
            Assert.assertEquals(Arrays.asList("-i", "--aBooleanOption", "-j", "--anOption"), Arrays.asList(options.getOptionValues("anOption", "aBooleanOption", "aStringOption")));
            Assert.assertEquals(Arrays.asList("true"), Arrays.asList(options.getOptionValues("aBooleanOption")));
            Assert.assertEquals(Arrays.asList(""), Arrays.asList(options.getOptionValues("anOption")));
            Assert.assertEquals(Arrays.asList(""), Arrays.asList(options.getOptionValues("aStringOption")));
        } catch (ParseException e) {
            Assert.fail("Should parse mixed options correctly.");
        }
    }

    @Test
    public void testNoOptions() {
        Options options = new Options();

        String[] args = {};
        try {
            new PosixParser().parse(options, args, true);
            Assert.assertEquals(new String[0], Arrays.asList(options.getOptions()).toArray(new String[0]));
        } catch (ParseException e) {
            Assert.fail("Should parse
            empty arguments correctly.");
        }
    }

    @Test
    public void testHelp() {
        Options options = new Options();
        options.addOption("h", "help", false, "Print this message.");

        String[] args = {"--help"};
        HelpFormatter formatter = new HelpFormatter();
        try {
            new PosixParser().parse(options, args, false);
            formatter.printHelp("test", options);
        } catch (ParseException e) {
            Assert.fail("Should parse help option correctly.");
        }
    }

    @Test(expected = ParseException.class)
    public void testMandatoryOptionMissing() {
        Options options = new Options();
        options.addOption("k", "aMandatoryOption", true, "A mandatory option");

        String[] args = {};
        new PosixParser().parse(options, args, true);
    }
}