package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse_withOptionsAndArguments() {
        Options options = new Options();
        Option option1 = new Option("f", false, "Option Description 1");
        Option option2 = new Option("r", true, "Option Description 2");
        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"-f", "-r", "value"};

        Parser parser = new DefaultParser();
        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("f"));
        assertTrue(result.hasOption("r"));
        assertEquals("value", result.getOptionValue("r"));
    }

    @Test
    public void testParse_withProperties() {
        Options options = new Options();
        Option option = new Option("f", false, "Option Description");
        options.addOption(option);

        String[] arguments = {};

        Properties properties = new Properties();
        properties.setProperty("f", "true");

        Parser parser = new DefaultParser();
        CommandLine result = parser.parse(options, arguments, properties);

        assertTrue(result.hasOption("f"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParse_missingRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("f", false, "Option Description 1");
        Option option2 = new Option("r", true, "Option Description 2");
        option2.setRequired(true);
        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"-f"};

        Parser parser = new DefaultParser();
        parser.parse(options, arguments);
    }

    @Test
    public void testParse_stopAtNonOption() {
        Options options = new Options();
        Option option1 = new Option("f", false, "Option Description 1");
        Option option2 = new Option("r", true, "Option Description 2");
        options.addOption(option1);
        options.addOption(option2);

        String[] arguments = {"-f", "arg1", "-r"};

        Parser parser = new DefaultParser();
        parser.setStopAtNonOption(true);
        CommandLine result = parser.parse(options, arguments);

        assertTrue(result.hasOption("f"));
        assertFalse(result.hasOption("r"));
        assertEquals(2, result.getArgList().size());
        assertEquals("arg1", result.getArgList().get(0));
        assertEquals("-r", result.getArgList().get(1));
    }

    @Test
    public void testProcessArgs() {
        Option option = new Option("v", true, "Option Description");

        Parser parser = new DefaultParser();

        ListIterator<String> iterator = Arrays.asList("value1", "value2").listIterator();
        parser.processArgs(option, iterator);

        assertTrue(option.hasArg());
        assertEquals("value1", option.getValue());
        assertNull(iterator.next());
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_missingArgument() {
        Option option = new Option("v", true, "Option Description");

        Parser parser = new DefaultParser();

        ListIterator<String> iterator = Arrays.asList().listIterator();
        parser.processArgs(option, iterator);
    }

    @Test
    public void testProcessOption() {
        Options options = new Options();
        Option option = new Option("f", false, "Option Description");
        options.addOption(option);

        Parser parser = new DefaultParser();

        ListIterator<String> iterator = Arrays.asList("-f").listIterator();
        parser.processOption("-f", iterator);

        assertTrue(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertEquals("", option.getValue());
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_unrecognizedOption() {
        Options options = new Options();
        Option option = new Option("f", false, "Option Description");
        options.addOption(option);

        Parser parser = new DefaultParser();

        ListIterator<String> iterator = Arrays.asList("-u").listIterator();
        parser.processOption("-u", iterator);
    }
}