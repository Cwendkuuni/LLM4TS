package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseEmpty() throws ParseException {
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), new String[0]);

        assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithSimpleOptions() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "1", "--b", "2"};
        Options options = new Options();
        Option optionA = OptionBuilder.withLongOpt("a")
                .withValueSeparator(new String[0])
                .withValueParsers(new IntParser())
                .isRequired()
                .create();
        Option optionB = OptionBuilder.withLongOpt("b")
                .withValueSeparator(new String[0])
                .create();
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLine cmd = parser.parse(options, arguments);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        assertTrue(optionsList.get(0).getValue().equals("1"));
        assertTrue(optionsList.get(1).getValue().equals("2"));
    }

    @Test
    public void testParseWithSimpleOptionsAndArgs() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"-a", "1", "--b", "2", "arg1", "arg2"};
        Options options = new Options();
        Option optionA = OptionBuilder.withLongOpt("a")
                .withValueSeparator(new String[0])
                .withValueParsers(new IntParser())
                .isRequired()
                .create();
        Option optionB = OptionBuilder.withLongOpt("b")
                .withValueSeparator(new String[0])
                .create();
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLine cmd = parser.parse(options, arguments);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        assertTrue(optionsList.get(0).getValue().equals("1"));
        assertTrue(optionsList.get(1).getValue().equals("2"));
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
    }

    @Test
    public void testParseWithGroupedOptions() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"--foo", "-c", "-a", "1", "-b", "2"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("foo", "Foo group");
        Option optionA = OptionBuilder.withLongOpt("a")
                .withValueSeparator(new String[0])
                .withValueParsers(new IntParser())
                .isRequired()
                .create();
        Option optionB = OptionBuilder.withLongOpt("b")
                .withValueSeparator(new String[0])
                .create();
        group.addOption(optionA);
        group.addOption(optionB);
        options.addOption(group);

        CommandLine cmd = parser.parse(options, arguments);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(2, optionsList.size());
        assertTrue(optionsList.get(0).getValue().equals("1"));
        assertTrue(optionsList.get(1).getValue().equals("2"));
        assertTrue(group.isSelected());
    }

    @Test
    public void testParseWithMixedOptionsAndArgs() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"--foo", "--b", "-a", "1", "arg1", "--c", "2", "arg2"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("foo", "Foo group");
        Option optionA = OptionBuilder.withLongOpt("a")
                .withValueSeparator(new String[0])
                .withValueParsers(new IntParser())
                .isRequired()
                .create();
        Option optionB = OptionBuilder.withLongOpt("b")
                .withValueSeparator(new String[0])
                .withValueParsers(new IntParser())
                .create();
        Option optionC = OptionBuilder.withLongOpt("c")
                .isRequired()
                .create();
        group.addOption(optionA);
        group.addOption(optionB);
        options.addOption(group);
        options.addOption(optionC);

        CommandLine cmd = parser.parse(options, arguments);

        List<Option> optionsList = cmd.getOptions();
        assertEquals(3, optionsList.size());
        assertTrue(optionsList.get(0).getValue().equals("1"));
        assertTrue(optionsList.get(1).getValue().equals("2"));
        assertTrue(optionsList.get(2).isRequired());
        assertEquals(4, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(2));
        assertNull(cmd.getArgs().get(1));
        assertNull(cmd.getArgs().get(3));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"--a", "arg1"};
        Options options = new Options();
        Option optionA = OptionBuilder.withLongOpt("a")
                .hasArg(true)
                .create();
        options.addOption(optionA);

        parser.parse(options, arguments);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"--unknown"};
        Options options = new Options();

        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"-a"};
        Options options = new Options();
        Option optionA = OptionBuilder.withLongOpt("a")
                .isRequired()
                .create();
        options.addOption(optionA);

        parser.parse(options, arguments);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingAllRequiredOptions() throws ParseException {
        Parser parser = new DefaultParser();
        String[] arguments = {"--foo"};
        Options options = new Options();
        Option optionA = OptionBuilder.withLongOpt("a")
                .isRequired()
                .create();
        Option optionB = OptionBuilder.withLongOpt("b")
                .isRequired()
                .create();
        Option optionC = OptionBuilder.withLongOpt("c")
                .isRequired()
                .create();
        OptionGroup group = new OptionGroup("foo", "Foo group");
        group.addOption(optionA);
        group.addOption(optionB);
        group.addOption(optionC);
        options.addOption(group);

        parser.parse(options, arguments);
    }
}