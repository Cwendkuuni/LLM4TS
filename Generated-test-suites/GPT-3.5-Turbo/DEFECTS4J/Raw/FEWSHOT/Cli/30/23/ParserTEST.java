import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

public class ParserTest {
    @Test
    public void testSetOptions() {
        Options options = new Options();
        UtilTest test = new UtilTest();

        test.setOptions(options);

        assertEquals(options, test.getOptions());
        assertEquals(options.getRequiredOptions(), test.getRequiredOptions());
    }

    @Test
    public void testGetOptions() {
        Options options = new Options();
        UtilTest test = new UtilTest();

        test.setOptions(options);

        assertEquals(options, test.getOptions());
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option = new Option("option", true, "Option test");

        options.addOption(option);
        options.addOptionGroup(new OptionGroup().addOption(option));

        UtilTest test = new UtilTest();

        test.setOptions(options);

        assertEquals(Arrays.asList(option, option), test.getRequiredOptions());
    }

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        Option option = new Option("option", true, "Option test");

        options.addOption(option);

        UtilTest test = new UtilTest();

        String[] arguments = {"-option", "value"};
        String[] result = test.flatten(options, arguments, true);

        assertArrayEquals(arguments, result);
    }

    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        Option option = new Option("option", true, "Option test");

        options.addOption(option);

        UtilTest test = new UtilTest();

        String[] arguments = {"-option", "value"};
        CommandLine result = test.parse(options, arguments);

        assertTrue(result.hasOption(option));
        assertEquals("value", result.getOptionValue("option"));
    }

    @Test
    public void testProcessProperties() throws ParseException {
        Options options = new Options();
        Option option = new Option("option", true, "Option test");

        options.addOption(option);

        UtilTest test = new UtilTest();

        Properties properties = new Properties();
        properties.setProperty("option", "value");

        test.processProperties(properties);

        CommandLine result = test.cmd;

        assertTrue(result.hasOption(option));
        assertEquals("value", result.getOptionValue("option"));
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Options options = new Options();
        Option option = new Option("option", true, "Option test");

        options.addOption(option);

        UtilTest test = new UtilTest();

        ListIterator iterator = Arrays.asList("-option", "value").listIterator();

        test.processArgs(option, iterator);

        CommandLine result = test.cmd;

        assertTrue(result.hasOption(option));
        assertEquals("value", result.getOptionValue("option"));
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("option", true, "Option test");

        options.addOption(option);

        UtilTest test = new UtilTest();
        ListIterator iterator = Arrays.asList("-option", "value").listIterator();

        test.processOption("-option", iterator);

        CommandLine result = test.cmd;

        assertTrue(result.hasOption(option));
        assertEquals("value", result.getOptionValue("option"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionThrowsUnrecognizedOptionException() throws ParseException {
        Options options = new Options();
        Option option = new Option("option", true, "Option test");

        options.addOption(option);

        UtilTest test = new UtilTest();
        ListIterator iterator = Arrays.asList("-unknownOption", "value").listIterator();

        test.processOption("-unknownOption", iterator);
    }

    @Test
    public void testUpdateRequiredOptions() throws ParseException {
        Options options = new Options();
        Option option = new Option("option", true, "Option test");

        options.addOption(option);

        UtilTest test = new UtilTest();

        test.updateRequiredOptions(option);

        assertTrue(test.getRequiredOptions().isEmpty());
    }

    @Test
    public void testCheckRequiredOptions() throws MissingOptionException {
        Options options = new Options();
        Option option = new Option("option", true, "Option test");

        options.addOption(option);

        UtilTest test = new UtilTest();

        test.checkRequiredOptions();

        assertEquals(Arrays.asList(option), test.getRequiredOptions());
    }
}