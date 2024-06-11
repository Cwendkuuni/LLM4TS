package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class ParserTest {

    @Test
    public void testParse_EmptyArgs() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, new String[0]);
        assertTrue(cmd.getArgs().isEmpty());
        assertFalse(cmd.hasOption("nonExistentOption"));
        assertFalse(cmd.hasOptions());
    }

    @Test
    public void testParse_BasicOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2");
        Option option3 = new Option("o", "opt3", true, "Option 3");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"-o", "opt1", "--opt2", "value", "-o", "opt3", "anotherValue"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("opt1"));
        assertFalse(cmd.hasOption("nonExistentOption"));
        assertTrue(cmd.hasOption("opt2", true));
        assertSame(option2, cmd.getOptionValue("opt2"));
        assertTrue(cmd.hasOption("opt3"));
        assertSame(option3, cmd.getOptionValue("opt3"));
        assertArrayEquals(new String[]{"anotherValue"}, cmd.getArgs());
    }

    @Test
    public void testParse_MixedOptionsAndArgs() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2");
        Option option3 = new Option("o", "opt3", true, "Option 3");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"arg1", "-o", "opt1", "--opt2", "value", "-o", "opt3=anotherValue", "arg2"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("opt1"));
        assertFalse(cmd.hasOption("nonExistentOption"));
        assertTrue(cmd.hasOption("opt2", true));
        assertSame(option2, cmd.getOptionValue("opt2"));
        assertTrue(cmd.hasOption("opt3"));
        assertSame(option3, cmd.getOptionValue("opt3"));
        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
    }

    @Test
    public void testParse_LongOptions() throws ParseException {
        Options options = new Options();
        Option longOption1 = new Option("longOpt1", "longOpt1", false, "Long Option 1");
        Option longOption2 = new Option("longOpt2", "longOpt2", true, "Long Option 2");

        options.addOption(longOption1);
        options.addOption(longOption2);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"--longOpt1", "--longOpt2=value"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("longOpt1"));
        assertFalse(cmd.hasOption("nonExistentOption"));
        assertTrue(cmd.hasOption("longOpt2", true));
        assertSame(longOption2, cmd.getOptionValue("longOpt2"));
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test
    public void testParse_GroupedOptions() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2");

        group.addOption(option1);
        group.addOption(option2);
        options.addOption(group);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"-g", "--opt1", "--opt2=value"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("opt1"));
        assertFalse(cmd.hasOption("nonExistentOption"));
        assertTrue(cmd.hasOption("opt2", true));
        assertSame(option2, cmd.getOptionValue("opt2"));
        assertTrue(cmd.hasOptionGroup("group"));
        assertTrue(cmd.getOptionGroup("group").isSelected());
        assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testParse_MissingRequiredOption() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", true, "Option 1");

        options.addOption(option1);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"arg1", "arg2"};
        parser.parse(options, args);

        fail("Expected ParseException but it did not occur");
    }

    @Test(expected = MissingArgumentException.class)
    public void testParse_MissingOptionArgument() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", true, "Option 1 with arg");

        options.addOption(option1);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"arg1", "-o", "opt1"};
        parser.parse(options, args);

        fail("Expected MissingArgumentException but it did not occur");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOption() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", false, "Option 1");

        options.addOption(option1);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"arg1", "--nonExistentOption"};
        parser.parse(options, args);

        fail("Expected UnrecognizedOptionException but it did not occur");
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_MissingRequiredOptions() throws ParseException {
        Options options = new Options();
        Option optionGroup = new OptionGroup("group", "Group");
        Option option1 = new Option("o", "opt1", true, "Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        options.addOption(optionGroup);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"arg1"};
        parser.parse(options, args);

        fail("Expected MissingOptionException but it did not occur");
    }

    @Test
    public void testParse_Properties() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "opt1", false, "Option 1");
        Option option2 = new Option("o", "opt2", true, "Option 2");

        options.addOption(option1);
        options.addOption(option2);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        Properties properties = new Properties();
        properties.put("opt1", "value1");
        properties.put("opt2", "value2");

        CommandLine cmd = parser.parse(options, new String[0], properties);

        assertTrue(cmd.hasOption("opt1"));
        assertSame("value1", cmd.getOptionValue("opt1"));
        assertTrue(cmd.hasOption("opt2", true));
        assertSame("value2", cmd.getOptionValue("opt2"));
        assertArrayEquals(new String[0], cmd.getArgs());
    }
}