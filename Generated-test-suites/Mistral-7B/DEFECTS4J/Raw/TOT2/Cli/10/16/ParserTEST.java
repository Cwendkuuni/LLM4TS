import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.cli.ParsePosition.PARSED_INT;
import static org.apache.commons.cli.ParsePosition.UNDEF;

/**
 * Test for Parser class
 */
public class ParserTest {

    @Test
    public void testParser() throws ParseException {
        String[] args = new String[]{"--foo", "-b", "-c", "arg1", "--", "-d", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", false, "b");
        options.addOption("d", null, 1, "d");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(true, cmd.hasOption("foo"));
        Assert.assertEquals("arg1", cmd.getOptionValue("d"));
        Assert.assertEquals(false, cmd.hasOption("bar"));
        Assert.assertNull(cmd.getOptionValue("nonExistentOption"));
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());

        try {
            parser.parse(options, args, false);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals(1, e.getParsedIndex());
            Assert.assertEquals("Unexpected option: nonExistentOption", e.getMessage());
        }

        try {
            parser.parse(options, new String[]{"--foo", "--foo"}, false);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals(1, e.getParsedIndex());
            Assert.assertEquals("Duplicate option: foo", e.getMessage());
        }

        try {
            parser.parse(options, new String[]{"--foo=1", "--foo=2"}, false);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals(1, e.getParsedIndex());
            Assert.assertEquals("Duplicate option value for option: foo", e.getMessage());
        }

        try {
            parser.parse(options, new String[]{"--foo", "--foo=1", "arg1"}, false);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals(2, e.getParsedIndex());
            Assert.assertEquals("Unexpected option: arg1", e.getMessage());
        }

        Assert.assertEquals(PARSED_INT, cmd.getOption("d").getValueType());
        Assert.assertEquals(1, cmd.getOption("d").getValue().length);
        Assert.assertEquals(1, Arrays.asList(cmd.getOptionValues("d")).size());
        Assert.assertEquals("1", cmd.getOptionValue("d")[0]);

        try {
            parser.parse(options, new String[]{"--foo=1", "--foo=2"}, false);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals(1, e.getParsedIndex());
            Assert.assertEquals("Duplicate option value for option: foo", e.getMessage());
        }

        try {
            parser.parse(options, new String[]{"--foo=1", "--foo=2", "arg1"}, false);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals(2, e.getParsedIndex());
            Assert.assertEquals("Unexpected option: arg1", e.getMessage());
        }
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = new String[]{"--foo", "--bar=value1", "--baz=value2"};
        Properties properties = new Properties();
        properties.setProperty("foo", "value3");
        properties.setProperty("bar", "value4");

        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", false, "b");
        options.addOption("d", null, 1, "d");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties);

        Assert.assertEquals(true, cmd.hasOption("foo"));
        Assert.assertEquals("value3", cmd.getOptionValue("foo"));
        Assert.assertEquals("value4", cmd.getOptionValue("bar"));
        Assert.assertNull(cmd.getOptionValue("nonExistentOption"));
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, cmd.getArgs());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] args = new String[]{"--foo", "arg1", "--", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals(true, cmd.hasOption("foo"));
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }
}