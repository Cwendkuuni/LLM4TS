import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testSimpleParse() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] arguments = new String[]{"-a", "--b=3"};

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, arguments);

            Assert.assertEquals(true, cmd.hasOption("a"));
            Assert.assertEquals(3, cmd.getOptionValue("b").length());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithRequiredOptions() {
        Options options = new Options();
        options.addRequiredOption("a", true, "Option A");
        options.addRequiredOption("b", true, "Option B");

        String[] arguments = new String[0];

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, arguments);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("Missing required options: [a, b]", e.getMessage());
        }

        arguments = new String[]{"-b"};

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, arguments);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("Missing required option: a", e.getMessage());
        }

        arguments = new String[]{"-a"};

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, arguments);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("Missing required option: b", e.getMessage());
        }

        arguments = new String[]{"-a", "-b"};

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, arguments);
            Assert.assertEquals(2, cmd.getArgs().length);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithOptionalOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] arguments = new String[]{"-a", "--b=3", "arg1", "arg2"};

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, arguments);
            Assert.assertEquals(4, cmd.getArgs().length);
            Assert.assertEquals(true, cmd.hasOption("a"));
            Assert.assertEquals("3", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        arguments = new String[]{"-a", "arg1", "arg2"};

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, arguments);
            Assert.assertEquals(3, cmd.getArgs().length);
            Assert.assertEquals(true, cmd.hasOption("a"));
            Assert.assertNull(cmd.getOptionValue("b"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithInvalidOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = new String[]{"-x", "--a=3", "arg1"};

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, arguments);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: x", e.getMessage());
        }

        arguments = new String[]{"--a=3", "arg1"};

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, arguments);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("Invalid value for option: 'a': '3'", e.getMessage());
        }
    }

    @Test
    public void testParseWithLongOptions() {
        Options options = new Options();
        options.addLongOption("long-option-a", true, "Long Option A");

        String[] arguments = new String[]{"--long-option-a"};

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, arguments);
            Assert.assertEquals(0, cmd.getArgs().length);
            Assert.assertEquals(true, cmd.hasOption("long-option-a"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        arguments = new String[]{"--long-option-a=value"};

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, arguments);
            Assert.assertEquals(0, cmd.getArgs().length);
            Assert.assertEquals("value", cmd.getOptionValue("long-option-a"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParseWithArguments() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] arguments = new String[]{"--", "arg1", "arg2"};

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, arguments);
            Assert.assertEquals(2, cmd.getArgs().length);
            Assert.assertEquals("arg1", cmd.getArg(0));
            Assert.assertEquals("arg2", cmd.getArg(1));
            Assert.assertFalse(cmd.hasOptions());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}