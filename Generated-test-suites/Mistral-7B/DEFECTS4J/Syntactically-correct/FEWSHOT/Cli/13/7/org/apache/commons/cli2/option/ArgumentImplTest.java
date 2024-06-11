package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.validation.Validator;
import org.apache.commons.cli2.validation.InvalidArgumentException;
import org.apache.commons.cli2.util.HelpFormatter;

import java.util.*;

public class ArgumentImplTest {
    @Test
    public void testArgumentImpl() {
        Argument argument = new ArgumentImpl("arg", "", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default"), 0);

        assertEquals("arg", argument.getPreferredName());
        assertEquals("", argument.getDescription());
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
        assertEquals(DEFAULT_INITIAL_SEPARATOR, argument.getInitialSeparator());
        assertEquals(DEFAULT_SUBSEQUENT_SEPARATOR, argument.getSubsequentSeparator());
        assertEquals(false, argument.canProcess(null, ""));
        assertEquals(Collections.EMPTY_SET, argument.getPrefixes());
        assertNull(argument.getValidator());
        assertEquals(null, argument.getConsumeRemaining());
        assertEquals(Arrays.asList("default"), argument.getDefaultValues());

        Argument argumentWithDescription = new ArgumentImpl("arg", "Description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default"), 0);

        assertEquals("arg", argumentWithDescription.getPreferredName());
        assertEquals("Description", argumentWithDescription.getDescription());
        assertEquals(1, argumentWithDescription.getMinimum());
        assertEquals(1, argumentWithDescription.getMaximum());
        assertEquals(DEFAULT_INITIAL_SEPARATOR, argumentWithDescription.getInitialSeparator());
        assertEquals(DEFAULT_SUBSEQUENT_SEPARATOR, argumentWithDescription.getSubsequentSeparator());
        assertEquals(false, argumentWithDescription.canProcess(null, ""));
        assertEquals(Collections.EMPTY_SET, argumentWithDescription.getPrefixes());
        assertNull(argumentWithDescription.getValidator());
        assertEquals(null, argumentWithDescription.getConsumeRemaining());
        assertEquals(Arrays.asList("default"), argumentWithDescription.getDefaultValues());
    }

    @Test
    public void testProcessValues() {
        Argument argument = new ArgumentImpl("arg", "", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default"), 0);
        WriteableCommandLine commandLine = new DefaultCommandLine();
        List<String> args = new ArrayList<>();
        args.add("--arg");
        args.add("value1");
        args.add("value2");

        argument.process(commandLine, args.listIterator());

        assertEquals("value1", commandLine.getValues(argument).get(0));
        assertEquals("value2", commandLine.getValues(argument).get(1));
    }

    @Test
    public void testProcessValuesWithConsumeRemaining() {
        Argument argument = new ArgumentImpl("arg", "", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, "--", Arrays.asList("default"), 0);
        WriteableCommandLine commandLine = new DefaultCommandLine();
        List<String> args = new ArrayList<>();
        args.add("--arg");
        args.add("value1");
        args.add("value2");
        args.add("--");
        args.add("value3");
        args.add("value4");

        argument.process(commandLine, args.listIterator());

        assertEquals(3, commandLine.getValues(argument).size());
        assertEquals("value1", commandLine.getValues(argument).get(0));
        assertEquals("value2", commandLine.getValues(argument).get(1));
        assertEquals("value3 value4", commandLine.getValues(argument).get(2));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithTooManyValues() {
        Argument argument = new ArgumentImpl("arg", "", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default"), 0);
        WriteableCommandLine commandLine = new DefaultCommandLine();
        List<String> args = new ArrayList<>();
        args.add("--arg");
        args.add("value1");
        args.add("value2");
        args.add("value3");

        argument.process(commandLine, args.listIterator());

        fail("Should have thrown OptionException");
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithInvalidValue() {
        Argument argument = new ArgumentImpl("arg", "", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default"), 0);
        WriteableCommandLine commandLine = new DefaultCommandLine();
        List<String> args = new ArrayList<>();
        args.add("--arg");
        args.add("value1");
        args.add("value2");
        args.add("--invalid");

        argument.process(commandLine, args.listIterator());

        fail("Should have thrown OptionException");
    }

    @Test
    public void testValidate() {
        Argument argument = new ArgumentImpl("arg", "", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default"), 0);
        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.setValues(argument, Arrays.asList("default"));

        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyValues() {
        Argument argument = new ArgumentImpl("arg", "", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default"), 0);
        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.setValues(argument, Arrays.asList("default", "extra"));

        try {
            argument.validate(commandLine);
            fail("Should have thrown OptionException");
        } catch (OptionException e) {
            // pass
        }
    }

    @Test(expected = OptionException.class)
    public void testValidateWithInvalidValue() {
        Argument argument = new ArgumentImpl("arg", "", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default"), 0);
        WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.setValues(argument, Arrays.asList("default", "invalid"));

        try {
            argument.validate(commandLine);
            fail("Should have thrown OptionException");
        } catch (OptionException e) {
            // pass
        }
    }

    @Test
    public void testAppendUsage() {
        Argument argument = new ArgumentImpl("arg", "", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default"), 0);

        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);

        assertEquals("arg", buffer.toString());
    }

    @Test
    public void testAppendUsageWithMinimum() {
        Argument argument = new ArgumentImpl("arg", "", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default"), 0);

        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);

        assertEquals("arg", buffer.toString());
    }

    @Test
    public void testAppendUsageWithOptional() {
        Argument argument = new ArgumentImpl("arg", "", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default"), 0);

        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_OPTIONAL);
        argument.appendUsage(buffer, helpSettings, null);

        assertEquals("[arg]", buffer.toString());
    }

    @Test
    public void testAppendUsageWithNumbered() {
        Argument argument = new ArgumentImpl("arg", "", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default"), 0);

        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED);
        argument.appendUsage(buffer, helpSettings, null);

        assertEquals("1:arg", buffer.toString());
    }

    @Test
    public void testAppendUsageWithBrackets() {
        Argument argument = new ArgumentImpl("arg", "", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default"), 0);

        StringBuffer buffer = new StringBuffer();
        Set helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_BRACKETED);
        argument.appendUsage(buffer, helpSettings, null);

        assertEquals("<arg>", buffer.toString());
    }

    @Test
    public void testAppendUsageWithDescription() {
        Argument argument = new ArgumentImpl("arg", "Description", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default"), 0);

        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);

        assertEquals("arg (Description)", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        Argument argument = new ArgumentImpl("arg", "", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default"), 0);

        List<HelpLine> helpLines = argument.helpLines(1, null, null);

        HelpLine helpLine = helpLines.get(0);
        assertEquals("arg (required)", helpLine.getHeadText());
        assertEquals("", helpLine.getLongText());
    }

    @Test
    public void testHelpFormatter() {
        Argument argument = new ArgumentImpl("arg", "", 1, 1, DEFAULT_INITIAL_SEPARATOR, DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default"), 0);
        Set helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_NAME);
        helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_DESCRIPTION);
        helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_OPTIONAL);

        HelpFormatter formatter = new HelpFormatter();
        String helpText = formatter.printHelp("test", Arrays.asList("arg"), helpSettings, true);

        assertTrue(helpText.contains("arg (required)"));
    }
}