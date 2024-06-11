import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testFlatten() {
        Options opts = new Options();
        String[] arguments = new String[] {"-a", "-b", "value"};
        String[] flattened = Parser.flatten(opts, arguments, false);
        assertTrue(flattened.length == arguments.length);
    }

    @Test
    public void testParseWithOptionsAndArguments() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] arguments = new String[] {"-a", "arg1", "arg2"};

        try {
            CommandLine cmd = Parser.parse(options, arguments);

            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.getArgList().contains("arg1"));
            assertTrue(cmd.getArgList().contains("arg2"));

        } catch (ParseException e) {
            fail("Parse exception occurred");
        }
    }

    @Test
    public void testParseWithProperties() {
        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("b", false, "option b");

        Properties properties = new Properties();
        properties.setProperty("a", "value");
        properties.setProperty("b", "true");

        String[] arguments = new String[] {};

        try {
            CommandLine cmd = Parser.parse(options, arguments, properties);

            assertTrue(cmd.hasOption("a"));
            assertEquals("value", cmd.getOptionValue("a"));
            assertTrue(cmd.hasOption("b"));


        } catch (ParseException e) {
            fail("Parse exception occurred");
        }
    }

    @Test
    public void testParseWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");

        String[] arguments = new String[] {"-a", "arg1", "-b", "arg2", "arg3"};

        try {
            CommandLine cmd = Parser.parse(options, arguments, true);

            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertTrue(cmd.getArgList().contains("arg1"));
            assertEquals(0, cmd.getArgList().indexOf("arg2"));

        } catch (ParseException e) {
            fail("Parse exception occurred");
        }
    }

    @Test
    public void testParseWithStopAtNonOptionAndProperties() {
        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("b", false, "option b");

        Properties properties = new Properties();
        properties.setProperty("a", "value");
        properties.setProperty("b", "true");

        String[] arguments = new String[] {"-a", "arg1", "-b", "arg2", "arg3"};

        try {
            CommandLine cmd = Parser.parse(options, arguments, properties, true);

            assertTrue(cmd.hasOption("a"));
            assertEquals("value", cmd.getOptionValue("a"));
            assertTrue(cmd.hasOption("b"));
            assertTrue(cmd.getArgList().contains("arg1"));
            assertEquals(0, cmd.getArgList().indexOf("arg2"));

        } catch (ParseException e) {
            fail("Parse exception occurred");
        }
    }

    @Test
    public void testProcessArgs() {
        Option opt = new Option("f", "file", false, "file option");
        ListIterator iter = Arrays.asList(new String[] {"file.txt"}).listIterator();

        try {
            Parser.processArgs(opt, iter);

            assertTrue(opt.hasArg());
            assertEquals("file.txt", opt.getValue());
        } catch (ParseException e) {
            fail("Parse exception occurred");
        }
    }

    @Test
    public void testProcessOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        Option opt = new Option("b", "bool", false, "option b");
        options.addOption(opt);

        ListIterator iter = Arrays.asList(new String[] {"-a"}).listIterator();

        try {
            Parser.processOption("-a", iter);
            assertTrue(cmd.hasOption("a"));
        } catch (ParseException e) {
            fail("Parse exception occurred");
        }
        
        ListIterator iter2 = Arrays.asList(new String[] {"-b"}).listIterator();
        try {
            Parser.processOption("-b", iter2);
            assertTrue(cmd.hasOption("b"));
        } catch (ParseException e) {
            fail("Parse exception occurred");
        }
    }

    @Test
    public void testCheckRequiredOptionsWithMissingOption() {
        Options options = new Options();
        Option opt = new Option("a", false, "option a");
        opt.setRequired(true);
        options.addOption(opt);

        String[] arguments = new String[] {};

        try {
            CommandLine cmd = Parser.parse(options, arguments);
            fail("MissingOptionException should have been thrown");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option: a", e.getMessage());
        }
    }

    @Test
    public void testCheckRequiredOptionsWithAllOptionsPresent() {
        Options options = new Options();
        Option opt1 = new Option("a", false, "option a");
        Option opt2 = new Option("b", false, "option b");
        opt1.setRequired(true);
        opt2.setRequired(true);
        options.addOption(opt1);
        options.addOption(opt2);

        String[] arguments = new String[] {"-a", "-b"};

        try {
            CommandLine cmd = Parser.parse(options, arguments);

            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));

        } catch (ParseException e) {
            fail("Parse exception occurred");
        }
    }

    @Test
    public void testProcessProperties() {
        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("b", false, "option b");

        Properties properties = new Properties();
        properties.setProperty("a", "value");
        properties.setProperty("b", "true");

        try {
            Parser.processProperties(properties);

            assertTrue(cmd.hasOption("a"));
            assertEquals("value", cmd.getOptionValue("a"));
            assertTrue(cmd.hasOption("b"));

        } catch (ParseException e) {
            fail("Parse exception occurred");
        }
    }
}