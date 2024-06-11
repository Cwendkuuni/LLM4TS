import org.apache.commons.cli.*;
import org.junit.Test;
import org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSimpleUsage() throws ParseException {
        String[] args = {"-a", "1", "-b", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("1", cmd.getArgs().get(0));
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test
    public void testMissingOption() throws ParseException {
        String[] args = {"-a", "1", "-c", "3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try {
            parser.parse(options, args);
            fail("MissingOptionException expected");
        } catch (MissingOptionException e) {
            assertEquals("Missing required option 'b'", e.getMessage());
        }

        Assert.assertNull(parser.cmd);
    }

    @Test
    public void testMissingArgument() throws ParseException {
        String[] args = {"-a", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "Argument B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try {
            parser.parse(options, args);
            fail("MissingArgumentException expected");
        } catch (MissingArgumentException e) {
            assertEquals("Missing argument for option: b", e.getMessage());
        }

        Assert.assertNull(parser.cmd);
    }

    @Test
    public void testInvalidOption() throws ParseException {
        String[] args = {"--invalid"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try {
            parser.parse(options, args);
            fail("UnrecognizedOptionException expected");
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: invalid", e.getMessage());
        }

        Assert.assertNull(parser.cmd);
    }

    @Test
    public void testLongOption() throws ParseException {
        String[] args = {"--long-option"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, args);
        Assert.assertTrue(cmd.hasOption("longOption"));
        Assert.assertNull(cmd.getOptionValue("longOption"));
    }

    @Test
    public void testOptionWithValue() throws ParseException {
        String[] args = {"-a", "1", "--value", "3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("value", true, "Value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals("3", cmd.getOptionValue("value"));
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("value"));
    }

    @Test
    public void testOptionGroup() throws ParseException {
        String[] args = {"-g", "group1", "-a", "1", "-g", "group2"};
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        OptionGroup group2 = new OptionGroup("group2", "Group 2");
        group1.addOption(new Option("a", true, "Option A"));
        group2.addOption(new Option("x", true, "Option X"));
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(2, cmd.getOptions().size());
        Assert.assertTrue(cmd.getOptions().get(0) instanceof OptionGroup);
        Assert.assertEquals("group1", cmd.getOptions().get(0).getName());
        Assert.assertTrue(cmd.getOptions().get(1) instanceof Option);
        Assert.assertEquals("1", cmd.getOptionValue("a"));
    }

    @Test
    public void testMixedLongAndShortOptions() throws ParseException {
        String[] args = {"--long-option", "-a", "--another-long-option", "-b"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option");
        options.addOption("a", true, "Option A");
        options.addOption("anotherLongOption", true, "Another long option");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(3, cmd.getOptions().size());
        Assert.assertTrue(cmd.getOptions().get(0) instanceof Option);
        Assert.assertEquals("true", cmd.getOptionValue("longOption"));
        Assert.assertTrue(cmd.getOptions().get(1) instanceof Option);
        Assert.assertEquals("true", cmd.getOptionValue("a"));
        Assert.assertTrue(cmd.getOptions().get(2) instanceof Option);
        Assert.assertEquals("true", cmd.getOptionValue("anotherLongOption"));
    }

    @Test
    public void testLongOptionWithArgument() throws ParseException {
        String[] args = {"--arg", "value"};
        Options options = new Options();
        options.addOption("arg", true, "Argument", true, "Value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals("value", cmd.getOptionValue("arg"));
    }

    @Test
    public void testOptionalArgument() throws ParseException {
        String[] args = {"-a", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("b", false, "Option B (optional)");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args);

        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testOptionWithValueAndArgument() throws ParseException {
        String[] args = {"--value", "3", "--arg", "value"};
        Options options = new Options();
        options.addOption("value", true, "Value");
        options.addOption("arg", true, "Argument", true, "Value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals("3", cmd.getOptionValue("value"));
        Assert.assertEquals("value", cmd.getOptionValue("arg"));
    }

    @Test
    public void testOptionsWithProperties() throws ParseException {
        String[] args = {"--prop1", "prop1=value1", "--prop2=value2"};
        Properties properties = new Properties();
        properties.setProperty("prop1", "value1");
        properties.setProperty("prop2", "value2");
        Options options = new Options();
        options.addOption("prop1", true, "Property 1");
        options.addOption("prop2", true, "Property 2");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, properties);

        Assert.assertEquals(2, cmd.getOptions().size());
        Assert.assertEquals("value1", cmd.getOptionValue("prop1"));
        Assert.assertEquals("value2", cmd.getOptionValue("prop2"));
        Assert.assertEquals(properties.getProperty("prop1"), cmd.getOptionValue("prop1"));
        Assert.assertEquals(properties.getProperty("prop2"), cmd.getOptionValue("prop2"));
    }

    @Test
    public void testMaxArgs() throws ParseException {
        String[] args = {"-a", "1", "-a", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A", 1);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try {
            parser.parse(options, args);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot process duplicate argument: a", e.getMessage());
        }

        Assert.assertNull(parser.cmd);
    }

    @Test
    public void testEmptyOptions() throws ParseException {
        String[] args = {};
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args);
        Assert.assertNull(cmd);
    }
}