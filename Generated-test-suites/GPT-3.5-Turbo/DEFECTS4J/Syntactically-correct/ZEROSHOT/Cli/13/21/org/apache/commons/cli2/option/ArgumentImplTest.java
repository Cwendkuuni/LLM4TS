import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ArgumentImplTest {

    private ArgumentImpl argument;

    @Before
    public void setup() {
        argument = new ArgumentImpl(
                "arg",
                "description",
                1,
                Integer.MAX_VALUE,
                ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR,
                null,
                ArgumentImpl.DEFAULT_CONSUME_REMAINING,
                null,
                0
        );
    }

    @Test
    public void testGetPreferredName() {
        String expected = "arg";
        String actual = argument.getPreferredName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetInitialSeparator() {
        char expected = ArgumentImpl.DEFAULT_INITIAL_SEPARATOR;
        char actual = argument.getInitialSeparator();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetSubsequentSeparator() {
        char expected = ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR;
        char actual = argument.getSubsequentSeparator();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetConsumeRemaining() {
        String expected = ArgumentImpl.DEFAULT_CONSUME_REMAINING;
        String actual = argument.getConsumeRemaining();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultValues() {
        List<String> expected = null;
        List<String> actual = argument.getDefaultValues();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetValidator() {
        Validator expected = null;
        Validator actual = argument.getValidator();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetMaximum() {
        int expected = Integer.MAX_VALUE;
        int actual = argument.getMaximum();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetMinimum() {
        int expected = 1;
        int actual = argument.getMinimum();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetDescription() {
        String expected = "description";
        String actual = argument.getDescription();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsRequired() {
        boolean expected = true;
        boolean actual = argument.isRequired();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testProcessValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = new ArrayList<String>().listIterator();
        Option option = new OptionImpl(0, false);

        List<String> values = new ArrayList<String>();
        values.add("value1");
        values.add("value2");
        arguments.add("value1");
        arguments.add("value2");

        argument.processValues(commandLine, arguments, option);

        List<String> expectedValues = new ArrayList<String>();
        expectedValues.add("value1");
        expectedValues.add("value2");

        List<String> actualValues = commandLine.getValues(option);

        Assert.assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String arg = "arg";
        boolean expected = true;
        boolean actual = argument.canProcess(commandLine, arg);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPrefixes() {
        int expected = 0;
        int actual = argument.getPrefixes().size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> args = new ArrayList<String>().listIterator();

        argument.process(commandLine, args);

        List<String> expectedValues = new ArrayList<String>();
        List<String> actualValues = commandLine.getValues(argument);

        Assert.assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl(0, false);

        List<String> values = new ArrayList<String>();
        values.add("value1");
        values.add("value2");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        argument.validate(commandLine, option);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMissingValues() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl(0, false);

        argument.validate(commandLine, option);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithUnexpectedValue() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl(0, false);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addValue(option, "value3");

        argument.validate(commandLine, option);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<DisplaySetting>();
        helpSettings.add(DisplaySetting.DISPLAY_OPTIONAL);
        Comparator<HelpLine> comp = null;

        argument.appendUsage(buffer, helpSettings, comp);

        String expected = "arg";
        String actual = buffer.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHelpLines() {
        int depth = 0;
        Set<DisplaySetting> helpSettings = new HashSet<DisplaySetting>();
        Comparator<HelpLine> comp = null;

        List<HelpLine> expectedLines = new ArrayList<HelpLine>();
        HelpLineImpl helpLine = new HelpLineImpl(argument, depth);
        expectedLines.add(helpLine);

        List<HelpLine> actualLines = argument.helpLines(depth, helpSettings, comp);

        Assert.assertEquals(expectedLines, actualLines);
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        argument.defaults(commandLine);

        List<String> expectedValues = new ArrayList<String>();
        List<String> actualValues = commandLine.getValues(argument);

        Assert.assertEquals(expectedValues, actualValues);
    }
}