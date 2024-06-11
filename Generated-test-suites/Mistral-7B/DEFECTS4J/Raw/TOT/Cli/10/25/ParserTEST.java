import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseSimple() throws Exception {
        String[] args = {"-a", "1", "--option", "Value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("option", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("a"));
        assertEquals(true, cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("option"));
        assertEquals("Value", cmd.getOptionValue("option"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws Exception {
        String[] args = {"--option"};
        Options options = new Options();
        options.addOption("option", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        fail("Expected MissingArgumentException");
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingOption() throws Exception {
        String[] args = {"-x", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        fail("Expected MissingOptionException");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws Exception {
        String[] args = {"--unknown", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        fail("Expected UnrecognizedOptionException");
    }

    @Test
    public void testParseStopAtNonOption() throws Exception {
        String[] args = {"--", "-n", "value"};
        Options options = new Options();
        options.addOption("n", true, "Option N");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        assertTrue(cmd.getArgs().length > 0);
        assertEquals("value", cmd.getArgs()[0]);
    }

    @Test
    public void testParseProperties() throws Exception {
        String[] args = {"--foo=bar", "--baz=qux"};
        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        options.addOption("baz", true, "Option Baz");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertEquals("bar", cmd.getOptionValue("foo"));
        assertEquals("qux", cmd.getOptionValue("baz"));
    }

    @Test
    public void testParseRequiredOptions() throws Exception {
        String[] args = {"--option1", "1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2")
                .requireOrder(true)
                .required(true);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() throws Exception {
        String[] args = {"--option2"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1")
                .required(true);
        options.addOption("option2", true, "Option 2")
                .required(true);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        fail("Expected MissingOptionException");
    }

    @Test
    public void testParseOptionGroup() throws Exception {
        String[] args = {"--option1", "-g", "group1", "value1", "-g", "group2", "value2"};
        Options options = new Options();

        OptionGroup group1 = new OptionGroup("group1", "Option Group 1");
        group1.addOption(new Option("option1", true, "Option 1 Group 1"));

        OptionGroup group2 = new OptionGroup("group2", "Option Group 2");
        group2.addOption(new Option("option2", true, "Option 1 Group 2"));

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.getOptionValue("option1"));
        assertTrue(cmd.getOptionValue("option2"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingOptionGroup() throws Exception {
        String[] args = {"--option1", "-g", "group2", "value2"};
        Options options = new Options();

        OptionGroup group1 = new OptionGroup("group1", "Option Group 1");
        group1.addOption(new Option("option1", true, "Option 1 Group 1"));

        OptionGroup group2 = new OptionGroup("group2", "Option Group 2");
        group2.addOption(new Option("option2", true, "Option 1 Group 2"));

        options.addOptionGroup(group1);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        fail("Expected MissingOptionException");
    }
}