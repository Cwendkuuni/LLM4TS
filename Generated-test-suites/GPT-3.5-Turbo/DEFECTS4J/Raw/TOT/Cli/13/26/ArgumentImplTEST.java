
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentImplTest {

    ArgumentImpl argument;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("name", "description", 1, 3, ' ', ' ', null, "--", null, 0);
    }

    @Test
    public void testGetPreferredName() {
        Assert.assertEquals("name", argument.getPreferredName());
    }

    @Test
    public void testGetInitialSeparator() {
        Assert.assertEquals(' ', argument.getInitialSeparator());
    }

    @Test
    public void testGetSubsequentSeparator() {
        Assert.assertEquals(' ', argument.getSubsequentSeparator());
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
    public void testCanProcess() {
        Assert.assertTrue(argument.canProcess(null, ""));
    }

    @Test
    public void testGetPrefixes() {
        Assert.assertEquals(0, argument.getPrefixes().size());
    }

    @Test
    public void testGetTriggers() {
        Assert.assertEquals(0, argument.getTriggers().size());
    }

    @Test
    public void testIsRequired() {
        Assert.assertTrue(argument.isRequired());
    }

    @Test
    public void testDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new OptionImpl(0, false);
        
        argument.defaultValues(commandLine, option);

        Assert.assertEquals(0, commandLine.getValues(option).size());
    }

    @Test
    public void testProcess() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> args = Arrays.asList("value1", "value2").listIterator();
        
        argument.process(commandLine, args);

        Assert.assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(argument));
    }

    @Test(expected = OptionException.class)
    public void testProcessWithTooFewValues() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> args = Arrays.asList("value").listIterator();
        
        argument.process(commandLine, args);
    }

    @Test(expected = OptionException.class)
    public void testProcessWithTooManyValues() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> args = Arrays.asList("value1", "value2", "value3", "value4").listIterator();
        
        argument.process(commandLine, args);
    }

    @Test
    public void testProcessValues() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("value1", "value2").listIterator();
        Option option = new OptionImpl(0, false);
        
        argument.processValues(commandLine, arguments, option);

        Assert.assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option));
    }

    @Test
    public void testProcessValuesWithConsumeRemaining() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("value1", "--", "value2").listIterator();
        Option option = new OptionImpl(0, false);
        
        argument.processValues(commandLine, arguments, option);

        Assert.assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option));
        Assert.assertFalse(arguments.hasNext());
    }

    @Test
    public void testProcessValuesWithSubsequentSplit() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("value1,value2,value3").listIterator();
        Option option = new OptionImpl(0, false);
        
        argument.processValues(commandLine, arguments, option);

        Assert.assertEquals(Arrays.asList("value1", "value2", "value3"), commandLine.getValues(option));
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithSubsequentSplitUnexpectedValue() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("value1,value2,value3,").listIterator();
        Option option = new OptionImpl(0, false);
        
        argument.processValues(commandLine, arguments, option);
    }

    @Test
    public void testValidate() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        
        argument.validate(commandLine);

        Assert.assertTrue(true);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithMissingValues() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        argument.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithUnexpectedValue() throws OptionException {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        commandLine.addValue(argument, "value2");
        commandLine.addValue(argument, "value3");
        
        argument.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        argument.appendUsage(buffer, null, null);

        Assert.assertEquals("name", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        Assert.assertEquals("description", argument.getDescription());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> expectedHelpLines = new ArrayList<>();
        HelpLine helpLine = new HelpLineImpl(argument, 0);
        expectedHelpLines.add(helpLine);

        Assert.assertEquals(expectedHelpLines, argument.helpLines(0, null, null));
    }

    @Test
    public void testGetMaximum() {
        Assert.assertEquals(3, argument.getMaximum());
    }

    @Test
    public void testGetMinimum() {
        Assert.assertEquals(1, argument.getMinimum());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithMinExceedsMax() {
        ArgumentImpl invalidArgument = new ArgumentImpl("name", "description", 3, 1, ' ', ' ', null, "--", null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithTooFewDefaults() {
        List<String> defaults = Arrays.asList("value1");
        
        ArgumentImpl invalidArgument = new ArgumentImpl("name", "description", 1, 3, ' ', ' ', null, "--", defaults, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithTooManyDefaults() {
        List<String> defaults = Arrays.asList("value1", "value2", "value3", "value4");
        
        ArgumentImpl invalidArgument = new ArgumentImpl("name", "description", 1, 3, ' ', ' ', null, "--", defaults, 0);
    }

    @Test
    public void testStripBoundaryQuotes() {
        String token = "\"value\"";
        String expectedToken = "value";

        Assert.assertEquals(expectedToken, argument.stripBoundaryQuotes(token));
    }
}
