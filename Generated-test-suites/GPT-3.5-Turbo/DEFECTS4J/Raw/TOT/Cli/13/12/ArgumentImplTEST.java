import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ArgumentImplTest {

    @Test
    public void testGetPreferredName() {
        ArgumentImpl argument = new ArgumentImpl("name", "description", 1, 1, '\0', '\0', null,
                null, null, 0);
        String preferredName = argument.getPreferredName();
        Assert.assertEquals("name", preferredName);
    }

    @Test
    public void testGetInitialSeparator() {
        ArgumentImpl argument = new ArgumentImpl(null, null, 1, 1, '@', '\0', null,
                null, null, 0);
        char initialSeparator = argument.getInitialSeparator();
        Assert.assertEquals('@', initialSeparator);
    }

    @Test
    public void testGetSubsequentSeparator() {
        ArgumentImpl argument = new ArgumentImpl(null, null, 1, 1, '\0', '|', null,
                null, null, 0);
        char subsequentSeparator = argument.getSubsequentSeparator();
        Assert.assertEquals('|', subsequentSeparator);
    }

    @Test
    public void testGetConsumeRemaining() {
        ArgumentImpl argument = new ArgumentImpl(null, null, 1, 1, '\0', '\0', null,
                "--", null, 0);
        String consumeRemaining = argument.getConsumeRemaining();
        Assert.assertEquals("--", consumeRemaining);
    }

    @Test
    public void testGetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        ArgumentImpl argument = new ArgumentImpl(null, null, 1, 1, '\0', '\0', null,
                null, defaultValues, 0);
        List<String> argumentDefaultValues = argument.getDefaultValues();
        Assert.assertEquals(defaultValues, argumentDefaultValues);
    }

    @Test
    public void testIsRequired() {
        ArgumentImpl requiredArgument = new ArgumentImpl(null, null, 1, 1, '\0', '\0', null,
                null, null, 0);
        Assert.assertTrue(requiredArgument.isRequired());

        ArgumentImpl nonRequiredArgument = new ArgumentImpl(null, null, 0, 1, '\0', '\0', null,
                null, null, 0);
        Assert.assertFalse(nonRequiredArgument.isRequired());
    }

    @Test
    public void testProcessValues() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl(null, null, 1, 3, '\0', ',', null,
                null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = new ArrayList<String>().listIterator();
        arguments.add("value1,value2,value3");

        argument.processValues(commandLine, arguments, argument);

        List<String> actualValues = commandLine.getValues(argument);
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        expectedValues.add("value3");

        Assert.assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testCanProcess() {
        ArgumentImpl argument = new ArgumentImpl(null, null, 1, 1, '\0', '\0', null,
                null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        boolean canProcess = argument.canProcess(commandLine, "arg");
        Assert.assertTrue(canProcess);
    }

    @Test
    public void testGetPrefixes() {
        ArgumentImpl argument = new ArgumentImpl(null, null, 1, 1, '\0', '\0', null,
                null, null, 0);
        Set<Character> prefixes = argument.getPrefixes();
        Assert.assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testValidate() throws OptionException {
        ArgumentImpl argument = new ArgumentImpl(null, null, 2, 3, '\0', '\0', null,
                null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        commandLine.addValue(argument, "value3");

        argument.validate(commandLine, argument);
    }

    @Test
    public void testAppendUsage() {
        ArgumentImpl argument = new ArgumentImpl("arg", null, 1, 3, '\0', '\0', null,
                null, null, 0);
        StringBuffer buffer = new StringBuffer();
        Set<DisplaySetting> helpSettings = new HashSet<>();
        helpSettings.add(DisplaySetting.DISPLAY_OPTIONAL);
        Comparator comparator = null;

        argument.appendUsage(buffer, helpSettings, comparator);

        String expectedUsage = "arg arg1 [arg2] [arg3]";
        Assert.assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testGetDescription() {
        ArgumentImpl argument = new ArgumentImpl(null, "description", 1, 1, '\0', '\0', null,
                null, null, 0);
        String description = argument.getDescription();
        Assert.assertEquals("description", description);
    }

    @Test
    public void testHelpLines() {
        ArgumentImpl argument = new ArgumentImpl(null, null, 1, 1, '\0', '\0', null,
                null, null, 0);
        int depth = 0;
        Set<DisplaySetting> helpSettings = new HashSet<>();
        Comparator comparator = null;

        List<HelpLine> helpLines = argument.helpLines(depth, helpSettings, comparator);

        Assert.assertEquals(1, helpLines.size());
        HelpLine helpLine = helpLines.get(0);
        Assert.assertEquals(argument, helpLine.getOption());
        Assert.assertEquals(depth, helpLine.getDepth());
    }

    @Test
    public void testGetMaximum() {
        ArgumentImpl argument = new ArgumentImpl(null, null, 1, 5, '\0', '\0', null,
                null, null, 0);
        int maximum = argument.getMaximum();
        Assert.assertEquals(5, maximum);
    }

    @Test
    public void testGetMinimum() {
        ArgumentImpl argument = new ArgumentImpl(null, null, 2, 4, '\0', '\0', null,
                null, null, 0);
        int minimum = argument.getMinimum();
        Assert.assertEquals(2, minimum);
    }

    @Test
    public void testDefaults() {
        ArgumentImpl argument = new ArgumentImpl(null, null, 1, 1, '\0', '\0', null,
                null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();

        argument.defaults(commandLine);

        List<String> actualValues = commandLine.getValues(argument);
        List<String> expectedValues = new ArrayList<>();
        Assert.assertEquals(expectedValues, actualValues);
    }
}