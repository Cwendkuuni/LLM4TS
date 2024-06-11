import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ArgumentImplTest {

    private Argument argument;
    private CommandLine commandLine;

    @Before
    public void setUp() {
        String name = "arg";
        String description = "An example argument";
        int minimum = 1;
        int maximum = 3;
        char initialSeparator = '-';
        char subsequentSeparator = '=';
        String consumeRemaining = "--values";
        List defaultValues = Arrays.asList("default1", "default2");

        argument = new ArgumentImpl(name, description, minimum, maximum, initialSeparator, subsequentSeparator, null, consumeRemaining, defaultValues, 0);
        commandLine = new CommandLine();
    }

    @Test
    public void testGetName() {
        assertEquals(argument.getName(), "arg");
    }

    @Test
    public void testGetDescription() {
        assertEquals(argument.getDescription(), "An example argument");
    }

    @Test
    public void testGetMinimum() {
        assertEquals(argument.getMinimum(), 1);
    }

    @Test
    public void testGetMaximum() {
        assertEquals(argument.getMaximum(), 3);
    }

    @Test
    public void testGetInitialSeparator() {
        assertEquals(argument.getInitialSeparator(), '-');
    }

    @Test
    public void testGetSubsequentSeparator() {
        assertEquals(argument.getSubsequentSeparator(), '=');
    }

    @Test
    public void testGetConsumeRemaining() {
        assertEquals(argument.getConsumeRemaining(), "--values");
    }

    @Test
    public void testGetDefaultValues() {
        List<String> defaultValuesList = argument.getDefaultValues();
        assertEquals(defaultValuesList.size(), 2);
        assertEquals(defaultValuesList.get(0), "default1");
        assertEquals(defaultValuesList.get(1), "default2");
    }

    @Test
    public void testProcessValues() {
        List<String> arguments = Arrays.asList("--arg", "--arg=value1", "--arg=value1=value2", "--arg=value1=value2=value3");

        argument.process(commandLine, arguments.listIterator(), new Option("", true));

        List<String> values = commandLine.getValues("arg");

        assertEquals(values.size(), 3);
        assertEquals(values.get(0), "value1");
        assertEquals(values.get(1), "value2");
        assertEquals(values.get(2), "value3");
    }

    @Test
    public void testCanProcess() {
        assertTrue(argument.canProcess(commandLine, "value1"));
        assertTrue(argument.canProcess(commandLine, "--arg"));
        assertTrue(argument.canProcess(commandLine, "--arg=value1"));
        assertTrue(argument.canProcess(commandLine, "--arg=value1=value2"));
        assertTrue(argument.canProcess(commandLine, "--arg=value1=value2=value3"));
    }

    @Test
    public void testGetPrefixes() {
        Set<String> prefixes = argument.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testProcess() {
        List<String> arguments = Arrays.asList("--arg", "--arg=value1", "--arg=value1=value2", "--arg=value1=value2=value3");

        argument.process(commandLine, arguments.listIterator(), new Option("", true));

        List<String> values = commandLine.getValues("arg");

        assertEquals(values.size(), 3);
        assertEquals(values.get(0), "value1");
        assertEquals(values.get(1), "value2");
        assertEquals(values.get(2), "value3");
    }

    @Test
    public void testValidate() {
        List<String> arguments = Arrays.asList("--arg", "--arg=value1", "--arg=value1=value2", "--arg=value1=value2=value3", "--arg=value1=value2=value4");

        argument.validate(commandLine);

        List<String> values = commandLine.getValues("arg");

        assertEquals(values.size(), 3);
        assertEquals(values.get(0), "value1");
        assertEquals(values.get(1), "value2");
        assertEquals(values.get(2), "value3");

        try {
            argument.validate(commandLine, new Option("", true));
            fail();
        } catch (OptionException e) {
            //expected
        }

        List<String> invalidArguments = Arrays.asList("--arg", "--arg=value1", "--arg=value1=value2", "--arg=value1=value2=value3", "--arg=value1=value2=value4", "--arg=value1=value2=value5");

        try {
            argument.validate(commandLine, new Option("", true), invalidArguments.subList(0, 3).toArray(new String[0]));
            fail();
        } catch (OptionException e) {
            //expected
        }

        try {
            argument.validate(commandLine, new Option("", true), invalidArguments.subList(0, 2).toArray(new String[0]));
            fail();
        } catch (OptionException e) {
            //expected
        }
    }

    @Test
    public void testAppendUsage() {
        List<String> helpSettings = Arrays.asList(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED, DisplaySetting.DISPLAY_ARGUMENT_BRACKETED);

        StringBuffer buffer = new StringBuffer();

        argument.appendUsage(buffer, helpSettings, null);

        String expectedUsage = "  --arg [<arg>...] ";
        assertEquals(expectedUsage, buffer.toString());

        helpSettings.add(DisplaySetting.DISPLAY_OPTIONAL);

        buffer.setLength(0);

        argument.appendUsage(buffer, helpSettings, null);

        expectedUsage = "[--arg <arg>...] ";
        assertEquals(expectedUsage, buffer.toString());

        helpSettings.remove(DisplaySetting.DISPLAY_OPTIONAL);

        buffer.setLength(0);

        argument.appendUsage(buffer, helpSettings, null);

        expectedUsage = "  --arg [<arg>...] ";
        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = argument.helpLines(1, Arrays.asList(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED, DisplaySetting.DISPLAY_ARGUMENT_BRACKETED), null);

        String expectedHelpLine = "  --arg [<arg>...]";
        assertEquals(expectedHelpLine, helpLines.get(0).getShortOpt());
        assertEquals("An example argument\n", helpLines.get(0).getLongOpt());
        assertEquals("", helpLines.get(0).getDescription());
        assertEquals("", helpLines.get(0).getValuesDescription());
        assertEquals("", helpLines.get(0).getArgName());
        assertEquals("", helpLines.get(0).getArgDescription());
        assertEquals("", helpLines.get(0).getArgValues());
        assertEquals("", helpLines.get(0).getArgMinimum());
        assertEquals("", helpLines.get(0).getArgMaximum());
        assertEquals("", helpLines.get(0).getArgInitialSeparator());
        assertEquals("", helpLines.get(0).getArgSubsequentSeparator());
        assertEquals("", helpLines.get(0).getArgConsumeRemaining());
        assertEquals("", helpLines.get(0).getArgDefaults());
        assertEquals("", helpLines.get(0).getArgValidatorDescription());
    }

    @Test
    public void testIsRequired() {
        assertTrue(argument.isRequired());
    }

    @Test
    public void testDefaults() {
        argument.defaults(commandLine);

        List<String> values = commandLine.getValues("arg");

        assertEquals(values.size(), 2);
        assertEquals(values.get(0), "default1");
        assertEquals(values.get(1), "default2");
    }
}