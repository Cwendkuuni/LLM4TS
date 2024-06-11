import org.junit.Assert;
import org.junit.Test;

public class ArgumentImplTest {

    @Test
    public void testGetPreferredName() {
        ArgumentImpl argument = new ArgumentImpl("testName", "testDescription", 1, 1, null, null, null, null, null, 0);
        String preferredName = argument.getPreferredName();
        Assert.assertEquals("testName", preferredName);
    }

    @Test
    public void testProcessValues() {
        ArgumentImpl argument = new ArgumentImpl("testName", "testDescription", 1, 1, null, null, null, null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator arguments = Arrays.asList("value1", "value2").listIterator();
        Option option = new OptionImpl(0, false);
        try {
            argument.processValues(commandLine, arguments, option);
        } catch (OptionException e) {
            Assert.fail("Unexpected OptionException");
        }
        Assert.assertEquals("value1", commandLine.getValues(option).get(0));
        Assert.assertEquals("value2", commandLine.getValues(option).get(1));
    }

    @Test
    public void testCanProcess() {
        ArgumentImpl argument = new ArgumentImpl("testName", "testDescription", 1, 1, null, null, null, null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        boolean canProcess = argument.canProcess(commandLine, "testArg");
        Assert.assertTrue(canProcess);
    }

    @Test
    public void testGetInitialSeparator() {
        ArgumentImpl argument = new ArgumentImpl("testName", "testDescription", 1, 1, '-', null, null, null, null, 0);
        char initialSeparator = argument.getInitialSeparator();
        Assert.assertEquals('-', initialSeparator);
    }

    @Test
    public void testGetSubsequentSeparator() {
        ArgumentImpl argument = new ArgumentImpl("testName", "testDescription", 1, 1, null, ',', null, null, null, 0);
        char subsequentSeparator = argument.getSubsequentSeparator();
        Assert.assertEquals(',', subsequentSeparator);
    }

    @Test
    public void testGetConsumeRemaining() {
        ArgumentImpl argument = new ArgumentImpl("testName", "testDescription", 1, 1, null, null, null, "--", null, 0);
        String consumeRemaining = argument.getConsumeRemaining();
        Assert.assertEquals("--", consumeRemaining);
    }

    @Test
    public void testGetDefaultValues() {
        List defaultValues = Arrays.asList("default1", "default2");
        ArgumentImpl argument = new ArgumentImpl("testName", "testDescription", 1, 1, null, null, null, null, defaultValues, 0);
        List<String> argDefaultValues = argument.getDefaultValues();
        Assert.assertEquals("default1", argDefaultValues.get(0));
        Assert.assertEquals("default2", argDefaultValues.get(1));
    }

    @Test
    public void testGetValidator() {
        Validator validator = new ValidatorImpl();
        ArgumentImpl argument = new ArgumentImpl("testName", "testDescription", 1, 1, null, null, validator, null, null, 0);
        Validator argValidator = argument.getValidator();
        Assert.assertEquals(validator, argValidator);
    }

    @Test
    public void testValidate() {
        ArgumentImpl argument = new ArgumentImpl("testName", "testDescription", 1, 2, null, null, null, null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addValue(argument, "value1");
        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            Assert.fail("Unexpected OptionException");
        }
        commandLine.addValue(argument, "value2");
        try {
            argument.validate(commandLine);
        } catch (OptionException e) {
            Assert.fail("Unexpected OptionException");
        }
        commandLine.addValue(argument, "value3");
        try {
            argument.validate(commandLine);
            Assert.fail("Expected OptionException");
        } catch (OptionException e) {
            Assert.assertEquals("Unexpected value: value3", e.getMessage());
        }
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        ArgumentImpl argument = new ArgumentImpl("testName", "testDescription", 1, 2, null, null, null, null, null, 0);
        argument.appendUsage(buffer, Collections.emptySet(), null);
        Assert.assertEquals("testName", buffer.toString());
    }

    @Test
    public void testGetDescription() {
        ArgumentImpl argument = new ArgumentImpl("testName", "testDescription", 1, 1, null, null, null, null, null, 0);
        String description = argument.getDescription();
        Assert.assertEquals("testDescription", description);
    }

    @Test
    public void testHelpLines() {
        ArgumentImpl argument = new ArgumentImpl("testName", "testDescription", 1, 1, null, null, null, null, null, 0);
        List<HelpLine> helpLines = argument.helpLines(0, Collections.emptySet(), null);
        HelpLine helpLine = helpLines.get(0);
        Assert.assertEquals("testName", helpLine.getName());
        Assert.assertEquals("testDescription", helpLine.getDescription());
    }

    @Test
    public void testGetMaximum() {
        ArgumentImpl argument = new ArgumentImpl("testName", "testDescription", 1, 2, null, null, null, null, null, 0);
        int maximum = argument.getMaximum();
        Assert.assertEquals(2, maximum);
    }

    @Test
    public void testGetMinimum() {
        ArgumentImpl argument = new ArgumentImpl("testName", "testDescription", 1, 2, null, null, null, null, null, 0);
        int minimum = argument.getMinimum();
        Assert.assertEquals(1, minimum);
    }

    @Test
    public void testStripBoundaryQuotes() {
        ArgumentImpl argument = new ArgumentImpl("testName", "testDescription", 1, 1, null, null, null, null, null, 0);
        String strippedToken = argument.stripBoundaryQuotes("\"testValue\"");
        Assert.assertEquals("testValue", strippedToken);
    }

    @Test
    public void testIsRequired() {
        ArgumentImpl argument1 = new ArgumentImpl("testName", "testDescription", 1, 1, null, null, null, null, null, 0);
        Assert.assertTrue(argument1.isRequired());
        ArgumentImpl argument2 = new ArgumentImpl("testName", "testDescription", 0, 1, null, null, null, null, null, 0);
        Assert.assertFalse(argument2.isRequired());
    }

    @Test
    public void testDefaults() {
        ArgumentImpl argument = new ArgumentImpl("testName", "testDescription", 1, 1, null, null, null, null, Arrays.asList("default1", "default2"), 0);
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        argument.defaults(commandLine);
        Assert.assertEquals("default1", commandLine.getValues(argument).get(0));
        Assert.assertEquals("default2", commandLine.getValues(argument).get(1));
    }

}