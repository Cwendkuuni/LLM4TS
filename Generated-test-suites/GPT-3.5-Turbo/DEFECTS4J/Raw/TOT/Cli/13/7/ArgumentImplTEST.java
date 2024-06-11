import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ArgumentImplTest {

    private ArgumentImpl argument;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("test", "Test Argument", 0, 3, '\0', '\0', null, "--", null, 0);
    }

    @Test
    public void testGetPreferredName() {
        Assert.assertEquals("test", argument.getPreferredName());
    }

    @Test
    public void testGetInitialSeparator() {
        Assert.assertEquals('\0', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        Assert.assertEquals('\0', argument.getSubsequentSeparator());
    }

    @Test
    public void testGetConsumeRemaining() {
        Assert.assertEquals("--", argument.getConsumeRemaining());
    }

    @Test
    public void testGetDefaultValues() {
        Assert.assertNull(argument.getDefaultValues());
    }

    @Test
    public void testGetValidator() {
        Assert.assertNull(argument.getValidator());
    }

    @Test
    public void testGetMinimum() {
        Assert.assertEquals(0, argument.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        Assert.assertEquals(3, argument.getMaximum());
    }

    @Test
    public void testIsRequired() {
        Assert.assertFalse(argument.isRequired());
    }

    @Test
    public void testProcessValues_withNoValues() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = new ArrayList<String>().listIterator();
        OptionImpl option = new OptionImpl(0, false);

        argument.processValues(commandLine, arguments, option);

        Assert.assertTrue(commandLine.getValues(option).isEmpty());
    }

    @Test
    public void testProcessValues_withValuesUnderMinimum() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = new ArrayList<String>().listIterator();
        OptionImpl option = new OptionImpl(0, false);

        arguments.add("value1");

        argument.processValues(commandLine, arguments, option);

        Assert.assertTrue(commandLine.getValues(option).isEmpty());
    }

    @Test
    public void testProcessValues_withValuesAtMinimum() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = new ArrayList<String>().listIterator();
        OptionImpl option = new OptionImpl(0, false);

        arguments.add("value1");

        argument.processValues(commandLine, arguments, option);

        Assert.assertEquals("value1", commandLine.getValues(option).get(0));
    }

    @Test
    public void testProcessValues_withValuesAboveMinimumBelowMaximum() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = new ArrayList<String>().listIterator();
        OptionImpl option = new OptionImpl(0, false);

        arguments.add("value1");
        arguments.add("value2");

        argument.processValues(commandLine, arguments, option);

        Assert.assertEquals("value1", commandLine.getValues(option).get(0));
        Assert.assertEquals("value2", commandLine.getValues(option).get(1));
    }

    @Test
    public void testProcessValues_withValuesAtMaximum() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = new ArrayList<String>().listIterator();
        OptionImpl option = new OptionImpl(0, false);

        arguments.add("value1");
        arguments.add("value2");
        arguments.add("value3");

        argument.processValues(commandLine, arguments, option);

        Assert.assertEquals("value1", commandLine.getValues(option).get(0));
        Assert.assertEquals("value2", commandLine.getValues(option).get(1));
        Assert.assertEquals("value3", commandLine.getValues(option).get(2));
    }

    @Test(expected = OptionException.class)
    public void testProcessValues_withValuesAboveMaximum() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = new ArrayList<String>().listIterator();
        OptionImpl option = new OptionImpl(0, false);

        arguments.add("value1");
        arguments.add("value2");
        arguments.add("value3");
        arguments.add("value4");

        argument.processValues(commandLine, arguments, option);
    }

    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String arg = "test";

        Assert.assertTrue(argument.canProcess(commandLine, arg));
    }

    @Test
    public void testGetPrefixes() {
        Assert.assertEquals(0, argument.getPrefixes().size());
    }

    @Test
    public void testValidate_withValidValues() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        OptionImpl option = new OptionImpl(0, false);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        argument.validate(commandLine, option);
    }

    @Test(expected = OptionException.class)
    public void testValidate_withInvalidValues() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        OptionImpl option = new OptionImpl(0, false);

        commandLine.addValue(option, "value1");

        argument.validate(commandLine, option);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);
        Assert.assertEquals("test", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        Assert.assertEquals("Test Argument", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = argument.helpLines(0, null, null);
        Assert.assertEquals(1, helpLines.size());
        Assert.assertEquals("test - Test Argument", helpLines.get(0).toString());
    }

    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        OptionImpl option = new OptionImpl(0, false);

        argument.defaults(commandLine);

        Assert.assertTrue(commandLine.getValues(option).isEmpty());
    }

    @Test
    public void testDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        OptionImpl option = new OptionImpl(0, false);

        argument.defaultValues(commandLine, option);

        Assert.assertNull(commandLine.getDefaultValues(option));
    }

    @After
    public void tearDown() {
        argument = null;
    }
}