import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CommandLineTest {

    @Test
    public void testSimpleOption() {
        String[] args = new String[]{"-a", "1"};
        Options options = new Options();
        Option option = new Option("a", "arg", false, "Some argument");
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            Assert.assertTrue(commandLine.hasOption("a"));
            Assert.assertNull(commandLine.getOptionValue("a"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOptionWithValue() {
        String[] args = new String[]{"-a", "2", "hello"};
        Options options = new Options();
        Option option = new Option("a", "arg", true, "Some argument");

        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            Assert.assertTrue(commandLine.hasOption("a"));
            Assert.assertEquals("hello", commandLine.getOptionValue("a"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMultipleOptions() {
        String[] args = new String[]{"-a", "1", "-b", "2"};
        Options options = new Options();
        Option optionA = new Option("a", "argA", false, "Option A");
        Option optionB = new Option("b", "argB", false, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            Assert.assertTrue(commandLine.hasOption("a"));
            Assert.assertTrue(commandLine.hasOption("b"));
            Assert.assertNull(commandLine.getOptionValue("a"));
            Assert.assertNull(commandLine.getOptionValue("b"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMissingOption() {
        String[] args = new String[]{"1", "2"};
        Options options = new Options();
        Option option = new Option("a", "arg", false, "Some argument");
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            Assert.assertFalse(commandLine.hasOption("a"));
            Assert.assertNull(commandLine.getOptionValue("a"));
            Assert.assertEquals(args.length, commandLine.getArgList().size());
            Assert.assertEquals(Arrays.toString(args), Arrays.toString((String[]) commandLine.getArgs()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInvalidOption() {
        String[] args = new String[]{"-invalid"};
        Options options = new Options();
        Option option = new Option("a", "arg", false, "Some argument");
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            Assert.assertFalse(commandLine.hasOption("invalid"));
            Assert.assertNull(commandLine.getOptionValue("invalid"));
            Assert.assertEquals(1, commandLine.getArgList().size());
            Assert.assertEquals("-invalid", commandLine.getArgList().get(0));
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: -invalid", e.getMessage());
        }
    }

    @Test
    public void testMissingArgument() {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        Option option = new Option("a", "arg", true, "Some argument");
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            Assert.assertTrue(commandLine.hasOption("a"));
            Assert.assertNull(commandLine.getOptionValue("a"));
            Assert.assertEquals(1, commandLine.getArgList().size());
            Assert.assertEquals("-a", commandLine.getArgList().get(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMissingArgumentWithHelp() {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        Option option = new Option("a", "arg", true, "Some argument");
        options.addOption(option);
        Option helpOption = new Option("h", "help", false, "Print help");
        options.addOption(helpOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption("h")) {
                formatter.printHelp("test", options);
                return;
            }
            Assert.assertTrue(commandLine.hasOption("a"));
            Assert.assertNull(commandLine.getOptionValue("a"));
            Assert.assertEquals(1, commandLine.getArgList().size());
            Assert.assertEquals("-a", commandLine.getArgList().get(0));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        formatter.printHelp("test", options);
    }
}